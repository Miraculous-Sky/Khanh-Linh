import java.util.ArrayList;
import java.util.List;

public class ShoppingCartManagement {
    private final List<ShoppingCart> shoppingCarts;

    public ShoppingCartManagement(List<ShoppingCart> shoppingCarts) {
        this.shoppingCarts = shoppingCarts;

    }

    public ShoppingCartManagement() {
        this.shoppingCarts = new ArrayList<>();

    }

    public void addCart(ShoppingCart shoppingCart) { //TODO: check this
        for (int i = 0; i < shoppingCarts.size(); i++) {
            if (shoppingCarts.get(i).compareTo(shoppingCart) < 0) {
                shoppingCarts.add(i, shoppingCart);
            }
        }
    }

    public String showCarts() {
        String carts = "";
        for (ShoppingCart s : shoppingCarts) {
            carts += s.toString();
        }
        return carts;
    }


    public List<ShoppingCart> getAllCartsSortedByWeight() {
        return this.shoppingCarts;
    }
}
