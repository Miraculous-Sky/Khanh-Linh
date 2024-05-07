public class DigitalGift extends DigitalProduct implements Gift {
    private String message;

    public DigitalGift(String name, int quantityAvailable, double price) {
        super(name, quantityAvailable, price);
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
