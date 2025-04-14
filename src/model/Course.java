package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Course {
	private ArrayList<Student> studentList;
	private final String courseName;
	private ArrayList<Assignment> assignments;
	private boolean current;
	//tracks the weights of the assignments
	private HashMap<AssignmentType, Double> categoryWeights;
	
	public Course(String courseName) {
		this.courseName = courseName;
		current = true;
		
		//Default weight of assignments, must sum up to 1.0
        categoryWeights.put(AssignmentType.QUIZ, 0.2);
        categoryWeights.put(AssignmentType.ASSIGNMENT, 0.3);
        categoryWeights.put(AssignmentType.TEST, 0.5);
	}
	//work in progress, be able to change the weight of assignmnet type
	//must check that new weight does not bring the total weight>1.0
	/*
	public void setWeight(String type, double weight) {
		double prev;
		if(type.equalsIgnoreCase("assignment")) {
			prev =categoryWeights.replace(AssignmentType.ASSIGNMENT, weight);
			
		}
	}
	*/
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
		updateWeights();
	}
	
	public void removeAssignment(String name) {
		this.assignments.removeIf(Assignment -> Assignment.getAssignment().equalsIgnoreCase(name));
	}
	
	
	//Not sure if function works, make sure to test
	//updates the weight of the assignments, called when new assignment made
	private void updateWeights() {
        // Count how many of each assignment there is
        HashMap<AssignmentType, Integer> typeCounts = new HashMap<>();
        for (Assignment a : assignments) {
            typeCounts.put(a.getType(), typeCounts.getOrDefault(a.getType(), 0) + 1);
        }

        // Reassign weights based off total weight its supposed to have and divide it by
        //total type of assignment
        for (Assignment a : assignments) {
            double typeWeight = categoryWeights.getOrDefault(a.getType(), 0.0);
            int count = typeCounts.getOrDefault(a.getType(), 1);
            a.setWeight(typeWeight / count);
        }
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
