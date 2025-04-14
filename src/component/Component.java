package component;

import system.InventoryManager;
import java.util.Map;

public interface Component {
    double getCost();
    double getWeight();
    String getName();
    boolean checkStockAvailability(InventoryManager inventory, int quantity);
    void deductStock(InventoryManager inventory, int quantity);
    void addStock(InventoryManager inventory, int quantity);
    Map<Component, Double> getRequiredComponents();
} 