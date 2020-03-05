class Heap <T : Comparable<T>>(private var data: Array<T>, private var size: Int) {

    //had a lot of problems when using T?
    private var heap : Array<T?>
    /**
     * heapifies all subtrees, excluding leafs
     */
    init {
        if (size > data.size) error("Initial array is too small for initial size.")
        this.heap = data.sliceArray(0..size) as Array<T?>
        this.size = size
        for(pos in ((size - 1)/ 2) downTo 0) heapify(pos)
        print()
    }

    fun sort() : Array<T> {
        for(partition in size - 1 downTo 0) {
            swap(0, partition)
            sink(0, partition -1)
        }                            
        val sortedHeap = heap.sliceArray(0..size)
        heapify(0)
        print()
        //return sortedHeap as Array<T>
        return sortedHeap as Array<T>
    }

    //only for testing
    private fun print() {
        if(size == 0) return
        for(pos in 0 until size){
            println(heap[pos])
        }
    }

    /**
     * heapifies an unheapified array of <T : Comparable<T>>
     * @param pos the position of the root of which subtree to heapify
     * NOTE - could have replaced the heapify calls with swim/sink if they weren't recursive and made the heapify recursive
     */
    private fun heapify(pos: Int){
        //basecase
        if(isHeap(pos, size - 1)) return

        val lc = leftChild(pos)
        val rc = rightChild(pos)

        if(rc > size - 1){
            swap(lc, pos)
            return
        }
         if(lessThan(lc, rc)){
             swap(rc, pos)
             heapify(rc)
         } else {
             swap(lc, pos)
             heapify(lc)
         }
    }

    //Magic
    private fun swap(pos1: Int, pos2: Int) {
        heap[pos1] = heap[pos2].also { heap[pos2] = heap[pos1] }
    }

    /**
     * ArrayIndexOutOfBounds probably should not be handled here, but for now it makes sense
     */
    fun lessThan(pos1: Int, pos2: Int) : Boolean {
        return if (pos2 > size || heap[pos2] == null) false
               else                         heap[pos1]?.compareTo(heap[pos2]!!)!! < 0
    }

    /**
     * probably can be made cleaner
     */
    private fun isHeap(pos: Int, partition: Int): Boolean {
        if(isLeaf(pos) || size == 0 || leftChild(pos) > partition) return true
        return if(rightChild(pos) > partition)   (lessThan(leftChild(pos), pos))
               else                         (lessThan(leftChild(pos), pos) && (lessThan(rightChild(pos), pos)))
    }

    /**
     * Increasing the size of the array if possible
     */
    private fun insert(e: T){
        increaseSize()
        heap[size] = e
        swim(size)
        size++
    }

    /**
     * this will leave a dead element in the array, which is what the size partition is used for
     */
    private fun extract() : T? {
        val e = heap[0]
        swap(0, size -1)
        heap[size - 1] = null
        size--
        sink(0, size)
        return e
    }


    private fun Array<T?>.allocate(): Int = if (size == 0)  1 else size * 2

    /**
     * increases the amount of elements the array allocates if it is capped
     */
    private fun increaseSize(){
        if(size == heap.size || size == 0){
            val enlargedHeap = arrayOfNulls<Comparable<*>>(heap.allocate()) as Array<T?>
            System.arraycopy(heap, 0, enlargedHeap, 0, heap.size)
            heap = enlargedHeap
        }
    }

    /**
     * If the heap is not heapified before swimming, neither will it be after.
     */
    private fun swim(pos: Int){
        //basecase
        if(lessThan(parent(pos), pos)){
            swap(parent(pos), pos)
            swim(parent(pos))
        }
    }

    /**
     * Recursive function that sinks a element down the tree, prioritizing swapping the biggest of children
     */
    private fun sink(pos: Int, partition: Int){
        //basecase
        if(isHeap(pos, partition)) return
        if(rightChild(pos) > partition){
            swap(pos, leftChild(pos))
        } else {
            if(lessThan(leftChild(pos), rightChild(pos))){
                swap(pos, rightChild(pos))
                sink(rightChild(pos), partition)
            } else {
                swap(pos, leftChild(pos))
                sink(leftChild(pos), partition)
            }

        }
    }

    private fun rightChild(pos: Int) : Int{
        return (pos + 1) * 2
    }

    private fun leftChild(pos: Int) : Int{
        return (pos + 1) * 2 - 1
    }

    private fun parent(pos: Int) : Int {
        return (pos - 1) / 2
    }

    private fun isLeaf(pos: Int) : Boolean{
        return (size - 1) / 2 < pos
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            var arr = arrayOf(10,34,23,5,23,4567,34,23423,764)
            var heap = Heap(arr, 6)
            heap.insert(1)
            heap.insert(3)
            heap.insert(4)
            heap.insert(6)
            heap.insert(5)
            heap.insert(3)
            heap.insert(278)
            heap.insert(32)
            heap.insert(43)
            heap.insert(23)
            heap.sort()
        }
    }
}