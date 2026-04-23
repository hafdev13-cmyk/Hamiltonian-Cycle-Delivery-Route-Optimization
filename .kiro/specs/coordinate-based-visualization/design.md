# Design Document: Coordinate-Based Visualization

## Overview

This design refactors the Hamiltonian Cycle visualization system to replace the circular layout with a coordinate-based approach and adds real-time step-by-step algorithm visualization. The current implementation displays only the final result using a circular vertex arrangement. This enhancement transforms the visualization into an interactive learning tool that shows the backtracking algorithm's behavior as it explores paths, backtracks from dead ends, and ultimately finds a Hamiltonian cycle.

The design maintains the existing O(n!) time complexity of the backtracking algorithm while adding visualization hooks that update the UI at each step. The coordinate-based layout simulates a realistic city map, making the delivery route planning context more intuitive.

### Key Design Goals

1. Replace circular layout with coordinate-based positioning (x, y coordinates)
2. Add real-time visualization of algorithm progress using javax.swing.Timer
3. Implement visual state indicators (gray/blue/red) for vertices
4. Display cumulative distance tracking during path exploration
5. Maintain UI responsiveness during algorithm execution
6. Preserve existing algorithm correctness and time complexity

## Architecture

### Component Overview

The system consists of three primary components that will be refactored:

1. **Graph** - Data model storing vertices, edges, and coordinates
2. **HamiltonianCycle** - Algorithm engine with visualization callbacks
3. **GraphPanel** - Rendering component with animation support

### Architectural Changes

**Current Architecture:**
```
Main → HamiltonianCycle.findCycle() → returns int[]
    → GraphPanel(graph, cycle) → renders final result
```

**New Architecture:**
```
Main → HamiltonianCycle.findCycleWithVisualization(callback)
    ↓
    Algorithm notifies callback at each step
    ↓
    GraphPanel receives updates via callback
    ↓
    Timer schedules repaint() calls
    ↓
    UI updates in real-time
```

### Threading Model

- **Main Thread**: Initializes components, creates GUI
- **Swing EDT (Event Dispatch Thread)**: Handles all UI rendering and updates
- **Timer Thread**: Triggers periodic repaints during algorithm execution
- **Algorithm Execution**: Runs on a background thread (SwingWorker) to prevent UI blocking

The algorithm will use `SwingUtilities.invokeLater()` to marshal state updates to the EDT, ensuring thread-safe UI updates.

### Data Flow

```
1. Graph initialization with coordinates
   ↓
2. GraphPanel created with empty path
   ↓
3. Algorithm starts in background thread
   ↓
4. Each backtrack step → callback → update GraphPanel state
   ↓
5. Timer triggers repaint() → renders current state
   ↓
6. Algorithm completes → final state displayed
```

## Components and Interfaces

### Graph Class Modifications

**New Fields:**
```java
private final int[] vertexX;  // X-coordinates for each vertex
private final int[] vertexY;  // Y-coordinates for each vertex
private final int[][] distances;  // Euclidean distances between vertices
```

**New Methods:**
```java
public void setVertexPosition(int vertex, int x, int y)
public int getVertexX(int vertex)
public int getVertexY(int vertex)
public int getDistance(int u, int v)
public void generateRandomCoordinates(int width, int height, Random random)
public void generateGridCoordinates(int width, int height)
```

**Coordinate Generation Strategy:**
- Random distribution: Use `Random` to generate unique (x, y) pairs within bounds
- Grid distribution: Calculate grid positions based on sqrt(n) layout
- Collision detection: Ensure no two vertices share the same coordinates
- Distance calculation: Compute Euclidean distance and store in distance matrix

### HamiltonianCycle Class Modifications

**New Interface:**
```java
public interface VisualizationCallback {
    void onVertexVisited(int vertex, int depth, int[] currentPath, int distance);
    void onBacktrack(int vertex, int depth);
    void onCycleFound(int[] cycle, int totalDistance);
    void onSearchComplete(boolean found);
}
```

**New Fields:**
```java
private VisualizationCallback callback;
private int currentDistance;
```

**Modified Methods:**
```java
public int[] findCycleWithVisualization(VisualizationCallback callback)
private boolean backtrackWithVisualization(int currentVertex, int depth)
```

