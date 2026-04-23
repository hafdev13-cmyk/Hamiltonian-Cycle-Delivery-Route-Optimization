# Hamiltonian Cycle Delivery Route Planning System - Requirements Document

## Introduction

The Hamiltonian Cycle Delivery Route Planning System is designed to solve the classic Hamiltonian cycle problem for delivery route optimization. The system must find a tour that visits every customer location (vertex) exactly once and returns to the depot (starting point), forming a complete cycle. This is essential for delivery companies to optimize their logistics and ensure efficient route planning across all service locations.

The system will use a backtracking algorithm to explore possible routes and identify valid Hamiltonian cycles within a road network represented as a graph with 20+ delivery points.

## Glossary

- **Hamiltonian Cycle**: A cycle in a graph that visits every vertex exactly once and returns to the starting vertex
- **Vertex**: A delivery point or location in the road network
- **Edge**: A direct road connection between two vertices with an associated cost/distance
- **Adjacency Matrix**: A 2D array representation of the graph where entry (i,j) indicates if vertices i and j are connected
- **Depot**: The starting and ending point for the delivery route (typically vertex 0)
- **Backtracking Algorithm**: A recursive algorithm that explores all possible paths and backtracks when a path cannot lead to a solution
- **Cycle Closure**: The requirement that the last vertex in the path connects back to the first vertex
- **Path Validation**: The process of verifying that a cycle is valid (visits all vertices exactly once, maintains adjacency, closes properly)
- **Graph Data**: The collection of vertices and edges representing the delivery network
- **Route**: The ordered sequence of delivery points to visit
- **Delivery Point**: A customer location that must be visited exactly once

## Requirements

### Requirement 1: Graph Representation Using Adjacency Matrix

**User Story:** As a system developer, I want the road network represented as an adjacency matrix, so that I can efficiently check connectivity between delivery points and support the backtracking algorithm.

#### Acceptance Criteria

1. THE Graph SHALL store the road network using a 2D integer array (adjacency matrix)
2. WHEN two vertices are connected by a road, THE adjacency matrix entry (i,j) SHALL be set to 1
3. WHEN two vertices are not connected, THE adjacency matrix entry (i,j) SHALL be set to 0
4. THE adjacency matrix SHALL be symmetric (if vertex i connects to vertex j, then vertex j connects to vertex i)
5. THE diagonal entries (i,i) SHALL be 0 (no self-loops)
6. THE Graph SHALL provide a method to check if an edge exists between two vertices in constant time O(1)

### Requirement 2: Dataset with Minimum 20 Vertices

**User Story:** As a delivery company, I want the system to handle at least 20 delivery points, so that it can support realistic delivery networks.

#### Acceptance Criteria

1. THE GraphData SHALL initialize a dataset containing at least 20 vertices
2. WHEN the system starts, THE Graph SHALL be populated with exactly 20 or more vertices
3. THE vertices SHALL be numbered sequentially starting from 0
4. EACH vertex SHALL represent a unique delivery point in the network
5. THE adjacency matrix dimensions SHALL be n×n where n is the number of vertices (minimum 20)

### Requirement 3: Guarantee Hamiltonian Cycle Existence

**User Story:** As a system designer, I want to ensure that at least one valid Hamiltonian cycle exists in the graph, so that the algorithm can always find a solution.

#### Acceptance Criteria

1. THE GraphData SHALL construct the adjacency matrix such that at least one Hamiltonian cycle exists
2. WHEN the graph is initialized, THE connectivity SHALL be sufficient to form a complete cycle through all vertices
3. THE graph structure SHALL be verified to contain a valid Hamiltonian cycle before the algorithm begins execution
4. WHERE a test graph is used, THE test graph SHALL be designed with known Hamiltonian cycles for validation purposes

### Requirement 4: Display Complete Cycle Path with Delivery Points

**User Story:** As a delivery manager, I want to see the complete delivery route with all points listed in order, so that I can understand the exact sequence of deliveries.

#### Acceptance Criteria

1. WHEN a Hamiltonian cycle is found, THE Printer SHALL display the complete path in order
2. THE output SHALL show each delivery point number in the sequence they will be visited
3. THE output SHALL explicitly show the return to the starting point (cycle closure)
4. THE output SHALL include the total number of delivery points visited
5. THE output format SHALL be user-friendly and clearly labeled as a "Delivery Route"
6. THE output SHALL display a success message confirming the route is complete

### Requirement 5: No Vertex Repeats in Path

**User Story:** As a route validator, I want to ensure no delivery point is visited twice, so that the route is valid and efficient.

#### Acceptance Criteria

1. WHEN the HamiltonianCycle algorithm constructs a path, THE path SHALL contain each vertex exactly once
2. IF a vertex is already in the current path, THEN THE algorithm SHALL not add it again
3. THE algorithm SHALL maintain a visited array to track which vertices have been included in the current path
4. WHEN backtracking occurs, THE visited array SHALL be updated to mark the vertex as unvisited
5. THE final cycle path SHALL have length equal to the number of vertices in the graph

