package process;

import component.Product;
import system.InventoryManager;

public class ManufacturingProcess {
    private final Product product;
    private final InventoryManager inventoryManager;
    private ProcessState currentState;
    private ManufacturingOutcome finalOutcome;
    
    public ManufacturingProcess(Product product, InventoryManager inventoryManager) {
        this.product = product;
        this.inventoryManager = inventoryManager;
        this.currentState = new WaitingForStockState();
    }
    
    public void setState(ProcessState newState) {
        this.currentState = newState;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
    
    public ProcessState getCurrentState() {
        return currentState;
    }
    
    public void recordResult(ManufacturingOutcome outcome) {
        this.finalOutcome = outcome;
    }
    
    public ManufacturingOutcome getFinalOutcome() {
        return finalOutcome;
    }
    
    public void process() {
        currentState.handleProcess(this);
    }
    
    public boolean isCompleted() {
        return currentState.isTerminal();
    }
} 