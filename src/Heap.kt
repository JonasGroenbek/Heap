class Heap <T : Comparable<T>>(data: Array<T>, private var size: Int) {

    //had a lot of problems when using T?
    private var heap = Array<Any?>(size){ data[it] }

    /**
     * heapifies all subtrees, excluding leafs
     */
    init {
        if (size > data.size) error("Initial array is too small for initial size.")
        for(pos in ((size - 1)/ 2) downTo 0) heapify(pos)
    }

    @Suppress("UNCHECKED_CAST")
    private operator fun get(index: Int) = heap[index] as T

    fun sort(partition: Int) : Array<T> {
        swap(0, partition)
        sortElement(0, size)
        val sortedHeap = heap.filterNotNull() as Array<T>
        heapify(0)
        return sortedHeap
    }

    private fun sortElement(pos: Int, partition: Int){

    }

    //only for testing
    private fun print() {
        for(pos in 0..size){
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
        if(isHeap(pos)) return

        val lc = leftChild(pos)
        val rc = rightChild(pos)

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
        return if (pos2 > size || get(pos2) == null) false
               else                         get(pos1).compareTo(get(pos2)) < 0
    }

    /**
     * probably can be made cleaner
     */
    private fun isHeap(pos: Int): Boolean {
        if(isLeaf(pos) || size == 0) return true;
        println(pos)
        return if(rightChild(pos) > size)   (lessThan(leftChild(pos), pos))
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
        val e = get(0)
        swap(0, size -1)
        heap[size - 1] = null
        size--
        sink(0, size)
        return e
    }


    private fun Array<Any?>.allocate(): Int = if (size == 0)  1 else size * 2

    /**
     * increases the amount of elements the array allocates if it is capped
     */
    private fun increaseSize(){
        if(size == heap.size || size == 0){
            var enlargedHeap = arrayOfNulls<Any>(heap.allocate())
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
        if(isHeap(pos)) return
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
            var arr = arrayOf("asd","asd","345")
            var heap = Heap(arr, 0)
            heap.insert("hey")
            heap.insert("asd")
            heap.insert("ass")
            heap.insert("x")
            heap.insert("z")
            heap.insert("a")
            heap.insert("b")
            heap.insert("xxx")
            heap.insert("asdasd")
            heap.insert("xxxxx")
            heap.print()
        }
    }
}