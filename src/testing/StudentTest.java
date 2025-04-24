package testing;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        student = new Student("alice");
        course = new Course("Math");
        course.addStudent("alice");
        course.createAssignment("HW1", "assignment");
        course.createAssignment("Quiz1", "quiz");
        course.createAssignment("Test1", "test");
        course.setGrade("HW1", "alice", 90);
        course.setGrade("Quiz1", "alice", 80);
        course.setGrade("Test1", "alice", 70);
        student.getCourseList().add(course);
    }

    @Test
    void testViewAssignmentOnCourse() {
        String expected = "Assignments on course Math:\n";
        expected += "Assignment HW1, assignment: 90.0.\n\n";
        expected += "Assignment Quiz1, quiz: 80.0.\n\n";
        expected += "Assignment Test1, test: 70.0.\n\n";
        assertEquals(expected, student.viewAssignmentOnCourse("Math"));
    }

    @Test
    void testViewCourseAverage() {
        double expected = 90 * 0.3 + 80 * 0.2 + 70 * 0.5;
        assertEquals(expected, student.viewCourseAverage("Math"), 0.01);
    }

    @Test
    void testViewCourseFinalGrade() {
        course.endCourse();
        String expected = "Final grade for course Math: C\n";
        assertEquals(expected, student.viewCourseFinalGrade("Math"));
    }

    @Test
    void testGetGPAWithOneCourse() {
        course.endCourse();
        assertEquals(2.0, student.getGPA(), 0.01);
    }
    
    @Test
    void testGetGPAWithMultipleAssignments() {
        course.createAssignment("HW2", "assignment");
        course.createAssignment("Quiz2", "quiz");
        course.createAssignment("Test2", "test");
        course.createAssignment("Test3", "test");

        course.setGrade("HW2", "alice", 100);
        course.setGrade("Quiz2", "alice", 60);
        course.setGrade("Test2", "alice", 75);
        course.setGrade("Test3", "alice", 92);
    	course.endCourse();
        assertEquals(3.0, student.getGPA(), 0.01);
    }

    @Test
    void testGetGPAWithMultipleCourses() {
        course.endCourse();
        Course course2 = new Course("History");
        course2.addStudent("alice");
        course2.createAssignment("Final", "test");
        course2.createAssignment("project", "assignment");
        course2.createAssignment("quiz1", "quiz");
        course2.setGrade("Final", "alice", 95);
        course2.setGrade("project", "alice", 80);
        course2.setGrade("quiz1", "alice", 70);
        course2.endCourse();
        student.getCourseList().add(course2);
        System.out.println(student.getGPA());
        System.out.println(student.viewCourseFinalGrade("MatH"));

        System.out.println(student.viewCourseFinalGrade("HisTory"));
        System.out.println(student.viewAssignmentOnCourse("history"));
        System.out.println(student.viewAssignmentOnCourse("math"));

        assertEquals(2.5, student.getGPA(), 0.1); // average of B and C
    }

    @Test
    void testGetName() {
        assertEquals("alice", student.getName());
    }

    @Test
    void testGetAverageGrade() {
        double expected = 90 * 0.3 + 80 * 0.2 + 70 * 0.5;
        assertEquals(expected, student.getAverageGrade(), 0.01);
    }

    @Test
    void testGetCourseList() {
        assertTrue(student.getCourseList().contains(course));
    }

    @Test
    void testGetGPAWithNoFinishedCourses() {
        assertEquals(0.0, student.getGPA());
    }

    @Test
    void testViewCourseFinalGradeOngoing() {
        String expected = "Final grade for course Math: Class is currently ongoing.\n\n";
        assertEquals(expected, student.viewCourseFinalGrade("Math"));
    }

    @Test
    void testViewAssignmentOnUnknownCourse() {
        assertEquals("Assignments on course Unknown:\n", student.viewAssignmentOnCourse("Unknown"));
    }

    @Test
    void testViewCourseAverageUnknownCourse() {
        assertEquals(0.0, student.viewCourseAverage("Unknown"));
    }

    @Test
    void testViewCourseFinalGradeUnknownCourse() {
        assertEquals("Final grade for course Unknown: \n", student.viewCourseFinalGrade("Unknown"));
    }
} 
