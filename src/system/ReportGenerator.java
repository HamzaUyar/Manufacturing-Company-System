package system;

import component.Product;
import process.ManufacturingOutcome;
import process.ManufacturingProcess;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
        
        // Print the report with enhanced formatting
        String border = "══════════════════════════════════════════════════════════════";
        String subBorder = "──────────────────────────────────────────────────────────────";
        
        System.out.println("\n" + border);
        System.out.println("║               MANUFACTURING REPORT                       ║");
        System.out.println(border);
        
        System.out.println("\n▶ 1. SUCCESSFULLY MANUFACTURED PRODUCTS");
        System.out.println(subBorder);
        
        // Format as string with comma for decimal separator
        String successRateStr = String.format("%.1f", successRate).replace(".", ",");
        System.out.printf("  Success Rate: %s%% (%d of %d attempts)\n", 
                successRateStr, successCount, totalProcesses);
                
        if (successCount > 0) {
            System.out.println("\n  PRODUCT DETAILS:");
            
            // Define column widths
            int col1Width = 14;  // Product name
            int col2Width = 6;   // Units
            int col3Width = 12;  // Cost
            int col4Width = 12;  // Weight
            
            // Fixed-width table borders
            String tableBorder = String.format("  ┌─%s─┬─%s─┬─%s─┬─%s─┐",
                    "─".repeat(col1Width), "─".repeat(col2Width), "─".repeat(col3Width), "─".repeat(col4Width));
            String tableHeaderBorder = String.format("  ├─%s─┼─%s─┼─%s─┼─%s─┤",
                    "─".repeat(col1Width), "─".repeat(col2Width), "─".repeat(col3Width), "─".repeat(col4Width));
            String tableBottomBorder = String.format("  └─%s─┴─%s─┴─%s─┴─%s─┘",
                    "─".repeat(col1Width), "─".repeat(col2Width), "─".repeat(col3Width), "─".repeat(col4Width));
            
            System.out.println(tableBorder);
            
            // Header row
            System.out.printf("  │ %-"+col1Width+"s │ %-"+col2Width+"s │ %-"+col3Width+"s │ %-"+col4Width+"s │\n",
                    "Product", "Units", "Total Cost", "Total Weight");
            
            System.out.println(tableHeaderBorder);
            
            // Product rows
            for (String productName : sortedProductNames) {
                int count = productSuccessCount.get(productName);
                double totalCost = productTotalCost.get(productName);
                double totalWeight = productTotalWeight.get(productName);
                
                // Using comma as decimal separator to match locale
                String costStr = String.format("%.2f", totalCost).replace(".", ",") + " TL";
                String weightStr = String.format("%.2f", totalWeight).replace(".", ",") + " kg";
                
                System.out.printf("  │ %-"+col1Width+"s │ %"+col2Width+"d │ %"+col3Width+"s │ %"+col4Width+"s │\n", 
                        productName, count, costStr, weightStr);
            }
            
            System.out.println(tableHeaderBorder);
            
            // Grand total row
            String grandCostStr = String.format("%.2f", grandTotalCost).replace(".", ",") + " TL";
            String grandWeightStr = String.format("%.2f", grandTotalWeight).replace(".", ",") + " kg";
            
            System.out.printf("  │ %-"+col1Width+"s │ %"+col2Width+"d │ %"+col3Width+"s │ %"+col4Width+"s │\n", 
                    "GRAND TOTAL", successCount, grandCostStr, grandWeightStr);
            
            System.out.println(tableBottomBorder);
        }
        
        System.out.println("\n▶ 2. MANUFACTURING FAILURES");
        System.out.println(subBorder);
        
        int totalFailures = systemErrorCount + damagedComponentCount + stockShortageCount;
        System.out.println("  Summary:");
        
        // Define column widths for failure table
        int fcol1Width = 18;  // Failure type
        int fcol2Width = 6;   // Count
        int fcol3Width = 10;  // Percentage
        
        // Fixed-width failure table borders
        String ftableBorder = String.format("  ┌─%s─┬─%s─┬─%s─┐",
                "─".repeat(fcol1Width), "─".repeat(fcol2Width), "─".repeat(fcol3Width));
        String ftableHeaderBorder = String.format("  ├─%s─┼─%s─┼─%s─┤",
                "─".repeat(fcol1Width), "─".repeat(fcol2Width), "─".repeat(fcol3Width));
        String ftableBottomBorder = String.format("  └─%s─┴─%s─┴─%s─┘",
                "─".repeat(fcol1Width), "─".repeat(fcol2Width), "─".repeat(fcol3Width));
        
        System.out.println(ftableBorder);
        
        // Header row
        System.out.printf("  │ %-"+fcol1Width+"s │ %-"+fcol2Width+"s │ %-"+fcol3Width+"s │\n",
                "Failure Type", "Count", "Percentage");
        
        System.out.println(ftableHeaderBorder);
        
        // Format percentages with comma as decimal separator
        String sysErrorPct = String.format("%.1f", (double) systemErrorCount / totalProcesses * 100).replace(".", ",") + "%";
        String damCompPct = String.format("%.1f", (double) damagedComponentCount / totalProcesses * 100).replace(".", ",") + "%";
        String stockPct = String.format("%.1f", (double) stockShortageCount / totalProcesses * 100).replace(".", ",") + "%";
        String totalPct = String.format("%.1f", (double) totalFailures / totalProcesses * 100).replace(".", ",") + "%";
        
        System.out.printf("  │ %-"+fcol1Width+"s │ %"+fcol2Width+"d │ %"+fcol3Width+"s │\n", 
                "System Error", systemErrorCount, sysErrorPct);
        System.out.printf("  │ %-"+fcol1Width+"s │ %"+fcol2Width+"d │ %"+fcol3Width+"s │\n", 
                "Damaged Component", damagedComponentCount, damCompPct);
        System.out.printf("  │ %-"+fcol1Width+"s │ %"+fcol2Width+"d │ %"+fcol3Width+"s │\n", 
                "Stock Shortage", stockShortageCount, stockPct);
        
        System.out.println(ftableHeaderBorder);
        
        System.out.printf("  │ %-"+fcol1Width+"s │ %"+fcol2Width+"d │ %"+fcol3Width+"s │\n", 
                "TOTAL FAILURES", totalFailures, totalPct);
        
        System.out.println(ftableBottomBorder);
        
        System.out.println("\n" + border);
        System.out.println("║                  END OF REPORT                          ║");
        System.out.println(border + "\n");
    }
} 