import system.ManufacturingSystem;

public class Main {
    public static void main(String[] args) {
        String componentsFilePath = "components.csv";
        String productsFilePath = "products.csv";
        
        ManufacturingSystem manufacturingSystem = new ManufacturingSystem(
            componentsFilePath, productsFilePath);
        
        manufacturingSystem.run();
    }
} 