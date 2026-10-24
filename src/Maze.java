import java.util.Arrays;
import java.util.PriorityQueue;

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
        // ++++++++
        DisjointSet sets = new DisjointSet(w.cells());
        int[] broken = new int[w.cells() - 1];
        int used = 0;
        for (int i = 0; i < w.count(); i++) {
            if (sets.union(w.a()[i], w.b()[i])) broken[used++] = i;
        }
        return Arrays.copyOf(broken, used);
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        // ++++++++
        Integer[] order = new Integer[w.count()];
        for (int i = 0; i < order.length; i++) order[i] = i;
        Arrays.sort(order, (i, j) -> Integer.compare(w.cost()[i], w.cost()[j]));

        DisjointSet sets = new DisjointSet(w.cells());
        int[] broken = new int[w.cells() - 1];
        int used = 0;
        for (int wall : order) {
            if (sets.union(w.a()[wall], w.b()[wall])) broken[used++] = wall;
        }
        return Arrays.copyOf(broken, used);
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        // ++++++++
        int n = w.cells();
        int[][] incident = new int[n][];
        int[] degree = new int[n];
        for (int i = 0; i < w.count(); i++) {
            degree[w.a()[i]]++;
            degree[w.b()[i]]++;
        }
        for (int i = 0; i < n; i++) incident[i] = new int[degree[i]];
        int[] filled = new int[n];
        for (int i = 0; i < w.count(); i++) {
            incident[w.a()[i]][filled[w.a()[i]]++] = i;
            incident[w.b()[i]][filled[w.b()[i]]++] = i;
        }

        boolean[] inTree = new boolean[n];
        PriorityQueue<int[]> queue = new PriorityQueue<>((x, y) -> Integer.compare(x[0], y[0]));
        queue.add(new int[]{0, 0}); // cost, cell
        long total = 0;
        while (!queue.isEmpty()) {
            int[] next = queue.remove();
            int cell = next[1];
            if (inTree[cell]) continue;
            inTree[cell] = true;
            total += next[0];
            for (int wall : incident[cell]) {
                int other = w.a()[wall] == cell ? w.b()[wall] : w.a()[wall];
                if (!inTree[other]) queue.add(new int[]{w.cost()[wall], other});
            }
        }
        return total;
    }
}

