package process;

import java.util.Random;
import component.Product;
import system.InventoryManager;

public class InManufacturingState implements ProcessState {
    private final Random random = new Random();
    
    @Override
    public void handleProcess(ManufacturingProcess context) {
        // Generate a random number between 1 and 3 to determine the outcome
        int outcome = random.nextInt(3) + 1;
        
        Product product = context.getProduct();
        InventoryManager inventory = context.getInventoryManager();
        
        switch (outcome) {
            case 1: // Successful manufacturing
                // Deduct stock for all components only when manufacturing is successful
                product.deductStock(inventory, 1);
                
                // Add the manufactured product to inventory
                product.addStock(inventory, 1);
                
                // Transition to Completed state
                context.setState(new CompletedState());
                context.recordResult(ManufacturingOutcome.COMPLETED);
                break;
                
            case 2: // System error
                // Transition to Failed state
                context.setState(new FailedState());
                context.recordResult(ManufacturingOutcome.FAILED_SYSTEM_ERROR);
                break;
                
            case 3: // Damaged component
                // Transition to Failed state
                context.setState(new FailedState());
                context.recordResult(ManufacturingOutcome.FAILED_DAMAGED_COMPONENT);
                break;
        }
    }
    
    @Override
    public boolean isTerminal() {
        return false;
    }
    
    @Override
    public String getStateContext() {
        return "Currently manufacturing the product, deducting components from inventory if successful";
    }
}