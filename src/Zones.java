/**
 * Missions 4 and 5. The slides: a directed graph. Cells are 0 .. n-1, slide i goes from s.from()[i] to s.to()[i]
 * and takes s.time()[i]. There are no slides from a cell to itself, and the exit can always be reached from the start.
 *
 * Ready for you: Graphs.slidesFrom(s)[u] is the list of the slides that leave cell u, and Graphs.slidesInto(s)[v] is the list
 * of the slides that enter cell v (reversed graph). The lists hold slide numbers i; the cell at the other end is s.to()[i]
 * or s.from()[i].
 */
public class Zones {

    /**
     * Mission 4. Kosaraju. Returns zoneOf: zoneOf[cell] = number of the zone, numbers 0 .. zones-1.
     * Number the zones in the order in which the second pass finds them. That order is a topological order
     * of the zone graph: every slide between two zones goes from a smaller number to a larger number.
     *
     * Plan: 1) DFS on the slides, write the cells in the order in which they are finished (use a stack, not recursion);
     * 2) go through the cells from the last finished to the first; if a cell has no zone yet, DFS from it on the reversed
     * slides (slidesInto) and give every cell it reaches the next zone number.
     * Is DAG in Main is 1 when the number of zones equals the number of cells (no cycle), so you do not need a separate check.
     */
    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();
        int[][] from = Graphs.slidesFrom(s);    // from[u] = slides leaving u
        int[][] into = Graphs.slidesInto(s);    // into[v] = slides entering v

        // Pass 1: finishing order on the slides
        boolean[] visited = new boolean[n];
        int[] next = new int[n];    // which slide of u to try next
        int[] stack = new int[n];
        int[] order = new int[n];   // cells in the order they finish
        int done = 0;

        for (int start = 0; start < n; start++) {
            if (visited[start]) continue;
            int top = 0;
            stack[top++] = start;
            visited[start] = true;
            while (top > 0) {
                int u = stack[top - 1];
                if (next[u] < from[u].length) {     // u still has slides to try
                    int i = from[u][next[u]++];     // slide number
                    int v = s.to()[i];  // cell at the END of slide i
                    if (!visited[v]) {
                        visited[v] = true;
                        stack[top++] = v;
                    }
                } else {    // all slides of u done: u is finished
                    top--;
                    order[done++] = u;
                }
            }
        }

        // Pass 2: reversed slides, from the last finished cell
        int[] zoneOf = new int[n];
        java.util.Arrays.fill(zoneOf, -1);  // -1 = no zone yet
        int zones = 0;

        for (int j = n - 1; j >= 0; j--) {
            int u = order[j];
            if (zoneOf[u] != -1) continue;
            int top = 0;
            stack[top++] = u;
            zoneOf[u] = zones;
            while (top > 0) {
                int x = stack[--top];
                for (int i : into[x]) {
                    int y = s.from()[i];    // cell at the START of slide i
                    if (zoneOf[y] == -1) {
                        zoneOf[y] = zones;
                        stack[top++] = y;
                    }
                }
            }
            zones++;
        }
        return zoneOf;
    }

    /**
     * Mission 5. The graph of zones: the edge X to Y costs the cheapest slide from X to Y, walking inside a zone is free.
     * Go through the zones in the order of their numbers (no priority queue). Returns two numbers:
     * [0] flow stages: the number of zones on the longest chain of zones;
     * [1] the cost of the cheapest route from the zone of cell 0 to the zone of the last cell.
     */
    public static long[] routes(Input.Slides s, int[] zoneOf, int zoneCount) {
        // Slides between different zones, grouped by the zone they leave
        java.util.List<Integer>[] out = new java.util.List[zoneCount];
        for (int z = 0; z < zoneCount; z++) out[z] = new java.util.ArrayList<>();
        for (int i = 0; i < s.count(); i++) {
            int x = zoneOf[s.from()[i]], y = zoneOf[s.to()[i]];
            if (x != y) out[x].add(i);  // slide inside a zone is free, skip it
        }

        long[] stages = new long[zoneCount];
        java.util.Arrays.fill(stages, 1);   // every zone alone is a chain of 1

        long INF = Long.MAX_VALUE;
        long[] dist = new long[zoneCount];
        java.util.Arrays.fill(dist, INF);
        dist[zoneOf[0]] = 0;    // start zone costs 0

        // Zones in the order of their numbers = topological order
        for (int x = 0; x < zoneCount; x++) {
            for (int i : out[x]) {
                int y = zoneOf[s.to()[i]];
                stages[y] = Math.max(stages[y], stages[x] + 1);    // longest chain
                if (dist[x] != INF)
                    dist[y] = Math.min(dist[y], dist[x] + s.time()[i]);     // cheapest route
            }
        }

        long maxStages = 0;
        for (long st : stages) maxStages = Math.max(maxStages, st);
        return new long[]{maxStages, dist[zoneOf[s.n() - 1]]};
    }
}
