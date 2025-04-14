package system;

import component.Product;
import process.ManufacturingProcess;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManufacturingSystem {
    private final InventoryManager inventoryManager;
    private final DataLoader dataLoader;
    private final ReportGenerator reportGenerator;
    private final String componentsFilePath;
    private final String productsFilePath;
    
    public ManufacturingSystem(String componentsFilePath, String productsFilePath) {
        this.componentsFilePath = componentsFilePath;
        this.productsFilePath = productsFilePath;
        this.inventoryManager = new InventoryManager();
        this.dataLoader = new DataLoader();
        this.reportGenerator = new ReportGenerator();
    }
    
    public void run() {
        try {
            // Load data and initialize inventory
            List<ManufacturingOrder> orders = dataLoader.loadDataAndInitialize(
                componentsFilePath, productsFilePath, inventoryManager);
            
            // Process all manufacturing orders
            List<ManufacturingProcess> allResults = new ArrayList<>();
            
            // Iterate through each order
            for (ManufacturingOrder order : orders) {
                Product product = order.getProduct();
                int quantityToMake = order.getQuantity();
                
                // Try to manufacture each individual product
                for (int i = 0; i < quantityToMake; i++) {
                    ManufacturingProcess process = new ManufacturingProcess(product, inventoryManager);
                    
                    // Process until completed or failed
                    while (!process.isCompleted()) {
                        process.process();
                    }
                    
                    // Add to results
                    allResults.add(process);
                }
            }
            
            // Generate final report
            reportGenerator.generateReport(allResults);
            
        } catch (IOException e) {
            System.err.println("Error processing manufacturing system: " + e.getMessage());
            e.printStackTrace();
        }
    }
}