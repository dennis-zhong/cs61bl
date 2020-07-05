public class LinkedListDeque<T> implements Deque<T>{

    private static class TNode<T> {
        public T item;
        public TNode<T> next;
        public TNode<T> previous;

        public TNode(T item, TNode next) {
            this.item = item;
            this.next = next;
        }

        public TNode(T item, TNode next, TNode previous) {
            this.item = item;
            this.next = next;
            this.previous = previous;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {//checks both null case/same pointer
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            TNode that = (TNode) obj;
            return item.equals(that.item);
        }

        @Override
        public String toString() {
            if(this.item != null) {
                return this.item.toString();
            } else {
                return null;
            }
        }
    }

    private TNode<T> sentinel;
    private int size;

    //Constructor which creates an empty linked list deque.
    public LinkedListDeque(){
        sentinel = new TNode<T>(null, null, null);
        sentinel.next = sentinel;
        sentinel.previous = sentinel;
    }

    /*
    //Constructor that accepts value
    public LinkedListDeque(T input) {
        sentinel = new TNode<>(null, null, null);
        sentinel.next = new TNode<>(input, sentinel, sentinel);
        connectSentToEnd();
        size++;
    }

    //connection to prev for sent keeps getting cut off
    private void connectSentToEnd() {
        TNode<T> pointer = sentinel.next;
        while(!pointer.next.equals(sentinel)) pointer = pointer.next;
        sentinel.previous = pointer;
    }*/

    //Adds an item of type T to the front of the deque.
    @Override
    public void addFirst(T item) {
        TNode<T> temp = sentinel.next;
        sentinel.next = new TNode(item, temp, sentinel);
        temp.previous = sentinel.next;
        size++;
    }

    //Adds an item of type T to the back of the deque.
    @Override
    public void addLast(T item) {
        TNode<T> temp = sentinel.previous;
        sentinel.previous.next = new TNode<>(item, sentinel, temp);
        sentinel.previous = sentinel.previous.next;
        size++;
    }

    //equals method for Deques
    /*
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof LinkedListDeque)) {
            return false;
        }
        LinkedListDeque<T> copy = (LinkedListDeque<T>) obj;
        if (copy.size != this.size) {
            return false;
        }

        TNode<T> pointer1 = sentinel.next;
        TNode<T> pointer2 = copy.sentinel.next;
        while (pointer1 != sentinel  && pointer2 != copy.sentinel) {
            if (!pointer1.equals(pointer2)) return false;
            pointer1 = pointer1.next;
            pointer2 = pointer2.next;
        }
        return true;

    }*/

    //Returns the number of items in the deque.
    @Override
    public int size() {
        return this.size;
    }

    //Prints the items in the deque from first to last, separated by a space. Once all the items have been printed, print out a new line.
    @Override
    public void printDeque() {
        if (this.isEmpty()) {
            System.out.println("");
            return;
        }
        TNode<T> curr = sentinel.next;
        for (int i = 0; i < size-1; i++) {
            System.out.print(curr.toString()+" ");
            curr = curr.next;
        }
        System.out.println(curr.toString());
    }

    //Removes and returns the item at the front of the deque. If no such item exists, returns null.
    @Override
    public T removeFirst() {
        if(this.isEmpty()) return null;
        T item = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.previous = sentinel;
        size--;
        return item;
    }

    //Removes and returns the item at the back of the deque. If no such item exists, returns null.
    @Override
    public T removeLast() {
        if(this.isEmpty()) return null;
        T item = sentinel.previous.item;
        sentinel.previous.previous.next = sentinel;
        sentinel.previous = sentinel.previous.previous;
        size--;
        return item;
    }

    //Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth. If no such item exists, returns null. Must not alter the deque!
    @Override
    public T get(int index){
        if ((index >= this.size()) || index < 0) {
            return null; //catch errors
        } else {
            TNode<T> pointer = sentinel.next;
            while (index > 0) {
                pointer = pointer.next;
                index--;
            }
            return pointer.item;
        }
    }

    //Same as get, but this method should be implemented using recursion
    public T getRecursive(int index) {
        if ((index >= this.size()) || index < 0) {
            return null; //catch errors
        }

        TNode<T> pointer = sentinel.next;
        return getHelper(index, pointer);
    }

    private T getHelper(int index, TNode<T> curr) {
        if(index == 0) {
            return curr.item;
        } else {
            return this.getHelper(index-1, curr.next);
        }
    }
}
