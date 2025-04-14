# UML Class Diagram for Manufacturing System

This directory contains the PlantUML code for visualizing the class structure of the Manufacturing Company System.

## Understanding the Diagram

The UML class diagram illustrates:

1. **Composite Pattern Implementation**:
   - The `Component` interface provides a unified interface for products and basic components
   - `BasicComponent` is an abstract class for leaf components
   - Concrete leaf components: `RawMaterial`, `Paint`, and `Hardware`
   - `Product` is the composite component that can contain other components

2. **State Pattern Implementation**:
   - The `ProcessState` interface defines the behavior for different manufacturing states
   - Concrete states: `WaitingForStockState`, `InManufacturingState`, `CompletedState`, and `FailedState`
   - `ManufacturingProcess` acts as the context that maintains the current state

3. **GRASP Principles**:
   - Information Expert: Classes like `InventoryManager` and `DataLoader` manage their respective domain data
   - Controller: `ManufacturingSystem` coordinates the manufacturing process
   - Creator: Appropriate classes create instances they're responsible for

## Viewing the Diagram

To view the UML diagram:

1. **Online PlantUML Editor**:
   - Copy the content of `class_diagram.puml`
   - Paste it into an online PlantUML viewer like [PlantUML Web Server](http://www.plantuml.com/plantuml/uml)

2. **VS Code with PlantUML extension**:
   - Install the "PlantUML" extension for VS Code
   - Open the .puml file and use Alt+D to preview

3. **Generate an image**:
   - If you have PlantUML installed locally, run:
     ```
     plantuml class_diagram.puml
     ```
   - This will generate a .png image of the diagram in the same directory 