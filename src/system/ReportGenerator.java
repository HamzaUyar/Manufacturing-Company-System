package system;

import component.Product;
import process.ManufacturingOutcome;
import process.ManufacturingProcess;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGenerator {
    
    public void generateReport(List<ManufacturingProcess> completedProcesses) {
        int successCount = 0;
        int stockShortageCount = 0;
        int systemErrorCount = 0;
        int damagedComponentCount = 0;
        
        Map<String, Integer> productSuccessCount = new HashMap<>();
        Map<String, Double> productTotalCost = new HashMap<>();
        Map<String, Double> productTotalWeight = new HashMap<>();
        
        for (ManufacturingProcess process : completedProcesses) {
            Product product = process.getProduct();
            String productName = product.getName();
            ManufacturingOutcome outcome = process.getFinalOutcome();
            
            switch (outcome) {
                case COMPLETED:
                    successCount++;
                    
                    // Track successful products for detailed reporting
                    productSuccessCount.put(productName, 
                                          productSuccessCount.getOrDefault(productName, 0) + 1);
                    
                    // Add cost and weight to totals
                    productTotalCost.put(productName, 
                                       productTotalCost.getOrDefault(productName, 0.0) + product.getCost());
                    
                    productTotalWeight.put(productName, 
                                         productTotalWeight.getOrDefault(productName, 0.0) + product.getWeight());
                    break;
                    
                case FAILED_STOCK_SHORTAGE:
                    stockShortageCount++;
                    break;
                    
                case FAILED_SYSTEM_ERROR:
                    systemErrorCount++;
                    break;
                    
                case FAILED_DAMAGED_COMPONENT:
                    damagedComponentCount++;
                    break;
            }
        }
        
        // Print the report
        System.out.println("======= MANUFACTURING REPORT =======");
        System.out.println("1. SUCCESSFULLY MANUFACTURED PRODUCTS");
        System.out.println("Total: " + successCount);
        
        for (String productName : productSuccessCount.keySet()) {
            int count = productSuccessCount.get(productName);
            double totalCost = productTotalCost.get(productName);
            double totalWeight = productTotalWeight.get(productName);
            
            System.out.println("  " + productName + ": " + count + " units");
            System.out.println("    Total Cost: " + String.format("%.2f", totalCost) + " TL");
            System.out.println("    Total Weight: " + String.format("%.2f", totalWeight) + " kg");
        }
        
        System.out.println("\n2. MANUFACTURING FAILURES");
        System.out.println("  System Error: " + systemErrorCount);
        System.out.println("  Damaged Component: " + damagedComponentCount);
        System.out.println("  Stock Shortage: " + stockShortageCount);
        System.out.println("======= END OF REPORT =======");
    }
} 