import java.util.List;
import java.util.Scanner;

public class CLI {
    private final ProductManagement productManagement = new ProductManagement();
    private final ShoppingCartManagement shoppingCartManagement = new ShoppingCartManagement();
    private final Scanner scanner = new Scanner(System.in);

    private ShoppingCart currentCart = null;

    public void start() {
        int option;
        while (true) {
            displayMenu();
            option = Utils.getInt(scanner, "Enter the option (0-7): ", 0, 7);

            switch (option) {
                case 1:
                    Product product = createProduct();
                    if (productManagement.addProduct(product)) {
                        System.out.println("Product is added to the system.");
                    } else {
                        System.out.println("Product is NOT added to the system.");
                    }
                    break;
                case 2:
                    editProduct();
                    break;
                case 3:
                    createNewShoppingCart();
                    break;
                case 4:
                    addProductToCart();
                    break;
                case 5:
                    removeProductFromCart();
                    break;
                case 6:
                    displayCartAmount();
                    break;
                case 7:
                    displayAllCarts();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n_____MENU_____");
        System.out.println("1. Add product");
        System.out.println("2. Update product");
        System.out.println("3. Create shopping cart");
        System.out.println("4. Add products to the current shopping cart");
        System.out.println("5. Remove products from the current shopping cart");
        System.out.println("6. Display the cart amount");
        System.out.println("7. Display all shopping carts based on their total weight");
        System.out.println("0. Exit");
    }

    private Product createProduct() {
        System.out.println("Creating a new product...");
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter quantity available: ");
        int quantity = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Is this a physical product (yes/no)? ");
        boolean isPhysical = scanner.nextLine().trim().equalsIgnoreCase("yes");
        System.out.print("Is this a gift product (yes/no)? ");
        boolean isGift = scanner.nextLine().trim().equalsIgnoreCase("yes");

        Product product;
        if (isPhysical) {
            System.out.print("Enter weight: ");
            double weight = Double.parseDouble(scanner.nextLine());
            if (isGift) {
                product = new PhysicalGift(name, quantity, price, weight);
            } else {
                product = new PhysicalProduct(name, quantity, price, weight);
            }
        } else {
            if (isGift) {
                product = new DigitalGift(name, quantity, price);
            } else {
                product = new DigitalProduct(name, quantity, price);
            }
        }
        return product;
    }

    private void editProduct() {
        System.out.println("Editing an existing product...");
        System.out.print("Enter the name of the product to edit: ");
        String name = scanner.nextLine();
        Product product = productManagement.getProduct(name);
        if (product != null) {
            System.out.print("Enter new quantity available: ");
            int quantity = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter new price: ");
            double price = Double.parseDouble(scanner.nextLine());
            product.setQuantityAvailable(quantity);
            product.setPrice(price);
            productManagement.updateProduct(product);
            System.out.println("Product updated successfully.");
        } else {
            System.out.println("Product not found.");
        }
    }

    private void createNewShoppingCart() {
        System.out.println("Creating a new shopping cart...");
        currentCart = new ShoppingCart();
        System.out.println("New shopping cart created.");
    }

    private void addProductToCart() {
        System.out.println("Adding a product to the cart...");
        if (currentCart == null) {
            System.out.println("No active cart. Creating a new one.");
            createNewShoppingCart();
        }
        System.out.print("Enter the product name to add: ");
        String productName = scanner.nextLine();
        Product product = productManagement.getProduct(productName);
        if (product != null && currentCart.addItem(productName)) {
            if (product instanceof Gift) {
                System.out.print("Enter a gift message: ");
                String message = scanner.nextLine();
                ((Gift) product).setMessage(message);
            }
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Failed to add product to the cart.");
        }
    }


    private void removeProductFromCart() {
        System.out.println("Removing a product from the cart...");
        if (currentCart == null) {
            System.out.println("No active cart.");
            return;
        }
        System.out.print("Enter the product name to remove: ");
        String productName = scanner.nextLine();
        if (currentCart.removeItem(productName)) {
            System.out.println("Product removed successfully.");
        } else {
            System.out.println("Product not found in the cart.");
        }
    }

    private void displayCartAmount() {
        System.out.println("Displaying the cart amount...");
        if (currentCart == null) {
            System.out.println("No active cart.");
            return;
        }
        System.out.printf("Total amount in cart: $%.2f\n", currentCart.cartAmount());
    }

    private void displayAllCarts() {
        System.out.println("Displaying all carts sorted by total weight...");
        List<ShoppingCart> carts = shoppingCartManagement.getAllCartsSortedByWeight();
        for (ShoppingCart cart : carts) {
            System.out.printf("Cart ID: %s, Total weight: %.2f kg\n", cart.toString(), cart.totalWeight());
        }
    }

}
