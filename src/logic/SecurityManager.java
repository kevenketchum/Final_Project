package logic;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.*;
/**
 * SecurityManager Class
 *
 * Handles secure registration and login for users (students and teachers).
 * 
 * Features:
 * - Registers users with salted + hashed passwords (SHA-256)
 * - Logs in users by verifying hashed credentials
 * - Stores credentials in a local JSON file (users.json)
 */
public class SecurityManager {
    private static final String USER_FILE = "users.json";
    private Map<String, String[]> users; // username → [hashedPassword, salt]

    public SecurityManager() {
        users = new HashMap<>();
        loadUsers();
    }

    // Load users from the JSON file
    private void loadUsers() {
        try {
            File file = new File(USER_FILE);
            if (!file.exists()) return;

            String content = new String(Files.readAllBytes(Paths.get(USER_FILE)), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(content);

            for (String key : json.keySet()) {
                JSONObject userObj = json.getJSONObject(key);
                users.put(key, new String[]{userObj.getString("password"), userObj.getString("salt")});
            }
        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    // Save users to the JSON file
    private void saveUsers() {
        try (FileWriter writer = new FileWriter(USER_FILE)) {
            JSONObject json = new JSONObject();

            for (Map.Entry<String, String[]> entry : users.entrySet()) {
                JSONObject userObj = new JSONObject();
                userObj.put("password", entry.getValue()[0]);
                userObj.put("salt", entry.getValue()[1]);
                json.put(entry.getKey(), userObj);
            }

            writer.write(json.toString(4)); // Pretty print JSON
        } catch (Exception e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    // Register a new user with a salted and hashed password
    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println("Username already exists.");
            return false;
        }

        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        users.put(username, new String[]{hashedPassword, salt});
        saveUsers();
        return true;
    }

    // Login a user by verifying their password
    public boolean loginUser(String username, String password) {
        if (!users.containsKey(username)) {
            System.out.println("User not found.");
            return false;
        }

        String[] storedData = users.get(username);
        String storedHash = storedData[0];
        String salt = storedData[1];

        return storedHash.equals(hashPassword(password, salt));
    }

    // Generate a random 16-byte salt
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    // Hash password using SHA-256 and salt
    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt));
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
