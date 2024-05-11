package view.cli;

import service.ClaimService;
import service.CustomerService;
import utils.InputHelper;

import java.util.Scanner;

// TODO: review whole class
public class CLI {
    private final Scanner scanner = new Scanner(System.in);
    private final CustomerService customerService;
    private final ClaimService claimService;
    private final CommandFactory commandFactory;

    public CLI(CustomerService customerService, ClaimService claimService, CommandFactory commandFactory) {
        this.customerService = customerService;
        this.claimService = claimService;
        this.commandFactory = commandFactory;
    }

    public void start() {
        while (true) {
            displayMenu();
            int choice = InputHelper.getInt(scanner, "Select an option: ", false, 0,5);
            if (choice == 0) {
                System.out.println("Exiting...");
                break;
            }
            CommandKey commandKey = CommandKey.values()[choice - 1];
            Command command = commandFactory.getCommand(commandKey);
            if (command != null) command.execute(scanner);
        }
    }

    private void displayMenu() {
        System.out.println();
        System.out.println("_____ Insurance Claims Management System Menu _____");
        System.out.println("1. Add Claim");
        System.out.println("2. Update Claim");
        System.out.println("3. Delete Claim");
        System.out.println("4. Get One Claim");
        System.out.println("5. Get All Claims");
        System.out.println("0. Exit");
    }
}
