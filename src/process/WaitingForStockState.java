package process;

import component.Product;
import system.InventoryManager;

public class WaitingForStockState implements ProcessState {
    
    @Override
    public void handleProcess(ManufacturingProcess context) {
        Product product = context.getProduct();
        InventoryManager inventory = context.getInventoryManager();
        
        // Check if we have sufficient stock
        if (product.checkStockAvailability(inventory, 1)) {
            // Only check stock availability without deducting it here
            // Stock will be deducted only if manufacturing is successful
            
            // Transition to InManufacturing state
            context.setState(new InManufacturingState());
        } else {
            // Not enough stock, transition to Failed state
            context.setState(new FailedState());
            context.recordResult(ManufacturingOutcome.FAILED_STOCK_SHORTAGE);
        }
    }
    
    @Override
    public boolean isTerminal() {
        return false;
    }
    
    @Override
    public String getStateContext() {
        return "Checking inventory for required components";
    }
} 