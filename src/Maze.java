/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        DisjointSet ds = new DisjointSet(w.cells());
        int[] broken = new int[w.cells() - 1];   // insert here indexes of broken walls
        int cnt = 0;    // count how many walls we broke

        for (int k = 0; k < w.count(); k++) {
            if (ds.union(w.a()[k], w.b()[k])) { // two cells of wall k
                broken[cnt] = k;    // insert the index k into the broken array
                cnt++;  // add +1 to count of broken walls
            }
        }
        return broken;
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        DisjointSet ds = new DisjointSet(w.cells());
        int[] broken = new int[w.cells() - 1];   // insert here indexes of broken walls
        int cnt = 0;    // count how many walls we broke

        Integer[] order = new Integer[w.count()];   // walls numbers array
        for (int i = 0; i < w.count(); i++) order[i] = i; // fill by numbers in order 0,1,2,...
        java.util.Arrays.sort(order, (x, y) -> Integer.compare(w.cost()[x], w.cost()[y]));  // sort numbers of walls by their cost
        for (int i = 0; i < w.count(); i++) {
            int k = order[i];   // took i-th cheapest wall
            if (ds.union(w.a()[k], w.b()[k])) {     // two cells of wall k
                broken[cnt] = k;    // insert the index k into the broken array
                cnt++;  // add +1 to count of broken walls
            }
        }
        return broken;
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int n = w.cells();  // number of cells

        // For every cell: list of walls that touch it
        java.util.List<Integer>[] adj = new java.util.List[n];  // one list per cell
        for (int i = 0; i < n; i++) adj[i] = new java.util.ArrayList<>();   // empty list for every cell
        for (int k = 0; k < w.count(); k++) {
            adj[w.a()[k]].add(k);
            adj[w.b()[k]].add(k);
            // wall k touches both cells
        }

        // Heap of walls: the cheapest is on top
        java.util.PriorityQueue<Integer> pq =
                new java.util.PriorityQueue<>((x, y) -> Integer.compare(w.cost()[x], w.cost()[y]));     // compare walls by cost

        boolean[] inTree = new boolean[n];  // true if the cell is already in the tree
        long total = 0;     // total cost of the MST

        // Start from cell 0
        inTree[0] = true;
        for (int k : adj[0]) pq.add(k);

        // Grow the tree
        while (!pq.isEmpty()) {
            int k = pq.poll();  // cheapest wall
            int a = w.a()[k], b = w.b()[k];     // two cells of wall k
            if (inTree[a] && inTree[b]) continue;   // both cells in tree: skip

            int next = inTree[a] ? b : a;   // the cell that is NOT in the tree yet
            inTree[next] = true;    // add the new cell to the tree
            total += w.cost()[k];   // cost of wall k
            for (int k2 : adj[next]) pq.add(k2);    // walls of the new cell go to the heap
        }
        return total;   // cost of the cheapest maze
    }
}
