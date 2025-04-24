package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Course {
	private ArrayList<Student> studentList;
	private final String courseName;
	private ArrayList<Assignment> assignments;
	private boolean current;
	private HashMap<AssignmentType, Double> categoryWeights;

	public Course(String courseName) {
		this.courseName = courseName;
		this.studentList = new ArrayList<>();
		this.assignments = new ArrayList<>();
		this.current = true;
		this.categoryWeights = new HashMap<>();

		categoryWeights.put(AssignmentType.QUIZ, 0.2);
		categoryWeights.put(AssignmentType.ASSIGNMENT, 0.3);
		categoryWeights.put(AssignmentType.TEST, 0.5);
	}

	public boolean setWeight(String type, double newWeight) {
	    double totalWeight = newWeight;
	    AssignmentType newType= null;
	    if(type.equalsIgnoreCase("assignment")) {
			newType = AssignmentType.ASSIGNMENT;
		}
		else if(type.equalsIgnoreCase("quiz")) {
			newType = AssignmentType.QUIZ;
		}
		else if(type.equalsIgnoreCase("test")) {
			newType =  AssignmentType.TEST;
		}
		else return false;

	    for (AssignmentType key : categoryWeights.keySet()) {
	        if (key != newType) {
	            totalWeight += categoryWeights.get(key);
	        }
	    }

	    if (Math.abs(totalWeight - 1.0) > 0.0001) {
	        return false; 
	    }
	    categoryWeights.put(newType, newWeight);
	    updateWeights();
	    return true;
	}

	public void addStudent(String student) {
		studentList.add(new Student(student));
	}

	public void removeStudent(String studentName) {
		studentList.removeIf(Student -> Student.getName().equalsIgnoreCase(studentName));
	}

	@Override
	public String toString() {
		String answer = "Currently enrolled:\n";
		for(Student stu : studentList) {
			answer += stu.toString();
			answer += "\n";
		}
		return answer;
	}

	public void createAssignment(String name, String type) {
		Assignment current = new Assignment(name, type, studentList);
		assignments.add(current);
		updateWeights();
	}

	public void removeAssignment(String name) {
		this.assignments.removeIf(Assignment -> Assignment.getAssignment().equalsIgnoreCase(name));
	}

	private void updateWeights() {
        HashMap<AssignmentType, Integer> typeCounts = new HashMap<>();
        for (Assignment a : assignments) {
            typeCounts.put(a.getType(), typeCounts.getOrDefault(a.getType(), 0) + 1);
        }

        for (Assignment a : assignments) {
            double typeWeight = categoryWeights.getOrDefault(a.getType(), 0.0);
            int count = typeCounts.getOrDefault(a.getType(), 1);
            a.setWeight(typeWeight / count);
        }
	}

	public void endCourse() {
		current = false;
	}

	public void setGrade(String assignment, String name, double grade) {
		for(Assignment a : assignments) {
			if(a.getAssignment().trim().equalsIgnoreCase(assignment.trim())) {
				a.setStudentGrade(name.trim(), grade);
			}
		}
	}

	public String getStudentAssignment(String student) {
		String answer="";
		int index = 0;
		for(int i = 0; i < studentList.size(); i++) {
			if(studentList.get(i).getName().equals(student)) {
				index = i;
				break;
			}
		}
		for(Assignment a : assignments) {
			answer += a.toString(studentList.get(index));
			answer+="\n";
		}
		return answer;
	}

	public double getAssignmentGrade(String assignment, String name) {
		double grade = 0;
		for(Assignment a : assignments) {
			if(a.getAssignment().equalsIgnoreCase(assignment)) {
				grade = a.getStudentGrade(name);
			}
		}
		return grade;
	}

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

	public String getStudentFinalGrade(String studentName) {
		if(current) {
			return "Class is currently ongoing.\n";
		}
		else {
			double grade = getStudentGrade(studentName);
			if(grade<=100 & grade >=90) {
				return "A";
			}
			else if(grade <= 89 && grade >=80) {
				return "B";
			}
			else if(grade <= 79 && grade >= 70) {
				return "C";
			}
			else if(grade <= 69 && grade >=30) {
				return "D";
			}
			else if(grade <= 29 && grade >=0) {
				return "F";
			}
		}
		return "Error encountered";
	}

	public double getStudentGrade(String studentName) {
		double answer = 0.0;
		for(Assignment a : assignments) {
			double assignmentGrade = a.getStudentGrade(studentName.trim());
			assignmentGrade *= a.getWeight();
			answer += assignmentGrade;
		}
		return answer;
	}

	public double getStudentAverage(String studentName) {
		double answer = 0.0;
		for(Student a : studentList) {
			if(a.getName().equals(studentName)) {
				answer = getStudentGrade(a.getName());
			}
		}
		return assignments.isEmpty() ? 0.0 : answer / assignments.size();
	}

	public double getClassAverage() {
		double answer = 0.0;
		for(Student a : studentList) {
			answer += getStudentGrade(a.getName());
		}
		return studentList.isEmpty() ? 0.0 : answer / studentList.size();
	}

	public double getClassMedian(){
		ArrayList<Double> grades = new ArrayList<>();
		int numStudents = studentList.size();
		for(Student a : studentList) {
			grades.add(getStudentGrade(a.getName()));
		}
		Collections.sort(grades);
		if(numStudents % 2 == 1) {
			return grades.get(numStudents / 2);
		}
		else {
			int index = numStudents / 2;
			return (grades.get(index - 1) + grades.get(index)) / 2;
		}
	}

	public void dropAssignment(String assignmentName) {
		for(Assignment a : assignments) {
			if(a.getAssignment().equals(assignmentName)) {
				a.dropAssignment();
			}
		}
	}

	public String viewUngradedAssignments() {
		String answer = "Ungraded assignments:\n";
		for(Assignment a : assignments) {
			if(a.getAllGrades() == 0.0) {
				answer+= a.toString();
				answer += "\n";
			}
		}
		return answer;
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

	public ArrayList<Assignment> getAssignments() {
		return assignments;
	}
}
