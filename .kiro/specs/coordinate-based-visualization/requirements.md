# Requirements Document

## Introduction

This feature refactors the Hamiltonian Cycle (Delivery Route Planning) visualization system to provide a coordinate-based layout that simulates real-world city maps, along with step-by-step algorithm visualization. The current implementation uses a circular layout and displays only the final result. This enhancement will enable users to observe the backtracking algorithm's behavior in real-time, understand the pathfinding process, and visualize delivery routes on a map-like coordinate system.

## Glossary

- **Graph_System**: The Graph class that manages vertices, edges, and adjacency matrix
- **Visualization_Panel**: The GraphPanel class responsible for rendering the graph
- **Algorithm_Engine**: The HamiltonianCycle class implementing the backtracking algorithm
- **Vertex**: A node in the graph representing a delivery location
- **Depot**: Vertex 0, the starting and ending point of the delivery route
- **Active_Path**: The current path being explored by the backtracking algorithm
- **Backtrack_State**: The state when the algorithm retreats from a dead end
- **Coordinate_Space**: A 500x500 pixel area for vertex placement
- **Animation_Timer**: A javax.swing.Timer used to control visualization speed
- **Total_Distance**: The cumulative distance of the current path being explored

## Requirements

### Requirement 1: Coordinate-Based Vertex Layout

**User Story:** As a delivery route planner, I want vertices to be positioned using x,y coordinates instead of a circular layout, so that I can visualize routes on a realistic city map representation.

#### Acceptance Criteria

1. THE Graph_System SHALL assign each Vertex a unique x-coordinate within the Coordinate_Space
2. THE Graph_System SHALL assign each Vertex a unique y-coordinate within the Coordinate_Space
3. THE Graph_System SHALL distribute Vertex coordinates across the full Coordinate_Space
4. THE Visualization_Panel SHALL render edges between Vertices using their assigned coordinates
5. THE Visualization_Panel SHALL remove all circular layout calculations (sin/cos functions)

### Requirement 2: Step-by-Step Algorithm Visualization

**User Story:** As a developer learning backtracking algorithms, I want to see the algorithm's progress in real-time, so that I can understand how the pathfinding process works.

#### Acceptance Criteria

1. WHEN the Algorithm_Engine explores a new Vertex, THE Visualization_Panel SHALL update the display to show the current Active_Path
2. THE Visualization_Panel SHALL use an Animation_Timer to control the visualization speed
3. WHEN the Algorithm_Engine backtracks from a dead end, THE Visualization_Panel SHALL update the display to show the Backtrack_State
4. THE Algorithm_Engine SHALL maintain O(n!) time complexity during the search process
5. THE Visualization_Panel SHALL remain responsive during algorithm execution

### Requirement 3: Visual State Indicators

**User Story:** As a user observing the algorithm, I want different colors to represent different states, so that I can easily distinguish between visited, active, and backtracked nodes.

#### Acceptance Criteria

1. WHEN a Vertex has not been visited, THE Visualization_Panel SHALL render it in gray color
2. WHEN a Vertex is part of the Active_Path, THE Visualization_Panel SHALL render it in blue color
3. WHEN the Algorithm_Engine enters a Backtrack_State, THE Visualization_Panel SHALL render affected Vertices in red color
4. THE Visualization_Panel SHALL update Vertex colors in real-time as the algorithm progresses

### Requirement 4: Depot Identification

**User Story:** As a delivery route planner, I want the starting depot to be clearly marked, so that I can easily identify the route's origin point.

#### Acceptance Criteria

1. THE Visualization_Panel SHALL render the Depot with a distinct visual indicator
2. THE Visualization_Panel SHALL differentiate the Depot from other Vertices using either a unique icon or color
3. THE Depot SHALL remain visually distinct throughout the entire visualization process

### Requirement 5: Distance Tracking Display

**User Story:** As a route planner, I want to see the total distance of the current path, so that I can understand the cost of routes being explored.

#### Acceptance Criteria

1. THE Visualization_Panel SHALL display a Total_Distance label on the screen
2. WHEN the Active_Path grows, THE Visualization_Panel SHALL update the Total_Distance label
3. WHEN the Algorithm_Engine backtracks, THE Visualization_Panel SHALL update the Total_Distance label to reflect the reduced path
4. THE Total_Distance label SHALL remain visible throughout the visualization process

### Requirement 6: Enhanced Edge Visualization

**User Story:** As a user viewing the final result, I want the Hamiltonian cycle to stand out clearly, so that I can distinguish the solution from the underlying graph structure.

#### Acceptance Criteria

1. THE Visualization_Panel SHALL render the final Hamiltonian cycle path with a bold line style
2. THE Visualization_Panel SHALL render the final Hamiltonian cycle path in a distinct color
3. THE Visualization_Panel SHALL render background edges in a lighter or thinner style than the cycle path
4. THE Visualization_Panel SHALL ensure the cycle path remains visually prominent against the map-like background

### Requirement 7: Coordinate Distribution Strategy

**User Story:** As a system user, I want vertices to be distributed realistically across the coordinate space, so that the visualization resembles an actual city map.

#### Acceptance Criteria

1. THE Graph_System SHALL support random distribution of Vertex coordinates within the Coordinate_Space
2. THE Graph_System SHALL support grid-like distribution of Vertex coordinates within the Coordinate_Space
3. WHERE random distribution is selected, THE Graph_System SHALL ensure no two Vertices occupy the same coordinates
4. THE Graph_System SHALL store coordinate data persistently for each Vertex

### Requirement 8: UI Responsiveness During Search

**User Story:** As a user running the visualization, I want the UI to remain responsive during the algorithm execution, so that I can interact with the application without freezing.

#### Acceptance Criteria

1. WHILE the Algorithm_Engine performs the backtracking search, THE Visualization_Panel SHALL process UI events
2. WHILE the Algorithm_Engine performs the backtracking search, THE Visualization_Panel SHALL accept user input
3. THE Animation_Timer SHALL execute on a separate thread from the Algorithm_Engine
4. THE Visualization_Panel SHALL update at a rate that maintains smooth animation without blocking the UI thread
