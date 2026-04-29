// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HamiltonianCycle {
   private final Graph graph;
   private final int[] path;
   private final boolean[] visited;
   private final int vertexCount;
   private List<int[]> allCycles;

   public HamiltonianCycle(Graph var1) {
      this.graph = var1;
      this.vertexCount = var1.getVertexCount();
      this.path = new int[this.vertexCount];
      this.visited = new boolean[this.vertexCount];
      this.allCycles = new ArrayList();
   }

   public int[] findCycle() {
      this.path[0] = 0;
      this.visited[0] = true;
      return this.backtrack(0, 1) ? this.path : null;
   }

   public List<int[]> findAllCycles() {
      this.allCycles.clear();
      this.path[0] = 0;
      this.visited[0] = true;
      this.backtrackAll(0, 1);
      return this.allCycles;
   }

   private boolean backtrack(int var1, int var2) {
      if (var2 == this.vertexCount) {
         return this.isCycleClosed(var1);
      } else {
         for(int var3 = 0; var3 < this.vertexCount; ++var3) {
            if (this.isValid(var3, var1)) {
               this.path[var2] = var3;
               this.visited[var3] = true;
               if (this.backtrack(var3, var2 + 1)) {
                  return true;
               }

               this.visited[var3] = false;
            }
         }

         return false;
      }
   }

   private void backtrackAll(int var1, int var2) {
      if (var2 == this.vertexCount) {
         if (this.isCycleClosed(var1)) {
            int[] var4 = (int[])this.path.clone();
            this.allCycles.add(var4);
         }

      } else {
         for(int var3 = 0; var3 < this.vertexCount; ++var3) {
            if (this.isValid(var3, var1)) {
               this.path[var2] = var3;
               this.visited[var3] = true;
               this.backtrackAll(var3, var2 + 1);
               this.visited[var3] = false;
            }
         }

      }
   }

   private boolean isValid(int var1, int var2) {
      return this.visited[var1] ? false : this.graph.hasEdge(var2, var1);
   }

   private boolean isCycleClosed(int var1) {
      return this.graph.hasEdge(var1, this.path[0]);
   }

   public boolean validateCycle(int[] var1) {
      if (var1 != null && var1.length == this.vertexCount) {
         HashSet var2 = new HashSet();

         for(int var6 : var1) {
            if (var6 < 0 || var6 >= this.vertexCount || var2.contains(var6)) {
               return false;
            }

            var2.add(var6);
         }

         for(int var7 = 0; var7 < var1.length; ++var7) {
            int var8 = var1[var7];
            int var9 = var1[(var7 + 1) % var1.length];
            if (!this.graph.hasEdge(var8, var9)) {
               return false;
            }
         }

         return this.graph.hasEdge(var1[var1.length - 1], var1[0]);
      } else {
         return false;
      }
   }
}
