// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
public class Main {
   public Main() {
   }

   public static void main(String[] var0) {
      Graph var1 = GraphData.createDeliveryNetwork();
      if (var1 != null && var1.getVertexCount() >= 20) {
         System.out.println("Graph initialized with " + var1.getVertexCount() + " delivery points.\n");
         HamiltonianCycle var2 = new HamiltonianCycle(var1);
         System.out.println("Searching for Hamiltonian cycle...\n");
         int[] var3 = var2.findCycle();
         if (var3 != null) {
            if (var2.validateCycle(var3)) {
               Printer.printCycle(var3);
            } else {
               Printer.printError("Invalid cycle");
            }
         } else {
            Printer.printNoSolution();
         }

      } else {
         Printer.printError("Invalid graph");
      }
   }
}
