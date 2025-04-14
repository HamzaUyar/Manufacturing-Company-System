# Manufacturing Company System

This project implements a Manufacturing Company System in Java, utilizing the following design patterns:

- **Composite Design Pattern**: For representing the component hierarchy (raw materials, paint, hardware, and products)
- **State Design Pattern**: For representing the manufacturing process states
- **GRASP**: For system organization and responsibility assignment

## Project Structure

The project follows a simple package structure:

```
src/
├── component/       # Component classes (Composite Pattern)
├── process/         # Process state classes (State Pattern)
├── system/          # System management classes
├── resources/       # Data files
│   ├── components.csv  # Data for basic components
│   └── products.csv    # Data for products and recipes
└── Main.java        # Entry point
```

### Packages and Classes:

- `component`: Contains the Component interface and its implementations
  - `Component`: Interface defining methods for component operations
  - `BasicComponent`: Abstract class for leaf components
  - `RawMaterial`, `Paint`, `Hardware`: Concrete leaf components
  - `Product`: Composite component that may contain other components

- `process`: Contains classes related to the manufacturing process
  - `ProcessState`: Interface for different manufacturing states
  - `WaitingForStockState`, `InManufacturingState`, `CompletedState`, `FailedState`: Concrete states
  - `ManufacturingProcess`: Context class that manages state transitions
  - `ManufacturingOutcome`: Enum for possible manufacturing outcomes

- `system`: Contains system management classes
  - `InventoryManager`: Manages stock levels for components
  - `DataLoader`: Loads data from CSV files
  - `ManufacturingOrder`: Data class for product manufacturing orders
  - `ReportGenerator`: Generates summary reports
  - `ManufacturingSystem`: Main controller class that orchestrates the process

- `Main`: Entry point of the application

## Design Patterns Implementation

### Composite Pattern

- **Component Interface**: Defines operations for both simple and complex components
- **Leaf Components**: Raw materials, paint, and hardware components that cannot be broken down further
- **Composite Component**: Product that can contain other components

### State Pattern

- **Process States**: Different states of the manufacturing process (WaitingForStock, InManufacturing, Completed, Failed)
- **Context**: ManufacturingProcess that maintains the current state and delegates state-specific behavior

### GRASP Principles

- **Information Expert**: Classes have responsibilities that require their specific information
- **Creator**: Objects are created by classes that have the necessary information to create them
- **Controller**: ManufacturingSystem acts as a controller coordinating system activities
- **Low Coupling**: Interactions between components are minimized through well-defined interfaces
- **High Cohesion**: Classes have clearly defined, focused responsibilities

## How to Compile and Run

To compile and run the project:

```bash
# Compile
javac -d bin -cp src src/component/*.java src/process/*.java src/system/*.java src/Main.java

# Run
java -cp bin Main
```

## Manufacturing Process

Each product goes through several states:

1. **WaitingForStock**: Checks if required components are available in sufficient quantities
2. **InManufacturing**: Random simulation of manufacturing outcome (success, system error, damaged component)
3. **Completed**: Final state for successful manufacturing
4. **Failed**: Final state for unsuccessful manufacturing

## Report Generation

After manufacturing all products, a report is generated showing:
1. Successfully manufactured products with their total cost and weight
2. Manufacturing failures categorized by reason (stock shortage, system error, damaged component)
