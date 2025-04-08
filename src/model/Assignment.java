package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Assignment {
	//Create enum to show if assignment, quiz, or exam?
	private final String name;
	private final HashMap<Student, Double> grades = new HashMap<Student, Double>();
	private double weight;
	
	public Assignment(String name, String type, ArrayList<Student> students) {
		this.name = name;
		//weight determines what kind of assignment it is, change weight on future
		if(type.equalsIgnoreCase("assignment")) {
			weight = 10;
		}
		else if(type.equalsIgnoreCase("quiz")) {
			weight = 20;
		}
		else if(type.equalsIgnoreCase("exam")) {
			weight = 50;
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
}
