# Hamiltonian Cycle Delivery Route Planning System - Design Document

## Overview

The Hamiltonian Cycle Delivery Route Planning System is a Java-based solution that finds optimal delivery routes by solving the Hamiltonian cycle problem. The system uses a backtracking algorithm to explore all possible paths through a graph representing a delivery network with 20+ vertices (delivery points), ensuring each location is visited exactly once before returning to the depot.

The design emphasizes separation of concerns with distinct modules for graph representation, algorithm implementation, data initialization, and output formatting. This modular architecture enables independent testing, maintenance, and future enhancements.

## Architecture

### High-Level System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Main (Entry Point)                    │
│                    - Orchestrates workflow                   │
│                    - Coordinates all components              │
└────────────┬────────────────────────────────────────────────┘
             │
    ┌────────┴────────┬──────────────┬──────────────┐
    │                 │              │              │
    ▼                 ▼              ▼              ▼
┌─────────┐    ┌──────────┐   ┌────────────┐  ┌────────┐
│GraphData│    │  Graph   │   │Hamiltonian │  │Printer │
│         │    │          │   │  Cycle     │  │        │
│- Init   │    │- Matrix  │   │- Algorithm │  │-Output │
│- Data   │    │- Edges   │   │- Backtrack │  │-Format │
└─────────┘    └──────────┘   └────────────┘  └────────┘
```

### Module Responsibilities

**Graph Module**: Encapsulates the adjacency matrix representation and provides O(1) edge lookup operations.

**HamiltonianCycle Module**: Implements the backtracking algorithm to find valid cycles through the graph.

**GraphData Module**: Initializes the graph with 20+ vertices and guarantees at least one Hamiltonian cycle exists.

**Printer Module**: Formats and displays the discovered route in a user-friendly manner.

**Main Module**: Orchestrates the complete workflow from initialization through result display.

## Components and Interfaces

### Graph Class

**Purpose**: Represents the delivery network as an adjacency matrix and provides connectivity queries.

**Key Attributes**:
- `adjacencyMatrix`: 2D integer array where `matrix[i][j] = 1` if vertices i and j are connected, 0 otherwise
- `vertexCount`: Number of vertices in the graph (minimum 20)

**Key Methods**:
```java
public Graph(int vertexCount)
  // Initialize graph with given number of vertices
  // Creates n×n adjacency matrix initialized to 0

public void addEdge(int u, int v)
  // Add bidirectional edge between vertices u and v
  // Sets matrix[u][v] = 1 and matrix[v][u] = 1
  // Maintains symmetry property

public boolean hasEdge(int u, int v)
  // Check if edge exists between u and v in O(1) time
  // Returns matrix[u][v] == 1

public int getVertexCount()
  // Return total number of vertices

public int[][] getAdjacencyMatrix()
  // Return reference to adjacency matrix
```

**Design Decisions**:
- Adjacency matrix chosen over adjacency list for O(1) edge lookup, critical for backtracking algorithm
- Symmetric matrix enforces undirected graph property
- No self-loops (diagonal entries always 0)

### HamiltonianCycle Class

**Purpose**: Implements the backtracking algorithm to find a valid Hamiltonian cycle.

**Key Attributes**:
- `graph`: Reference to the Graph object
- `path`: Array storing the current route being explored
- `visited`: Boolean array tracking which vertices are in the current path
- `cycleFound`: Boolean flag indicating if a valid cycle has been discovered

**Key Methods**:
```java
public HamiltonianCycle(Graph graph)
  // Initialize solver with a graph instance

public int[] findCycle()
  // Main entry point for finding Hamiltonian cycle
  // Returns array containing the cycle path, or null if no cycle exists
  // Initiates backtracking from vertex 0

private boolean backtrack(int currentVertex, int depth)
  // Recursive backtracking function
  // Parameters:
  //   - currentVertex: Current position in the path
  //   - depth: Number of vertices visited so far
  // Returns true if a complete cycle is found

private boolean isValid(int vertex, int currentPos)
  // Validate if vertex can be added to current path
  // Checks: vertex not visited AND edge exists from current to vertex
  // Returns true if vertex is a valid next step

private boolean isCycleClosed()
  // Verify that last vertex connects back to first vertex
  // Returns true if adjacencyMatrix[path[n-1]][path[0]] == 1
