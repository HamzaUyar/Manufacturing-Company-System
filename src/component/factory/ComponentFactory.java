package component.factory;

import component.*;

/**
 * Factory for creating components based on their type.
 * This follows the Factory Method pattern and GRASP Creator principle.
 */
public class ComponentFactory {
    
    /**
     * Creates a new component based on the specified type
     * 
     * @param name The name of the component
     * @param unitCost The cost per unit
     * @param unitWeight The weight per unit
     * @param type The type of component (Raw Material, Hardware, Paint)
     * @return A new component of the specified type
     * @throws IllegalArgumentException if the type is unknown
     */
    public static Component createComponent(String name, double unitCost, double unitWeight, String type) {
        switch (type) {
            case "Raw Material":
                return new RawMaterial(name, unitCost, unitWeight);
            case "Hardware":
                return new Hardware(name, unitCost, unitWeight);
            case "Paint":
                return new Paint(name, unitCost, unitWeight);
            default:
                throw new IllegalArgumentException("Unknown component type: " + type);
        }
    }
    
    /**
     * Creates a new product with the given name
     * 
     * @param name The name of the product
     * @return A new empty product (components need to be added separately)
     */
    public static Product createProduct(String name) {
        return new Product(name);
    }
} 