import java.awt.*;
import java.awt.geom.*;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;


public class GraphPanel extends JPanel {

    private static final int   NODE_R   = 18;   // node radius
    private static final Color BG_TOP   = new Color(10,  14,  30);
    private static final Color BG_BOT   = new Color(18,  26,  52);

    private final Graph graph;
    private int[] path = new int[0];
    private Set<Integer> backtrackSet = new HashSet<>();
    private int distance = 0;

    private int    stepCount     = 0;
    private String statusMessage = "Press Play to start...";
    private int    currentFrom   = -1;
    private int    currentTo     = -1;
    private boolean cycleFound   = false;

    // Pulse animation for the current edge
    private float pulsePhase = 0f;
    private Timer pulseTimer;

    public GraphPanel(Graph graph, int[] cycle) {
        this.graph = graph;
        this.path  = (cycle != null) ? cycle : new int[0];
        setBackground(BG_TOP);
        startPulse();
    }

    // ── Pulse timer (repaint ~30 fps for the animated edge) ──────────
    private void startPulse() {
        pulseTimer = new Timer(33, e -> {
            pulsePhase = (pulsePhase + 0.12f) % (2f * (float) Math.PI);
            if (currentFrom >= 0) repaint();
        });
        pulseTimer.start();
    }

    // ── Public state update methods ───────────────────────────────────

    public void updateVisualizationState(int[] path, Set<Integer> back, int dist,
                                          int stepCount, String status,
                                          int edgeFrom, int edgeTo) {
        this.path         = path;
        this.backtrackSet = back;
        this.distance     = dist;
        this.stepCount    = stepCount;
        this.statusMessage = status;
        this.currentFrom  = edgeFrom;
        this.currentTo    = edgeTo;
        this.cycleFound   = false;
        repaint();
    }

    public void showCycleFound(int[] cycle, int dist, int stepCount) {
        this.path         = cycle;
        this.backtrackSet = new HashSet<>();
        this.distance     = dist;
        this.stepCount    = stepCount;
        this.statusMessage = "Hamiltonian Cycle FOUND!";
        this.currentFrom  = -1;
        this.currentTo    = -1;
        this.cycleFound   = true;
        repaint();
    }

    public void showNoSolution(int stepCount) {
        this.path         = new int[0];
        this.backtrackSet = new HashSet<>();
        this.distance     = 0;
        this.stepCount    = stepCount;
        this.statusMessage = "No Hamiltonian Cycle exists.";
        this.currentFrom  = -1;
        this.currentTo    = -1;
        this.cycleFound   = false;
        repaint();
    }

    // Legacy compat
    public void updateVisualizationState(int[] path, Set<Integer> back, int dist) {
        updateVisualizationState(path, back, dist, stepCount, statusMessage, -1, -1);
    }

    // ── Paint ─────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,        RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,           RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,   RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,      RenderingHints.VALUE_STROKE_PURE);

