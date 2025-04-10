package model;

import java.util.ArrayList;

public class Course {
	private ArrayList<Student> studentList;
	private final String courseName;
	private ArrayList<Assignment> assignments;
	private boolean current;
	
	public Course(String courseName) {
		this.courseName = courseName;
		current = true;
	}
	
	public void addStudent(String student) {
		studentList.add(new Student(student));
	}
	
	//Add student List from .txt file
	
	//Returns list of all students
	@Override
	public String toString() {
		String answer = "Currently enrolled:\n";
		for(Student stu : studentList) {
			answer += stu.toString();
			answer += "\n";
		}
		return answer;
	}
	
	//Creates new assignment and saves it on the class list
	public void createAssignment(String name, String type) {
		Assignment current = new Assignment(name, type, studentList);
		assignments.add(current);
	}
	
	//Set the course as finished
	public void endCourse() {
		current = false;
	}
	
	public void setGrade(String assignment, String name, double grade) {
		for(Assignment a : assignments) {
			if(a.getAssignment().equalsIgnoreCase(name)) {
				a.setStudentGrade(name, grade);
			}
		}
	}
	
	
	//When function called check if grade != 0.0, if it is then it is ungraded
	public double getGrade(String assignment, String name) {
		double grade = 0;
		for(Assignment a : assignments) {
			if(a.getAssignment().equalsIgnoreCase(assignment)) {
				grade = a.getStudentGrade(name);
			}
		}
		return grade;
	}
	
	public boolean isCourseOngoing() {
		return current;
	}
}