**Algorithm Integration:**
- Insert callback invocations at key points in backtrack() method
- Calculate cumulative distance as path grows
- Notify callback before and after each recursive call
- Maintain existing algorithm logic without modification to correctness

### GraphPanel Class Modifications

**New Fields:**
```java
private int[] currentPath;  // Current path being explored
private Set<Integer> backtrackVertices;  // Vertices in backtrack state
private int currentDistance;  // Total distance of current path
private Timer animationTimer;  // Controls visualization speed
private boolean isAnimating;  // Animation state flag
```

**New Methods:**
```java
public void updateVisualizationState(int[] path, Set<Integer> backtrackSet, int distance)
public void startAnimation()
public void stopAnimation()
private Color getVertexColor(int vertex)
private void drawDistanceLabel(Graphics2D g2d)
private void drawDepotMarker(Graphics2D g2d, int vertex)
```

**Rendering Logic:**
- Remove all circular layout calculations (sin/cos)
- Use Graph.getVertexX() and Graph.getVertexY() for positioning
- Apply color based on vertex state:
  - Gray: Not visited
  - Blue: In current path
  - Red: In backtrack state
- Draw depot (vertex 0) with distinct marker (e.g., star icon or green color)
- Display distance label in top-right corner

### Main Class Modifications

**New Structure:**
```java
public static void main(String[] args) {
    Graph graph = GraphData.createDeliveryNetwork();
    graph.generateRandomCoordinates(500, 500, new Random());
    
    GraphPanel panel = new GraphPanel(graph, null);
    JFrame frame = createFrame(panel);
    
    HamiltonianCycle solver = new HamiltonianCycle(graph);
    
    SwingWorker<int[], Void> worker = new SwingWorker<>() {
        @Override
        protected int[] doInBackground() {
            return solver.findCycleWithVisualization(
                new VisualizationCallbackImpl(panel)
            );
        }
        
        @Override
        protected void done() {
            panel.stopAnimation();
        }
    };
    
    worker.execute();
}
```

## Data Models

### Vertex Coordinate Model

```java
class VertexCoordinate {
    int x;  // X-coordinate in pixel space (0-500)
    int y;  // Y-coordinate in pixel space (0-500)
}
```

Stored as parallel arrays in Graph class for memory efficiency.

### Visualization State Model

```java
class VisualizationState {
    int[] currentPath;           // Vertices in current exploration path
    Set<Integer> backtrackSet;   // Vertices currently being backtracked
    int currentDistance;         // Cumulative distance of current path
    boolean isComplete;          // Whether algorithm has finished
}
```

Maintained in GraphPanel and updated via callback.

### Distance Matrix

```java
int[][] distances;  // distances[i][j] = Euclidean distance from i to j
```

Calculated once during coordinate generation:
```
distance = sqrt((x2 - x1)² + (y2 - y1)²)
```

## Correctness Properties


*A property is a characteristic or behavior that should hold true across all valid executions of a system—essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

### Property 1: Coordinate Uniqueness

*For any* graph with coordinate-based layout, no two vertices should occupy the same (x, y) coordinate pair.

**Validates: Requirements 1.1, 1.2, 7.3**

### Property 2: Coordinate Space Coverage

*For any* graph with n vertices where n ≥ 4, the vertices should be distributed such that at least two different quadrants of the coordinate space contain vertices.

**Validates: Requirements 1.3**

### Property 3: Visualization Updates on Vertex Visit

*For any* vertex visited during algorithm execution, the visualization callback should be invoked with the updated path state, and the GraphPanel's current path should reflect this change.

**Validates: Requirements 2.1**

### Property 4: Visualization Updates on Backtrack

*For any* backtrack operation during algorithm execution, the visualization callback should be invoked with the backtrack state, and the GraphPanel's backtrack set should be updated accordingly.

**Validates: Requirements 2.3**

### Property 5: Unvisited Vertex Color

*For any* vertex not in the current path and not in the backtrack set, the getVertexColor() method should return gray.

**Validates: Requirements 3.1**

### Property 6: Active Path Vertex Color

*For any* vertex in the current active path, the getVertexColor() method should return blue.

