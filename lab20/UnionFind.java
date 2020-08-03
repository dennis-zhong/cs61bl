import java.util.ArrayList;

public class UnionFind {

    Integer[] arr;

    /* Creates a UnionFind data structure holding N vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int N) {
        arr = new Integer[N];
        for(int i = 0; i<N; i++) {
            arr[i] = -1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        checkInput(v);
        return -arr[find(v)];
    }

    private void checkInput(int v) {
        if(v>=arr.length||v<0) {
            throw new IllegalArgumentException();
        }
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        if(arr[v]<0) {
            return -arr[v];
        } else {
            return arr[v];
        }
    }

    /* Returns true if nodes V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        if(arr[v1]<0||arr[v2]<0) {
            return false;
        }
        return find(v1)==find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid vertices are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        checkInput(v);
        ArrayList<Integer> path = findHelper(v, new ArrayList<Integer>());
        for(int i = 1; i<path.size(); i++) {
            arr[path.get(i)] = path.get(0);
        }
        return path.get(0);
    }

    private ArrayList<Integer> findHelper(int v, ArrayList<Integer> lst) {
        if(arr[v]<0) {
            lst.add(0, v);
            return lst;
        }
        lst.add(v);
        return findHelper(arr[v], lst);
    }

    /* Connects two elements V1 and V2 together. V1 and V2 can be any element,
       and a union-by-size heuristic is used. If the sizes of the sets are
       equal, tie break by connecting V1's root to V2's root. Union-ing a vertex
       with itself or vertices that are already connected should not change the
       structure. */
    public void union(int v1, int v2) {
        checkInput(v1);
        checkInput(v2);
        int size1 = sizeOf(v1);
        int size2 = sizeOf(v2);
        int larger, smaller = 0;
        if(size1>size2) {
            larger = v1;
            smaller = v2;
        } else {
            larger = v2;
            smaller = v1;
        }
        arr[find(smaller)] = find(larger);
        arr[larger] = -(size1+size2);
    }
}
