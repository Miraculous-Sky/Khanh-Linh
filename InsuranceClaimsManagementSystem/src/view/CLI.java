package view;

import conf.SystemConfig;
import manager.ClaimProcessManager;
import manager.ClaimProcessManagerImpl;
import manager.CustomerManagerImpl;
import model.Claim;
import model.Customer;
import model.Status;
import utils.Converter;
import utils.validation.ClaimValidation;
import utils.validation.CustomerValidation;

import java.util.Date;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

// TODO: review whole class
public class CLI {
    private final Scanner scanner = new Scanner(System.in);
    private final ClaimProcessManager claimProcessManager = new ClaimProcessManagerImpl();
    private final CustomerManagerImpl customerManager = new CustomerManagerImpl();

    public void start() {
        displayMenu();
        while (true) {
            int choice = getInt("Select an option: ", 1, 6);
            switch (choice) {
                case 1:
                    handleAddClaim();
                    break;
                case 2:
                    handleUpdateClaim();
                    break;
                case 3:
                    handleDeleteClaim();
                    break;
                case 4:
                    handleGetOneClaim();
                    break;
                case 5:
                    handleGetAllClaims();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;  // Exit the application
                default:
                    System.out.println("Invalid option. Please try again.");
            }
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
        System.out.println("6. Exit");
    }

    private int getInt(String msg, int min, int max) {
        System.out.print(msg);
        int result;
        while (true) {
            try {
                result = Integer.parseInt(scanner.nextLine());
                if (result > max || result < min) {
                    System.out.printf("Your number must be in range [%d-%d], try again: ", min, max);
                    continue;
                }
                return result;
            } catch (NumberFormatException e) {
                System.out.print("You must type a number, try again: ");
            }
        }
    }

    private void handleAddClaim() {
        String claimId = promptNewClaimId();
        Date claimDate = promptDate("Enter claim date (" + SystemConfig.DATE_FORMAT + "): ");
        Customer insuredPerson = promptPerson("Enter insured person ID (format c-xxxxxxx): ");
        SortedSet<String> documents = promptDocuments(claimId, insuredPerson.getInsuranceCard().getCardNumber());

        System.out.println("Enter the claim amount: ");
        double claimAmount = Converter.parseDouble(scanner.nextLine());
        while (ClaimValidation.isInvalidAmount(claimAmount)) {
            System.out.println("Invalid amount, try again: ");
            claimAmount = Converter.parseDouble(scanner.nextLine());
        }

        Status status = promptStatus();
        String receiverBankingInfo = promptBankingInfo("Enter the receiver's banking info (Bank - Name - Number): ");

        Claim claim = new Claim(claimId, claimDate, insuredPerson, insuredPerson.getInsuranceCard().getCardNumber(),
                null, documents, claimAmount, status, receiverBankingInfo);  // Exam date to be set
        claimProcessManager.add(claim);
        System.out.println("Claim added successfully: " + claim);
    }

    private String promptNewClaimId() {
        System.out.print("Enter claim ID (format f-xxxxxxxxxx): ");
        String claimId = scanner.nextLine();
        while (ClaimValidation.isInvalidId(claimId) || claimProcessManager.getOne(claimId) != null) {
            System.out.println("This claim ID is not valid or already exists. Please try another: ");
            claimId = scanner.nextLine();
        }
        return claimId;
    }

    private Date promptDate(String prompt) {
        System.out.print(prompt);
        Date date;
        while (true) {
            date = Converter.parseDate(scanner.nextLine());
            if (date == null)
                System.out.println("Invalid date format. Please enter in " + SystemConfig.DATE_FORMAT + " format: ");
            else
                return date;
        }
    }

    private Customer promptPerson(String prompt) {
        System.out.print(prompt);
        String customerId = scanner.nextLine();
        Customer customer;
        while (CustomerValidation.isInvalidId(customerId) ||
                (customer = customerManager.getOne(customerId)) == null) {
            System.out.println("Invalid ID or no customer found. Please try again: ");
            customerId = scanner.nextLine();
        }
        return customer;
    }

    private SortedSet<String> promptDocuments(String claimId, String cardNumber) {
        SortedSet<String> documents = new TreeSet<>();
        System.out.println("Enter document names, one per line. Enter a blank line to finish: ");
        String document;
        while (!(document = scanner.nextLine()).isEmpty()) {
            if (!document.toLowerCase().endsWith("pdf"))
                System.out.println("System accept pdf format only, try another file: ");
            else {
                if (document.matches(claimId + "_" + cardNumber + "_[\\w\\s]+\\.pdf")) {
                    documents.add(document);
                } else {
                    String correctedDocument = claimId + "_" + cardNumber + "_" + document;
                    documents.add(correctedDocument);
                }
            }
        }
        return documents;
    }

    private Status promptStatus() {
        System.out.print("Enter the status of the claim (N, P, D): ");
        Status status = Status.parse(scanner.nextLine().toUpperCase());
        while (status == null) {
            System.out.println("Invalid status. Please enter N, P, or D:");
            status = Status.parse(scanner.nextLine().toUpperCase());
        }
        return status;
    }

    private String promptBankingInfo(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private void handleUpdateClaim() {
        System.out.println("Updating a claim...");
        System.out.print("Enter Claim ID (format f-xxxxxxxxxx): ");
        String claimId = scanner.nextLine();
        Claim existingClaim = claimProcessManager.getOne(claimId);
        while (existingClaim == null) {
            System.out.println("No claim found with ID: " + claimId + ". Please try again:");
            claimId = scanner.nextLine();
            existingClaim = claimProcessManager.getOne(claimId);
        }

        updateClaimDetails(existingClaim);
        claimProcessManager.update(existingClaim);
        System.out.println("Claim updated successfully: " + existingClaim);
    }

    private void updateClaimDetails(Claim claim) {
        Date newClaimDate = promptDate("Enter new claim date (YYYY-MM-DD) or press enter to keep current:");
        if (newClaimDate != null) {
            claim.setClaimDate(newClaimDate);
        }

        System.out.print("Enter new insured person ID (format c-xxxxxxx) or press enter to keep current: ");
        String newCustomerId = scanner.nextLine();
        if (!newCustomerId.isEmpty()) {
            Customer newCustomer = customerManager.getOne(newCustomerId);
            while (newCustomer == null) {
                System.out.println("No customer found with ID: " + newCustomerId + ". Please try again:");
                newCustomerId = scanner.nextLine();
                newCustomer = customerManager.getOne(newCustomerId);
            }
            claim.setInsuredPerson(newCustomer);
        }
        claim.setCardNumber(claim.getInsuredPerson().getInsuranceCard().getCardNumber());

        Date newExamDate = promptDate("Enter new exam date (YYYY-MM-DD) or press enter to keep current:");
        if (newExamDate != null) {
            claim.setExamDate(newExamDate);
        }

        System.out.println("Enter new document names, one per line. Enter a blank line to finish adding:");
//        List<String> newDocuments = promptDocuments();
//        if (!newDocuments.isEmpty()) {
//            claim.setDocuments(newDocuments);
//        }

//        double newClaimAmount = promptDouble("Enter new claim amount or press enter to keep current:");
//        claim.setClaimAmount(newClaimAmount);

        Status newStatus = promptStatus();
        claim.setStatus(newStatus);

        String newReceiverBankingInfo = promptBankingInfo("Enter new receiver's banking info or press enter to keep current:");
        if (!newReceiverBankingInfo.isEmpty()) {
            claim.setReceiverBankingInfo(newReceiverBankingInfo);
        }

        System.out.println("Claim update completed: " + claim);
    }

    private void handleDeleteClaim() {
        System.out.println("Deleting a claim...");
        System.out.print("Enter Claim ID to delete (format f-xxxxxxxxxx): ");
        String claimId = scanner.nextLine();
        Claim claimToDelete = claimProcessManager.getOne(claimId);
        if (claimToDelete != null) {
            claimProcessManager.delete(claimToDelete);
            System.out.println("Claim deleted successfully.");
        } else {
            System.out.println("No claim found with ID: " + claimId + ". Please try again.");
        }
    }

    private void handleGetOneClaim() {
        System.out.print("Enter Claim ID to view (format f-xxxxxxxxxx): ");
        String claimId = scanner.nextLine();
        Claim claim = claimProcessManager.getOne(claimId);
        if (claim != null) {
            System.out.println("Claim details: " + claim);
        } else {
            System.out.println("No claim found with ID: " + claimId + ".");
        }
    }

    private void handleGetAllClaims() {
        System.out.println("All Claims: ");
        SortedSet<Claim> allClaims = claimProcessManager.getAll();
        for (Claim claim : allClaims) {
            System.out.println(claim);
        }
    }
}
