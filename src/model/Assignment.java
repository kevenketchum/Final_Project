package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Assignment {
	//Create enum to show if assignment, quiz, or exam?
	private final String name;
	private final HashMap<Student, Double> grades = new HashMap<Student, Double>();
	//Weight of assignment needs to sum to 1.0 counting all assignmnets on course
	//probably change to enum to show the assignment and then make function in course
	//to destribute the weight into all assignments
	private AssignmentType type;
	private double weight;
	
	public Assignment(String name, String type, ArrayList<Student> students) {
		this.name = name;
		//weight determines what kind of assignment it is, change weight on future
		this.weight = 0.0;
		if(type.equalsIgnoreCase("assignment")) {
			this.type = AssignmentType.ASSIGNMENT;
		}
		else if(type.equalsIgnoreCase("quiz")) {
			this.type = AssignmentType.QUIZ;
		}
		else if(type.equalsIgnoreCase("test")) {
			this.type =  AssignmentType.TEST;
		}
		else {
			System.out.println("Invalid type of assignment.\n");
			System.exit(1);
		}
		for(Student stu : students) {
			grades.put(stu, 0.0);
		}
	}
	
	public String getAssignment() {
		return this.name;
	}
	
	
	//Give new grade on assignment to Student name
	public void setStudentGrade(String name, double grade) {
		for(Student stu : grades.keySet()) {
			if(stu.getUsername().equalsIgnoreCase(name)) {
				grades.replace(stu, grade);
			}
		}
	}
	
	//Return the sum of all of the grades from all students
	public double getAllGrades(){
		double answer =0.0;
		for(Double grade : grades.values()) {
			answer += grade;
		}
		return answer;
	}
	
	//get grade for assignment from student
	//uses user's getUserName, change if this isn't just the name of the students
	public double getStudentGrade(String studentName) {
		double grade =0.0;
		for(Student stu : grades.keySet()) {
			if(stu.getUsername().equalsIgnoreCase(studentName)) {
				grade = grades.get(stu);
			}
		}
		
		return grade;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public double getWeight() {
		return this.weight;
	}
	
	public AssignmentType getType() {
		return this.type;
	}
	
	public void dropAssignment() {
		this.weight = 0.0;	}
}
