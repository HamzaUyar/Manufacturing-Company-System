package system;

import component.Product;

public class ManufacturingOrder {
    private final Product product;
    private final int quantity;
    
    public ManufacturingOrder(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public int getQuantity() {
        return quantity;
    }
} 