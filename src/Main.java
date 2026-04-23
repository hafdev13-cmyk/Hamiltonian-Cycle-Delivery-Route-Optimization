import javax.swing.*;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Graph graph = GraphData.createDeliveryNetwork();
        graph.generateRandomCoordinates(500, 500, new Random());

        JFrame frame = new JFrame("Hamiltonian Cycle");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphPanel panel = new GraphPanel(graph, null);
        frame.add(panel);
        frame.setVisible(true);

        new Thread(() -> {

            HamiltonianCycle solver = new HamiltonianCycle(graph);
            GraphPanelCallback cb = new GraphPanelCallback(panel);

            int[] cycle = solver.findCycleWithVisualization(cb);

            if (cycle != null)
                Printer.printCycle(cycle);
            else
                Printer.printNoSolution();

        }).start();
    }
}