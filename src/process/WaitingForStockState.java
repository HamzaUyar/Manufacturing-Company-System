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
            // Deduct stock for all components
            product.deductStock(inventory, 1);
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
} 