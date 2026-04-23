/**
 * Graph using adjacency matrix.
 */
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

    public int getVertexCount() {
        return vertexCount;
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public void setVertexPosition(int v, int x, int y) {
        vertexX[v] = x;
        vertexY[v] = y;
    }

    public int getVertexX(int v) {
        return vertexX[v];
    }

    public int getVertexY(int v) {
        return vertexY[v];
    }

    public int getDistance(int u, int v) {
        return distances[u][v];
    }

    public void generateRandomCoordinates(int width, int height, java.util.Random random) {

        java.util.Set<String> used = new java.util.HashSet<>();

        for (int i = 0; i < vertexCount; i++) {
            int x, y;
            String key;

            do {
                x = random.nextInt(width);
                y = random.nextInt(height);
                key = x + "," + y;
            } while (used.contains(key));

            used.add(key);
            setVertexPosition(i, x, y);
        }

        computeDistances();
    }

    private void computeDistances() {
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                if (i == j) {
                    distances[i][j] = 0;
                } else {
                    int dx = vertexX[j] - vertexX[i];
                    int dy = vertexY[j] - vertexY[i];
                    distances[i][j] = (int) Math.sqrt(dx * dx + dy * dy);
                }
            }
        }
    }
}