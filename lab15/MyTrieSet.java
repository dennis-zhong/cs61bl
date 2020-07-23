import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MyTrieSet implements TrieSet61BL {

    Node root;

    public MyTrieSet() {
        root = new Node();
    }

    /** Clears all items out of Trie */
    @Override
    public void clear() {
        root = new Node();
    }

    /** Returns true if the Trie contains KEY, false otherwise */
    @Override
    public boolean contains(String key) {
        Node pointer = root;
        do {
            if(pointer == null) {
                return false;
            }
            pointer = pointer.map.get(key.charAt(0));
            key = key.substring(1);
        } while (!key.equals(""));
        return pointer.isKey;
    }

    /** Returns a list of all words that start with PREFIX */
    @Override
    public List<String> keysWithPrefix(String prefix) {
        ArrayList<String> collective = new ArrayList<>();
        Node pointer = root;
        String copy = prefix;
        do {
            if(pointer == null) {
                return collective;
            }
            pointer = pointer.map.get(copy.charAt(0));
            copy = copy.substring(1);
        } while (!copy.equals(""));
        collectStrings(pointer, prefix, collective);
        return collective;
    }

    public void collectStrings(Node node, String prefix, ArrayList<String> collector) {
        if(node == null) {
            return;
        } else if(node.isKey) {
            collector.add(prefix);
        }
        for (char c: node.map.keySet()) {
            prefix+=c;
            collectStrings(node.map.get(c), prefix, collector);
            prefix = prefix.substring(0, prefix.length()-1);
        }
    }

    @Override
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }

    @Override public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                curr.map.put(c, new Node(c, false));
            }
            curr = curr.map.get(c);
        }
        curr.isKey = true;
    }

    private static class Node {
        char item;
        boolean isKey;
        HashMap<Character, Node> map;

        public Node() {
            isKey = false;
            map = new HashMap<>();
        }

        public Node(char chr, boolean isKey) {
            item = chr;
            this.isKey = isKey;
            map = new HashMap<>();
        }
    }
}
