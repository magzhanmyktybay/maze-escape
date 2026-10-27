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
        // TODO
        DisjointSet ds = new DisjointSet(w.cells());
        int[] broken = new int[w.cells() - 1];   // insert here indexes of broken walls
        int cnt = 0;                             // count how many walls we broke

        for (int k = 0; k < w.count(); k++) {
            if (ds.union(w.a()[k], w.b()[k])) {            // two cells of wall k
                broken[cnt] = k;               // insert the index k into the broken array
                cnt++; // add +1 to count of broken walls
            }
        }
        return broken;
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        // TODO
        return new int[0];
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        // TODO
        return 0;
    }
}
