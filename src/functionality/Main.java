import logic.SecurityManager;
import logic.AutoSaveService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SecurityManager auth = new SecurityManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Gradebook App!");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Clear newline

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        boolean success = false;

        if (choice == 1) {
            success = auth.registerUser(username, password);
            if (success) System.out.println("Registered successfully!");
        } else if (choice == 2) {
            success = auth.loginUser(username, password);
            if (success) System.out.println("Logged in successfully!");
            else System.out.println(" Login failed.");
        }

        if (success) {
            AutoSaveService autoSave = new AutoSaveService(() -> {
                System.out.println("[AutoSave] Saving data... (placeholder)");
            }, 5000); // every 5 seconds

            autoSave.start();

            System.out.println("Auto-save started. Press ENTER to stop.");
            scanner.nextLine(); // Wait for Enter
            autoSave.stop();
            System.out.println("Auto-save stopped. Exiting...");
        }

        scanner.close();
    }
}
