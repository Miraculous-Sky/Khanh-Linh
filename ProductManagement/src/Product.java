import java.util.Objects;

public abstract class Product implements Comparable<Product> {
    private String name;            //- name (String, unique among all products)
    private int quantityAvailable;  //- quantity available (non-negative integer)
    private double price;           //- price (double)

    public Product() {
    }

    public Product(String name, int quantityAvailable, double price) {
        this.name = name;
        this.quantityAvailable = quantityAvailable;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void increaseQuantity() {
        this.quantityAvailable++;
    }

    public void decreaseQuantity() {
        this.quantityAvailable--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public int compareTo(Product o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public abstract String toString();
}
