package model;

import java.util.ArrayList;

public abstract class User {
    protected String username;
    protected String role;
    protected ArrayList<Course> courses;

    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return role + ": " + username;
    }
    
    public void addCourse(String courseName) {
    	this.courses.add(new Course(courseName));
    }
    
    //Kind of shitty view all courses user has had, shows currently enrolled first then
    //all donde courses, toString() already prints all students in course change maybe?
    public String viewCourses() {
    	String allCourses="courses currently enrolled:\n";
    	for(Course c : courses) {
    		if(c.isCourseOngoing()) {
    			allCourses += c.getName()+".\n";
    		}
    	}
    	allCourses += "Completed courses:\n";
    	for(Course c : courses) {
    		if(!c.isCourseOngoing()) {
    			allCourses += c.getName()+".\n";
    		}
    	}
    	return allCourses;
    }
}
