package utils;

import model.Status;
import service.ClaimService;

import java.util.Date;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class InputHelper {
    public static String getString(Scanner scanner, String prompt, boolean acceptNull) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        if (acceptNull && input.isEmpty()) {
            return null;
        }
        return input;
    }

    public static Integer getInt(Scanner scanner, String prompt, boolean acceptNull) {
        return getInt(scanner, prompt, acceptNull, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static Integer getInt(Scanner scanner, String prompt, boolean acceptNull, int min, int max) {
        int result;
        String input = getString(scanner, prompt, acceptNull);
        while (true) {
            if (input == null) return null;
            try {
                result = Integer.parseInt(input);
                if (result >= min && result <= max) {
                    return result;
                } else {
                    input = getString(scanner,
                            String.format("Input must be between %d and %d. Please try again: ", min, max), acceptNull);
                }
            } catch (NumberFormatException e) {
                input = getString(scanner, "Invalid input. Please enter a valid integer: ", acceptNull);
            }
        }
    }

    public static Double getDouble(Scanner scanner, String prompt, boolean acceptNull) {
        Double result;
        String input = getString(scanner, prompt, acceptNull);
        while (true) {
            if (input == null) return null;
            result = Converter.parseDouble(input);
            if (Double.isNaN(result))
                input = getString(scanner, "Invalid input. Please enter a valid number: ", acceptNull);
            else
                return result;
        }
    }

    public static String getNewClaimId(Scanner scanner, boolean acceptNull, ClaimService claimService) {
        String prompt = "Enter a new claim ID (format f-xxxxxxxxxx): ";
        String claimId = getClaimId(scanner, prompt, acceptNull);
        while (claimService.getOne(claimId) != null) {
            claimId = getClaimId(scanner, "This claim ID is already exists. Please try another: ", acceptNull);
        }
        return claimId;
    }

    public static String getExistedClaimId(Scanner scanner, boolean acceptNull, ClaimService claimService) {
        String prompt = "Enter a existed claim ID (format f-xxxxxxxxxx): ";
        String claimId = getClaimId(scanner, prompt, acceptNull);
        while (claimService.getOne(claimId) == null) {
            claimId = getClaimId(scanner, "This claim ID is not exists. Please try another: ", acceptNull);
        }
        return claimId;
    }

    private static String getClaimId(Scanner scanner, String prompt, boolean acceptNull) {
        String claimId = getString(scanner, prompt, acceptNull);
        while (true) {
            if (claimId == null) return null;
            if (Validation.isInvalidClaimId(claimId))
                claimId = getString(scanner, "Invalid claim ID format. Please try again: ", false);
            else
                return claimId;
        }
    }

    public static Date getClaimDate(Scanner scanner, boolean acceptNull) {
        String prompt = "Enter claim date (YYYY-MM-DD): ";
        return getDate(scanner, prompt, acceptNull);
    }

    public static Date getExamDate(Scanner scanner, boolean acceptNull) {
        String prompt = "Enter exam date (YYYY-MM-DD): ";
        return getDate(scanner, prompt, acceptNull);
    }

    public static Date getDate(Scanner scanner, String prompt, boolean acceptNull) {
        String dateString = getString(scanner, prompt, acceptNull);
        Date result;

        while (true) {
            if (dateString == null) return null;
            result = Converter.parseDate(dateString);
            if (result == null) {
                dateString = getString(scanner, "Invalid date format. Please try again: ", acceptNull);
            } else
                return result;
        }
    }

    public static Double getClaimAmount(Scanner scanner, boolean acceptNull) {
        String prompt = "Enter the claim amount: ";
        Double amount = getDouble(scanner, prompt, acceptNull);
        while (true) {
            if (amount == null) return null;
            if (Validation.isInvalidAmount(amount))
                amount = getDouble(scanner, "Invalid amount. Please enter a positive number: ", acceptNull);
            else
                return amount;
        }
    }

    //    private Customer promptPerson(String prompt) {
//        System.out.print(prompt);
//        String customerId = scanner.nextLine();
//        Customer customer;
//        while (CustomerValidation.isInvalidCustomerId(customerId) ||
//                (customer = customerService.getOne(customerId)) == null) {
//            System.out.println("Invalid ID or no customer found. Please try again: ");
//            customerId = scanner.nextLine();
//        }
//        return customer;
//    }

    public static String getCustomerId(Scanner scanner, String prompt, boolean acceptNull) {
        String customerId = getString(scanner, prompt, acceptNull);
        while (true) {
            if (customerId == null) return null;
            if (Validation.isInvalidCustomerId(customerId))
                customerId = getString(scanner, "Invalid customer ID. Please try again.", acceptNull);
            else
                return customerId;
        }
    }

    public static Status getClaimStatus(Scanner scanner, boolean acceptNull) {
        String prompt = "Enter the status of the claim 'N' (New), 'P' (Process), or 'D' (Done): ";
        String status = getString(scanner, prompt, acceptNull);
        Status result;
        while (true) {
            if (status == null) return null;
            result = Status.parse(status);
            if (result == null)
                status = getString(scanner, "Invalid status. Please try again: ", acceptNull);
            else
                return result;
        }
    }

    public static String getBankingInfo(Scanner scanner, boolean acceptNull) {
        String prompt = "Enter banking information for payouts (Bank-Name-Number): ";
        String bankingInfo = getString(scanner, prompt, acceptNull);
        while (true) {
            if (bankingInfo == null) return null;
            if (Validation.isInvalidBankingInfo(bankingInfo))
                bankingInfo = getString(scanner, prompt, acceptNull);
            else
                return bankingInfo;
        }
    }

    public static SortedSet<String> getDocuments(Scanner scanner, boolean acceptNull) {
        String prompt;
        if (acceptNull) prompt = "Enter documents associated with the claim (enter blank to keep current): ";
        else prompt = "Enter documents associated with the claim (enter blank to finish): ";

        String document = getString(scanner, prompt, true);  // Allow blank entries directly here.
        if (acceptNull && document == null) return null;

        SortedSet<String> documents = new TreeSet<>();
        while (document != null) {
            documents.add(document);
            document = getString(scanner, "Another document name: ", true);  // Allow blank entries directly here.
        }
        return documents;
    }
}
