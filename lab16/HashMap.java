import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class HashMap<K, V> implements Map61BL<K, V> {

    LinkedList<Entry<K, V>>[] entryArr;
    int size;
    double maxLoad;

    public HashMap() {
        entryArr = (LinkedList<Entry<K, V>>[]) new LinkedList[16];
        size = 0;
        maxLoad = 0.75;
    }

    public HashMap(int initialCapacity) {
        entryArr = (LinkedList<Entry<K, V>>[]) new LinkedList[initialCapacity];
        size = 0;
        maxLoad = 0.75;
    }

    public HashMap(int initialCapacity, double loadFactor) {
        entryArr = (LinkedList<Entry<K, V>>[]) new LinkedList[initialCapacity];
        size = 0;
        maxLoad = loadFactor;
    }

    public int capacity() {
        return entryArr.length;
    }

    /* Returns the number of items contained in this map. */
    @Override
    public int size() {
        return size;
    }

    public double loadFactor() {
        return (double) (size+1)/entryArr.length;
    }

    /* Returns true if the map contains the KEY. */
    @Override
    public boolean containsKey(K key) {
        int index = Math.floorMod(key.hashCode(), entryArr.length);
        if(entryArr[index] == null) {
            return false;
        }
        for(Entry<K, V> entry: entryArr[index]) {
            if(entry.keyEquals(new Entry<K, V>(key, null))) {
                return true;
            }
        }
        return false;
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    @Override
    public V get(K key) {
        if(containsKey(key)) {
            LinkedList<Entry<K, V>> pointer = entryArr[Math.floorMod(key.hashCode(), entryArr.length)];
            return pointer.stream().filter(x->x.key.equals(key))
                    .collect(Collectors.toList()).get(0).value;
        }
        return null;
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    @Override
    public void put(K key, V value) {
        int index = Math.floorMod(key.hashCode(), entryArr.length);
        if(containsKey(key)) {
            LinkedList<Entry<K, V>> pointer = entryArr[index];
            for(int i = 0; i < pointer.size(); i++) {
                if(pointer.get(i).keyEquals(new Entry<K, V>(key, null))) {
                    pointer.get(i).value = value;
                    break;
                }
            }
        } else {
            if(loadFactor()>maxLoad) {
                resize();
            }
            if (entryArr[index] == null) {
                entryArr[index] = new LinkedList<Entry<K, V>>();
            }
            entryArr[index].addLast(new Entry<K, V>(key, value));
            size++;
        }
    }

    public void resize() {
        LinkedList[] temp = entryArr;
        LinkedList<Entry<K, V>> pointer;
        entryArr = new LinkedList[entryArr.length*2];
        size = 0;
        for(int i = 0; i < temp.length; i++) {
            pointer = temp[i];
            if (pointer == null) continue;
            for(Entry<K, V> entry: pointer) {
                this.put(entry.key, entry.value);
            }
        }
    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    @Override
    public V remove(K key) {
        Entry<K, V> removed = new Entry<>(null, null);
        if(containsKey(key)) {
            LinkedList<Entry<K, V>> pointer = entryArr[Math.floorMod(key.hashCode(), entryArr.length)];
            for(int i = 0; i < pointer.size(); i++) {
                if(pointer.get(i).keyEquals(new Entry<K, V>(key, null))) {
                    removed = pointer.get(i);
                    pointer.remove(pointer.get(i));
                    break;
                }
            }
            size--;
        }
        return removed.value;
    }

    @Override
    public boolean remove(K key, V value) {
        boolean removed = false;
        if(containsKey(key)) {
            LinkedList<Entry<K, V>> pointer = entryArr[Math.floorMod(key.hashCode(), entryArr.length)];
            for(int i = 0; i < pointer.size(); i++) {
                if(pointer.get(i).keyEquals(new Entry<K, V>(key, null))
                        && pointer.get(i).value.equals(value)) {
                    removed = true;
                    pointer.remove(pointer.get(i));
                    break;
                }
            }
            size--;
        }
        return removed;
    }

    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        entryArr = (LinkedList<Entry<K, V>>[]) new LinkedList[entryArr.length];
        size = 0;
    }

    private static class Entry<K, V> {

        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry<K, V> other) {
            return key.equals(other.key);
        }

        /* Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry
                    && key.equals(((Entry<K, V>) other).key)
                    && value.equals(((Entry<K, V>) other).value));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
