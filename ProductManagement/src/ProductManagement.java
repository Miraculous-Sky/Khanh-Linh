import java.util.HashSet;
import java.util.Set;

public class ProductManagement {
    private Set<Product> products = new HashSet<>();

    public ProductManagement() {
        this.products = new HashSet<>();
    }

    public ProductManagement(Set<Product> products) {
        this.products = products;
    }

    public boolean addProduct(Product product) {
        return this.products.add(product);
    } //check if the product is able to add or not

    public boolean updateProduct(Product product) { //check if the product is able to edit or not
        if (this.products.remove(product)) {
            return this.products.add(product);
        } else {
            return false;
        }
    }

    public Product getProduct(String name) {
        return this.products.stream().filter(p -> p.getName().equals(name)).findAny().orElse(null);

    }

    public Set<Product> getProducts() {
        return products;
    }


    public boolean removeProduct(Product product) {
        return this.products.remove(product);
    }

}
