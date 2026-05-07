public class HamiltonianCycle {

    private final Graph graph;
    private final int[] path;
    private final boolean[] visited;
    private final int n;

    private VisualizationCallback callback;
    private int distance;

    public HamiltonianCycle(Graph graph) {
        this.graph = graph;
        this.n = graph.getVertexCount();
        this.path = new int[n];
        this.visited = new boolean[n];
    }

    public int[] findCycleWithVisualization(VisualizationCallback cb) {

        this.callback = cb;
        distance = 0;

        path[0] = 0;
        visited[0] = true;

        boolean found = backtrack(0, 1);

        if (callback != null) callback.onSearchComplete(found);

        return found ? path : null;
    }

    private boolean backtrack(int current, int depth) {

        if (callback != null)
            callback.onVertexVisited(current, depth - 1, path, distance);

        if (depth == n) {
            if (graph.hasEdge(current, path[0])) {
                distance += graph.getDistance(current, path[0]);

                if (callback != null)
                    callback.onCycleFound(path.clone(), distance);

                return true;
            }
            return false;
        }

        for (int next = 0; next < n; next++) {

            if (!visited[next] && graph.hasEdge(current, next)) {

                path[depth] = next;
                visited[next] = true;
                distance += graph.getDistance(current, next);

                if (backtrack(next, depth + 1)) return true;

                visited[next] = false;
                distance -= graph.getDistance(current, next);

                if (callback != null)
                    callback.onBacktrack(next, depth);
            }
        }

        return false;
    }
}