### Requirement 6: Adjacency Requirement Between Consecutive Vertices

**User Story:** As a route planner, I want to ensure only connected delivery points are consecutive in the route, so that the route follows actual roads.

#### Acceptance Criteria

1. WHEN adding a vertex to the current path, THE algorithm SHALL verify that an edge exists between the current vertex and the next vertex
2. IF no edge exists between two consecutive vertices, THEN THE algorithm SHALL not add the next vertex to the path
3. THE adjacency check SHALL use the adjacency matrix to verify connectivity in O(1) time
4. WHEN backtracking, THE algorithm SHALL try alternative vertices that are adjacent to the current vertex
5. THE final cycle SHALL only contain edges that exist in the adjacency matrix

### Requirement 7: Cycle Closure Requirement

**User Story:** As a delivery coordinator, I want the route to return to the starting point, so that the delivery cycle is complete.

#### Acceptance Criteria

1. WHEN a complete path visiting all vertices is found, THE algorithm SHALL verify that the last vertex connects back to the first vertex (vertex 0)
2. IF the last vertex is not adjacent to the first vertex, THEN THE path SHALL not be considered a valid Hamiltonian cycle
3. THE cycle closure check SHALL be performed before accepting a solution
4. THE final output SHALL explicitly show the return edge from the last vertex back to the starting vertex
5. THE cycle SHALL form a closed loop with no missing connections

### Requirement 8: Worst-Case Complexity O(n!)

**User Story:** As a system architect, I want to understand the performance characteristics, so that I can set realistic expectations for larger datasets.

#### Acceptance Criteria

1. THE HamiltonianCycle algorithm SHALL use backtracking, which has worst-case time complexity of O(n!)
2. WHEN the graph size increases, THE execution time SHALL increase factorially in the worst case
3. THE algorithm documentation SHALL clearly state the O(n!) worst-case complexity
4. WHERE n is the number of vertices, THE maximum number of recursive calls SHALL be bounded by n!
5. THE algorithm SHALL be suitable for graphs with 20-30 vertices but may be slow for larger graphs

### Requirement 9: Backtracking Algorithm Implementation

**User Story:** As a developer, I want the system to use backtracking to find the Hamiltonian cycle, so that all possible paths are explored systematically.

#### Acceptance Criteria

1. THE HamiltonianCycle class SHALL implement a recursive backtracking algorithm
2. WHEN exploring paths, THE algorithm SHALL try each adjacent unvisited vertex
3. IF a path cannot lead to a solution, THE algorithm SHALL backtrack and try alternative vertices
4. THE algorithm SHALL maintain a path array to store the current route being explored
5. WHEN a complete cycle is found, THE algorithm SHALL return the valid path
6. IF no Hamiltonian cycle exists, THE algorithm SHALL return null or indicate failure

### Requirement 10: Find and Display Valid Hamiltonian Cycle

**User Story:** As a delivery system user, I want the system to find and display a valid Hamiltonian cycle, so that I can use it for route planning.

#### Acceptance Criteria

1. WHEN the system starts, THE HamiltonianCycle algorithm SHALL search for a valid Hamiltonian cycle
2. WHEN a valid cycle is found, THE Printer SHALL display the complete route
3. THE cycle SHALL satisfy all constraints: visit all vertices exactly once, maintain adjacency, and close properly
4. THE output SHALL be formatted clearly for delivery managers to understand
5. WHEN the algorithm completes, THE system SHALL indicate success or failure

### Requirement 11: Handle Graphs with 20+ Vertices

**User Story:** As a delivery company, I want the system to efficiently process networks with 20 or more delivery points, so that it scales to realistic business needs.

#### Acceptance Criteria

1. THE system SHALL accept graphs with 20 or more vertices without modification
2. WHEN processing a 20-vertex graph, THE algorithm SHALL complete execution
3. THE adjacency matrix SHALL scale to accommodate n×n dimensions for n ≥ 20
4. THE backtracking algorithm SHALL work correctly regardless of the number of vertices (within computational limits)
5. THE system SHALL handle vertex numbering from 0 to n-1 consistently

### Requirement 12: Validate Cycle Correctness

**User Story:** As a quality assurance engineer, I want the system to validate that the found cycle is correct, so that I can trust the route.

#### Acceptance Criteria

1. WHEN a cycle is found, THE system SHALL verify that all vertices are visited exactly once
2. THE system SHALL verify that consecutive vertices in the cycle are adjacent in the graph
3. THE system SHALL verify that the last vertex connects back to the first vertex
4. IF any validation check fails, THEN THE cycle SHALL be rejected
5. THE validation process SHALL be performed before displaying the route to the user

### Requirement 13: Display Route in User-Friendly Format

**User Story:** As a delivery manager, I want the route displayed in a clear, easy-to-read format, so that drivers can understand their delivery sequence.

#### Acceptance Criteria

1. THE Printer SHALL display the route with clear labels and formatting
2. THE output SHALL show each delivery point in sequence with arrows indicating the direction of travel
3. THE output SHALL include a header and footer to clearly mark the route information
4. THE output SHALL display the total number of delivery points visited
5. THE output SHALL use consistent formatting for all route displays

