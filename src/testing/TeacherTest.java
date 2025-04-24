package testing;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {

    private Teacher teacher;
    private Course course;

    @BeforeEach
    void setUp() {
        teacher = new Teacher("mrsmith", null);
        course = new Course("Math");
        course.addStudent("alice");
        course.addStudent("bob");
        course.createAssignment("HW1", "assignment");
        course.createAssignment("Test1", "test");
        course.setGrade("HW1", "alice", 95);
        course.setGrade("Test1", "alice", 85);
        teacher.addCourse(course);
    }

    @Test
    void testViewStudentsInCourse() {
        assertTrue(teacher.viewStudentsInCourse("Math").contains("alice"));
    }

    @Test
    void testViewStudentsInUnknownCourse() {
        assertEquals("Course not found.\n", teacher.viewStudentsInCourse("Science"));
    }

    @Test
    void testViewUngradedAssignments() {
        course.createAssignment("Quiz1", "quiz");
        assertTrue(teacher.viewUngradedAssignments("Math").contains("Quiz1"));
    }

    @Test
    void testViewUngradedAssignmentsUnknownCourse() {
        assertEquals("Course not found\n", teacher.viewUngradedAssignments("Science"));
    }

    @Test
    void testAddAndRemoveStudent() {
        teacher.addStudentToCourse("charlie", "Math");
        assertTrue(course.toString().contains("charlie"));
        teacher.removeStudentToCourse("charlie", "Math");
        assertFalse(course.toString().contains("charlie"));
    }

    @Test
    void testAddAssignmentToCourse() {
        teacher.addAssignmentToCourse("Quiz2", "Math", "quiz");
        assertEquals(3, course.getAssignments().size());
    }

    @Test
    void testAddGradeFromStudentToCourse() {
        teacher.addGradeFromStudentToCourse("Math", "HW1", "bob", 88);
        assertEquals(88, course.getAssignmentGrade("HW1", "bob"));
    }

    @Test
    void testDropAssignment() {
        assertTrue(teacher.dropAssignmnet("Math", "HW1"));
        assertEquals(0.0, course.getAssignmentGrade("HW1", "alice"), 0.01);
    }

    @Test
    void testDropAssignmentWrongCourse() {
        assertFalse(teacher.dropAssignmnet("Mathh", "WrongName"));
    }

    @Test
    void testRemoveAssignmentFromCourse() {
        teacher.removeAssignmnetFromCourse("Math", "HW1");
        assertEquals(1, course.getAssignments().size());
    }

    @Test
    void testSetWeightOnAssignmentTypeSuccess() {
        String response = teacher.setWeightOnAssignmentType("Math", "quiz", 0.2);
        assertTrue(response.contains("successfully changed weight"));
    }

    @Test
    void testSetWeightOnAssignmentTypeFail() {
        String response = teacher.setWeightOnAssignmentType("Science", "quiz", 0.2);
        assertEquals("Incorrect weight or course not found", response);
    }

    @Test
    void testStudentAverageOnCourse() {
        String result = teacher.studentAverageonCourse("Math", "alice");
        assertTrue(result.contains("Student alice have an average of"));
    }

    @Test
    void testStudentAverageOnCourseNotFound() {
        String result = teacher.studentAverageonCourse("Science", "alice");
        assertEquals("Course or Student not found", result);
    }

    @Test
    void testViewCourseAverage() {
        String result = teacher.viewCourseAverage("Math");
        assertTrue(result.contains("The average on course Math is"));
    }

    @Test
    void testViewCourseAverageNotFound() {
        assertEquals("Course not found", teacher.viewCourseAverage("Science"));
    }

    @Test
    void testViewCourseMedian() {
        String result = teacher.viewCourseMedian("Math");
        assertTrue(result.contains("The median on course Math is"));
    }

    @Test
    void testViewCourseMedianNotFound() {
        assertEquals("Course not found", teacher.viewCourseMedian("Science"));
    }

    @Test
    void testMakeGroups() {
        String result = teacher.makeGroups("Math", 1);
        assertNotNull(result);
        assertTrue(result.contains("Group"));
    }
} 
