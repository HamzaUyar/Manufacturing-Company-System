package system;

import component.*;
import component.factory.ComponentFactory;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
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
            int lineNumber = 1;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] parts = line.split(";");
                    if (parts.length < 5) {
                        throw new ParseException("Insufficient data columns", lineNumber);
                    }
                    
                    String name = parts[0].trim();
                    if (name.isEmpty()) {
                        throw new ParseException("Empty component name", lineNumber);
                    }
                    
                    double unitCost;
                    try {
                        unitCost = Double.parseDouble(parts[1].replace(",", "."));
                    } catch (NumberFormatException e) {
                        throw new ParseException("Invalid unit cost: " + parts[1], lineNumber);
                    }
                    
                    double unitWeight;
                    try {
                        unitWeight = Double.parseDouble(parts[2].replace(",", "."));
                    } catch (NumberFormatException e) {
                        throw new ParseException("Invalid unit weight: " + parts[2], lineNumber);
                    }
                    
                    String type = parts[3].trim();
                    if (!type.equals("Raw Material") && !type.equals("Hardware") && !type.equals("Paint")) {
                        throw new ParseException("Invalid component type: " + type, lineNumber);
                    }
                    
                    String stockInfo = parts[4].trim();
                    int stockQuantity;
                    try {
                        stockQuantity = Integer.parseInt(stockInfo.split(" ")[0]);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        throw new ParseException("Invalid stock quantity: " + stockInfo, lineNumber);
                    }
                    
                    // Use factory to create component
                    Component component = ComponentFactory.createComponent(name, unitCost, unitWeight, type);
                    
                    if (basicComponentsMap.containsKey(name)) {
                        System.out.println("Warning: Duplicate component name '" + name + "' at line " + lineNumber);
                    }
                    
                    basicComponentsMap.put(name, component);
                    inventory.setInitialStock(component, stockQuantity);
                    
                } catch (ParseException e) {
                    System.err.println("Error parsing components CSV at line " + e.getErrorOffset() + ": " + e.getMessage());
                    // Continue with next line instead of failing completely
                }
            }
            
            if (basicComponentsMap.isEmpty()) {
                throw new IOException("No valid components loaded from " + csvPath);
            }
        } catch (FileNotFoundException e) {
            throw new IOException("Components CSV file not found: " + csvPath, e);
        }
    }
    
    private List<ManufacturingOrder> loadManufacturingOrders(String csvPath) throws IOException {
        List<ManufacturingOrder> orders = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
            // Read header to get component names
            String header = reader.readLine();
            if (header == null) {
                throw new IOException("Products CSV file is empty");
            }
            
            String[] headerParts = header.split(";");
            if (headerParts.length < 2) {
                throw new IOException("Products CSV header is invalid");
            }
            
            int lineNumber = 1;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] parts = line.split(";");
                    if (parts.length < 2) {
                        throw new ParseException("Insufficient data columns", lineNumber);
                    }
                    
                    String productName = parts[0].trim();
                    if (productName.isEmpty()) {
                        throw new ParseException("Empty product name", lineNumber);
                    }
                    
                    // Use factory to create product
                    Product product = ComponentFactory.createProduct(productName);
                    
                    // Last column is the quantity to manufacture
                    String quantityStr = parts[parts.length - 1].trim();
                    int quantityToManufacture;
                    try {
                        quantityToManufacture = Integer.parseInt(quantityStr);
                        if (quantityToManufacture <= 0) {
                            throw new ParseException("Quantity must be positive: " + quantityStr, lineNumber);
                        }
                    } catch (NumberFormatException e) {
                        throw new ParseException("Invalid quantity: " + quantityStr, lineNumber);
                    }
                    
                    // Add components to the product
                    for (int i = 1; i < parts.length - 1; i++) {
                        if (i >= headerParts.length) {
                            continue;
                        }
                        
                        String componentName = headerParts[i].trim();
                        String compQuantityStr = parts[i].replace(",", ".");
                        
                        double quantity;
                        try {
                            quantity = Double.parseDouble(compQuantityStr);
                        } catch (NumberFormatException e) {
                            System.err.println("Warning: Invalid component quantity '" + compQuantityStr + 
                                             "' for " + componentName + " in product " + productName);
                            continue;
                        }
                        
                        if (quantity > 0) {
                            Component component = basicComponentsMap.get(componentName);
                            if (component != null) {
                                product.addComponent(component, quantity);
                            } else {
                                System.err.println("Warning: Unknown component '" + componentName + 
                                                 "' referenced in product " + productName);
                            }
                        }
                    }
                    
                    // Only add product if it has components
                    if (!product.getRequiredComponents().isEmpty()) {
                        orders.add(new ManufacturingOrder(product, quantityToManufacture));
                    } else {
                        System.err.println("Warning: Product '" + productName + 
                                         "' has no components and will be ignored");
                    }
                    
                } catch (ParseException e) {
                    System.err.println("Error parsing products CSV at line " + e.getErrorOffset() + ": " + e.getMessage());
                    // Continue with next line instead of failing completely
                }
            }
            
            if (orders.isEmpty()) {
                throw new IOException("No valid products loaded from " + csvPath);
            }
            
        } catch (FileNotFoundException e) {
            throw new IOException("Products CSV file not found: " + csvPath, e);
        }
        
        return orders;
    }
    
    public List<ManufacturingOrder> loadDataAndInitialize(String componentsCsvPath, String productsCsvPath, InventoryManager inventoryManager) throws IOException {
        loadBasicComponents(componentsCsvPath, inventoryManager);
        return loadManufacturingOrders(productsCsvPath);
    }
} 