### Requirement 14: Separate Concerns - Graph Module

**User Story:** As a developer, I want the graph representation logic separated from the algorithm, so that the code is maintainable and testable.

#### Acceptance Criteria

1. THE Graph class SHALL encapsulate all graph representation logic
2. THE Graph class SHALL provide methods to initialize the adjacency matrix
3. THE Graph class SHALL provide methods to check edge existence
4. THE Graph class SHALL not contain algorithm logic
5. THE Graph class SHALL be reusable for different graph problems

### Requirement 15: Separate Concerns - Algorithm Module

**User Story:** As a developer, I want the Hamiltonian cycle algorithm isolated from graph representation, so that the algorithm can be tested independently.

#### Acceptance Criteria

1. THE HamiltonianCycle class SHALL contain only the backtracking algorithm logic
2. THE HamiltonianCycle class SHALL accept a Graph object as input
3. THE HamiltonianCycle class SHALL not directly manipulate the adjacency matrix
4. THE HamiltonianCycle class SHALL provide a method to find and return the cycle
5. THE HamiltonianCycle class SHALL be independent of output formatting

### Requirement 16: Separate Concerns - Data Module

**User Story:** As a developer, I want the graph data initialization separated from other concerns, so that different datasets can be easily swapped.

#### Acceptance Criteria

1. THE GraphData class SHALL handle all graph initialization and data setup
2. THE GraphData class SHALL create the adjacency matrix with appropriate connectivity
3. THE GraphData class SHALL ensure at least one Hamiltonian cycle exists in the generated graph
4. THE GraphData class SHALL not contain algorithm or display logic
5. THE GraphData class SHALL be easily modifiable to support different graph configurations

### Requirement 17: Separate Concerns - Utilities Module

**User Story:** As a developer, I want output formatting separated from core logic, so that display logic can be changed without affecting the algorithm.

#### Acceptance Criteria

1. THE Printer class SHALL handle all output formatting and display
2. THE Printer class SHALL accept the cycle path as input
3. THE Printer class SHALL not contain algorithm or graph logic
4. THE Printer class SHALL provide clear, formatted output
5. THE Printer class SHALL be easily modifiable to support different output formats

### Requirement 18: Proper Error Handling

**User Story:** As a system user, I want the system to handle errors gracefully, so that I receive clear feedback when something goes wrong.

#### Acceptance Criteria

1. IF the graph is invalid or empty, THE system SHALL display an appropriate error message
2. IF no Hamiltonian cycle exists, THE system SHALL indicate that no solution was found
3. IF the algorithm encounters an error, THE system SHALL catch and report it
4. THE error messages SHALL be clear and actionable
5. THE system SHALL not crash due to invalid input or missing data

### Requirement 19: Performance Considerations for Backtracking

**User Story:** As a system architect, I want the backtracking implementation to be efficient, so that it performs well within acceptable time limits.

#### Acceptance Criteria

1. THE algorithm SHALL use early termination when a complete cycle is found
2. THE algorithm SHALL avoid redundant checks by maintaining the visited array
3. THE algorithm SHALL use efficient adjacency checking via the adjacency matrix
4. THE algorithm SHALL minimize memory usage by reusing the path array during backtracking
5. THE algorithm documentation SHALL include performance notes and limitations

### Requirement 20: Main Entry Point Integration

**User Story:** As a system user, I want a single entry point to run the entire system, so that I can easily execute the delivery route planning.

#### Acceptance Criteria

1. THE Main class SHALL initialize the graph with delivery point data
2. THE Main class SHALL create a HamiltonianCycle solver instance
3. THE Main class SHALL execute the algorithm to find the cycle
4. THE Main class SHALL display the results using the Printer
5. THE Main class SHALL handle the complete workflow from initialization to output

---

## Acceptance Criteria Testing Strategy

### Property-Based Testing Approach

The following properties should be verified through testing:

1. **Invariant Property**: After finding a Hamiltonian cycle, the cycle length equals the number of vertices
   - Property: `cycle.length == graph.vertexCount`

2. **Round-Trip Property**: The cycle can be verified by checking each consecutive pair of vertices
   - Property: For all consecutive vertices (v_i, v_{i+1}) in the cycle, an edge must exist in the adjacency matrix

3. **Idempotence Property**: Running the algorithm multiple times on the same graph produces the same cycle
   - Property: `findCycle(graph) == findCycle(graph)` (same result on repeated calls)

4. **Metamorphic Property**: The number of vertices in the cycle must be less than or equal to the total vertices
   - Property: `cycle.length <= graph.vertexCount`

5. **Cycle Closure Property**: The last vertex in the cycle must connect back to the first vertex
   - Property: `adjacencyMatrix[cycle[n-1]][cycle[0]] == 1`

6. **No Repetition Property**: Each vertex appears exactly once in the cycle
   - Property: `Set(cycle).size() == cycle.length`

---

## Document Version

- **Version**: 1.0
- **Date**: 2024
- **Status**: Initial Requirements Document
