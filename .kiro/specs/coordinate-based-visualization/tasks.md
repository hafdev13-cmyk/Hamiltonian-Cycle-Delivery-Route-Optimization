# Implementation Plan: Coordinate-Based Visualization

## Overview

This implementation plan transforms the Hamiltonian Cycle visualization from a static circular layout to a dynamic coordinate-based system with real-time algorithm visualization. The work is organized into discrete coding tasks that build incrementally, starting with coordinate infrastructure, then adding visualization callbacks, and finally integrating the animation system.

## Tasks

- [x] 1. Add coordinate infrastructure to Graph class
  - [x] 1.1 Add coordinate storage fields and distance matrix to Graph
    - Add `private final int[] vertexX` and `private final int[] vertexY` arrays
    - Add `private final int[][] distances` matrix for Euclidean distances
    - Initialize arrays in constructor
    - _Requirements: 1.1, 1.2, 7.4_
  
  - [x] 1.2 Implement coordinate getter and setter methods
    - Implement `setVertexPosition(int vertex, int x, int y)`
    - Implement `getVertexX(int vertex)` and `getVertexY(int vertex)`
    - Implement `getDistance(int u, int v)`
    - Add bounds validation for vertex indices
    - _Requirements: 1.1, 1.2, 7.4_
  
  - [ ]* 1.3 Write property test for coordinate persistence
    - **Property 12: Coordinate Persistence**
    - **Validates: Requirements 7.4**
  
  - [x] 1.4 Implement random coordinate generation
    - Implement `generateRandomCoordinates(int width, int height, Random random)`
    - Add collision detection with retry logic (max 1000 attempts)
    - Calculate and populate distance matrix using Euclidean distance formula
    - Throw `IllegalStateException` if coordinate space exhausted
    - _Requirements: 7.1, 7.3_
  
  - [ ]* 1.5 Write property tests for random coordinate generation
    - **Property 1: Coordinate Uniqueness**
    - **Validates: Requirements 1.1, 1.2, 7.3**
    - **Property 10: Random Coordinate Bounds**
    - **Validates: Requirements 7.1**
  
  - [x] 1.6 Implement grid coordinate generation
    - Implement `generateGridCoordinates(int width, int height)`
    - Calculate grid spacing based on sqrt(n) layout
    - Populate distance matrix for grid coordinates
    - _Requirements: 7.2_
  
  - [ ]* 1.7 Write property tests for grid coordinate generation
    - **Property 2: Coordinate Space Coverage**
    - **Validates: Requirements 1.3**
    - **Property 11: Grid Coordinate Alignment**
    - **Validates: Requirements 7.2**

- [ ] 2. Checkpoint - Verify coordinate infrastructure
  - Ensure all tests pass, ask the user if questions arise.

- [x] 3. Add visualization callback interface to HamiltonianCycle
  - [x] 3.1 Create VisualizationCallback interface
    - Define `onVertexVisited(int vertex, int depth, int[] currentPath, int distance)` method
    - Define `onBacktrack(int vertex, int depth)` method
    - Define `onCycleFound(int[] cycle, int totalDistance)` method
    - Define `onSearchComplete(boolean found)` method
    - _Requirements: 2.1, 2.3_
  
  - [x] 3.2 Add callback support to HamiltonianCycle class
    - Add `private VisualizationCallback callback` field
    - Add `private int currentDistance` field for distance tracking
    - Implement `findCycleWithVisualization(VisualizationCallback callback)` method
    - _Requirements: 2.1, 2.3_
  
  - [x] 3.3 Integrate callback invocations into backtracking algorithm
    - Create `backtrackWithVisualization(int currentVertex, int depth)` method
    - Add `onVertexVisited()` callback before recursive calls
    - Add `onBacktrack()` callback after unsuccessful paths
    - Update `currentDistance` as path grows and shrinks
    - Wrap callback invocations in try-catch blocks for error handling
    - _Requirements: 2.1, 2.3, 5.2, 5.3_
  
  - [ ]* 3.4 Write property tests for visualization callbacks
    - **Property 3: Visualization Updates on Vertex Visit**
    - **Validates: Requirements 2.1**
    - **Property 4: Visualization Updates on Backtrack**
    - **Validates: Requirements 2.3**
  
  - [ ]* 3.5 Write property tests for distance tracking
    - **Property 8: Distance Increases with Path Growth**
    - **Validates: Requirements 5.2**
    - **Property 9: Distance Decreases with Backtrack**
    - **Validates: Requirements 5.3**

- [ ] 4. Checkpoint - Verify callback integration
  - Ensure all tests pass, ask the user if questions arise.

