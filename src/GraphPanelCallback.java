import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class GraphPanelCallback implements VisualizationCallback {

    private final GraphPanel panel;

    // Mutable state tracked across steps
    private final Set<Integer> backtrackSet = new HashSet<>();
    private int stepCount = 0;

    // Playback control
    private volatile boolean paused = false;
    private volatile long delayMs = 600; // default speed

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition resumeCondition = lock.newCondition();

    public GraphPanelCallback(GraphPanel panel) {
        this.panel = panel;
    }


    @Override
    public void onVertexVisited(int vertex, int depth, int[] path, int distance) {
        stepCount++;
        backtrackSet.clear();

        // Build the active path slice: path[0..depth]
        int[] active = new int[depth + 1];
        System.arraycopy(path, 0, active, 0, depth + 1);

        // The edge being explored: from path[depth-1] to vertex (if depth > 0)
        int edgeFrom = (depth > 0) ? path[depth - 1] : -1;
        int edgeTo   = vertex;

        String status = String.format("Step %d — Visiting vertex %d  (depth %d)",
                stepCount, vertex, depth);

        panel.updateVisualizationState(
                active, new HashSet<>(backtrackSet), distance,
                stepCount, status, edgeFrom, edgeTo);

        waitForNextStep();
    }

    @Override
    public void onBacktrack(int vertex, int depth) {
        stepCount++;
        backtrackSet.add(vertex);

        String status = String.format("Step %d — Backtrack from vertex %d  (depth %d)",
                stepCount, vertex, depth);

        panel.updateVisualizationState(
                new int[0], new HashSet<>(backtrackSet), 0,
                stepCount, status, -1, -1);

        waitForNextStep();
    }

    @Override
    public void onCycleFound(int[] cycle, int totalDistance) {
        stepCount++;
        panel.showCycleFound(cycle, totalDistance, stepCount);
    }

    @Override
    public void onSearchComplete(boolean found) {
        if (!found) {
            panel.showNoSolution(stepCount);
        }
    }

    // ─────────────────────────────────────────────
    //  Playback controls (called from UI thread)
    // ─────────────────────────────────────────────

    /** Pause execution after the current step. */
    public void pause() {
        paused = true;
    }

    /** Resume continuous playback. */
    public void play() {
        lock.lock();
        try {
            paused = false;
            resumeCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /** Advance exactly one step while paused. */
    public void step() {
        lock.lock();
        try {
            // Allow one step through, then re-pause
            paused = true;
            resumeCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /** Set delay between steps in milliseconds (0 = as fast as possible). */
    public void setDelay(long ms) {
        this.delayMs = ms;
    }

    public boolean isPaused() {
        return paused;
    }

    // ─────────────────────────────────────────────
    //  Internal: wait / sleep logic
    // ─────────────────────────────────────────────

    private void waitForNextStep() {
        // First apply the speed delay
        if (delayMs > 0) {
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        // Then block if paused
        lock.lock();
        try {
            while (paused) {
                try {
                    resumeCondition.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                // After a step() call we re-pause immediately
                if (!paused) break;
                paused = true; // re-pause for next step
                break;
            }
        } finally {
            lock.unlock();
        }
    }
}
