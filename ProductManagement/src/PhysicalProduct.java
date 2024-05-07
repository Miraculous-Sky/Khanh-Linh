public class PhysicalProduct extends Product {
    private double weight;

    public PhysicalProduct() {
    }

    public PhysicalProduct(double weight) {
        this.weight = weight;
    }

    public PhysicalProduct(String name, int quantityAvailable, double price, double weight) {
        super(name, quantityAvailable, price);
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "PHYSICAL - " + getName();
    }
}
