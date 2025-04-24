package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {

    public Student(String username) {
        super(username, "student");
    }

    public String viewAssignmentOnCourse(String courseName) {
        String answer ="Assignments on course "+courseName+":\n";
        for(Course c : courses) {
            if(c.equals(courseName)) {
                answer+= c.getStudentAssignment(this.username);
            }
        }
        return answer;
    }

    public double viewCourseAverage(String courseName) {
        double answer = 0.0;
        for(Course c : courses) {
            if(c.equals(courseName)) {
                answer = c.getStudentGrade(this.username);
            }
        }
        return answer;
    }

    public String viewCourseFinalGrade(String courseName) {
        String answer="Final grade for course "+courseName+": ";
        for(Course c : courses) {
            if(c.equals(courseName)) {
                answer += c.getStudentFinalGrade(this.username);
                break;
            }
        }
        answer+="\n";
        return answer;
    }

    public double getGPA() {
        ArrayList<String> allGrades = new ArrayList<String>();
        for(Course c : courses) {
            if(!c.isCourseOngoing()) {
                allGrades.add(c.getStudentFinalGrade(this.username));
            }
        }
        double gpa = 0.0;
        for(String grade : allGrades) {
            if(grade.equals("A")) {
                gpa += 4;
            }
            else if(grade.equals("B")) {
                gpa += 3;
            }
            else if(grade.equals("C")) {
                gpa += 2;
            }
            else if(grade.equals("D")) {
                gpa += 1;
            }
        }
        if(gpa == 0.0) {
        	return 0;
        }
        return gpa / allGrades.size();
    }

    public String getName() {
        return this.username;
    }

    public double getAverageGrade() {
        double total = 0.0;
        int count = 0;
        for (Course c : courses) {
            if (c.isCourseOngoing()) {
                total += c.getStudentGrade(this.username);
                count++;
            }
        }
        return count == 0 ? 0.0 : total / count;
    }

    public ArrayList<Course> getCourseList() {
        return courses;
    }
}
