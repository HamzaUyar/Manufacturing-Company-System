package system;

import component.Product;
import observer.ConsoleLogger;
import process.ManufacturingProcess;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class that coordinates the manufacturing process
 * according to the GRASP Controller pattern.
 */
public class ManufacturingController {
    private final DataLoader dataLoader;
    private final InventoryManager inventoryManager;
    private final ReportGenerator reportGenerator;
    private final ConsoleLogger consoleLogger;
    
    public ManufacturingController() {
        this.dataLoader = new DataLoader();
        this.inventoryManager = new InventoryManager();
        this.reportGenerator = new ReportGenerator();
        this.consoleLogger = new ConsoleLogger();
    }
    
    /**
     * Loads data and processes manufacturing orders
     */
    public void processManufacturingOrders(String componentsFilePath, String productsFilePath) {
        try {
            // Load data and initialize inventory
            List<ManufacturingOrder> orders = dataLoader.loadDataAndInitialize(
                componentsFilePath, productsFilePath, inventoryManager);
            
            // Process all manufacturing orders
            List<ManufacturingProcess> allResults = processOrders(orders);
            
            // Generate final report
            reportGenerator.generateReport(allResults);
            
        } catch (IOException e) {
            System.err.println("Error processing manufacturing system: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Processes orders in the exact sequence they appear in the CSV file
     */
    private List<ManufacturingProcess> processOrders(List<ManufacturingOrder> orders) {
        List<ManufacturingProcess> allResults = new ArrayList<>();
        
        System.out.println("\n== MANUFACTURING PROCESS DETAILS ==\n");
        
        // Iterate through each order in the exact order they appear in the CSV
        for (ManufacturingOrder order : orders) {
            Product product = order.getProduct();
            int quantityToMake = order.getQuantity();
            
            // Try to manufacture each individual product
            for (int i = 0; i < quantityToMake; i++) {
                ManufacturingProcess process = createManufacturingProcess(product);
                
                // Process until completed or failed
                completeManufacturingProcess(process);
                
                // Add to results
                allResults.add(process);
            }
        }
        
        return allResults;
    }
    
    /**
     * Creates a new manufacturing process with observer attached
     */
    private ManufacturingProcess createManufacturingProcess(Product product) {
        ManufacturingProcess process = new ManufacturingProcess(product, inventoryManager);
        process.addObserver(consoleLogger);
        return process;
    }
    
    /**
     * Completes a manufacturing process
     */
    private void completeManufacturingProcess(ManufacturingProcess process) {
        while (!process.isCompleted()) {
            process.process();
        }
    }
} 