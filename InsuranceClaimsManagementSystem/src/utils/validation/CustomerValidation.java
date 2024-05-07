package utils.validation;

public class CustomerValidation {
    public static boolean isInvalidId(String id) {
        return !id.matches("c-\\d{7}");
    }

    public static boolean isTypeValid(String type) {
        return "H".equals(type) || "D".equals(type);
    }
}
