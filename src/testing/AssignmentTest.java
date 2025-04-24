package testing;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;

import model.Assignment;
import model.AssignmentType;
import model.Student;

public class AssignmentTest {

    private Student student1;
    private Student student2;
    private ArrayList<Student> students;
    private Assignment assignment;

    @BeforeEach
    public void setUp() {
        student1 = new Student("alice");
        student2 = new Student("bob");
        students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        assignment = new Assignment("Homework 1", "assignment", students);
    }

    @Test
    public void testConstructorWithAssignmentType() {
        assignment = new Assignment("Homework 1", "assignment", students);
        assertEquals("Homework 1", assignment.getAssignment());
        assertEquals(AssignmentType.ASSIGNMENT, assignment.getType());
        assertEquals(0.0, assignment.getWeight());
        assertEquals(0.0, assignment.getStudentGrade("alice"));
        assertEquals(0.0, assignment.getStudentGrade("bob"));
        assertEquals("Assignment Homework 1, assignment: Dropped.\n", assignment.toString(student1));
    }

    @Test
    public void testConstructorWithQuizType() {
        assignment = new Assignment("Quiz 1", "quiz", students);
        assertEquals(AssignmentType.QUIZ, assignment.getType());
        assertEquals("Assignment Quiz 1, quiz: Dropped.\n", assignment.toString(student1));
    }

    @Test
    public void testConstructorWithTestType() {
        assignment = new Assignment("Midterm", "test", students);
        assertEquals(AssignmentType.TEST, assignment.getType());
        assertEquals("Assignment Midterm, test: Dropped.\n", assignment.toString(student1));
    }

    @Test
    public void testSetStudentGradeAndGetStudentGrade() {
        assignment = new Assignment("Homework 1", "assignment", students);
        assignment.setStudentGrade("alice", 85.0);
        assignment.setStudentGrade("bob", 90.0);
        assertEquals(85.0, assignment.getStudentGrade("alice"));
        assertEquals(90.0, assignment.getStudentGrade("bob"));
    }

    @Test
    public void testGetAllGrades() {
        assignment = new Assignment("Homework 1", "assignment", students);
        assignment.setStudentGrade("alice", 85.0);
        assignment.setStudentGrade("bob", 90.0);
        assertEquals(175.0, assignment.getAllGrades());
    }

    @Test
    public void testToStringWithGradesAndWeight() {
        assignment = new Assignment("Homework 1", "assignment", students);
        assignment.setWeight(1.0);
        assignment.setStudentGrade("alice", 95.5);
        String expected = "Assignment Homework 1, assignment: 95.5.\n";
        assertEquals(expected, assignment.toString(student1));
    }

    @Test
    public void testSetAndGetWeight() {
        assignment = new Assignment("Homework 1", "assignment", students);
        assignment.setWeight(0.5);
        assertEquals(0.5, assignment.getWeight());
    }

    @Test
    public void testDropAssignment() {
        assignment = new Assignment("Homework 1", "assignment", students);
        assignment.setWeight(0.8);
        assignment.dropAssignment();
        assertEquals(0.0, assignment.getWeight());
    }
}




