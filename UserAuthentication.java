import java.io.*;
import java.util.*;

public class UserAuthentication {

    private static final Scanner scanner = new Scanner(System.in);
    private static final String USER_FILE = "user.txt";
    private static final String LINE = "------------------------------------------------------------";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "pass123";

    public static String authenticate() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("              WELCOME TO THE MUSIC SYSTEM");
        System.out.println("============================================================");

        while (true) {
            System.out.println();
            System.out.println(LINE);
            System.out.println("  LOGIN MENU");
            System.out.println(LINE);
            System.out.println("  1. Login as User");
            System.out.println("  2. Register User");
            System.out.println("  3. Login as Admin");
            System.out.println("  0. Exit");
            System.out.println(LINE);

            System.out.print("  Select an option (0-3): ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    String user = loginUser();
                    if (user != null) return user;
                    break;
                case "2":
                    registerUser();
                    break;
                case "3":
                    String admin = loginAdmin();
                    if (admin != null) return admin;
                    break;
                case "0":
                    System.out.println("\n  Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("  Invalid option. Please try again.");
            }
        }
    }

    private static String loginAdmin() {
        System.out.println();
        System.out.println(LINE);
        System.out.println("  ADMIN LOGIN");
        System.out.println(LINE);

        System.out.print("  Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("  Password: ");
        String password = scanner.nextLine().trim();

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            System.out.println("\n  Admin login successful!");
            return "admin";
        }

        System.out.println("\n  Invalid admin credentials.");
        return null;
    }

    private static String loginUser() {
        System.out.println();
        System.out.println(LINE);
        System.out.println("  USER LOGIN");
        System.out.println(LINE);

        System.out.print("  Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("  Password: ");
        String password = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("\n  Username and password cannot be empty.");
            return null;
        }

        List<String[]> users = loadUsers();
        for (String[] user : users) {
            if (user[0].equals(username) && user[1].equals(password)) {
                System.out.println("\n  Login successful! Welcome, " + username + "!");
                return username;
            }
        }

        System.out.println("\n  Invalid username or password.");
        return null;
    }

    private static void registerUser() {
        System.out.println();
        System.out.println(LINE);
        System.out.println("  REGISTER NEW USER");
        System.out.println(LINE);

        System.out.print("  Username: ");
        String username = scanner.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("\n  Username cannot be empty.");
            return;
        }

        if (username.equals(ADMIN_USERNAME)) {
            System.out.println("\n  Username 'admin' is reserved.");
            return;
        }

        List<String[]> users = loadUsers();
        for (String[] user : users) {
            if (user[0].equals(username)) {
                System.out.println("\n  Username already exists.");
                return;
            }
        }

        System.out.print("  Password: ");
        String password = scanner.nextLine().trim();

        if (password.isEmpty()) {
            System.out.println("\n  Password cannot be empty.");
            return;
        }

        if (!isValidPassword(password)) {
            System.out.println("\n  Password must contain at least one letter and one number.");
            return;
        }

        System.out.print("  Confirm Password: ");
        String confirmPassword = scanner.nextLine().trim();

        if (!password.equals(confirmPassword)) {
            System.out.println("\n  Passwords do not match.");
            return;
        }

        saveUser(username, password);
        System.out.println("\n  Registration successful! You can now login.");
    }

    private static boolean isValidPassword(String password) {
        boolean hasLetter = false;
        boolean hasNumber = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasNumber = true;
        }

        return hasLetter && hasNumber;
    }

    private static List<String[]> loadUsers() {
        List<String[]> users = new ArrayList<>();
        File file = new File(USER_FILE);

        if (!file.exists()) {
            return users;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    users.add(parts);
                }
            }
        } catch (IOException e) {
            System.out.println("  Error reading user file.");
        }

        return users;
    }

    private static void saveUser(String username, String password) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            bw.write(username + "|" + password);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("  Error saving user.");
        }
    }
}
