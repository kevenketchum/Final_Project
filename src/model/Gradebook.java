package model;

import org.json.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Gradebook {
    private static final String FILE_PATH = "gradebook.json";
    private Map<String, List<Double>> studentGrades;

    public Gradebook() {
        this.studentGrades = new HashMap<>();
    }

    public void addGrade(String username, double grade) {
        studentGrades.computeIfAbsent(username, k -> new ArrayList<>()).add(grade);
    }

    public List<Double> getGrades(String username) {
        return studentGrades.getOrDefault(username, new ArrayList<>());
    }

    public double getAverage(String username) {
        List<Double> grades = getGrades(username);
        if (grades.isEmpty()) return 0.0;
        double total = 0.0;
        for (double grade : grades) total += grade;
        return total / grades.size();
    }

    public void saveToFile() {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, List<Double>> entry : studentGrades.entrySet()) {
            JSONArray gradeArray = new JSONArray(entry.getValue());
            json.put(entry.getKey(), gradeArray);
        }

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(json.toString(4));
        } catch (IOException e) {
            System.out.println("Failed to save gradebook: " + e.getMessage());
        }
    }

    public static Gradebook loadFromFile() {
        Gradebook gradebook = new Gradebook();
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            JSONObject json = new JSONObject(content);
            for (String user : json.keySet()) {
                JSONArray grades = json.getJSONArray(user);
                List<Double> gradeList = new ArrayList<>();
                for (int i = 0; i < grades.length(); i++) {
                    gradeList.add(grades.getDouble(i));
                }
                gradebook.studentGrades.put(user, gradeList);
            }
        } catch (IOException e) {
            System.out.println("No gradebook file found. Starting fresh.");
        }
        return gradebook;
    }
}

