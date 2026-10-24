# Assignment 3. Maze Escape

Student ID: 251449
Name: Nauryzbay
Group: SE-2536
Maze size (rows x columns): 12 x 11

This file is the report. Fill it in as you go. How to start: `START_HERE.md`. The task: `docs/Assignment3.pdf`.

## Program output

The 13 lines your program prints on your data (`java -cp out Main`):

    Broken: 131
    Kept: 110
    Last broken wall: 181
    MST cost: 38552
    Heaviest broken wall: 814
    Path length: 33
    DFS visited: 97
    Dead ends: 37
    Zones: 12
    Largest zone: 17
    Is DAG: 0
    Flow stages: 7
    Fastest route cost: 19

My maze:

![maze](output/maze.png)

## Questions

Answer in your own words, two or three sentences each.

Mission 0. What do find and union do, and what makes them fast in your code?

`find` returns the representative (root) of the set containing a cell. `union` joins two different sets and returns false if they are already connected. Path compression makes future finds shorter, and union by size keeps the trees shallow.

Mission 1. Why is Broken always R*C - 1, and Kept always (R-1)*(C-1)?

The broken walls form a spanning tree of all R*C cells. A tree with n cells always has n - 1 edges, so Broken is R*C - 1. The grid starts with R*(C-1) + C*(R-1) inner walls; after breaking the tree edges, the remaining number is (R-1)*(C-1).

Mission 2. My MST cost from Kruskal and from Prim, and why they are equal:

My MST cost is 38552 with both Kruskal and Prim. They are equal because both algorithms find a minimum spanning tree: Kruskal chooses globally cheapest safe walls, while Prim grows one cheapest connected tree.

Mission 3. Why is there exactly one path between two cells? How did you avoid a stack overflow on a huge maze?

The maze is a connected tree with no cycles, so there is exactly one path between every pair of cells. I used arrays as an explicit DFS stack instead of recursive method calls, so a very large maze does not overflow the Java call stack.

Mission 4. Why does the second pass of Kosaraju go on the reversed graph? Why do the slides have no cycle exactly when Zones equals the number of cells?

The first pass gives a finishing order; following reversed edges in the second pass prevents the search from leaving the current strongly connected component. If every cell is its own zone, no two different cells can reach each other in both directions, so the directed slide graph has no cycle.

Mission 5. Why is it enough to go through the zones in the order of their numbers? What does this pass cost?

Kosaraju numbers the zones in topological order, so every edge between zones goes from a smaller number to a larger number. Therefore, when processing one zone, all possible earlier route costs are already known. The pass takes O(n + m) time, where n is the number of cells and m is the number of slides.

## AI use

I used ChatGPT/Codex to ask for explanations of the algorithms and help understanding the assignment. I also used it to discuss the implementation of DSU, Kruskal, Prim, iterative DFS, Kosaraju, and the route calculation.

## Timing (Mission 7)

Milliseconds, 5 runs each, from `java -cp out Bench`.

| maze | Kruskal | Prim |
|---|---|---|
| 100 x 100 | 10 ms | 7 ms |
| 300 x 300 | 77 ms | 60 ms |
| 1000 x 1000 | 1250 ms | 1563 ms |

The running time increases quickly as the number of cells and walls increases. Both algorithms must examine many more walls in a larger grid, while Kruskal also sorts the walls and Prim uses a priority queue. Exact timings can vary between runs and computers.

## Complexity

n is the number of cells, m the number of walls (or slides). Fill in every algorithm you wrote.

| algorithm | time | space | assumptions |
|---|---|---|---|
| DSU find and union | O(α(n)) amortized | O(n) | Path compression and union by size |
| Kruskal | O(m log m) | O(n + m) | Sorting m walls dominates |
| Prim | O(m log m) | O(n + m) | Binary priority queue |
| DFS | O(n) | O(n) | The maze is a tree with n - 1 edges |
| Kosaraju | O(n + m) | O(n + m) | Iterative stacks and adjacency lists |
| Route in the zone graph | O(n + m) | O(n + m) | Zones are already in topological order |

## Before you submit

- data/walls.txt, data/slides.txt and output/maze.png are in the repository.
- `java tools/Check.java` shows all examples right.
- This README is filled in, the program output and the section AI use included.
- The work is pushed to main (at least one commit per mission) and the repository is public.
- The link is attached in Moodle.
