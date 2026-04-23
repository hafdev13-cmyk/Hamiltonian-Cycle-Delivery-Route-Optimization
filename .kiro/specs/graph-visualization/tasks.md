# Implementation Plan: Graph Visualization Feature

## Overview

This implementation plan breaks down the Graph Visualization feature into discrete, testable coding tasks. The feature extends the Hamiltonian Cycle Delivery Route Planning System with interactive visual representation of the graph and discovered cycle. The implementation focuses on completing and validating the GraphPanel component, integrating it with the Main class, and ensuring all correctness properties are verified through comprehensive testing.

The implementation follows a layered approach: first establishing core GraphPanel functionality, then adding rendering layers (edges, vertices, labels), then implementing error handling and null cycle support, and finally integrating with the Main class and comprehensive testing.

## Tasks

- [ ] 1. Set up GraphPanel foundation and vertex positioning
  - Verify GraphPanel constructor accepts Graph and cycle parameters
  - Ensure calculateVertexPositions() correctly implements circular layout algorithm
  - Validate vertex position calculations for various vertex counts (20, 25, 30)
  - Verify background color is set to white
  - _Requirements: 1.1, 1.2, 4.1, 4.2, 4.4_

  - [ ]* 1.1 Write property test for circular layout positioning
    - **Property 9: Vertices Positioned in Circle**
    - **Validates: Requirements 4.1, 4.2, 4.4**
    - Test that all vertices are positioned on circle with center (300, 300) and radius 200
    - Test that angular spacing between consecutive vertices equals 2π/n

  - [ ]* 1.2 Write property test for vertex position determinism
    - **Property 22: Layout Deterministic**
    - **Validates: Requirements 4.6**
    - Test that calculating positions twice produces identical coordinates

- [ ] 2. Implement non-cycle edge rendering
  - Render all edges from adjacency matrix in light gray (RGB: 200, 200, 200)
  - Use thin stroke (BasicStroke(1)) for non-cycle edges
  - Draw non-cycle edges before vertices (background layer)
  - Verify all edges in adjacency matrix are rendered
  - _Requirements: 7.1, 7.2, 7.3, 7.4, 18.1_

  - [ ]* 2.1 Write property test for all edges rendered
    - **Property 1: All Graph Edges Rendered**
    - **Validates: Requirements 7.1, 7.4**
    - Test that for any graph, all edges where matrix[i][j] == 1 are rendered

  - [ ]* 2.2 Write property test for non-cycle edge color
    - **Property 3: Non-Cycle Edges Colored Light Gray**
    - **Validates: Requirements 7.2, 7.3**
    - Test that all non-cycle edges are rendered in light gray (200, 200, 200)

- [ ] 3. Implement cycle edge rendering with highlighting
  - Render cycle edges in red (RGB: 255, 0, 0) with thick stroke (BasicStroke(3))
  - Draw cycle edges after non-cycle edges (middle layer)
  - Render edges in order specified by cycle array
  - Include cycle closure edge (cycle[n-1] to cycle[0])
  - Handle null cycle gracefully (skip cycle edge rendering)
  - _Requirements: 2.1, 2.2, 2.3, 2.4, 12.1, 12.2, 12.3, 12.6, 16.1, 16.5, 18.2_

  - [ ]* 3.1 Write property test for cycle edges colored red
    - **Property 2: Cycle Edges Colored Red**
    - **Validates: Requirements 2.1, 2.5**
    - Test that all edges in cycle array are rendered in red (255, 0, 0)

  - [ ]* 3.2 Write property test for cycle edge thickness
    - **Property 4: Cycle Edges Thicker Than Non-Cycle Edges**
    - **Validates: Requirements 2.2**
    - Test that cycle edges have stroke width 3 while non-cycle edges have width 1

  - [ ]* 3.3 Write property test for cycle edge rendering order
    - **Property 5: Cycle Edges Drawn on Top**
    - **Validates: Requirements 2.4, 18.1, 18.2, 18.5**
    - Test that cycle edges are drawn after non-cycle edges in rendering order

  - [ ]* 3.4 Write property test for cycle path order
    - **Property 13: Cycle Path Rendered in Order**
    - **Validates: Requirements 12.1, 12.2, 12.6**
    - Test that edges are rendered in exact order: cycle[0]→cycle[1], cycle[1]→cycle[2], etc.

  - [ ]* 3.5 Write property test for cycle closure edge
    - **Property 12: Cycle Closure Edge Rendered**
    - **Validates: Requirements 12.3, 16.1, 16.5**
    - Test that edge from cycle[n-1] to cycle[0] is rendered in red with thick stroke

