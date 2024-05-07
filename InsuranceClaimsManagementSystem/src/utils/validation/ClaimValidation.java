package utils.validation;

/**
 * Provides utility methods to validate claim data.
 */
public class ClaimValidation {
    public static boolean isInvalidId(String claimId) {
        return !claimId.matches("f-\\d{10}");
    }

    public static boolean isInvalidAmount(double amount) {
        return Double.isNaN(amount) || amount <= 0;
    }

    public static boolean isInvalidBankingInfo(String bankInfo) {
        // Bank – Name – Number
        String bankRegex = "[\\w ]+";
        String nameRegex = "[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*";
        String numberRegex = "\\d+";
        return !bankInfo.matches(bankRegex + "-" + nameRegex + "-" + numberRegex);
    }
}