```

**Algorithm Pseudocode**:
```
function findCycle():
  path = new array[vertexCount]
  visited = new boolean[vertexCount]
  path[0] = 0  // Start from depot (vertex 0)
  visited[0] = true
  
  if backtrack(0, 1):
    return path
  else:
    return null

function backtrack(currentVertex, depth):
  // Base case: all vertices visited
  if depth == vertexCount:
    // Check if cycle closes (last vertex connects to first)
    if hasEdge(path[depth-1], path[0]):
      return true
    else:
      return false
  
  // Try each adjacent unvisited vertex
  for nextVertex in 0 to vertexCount-1:
    if isValid(nextVertex, depth):
      path[depth] = nextVertex
      visited[nextVertex] = true
      
      if backtrack(nextVertex, depth + 1):
        return true
      
      // Backtrack: undo the choice
      visited[nextVertex] = false
  
  return false
```

**Design Decisions**:
- Backtracking chosen for systematic exploration of all possible paths
- Visited array prevents revisiting vertices in current path
- Early termination when complete cycle found
- Cycle closure verified before accepting solution

### GraphData Class

**Purpose**: Initializes the graph with 20+ vertices and ensures at least one Hamiltonian cycle exists.

**Key Methods**:
```java
public static Graph createDeliveryNetwork()
  // Create and return a graph with guaranteed Hamiltonian cycle
  // Minimum 20 vertices
  // Returns fully initialized Graph object

private static void buildConnectedGraph(Graph graph)
  // Build a connected graph structure
  // Ensures all vertices are reachable from vertex 0

private static void addHamiltonianCycle(Graph graph)
  // Add edges forming a guaranteed Hamiltonian cycle
  // Creates cycle: 0 -> 1 -> 2 -> ... -> n-1 -> 0

private static void addRandomEdges(Graph graph)
  // Add additional random edges to increase connectivity
  // Improves algorithm performance by providing more path options
```

**Graph Construction Strategy**:
1. Create graph with 20+ vertices
2. Add edges forming a guaranteed cycle: 0→1→2→...→n-1→0
3. Add additional random edges to increase connectivity
4. Verify cycle exists before returning

**Design Decisions**:
- Guaranteed cycle ensures algorithm always finds a solution
- Additional random edges provide realistic network topology
- Vertices numbered 0 to n-1 for consistency
- Symmetric edges maintain undirected graph property

### Printer Class

**Purpose**: Formats and displays the discovered Hamiltonian cycle in user-friendly format.

**Key Methods**:
```java
public static void printCycle(int[] path)
  // Display the complete delivery route
  // Parameters:
  //   - path: Array containing the cycle path
  // Output includes:
  //   - Header with success message
  //   - Delivery points in sequence with arrows
  //   - Cycle closure (return to start)
  //   - Total delivery points count
  //   - Footer confirming completion

public static void printError(String message)
  // Display error message when no cycle found
  // Parameters:
  //   - message: Error description

public static void printNoSolution()
  // Display message when algorithm cannot find cycle
```

**Output Format**:
```
========================================
Hamiltonian Cycle Found!
========================================

Delivery Route:
Delivery Point 0 -> Delivery Point 1 -> ... -> Delivery Point n-1 -> Delivery Point 0

========================================
Route completed successfully!
Total delivery points visited: n
========================================
```

**Design Decisions**:
- Clear visual separators for readability
- Explicit cycle closure shown in output
- Vertex count displayed for verification
- Success/failure messages clearly indicated

### Main Class

**Purpose**: Orchestrates the complete workflow from initialization through result display.

**Key Methods**:
```java
public static void main(String[] args)
  // Entry point for the application
  // Workflow:
  //   1. Initialize graph with delivery data
  //   2. Create HamiltonianCycle solver
  //   3. Execute algorithm to find cycle
  //   4. Display results or error message
  //   5. Handle exceptions gracefully
```

**Workflow**:
```
1. Create GraphData and initialize graph
2. Instantiate HamiltonianCycle with graph
3. Call findCycle() to search for solution
4. If cycle found:
     - Display cycle using Printer
   Else:
     - Display "No solution found" message
