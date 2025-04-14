package model;

import java.util.ArrayList;
import java.util.Collections;

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
	
	public void removeStudent(String studentName) {
		studentList.removeIf(Student -> Student.getName().equalsIgnoreCase(studentName));
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
	
	public void removeAssignment(String name) {
		this.assignments.removeIf(Assignment -> Assignment.getAssignment().equalsIgnoreCase(name));
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
	public double getAssignmentGrade(String assignment, String name) {
		double grade = 0;
		for(Assignment a : assignments) {
			if(a.getAssignment().equalsIgnoreCase(assignment)) {
				grade = a.getStudentGrade(name);
			}
		}
		return grade;
	}
	
	//make groups of the students using the given size, the groups is just a string containing multiple
	//students names? maybe make a 2dArray
	public ArrayList<ArrayList<Student>> makeGroups(int size){
		ArrayList<Student> copyStudentList = new ArrayList<>(studentList);
		Collections.shuffle(copyStudentList);
		ArrayList<ArrayList<Student>> groups = new ArrayList<>();
		
		for(int i = 0; i < copyStudentList.size(); i += size) {
			int end = Math.min(i + size,  copyStudentList.size());
			ArrayList<Student> group = new ArrayList<>(copyStudentList.subList(i, end));
			groups.add(group);
		}
		return groups;
		
	}
	
	//Gets student grade by getting assg1Grade*weight + assg2Grade*weight...
	//need to make sure that all of the assignments weight == 1.0
	//make function to destribute weight on assignmnets?
	public double getStudentGrade(String studentName) {
		double answer = 0.0;
		for(Assignment a : assignments) {
			double assignmentGrade = a.getStudentGrade(studentName);
			assignmentGrade *= a.getWeight();
			answer += assignmentGrade;
		}
		return answer;
	}
	
	//Get class average by getting all students grades and dividing it
	public double getClassAverage() {
		double answer = 0.0;
		for(Student a : studentList) {
			answer += getStudentGrade(a.getName());
		}
		return answer / studentList.size();
	}
	
	public String getName() {
		return this.courseName;
	}
	
	public boolean isCourseOngoing() {
		return current;
	}
	
	
	public boolean equals(String name){
		return this.courseName.equals(name);
	}
}
