package model;

import java.util.Comparator;

public class FirstNameComparator implements Comparator<Student>{

	
	@Override
    public int compare(Student o1, Student o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }

}
