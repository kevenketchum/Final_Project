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
    
    //make groups given the name of a course and the size of the groups
    public ArrayList<String> makeGroups(String courseName, int size){
    	for(Course c : courses) {
    		if(c.equals(courseName)) {
    			return c.makeGroups(size);
    		}
    	}
    	
    	return null;
    }
    
    //Add or remove a student from a specific course, should the parameter be a string or Student obj?
    public void addStudentToCourse(String studentName, String courseName) {
    	for(Course c : courses) {
    		if(c.equals(courseName)) {
    			c.addStudent(studentName);
    		}
    	}
    }
    
    public void removeStudentToCourse(String studentName, String courseName) {
    	for(Course c: courses) {
    		if(c.equals(courseName)) {
    			c.removeStudent(studentName);
    		}
    	}
    }
    
    public void addAssignmentToCourse(String assignmentName, String courseName, String type) {
    	for(Course c : courses) {
    		if(c.equals(courseName)) {
    			c.createAssignment(assignmentName, type);
    		}
    	}
    }
    
    public void removeAssignmnetFromCourse(String assignmentName, String courseName) {
    	for(Course c : courses) {
    		if(c.equals(courseName)) {
    			c.removeAssignment(assignmentName);
    		}
    	}
    }
}
