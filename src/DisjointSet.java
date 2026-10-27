/**
 * Mission 0. Disjoint set union (DSU).
 * Cells are numbered 0 .. n-1. Write the whole class here.
 * Needed: union by size (the smaller tree goes under the larger one) and path compression in find.
 */
public class DisjointSet {
    int[] parent;
    int[] size;
    int sets;
    public DisjointSet(int n) {
        // TODO
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
            sets = n;
        }
    }

    /** The root of the set that contains x. */
    public int find(int x) {
        // TODO
        if (x != parent[x]) {
            parent[x] = find(parent[x]);
            return parent[x];
        }
        return x;
    }

    /** Joins the sets of a and b. Returns true if they were different sets, false if they were already one set. */
    public boolean union(int a, int b) {
        // TODO
        int ra = find(a);
        int rb = find(b);
        if (ra != rb) {
            if (size[ra] >= size[rb]) {
                parent[rb] = ra;
                size[ra] += size[rb];
            }
            else {
                parent[ra] = rb;
                size[rb] += size[ra];
            }
            sets--;
            return true;
        }
        return false;
    }

    public boolean connected(int a, int b) {
        // TODO
        if (find(a) == find(b)) {
            return true;
        }
        return false;
    }

    /** Number of sets now. */
    public int count() {
        // TODO
        return sets;
    }

}