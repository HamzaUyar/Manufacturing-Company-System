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
    
    /**
     * Adds a component (can be a basic component or another product) with the specified quantity.
     * This allows for recursive composition of products.
     */
    public void addComponent(Component component, double quantity) {
        if (component == this) {
            throw new IllegalArgumentException("A product cannot contain itself as a component");
        }
        
        // Check for circular references if the component is a product
        if (component instanceof Product) {
            Product productComponent = (Product) component;
            if (hasCircularDependency(productComponent)) {
                throw new IllegalArgumentException("Circular dependency detected between products");
            }
        }
        
        requiredComponents.put(component, quantity);
    }
    
    /**
     * Checks if adding the given product would create a circular dependency
     */
    private boolean hasCircularDependency(Product product) {
        // Check if any of the product's direct components references this product
        for (Component component : product.getRequiredComponents().keySet()) {
            if (component == this) {
                return true;
            }
            
            // Recursively check if any sub-product references this product
            if (component instanceof Product) {
                if (hasCircularDependency((Product) component)) {
                    return true;
                }
            }
        }
        
        return false;
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
    public String toString() {
        return "Product: " + name;
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