public class PandaBear extends Bear implements Comparable<PandaBear> {

    public PandaBear(int[] supply, int w) {
        super(supply, w);
    }

    @Override
    public int compareTo(PandaBear o) {
        if(this.weight<o.weight) {
            return -1;
        } else if(this.weight == o.weight) {
            int min = Math.min(this.foodSupply.length, o.foodSupply.length);
            for(int i = 0; i < min; i++) {
                if(foodSupply[i]<o.foodSupply[i]) {
                    return -1;
                } else if(foodSupply[i]>o.foodSupply[i]) {
                    return 1;
                }
            }
            if(min != foodSupply.length || min != o.foodSupply.length) {
                if(foodSupply.length < o.foodSupply.length) {
                    return -1;
                } else if(foodSupply.length < o.foodSupply.length) {
                    return 1;
                }
            }
            return 0;
        } else {
            return 1;
        }
    }
}