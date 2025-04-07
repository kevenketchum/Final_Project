package functionality;

import logic.SecurityManager;
import logic.AutoSaveService;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static SecurityManager securityManager;
    private static AutoSaveService autoSaveService;

    public static void main(String[] args) {
        securityManager = new SecurityManager();

        System.out.println("Welcome to the Project Simulator!");
        System.out.println("Please choose from: [register | login | exit]");

        boolean isRunning = true;
        boolean loggedIn = false;
        String currentUser = null;

        while (isRunning) {
            System.out.print("> ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "register":
                    System.out.print("Enter username: ");
                    String newUser = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String newPass = scanner.nextLine();
                    boolean registered = securityManager.registerUser(newUser, newPass);
                    if (registered) {
                        System.out.println("Success!");
                    } else {
                        System.out.println("Registration failed (username unavailable).");
                    }
                    break;

                case "login":
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    boolean success = securityManager.loginUser(username, password);
                    if (success) {
                        loggedIn = true;
                        currentUser = username;
                        System.out.println("Login successful. Welcome back, "
                        + username + "!");
                        startAutoSave();
                        simulateSession();
                        stopAutoSave();
                        loggedIn = false;
                        currentUser = null;
                    } else {
                        System.out.println("Login failed. Check entered credentials and try again.");
                    }
                    break;

                case "exit":
                    System.out.println("Exiting app. Goodbye!");
                    isRunning = false;
                    break;

                default:
                    System.out.println("Invalid command. Use: [register | login | exit]");
            }
        }

        scanner.close();
    }

    private static void startAutoSave() {
        Runnable dummySaveTask = () -> System.out.println("Auto-saving user session.");
        autoSaveService = new AutoSaveService(dummySaveTask, 5000); 
        // every 5 seconds
        autoSaveService.start();
    }

    private static void stopAutoSave() {
        if (autoSaveService != null) {
            autoSaveService.stop();
            System.out.println("Auto-save stopped.");
        }
    }

    private static void simulateSession() {
        System.out.println("You are now inside the matrix. Type 'logout' at any time to exit to the main menu.");
        while (true) {
            System.out.print("Session > ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("logout")) {
                System.out.println("Logging out.");
                break;
            } else {
                System.out.println("Simulating: '" + input + "' command (no functionality yet)");
            }
        }
    }
}
        scanner.close();
    }
}
