# Bugfix Design Document

## Overview

Fix the Hamiltonian cycle finder to display ALL cycles instead of just one, using the existing `findAllCycles()` method.

## Technical Context

**Key Files:**
- `src/Main.java` - Entry point, needs to use `findAllCycles()` and print all paths
- `src/Printer.java` - Needs new method to print multiple cycles with labels
- `src/HamiltonianCycle.java` - Already has `findAllCycles()` method

**Current Flow:**
```
Main.findCycle() → Printer.printCycle() → Single path output
```

**Fixed Flow:**
```
Main.findAllCycles() → Printer.printAllCycles() → Multiple paths with labels
```

## Implementation Plan

### 1. Update Printer.java

Add `printAllCycles()` method to display multiple cycles with labels:

```java
public static void printAllCycles(List<int[]> cycles) {
    if (cycles.isEmpty()) {
        printNoSolution();
        return;
    }
    
    System.out.println("========================================");
    System.out.println("Hamiltonian Cycles Found: " + cycles.size());
    System.out.println("========================================\n");
    
    for (int i = 0; i < cycles.size(); i++) {
        System.out.println("path-" + (i + 1) + ":");
        int[] path = cycles.get(i);
        for (int j = 0; j < path.length; j++) {
            System.out.print("Delivery Point " + path[j] + " -> ");
        }
        System.out.println("Delivery Point " + path[0]);
        System.out.println();
    }
    
    System.out.println("========================================");
    System.out.println("All routes completed successfully!");
    System.out.println("========================================");
}
```

### 2. Update Main.java

Change from `findCycle()` to `findAllCycles()`:

```java
Graph graph = GraphData.createDeliveryNetwork();
HamiltonianCycle solver = new HamiltonianCycle(graph);
List<int[]> cycles = solver.findAllCycles();

if (!cycles.isEmpty()) {
    Printer.printAllCycles(cycles);
} else {
    Printer.printNoSolution();
}
```

### 3. Graph Visualization

The existing `GraphPanel` already accepts a cycle array. For multiple cycles, we have options:
- Show all cycles overlaid on the graph
- Add navigation to switch between cycles
- Keep visualization for the first cycle only (simpler)

**Decision:** Keep visualization showing the first cycle for simplicity.

## Bug Condition

**C(X):** The bug condition is always met when `findAllCycles()` returns multiple cycles but only one is printed.

**Fix Checking Property:** All cycles returned by `findAllCycles()` must be printed with labels "path-1", "path-2", etc.

**Preservation Goal:** When no cycles exist, "No Solution Found" must still be printed.

## Testing Strategy

1. **Fix Checking:** Verify all cycles are printed with correct labels
2. **Preservation Checking:** Verify "No Solution Found" still works for graphs without cycles
3. **Edge Cases:** Test with graphs that have 0, 1, and multiple cycles