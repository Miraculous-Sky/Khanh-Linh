public class DigitalProduct extends Product {

    public DigitalProduct() {
    }

    public DigitalProduct(String name, int quantityAvailable, double price) {
        super(name, quantityAvailable, price);
    }


    @Override
    public String toString() {
        return "DIGITAL - " + getName();
    }

}
