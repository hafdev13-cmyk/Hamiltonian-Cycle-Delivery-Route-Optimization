
public class Graph {

    private final int[][] adjacencyMatrix;
    private final int vertexCount;
    private final int[] vertexX;
    private final int[] vertexY;
    private final int[][] distances;

    public Graph(int vertexCount) {
        this.vertexCount = vertexCount;
        this.adjacencyMatrix = new int[vertexCount][vertexCount];
        this.vertexX = new int[vertexCount];
        this.vertexY = new int[vertexCount];
        this.distances = new int[vertexCount][vertexCount];
    }

    public void addEdge(int u, int v) {
        if (u >= 0 && v >= 0 && u < vertexCount && v < vertexCount && u != v) {
            adjacencyMatrix[u][v] = 1;
            adjacencyMatrix[v][u] = 1;
        }
    }

    public boolean hasEdge(int u, int v) {
        return adjacencyMatrix[u][v] == 1;
    }

    public int getVertexCount() { return vertexCount; }

    public int[][] getAdjacencyMatrix() { return adjacencyMatrix; }

    public void setVertexPosition(int v, int x, int y) {
        vertexX[v] = x;
        vertexY[v] = y;
    }

    public int getVertexX(int v) { return vertexX[v]; }
    public int getVertexY(int v) { return vertexY[v]; }

    public int getDistance(int u, int v) { return distances[u][v]; }

    /**
     * Places vertices using a force-directed (Fruchterman-Reingold style) layout.
     * Repulsion keeps nodes apart; a weak centering force keeps them in bounds.
     * topMargin lets callers reserve space for a HUD bar at the top.
     */
    public void generateRandomCoordinates(int width, int height, java.util.Random random) {
        generateRandomCoordinates(width, height, 100, 50, random);
    }

    public void generateRandomCoordinates(int width, int height,
                                           int topMargin, int sideMargin,
                                           java.util.Random random) {

        int left   = sideMargin;
        int right  = width  - sideMargin;
        int top    = topMargin;
        int bottom = height - sideMargin;
        int innerW = right  - left;
        int innerH = bottom - top;

        // ── 1. Initial placement on a jittered grid ──────────────────
        int cols = (int) Math.ceil(Math.sqrt(vertexCount));
        int rows = (int) Math.ceil((double) vertexCount / cols);

        double[] fx = new double[vertexCount];
        double[] fy = new double[vertexCount];

        for (int i = 0; i < vertexCount; i++) {
            int col = i % cols;
            int row = i / cols;
            fx[i] = left + (col + 0.5) * innerW / cols + (random.nextDouble() - 0.5) * 30;
            fy[i] = top  + (row + 0.5) * innerH / rows + (random.nextDouble() - 0.5) * 30;
        }

        // ── 2. Force-directed relaxation ─────────────────────────────
        double minDist  = 90.0;
        double repulse  = minDist * minDist * 2.5;
        double attract  = 0.004;
        double cx       = (left + right)  / 2.0;
        double cy       = (top  + bottom) / 2.0;

        for (int iter = 0; iter < 300; iter++) {
            double[] dx = new double[vertexCount];
            double[] dy = new double[vertexCount];

            for (int i = 0; i < vertexCount; i++) {
                for (int j = i + 1; j < vertexCount; j++) {
                    double ddx = fx[i] - fx[j];
                    double ddy = fy[i] - fy[j];
                    double dist = Math.max(Math.sqrt(ddx * ddx + ddy * ddy), 1.0);
                    double force = repulse / (dist * dist);
                    dx[i] += (ddx / dist) * force;
                    dy[i] += (ddy / dist) * force;
                    dx[j] -= (ddx / dist) * force;
                    dy[j] -= (ddy / dist) * force;
                }
            }

            for (int i = 0; i < vertexCount; i++) {
                dx[i] += (cx - fx[i]) * attract;
                dy[i] += (cy - fy[i]) * attract;
            }

            double cooling = 1.0 - iter / 300.0;
            double maxStep = 8.0 * cooling + 0.5;
            for (int i = 0; i < vertexCount; i++) {
                double mag = Math.sqrt(dx[i] * dx[i] + dy[i] * dy[i]);
                if (mag > maxStep) { dx[i] = dx[i] / mag * maxStep; dy[i] = dy[i] / mag * maxStep; }
                fx[i] = Math.max(left, Math.min(right,  fx[i] + dx[i]));
                fy[i] = Math.max(top,  Math.min(bottom, fy[i] + dy[i]));
            }
        }

        // ── 3. Commit positions ───────────────────────────────────────
        for (int i = 0; i < vertexCount; i++) {
            setVertexPosition(i, (int) fx[i], (int) fy[i]);
        }

        computeDistances();
    }

    private void computeDistances() {
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                if (i == j) {
                    distances[i][j] = 0;
                } else {
                    int ddx = vertexX[j] - vertexX[i];
                    int ddy = vertexY[j] - vertexY[i];
                    distances[i][j] = (int) Math.sqrt(ddx * ddx + ddy * ddy);
                }
            }
        }
    }
}
