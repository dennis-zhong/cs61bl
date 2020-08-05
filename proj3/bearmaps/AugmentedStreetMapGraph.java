package bearmaps;

import bearmaps.utils.Constants;
import bearmaps.utils.graph.streetmap.Node;
import bearmaps.utils.graph.streetmap.StreetMapGraph;
import bearmaps.utils.ps.KDTree;
import bearmaps.utils.ps.Point;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    private KDTree tree;
    private HashMap<Point, Node> map;
    private Trie trie;
    private HashMap<String, Node> names;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        map = new HashMap<>();
        List<Node> nodes = this.getAllNodes();
        for(Node node: nodes) {
            double x = projectToX(node.lon(), node.lat());
            double y = projectToY(node.lon(), node.lat());
            map.put(new Point(x, y), node);
        }
        tree = new KDTree(new ArrayList<Point>(map.keySet()));

        trie = new Trie();//EC
        names = new HashMap<>();
        for(Node node: this.getAllNodes().stream().filter(x->x.name()!=null).collect(Collectors.toList())) {
            trie.add(cleanString(node.name()));
            names.put(cleanString(node.name()), node);
        }
    }


    /**
     * For Project Part III
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        double x = projectToX(lon, lat);
        double y = projectToY(lon, lat);
        return map.get(tree.nearest(x, y)).id();
    }

    /**
     * Return the Euclidean x-value for some point, p, in Berkeley. Found by computing the
     * Transverse Mercator projection centered at Berkeley.
     * @param lon The longitude for p.
     * @param lat The latitude for p.
     * @return The flattened, Euclidean x-value for p.
     * @source https://en.wikipedia.org/wiki/Transverse_Mercator_projection
     */
    static double projectToX(double lon, double lat) {
        double dlon = Math.toRadians(lon - ROOT_LON);
        double phi = Math.toRadians(lat);
        double b = Math.sin(dlon) * Math.cos(phi);
        return (K0 / 2) * Math.log((1 + b) / (1 - b));
    }

    /**
     * Return the Euclidean y-value for some point, p, in Berkeley. Found by computing the
     * Transverse Mercator projection centered at Berkeley.
     * @param lon The longitude for p.
     * @param lat The latitude for p.
     * @return The flattened, Euclidean y-value for p.
     * @source https://en.wikipedia.org/wiki/Transverse_Mercator_projection
     */
    static double projectToY(double lon, double lat) {
        double dlon = Math.toRadians(lon - ROOT_LON);
        double phi = Math.toRadians(lat);
        double con = Math.atan(Math.tan(phi) / Math.cos(dlon));
        return K0 * (con - Math.toRadians(ROOT_LAT));
    }


    /**
     * For Project Part IV (extra credit)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        ArrayList<String> locations = new ArrayList<>();
        for(String str: trie.keysWithPrefix(prefix)) {
            locations.add(names.get(str).name());
        }
        return locations;
    }

    /**
     * For Project Part IV (extra credit)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        if(locationName==null || locationName.equals("")) {
            return new LinkedList<>();
        }
        LinkedList<Map<String, Object>> map = new LinkedList<>();
        List<String> locations = trie.keysWithPrefix(cleanString(locationName));
        for(String string: locations) {
            Node node = names.get(string);
            HashMap<String, Object> curr = new HashMap<>();
            curr.put("lat", node.lat());
            curr.put("lon", node.lon());
            curr.put("name", node.name());
            curr.put("id", node.id());
            map.add(curr);
        }
        return map;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

        
    /**
     * Scale factor at the natural origin, Berkeley. Prefer to use 1 instead of 0.9996 as in UTM.
     * @source https://gis.stackexchange.com/a/7298
     */
    private static final double K0 = 1.0;
    /** Latitude centered on Berkeley. */
    private static final double ROOT_LAT = (Constants.ROOT_ULLAT + Constants.ROOT_LRLAT) / 2;
    /** Longitude centered on Berkeley. */
    private static final double ROOT_LON = (Constants.ROOT_ULLON + Constants.ROOT_LRLON) / 2;

    private class Trie {

        Node root;

        public Trie() {
            root = new Node();
        }

        /** Clears all items out of Trie */
        public void clear() {
            root = new Node();
        }

        /** Returns true if the Trie contains KEY, false otherwise */
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

        private void collectStrings(Node node, String prefix, ArrayList<String> collector) {
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

        public void add(String key) {
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

        private class Node {
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
}
