public class ArrayDeque<T> implements Deque<T>{

    public T[] tArr;
    private int nextFirst;
    private int nextLast;
    private int size;

    public ArrayDeque() {
         tArr = (T[]) new Object[8];
         nextFirst = 3;
         nextLast = 4;
    }

    private void checkExpand() {
        if(size==tArr.length) {
            int front = (nextFirst + 1)%tArr.length;
            int end = nextLast - 1;
            end = modUp(end);
            T[] newArr = (T[]) new Object[tArr.length*2];
            if(front>end) {
                System.arraycopy(tArr, front, newArr, newArr.length / 4, tArr.length - front);
                System.arraycopy(tArr, 0, newArr, newArr.length / 4 + tArr.length - front, nextLast);
            } else {
                System.arraycopy(tArr, front, newArr, newArr.length / 4, size);
            }
            nextFirst = newArr.length/4-1;
            nextLast = newArr.length/4+size;
            tArr = newArr;
        }
    }

    private void checkDownsize() {//check if this makes more sense than combined
        if ((size*4<=tArr.length) && tArr.length>=16) {
            int front = (nextFirst + 1)%tArr.length;
            int end = nextLast - 1;
            end = modUp(end);
            T[] newArr = (T[]) new Object[tArr.length/2];
            if(front>end) {
                System.arraycopy(tArr, front, newArr, newArr.length/4, tArr.length-front);
                System.arraycopy(tArr, 0, newArr, newArr.length / 4 + tArr.length - front, nextLast);            } else {
                System.arraycopy(tArr, front, newArr, newArr.length/4, size);
            }
            tArr = newArr;
            nextFirst = newArr.length/4-1;
            nextLast = newArr.length/4+size;
        }
    }

    //like % but the other way
    private int modUp(int x) {
        if(x<0) {
            x+=tArr.length;
        }
        return x;
    }

    //Adds an item of type T to the front of the deque.
    @Override
    public void addFirst(T item) {
        checkExpand();
        tArr[nextFirst] = item;
        nextFirst--;
        nextFirst = modUp(nextFirst);
        size++;
    }

    //Adds an item of type T to the back of the deque.
    @Override
    public void addLast(T item) {
        checkExpand();
        tArr[nextLast] = item;
        nextLast++;
        nextLast = nextLast%tArr.length;
        size++;
    }

    //Returns the number of items in the deque.
    @Override
    public int size() {
        return size;
    }

    //Prints the items in the deque from first to last, separated by a space. Once all the items have been printed, print out a new line.
    @Override
    public void printDeque() {
        int i = 0;//keep track of how many there are
        int j = (nextFirst+1)%tArr.length;//match i w the correct location in arr
        while(i<size-1) {
            System.out.print(tArr[j].toString()+" ");
            i++;
            j++;
            j%=tArr.length;
        }
        System.out.println(tArr[j].toString());
    }

    //Removes and returns the item at the front of the deque. If no such item exists, returns null.
    @Override
    public T removeFirst() {
        if(this.isEmpty()) {
            return null;
        }
        int front = (nextFirst+1)%tArr.length;
        T temp = tArr[front];
        tArr[front] = null;
        nextFirst = front;
        size--;
        checkDownsize();
        return temp;
    }

    //Removes and returns the item at the back of the deque. If no such item exists, returns null.
    @Override
    public T removeLast() {
        if(this.isEmpty()) {
            return null;
        }
        int end = modUp((nextLast-1));
        T temp = tArr[end];
        tArr[end] = null;
        nextLast = end;
        size--;
        checkDownsize();
        return temp;
    }

    //Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth. If no such item exists, returns null. Must not alter the deque!
    @Override
    public T get(int index){
        return tArr[(nextFirst+1+index)%tArr.length];
    }
}
