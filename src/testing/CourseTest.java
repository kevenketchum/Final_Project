package testing;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course("Math");
        course.addStudent("alice");
        course.addStudent("bob");
        course.createAssignment("HW1", "assignment");
        course.createAssignment("Quiz1", "quiz");
        course.createAssignment("Test1", "test");
        course.setGrade("HW1", "alice", 90);
        course.setGrade("Quiz1", "alice", 80);
        course.setGrade("Test1", "alice", 70);
        course.setGrade("HW1", "bob", 85);
        course.setGrade("Quiz1", "bob", 75);
        course.setGrade("Test1", "bob", 65);
    }
    
    /*
    @Test
    void testSetWeightValid() {
        assertTrue(course.setWeight("quiz", 0.1));
        double expectedWeight = 0.3 / 1; // only one quiz
        double actualWeight = course.getAssignments().stream().filter(a -> a.getType() == AssignmentType.QUIZ).findFirst().get().getWeight();
        assertEquals(expectedWeight, actualWeight, 0.01);
    }
    */

    @Test
    void testSetWeightInvalid() {
        assertFalse(course.setWeight("quiz", 0.9));
    }

    @Test
    void testSetWeightInvalidType() {
        assertFalse(course.setWeight("project", 0.3));
    }

    @Test
    void testRemoveStudent() {
        course.removeStudent("bob");
        double expected = 90 * 0.3 + 80 * 0.2 + 70 * 0.5;
        assertEquals(expected, course.getClassAverage(), 0.01);
    }

    @Test
    void testToStringOutput() {
        String output = course.toString();
        assertTrue(output.contains("alice"));
        assertTrue(output.contains("bob"));
    }

    @Test
    void testRemoveAssignment() {
        course.removeAssignment("HW1");
        assertEquals(2, course.getAssignments().size());
    }

    @Test
    void testEndCourseAndFinalGrades() {
        course.endCourse();
        assertEquals("C", course.getStudentFinalGrade("alice"));
    }

    @Test
    void testFinalGradeOutOfRange() {
        course.endCourse();
        course.setGrade("Test1", "alice", -100);
        assertEquals("Error encountered", course.getStudentFinalGrade("alice"));
    }

    @Test
    void testGetAssignmentGrade() {
        assertEquals(90.0, course.getAssignmentGrade("HW1", "alice"));
    }

    @Test
    void testMakeGroups() {
        ArrayList<ArrayList<Student>> groups = course.makeGroups(1);
        assertEquals(2, groups.size());
        assertEquals(1, groups.get(0).size());
    }

    @Test
    void testGetStudentAverage() {
        double grade = 90 * 0.3 + 80 * 0.2 + 70 * 0.5;
        double expected = grade / 3;
        assertEquals(expected, course.getStudentAverage("alice"), 0.01);
    }

    @Test
    void testGetClassAverage() {
        double aliceGrade = 90 * 0.3 + 80 * 0.2 + 70 * 0.5;
        double bobGrade = 85 * 0.3 + 75 * 0.2 + 65 * 0.5;
        double expected = (aliceGrade + bobGrade) / 2;
        assertEquals(expected, course.getClassAverage(), 0.01);
    }

    @Test
    void testGetClassMedian() {
        double aliceGrade = 90 * 0.3 + 80 * 0.2 + 70 * 0.5;
        double bobGrade = 85 * 0.3 + 75 * 0.2 + 65 * 0.5;
        double expected = (aliceGrade + bobGrade) / 2;
        assertEquals(expected, course.getClassMedian(), 0.01);
    }

    @Test
    void testDropAssignment() {
        course.dropAssignment("Quiz1");
        assertEquals(0.0, course.getAssignmentGrade("Quiz1", "alice"), 0.01);
    }

    @Test
    void testViewUngradedAssignments() {
        course.createAssignment("Extra", "assignment");
        String result = course.viewUngradedAssignments();
        
        assertTrue(result.contains("Extra"));
    }

    @Test
    void testEqualsCourseName() {
        assertTrue(course.equals("Math"));
        assertFalse(course.equals("Science"));
    }

    @Test
    void testIsCourseOngoing() {
        assertTrue(course.isCourseOngoing());
        course.endCourse();
        assertFalse(course.isCourseOngoing());
    }

    @Test
    void testGetName() {
        assertEquals("Math", course.getName());
    }

    @Test
    void testEmptyAssignmentListReturnsZeroAverage() {
        Course newCourse = new Course("Physics");
        newCourse.addStudent("eve");
        assertEquals(0.0, newCourse.getStudentAverage("eve"));
        assertEquals(0.0, newCourse.getClassAverage());
        assertEquals(0.0, newCourse.getClassMedian());
    }
    
    @Test
    void testGetStudentAssignment() {
        String result = course.getStudentAssignment("alice");
        assertTrue(result.contains("Assignment HW1, assignment: 90.0."));
        assertTrue(result.contains("Assignment Quiz1, quiz: 80.0."));
        assertTrue(result.contains("Assignment Test1, test: 70.0."));
    }
}
