package process;

import component.Product;
import observer.ProcessObserver;
import system.InventoryManager;

import java.util.ArrayList;
import java.util.List;

public class ManufacturingProcess {
    private final Product product;
    private final InventoryManager inventoryManager;
    private ProcessState currentState;
    private ManufacturingOutcome finalOutcome;
    private final List<ProcessObserver> observers;
    
    public ManufacturingProcess(Product product, InventoryManager inventoryManager) {
        this.product = product;
        this.inventoryManager = inventoryManager;
        this.currentState = new WaitingForStockState();
        this.observers = new ArrayList<>();
        
        // No longer notify observers at construction time
    }
    
    public void addObserver(ProcessObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(ProcessObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyStateChange(ProcessState oldState, ProcessState newState) {
        if (observers.isEmpty()) {
            return; // Skip notification if no observers are attached
        }
        
        for (ProcessObserver observer : observers) {
            observer.onStateChange(this, oldState, newState);
        }
    }
    
    private void notifyProcessCompleted() {
        if (observers.isEmpty()) {
            return; // Skip notification if no observers are attached
        }
        
        for (ProcessObserver observer : observers) {
            observer.onProcessCompleted(this);
        }
    }
    
    public void setState(ProcessState newState) {
        ProcessState oldState = this.currentState;
        this.currentState = newState;
        notifyStateChange(oldState, newState);
        
        if (newState.isTerminal()) {
            // Ensure we have a finalOutcome before notifying completion
            if (finalOutcome == null) {
                if (newState instanceof FailedState) {
                    // Default to system error if no specific reason was set
                    recordResult(ManufacturingOutcome.FAILED_SYSTEM_ERROR);
                } else if (newState instanceof CompletedState) {
                    recordResult(ManufacturingOutcome.COMPLETED);
                }
            }
            
            notifyProcessCompleted();
        }
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