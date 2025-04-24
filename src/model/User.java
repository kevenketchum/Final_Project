package model;

import java.util.ArrayList;

public abstract class User {
    protected String username;
    protected String role;
    protected ArrayList<Course> courses;

    public User(String username, String role) {
        this.username = username;
        this.role = role;
        this.courses = new ArrayList<>();
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

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    public String viewCourses() {
        String allCourses = "courses currently enrolled:\n";
        for (Course c : courses) {
            if (c.isCourseOngoing()) {
                allCourses += c.getName() + ".\n";
            }
        }
        allCourses += "Completed courses:\n";
        for (Course c : courses) {
            if (!c.isCourseOngoing()) {
                allCourses += c.getName() + ".\n";
            }
        }
        return allCourses;
    }
}
