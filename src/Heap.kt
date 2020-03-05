class Heap <T : Comparable<T>>/*(private var heap: Array<T?>, private var size: Int)*/{

    private var heap: Array<T?>
    private var size = 0

    /**
     * Pretty suprised that I need multiple constructors for handling this
     */
    constructor(heap: Array<T>){
        this.heap = heap as Array<T?>
        this.size = heap.size - 1
    }

    /**
     * Ideally this should check for null elements.
     */
    constructor(heap: Array<T?>, size : Int){
        this.heap = heap
        this.size = size
    }

    /**
     * heapifies all subtrees, excluding leafs
     */
    init {
        for(pos in ((size - 1)/ 2) downTo 0) heapify(pos)
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
        return if (pos2 > size || pos2 == null) false
               else                             heap[pos1]?.compareTo(heap[pos2]!!)!! < 0
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
        val e = heap[0]
        swap(0, size -1)
        heap[size - 1] = null
        size--
        sink(0)
        return e
    }

    /**
     * increases the amount of elements the array allocates if it is capped
     */
    private fun increaseSize(){
        if(size == heap.size - 1){
            //okay this is garbage
            var enlargedHeap = arrayOfNulls<Comparable<*>>(heap.size * 2) as Array<T?>
            System.arraycopy(heap, 0, enlargedHeap, 0, size)
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
    private fun sink(pos: Int){
        //basecase
        if(isHeap(pos)) return
        if(rightChild(pos) > size){
            swap(pos, leftChild(pos))
        } else {
            if(lessThan(leftChild(pos), rightChild(pos))){
                swap(pos, rightChild(pos))
                sink(rightChild(pos))
            } else {
                swap(pos, leftChild(pos))
                sink(leftChild(pos))
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
            /*var arr = arrayOfNulls<String>(9)
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
            heap.print()*/
        }
    }
}