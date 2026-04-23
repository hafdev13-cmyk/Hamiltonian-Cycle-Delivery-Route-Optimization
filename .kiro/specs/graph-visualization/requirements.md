# Graph Visualization Feature - Requirements Document

## Introduction

The Graph Visualization feature extends the existing Hamiltonian Cycle Delivery Route Planning System with interactive visual representation of the graph and discovered Hamiltonian cycle. Currently, the system outputs route information to the console. This feature adds a graphical user interface that displays the delivery network as a visual graph with vertices and edges, highlighting the discovered Hamiltonian cycle path in a distinct color. This enhancement enables delivery managers and route planners to quickly understand network topology and verify route validity through visual inspection.

## Glossary

- **Graph Visualization**: A graphical representation of the graph structure showing vertices and edges
- **Vertex**: A delivery point displayed as a circle in the visualization
- **Edge**: A road connection between two vertices displayed as a line
- **Cycle Path**: The Hamiltonian cycle edges highlighted in a distinct color (red)
- **Non-Cycle Edge**: Graph edges that are not part of the discovered Hamiltonian cycle
- **Vertex Position**: The (x, y) coordinate where a vertex is drawn on the canvas
- **Layout Algorithm**: The method used to calculate vertex positions (circular layout)
- **JFrame**: The main window container for the visualization
- **JPanel**: The drawing surface where the graph is rendered
- **Graphics2D**: Java 2D graphics API used for rendering shapes and text
- **Rendering Hint**: Configuration for graphics quality (antialiasing, etc.)
- **Legend**: Visual guide explaining the meaning of colors and symbols in the visualization
- **Cycle Vertex**: A vertex that is part of the discovered Hamiltonian cycle
- **Non-Cycle Vertex**: A vertex that exists in the graph but is not part of the discovered cycle
- **Delivery Manager**: The user who views the visualization to understand and verify delivery routes

## Requirements

### Requirement 1: Display Graph as Visual Representation

**User Story:** As a delivery manager, I want to see the delivery network displayed as a visual graph, so that I can understand the network topology at a glance.

#### Acceptance Criteria

1. WHEN the visualization is launched, THE system SHALL display the graph with all vertices and edges
2. THE vertices SHALL be rendered as circles with distinct visual appearance
3. THE edges SHALL be rendered as lines connecting vertices
4. THE visualization SHALL use a graphical window (JFrame) to display the graph
5. THE visualization SHALL render on a drawing surface (JPanel) with proper graphics context
6. THE visualization SHALL be readable and not cluttered for graphs with 20+ vertices

### Requirement 2: Highlight Hamiltonian Cycle Path

**User Story:** As a route planner, I want the discovered Hamiltonian cycle to be visually distinct, so that I can easily identify the delivery route.

#### Acceptance Criteria

1. WHEN a Hamiltonian cycle is found, THE cycle edges SHALL be displayed in a distinct color (red)
2. THE cycle edges SHALL have a thicker line width than non-cycle edges
3. THE cycle path SHALL be visually distinguishable from other graph edges
4. WHEN the visualization is rendered, THE cycle path SHALL be drawn on top of other edges
5. THE cycle edges SHALL clearly show the sequence of deliveries in order

### Requirement 3: Differentiate Cycle and Non-Cycle Vertices

**User Story:** As a delivery coordinator, I want to see which vertices are part of the cycle, so that I can verify all delivery points are included.

#### Acceptance Criteria

1. WHEN vertices are rendered, THE vertices in the Hamiltonian cycle SHALL be displayed in one color (blue)
2. THE vertices not in the cycle SHALL be displayed in a different color (gray)
3. THE color difference SHALL be immediately apparent to the user
4. EACH vertex SHALL display its vertex number as a label
5. THE vertex labels SHALL be readable and properly positioned within the vertex circle

### Requirement 4: Implement Circular Layout Algorithm

**User Story:** As a system designer, I want vertices positioned in a circular arrangement, so that the graph is visually balanced and all vertices are visible.