5. Handle any exceptions and display error
```

**Design Decisions**:
- Single entry point for simplicity
- Error handling prevents crashes
- Clear separation of concerns maintained
- Extensible for future enhancements

## Data Models

### Adjacency Matrix Representation

**Structure**: 2D integer array of size n×n where n ≥ 20

**Properties**:
- `matrix[i][j] = 1` if edge exists between vertices i and j
- `matrix[i][j] = 0` if no edge exists
- `matrix[i][i] = 0` (no self-loops)
- `matrix[i][j] = matrix[j][i]` (symmetric for undirected graph)

**Memory**: O(n²) space complexity

**Access**: O(1) time for edge lookup

### Path Array

**Structure**: Integer array of size n containing vertex indices

**Content**: Ordered sequence of vertices representing the delivery route

**Example**: `[0, 3, 1, 4, 2, 0]` represents cycle 0→3→1→4→2→0

### Visited Array

**Structure**: Boolean array of size n

**Purpose**: Track which vertices are in the current path during backtracking

**Usage**: Prevents revisiting vertices in the same path exploration

### Graph State

**Vertices**: Numbered 0 to n-1 (n ≥ 20)

**Edges**: Bidirectional connections representing roads between delivery points

**Connectivity**: Guaranteed to contain at least one Hamiltonian cycle

## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system—essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

### Property 1: Adjacency Matrix Symmetry

For any graph, the adjacency matrix must be symmetric: for all vertices i and j, `matrix[i][j] == matrix[j][i]`.

**Validates: Requirements 1.4**

### Property 2: No Self-Loops

For any graph, diagonal entries must be zero: for all vertices i, `matrix[i][i] == 0`.

**Validates: Requirements 1.5**

### Property 3: Minimum Vertex Count

For any initialized graph, the vertex count must be at least 20: `vertexCount >= 20`.

**Validates: Requirements 2.1, 2.2**

### Property 4: Sequential Vertex Numbering

For any graph with n vertices, vertices are numbered 0 to n-1 with no gaps or duplicates.

**Validates: Requirements 2.3**

### Property 5: Guaranteed Hamiltonian Cycle Existence

For any graph created by GraphData, the algorithm must find at least one valid Hamiltonian cycle.

**Validates: Requirements 3.1, 3.2**

### Property 6: Path Length Equals Vertex Count

For any found Hamiltonian cycle, the path length must equal the number of vertices: `path.length == vertexCount`.

**Validates: Requirements 5.5, 12.1**

### Property 7: No Vertex Repetition

For any found Hamiltonian cycle, each vertex appears exactly once in the path: `Set(path).size() == path.length`.

**Validates: Requirements 5.1, 5.2**

### Property 8: Consecutive Vertices Are Adjacent

For any found Hamiltonian cycle, all consecutive vertices must be connected: for all i in [0, n-1], `hasEdge(path[i], path[(i+1) % n]) == true`.

**Validates: Requirements 6.1, 6.5, 12.2**

### Property 9: Cycle Closure

For any found Hamiltonian cycle, the last vertex must connect back to the first: `hasEdge(path[n-1], path[0]) == true`.

**Validates: Requirements 7.1, 7.2, 12.3**

### Property 10: Complete Cycle Validation

For any found Hamiltonian cycle, all three constraints must hold simultaneously:
- All vertices visited exactly once
- All consecutive vertices are adjacent
- Cycle closes properly

**Validates: Requirements 10.3**

### Property 11: Algorithm Returns Valid Result or Null

For any graph, the algorithm returns either a valid Hamiltonian cycle or null (no solution).

**Validates: Requirements 9.5, 9.6**

### Property 12: Graph Accepts 20+ Vertices

For any vertex count n ≥ 20, the system can create and process a graph with n vertices without modification.

**Validates: Requirements 11.1, 11.2, 11.3, 11.4, 11.5**

### Property 13: Edge Existence Check in O(1)

For any graph, checking if an edge exists between two vertices completes in constant time using the adjacency matrix.

**Validates: Requirements 1.6**

### Property 14: Graph Initialization Methods Exist

For any Graph instance, methods exist to initialize the adjacency matrix and check edge existence.

**Validates: Requirements 14.2, 14.3**

### Property 15: HamiltonianCycle Accepts Graph Input

For any Graph instance, HamiltonianCycle can be instantiated with that graph and provides a method to find cycles.

**Validates: Requirements 15.2, 15.4**

### Property 16: GraphData Creates Valid Graph

For any graph created by GraphData, it contains at least 20 vertices and at least one Hamiltonian cycle.

**Validates: Requirements 16.2, 16.3**

### Property 17: Printer Accepts Path Input

For any valid path array, Printer can format and display it without errors.

**Validates: Requirements 17.2**

### Property 18: System Handles Invalid Input Gracefully

For any invalid input (null graph, empty graph, invalid path), the system displays an error message and does not crash.

**Validates: Requirements 18.1, 18.5**

### Property 19: Main Orchestrates Complete Workflow

For any valid graph, Main successfully initializes the graph, creates a solver, executes the algorithm, and displays results.

**Validates: Requirements 20.1, 20.2, 20.3, 20.4, 20.5**

### Property 20: Output Contains All Required Information

For any found Hamiltonian cycle, the output includes the complete path, cycle closure, vertex count, and success message.

**Validates: Requirements 4.1, 4.2, 4.3, 4.4, 13.4**

## Error Handling

### Invalid Graph Detection

**Scenario**: Graph is null or has fewer than 20 vertices

**Handling**:
```java
if (graph == null || graph.getVertexCount() < 20) {
  Printer.printError("Invalid graph: must have at least 20 vertices");
  return;
}
```

### No Cycle Found

**Scenario**: Algorithm completes without finding a Hamiltonian cycle

**Handling**:
```java
int[] cycle = solver.findCycle();
if (cycle == null) {
  Printer.printNoSolution();
  return;
}
```

### Invalid Path Validation

**Scenario**: Found path fails validation checks

**Handling**:
```java
if (!validateCycle(cycle, graph)) {
  Printer.printError("Invalid cycle: validation failed");
  return;
}
```

**Validation Checks**:
1. Path length equals vertex count
2. Each vertex appears exactly once
3. All consecutive vertices are adjacent
4. Last vertex connects to first vertex

### Exception Handling

**Scenario**: Unexpected runtime errors

**Handling**:
```java
try {
  // Main workflow
} catch (Exception e) {
  Printer.printError("System error: " + e.getMessage());
  e.printStackTrace();
}
```

## Testing Strategy

### Unit Testing Approach

**Graph Class Tests**:
- Test adjacency matrix initialization with various vertex counts
- Verify edge addition maintains symmetry
- Test edge existence checks return correct values
- Verify no self-loops are created
- Test with minimum (20) and larger vertex counts

**HamiltonianCycle Class Tests**:
- Test algorithm finds cycle in simple graphs
- Test algorithm returns null when no cycle exists
- Test path validation for found cycles
- Test with graphs of varying sizes (20-30 vertices)
- Test backtracking behavior with dead-end paths

**GraphData Class Tests**:
- Verify created graphs have at least 20 vertices
- Verify created graphs contain valid Hamiltonian cycles
- Test graph connectivity is sufficient
- Verify vertices are numbered 0 to n-1

**Printer Class Tests**:
- Test output formatting with various path sizes
- Verify output contains all required information
- Test error message display
- Verify output readability

**Main Class Tests**:
- Test complete workflow execution
- Test error handling for invalid inputs
- Test output display
- Test graceful failure scenarios

### Property-Based Testing Approach

**Testing Framework**: Use JUnit with property-based testing library (e.g., QuickCheck for Java)

**Property Test Configuration**:
- Minimum 100 iterations per property test
- Generate random graphs with 20-30 vertices
- Generate random valid paths for validation
- Tag each test with design property reference

**Property Test Examples**:

```java
// Property 1: Adjacency Matrix Symmetry
@Property
void testAdjacencyMatrixSymmetry(@ForAll Graph graph) {
  int[][] matrix = graph.getAdjacencyMatrix();
  for (int i = 0; i < matrix.length; i++) {
    for (int j = 0; j < matrix.length; j++) {
      assertEquals(matrix[i][j], matrix[j][i]);
    }
  }
}
// Feature: hamiltonian-cycle-delivery, Property 1: Adjacency Matrix Symmetry

