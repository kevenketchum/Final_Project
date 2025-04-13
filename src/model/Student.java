package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private List<Double> grades;

    public Student(String username) {
        super(username, "student");
        this.grades = new ArrayList<>();
    }

    public void addGrade(double grade) {
        grades.add(grade);
    }

    public List<Double> getGrades() {
        return grades;
    }

    public double getAverageGrade() {
        if (grades.isEmpty()) return 0.0;
        double sum = 0.0;
        for (double grade : grades) {
            sum += grade;
        }
        return sum / grades.size();
    }
    
    public String getName() {
    	return this.username;
    }
}
