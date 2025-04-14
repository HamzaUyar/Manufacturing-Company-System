import system.ManufacturingSystem;

public class Main {
    public static void main(String[] args) {
        String componentsFilePath = "src/resources/components.csv";
        String productsFilePath = "src/resources/products.csv";
        
        ManufacturingSystem manufacturingSystem = new ManufacturingSystem(
            componentsFilePath, productsFilePath);
        
        manufacturingSystem.run();
    }
} 