package component;

import system.InventoryManager;
import java.util.HashMap;
import java.util.Map;

public class Product implements Component {
    private final String name;
    private final Map<Component, Double> requiredComponents;
    
    public Product(String name) {
        this.name = name;
        this.requiredComponents = new HashMap<>();
    }
    
    public void addComponent(Component component, double quantity) {
        requiredComponents.put(component, quantity);
    }
    
    @Override
    public double getCost() {
        double totalCost = 0;
        for (Map.Entry<Component, Double> entry : requiredComponents.entrySet()) {
            totalCost += entry.getKey().getCost() * entry.getValue();
        }
        return totalCost;
    }
    
    @Override
    public double getWeight() {
        double totalWeight = 0;
        for (Map.Entry<Component, Double> entry : requiredComponents.entrySet()) {
            totalWeight += entry.getKey().getWeight() * entry.getValue();
        }
        return totalWeight;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public Map<Component, Double> getRequiredComponents() {
        return new HashMap<>(requiredComponents);
    }
    
    @Override
    public boolean checkStockAvailability(InventoryManager inventory, int quantity) {
        for (Map.Entry<Component, Double> entry : requiredComponents.entrySet()) {
            Component component = entry.getKey();
            double requiredQuantity = entry.getValue() * quantity;
            if (!component.checkStockAvailability(inventory, (int) Math.ceil(requiredQuantity))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void deductStock(InventoryManager inventory, int quantity) {
        for (Map.Entry<Component, Double> entry : requiredComponents.entrySet()) {
            Component component = entry.getKey();
            double requiredQuantity = entry.getValue() * quantity;
            component.deductStock(inventory, (int) Math.ceil(requiredQuantity));
        }
    }
    
    @Override
    public void addStock(InventoryManager inventory, int quantity) {
        inventory.addStock(this, quantity);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return name.equals(product.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
} 