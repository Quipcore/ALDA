package heap;

// Klassen i denna fil måste döpas om till DHeap för att testerna ska fungera.

//BinaryHeap class
//
//CONSTRUCTION: with optional capacity (that defaults to 100)
//            or an array containing initial items
//
//******************PUBLIC OPERATIONS*********************
//void insert( x )       --> Insert x
//Comparable deleteMin( )--> Return and remove smallest item
//Comparable findMin( )  --> Return smallest item
//boolean isEmpty( )     --> Return true if empty; else false
//void makeEmpty( )      --> Remove all items
//******************ERRORS********************************
//Throws UnderflowException as appropriate

/**
 * Implements a binary heap.
 * Note that all "matching" is based on the compareTo method.
 *
 * @author Mark Allen Weiss
 */
public class DHeap<T extends Comparable<? super T>> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final int DEFAULT_D_VALUE = 2;
    private int dValue;
    private int currentSize;      // Number of elements in heap
    private T[] array; // The heap array

    /**
     * Construct the binary heap.12
     */
    public DHeap() {
        this(DEFAULT_D_VALUE);
    }


    /**
     * Construct the binary heap.
     *
     * @param dValue the capacity of the binary heap.
     */
    public DHeap(int dValue) {
        if(dValue < 2){
            throw new IllegalArgumentException(dValue + " is an invalid d-value for the heap");
        }
        this.dValue = dValue;
        currentSize = 0;
        array = (T[]) new Comparable[DEFAULT_CAPACITY];
    }

    /**
     * Construct the binary heap given an array of items.
     */
    @Deprecated
    public DHeap(T[] items) {
        array = (T[]) new Comparable[(items.length + dValue) * 11 / 10];

        int i = 1;
        for (T item : items) {
            array[i++] = item;
        }
        buildHeap();
    }

    // Test program
    public static void main(String[] args) {
        int numItems = 10000;
        DHeap<Integer> h = new DHeap<>();

        for (int i = 37; i != 0; i = (i + 37) % numItems) {
            h.insert(i);
        }
        for (int i = 1; i < numItems; i++) {
            if (h.deleteMin() != i) {
                System.out.println("Oops! " + i);
            }
        }
    }

    /**
     * Insert into the priority queue, maintaining heap order.
     * Duplicates are allowed.
     *
     * @param x the item to insert.
     */
    public void insert(T x) {
        if (currentSize == array.length - 1) {
            enlargeArray(array.length * dValue + 1);
        }

        // Percolate up
//        int hole = ++currentSize;
//        for (array[0] = x; x.compareTo(array[parentIndex(hole)]) < 0; hole = parentIndex(hole)) {
//            array[hole] = array[parentIndex(hole)];
//        }
//        array[hole] = x;
        currentSize++;
        int holePos = currentSize;

        array[0] = x;

        while (x.compareTo(array[parentIndex(holePos)]) < 0){
            array[holePos] = array[parentIndex(holePos)];
            holePos = parentIndex(holePos);
        }
        array[holePos] = x;

    }

    private void enlargeArray(int newSize) {
        T[] old = array;
        array = (T[]) new Comparable[newSize];
        for (int i = 0; i < old.length; i++) {
            array[i] = old[i];
        }
    }

    /**
     * Find the smallest item in the priority queue.
     *
     * @return the smallest item, or throw an UnderflowException if empty.
     */
    public T findMin() {
        if (isEmpty()) {
            throw new UnderflowException();
        }
        return array[1];
    }

    /**
     * Remove the smallest item from the priority queue.
     *
     * @return the smallest item, or throw an UnderflowException if empty.
     */
    public T deleteMin() {
        if (isEmpty()) {
            throw new UnderflowException();
        }

        T minItem = findMin();
        array[1] = array[currentSize--];
        percolateDown(1);

        return minItem;
    }

    /**
     * Establish heap order property from an arbitrary
     * arrangement of items. Runs in linear time.
     */
    @Deprecated
    private void buildHeap() {
        for (int i = currentSize / dValue; i > 0; i--) {
            percolateDown(i);
        }
    }

    /**
     * Test if the priority queue is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * Make the priority queue logically empty.
     */
    public void makeEmpty() {
        currentSize = 0;
    }

    /**
     * Internal method to percolate down in the heap.
     *
     * @param hole the index at which the percolate begins.
     */
    private void percolateDown(int hole) {
        int child;
        T tmp = array[hole];

        for (; hole * dValue <= currentSize; hole = child) {
            child = hole * dValue;
            if (child != currentSize && array[child + 1].compareTo(array[child]) < 0) {
                child++;
            }
            if (array[child].compareTo(tmp) < 0) {
                array[hole] = array[child];
            }
            else {
                break;
            }
        }
        array[hole] = tmp;
    }


    public int firstChildIndex(int parentIndex) {
        if(parentIndex <= 0){
            throw new IllegalArgumentException("Index to low");
        }
        return dValue * (parentIndex -1) + 2;
    }

    public int parentIndex(int childIndex) {
        if(childIndex <= 1){ //First pos is root and has no parents
            throw new IllegalArgumentException("Index to low");
        }

        return (childIndex - 2)/dValue +1;
    }

    public int size() {
        return currentSize;
    }

    public T get(int index) {
        return array[index];
    }
}
