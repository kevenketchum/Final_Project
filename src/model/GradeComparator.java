package model;
import java.util.Comparator;

public class GradeComparator implements Comparator<Student> {
    private final String courseName;

    public GradeComparator(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public int compare(Student s1, Student s2) {
        double grade1 = s1.viewCourseAverage(courseName);
        double grade2 = s2.viewCourseAverage(courseName);

        return Double.compare(grade1, grade2); // Sort ascending
    }
}
