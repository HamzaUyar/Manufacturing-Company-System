package system;

import component.Product;
import process.ManufacturingOutcome;
import process.ManufacturingProcess;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

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
        
        int totalProcesses = completedProcesses.size();
        double successRate = (double) successCount / totalProcesses * 100;
        
        // Prepare sorted list of products for consistent ordering
        List<String> sortedProductNames = new ArrayList<>(productSuccessCount.keySet());
        Collections.sort(sortedProductNames);
        
        // Calculate grand totals
        double grandTotalCost = 0.0;
        double grandTotalWeight = 0.0;
        for (String product : productSuccessCount.keySet()) {
            grandTotalCost += productTotalCost.get(product);
            grandTotalWeight += productTotalWeight.get(product);
        }
        
        // Print the end-of-run report
        System.out.println("\n===== END-OF-RUN MANUFACTURING REPORT =====\n");
        
        // 1. Successfully manufactured products
        System.out.println("1. SUCCESSFULLY MANUFACTURED PRODUCTS");
        System.out.println("   Successfully manufactured products: " + successCount);
        System.out.println("   Total cost: " + String.format("%.2f", grandTotalCost).replace(".", ",") + " TL");
        System.out.println("   Total weight: " + String.format("%.2f", grandTotalWeight).replace(".", ",") + " kg");
        
        // 2. Failed products by category
        System.out.println("\n2. FAILED PRODUCTS");
        System.out.println("   Failed by system error: " + systemErrorCount);
        System.out.println("   Failed by damaged component: " + damagedComponentCount);
        System.out.println("   Failed by stock shortage: " + stockShortageCount);
        
        System.out.println("\n===== END OF REPORT =====\n");
    }
} 