**Validates: Requirements 3.2**

### Property 7: Backtrack Vertex Color

*For any* vertex in the backtrack set, the getVertexColor() method should return red.

**Validates: Requirements 3.3**

### Property 8: Distance Increases with Path Growth

*For any* path extension where a vertex is added to the current path, the total distance should increase by the edge distance between the last vertex and the newly added vertex.

**Validates: Requirements 5.2**

### Property 9: Distance Decreases with Backtrack

*For any* backtrack operation where a vertex is removed from the current path, the total distance should decrease by the edge distance that was previously added.

**Validates: Requirements 5.3**

### Property 10: Random Coordinate Bounds

*For any* graph with randomly generated coordinates within bounds (width, height), all vertex x-coordinates should be in [0, width] and all vertex y-coordinates should be in [0, height].

**Validates: Requirements 7.1**

### Property 11: Grid Coordinate Alignment

*For any* graph with grid-generated coordinates, the coordinates should follow a regular grid pattern where vertices are evenly spaced.

**Validates: Requirements 7.2**

### Property 12: Coordinate Persistence

*For any* vertex, setting its coordinates to (x, y) and then retrieving them should return the same (x, y) values.

**Validates: Requirements 7.4**

## Error Handling

### Coordinate Generation Errors

**Collision Detection:**
- When generating random coordinates, if a collision is detected (same x, y as existing vertex), regenerate coordinates
- Maximum retry limit: 1000 attempts per vertex
- If limit exceeded, throw `IllegalStateException` with message indicating coordinate space exhaustion

**Bounds Validation:**
- Validate that width and height are positive (> 0)
- Throw `IllegalArgumentException` if bounds are invalid
- Minimum coordinate space: 100x100 pixels to ensure reasonable distribution

### Visualization Callback Errors

**Null Callback Handling:**
- If callback is null, algorithm should execute without visualization (fallback to original behavior)
- No exceptions thrown for null callbacks

**Callback Exception Handling:**
- Wrap all callback invocations in try-catch blocks
- Log exceptions but continue algorithm execution
- Prevents visualization errors from breaking algorithm correctness

### Threading Errors

**SwingWorker Exceptions:**
- Override `done()` method to check for exceptions via `get()`
- Display error dialog to user if algorithm fails
- Ensure UI returns to stable state even on failure

**Timer Errors:**
- If Timer fails to start, log warning and continue without animation
- Visualization should still show final result even if animation fails

### Distance Calculation Errors

**Overflow Protection:**
- Use `long` for distance accumulation to prevent integer overflow
- Validate that individual edge distances are non-negative
- Throw `IllegalStateException` if negative distance detected

**Missing Coordinate Errors:**
- Validate that all vertices have assigned coordinates before distance calculation
- Throw `IllegalStateException` if vertex coordinates are uninitialized

## Testing Strategy

### Unit Testing Approach

Unit tests will focus on specific examples, edge cases, and integration points:

**Graph Coordinate Tests:**
- Test setting and getting coordinates for specific vertices
- Test coordinate generation with small graphs (n=4, n=10)
- Test edge cases: single vertex, two vertices
- Test collision detection with constrained coordinate space

**Visualization State Tests:**
- Test callback invocation with mock callbacks
- Test state updates in GraphPanel with specific paths
- Test color assignment for specific vertex states
- Test distance calculation for known paths

**Edge Cases:**
- Empty graph (n=0)
- Single vertex graph (n=1)
- Graph with no Hamiltonian cycle
- Coordinate space smaller than vertex count

### Property-Based Testing Approach

Property tests will verify universal properties across randomized inputs using a property-based testing library. Each test will run a minimum of 100 iterations.

**Library Selection:** Use **JUnit-Quickcheck** for Java property-based testing.

**Configuration:**
```java
@RunWith(JUnitQuickcheck.class)
public class CoordinateVisualizationPropertyTests {
    @Property(trials = 100)
    public void testPropertyName(/* generators */) {
        // Property test implementation
    }
}
```

**Property Test Specifications:**

**Property 1: Coordinate Uniqueness**
- **Tag:** Feature: coordinate-based-visualization, Property 1: For any graph with coordinate-based layout, no two vertices should occupy the same (x, y) coordinate pair
- **Generator:** Random graphs with 4-20 vertices
- **Test:** Generate coordinates, verify no duplicate (x, y) pairs

