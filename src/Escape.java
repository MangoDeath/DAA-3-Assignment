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
        // ++++++++
        int n = w.cells();
        int[][] graph = Graphs.open(w, broken);
        int deadEnds = 0;
        for (int[] neighbours : graph) if (neighbours.length == 1) deadEnds++;

        boolean[] seen = new boolean[n];
        int[] parent = new int[n];
        int[] stack = new int[n];
        int[] nextDirection = new int[n];
        int top = 0;
        stack[0] = 0;
        seen[0] = true;
        parent[0] = -1;
        int visited = 1;
        int exit = n - 1;

        while (top >= 0 && stack[top] != exit) {
            int cell = stack[top];
            if (nextDirection[top] == 4) {
                top--;
                continue;
            }
            int direction = nextDirection[top]++;
            int neighbour = neighbour(cell, direction, w.rows(), w.cols());
            if (neighbour >= 0 && !seen[neighbour] && open(graph[cell], neighbour)) {
                seen[neighbour] = true;
                visited++;
                parent[neighbour] = cell;
                stack[++top] = neighbour;
                nextDirection[top] = 0;
            }
        }

        int length = 0;
        for (int cell = exit; cell != -1; cell = parent[cell]) length++;
        int[] path = new int[length];
        for (int cell = exit, i = length - 1; cell != -1; cell = parent[cell]) path[i--] = cell;
        return new Result(path, visited, deadEnds);
    }

    private static int neighbour(int cell, int direction, int rows, int cols) {
        // ++++++++
        int row = cell / cols;
        int col = cell % cols;
        if (direction == 0) return row > 0 ? cell - cols : -1;
        if (direction == 1) return col + 1 < cols ? cell + 1 : -1;
        if (direction == 2) return row + 1 < rows ? cell + cols : -1;
        return col > 0 ? cell - 1 : -1;
    }

    private static boolean open(int[] neighbours, int cell) {
        // ++++++++
        for (int neighbour : neighbours) if (neighbour == cell) return true;
        return false;
    }
}