        drawBackground(g2);
        drawGridDots(g2);
        drawEdges(g2);
        drawCurrentEdge(g2);
        drawPath(g2);
        drawVertices(g2);
        drawHUD(g2);
    }

    // ── Background: vertical gradient + subtle vignette ──────────────
    private void drawBackground(Graphics2D g) {
        int w = getWidth(), h = getHeight();
        GradientPaint bg = new GradientPaint(0, 0, BG_TOP, 0, h, BG_BOT);
        g.setPaint(bg);
        g.fillRect(0, 0, w, h);

        // Vignette (dark edges)
        RadialGradientPaint vignette = new RadialGradientPaint(
            new Point2D.Float(w / 2f, h / 2f),
            Math.max(w, h) * 0.72f,
            new float[]{0f, 1f},
            new Color[]{new Color(0, 0, 0, 0), new Color(0, 0, 0, 120)}
        );
        g.setPaint(vignette);
        g.fillRect(0, 0, w, h);
    }

    // ── Subtle dot grid ───────────────────────────────────────────────
    private void drawGridDots(Graphics2D g) {
        g.setColor(new Color(255, 255, 255, 18));
        int step = 40;
        for (int x = step; x < getWidth(); x += step) {
            for (int y = step; y < getHeight(); y += step) {
                g.fillOval(x - 1, y - 1, 2, 2);
            }
        }
    }

    // ── All graph edges (unexplored — bold and clearly visible) ──────
    private void drawEdges(Graphics2D g) {
        int[][] m = graph.getAdjacencyMatrix();
        int n = graph.getVertexCount();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (m[i][j] != 1) continue;

                int x1 = graph.getVertexX(i), y1 = graph.getVertexY(i);
                int x2 = graph.getVertexX(j), y2 = graph.getVertexY(j);

                // Wide glow halo — fully opaque, wide enough to see easily
                g.setColor(new Color(80, 130, 255, 80));
                g.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.drawLine(x1, y1, x2, y2);

                // Core line — bright white-blue, fully opaque, thick
                g.setColor(new Color(180, 210, 255, 255));
                g.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }

    // ── Current edge being explored: pulsing orange glow ─────────────
    private void drawCurrentEdge(Graphics2D g) {
        if (currentFrom < 0 || currentTo < 0) return;

        int x1 = graph.getVertexX(currentFrom), y1 = graph.getVertexY(currentFrom);
        int x2 = graph.getVertexX(currentTo),   y2 = graph.getVertexY(currentTo);

        float pulse = 0.55f + 0.45f * (float) Math.sin(pulsePhase);

        // Outer glow
        g.setColor(new Color(1f, 0.6f, 0f, 0.25f * pulse));
        g.setStroke(new BasicStroke(14f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(x1, y1, x2, y2);

        // Mid glow
        g.setColor(new Color(1f, 0.7f, 0.1f, 0.5f * pulse));
        g.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(x1, y1, x2, y2);

        // Core dashed line
        g.setColor(new Color(255, 200, 60, (int)(220 * pulse)));
        g.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                0, new float[]{9, 6}, pulsePhase * 6));
        g.drawLine(x1, y1, x2, y2);
    }

    // ── Active path: glowing blue / green ────────────────────────────
    private void drawPath(Graphics2D g) {
        if (path.length < 2) return;

        Color glowColor = cycleFound ? new Color(0, 230, 100) : new Color(60, 140, 255);
        Color coreColor = cycleFound ? new Color(80, 255, 140) : new Color(100, 180, 255);

        for (int i = 0; i < path.length - 1; i++) {
            drawGlowLine(g, path[i], path[i + 1], glowColor, coreColor);
        }

        if (cycleFound && path.length == graph.getVertexCount()) {
            drawGlowLine(g, path[path.length - 1], path[0], glowColor, coreColor);
        }
    }

    private void drawGlowLine(Graphics2D g, int a, int b, Color glow, Color core) {
        int x1 = graph.getVertexX(a), y1 = graph.getVertexY(a);
        int x2 = graph.getVertexX(b), y2 = graph.getVertexY(b);

        // Outer glow
        g.setColor(new Color(glow.getRed(), glow.getGreen(), glow.getBlue(), 50));
        g.setStroke(new BasicStroke(12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(x1, y1, x2, y2);

        // Mid glow
        g.setColor(new Color(glow.getRed(), glow.getGreen(), glow.getBlue(), 100));
        g.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(x1, y1, x2, y2);

        // Core
        g.setColor(core);
        g.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(x1, y1, x2, y2);
    }

    // ── Vertices ──────────────────────────────────────────────────────
    private void drawVertices(Graphics2D g) {
        int n = graph.getVertexCount();
        for (int i = 0; i < n; i++) {
            int x = graph.getVertexX(i);
            int y = graph.getVertexY(i);

            // Determine state colors
            Color inner, outer, ring;
            if (cycleFound && isInPath(i)) {
                inner = new Color(40, 200, 100);
                outer = new Color(10,  80,  40);
                ring  = new Color(80, 255, 140);
            } else if (i == currentTo) {
                inner = new Color(255, 180,  40);
                outer = new Color(120,  70,   0);
                ring  = new Color(255, 220, 100);
            } else if (backtrackSet.contains(i)) {
                inner = new Color(220,  60,  60);
                outer = new Color( 90,  20,  20);
                ring  = new Color(255, 100, 100);
            } else if (isInPath(i)) {
                inner = new Color( 50, 130, 255);
                outer = new Color( 10,  40, 120);
                ring  = new Color(120, 190, 255);
            } else {
                inner = new Color( 55,  65,  95);
                outer = new Color( 20,  25,  45);
                ring  = new Color( 90, 110, 160);
            }

            // Drop shadow
            g.setColor(new Color(0, 0, 0, 80));
            g.fillOval(x - NODE_R + 3, y - NODE_R + 4, NODE_R * 2, NODE_R * 2);

            // Outer ring glow
            g.setColor(new Color(ring.getRed(), ring.getGreen(), ring.getBlue(), 60));
            g.fillOval(x - NODE_R - 4, y - NODE_R - 4, (NODE_R + 4) * 2, (NODE_R + 4) * 2);

            // Radial gradient fill
            RadialGradientPaint grad = new RadialGradientPaint(
                new Point2D.Float(x - NODE_R / 3f, y - NODE_R / 3f),
                NODE_R * 1.2f,
                new float[]{0f, 1f},
                new Color[]{inner, outer}
            );
            g.setPaint(grad);
            g.fillOval(x - NODE_R, y - NODE_R, NODE_R * 2, NODE_R * 2);

            // Ring border
            g.setColor(ring);
            g.setStroke(new BasicStroke(1.8f));
            g.drawOval(x - NODE_R, y - NODE_R, NODE_R * 2, NODE_R * 2);

            // Specular highlight (top-left shine)
            g.setColor(new Color(255, 255, 255, 55));
            g.fillOval(x - NODE_R / 2 - 3, y - NODE_R / 2 - 3, NODE_R / 2, NODE_R / 2);

            // Label
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 11));
            String label = String.valueOf(i);
            FontMetrics fm = g.getFontMetrics();
            g.drawString(label, x - fm.stringWidth(label) / 2, y + fm.getAscent() / 2 - 1);
        }
    }

    // ── HUD overlay ───────────────────────────────────────────────────
    private void drawHUD(Graphics2D g) {
        drawInfoBar(g);
        drawLegend(g);
    }

    private void drawInfoBar(Graphics2D g) {
        int w = getWidth();
        int barH = 78;

        // Frosted glass background
        g.setColor(new Color(8, 14, 35, 200));
        g.fillRoundRect(10, 10, w - 20, barH, 14, 14);

        // Accent left border
        Color accent = cycleFound ? new Color(60, 220, 100) : new Color(80, 140, 255);
        g.setColor(accent);
        g.setStroke(new BasicStroke(3f));
        g.drawLine(14, 16, 14, 10 + barH - 6);

        // Status text
        Color statusColor = cycleFound ? new Color(80, 255, 130) : new Color(255, 220, 80);
        g.setColor(statusColor);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(statusMessage, 24, 32);

        // Stats row
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(new Color(160, 180, 220));
        g.drawString("Steps: " + stepCount, 24, 52);
        g.drawString("|", 100, 52);
        g.drawString("Distance: " + distance, 112, 52);

        // Path row
        if (path.length > 0) {
            StringBuilder sb = new StringBuilder("Path:  ");
            for (int i = 0; i < path.length; i++) {
                sb.append(path[i]);
                if (i < path.length - 1) sb.append(" > ");
            }
            if (cycleFound) sb.append(" > ").append(path[0]);
            String pathStr = sb.toString();
            g.setFont(new Font("Arial", Font.PLAIN, 11));
            FontMetrics fm = g.getFontMetrics();
            int maxW = w - 40;
            while (fm.stringWidth(pathStr) > maxW && pathStr.length() > 12) {
                pathStr = pathStr.substring(0, pathStr.length() - 1);
            }
            if (!sb.toString().equals(pathStr)) pathStr += "...";
            g.setColor(new Color(140, 200, 255));
            g.drawString(pathStr, 24, 70);
        }
    }

    private void drawLegend(Graphics2D g) {
        int lw = 162, lh = 140;   // taller to fit edge entry
        int lx = getWidth()  - lw - 12;
        int ly = getHeight() - lh - 12;

        // Background
        g.setColor(new Color(8, 14, 35, 200));
        g.fillRoundRect(lx, ly, lw, lh, 12, 12);

        // Title
        g.setFont(new Font("Arial", Font.BOLD, 11));
        g.setColor(new Color(160, 180, 220));
        g.drawString("LEGEND", lx + 10, ly + 16);

        // Separator
        g.setColor(new Color(60, 80, 130));
        g.setStroke(new BasicStroke(1f));
        g.drawLine(lx + 8, ly + 20, lx + lw - 8, ly + 20);

        Object[][] rows = {
            {new Color( 50, 130, 255), new Color(120, 190, 255), "In current path"},
            {new Color(255, 180,  40), new Color(255, 220, 100), "Being explored"},
            {new Color(220,  60,  60), new Color(255, 100, 100), "Backtracked"},
            {new Color( 40, 200, 100), new Color( 80, 255, 140), "Cycle found"},
            {new Color( 55,  65,  95), new Color( 90, 110, 160), "Unvisited"},
        };

        g.setFont(new Font("Arial", Font.PLAIN, 11));
        for (int i = 0; i < rows.length; i++) {
            int ry = ly + 32 + i * 18;
            Color inner = (Color) rows[i][0];
            Color ring  = (Color) rows[i][1];

            // Mini node
            RadialGradientPaint grad = new RadialGradientPaint(
                new Point2D.Float(lx + 16, ry - 3),
                7f,
                new float[]{0f, 1f},
                new Color[]{inner, inner.darker()}
            );
            g.setPaint(grad);
            g.fillOval(lx + 10, ry - 8, 12, 12);
            g.setColor(ring);
            g.setStroke(new BasicStroke(1.2f));
            g.drawOval(lx + 10, ry - 8, 12, 12);

            g.setColor(new Color(200, 215, 240));
            g.drawString((String) rows[i][2], lx + 28, ry);
        }

        // Unexplored edge entry (line sample, not a node)
        int ey = ly + 32 + rows.length * 18 + 4;
        g.setColor(new Color(80, 130, 255, 80));
        g.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(lx + 10, ey - 3, lx + 22, ey - 3);
        g.setColor(new Color(180, 210, 255, 255));
        g.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(lx + 10, ey - 3, lx + 22, ey - 3);
        g.setColor(new Color(200, 215, 240));
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.drawString("Unexplored edge", lx + 28, ey);
    }

    // ── Helpers ───────────────────────────────────────────────────────
    private boolean isInPath(int v) {
        for (int p : path) if (p == v) return true;
        return false;
    }

    public void startAnimation() {}
    public void stopAnimation() {}
}
