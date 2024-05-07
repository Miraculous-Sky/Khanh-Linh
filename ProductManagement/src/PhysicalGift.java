public class PhysicalGift extends PhysicalProduct implements Gift {
    private String message;

    public PhysicalGift(String name, int quantityAvailable, double price, double weight) {
        super(name, quantityAvailable, price, weight);
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(String msg) {
        this.message = msg;
    }
}