#### Acceptance Criteria

1. THE visualization SHALL calculate vertex positions using a circular layout algorithm
2. WHEN vertices are positioned, THEY SHALL be arranged in a circle around a center point
3. THE radius of the circle SHALL be sufficient to prevent vertex overlap
4. THE vertices SHALL be evenly distributed around the circle
5. THE layout SHALL accommodate graphs with 20+ vertices without overlap
6. THE vertex positions SHALL be calculated based on vertex index and total vertex count

### Requirement 5: Display Legend Explaining Visualization

**User Story:** As a delivery manager, I want a legend explaining the visualization symbols, so that I can understand what each color and symbol means.

#### Acceptance Criteria

1. WHEN the visualization is displayed, THE legend SHALL be visible on the screen
2. THE legend SHALL explain the meaning of cycle vertices (blue circles)
3. THE legend SHALL explain the meaning of non-cycle vertices (gray circles)
4. THE legend SHALL explain the meaning of cycle edges (red lines)
5. THE legend SHALL be positioned in a non-intrusive location (e.g., top-left corner)
6. THE legend text SHALL be clear and easy to read

### Requirement 6: Integrate Visualization with Existing System

**User Story:** As a system developer, I want the visualization to integrate seamlessly with the existing Hamiltonian cycle algorithm, so that users can see results without code changes.

#### Acceptance Criteria

1. THE visualization SHALL accept a Graph object and a Hamiltonian cycle path as input
2. THE visualization SHALL not modify the Graph or cycle data
3. THE visualization SHALL be launched after the algorithm finds a cycle
4. THE visualization SHALL work with the existing GraphData, HamiltonianCycle, and Graph classes
5. THE visualization SHALL be optional (system works with or without it)
6. THE existing console output (Printer) SHALL continue to work alongside visualization

### Requirement 7: Render All Graph Edges

**User Story:** As a network analyst, I want to see all edges in the graph, so that I can understand the complete network connectivity.

#### Acceptance Criteria

1. WHEN the visualization is rendered, THE system SHALL draw all edges from the adjacency matrix
2. THE non-cycle edges SHALL be displayed in a light color (light gray)
3. THE non-cycle edges SHALL have a thin line width
4. WHEN an edge exists in the adjacency matrix, IT SHALL be drawn on the visualization
5. THE edges SHALL be drawn before vertices to appear behind them
6. THE edges SHALL not obscure the vertex visibility

### Requirement 8: Apply Graphics Quality Enhancements

**User Story:** As a user, I want the visualization to look smooth and professional, so that it's pleasant to view.

#### Acceptance Criteria

1. THE visualization SHALL use antialiasing for smooth rendering of shapes and text
2. THE graphics rendering hints SHALL be configured for quality output
3. THE fonts SHALL be readable and properly sized
4. THE colors SHALL be distinct and not cause visual strain
5. THE line strokes SHALL be properly configured for thickness and appearance
6. THE overall appearance SHALL be professional and polished

### Requirement 9: Handle Graphs with Varying Vertex Counts

**User Story:** As a system user, I want the visualization to work with different graph sizes, so that it's flexible for various network configurations.

#### Acceptance Criteria

1. THE visualization SHALL work correctly with graphs containing 20 vertices
2. THE visualization SHALL work correctly with graphs containing more than 20 vertices (up to 30+)
3. THE layout algorithm SHALL scale appropriately for different vertex counts
4. THE vertex positions SHALL be recalculated based on the actual vertex count
5. THE visualization SHALL remain readable regardless of vertex count
6. THE legend and other UI elements SHALL scale appropriately

### Requirement 10: Display Vertex Labels Clearly

**User Story:** As a delivery coordinator, I want to see vertex numbers clearly, so that I can identify specific delivery points.

#### Acceptance Criteria

