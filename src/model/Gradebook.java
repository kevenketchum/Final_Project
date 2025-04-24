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

        JSONObject studentGradeData = new JSONObject();
        for (Map.Entry<String, List<Double>> entry : studentGrades.entrySet()) {
            JSONArray gradeArray = new JSONArray(entry.getValue());
            studentGradeData.put(entry.getKey(), gradeArray);
        }
        json.put("studentGrades", studentGradeData);

        JSONObject courseData = new JSONObject();
        for (Map.Entry<String, Course> entry : courses.entrySet()) {
            String courseName = entry.getKey();
            Course course = entry.getValue();

            JSONObject courseObj = new JSONObject();
            JSONArray assignmentArr = new JSONArray();

            for (Assignment assignment : course.getAssignments()) {
                JSONObject aObj = new JSONObject();
                aObj.put("name", assignment.getAssignment());
                aObj.put("type", assignment.getType().toString());
                aObj.put("weight", assignment.getWeight());

                JSONObject grades = new JSONObject();
                for (Map.Entry<Student, Double> g : assignment.getGrades().entrySet()) {
                    grades.put(g.getKey().getUsername(), g.getValue());
                }

                aObj.put("grades", grades);
                assignmentArr.put(aObj);
            }

            courseObj.put("assignments", assignmentArr);
            courseData.put(courseName, courseObj);
        }
        json.put("courses", courseData);

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

            if (json.has("studentGrades")) {
                JSONObject studentGradesObj = json.getJSONObject("studentGrades");
                for (String user : studentGradesObj.keySet()) {
                    JSONArray grades = studentGradesObj.getJSONArray(user);
                    List<Double> gradeList = new ArrayList<>();
                    for (int i = 0; i < grades.length(); i++) {
                        gradeList.add(grades.getDouble(i));
                    }
                    gradebook.studentGrades.put(user, gradeList);
                }
            }

            if (json.has("courses")) {
                JSONObject courseData = json.getJSONObject("courses");
                for (String courseName : courseData.keySet()) {
                    JSONObject courseObj = courseData.getJSONObject(courseName);
                    JSONArray assignments = courseObj.getJSONArray("assignments");
                    Course course = new Course(courseName);

                    for (int i = 0; i < assignments.length(); i++) {
                        JSONObject aObj = assignments.getJSONObject(i);
                        String name = aObj.getString("name");
                        String type = aObj.getString("type");
                        double weight = aObj.getDouble("weight");
                        JSONObject gradesObj = aObj.getJSONObject("grades");

                        Assignment assignment = new Assignment(name, type, new ArrayList<>());
                        assignment.setWeight(weight);

                        for (String student : gradesObj.keySet()) {
                            double grade = gradesObj.getDouble(student);
                            assignment.setStudentGrade(student, grade);
                        }

                        course.getAssignments().add(assignment);
                    }

                    gradebook.addCourse(course);
                }
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
