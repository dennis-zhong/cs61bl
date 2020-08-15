public class ModNCounter {

    private int myCount;
    private int top;

    public ModNCounter(int top) {
        myCount = 0;
        this.top = top;
    }

    public void increment() {
        myCount++;
        if (myCount == top){
            myCount = 0;
        }
    }

    public void reset() {
        myCount = 0;
    }

    public int value() {
        return myCount;
    }

}