- [ ] 4. Implement vertex rendering with cycle differentiation
  - Render vertices as circles with radius 20 pixels
  - Color cycle vertices blue (RGB: 0, 100, 255)
  - Color non-cycle vertices gray (RGB: 100, 100, 100)
  - Draw vertex borders in black with 2-pixel stroke
  - Draw vertices after edges (foreground layer)
  - Verify cycle membership detection works correctly
  - _Requirements: 3.1, 3.2, 3.3, 17.1, 17.2, 17.4, 18.3, 18.6_

  - [ ]* 4.1 Write property test for cycle vertices colored blue
    - **Property 6: Cycle Vertices Colored Blue**
    - **Validates: Requirements 3.1, 17.1**
    - Test that all vertices in cycle array are rendered in blue (0, 100, 255)

  - [ ]* 4.2 Write property test for non-cycle vertices colored gray
    - **Property 7: Non-Cycle Vertices Colored Gray**
    - **Validates: Requirements 3.2, 17.2**
    - Test that all vertices not in cycle array are rendered in gray (100, 100, 100)

  - [ ]* 4.3 Write property test for vertex borders black
    - **Property 17: Vertex Borders Black**
    - **Validates: Requirements 17.4**
    - Test that all vertex borders are rendered in black

  - [ ]* 4.4 Write property test for vertices drawn last
    - **Property 21: Vertices Drawn Last**
    - **Validates: Requirements 18.3, 18.6**
    - Test that vertices are drawn after all edges in rendering order

- [ ] 5. Implement vertex labels with proper positioning
  - Display vertex index as label (0, 1, 2, ...)
  - Render labels in white color for contrast
  - Use Arial Bold 12pt font
  - Center labels within vertex circles
  - Calculate label position using FontMetrics
  - Verify labels are readable and properly positioned
  - _Requirements: 3.4, 10.1, 10.2, 10.3_

  - [ ]* 5.1 Write property test for all vertices have labels
    - **Property 8: All Vertices Have Labels**
    - **Validates: Requirements 3.4, 10.1, 10.2**
    - Test that every vertex displays its index number as a centered label

  - [ ]* 5.2 Write property test for vertex label color
    - **Property 18: Vertex Labels White**
    - **Validates: Requirements 10.3**
    - Test that all vertex labels are rendered in white color

- [ ] 6. Implement legend rendering
  - Draw legend in top-left corner (x=10, y=20)
  - Display title "Legend:" in Arial Bold 12pt
  - Show cycle vertex explanation (blue square + "Cycle Vertex" text)
  - Show non-cycle vertex explanation (gray square + "Other Vertex" text)
  - Show cycle edge explanation (red line + "Cycle Path" text)
  - Use 20-pixel line height spacing between items
  - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5_

  - [ ]* 6.1 Write property test for legend visibility and position
    - **Property 19: Legend Visible and Positioned**
    - **Validates: Requirements 5.1, 5.5**
    - Test that legend is rendered in top-left corner (x=10, y=20)

  - [ ]* 6.2 Write property test for legend content
    - **Property 20: Legend Contains All Required Items**
    - **Validates: Requirements 5.2, 5.3, 5.4**
    - Test that legend contains explanations for cycle vertices, non-cycle vertices, and cycle edges

- [ ] 7. Implement antialiasing and graphics quality
  - Set RenderingHints.KEY_ANTIALIASING to VALUE_ANTIALIAS_ON
  - Set RenderingHints.KEY_TEXT_ANTIALIASING to VALUE_TEXT_ANTIALIAS_ON
  - Configure Graphics2D for smooth rendering
  - Verify professional appearance of shapes and text
  - _Requirements: 8.1, 8.2_

  - [ ]* 7.1 Write property test for antialiasing applied
    - **Property 16: Antialiasing Applied**
    - **Validates: Requirements 8.1, 8.2**
    - Test that antialiasing rendering hints are set to VALUE_ANTIALIAS_ON