1. WHEN vertices are rendered, EACH vertex SHALL display its index number as a label
2. THE label SHALL be positioned in the center of the vertex circle
3. THE label text SHALL be white or a contrasting color for readability
4. THE label font SHALL be bold and appropriately sized
5. THE label SHALL not extend outside the vertex circle
6. THE label positioning SHALL be calculated to center the text within the vertex

### Requirement 11: Create JFrame Window Container

**User Story:** As a system user, I want the visualization displayed in a separate window, so that I can view it independently from the console.

#### Acceptance Criteria

1. THE visualization SHALL create a JFrame window to display the graph
2. THE JFrame SHALL have an appropriate title indicating it's a graph visualization
3. THE JFrame SHALL be sized appropriately to display the graph clearly (e.g., 600x600 pixels)
4. THE JFrame SHALL be set to visible when created
5. THE JFrame SHALL allow the user to close the window
6. THE JFrame SHALL be positioned appropriately on the screen

### Requirement 12: Render Cycle Path in Correct Order

**User Story:** As a route validator, I want the cycle path drawn in the correct sequence, so that I can verify the delivery order.

#### Acceptance Criteria

1. WHEN the cycle path is rendered, THE edges SHALL be drawn in the order specified by the cycle array
2. THE first edge SHALL connect cycle[0] to cycle[1]
3. THE last edge SHALL connect cycle[n-1] back to cycle[0] (cycle closure)
4. THE edges SHALL form a continuous path without gaps
5. THE path SHALL visit each vertex in the cycle exactly once
6. THE visual representation SHALL match the cycle array order

### Requirement 13: Separate Visualization Concerns from Algorithm

**User Story:** As a developer, I want visualization logic separated from the algorithm, so that the code is maintainable and testable.

#### Acceptance Criteria

1. THE GraphPanel class SHALL contain only visualization rendering logic
2. THE GraphPanel SHALL not contain algorithm logic
3. THE GraphPanel SHALL accept Graph and cycle data as constructor parameters
4. THE GraphPanel SHALL not modify the Graph or cycle data
5. THE visualization SHALL be independent of the HamiltonianCycle algorithm
6. THE visualization SHALL be optional and not required for the system to function

### Requirement 14: Handle Null or Invalid Cycle Data

**User Story:** As a system user, I want the visualization to handle cases where no cycle is found, so that I receive appropriate feedback.

#### Acceptance Criteria

1. IF the cycle parameter is null, THE visualization SHALL display the graph without highlighting a cycle path
2. IF the cycle parameter is null, THE vertices SHALL all be displayed in the non-cycle color
3. THE visualization SHALL not crash when cycle is null
4. THE visualization SHALL display all graph edges normally when no cycle is provided
5. THE legend SHALL indicate that no cycle is currently highlighted
6. THE system SHALL display an appropriate message when no cycle is found

### Requirement 15: Position Vertices Without Overlap

**User Story:** As a user, I want vertices positioned so they don't overlap, so that I can see all vertices clearly.

#### Acceptance Criteria

1. THE circular layout algorithm SHALL calculate positions that prevent vertex overlap
2. THE radius of the circle SHALL be large enough to accommodate all vertices
3. THE vertex radius (20 pixels) SHALL be considered when calculating positions
4. THE padding around the canvas SHALL be sufficient to keep vertices visible
5. FOR graphs with 20+ vertices, THE vertices SHALL remain distinct and non-overlapping
6. THE layout SHALL be recalculated if the window is resized

### Requirement 16: Display Cycle Closure Visually

**User Story:** As a delivery manager, I want to see the return path to the starting point, so that I can verify the cycle is complete.

#### Acceptance Criteria

1. WHEN the cycle is rendered, THE edge from the last vertex back to the first vertex SHALL be drawn
2. THE cycle closure edge SHALL be the same color and thickness as other cycle edges (red, thick)
3. THE cycle closure edge SHALL be visually distinct from non-cycle edges
4. THE cycle closure SHALL be explicitly shown in the visualization
5. THE cycle closure edge SHALL connect cycle[n-1] to cycle[0]
6. THE visualization SHALL make it clear that the cycle returns to the starting point

