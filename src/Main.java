import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.util.Random;

public class Main {

    private static final Color C_BG       = new Color(12, 18, 38);
    private static final Color C_PANEL    = new Color(18, 26, 52);
    private static final Color C_BORDER   = new Color(40, 60, 110);
    private static final Color C_TEXT     = new Color(180, 200, 240);

    public static void main(String[] args) {
        // Use system look-and-feel as base, then override colors
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}

        Graph graph = GraphData.createDeliveryNetwork();
        // width=800, height=680, topMargin=105 (clears HUD bar), sideMargin=55
        graph.generateRandomCoordinates(800, 680, 105, 55, new Random());

        SwingUtilities.invokeLater(() -> buildAndShowUI(graph));
    }

    private static void buildAndShowUI(Graph graph) {

        JFrame frame = new JFrame("Hamiltonian Cycle  —  Step-by-Step Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(0, 0));
        frame.getContentPane().setBackground(C_BG);


        GraphPanel graphPanel = new GraphPanel(graph, null);
        graphPanel.setPreferredSize(new Dimension(800, 680));

     
        JPanel titleBar = buildTitleBar();


        JPanel controls = buildControlPanel(graph, graphPanel);

        frame.add(titleBar,   BorderLayout.NORTH);
        frame.add(graphPanel, BorderLayout.CENTER);
        frame.add(controls,   BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(700, 600));
        frame.setVisible(true);
    }

    private static JPanel buildTitleBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(8, 12, 28));
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, C_BORDER));
        bar.setPreferredSize(new Dimension(0, 42));

        JLabel title = new JLabel("  Hamiltonian Cycle  —  Backtracking Visualization");
        title.setForeground(new Color(140, 180, 255));
        title.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel sub = new JLabel("AI Course Project  ");
        sub.setForeground(new Color(80, 100, 150));
        sub.setFont(new Font("Arial", Font.PLAIN, 12));

        bar.add(title, BorderLayout.WEST);
        bar.add(sub,   BorderLayout.EAST);
        return bar;
    }


    private static JPanel buildControlPanel(Graph graph, GraphPanel graphPanel) {

        GraphPanelCallback[] cbHolder    = {null};
        Thread[]             solverThread = {null};


        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(C_BG);
        wrapper.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, C_BORDER));


        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 10));
        row.setBackground(C_PANEL);
        row.setBorder(new EmptyBorder(4, 16, 4, 16));

        JButton playBtn  = makeButton("  Play  ",  new Color(34, 139, 68),  new Color(50, 180, 90));
        JButton pauseBtn = makeButton(" Pause  ",  new Color(160, 110, 20), new Color(210, 150, 30));
        JButton stepBtn  = makeButton("  Step  ",  new Color(30,  90, 180), new Color(60, 130, 220));
        JButton resetBtn = makeButton(" Reset  ",  new Color(140,  40, 40), new Color(190,  60, 60));

        pauseBtn.setEnabled(false);
        stepBtn.setEnabled(false);

 
        JSeparator sep = new JSeparator(JSeparator.VERTICAL);
        sep.setPreferredSize(new Dimension(1, 30));
        sep.setForeground(C_BORDER);

   
        JLabel speedLabel = styledLabel("Speed");
        JSlider speedSlider = buildSlider();

        JLabel speedValue = styledLabel("x5");
        speedValue.setForeground(new Color(120, 180, 255));
        speedValue.setPreferredSize(new Dimension(28, 20));

        speedSlider.addChangeListener(e -> {
            int v = speedSlider.getValue();
            speedValue.setText("x" + v);
            if (cbHolder[0] != null) cbHolder[0].setDelay(sliderToDelay(v));
        });

        playBtn.addActionListener(e -> {
            if (cbHolder[0] == null || !isAlive(solverThread[0])) {
                GraphPanelCallback cb = new GraphPanelCallback(graphPanel);
                cb.setDelay(sliderToDelay(speedSlider.getValue()));
                cbHolder[0] = cb;

                solverThread[0] = new Thread(() -> {
                    HamiltonianCycle solver = new HamiltonianCycle(graph);
                    int[] cycle = solver.findCycleWithVisualization(cb);
                    if (cycle != null) Printer.printCycle(cycle);
                    else               Printer.printNoSolution();

                    SwingUtilities.invokeLater(() -> {
                        playBtn.setEnabled(true);
                        pauseBtn.setEnabled(false);
                        stepBtn.setEnabled(false);
                    });
                }, "solver-thread");
                solverThread[0].setDaemon(true);
                solverThread[0].start();
            } else {
                cbHolder[0].play();
            }
            playBtn.setEnabled(false);
            pauseBtn.setEnabled(true);
            stepBtn.setEnabled(true);
        });

        pauseBtn.addActionListener(e -> {
            if (cbHolder[0] != null) cbHolder[0].pause();
            playBtn.setEnabled(true);
            pauseBtn.setEnabled(false);
        });

        stepBtn.addActionListener(e -> {
            if (cbHolder[0] != null) {
                if (!cbHolder[0].isPaused()) {
                    cbHolder[0].pause();
                    playBtn.setEnabled(true);
                    pauseBtn.setEnabled(false);
                }
                cbHolder[0].step();
            }
        });

        resetBtn.addActionListener(e -> {
            if (solverThread[0] != null) solverThread[0].interrupt();
            cbHolder[0]    = null;
            solverThread[0] = null;

            graph.generateRandomCoordinates(800, 680, 105, 55, new Random());
            graphPanel.updateVisualizationState(
                new int[0], new java.util.HashSet<>(), 0,
                0, "Press Play to start...", -1, -1);

            playBtn.setEnabled(true);
            pauseBtn.setEnabled(false);
            stepBtn.setEnabled(false);
        });

        row.add(playBtn);
        row.add(pauseBtn);
        row.add(stepBtn);
        row.add(resetBtn);
        row.add(sep);
        row.add(speedLabel);
        row.add(speedSlider);
        row.add(speedValue);

        wrapper.add(row, BorderLayout.CENTER);
        return wrapper;
    }



    private static JButton makeButton(String text, Color base, Color hover) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover() ? hover : base;
                GradientPaint gp = new GradientPaint(0, 0, bg.brighter(), 0, getHeight(), bg.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(hover.brighter());
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setBorder(new EmptyBorder(7, 16, 7, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private static JLabel styledLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(C_TEXT);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        return l;
    }

    private static JSlider buildSlider() {
        JSlider s = new JSlider(1, 10, 5);
        s.setBackground(C_PANEL);
        s.setForeground(C_TEXT);
        s.setPreferredSize(new Dimension(140, 32));
        s.setMajorTickSpacing(3);
        s.setPaintTicks(true);
        s.setToolTipText("Simulation speed");
        return s;
    }



    private static long sliderToDelay(int v) {
        // v=1 → 1500ms, v=10 → 50ms
        return (long) (1500 - (v - 1) * (1450.0 / 9));
    }

    private static boolean isAlive(Thread t) {
        return t != null && t.isAlive();
    }
}