**Property 2: Coordinate Space Coverage**
- **Tag:** Feature: coordinate-based-visualization, Property 2: For any graph with n vertices where n ≥ 4, the vertices should be distributed such that at least two different quadrants of the coordinate space contain vertices
- **Generator:** Random graphs with 4-20 vertices
- **Test:** Generate coordinates, verify vertices span multiple quadrants

**Property 3: Visualization Updates on Vertex Visit**
- **Tag:** Feature: coordinate-based-visualization, Property 3: For any vertex visited during algorithm execution, the visualization callback should be invoked with the updated path state
- **Generator:** Random graphs with Hamiltonian cycles
- **Test:** Track callback invocations, verify path updates match visits

**Property 4: Visualization Updates on Backtrack**
- **Tag:** Feature: coordinate-based-visualization, Property 4: For any backtrack operation during algorithm execution, the visualization callback should be invoked with the backtrack state
- **Generator:** Random graphs that force backtracking
- **Test:** Track backtrack callbacks, verify state updates

**Property 5: Unvisited Vertex Color**
- **Tag:** Feature: coordinate-based-visualization, Property 5: For any vertex not in the current path and not in the backtrack set, the getVertexColor() method should return gray
- **Generator:** Random vertex states
- **Test:** Verify color for unvisited vertices

**Property 6: Active Path Vertex Color**
- **Tag:** Feature: coordinate-based-visualization, Property 6: For any vertex in the current active path, the getVertexColor() method should return blue
- **Generator:** Random paths
- **Test:** Verify color for path vertices

**Property 7: Backtrack Vertex Color**
- **Tag:** Feature: coordinate-based-visualization, Property 7: For any vertex in the backtrack set, the getVertexColor() method should return red
- **Generator:** Random backtrack sets
- **Test:** Verify color for backtracked vertices

**Property 8: Distance Increases with Path Growth**
- **Tag:** Feature: coordinate-based-visualization, Property 8: For any path extension where a vertex is added to the current path, the total distance should increase by the edge distance
- **Generator:** Random graphs with coordinates, random paths
- **Test:** Extend path, verify distance increases correctly

**Property 9: Distance Decreases with Backtrack**
- **Tag:** Feature: coordinate-based-visualization, Property 9: For any backtrack operation where a vertex is removed from the current path, the total distance should decrease by the edge distance
- **Generator:** Random graphs with coordinates, random paths
- **Test:** Remove vertex, verify distance decreases correctly

**Property 10: Random Coordinate Bounds**
- **Tag:** Feature: coordinate-based-visualization, Property 10: For any graph with randomly generated coordinates within bounds (width, height), all vertex coordinates should be within bounds
- **Generator:** Random graphs, random bounds (100-1000)
- **Test:** Generate coordinates, verify all within bounds

**Property 11: Grid Coordinate Alignment**
- **Tag:** Feature: coordinate-based-visualization, Property 11: For any graph with grid-generated coordinates, the coordinates should follow a regular grid pattern
- **Generator:** Random graphs with 4-25 vertices
- **Test:** Generate grid coordinates, verify regular spacing

**Property 12: Coordinate Persistence**
- **Tag:** Feature: coordinate-based-visualization, Property 12: For any vertex, setting its coordinates to (x, y) and then retrieving them should return the same (x, y) values
- **Generator:** Random vertices, random coordinates
- **Test:** Set coordinates, retrieve, verify equality (round-trip property)

### Integration Testing

**End-to-End Visualization Test:**
- Create graph with known Hamiltonian cycle
- Run algorithm with mock callback
- Verify callback sequence matches expected algorithm behavior
- Verify final visualization state matches solution

**UI Responsiveness Test:**
- Start algorithm on background thread
- Verify UI thread remains responsive (can process events)
- Verify Timer triggers repaints at expected intervals

### Test Coverage Goals

- Unit test coverage: 80% of new code
- Property test coverage: All 12 correctness properties
- Integration test coverage: Complete visualization workflow
- Edge case coverage: All error conditions documented in Error Handling section
