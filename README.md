# Manufacturing Company System

This project implements a Manufacturing Company System in Java, utilizing the following design patterns and principles:

- **Composite Design Pattern**: For representing the component hierarchy (raw materials, paint, hardware, and products)
- **State Design Pattern**: For representing the manufacturing process states
- **Observer Design Pattern**: For monitoring and logging the manufacturing process
- **Factory Method Pattern**: For component creation
- **GRASP Principles**: For system organization and responsibility assignment

## Project Structure

The project follows a package structure organized by design pattern and responsibility:

```
src/
├── component/       # Component classes (Composite Pattern)
│   └── factory/     # Component factory (Factory Method Pattern)
├── process/         # Process state classes (State Pattern)
├── observer/        # Observer classes for monitoring process
├── system/          # System management classes
│   ├── ManufacturingController  # Controller following GRASP principles
│   └── ManufacturingSystem      # Main system class
├── Main.java        # Entry point
├── components.csv   # Data for basic components
└── products.csv     # Data for products and recipes
```

### Packages and Classes:

- `component`: Contains the Component interface and its implementations
  - `Component`: Interface defining methods for component operations
  - `BasicComponent`: Abstract class for leaf components
  - `RawMaterial`, `Paint`, `Hardware`: Concrete leaf components
  - `Product`: Composite component that may contain other components
  - `factory/ComponentFactory`: Factory for creating components based on type

- `process`: Contains classes related to the manufacturing process
  - `ProcessState`: Interface for different manufacturing states
  - `WaitingForStockState`, `InManufacturingState`, `CompletedState`, `FailedState`: Concrete states
  - `ManufacturingProcess`: Context class that manages state transitions
  - `ManufacturingOutcome`: Enum for possible manufacturing outcomes

- `observer`: Contains classes for monitoring the manufacturing process
  - `ProcessObserver`: Interface for observing state changes
  - `ConsoleLogger`: Concrete observer that logs process events to console

- `system`: Contains system management classes
  - `InventoryManager`: Manages stock levels for components
  - `DataLoader`: Loads data from CSV files with robust error handling
  - `ManufacturingOrder`: Data class for product manufacturing orders
  - `ReportGenerator`: Generates summary reports
  - `ManufacturingController`: Controller class implementing GRASP Controller pattern
  - `ManufacturingSystem`: Main system class that delegates to the controller

- `Main`: Entry point of the application

## Design Patterns Implementation

### Composite Pattern

- **Component Interface**: Defines operations for both simple and complex components
- **Leaf Components**: Raw materials, paint, and hardware components that cannot be broken down further
- **Composite Component**: Product that can contain other components (including other products, supporting recursion)

### State Pattern

- **Context**: ManufacturingProcess maintains the current state and delegates state-specific behavior
- **Process States**: Different states of the manufacturing process with specific behaviors
  - **WaitingForStock**: Checks if required components are available in sufficient quantities
  - **InManufacturing**: Random simulation of manufacturing outcome (success, system error, damaged component)
  - **Completed**: Final state for successful manufacturing
  - **Failed**: Final state for unsuccessful manufacturing

### Observer Pattern

- **Subject**: ManufacturingProcess maintains a list of observers and notifies them of state changes
- **Observer Interface**: ProcessObserver defines methods for receiving notifications
- **Concrete Observer**: ConsoleLogger logs state transitions and component details

### Factory Method Pattern

- **Factory**: ComponentFactory creates appropriate component types based on input parameters
- **Products**: Different component types (RawMaterial, Hardware, Paint, Product)

### GRASP Principles

- **Information Expert**: Classes have responsibilities that require their specific information
- **Creator**: Objects are created by classes that have the necessary information to create them
- **Controller**: ManufacturingController acts as a controller coordinating system activities
- **Low Coupling**: Interactions between components are minimized through well-defined interfaces
- **High Cohesion**: Classes have clearly defined, focused responsibilities

## Manufacturing Process

Each product goes through a workflow:

1. **WaitingForStock**: Checks if required components are available in inventory
2. **InManufacturing**: Simulates manufacturing with random outcomes
   - Random outcome 1: Success (transition to Completed)
   - Random outcome 2: System error (transition to Failed)
   - Random outcome 3: Damaged component (transition to Failed)
3. **Completed/Failed**: Terminal states

## Report Generation

After manufacturing all products, a report is generated showing:

1. Successfully manufactured products: count, total cost, and total weight
2. Products failed by system error: count
3. Products failed by damaged component: count
4. Products failed by stock shortage: count
