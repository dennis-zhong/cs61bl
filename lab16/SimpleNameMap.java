import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class SimpleNameMap {

    LinkedList<Entry>[] entryArr;
    int size;
    static final double maxLoad = 0.75;

    public SimpleNameMap() {
        entryArr = (LinkedList<Entry>[]) new LinkedList[10];
        size = 0;
    }

    /* Returns the number of items contained in this map. */
    public int size() {
        return size;
    }

    public double loadFactor() {
        return (double) (size+1)/entryArr.length;
    }

    /* Returns true if the map contains the KEY. */
    public boolean containsKey(String key) {
        int index = Math.floorMod(key.hashCode(), entryArr.length);
        if(entryArr[index] == null) {
            return false;
        }
        for(Entry entry: entryArr[index]) {
            if(entry.keyEquals(new Entry(key, null))) {
                return true;
            }
        }
        return false;
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    public String get(String key) {
        if(containsKey(key)) {
            LinkedList<Entry> pointer = entryArr[Math.floorMod(key.hashCode(), entryArr.length)];
            return pointer.stream().filter(x->x.key.equals(key))
                    .collect(Collectors.toList()).get(0).value;
        }
        return null;
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    public void put(String key, String value) {
        int index = Math.floorMod(key.hashCode(), entryArr.length);
        if(containsKey(key)) {
            LinkedList<Entry> pointer = entryArr[index];
            for(int i = 0; i < pointer.size(); i++) {
                if(pointer.get(i).keyEquals(new Entry(key, null))) {
                    pointer.get(i).value = value;
                    break;
                }
            }
        } else {
            if(loadFactor()>maxLoad) {
                resize();
            }
            if (entryArr[index] == null) {
                entryArr[index] = new LinkedList<Entry>();
            }
            entryArr[index].addLast(new Entry(key, value));
            size++;
        }
    }

    public void resize() {
        LinkedList[] temp = entryArr;
        LinkedList<Entry> pointer;
        entryArr = new LinkedList[entryArr.length*2];
        size = 0;
        for(int i = 0; i < temp.length; i++) {
            pointer = temp[i];
            if (pointer == null) continue;
            for(Entry entry: pointer) {
                this.put(entry.key, entry.value);
            }
        }
    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    public String remove(String key) {
        Entry removed = new Entry(null, null);
        if(containsKey(key)) {
            LinkedList<Entry> pointer = entryArr[Math.floorMod(key.hashCode(), entryArr.length)];
            for(int i = 0; i < pointer.size(); i++) {
                if(pointer.get(i).keyEquals(new Entry(key, null))) {
                    removed = pointer.get(i);
                    pointer.remove(pointer.get(i));
                    break;
                }
            }
            size--;
        }
        return removed.value;
    }

    public int hashCode(String entry) {
        return (int) (entry.charAt(0) - 'A');
    }

    private static class Entry {

        private String key;
        private String value;

        Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry other) {
            return key.equals(other.key);
        }

        /* Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry
                    && key.equals(((Entry) other).key)
                    && value.equals(((Entry) other).value));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}