@Suppress("UNCHECKED_CAST")
class Heap <T : Comparable<T>>(data: Array<T>, private var size: Int) {

    private var heap : Array<T?>

    init {
        if (size > data.size) error("Initial array is too small for initial size.")
        this.heap = data.sliceArray(0..size) as Array<T?>
        for(pos in ((size - 1)/ 2) downTo 0) heapify(pos)
    }

    /**
     * performing a heapsort
     * @return a full Array<T> without null elements
     */
    fun sort() : Array<T> {
        for(partition in size - 1 downTo 0) {
            swap(0, partition)
            sink(0, partition -1)
        }                            
        val sortedHeap = heap.sliceArray(0 until size)
        //since heap sort is inplace and reverses the order, the heap structure is broken
        heapify(0)
        return sortedHeap as Array<T>
    }

    /**
     * NOTE - could have replaced the heapify calls with swim/sink had they not been recursive.
     * That is probably a better solution since heapify has to be wrapped in a loop.
     * @param pos the position of the root of which subtree to heapify
     */
    private fun heapify(pos: Int){
        //basecase
        if(shouldSustain(pos, size - 1)) return

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

    /**
     * This is used as a check whether a sink/heapify should be sustained
     * NOTE - could probably be made a lot cleaner.
     */
    private fun shouldSustain(pos: Int, partition: Int): Boolean {
        if(isLeaf(pos) || size == 0 || leftChild(pos) > partition) return true
        return if(rightChild(pos) > partition)   (lessThan(leftChild(pos), pos))
        else   (lessThan(leftChild(pos), pos) && (lessThan(rightChild(pos), pos)))
    }

    /**
     * Recursively swim an element up the heap to maintain a heapifified state
     */
    private fun swim(pos: Int){
        //basecase
        if(lessThan(parent(pos), pos)){
            swap(parent(pos), pos)
            swim(parent(pos))
        }
    }

    /**
     * Recursively sink an element down the tree, prioritizing swapping the larger child
     * @param pos the position of the element to sink
     * @param partition defines the position to scope the sink to. For full sink size - 1
     */
    private fun sink(pos: Int, partition: Int){
        //basecase
        if(shouldSustain(pos, partition)) return

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

    //Magic
    private fun swap(pos1: Int, pos2: Int) {
        heap[pos1] = heap[pos2].also { heap[pos2] = heap[pos1] }
    }

    /**
     * NOTE - ArrayIndexOutOfBounds probably should not be handled here, but for now it makes sense
     * @param pos1 position of element that checks if it is less than the other return true
     * @param pos2 position of element that checks if it is greater than the other return true
     */
    private fun lessThan(pos1: Int, pos2: Int) : Boolean {
        return if (pos2 > size || heap[pos2] == null) false
        else                                   heap[pos1]?.compareTo(heap[pos2]!!)!! < 0
    }

    fun insert(e: T){
        increaseSize()
        heap[size] = e
        swim(size - 1)
        size++
    }

    private fun allocate(): Int = if (size == 0)  1 else size * 2

    /**
     * increases the amount of elements the array allocates if it is currently capped
     */
    private fun increaseSize(){
        if(size == heap.size || size == 0){
            heap = heap.copyOf(allocate())
        }
    }

    /**
     * Extracting and returning the top element from the heap, setting empty value to null
     */
    fun extract() : T {
        val e = heap[0]
        swap(0, --size)
        heap[size] = null
        sink(0, size)
        return e as T
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

    //only for testing
    private fun print() {
        if(size == 0) return
        for(pos in 0 until size){
            println(heap[pos])
        }
    }
}

fun main() {
    val arr = arrayOf(10,34,23,5,23,4567,34,23423,764)
    val heap = Heap(arr, 6)

    heap.insert(278)
    heap.extract()
    heap.insert(12)

    val sorted : Array<Int> = heap.sort()

    for(el in 0 until sorted.size){
        println(sorted[el])
    }
}