/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells(), cols = w.cols(), exit = n - 1;
        int[][] open = Graphs.open(w, broken);   // open[c] = cells behind broken walls

        // Dead ends: cells with exactly one open door
        int deadEnds = 0;
        for (int c = 0; c < n; c++)
            if (open[c].length == 1) deadEnds++;

        boolean[] visited = new boolean[n];
        int[] dir = new int[n];     // next direction to try for each cell
        int[] stack = new int[n];   // our own stack = current path
        int top = 0;    // number of cells in the stack

        stack[top++] = 0;   // start in cell 0
        visited[0] = true;
        int count = 1;

        while (top > 0) {
            int cur = stack[top - 1];   // cell on top of the stack
            if (cur == exit) break;     // found the exit

            if (dir[cur] == 4) {    // all 4 directions tried: dead end, go back
                top--;
                continue;
            }

            int d = dir[cur]++;     // take next direction
            int nb;     // neighbour in direction d
            if (d == 0)      nb = cur - cols;   // up
            else if (d == 1) nb = cur + 1;  // right
            else if (d == 2) nb = cur + cols;   // down
            else             nb = cur - 1;  // left

            if (isOpen(open, cur, nb) && !visited[nb]) {
                visited[nb] = true;
                count++;
                stack[top++] = nb;  // step forward
            }
        }

        int[] path = java.util.Arrays.copyOf(stack, top);   // stack = path from 0 to exit
        return new Result(path, count, deadEnds);
    }

    // true if nb is in the list of open neighbours of cur
    static boolean isOpen(int[][] open, int cur, int nb) {
        for (int x : open[cur]) if (x == nb) return true;
        return false;
    }
}
