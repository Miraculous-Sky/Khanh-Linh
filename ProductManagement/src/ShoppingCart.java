import java.util.HashSet;
import java.util.Set;

public class ShoppingCart implements Comparable<ShoppingCart> {
    private final Set<String> productNames = new HashSet<>();

    private ProductManagement productManagement;

    public ShoppingCart() {
    }

    public ShoppingCart(ProductManagement productManagement) {
        this.productManagement = productManagement;
    }

    public Set<String> getProductNames() {
        return productNames;
    }

    public boolean addItem(String productName) {
        Product product = productManagement.getProduct(productName);
        if (product == null || product.getQuantityAvailable() <= 0 || productNames.contains(productName)) {
            return false;
        }
        productNames.add(productName);
        product.decreaseQuantity();
        return true;
    }

    public boolean removeItem(String productName) {
        Product product = productManagement.getProduct(productName);
        if (product == null || !productNames.contains(productName)) {
            return false;
        }
        productNames.remove(productName);
        product.increaseQuantity();
        return true;
    }

    public double totalWeight() {
        double weight = 0;
        for (String n : productNames) {
            for (Product p : productManagement.getProducts()) {
                if (p.getName().equals(n) && p instanceof PhysicalProduct) {
                    weight += ((PhysicalProduct) p).getWeight();
                }
            }
        }
        return weight;
    }

    public double cartAmount() {
        double amount = 0;
        for (String n : productNames) {
            for (Product p : productManagement.getProducts()) {
                if (p.getName().equals(n)) {
                    amount += p.getPrice();
                }

            }
        }
        return amount + totalWeight() * Config.BASE_FEE;
    }


    @Override
    public int compareTo(ShoppingCart o) {
        return Double.compare(this.totalWeight(), o.totalWeight());
    }

    @Override
    public String toString() {
        return "ShoppingCart:" +
                "productNames=" + productNames;
    }

}
