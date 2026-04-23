import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class GraphPanel extends JPanel {

    private final Graph graph;
    private int[] path = new int[0];
    private Set<Integer> backtrack = new HashSet<>();
    private int distance = 0;

    public GraphPanel(Graph graph, int[] cycle) {
        this.graph = graph;
        this.path = (cycle != null) ? cycle : new int[0];
        setBackground(Color.WHITE);
    }

    public void updateVisualizationState(int[] path, Set<Integer> back, int dist) {
        this.path = path;
        this.backtrack = back;
        this.distance = dist;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // smooth drawing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawEdges(g2);
        drawPath(g2);
        drawVertices(g2);

        // draw distance
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Distance: " + distance, 10, 20);
    }

    // ================= EDGES =================
    private void drawEdges(Graphics2D g) {
        int[][] m = graph.getAdjacencyMatrix();

        g.setColor(new Color(180, 180, 180)); // light gray
        g.setStroke(new BasicStroke(2)); // thicker edges

        for (int i = 0; i < graph.getVertexCount(); i++) {
            for (int j = i + 1; j < graph.getVertexCount(); j++) {

                if (m[i][j] == 1) {
                    g.drawLine(
                        graph.getVertexX(i), graph.getVertexY(i),
                        graph.getVertexX(j), graph.getVertexY(j)
                    );
                }
            }
        }
    }

    // ================= PATH =================
    private void drawPath(Graphics2D g) {

        g.setColor(Color.BLUE);
        g.setStroke(new BasicStroke(4)); // thick path

        for (int i = 0; i < path.length - 1; i++) {
            g.drawLine(
                graph.getVertexX(path[i]), graph.getVertexY(path[i]),
                graph.getVertexX(path[i + 1]), graph.getVertexY(path[i + 1])
            );
        }
    }

    // ================= VERTICES =================
    private void drawVertices(Graphics2D g) {

        for (int i = 0; i < graph.getVertexCount(); i++) {

            int x = graph.getVertexX(i);
            int y = graph.getVertexY(i);

            // color logic
            if (backtrack.contains(i)) {
                g.setColor(Color.RED); // backtracking
            } else if (isInPath(i)) {
                g.setColor(new Color(0, 100, 255)); // active path
            } else {
                g.setColor(new Color(120, 120, 120)); // unvisited
            }

            // bigger circle
            g.fillOval(x - 15, y - 15, 30, 30);

            // border
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(2));
            g.drawOval(x - 15, y - 15, 30, 30);

            // label
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 12));

            String text = String.valueOf(i);
            FontMetrics fm = g.getFontMetrics();

            g.drawString(
                text,
                x - fm.stringWidth(text) / 2,
                y + fm.getAscent() / 2
            );
        }
    }

    private boolean isInPath(int v) {
        for (int p : path) {
            if (p == v) return true;
        }
        return false;
    }

    // animation (optional)
    public void startAnimation() {}
    public void stopAnimation() {}
}