// Property 7: No Vertex Repetition
@Property
void testNoVertexRepetition(@ForAll Graph graph) {
  HamiltonianCycle solver = new HamiltonianCycle(graph);
  int[] cycle = solver.findCycle();
  if (cycle != null) {
    Set<Integer> vertices = new HashSet<>(Arrays.asList(cycle));
    assertEquals(vertices.size(), cycle.length);
  }
}
// Feature: hamiltonian-cycle-delivery, Property 7: No Vertex Repetition

// Property 8: Consecutive Vertices Are Adjacent
@Property
void testConsecutiveVerticesAdjacent(@ForAll Graph graph) {
  HamiltonianCycle solver = new HamiltonianCycle(graph);
  int[] cycle = solver.findCycle();
  if (cycle != null) {
    for (int i = 0; i < cycle.length; i++) {
      int current = cycle[i];
      int next = cycle[(i + 1) % cycle.length];
      assertTrue(graph.hasEdge(current, next));
    }
  }
}
// Feature: hamiltonian-cycle-delivery, Property 8: Consecutive Vertices Are Adjacent
```

### Integration Testing

**End-to-End Workflow**:
1. Initialize graph with GraphData
2. Create HamiltonianCycle solver
3. Execute findCycle()
4. Validate result
5. Display using Printer
6. Verify output format

**Test Scenarios**:
- Successful cycle discovery and display
- Graceful handling when no cycle exists
- Error handling for invalid inputs
- Output formatting verification

### Test Coverage Goals

- **Unit Tests**: 80%+ code coverage
- **Property Tests**: All 20 correctness properties covered
- **Integration Tests**: Complete workflow validation
- **Edge Cases**: Minimum vertex count, maximum connectivity, sparse graphs

## Key Design Decisions

### 1. Adjacency Matrix vs. Adjacency List

**Decision**: Use adjacency matrix

**Rationale**:
- O(1) edge lookup critical for backtracking algorithm
- Backtracking requires frequent adjacency checks
- Graph is relatively small (20-30 vertices), so O(n²) space is acceptable
- Simplifies implementation and testing

### 2. Backtracking Algorithm

**Decision**: Implement recursive backtracking

**Rationale**:
- Systematic exploration of all possible paths
- Natural fit for Hamiltonian cycle problem
- Allows early termination when solution found
- Well-understood algorithm with clear correctness properties

### 3. Guaranteed Cycle in GraphData

**Decision**: Ensure at least one Hamiltonian cycle exists

**Rationale**:
- Simplifies testing and validation
- Ensures algorithm always finds a solution
- Realistic for delivery networks (well-connected)
- Allows focus on algorithm correctness rather than edge cases

### 4. Modular Architecture

**Decision**: Separate concerns into distinct classes

**Rationale**:
- Enables independent testing of each component
- Improves code maintainability
- Allows future enhancements without affecting other modules
- Follows SOLID principles

### 5. Cycle Closure Verification

**Decision**: Verify last vertex connects to first before accepting solution

**Rationale**:
- Ensures valid Hamiltonian cycle (not just Hamiltonian path)
- Critical for delivery route (must return to depot)
- Simple O(1) check using adjacency matrix

### 6. Visited Array for Backtracking

**Decision**: Maintain boolean array to track visited vertices

**Rationale**:
- Prevents revisiting vertices in current path
- O(1) lookup for visited status
- Simplifies backtracking logic
- Standard approach for backtracking algorithms

## Performance Characteristics

### Time Complexity

- **Edge Lookup**: O(1) using adjacency matrix
- **Backtracking Algorithm**: O(n!) worst-case (explores all permutations)
- **Cycle Validation**: O(n) (check all vertices and edges)
- **Overall**: O(n!) dominated by backtracking

### Space Complexity

- **Adjacency Matrix**: O(n²)
- **Path Array**: O(n)
- **Visited Array**: O(n)
- **Recursion Stack**: O(n) (maximum depth)
- **Overall**: O(n²)

### Practical Performance

- **Suitable for**: 20-30 vertices (completes in reasonable time)
- **Challenging for**: 40+ vertices (factorial growth becomes prohibitive)
- **Optimization**: Early termination when cycle found reduces average case

## Future Enhancements

1. **Heuristic Optimization**: Add branch-and-bound or other pruning strategies
2. **Multiple Solutions**: Find all Hamiltonian cycles, not just first one
3. **Weighted Edges**: Support edge weights for cost optimization
4. **Visualization**: Add graphical display of routes
5. **Parallel Processing**: Explore multiple branches concurrently
6. **Approximation Algorithms**: Implement faster approximation methods for larger graphs

## Conclusion

The Hamiltonian Cycle Delivery Route Planning System provides a clean, modular implementation of the classic Hamiltonian cycle problem. The design emphasizes correctness through comprehensive property-based testing, maintainability through separation of concerns, and clarity through well-defined interfaces and documentation. The backtracking algorithm systematically explores all possible routes while the adjacency matrix representation enables efficient connectivity queries. With guaranteed cycle existence in the generated graphs, the system reliably finds valid delivery routes for networks with 20+ delivery points.
