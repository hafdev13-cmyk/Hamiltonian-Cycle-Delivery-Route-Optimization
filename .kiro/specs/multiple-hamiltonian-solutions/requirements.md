# Requirements Document: Multiple Hamiltonian Solutions

## Introduction

This feature enhances the Hamiltonian cycle solver to discover and display multiple distinct solution paths instead of just one. The system will find up to 5 different Hamiltonian cycles and present them in the terminal output, allowing users to see alternative delivery routes for their network.

## Glossary

- **Hamiltonian_Cycle**: A cycle that visits every vertex in a graph exactly once and returns to the starting vertex
- **Solution_Path**: A valid Hamiltonian cycle represented as an ordered sequence of vertices
- **Distinct_Cycle**: Two cycles are distinct if they represent different orderings of vertices (rotations of the same cycle are considered the same)
- **Backtracking_Algorithm**: A recursive algorithm that explores all possible paths and undoes choices that don't lead to solutions
- **Terminal_Output**: Text displayed in the console/command-line interface
- **Cycle_Solver**: The HamiltonianCycle class that discovers Hamiltonian cycles
- **Solution_Collector**: A mechanism to gather and store multiple valid cycles during the search process
- **Maximum_Solutions**: The upper limit of distinct cycles to discover (set to 5)

## Requirements

### Requirement 1: Discover Multiple Hamiltonian Cycles

**User Story:** As a route planner, I want to see multiple alternative delivery routes, so that I can choose the best option for my network.

#### Acceptance Criteria

1. WHEN THE Cycle_Solver searches for Hamiltonian cycles, THE Cycle_Solver SHALL continue searching after finding the first cycle
2. WHEN THE Cycle_Solver discovers a valid cycle, THE Solution_Collector SHALL store the cycle if it is distinct from previously found cycles
3. WHEN THE Cycle_Solver has found 5 distinct cycles OR exhausted all possible paths, THE Cycle_Solver SHALL stop searching
4. THE Cycle_Solver SHALL return all discovered cycles as a collection, not just a single cycle

### Requirement 2: Validate Cycle Distinctness

**User Story:** As a system, I want to ensure no duplicate cycles are stored, so that the output shows truly different routes.

#### Acceptance Criteria

1. WHEN comparing two cycles, THE Cycle_Solver SHALL consider rotations of the same cycle as identical (e.g., [0,1,2,3] and [1,2,3,0] are the same)
2. WHEN comparing two cycles, THE Cycle_Solver SHALL consider reverse cycles as identical (e.g., [0,1,2,3] and [0,3,2,1] are the same)
3. WHEN a new cycle is discovered, THE Solution_Collector SHALL check if it matches any previously stored cycle before adding it
4. IF a cycle is not distinct, THEN THE Solution_Collector SHALL discard it and continue searching

### Requirement 3: Format Multiple Solutions for Terminal Display

**User Story:** As a user, I want to see all discovered routes clearly formatted in the terminal, so that I can easily compare them.

#### Acceptance Criteria

1. WHEN multiple cycles are found, THE Printer SHALL display each cycle with a unique identifier (e.g., "Route 1", "Route 2")
2. WHEN displaying a cycle, THE Printer SHALL show the complete path with all vertices in order and cycle closure
3. WHEN displaying multiple cycles, THE Printer SHALL separate each cycle with clear visual formatting
4. THE Printer SHALL display a summary showing the total number of cycles found and the maximum limit (e.g., "Found 3 of 5 possible routes")

### Requirement 4: Handle Cases with Fewer Than 5 Solutions

**User Story:** As a user, I want clear feedback when fewer than 5 routes exist, so that I understand the search was complete.

#### Acceptance Criteria

1. IF fewer than 5 distinct cycles exist in the graph, THEN THE Printer SHALL display all found cycles
2. WHEN the search completes with fewer than 5 cycles, THE Printer SHALL indicate that the search exhausted all possibilities (e.g., "Found 2 of 5 possible routes (all routes discovered)")
3. IF no Hamiltonian cycles exist, THEN THE Printer SHALL display the "No Solution Found" message

### Requirement 5: Maintain Backward Compatibility with Visualization

**User Story:** As a developer, I want the visualization to work with multiple solutions, so that the GUI remains functional.

#### Acceptance Criteria

1. WHEN multiple cycles are discovered, THE Main class SHALL pass all cycles to the GraphPanel for visualization
2. WHEN the GraphPanel receives multiple cycles, THE GraphPanel SHALL display the first cycle by default
3. THE GraphPanel MAY provide a mechanism to switch between cycles (optional enhancement for future work)

### Requirement 6: Validate All Discovered Cycles

**User Story:** As a system, I want to ensure all output cycles are valid, so that users receive correct information.

#### Acceptance Criteria

1. WHEN a cycle is added to the Solution_Collector, THE Cycle_Solver SHALL validate it using the existing validateCycle method
2. IF a cycle fails validation, THEN THE Cycle_Solver SHALL discard it and continue searching
3. WHEN displaying cycles, THE Printer SHALL only display cycles that have been validated

### Requirement 7: Optimize Search Performance

**User Story:** As a user, I want the search to complete in reasonable time, so that I don't wait excessively for results.

#### Acceptance Criteria

1. WHEN THE Cycle_Solver has found 5 distinct cycles, THE Cycle_Solver SHALL stop searching immediately
2. THE Cycle_Solver SHALL use the existing backtracking algorithm without significant performance degradation
3. WHEN searching for multiple cycles, THE Cycle_Solver SHALL maintain the same time complexity class as the single-cycle search

