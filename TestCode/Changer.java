public class Changer {
    public Object run(LinkedList toChange, boolean toArrayList) {
        if(toArrayList) {
            ArrayList<Object> list = new ArrayList<>();
            while(toChange.next != null) {
                list.add(toChange.item);
                toChange = toChange.next;
            }
            return list;
        } else {
            Object[] arr = new Object[toChange.size()];
            int i = 0;
            while(toChange.next != null) {
                arr[i](toChange.item);
                toChange = toChange.next;
                i++;
            }
            return arr;
        }
    }//.29, .29, .7
}