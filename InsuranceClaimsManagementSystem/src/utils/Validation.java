package utils;

public class Validation {

    /**
     * Validates claim ID format.
     *
     * @param claimId The claim ID to validate.
     * @return true if the claim ID is valid, false otherwise.
     */
    public static boolean isInvalidClaimId(String claimId) {
        return claimId == null || !claimId.matches("f-\\d{10}");
    }

    /**
     * Validates if the amount is a valid number greater than zero.
     *
     * @param amount The amount to validate.
     * @return true if the amount is valid, false otherwise.
     */
    public static boolean isInvalidAmount(double amount) {
        return Double.isNaN(amount) || amount < 0;
    }

    /**
     * Validates banking information.
     *
     * @param bankInfo The banking information to validate.
     * @return true if the banking information is valid, false otherwise.
     */
    public static boolean isInvalidBankingInfo(String bankInfo) {
        String bankRegex = "[\\w ]+";
        String nameRegex = "[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*";
        String numberRegex = "\\d+";
        return bankInfo == null || !bankInfo.matches(bankRegex + "-" + nameRegex + "-" + numberRegex);
    }

    public static boolean isInvalidCustomerName(String name) {
        String nameRegex = "[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*";
        return name == null || !name.matches(nameRegex);
    }

    /**
     * Validates card ID.
     *
     * @param id The card ID to validate.
     * @return true if the card ID is valid, false otherwise.
     */
    public static boolean isInvalidCardId(String id) {
        return id == null || !id.matches("\\d{10}");
    }

    /**
     * Validates customer ID.
     *
     * @param id The customer ID to validate.
     * @return true if the customer ID is valid, false otherwise.
     */
    public static boolean isInvalidCustomerId(String id) {
        return id == null || !id.matches("c-\\d{7}");
    }

    /**
     * Validates customer type.
     *
     * @param type The customer type to validate.
     * @return true if the customer type is valid, false otherwise.
     */
    public static boolean isValidCustomerType(String type) {
        return "H".equals(type) || "D".equals(type);
    }
}
