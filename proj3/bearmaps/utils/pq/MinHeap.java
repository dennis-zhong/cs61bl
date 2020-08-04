package bearmaps.utils.pq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

/* A MinHeap class of Comparable elements backed by an ArrayList. */
public class MinHeap<E extends Comparable<E>> {

    /* An ArrayList that stores the elements in this MinHeap. */
    private ArrayList<E> contents;
    private int size;
    private HashMap<E, Integer> indexMap;

    /* Initializes an empty MinHeap. */
    public MinHeap() {
        contents = new ArrayList<E>();
        contents.add(null);
        indexMap = new HashMap<>();
    }

    /* Returns the element at index INDEX, and null if it is out of bounds. */
    private E getElement(int index) {
        if (index >= contents.size() || index<0) {
            return null;
        } else {
            return contents.get(index);
        }
    }

    /* Sets the element at index INDEX to ELEMENT. If the ArrayList is not big
       enough, add elements until it is the right size. */
    private void setElement(int index, E element) {
        while (index >= contents.size()) {
            contents.add(null);
        }
        contents.set(index, element);
        indexMap.put(element, index);
    }

    /* Swaps the elements at the two indices. */
    private void swap(int index1, int index2) {
        E element1 = getElement(index1);
        E element2 = getElement(index2);
        setElement(index2, element1);
        setElement(index1, element2);
    }

    /* Prints out the underlying heap sideways. Use for debugging. */
    @Override
    public String toString() {
        return toStringHelper(1, "");
    }

    /* Recursive helper method for toString. */
    private String toStringHelper(int index, String soFar) {
        if (getElement(index) == null) {
            return "";
        } else {
            String toReturn = "";
            int rightChild = getRightOf(index);
            toReturn += toStringHelper(rightChild, "        " + soFar);
            if (getElement(rightChild) != null) {
                toReturn += soFar + "    /";
            }
            toReturn += "\n" + soFar + getElement(index) + "\n";
            int leftChild = getLeftOf(index);
            if (getElement(leftChild) != null) {
                toReturn += soFar + "    \\";
            }
            toReturn += toStringHelper(leftChild, "        " + soFar);
            return toReturn;
        }
    }

    /* Returns the index of the left child of the element at index INDEX. */
    private int getLeftOf(int index) {
        return index*2;
    }

    /* Returns the index of the right child of the element at index INDEX. */
    private int getRightOf(int index) {
        return index*2+1;
    }

    /* Returns the index of the parent of the element at index INDEX. */
    private int getParentOf(int index) {
        return index/2;
    }

    /* Returns the index of the smaller element. At least one index has a
       non-null element. If the elements are equal, return either index. */
    private int min(int index1, int index2) {
        if(getElement(index1) == null) {
            return index2;
        } else if(getElement(index2) == null) {
            return index1;
        } else if (getElement(index1).compareTo(getElement(index2))<0){
            return index1;
        } else {
            return index2;
        }
    }

    /* Returns but does not remove the smallest element in the MinHeap. */
    public E findMin() {
        return getElement(1);
    }

    /* Bubbles up the element currently at index INDEX. */
    private void bubbleUp(int index) {
        if(getElement(getParentOf(index)) == null || getElement(index).compareTo(getElement(getParentOf(index)))>0) {
            return;
        }
        swap(index, getParentOf(index));
        bubbleUp(getParentOf(index));
    }

    /* Bubbles down the element currently at index INDEX. */
    private void bubbleDown(int index) {
        if(getElement(index) == null || (min(index, getLeftOf(index)) == index
                && min(index, getRightOf(index)) == index)) {
            return;
        } else if(min(getLeftOf(index), getRightOf(index)) == getLeftOf(index)) {
            swap(index, getLeftOf(index));
            bubbleDown(getLeftOf(index));
        } else {
            swap(index, getRightOf(index));
            bubbleDown(getRightOf(index));
        }
    }

    /* Returns the number of elements in the MinHeap. */
    public int size() {
        return this.size;
    }

    /* Inserts ELEMENT into the MinHeap. If ELEMENT is already in the MinHeap,
       throw an IllegalArgumentException.*/
    public void insert(E element) {
        if(this.contains(element)) {
            throw new IllegalArgumentException();
        }
        size+=1;
        setElement(size, element);
        bubbleUp(size);
    }

    /* Returns and removes the smallest element in the MinHeap. */
    public E removeMin() {
        swap(1, size);
        E node = contents.remove(size);
        bubbleDown(1);
        indexMap.remove(node);
        size--;
        return node;
    }

    /* Replaces and updates the position of ELEMENT inside the MinHeap, which
       may have been mutated since the initial insert. If a copy of ELEMENT does
       not exist in the MinHeap, throw a NoSuchElementException. Item equality
       should be checked using .equals(), not ==. */
    public void update(E element) {
        Integer index = 0;
        index = indexMap.get(element);
        if(index == 0) {
            throw new NoSuchElementException();
        }
        setElement(index, element);//recursively traverse left right if this.priority<other.priority: stop
        bubbleUp(index);
        bubbleDown(index);
    }

    private int updateHelper(E element, int index) {
        if(index>size) {
            return 0;
        } else if(contents.get(index).equals(element)) {
            return index;
        } //else if(contents.get(index).compareTo(container.get(element))>0) {
            //return 0;}
        else {
            return updateHelper(element, getLeftOf(index)) + updateHelper(element, getRightOf(index));
        }
    }

    /* Returns true if ELEMENT is contained in the MinHeap. Item equality should
       be checked using .equals(), not ==. */
    public boolean contains(E element) {
        return indexMap.containsKey(element);
    }

    private boolean containshelper(E element, int index) {
        if(index>size) {
            return false;
        } else if(contents.get(index).equals(element)) {
            return true;
        } //else if(contents.get(index).compareTo(container.get(element))>0) {
            //return false;}
        else {
            return containshelper(element, getLeftOf(index)) || containshelper(element, getRightOf(index));
        }
    }
}
