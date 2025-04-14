package component;

import system.InventoryManager;
import java.util.Collections;
import java.util.Map;

public abstract class BasicComponent implements Component {
    protected final String name;
    protected final double unitCost;
    protected final double unitWeight;
    
    public BasicComponent(String name, double unitCost, double unitWeight) {
        this.name = name;
        this.unitCost = unitCost;
        this.unitWeight = unitWeight;
    }
    
    @Override
    public double getCost() {
        return unitCost;
    }
    
    @Override
    public double getWeight() {
        return unitWeight;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public Map<Component, Double> getRequiredComponents() {
        return Collections.emptyMap();
    }
    
    @Override
    public boolean checkStockAvailability(InventoryManager inventory, int quantity) {
        return inventory.checkStock(this, quantity);
    }
    
    @Override
    public void deductStock(InventoryManager inventory, int quantity) {
        inventory.deductStock(this, quantity);
    }
    
    @Override
    public void addStock(InventoryManager inventory, int quantity) {
        inventory.addStock(this, quantity);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BasicComponent that = (BasicComponent) obj;
        return name.equals(that.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
} 