- [ ] 8. Implement null cycle handling
  - Handle null cycle parameter gracefully without exceptions
  - Render all edges in light gray when cycle is null
  - Render all vertices in gray when cycle is null
  - Skip cycle edge rendering when cycle is null
  - Verify visualization remains functional with null cycle
  - _Requirements: 14.1, 14.2, 14.3, 14.4_

  - [ ]* 8.1 Write property test for null cycle handling
    - **Property 14: Null Cycle Handled Gracefully**
    - **Validates: Requirements 14.1, 14.2, 14.3, 14.4**
    - Test that GraphPanel renders without exceptions when cycle is null
    - Test that all vertices rendered in gray and all edges in light gray

- [ ] 9. Implement vertex overlap prevention validation
  - Verify minimum distance between vertices exceeds 2 * VERTEX_RADIUS (40 pixels)
  - Calculate distances between all vertex pairs
  - Validate layout prevents overlap for supported graph sizes (20-30 vertices)
  - Ensure radius of 200 is sufficient for vertex count range
  - _Requirements: 4.3, 15.1, 15.5_

  - [ ]* 9.1 Write property test for no vertex overlap
    - **Property 10: No Vertex Overlap**
    - **Validates: Requirements 4.3, 15.1, 15.5**
    - Test that minimum distance between any two vertices > 40 pixels for graphs with 20-30 vertices

  - [ ]* 9.2 Write property test for vertices within canvas bounds
    - **Property 11: Vertices Within Canvas Bounds**
    - **Validates: Requirements 15.4**
    - Test that all vertex circles are completely within canvas bounds

- [ ] 10. Implement input data immutability validation
  - Verify Graph object is not modified during rendering
  - Verify cycle array is not modified during rendering
  - Ensure no side effects on input parameters
  - _Requirements: 6.2, 13.4_

  - [ ]* 10.1 Write property test for input data not modified
    - **Property 15: Input Data Not Modified**
    - **Validates: Requirements 6.2, 13.4**
    - Test that Graph and cycle array remain unchanged after rendering

- [ ] 11. Implement GraphPanel constructor validation
  - Validate Graph parameter is not null (throw IllegalArgumentException if null)
  - Accept cycle parameter as null (valid case)
  - Initialize vertex position arrays
  - Set background color to white
  - _Requirements: 6.1, 13.3_

  - [ ]* 11.1 Write property test for GraphPanel initialization
    - **Property 25: GraphPanel Accepts Graph and Cycle**
    - **Validates: Requirements 6.1, 13.3**
    - Test that GraphPanel successfully initializes with valid Graph and any cycle (including null)

- [ ] 12. Implement scalability validation
  - Verify layout works correctly for graphs with 20 vertices
  - Verify layout works correctly for graphs with 25 vertices
  - Verify layout works correctly for graphs with 30+ vertices
  - Ensure readability maintained across vertex count range
  - _Requirements: 9.1, 9.2, 9.3_

  - [ ]* 12.1 Write property test for scalable layout
    - **Property 23: Scalable Layout**
    - **Validates: Requirements 9.1, 9.2, 9.3**
    - Test that circular layout prevents overlap and maintains readability for 20-30 vertices

- [ ] 13. Implement visual distinction validation
  - Verify cycle edges are visually distinct from non-cycle edges
  - Confirm distinction through color (red vs. light gray) and thickness (3 vs. 1)
  - Ensure visual hierarchy is clear
  - _Requirements: 2.3, 16.3_

  - [ ]* 13.1 Write property test for cycle edges visually distinct
    - **Property 24: Cycle Edges Visually Distinct**
    - **Validates: Requirements 2.3, 16.3**
    - Test that cycle edges are visually distinct through color and thickness

- [ ] 14. Checkpoint - Verify GraphPanel rendering complete
  - Ensure all rendering methods are implemented and functional
  - Verify paintComponent() method renders all layers correctly
  - Confirm all properties pass validation
  - Ensure no exceptions thrown during rendering
  - Ask the user if questions arise.

- [ ] 15. Integrate GraphPanel with Main class
  - Create Main class if not exists
  - Import necessary Swing classes (JFrame, JPanel)
  - Create method to initialize GraphData with 20+ vertices
  - Create method to find Hamiltonian cycle using HamiltonianCycle
  - Create method to launch GraphPanel visualization in JFrame
  - Set JFrame title to "Graph Visualization"
  - Set JFrame size to 600x600 pixels
  - Set JFrame to visible
  - Add GraphPanel to JFrame content pane
  - _Requirements: 1.4, 1.5, 11.1, 11.2, 11.3, 11.4, 11.5, 11.6, 20.1, 20.2, 20.3, 20.4, 20.5, 20.6_

