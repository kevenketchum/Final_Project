package model;

import java.util.*;

public class Gradebook {
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
        for (double grade : grades) {
            total += grade;
        }
        return total / grades.size();
    }
}
