package utils.validation;

public class CardValidation {
    public static boolean isIdValid(String id) {
        return id.matches("\\d{10}");
    }
}
