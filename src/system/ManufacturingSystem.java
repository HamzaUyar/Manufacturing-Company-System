package system;

/**
 * Main system class that delegates to the controller.
 */
public class ManufacturingSystem {
    private final String componentsFilePath;
    private final String productsFilePath;
    private final ManufacturingController controller;
    
    public ManufacturingSystem(String componentsFilePath, String productsFilePath) {
        this.componentsFilePath = componentsFilePath;
        this.productsFilePath = productsFilePath;
        this.controller = new ManufacturingController();
    }
    
    public void run() {
        controller.processManufacturingOrders(componentsFilePath, productsFilePath);
    }
}