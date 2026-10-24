import java.util.Arrays;

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
        // ++++++++
        int n = s.n();
        int[][] from = Graphs.slidesFrom(s);
        int[][] into = Graphs.slidesInto(s);
        boolean[] seen = new boolean[n];
        int[] finished = new int[n];
        int finishedCount = 0;

        // Iterative DFS: nextEdge stores the next outgoing slide to inspect.
        int[] stack = new int[n];
        int[] nextEdge = new int[n];
        for (int start = 0; start < n; start++) {
            if (seen[start]) continue;
            int top = 0;
            stack[top] = start;
            seen[start] = true;
            while (top >= 0) {
                int cell = stack[top];
                if (nextEdge[top] < from[cell].length) {
                    int slide = from[cell][nextEdge[top]++];
                    int next = s.to()[slide];
                    if (!seen[next]) {
                        seen[next] = true;
                        stack[++top] = next;
                        nextEdge[top] = 0;
                    }
                } else {
                    finished[finishedCount++] = cell;
                    top--;
                }
            }
        }

        int[] zoneOf = new int[n];
        Arrays.fill(zoneOf, -1);
        int zones = 0;
        for (int i = n - 1; i >= 0; i--) {
            int start = finished[i];
            if (zoneOf[start] != -1) continue;
            int top = 0;
            stack[top] = start;
            zoneOf[start] = zones;
            while (top >= 0) {
                int cell = stack[top--];
                for (int slide : into[cell]) {
                    int previous = s.from()[slide];
                    if (zoneOf[previous] == -1) {
                        zoneOf[previous] = zones;
                        stack[++top] = previous;
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
        // ++++++++
        int[] stages = new int[zoneCount];
        Arrays.fill(stages, 1);
        long infinity = Long.MAX_VALUE / 4;
        long[] distance = new long[zoneCount];
        Arrays.fill(distance, infinity);
        distance[zoneOf[0]] = 0;
        int[][] from = Graphs.slidesFrom(s);

        // Put the cells of each zone together, so each cell is processed once.
        int[] count = new int[zoneCount];
        for (int zone : zoneOf) count[zone]++;
        int[][] cellsInZone = new int[zoneCount][];
        for (int zone = 0; zone < zoneCount; zone++) cellsInZone[zone] = new int[count[zone]];
        int[] filled = new int[zoneCount];
        for (int cell = 0; cell < s.n(); cell++) {
            int zone = zoneOf[cell];
            cellsInZone[zone][filled[zone]++] = cell;
        }

        for (int zone = 0; zone < zoneCount; zone++) {
            for (int cell : cellsInZone[zone]) {
                for (int slide : from[cell]) {
                    int nextZone = zoneOf[s.to()[slide]];
                    if (nextZone == zone) continue;
                    stages[nextZone] = Math.max(stages[nextZone], stages[zone] + 1);
                    if (distance[zone] != infinity) {
                        distance[nextZone] = Math.min(distance[nextZone], distance[zone] + s.time()[slide]);
                    }
                }
            }
        }
        int longest = 0;
        for (int stage : stages) longest = Math.max(longest, stage);
        return new long[]{longest, distance[zoneOf[s.n() - 1]]};
    }
}

