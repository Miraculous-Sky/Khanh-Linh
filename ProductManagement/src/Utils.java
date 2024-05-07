import java.util.Scanner;

public class Utils {
    public static int getInt(Scanner scanner, String msg, int min, int max) {
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

    public static boolean getBoolean(Scanner scanner, String msg, String y, String n) {
        System.out.print(msg);
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase(y)) return true;
            if (line.equalsIgnoreCase(n)) return false;
            System.out.printf("You must type \"%s\" or \"%s\", try again: ", y, n);
        }
    }
}