### Requirement 17: Provide Visual Feedback for Cycle Vertices

**User Story:** As a route planner, I want cycle vertices visually emphasized, so that I can quickly identify which delivery points are in the route.

#### Acceptance Criteria

1. WHEN a vertex is part of the cycle, IT SHALL be displayed in blue
2. WHEN a vertex is not part of the cycle, IT SHALL be displayed in gray
3. THE color difference SHALL be immediately apparent
4. THE vertex border SHALL be black for all vertices for consistency
5. THE vertex fill color SHALL indicate cycle membership
6. THE visual distinction SHALL be maintained throughout the visualization

### Requirement 18: Render Graphics with Proper Layering

**User Story:** As a user, I want the visualization rendered in the correct order, so that important elements are visible.

#### Acceptance Criteria

1. THE non-cycle edges SHALL be drawn first (background layer)
2. THE cycle edges SHALL be drawn second (middle layer)
3. THE vertices SHALL be drawn last (foreground layer)
4. THE legend SHALL be drawn last (top layer)
5. THE layering SHALL ensure that cycle edges are visible over non-cycle edges
6. THE vertices SHALL be visible over all edges

### Requirement 19: Support Interactive Window Management

**User Story:** As a system user, I want to manage the visualization window, so that I can control when it's displayed.

#### Acceptance Criteria

1. THE visualization window SHALL be closeable by the user
2. THE visualization window SHALL be resizable (optional enhancement)
3. THE visualization SHALL remain responsive while displayed
4. THE window close action SHALL not crash the application
5. THE visualization SHALL be independent of the console output
6. THE user SHALL be able to view the visualization and console simultaneously

### Requirement 20: Integrate Visualization into Main Workflow

**User Story:** As a system user, I want the visualization to launch automatically after finding a cycle, so that I can see results immediately.

#### Acceptance Criteria

1. WHEN the HamiltonianCycle algorithm finds a cycle, THE visualization SHALL be launched
2. THE visualization SHALL display the graph and the discovered cycle
3. THE visualization SHALL be launched after the console output (Printer)
4. THE visualization SHALL not block the console output
5. THE visualization SHALL be optional (can be disabled if needed)
6. THE Main class SHALL orchestrate the visualization launch

---

## Acceptance Criteria Testing Strategy

### Property-Based Testing Approach

The following properties should be verified through testing:

1. **Invariant Property**: All vertices in the cycle are displayed in the correct color
   - Property: For all vertices in the cycle array, the vertex color must be blue

2. **Round-Trip Property**: The visualization can be created and destroyed without errors
   - Property: Creating a visualization and closing it should not cause exceptions

3. **Idempotence Property**: Rendering the same graph and cycle multiple times produces identical output
   - Property: Multiple renders of the same data produce the same visual result

4. **Metamorphic Property**: The number of vertices displayed equals the graph vertex count
   - Property: `verticesDisplayed.count == graph.vertexCount`

5. **Cycle Path Property**: All edges in the cycle path are drawn in red
   - Property: For all consecutive vertices in the cycle, the edge is rendered in red

6. **Non-Cycle Edge Property**: All non-cycle edges are drawn in light gray
   - Property: For all edges not in the cycle, the edge is rendered in light gray

7. **Vertex Position Property**: All vertices are positioned within the canvas bounds
   - Property: For all vertices, `0 <= vertexX[i] <= canvasWidth` and `0 <= vertexY[i] <= canvasHeight`

8. **Label Visibility Property**: All vertex labels are readable and positioned correctly
   - Property: Each vertex label is centered within its vertex circle

9. **Layout Consistency Property**: Vertex positions are consistent for the same graph
   - Property: Calculating positions twice for the same graph produces identical coordinates

10. **Cycle Closure Property**: The last vertex connects back to the first in the visualization
    - Property: An edge is drawn from cycle[n-1] to cycle[0]

---

## Document Version

- **Version**: 1.0
- **Date**: 2024
- **Status**: Initial Requirements Document
