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
    private Map<String, User> users = new HashMap<>();
    private Map<String, Course> courses = new HashMap<>();

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

    public Set<String> getAllUsernames() {
        return users.keySet();
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public void registerUser(User user) {
        users.put(user.getUsername(), user);
    }

    public void addCourse(Course course) {
        courses.put(course.getName(), course);
    }

    public Course getCourse(String name) {
        return courses.get(name);
    }

    public boolean addStudentToCourse(String username, String courseName) {
        User user = getUser(username);
        Course course = getCourse(courseName);
        if (user instanceof Student && course != null) {
            course.addStudent(username);
            return true;
        }
        return false;
    }

    public Set<String> getAllCourseNames() {
        return courses.keySet();
    }
}