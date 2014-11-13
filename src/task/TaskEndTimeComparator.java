package task;

import java.util.Comparator;

/**
 * This comparator compares two Tasks by their end time
 * 
//@author a0066754w
 *
 */

public class TaskEndTimeComparator implements Comparator<Task>{
	@Override
    public int compare(Task task1, Task task2) {
		return task1.getEndTime().compareTo(task2.getEndTime());
    }
}