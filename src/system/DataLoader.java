package system;

import component.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLoader {
    private final Map<String, Component> basicComponentsMap = new HashMap<>();
    
    private void loadBasicComponents(String csvPath, InventoryManager inventory) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
            // Skip header line
            String line = reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String name = parts[0];
                double unitCost = Double.parseDouble(parts[1].replace(",", "."));
                double unitWeight = Double.parseDouble(parts[2].replace(",", "."));
                String type = parts[3];
                String stockInfo = parts[4];
                int stockQuantity = Integer.parseInt(stockInfo.split(" ")[0]);
                
                Component component;
                switch (type) {
                    case "Raw Material":
                        component = new RawMaterial(name, unitCost, unitWeight);
                        break;
                    case "Hardware":
                        component = new Hardware(name, unitCost, unitWeight);
                        break;
                    case "Paint":
                        component = new Paint(name, unitCost, unitWeight);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown component type: " + type);
                }
                
                basicComponentsMap.put(name, component);
                inventory.setInitialStock(component, stockQuantity);
            }
        }
    }
    
    private List<ManufacturingOrder> loadManufacturingOrders(String csvPath) throws IOException {
        List<ManufacturingOrder> orders = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
            // Read header to get component names
            String header = reader.readLine();
            String[] headerParts = header.split(";");
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String productName = parts[0];
                Product product = new Product(productName);
                
                // Last column is the quantity to manufacture
                String quantityStr = parts[parts.length - 1].trim();
                int quantityToManufacture = Integer.parseInt(quantityStr);
                
                // Add components to the product
                for (int i = 1; i < parts.length - 1; i++) {
                    if (i >= headerParts.length) {
                        break;
                    }
                    
                    String componentName = headerParts[i];
                    String compQuantityStr = parts[i].replace(",", ".");
                    double quantity = Double.parseDouble(compQuantityStr);
                    
                    if (quantity > 0) {
                        Component component = basicComponentsMap.get(componentName);
                        if (component != null) {
                            product.addComponent(component, quantity);
                        }
                    }
                }
                
                orders.add(new ManufacturingOrder(product, quantityToManufacture));
            }
        }
        
        return orders;
    }
    
    public List<ManufacturingOrder> loadDataAndInitialize(String componentsCsvPath, String productsCsvPath, InventoryManager inventoryManager) throws IOException {
        loadBasicComponents(componentsCsvPath, inventoryManager);
        return loadManufacturingOrders(productsCsvPath);
    }
} 