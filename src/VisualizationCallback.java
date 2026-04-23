public interface VisualizationCallback {

    void onVertexVisited(int vertex, int depth, int[] path, int distance);

    void onBacktrack(int vertex, int depth);

    void onCycleFound(int[] cycle, int totalDistance);

    void onSearchComplete(boolean found);
}