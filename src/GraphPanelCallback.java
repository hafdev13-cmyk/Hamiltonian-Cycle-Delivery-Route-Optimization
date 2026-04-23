import java.util.HashSet;
import java.util.Set;

public class GraphPanelCallback implements VisualizationCallback {

    private final GraphPanel panel;
    private final Set<Integer> backtrackSet = new HashSet<>();

    public GraphPanelCallback(GraphPanel panel) {
        this.panel = panel;
    }

    @Override
    public void onVertexVisited(int v, int depth, int[] path, int distance) {

        backtrackSet.clear();

        int[] active = new int[depth + 1];
        System.arraycopy(path, 0, active, 0, depth + 1);

        panel.updateVisualizationState(active, new HashSet<>(backtrackSet), distance);
        sleep();
    }

    @Override
    public void onBacktrack(int v, int depth) {

        backtrackSet.add(v);

        panel.updateVisualizationState(new int[0], new HashSet<>(backtrackSet), 0);
        sleep();
    }

    @Override
    public void onCycleFound(int[] cycle, int totalDistance) {
        panel.updateVisualizationState(cycle, new HashSet<>(), totalDistance);
    }

    @Override
    public void onSearchComplete(boolean found) {}

    private void sleep() {
    try {
        Thread.sleep(3000); // 3 seconds delay
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}
}