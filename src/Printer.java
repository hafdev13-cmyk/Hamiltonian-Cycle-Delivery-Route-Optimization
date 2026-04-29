public class Printer {

    public static void printCycle(int[] cycle) {

        System.out.println("\nHamiltonian Cycle:");

        for (int v : cycle) {
            System.out.print(v + " -> ");
        }

        System.out.println(cycle[0]);
    }

    public static void printNoSolution() {
        System.out.println("No cycle found.");
    }
}
