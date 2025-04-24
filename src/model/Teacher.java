package model;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends User {

    public Teacher(String username, Gradebook gradebook) {
        super(username, "teacher");
    }


    
    //make groups given the name of a course and the size of the groups
    //Return a string containing the groups?
    public String makeGroups(String courseName, int size){
    	ArrayList<ArrayList<Student>> groups = null;
    	for(Course c : courses) {
    		if(c.equals(courseName)) {
    			groups = c.makeGroups(size);
    		}
    	}
    	String answer = null;
    	for(int i = 1; i <= groups.size(); i++) {
    		answer += "Group "+"i"+":\n";
    		for(int j = 0; j < groups.get(i).size(); j++) {
    			answer += groups.get(i).get(j);
    			answer +=".\n";
    		}
    		answer+="\n";
    	}
    	
    	return answer;
    }
    
    public String viewStudentsInCourse(String courseName) {
    	for(Course c : courses) {
    		if(c.equals(courseName)) {
    			return c.toString();
    		}
    	}
    	return "Course not found.\n";
    }
    
    public boolean dropAssignmnet(String courseName, String assignmnetName) {
    	for(Course c : courses) {
    		if(c.equals(assignmnetName)) {
    			c.dropAssignment(assignmnetName);
    			return true;
    		}
    	}
    	return false;
    }
    
    public String viewUngradedAssignments(String courseName) {
    	for(Course c : courses) {
    		if(c.equals(courseName)) {
    			return c.viewUngradedAssignments();
    		}
    	}
    	return "Course not found\n";
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

    public String getStudentByName(String courseName) {
    	ArrayList<Student> students = new ArrayList<Student>();
    	for(Course c : courses) {
    		if(c.equals(courseName)) {
    			students = c.sortByName();
    		}
    	}
    	String answer = "";
    	for(Student s : students) {
    		answer += s.toString();
    	}
    	return answer;
    }
    
    public String getStudentByGrade(String courseName) {
    	ArrayList<Student> students = new ArrayList<Student>();
    	for(Course c : courses) {
    		if(c.equals(courseName)) {
    			students = c.sortByGrade();
    		}
    	}
    	String answer = "";
    	for(Student s : students) {
    		answer += s.toString();
    	}
    	return answer;
    }
    
    public void addGradeFromStudentToCourse(String courseName, String assignmnetName, String studentName, double grade) {
    	for(Course c : courses) {
    		if(c.equals(courseName)) {
    			c.setGrade(assignmnetName, studentName, grade);
    		}
    	}
    }
    
    public void removeAssignmnetFromCourse(String courseName, String assignmentName) {
    	for(Course c : courses) {
    		if(c.equals(courseName)) {
    			c.removeAssignment(assignmentName);
    		}
    	}
    }
    
    public String setWeightOnAssignmentType(String courseName, String type, double grade) {
    	boolean found = false;
    	for(Course c : courses) {
    		if(c.equals(courseName)) {
    			found = c.setWeight(type, grade);
    			break;
    		}
    	}
    	if(found) {
    		return "successfully changed weight on "+type+".\n";
    	}
    	return "Incorrect weight or course not found";
    }
    
    public String studentAverageonCourse(String courseName, String studentName) {
    	for(Course c : courses) {
    		if(c.equals(courseName)) {
    			String answer = "Student "+studentName+" have an average of " ;
    			answer += c.getStudentAverage(studentName);
    			answer+=" in the course: "+courseName+".\n";
    			return answer;
    		}
    	}
    	return "Course or Student not found";
    }
    
    public String viewCourseAverage(String courseName) {
    	for(Course c : courses) {
    		if(c.equals(courseName)) {
    			String answer = "The average on course "+courseName+" is ";
    			answer += c.getClassAverage();
    			return answer;
    		
    		}
    	}
    	return "Course not found";
    }
    
    public String viewCourseMedian(String courseName) {
    	for(Course c : courses) {
    		if(c.equals(courseName)) {
    			String answer = "The median on course "+courseName+" is ";
    			answer += c.getClassMedian();
    			return answer;
    		}
    	}
    	return "Course not found";
    }
}
