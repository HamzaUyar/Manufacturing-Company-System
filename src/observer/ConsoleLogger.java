package observer;

import component.Component;
import process.*;

import java.util.Map;

public class ConsoleLogger implements ProcessObserver {
    @Override
    public void onStateChange(ManufacturingProcess process, ProcessState oldState, ProcessState newState) {
        String productName = process.getProduct().getName();
        String oldStateName = oldState != null ? oldState.getClass().getSimpleName() : "Initial";
        String newStateName = newState.getClass().getSimpleName();
        
        // Remove "State" suffix for cleaner output
        oldStateName = oldStateName.replace("State", "");
        newStateName = newStateName.replace("State", "");
        
        // For failed states, include the reason
        String additionalInfo = "";
        if (newState instanceof FailedState) {
            ManufacturingOutcome outcome = process.getFinalOutcome();
            if (outcome == ManufacturingOutcome.FAILED_STOCK_SHORTAGE) {
                additionalInfo = " (Reason: Stock Shortage)";
            } else if (outcome == ManufacturingOutcome.FAILED_SYSTEM_ERROR) {
                additionalInfo = " (Reason: System Error)";
            } else if (outcome == ManufacturingOutcome.FAILED_DAMAGED_COMPONENT) {
                additionalInfo = " (Reason: Damaged Component)";
            }
        }
        
        System.out.printf("Product '%s': State changed from %s to %s%s\n", 
                productName, oldStateName, newStateName, additionalInfo);
        System.out.printf("  Context: %s\n", newState.getStateContext());
                
        // If it's the first state transition, print product component details
        if (oldState == null) {
            System.out.println("  Component details:");
            Map<Component, Double> components = process.getProduct().getRequiredComponents();
            for (Map.Entry<Component, Double> entry : components.entrySet()) {
                Component component = entry.getKey();
                Double quantity = entry.getValue();
                System.out.printf("    - %s: %.2f (Cost: %.2f TL, Weight: %.2f kg)\n", 
                        component.getName(), quantity, component.getCost(), component.getWeight());
            }
            System.out.println();
        }
    }
    
    @Override
    public void onProcessCompleted(ManufacturingProcess process) {
        String productName = process.getProduct().getName();
        ManufacturingOutcome outcome = process.getFinalOutcome();
        
        // Check if outcome is null to avoid NullPointerException
        String outcomeStr = outcome != null ? outcome.toString() : "UNKNOWN";
        
        System.out.printf("Process for '%s' completed with outcome: %s\n\n", 
                productName, outcomeStr);
    }
} 