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
        double totalCost = 0.0;
        double totalWeight = 0.0;
        
        for (ManufacturingProcess process : completedProcesses) {
            Product product = process.getProduct();
            ManufacturingOutcome outcome = process.getFinalOutcome();
            
            switch (outcome) {
                case COMPLETED:
                    successCount++;
                    totalCost += product.getCost();
                    totalWeight += product.getWeight();
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
        
        // Print a more visually appealing report
        String border = "+---------------------------------------------------------------------------+";
        String titleBorder = "+===========================================================================+";
        
        System.out.println("\n" + titleBorder);
        System.out.println("|                      MANUFACTURING SYSTEM REPORT                        |");
        System.out.println(titleBorder);
        
        // Success section
        System.out.println("|                                                                         |");
        System.out.println("| 1. SUCCESSFULLY MANUFACTURED PRODUCTS                                   |");
        System.out.println("|    ----------------------------------------                             |");
        System.out.printf("| ▶ Successfully manufactured products: %-36d |\n", successCount);
        System.out.printf("| ▶ Total cost: %-52s |\n", String.format("%.2f", totalCost).replace(".", ",") + " TL");
        System.out.printf("| ▶ Total weight: %-50s |\n", String.format("%.2f", totalWeight).replace(".", ",") + " kg");
        
        // Failed products section
        System.out.println("|                                                                         |");
        System.out.println("| MANUFACTURING FAILURES                                                  |");
        System.out.println("|    ---------------------                                                |");
        System.out.printf("| 2. Products failed by system error: %-35d |\n", systemErrorCount);
        System.out.printf("| 3. Products failed by damaged component: %-31d |\n", damagedComponentCount);
        System.out.printf("| 4. Products failed by stock shortage: %-34d |\n", stockShortageCount);
        System.out.println("|                                                                         |");
        System.out.println(titleBorder);
    }
} 