- [ ] 16. Implement Main workflow orchestration
  - Initialize GraphData to create graph with 20+ vertices
  - Execute HamiltonianCycle algorithm to find cycle
  - Print cycle to console using Printer (if cycle found)
  - Create GraphPanel with Graph and cycle
  - Display GraphPanel in JFrame window
  - Handle case where no cycle is found (display message, show visualization anyway)
  - Ensure console output and visualization work independently
  - _Requirements: 6.3, 6.4, 6.5, 6.6, 13.1, 13.2, 13.5, 13.6, 19.1, 19.2, 19.3, 19.4, 19.5, 19.6, 20.1, 20.2, 20.3, 20.4, 20.5, 20.6_

- [ ] 17. Implement error handling in Main class
  - Handle null Graph gracefully
  - Handle null cycle gracefully (visualization still displays)
  - Catch and handle exceptions during visualization launch
  - Provide meaningful error messages
  - Ensure application doesn't crash on visualization errors
  - _Requirements: 14.1, 14.2, 14.3, 14.4, 19.4_

- [ ] 18. Checkpoint - Verify Main integration complete
  - Ensure Main class successfully creates and displays GraphPanel
  - Verify visualization launches after algorithm execution
  - Confirm console output and visualization work together
  - Verify window close functionality works
  - Ensure no exceptions thrown during workflow
  - Ask the user if questions arise.

- [ ] 19. Write comprehensive unit tests for GraphPanel
  - Test constructor with valid Graph and cycle
  - Test constructor with valid Graph and null cycle
  - Test constructor with null Graph (should throw exception)
  - Test vertex position calculations for various vertex counts
  - Test that all graph edges are rendered
  - Test that cycle edges are rendered in correct color
  - Test that non-cycle edges are rendered in correct color
  - Test that cycle vertices are rendered in blue
  - Test that non-cycle vertices are rendered in gray
  - Test that vertex labels are rendered correctly
  - Test that legend is rendered with all items
  - Test rendering with null cycle
  - Test edge cases (minimum vertex count, larger vertex counts)
  - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1, 3.2, 3.3, 3.4, 3.5, 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 6.1, 6.2, 6.3, 6.4, 6.5, 6.6, 7.1, 7.2, 7.3, 7.4, 7.5, 7.6, 8.1, 8.2, 8.3, 8.4, 8.5, 8.6, 9.1, 9.2, 9.3, 9.4, 9.5, 9.6, 10.1, 10.2, 10.3, 10.4, 10.5, 10.6, 14.1, 14.2, 14.3, 14.4, 14.5, 14.6_

- [ ] 20. Write integration tests for Main workflow
  - Test complete workflow: GraphData → HamiltonianCycle → GraphPanel
  - Test visualization launch after algorithm execution
  - Test console output and visualization work together
  - Test window close functionality
  - Test with various graph sizes (20, 25, 30 vertices)
  - Test with graphs that have cycles
  - Test with graphs that don't have cycles (null cycle handling)
  - Verify no exceptions thrown during complete workflow
  - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 6.1, 6.2, 6.3, 6.4, 6.5, 6.6, 11.1, 11.2, 11.3, 11.4, 11.5, 11.6, 13.1, 13.2, 13.3, 13.4, 13.5, 13.6, 19.1, 19.2, 19.3, 19.4, 19.5, 19.6, 20.1, 20.2, 20.3, 20.4, 20.5, 20.6_

- [ ] 21. Final checkpoint - Ensure all tests pass
  - Run all unit tests for GraphPanel
  - Run all property-based tests for correctness properties
  - Run all integration tests for Main workflow
  - Verify 85%+ code coverage of GraphPanel
  - Verify all 25 correctness properties validated
  - Ensure no failing tests
  - Ask the user if questions arise.

## Notes

- Tasks marked with `*` are optional property-based and unit tests that can be skipped for faster MVP
- Each task references specific requirements for traceability
- Checkpoints (tasks 14, 18, 21) ensure incremental validation
- Property tests validate universal correctness properties from design document
- Unit tests validate specific examples and edge cases
- Integration tests validate complete workflow
- GraphPanel is partially implemented; tasks focus on completion and validation
- Main class is empty; tasks focus on creating orchestration logic
- All code should follow existing style and conventions in the codebase
