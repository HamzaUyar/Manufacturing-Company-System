package system;

import component.Component;
import java.util.HashMap;
import java.util.Map;

public class InventoryManager {
    private final Map<Component, Integer> stockLevels;
    
    public InventoryManager() {
        this.stockLevels = new HashMap<>();
    }
    
    public void setInitialStock(Component component, int quantity) {
        stockLevels.put(component, quantity);
    }
    
    public boolean checkStock(Component component, int quantity) {
        Integer available = stockLevels.getOrDefault(component, 0);
        return available >= quantity;
    }
    
    public boolean deductStock(Component component, int quantity) {
        Integer available = stockLevels.getOrDefault(component, 0);
        if (available < quantity) {
            return false;
        }
        stockLevels.put(component, available - quantity);
        return true;
    }
    
    public void addStock(Component component, int quantity) {
        Integer current = stockLevels.getOrDefault(component, 0);
        stockLevels.put(component, current + quantity);
    }
    
    public int getStockLevel(Component component) {
        return stockLevels.getOrDefault(component, 0);
    }
} 