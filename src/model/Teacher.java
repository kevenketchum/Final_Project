package model;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends User {
    private List<Student> studentList;
    private Gradebook gradebook;

    public Teacher(String username, Gradebook gradebook) {
        super(username, "teacher");
        this.studentList = new ArrayList<>();
        this.gradebook = gradebook;
    }

    public void assignGrade(Student student, double grade) {
        gradebook.addGrade(student.getUsername(), grade);
        if (!studentList.contains(student)) {
            studentList.add(student);
        }
    }

    public List<Student> getStudents() {
        return studentList;
    }

    public void displayStudentGrades(String username) {
        List<Double> grades = gradebook.getGrades(username);
        if (grades == null || grades.isEmpty()) {
            System.out.println("No grades found for student: " + username);
        } else {
            System.out.println("Grades for " + username + ": " + grades);
            System.out.printf("Average: %.2f\n", gradebook.getAverage(username));
        }
    }
}
