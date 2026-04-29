import java.io.*;
import java.util.Random;

public class GraphData {

    private static final int N = 20;

    public static Graph createDeliveryNetwork() {

        Graph graph = new Graph(N);

        try {
            loadFromCSV(graph, "data.csv");
        } catch (Exception e) {
            createDefaultGraph(graph);
        }

        return graph;
    }

    private static void loadFromCSV(Graph graph, String file) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        boolean first = true;

        while ((line = br.readLine()) != null) {

            if (first) {
                first = false;
                continue;
            }

            String[] p = line.split(",");

            int u = Integer.parseInt(p[0].trim());
            int v = Integer.parseInt(p[1].trim());

            graph.addEdge(u, v);
        }

        br.close();
    }

    private static void createDefaultGraph(Graph graph) {

        for (int i = 0; i < N; i++) {
            graph.addEdge(i, (i + 1) % N);
        }

        Random r = new Random(42);

        for (int i = 0; i < N / 2; i++) {
            int u = r.nextInt(N);
            int v = r.nextInt(N);

            if (u != v && !graph.hasEdge(u, v)) {
                graph.addEdge(u, v);
            }
        }
    }
}
