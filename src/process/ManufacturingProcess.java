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
        
        // Notify observers of initial state
        notifyStateChange(null, currentState);
    }
    
    public void addObserver(ProcessObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(ProcessObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyStateChange(ProcessState oldState, ProcessState newState) {
        for (ProcessObserver observer : observers) {
            observer.onStateChange(this, oldState, newState);
        }
    }
    
    private void notifyProcessCompleted() {
        for (ProcessObserver observer : observers) {
            observer.onProcessCompleted(this);
        }
    }
    
    public void setState(ProcessState newState) {
        ProcessState oldState = this.currentState;
        this.currentState = newState;
        notifyStateChange(oldState, newState);
        
        if (newState.isTerminal()) {
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