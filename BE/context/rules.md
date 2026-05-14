# Instruction Rules

## R1: Algorithm Selection
*   When faced with a logic problem, first identify and justify whether the optimal solution requires **Backtracking**, **Greedy**, **Dynamic Programming**, or **Divide and Conquer**.
*   Classify the problem by type (**Search**, **Sorting**, **Optimization**, or **Traversal**) to apply the appropriate design pattern.

## R2: Efficiency and Pruning
*   In **Backtracking** algorithms, it is mandatory to include pruning conditions to avoid unnecessary exploration of the search space.
*   In **Dynamic Programming**, prioritize memory savings and execution speed (time complexity).

## R3: Code Standards and SOLID
*   Maintain the **Single Responsibility Principle (SRP)** in every generated class.
*   Variable names must reflect their purpose within the algorithm (e.g., `minimumCost`, `bestPath`).

## R4: Testing Quality
*   Every algorithm must be validated with unit tests covering **base cases**, **general cases**, and **edge cases**.
*   Ensure the code is testable by avoiding tight coupling.

## R5: Suggested Interaction
*   If the problem statement is ambiguous, ask for clarifications regarding **time or memory constraints** before proposing a definitive solution.