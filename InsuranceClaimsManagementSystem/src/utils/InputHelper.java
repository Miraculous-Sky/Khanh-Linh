package utils;

import java.util.Scanner;

public class InputHelper {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static int getInt(String prompt) {
        return getInt(prompt, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int getInt(String prompt, int min, int max) {
        System.out.print(prompt);
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

    public static double getDouble(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            scanner.next();
            System.out.println("Invalid input. Please enter a valid amount.");
            System.out.print(prompt);
        }
        return scanner.nextDouble();
    }
}