- [x] 5. Refactor GraphPanel for coordinate-based rendering
  - [x] 5.1 Add visualization state fields to GraphPanel
    - Add `private int[] currentPath` for active path tracking
    - Add `private Set<Integer> backtrackVertices` for backtrack state
    - Add `private int currentDistance` for distance display
    - Add `private Timer animationTimer` for animation control
    - Add `private boolean isAnimating` flag
    - _Requirements: 2.1, 2.2, 2.3, 5.1_
  
  - [x] 5.2 Remove circular layout and use coordinate-based positioning
    - Remove `calculateVertexPositions()` method with sin/cos calculations
    - Update `drawVertices()` to use `graph.getVertexX()` and `graph.getVertexY()`
    - Update `drawAllEdges()` to use coordinate-based positions
    - Update `drawCycleEdges()` to use coordinate-based positions
    - _Requirements: 1.4, 1.5_
  
  - [x] 5.3 Implement vertex color logic based on state
    - Implement `getVertexColor(int vertex)` method
    - Return gray for unvisited vertices
    - Return blue for vertices in current path
    - Return red for vertices in backtrack set
    - Update `drawVertices()` to use `getVertexColor()`
    - _Requirements: 3.1, 3.2, 3.3, 3.4_
  
  - [ ]* 5.4 Write property tests for vertex coloring
    - **Property 5: Unvisited Vertex Color**
    - **Validates: Requirements 3.1**
    - **Property 6: Active Path Vertex Color**
    - **Validates: Requirements 3.2**
    - **Property 7: Backtrack Vertex Color**
    - **Validates: Requirements 3.3**
  
  - [x] 5.5 Add depot marker visualization
    - Implement `drawDepotMarker(Graphics2D g2d, int vertex)` method
    - Render depot (vertex 0) with distinct visual indicator (green color or star icon)
    - Call `drawDepotMarker()` in `paintComponent()` after drawing vertices
    - _Requirements: 4.1, 4.2, 4.3_
  
  - [x] 5.6 Add distance tracking display
    - Implement `drawDistanceLabel(Graphics2D g2d)` method
    - Display current distance in top-right corner
    - Format distance with appropriate precision
    - Call `drawDistanceLabel()` in `paintComponent()`
    - _Requirements: 5.1, 5.2, 5.3, 5.4_
  
  - [x] 5.7 Implement visualization state update method
    - Implement `updateVisualizationState(int[] path, Set<Integer> backtrackSet, int distance)`
    - Update `currentPath`, `backtrackVertices`, and `currentDistance` fields
    - Call `repaint()` to trigger UI update
    - Use `SwingUtilities.invokeLater()` for thread-safe updates
    - _Requirements: 2.1, 2.3, 8.1, 8.2_
  
  - [x] 5.8 Enhance cycle path visualization
    - Update `drawCycleEdges()` to use bold line style (BasicStroke width 3-4)
    - Use distinct color for cycle path (red or bright color)
    - Render background edges with lighter/thinner style
    - _Requirements: 6.1, 6.2, 6.3, 6.4_

- [ ] 6. Checkpoint - Verify GraphPanel refactoring
  - Ensure all tests pass, ask the user if questions arise.

- [x] 7. Implement animation system with Timer
  - [x] 7.1 Create animation timer in GraphPanel
    - Implement `startAnimation()` method to initialize and start Timer
    - Implement `stopAnimation()` method to stop Timer
    - Configure Timer with appropriate delay (50-100ms for smooth animation)
    - Timer action should call `repaint()` on each tick
    - _Requirements: 2.2, 8.3, 8.4_
  
  - [x] 7.2 Implement VisualizationCallback in GraphPanel
    - Create inner class or separate class implementing VisualizationCallback
    - Implement `onVertexVisited()` to update path and distance, then call `updateVisualizationState()`
    - Implement `onBacktrack()` to update backtrack set, then call `updateVisualizationState()`
    - Implement `onCycleFound()` to store final cycle and stop animation
    - Implement `onSearchComplete()` to stop animation
    - _Requirements: 2.1, 2.3_

- [x] 8. Integrate SwingWorker for background algorithm execution
  - [x] 8.1 Refactor Main class to use SwingWorker
    - Create SwingWorker instance with `doInBackground()` calling `findCycleWithVisualization()`
    - Pass GraphPanel's callback implementation to algorithm
    - Override `done()` method to handle completion and errors
    - Call `worker.execute()` to start background execution
    - _Requirements: 2.5, 8.1, 8.2, 8.3_
  
  - [x] 8.2 Update Main to initialize coordinates before visualization
    - Call `graph.generateRandomCoordinates(500, 500, new Random())` after graph creation
    - Pass graph with coordinates to GraphPanel constructor
    - Initialize GraphPanel with null cycle (will be updated via callbacks)
    - _Requirements: 1.1, 1.2, 7.1_
  
  - [x] 8.3 Add error handling for SwingWorker
    - Wrap `get()` call in `done()` method with try-catch
    - Display error dialog using `JOptionPane` if algorithm fails
    - Ensure GraphPanel returns to stable state on error
    - Log exceptions for debugging
    - _Requirements: 8.1, 8.2_

- [x] 9. Final integration and wiring
  - [x] 9.1 Wire all components together in Main
    - Ensure graph initialization → coordinate generation → GraphPanel creation → SwingWorker execution flow
    - Verify callback chain: algorithm → callback → GraphPanel → Timer → repaint
    - Test with existing GraphData.createDeliveryNetwork()
    - _Requirements: 1.1, 1.2, 2.1, 2.2, 2.3_
  
  - [x] 9.2 Update legend to reflect new visualization states
    - Update `drawLegend()` to include gray (unvisited), blue (active), red (backtrack) states
    - Add depot marker explanation to legend
    - Add distance label explanation if needed
    - _Requirements: 3.1, 3.2, 3.3, 4.1_
  
  - [ ]* 9.3 Write integration tests for end-to-end visualization
    - Test complete workflow: graph creation → coordinate generation → algorithm execution → visualization updates
    - Verify callback sequence matches expected algorithm behavior
    - Test with graphs that have and don't have Hamiltonian cycles
    - _Requirements: 2.1, 2.3, 2.5, 8.1_

- [ ] 10. Final checkpoint - Complete system verification
  - Ensure all tests pass, ask the user if questions arise.

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP
- Each task references specific requirements for traceability
- Checkpoints ensure incremental validation at logical breakpoints
- Property tests validate universal correctness properties from the design document
- Unit tests validate specific examples and edge cases
- The implementation maintains existing algorithm correctness (O(n!) complexity)
- All UI updates use SwingUtilities.invokeLater() for thread safety
- Error handling prevents visualization failures from breaking algorithm execution
