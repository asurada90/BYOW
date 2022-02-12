package byow.Core;

public class UnionFind {

    private int[] Arr;

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        Arr = new int[N];
        for (int i = 0; i < N; i++){
            Arr[i] = -1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        int root = find(v);
        return -Arr[root];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        return Arr[v];
    }

    /* Returns true if nodes V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        return (find(v1) == find(v2));
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (v >= Arr.length){
            throw new IllegalArgumentException();
        }

        if (Arr[v] < 0){
            return v;
        }

        int root = find(parent(v));
        Arr[v] = root;
        return root;
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing a item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        if (v1 >= Arr.length || v2 >= Arr.length){
            throw new IllegalArgumentException();
        }

        if (connected(v1, v2)){
            return;
        }

        if (sizeOf(v1) <= sizeOf(v2)){
            int temp = sizeOf(v1);
            Arr[find(v1)] = find(v2);
            Arr[find(v2)] -= temp;
        }

        else{
            int temp = sizeOf(v2);
            Arr[find(v2)] = find(v1);
            Arr[find(v1)] -= temp;
        }
    }
}
