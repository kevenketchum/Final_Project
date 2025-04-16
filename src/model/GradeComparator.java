package model;

import java.util.Comparator;

public class GradeComparator implements Comparator<Student> {

	//Comparator probably doesnt work, still havent updated Student class
	//Maybe we need to compare the students with a specific grade of a class?
	@Override
	public int compare(Student o1, Student o2) {
		if(o1.getAverageGrade() > o2.getAverageGrade()) {
			return 1;
		}
		else if(o1.getAverageGrade() < o2.getAverageGrade()) {
			return -1;
		}
		return 0;
	}
	
}
