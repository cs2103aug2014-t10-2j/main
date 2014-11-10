package task;

import java.util.Comparator;

/**
 * This comparator compares two Tasks by their start time
 * 
//@author jellymac
 *
 */

public class TaskStartTimeComparator implements Comparator<Task> {
	@Override
    public int compare(Task task1, Task task2) {
		return task1.getStartTime().compareTo(task2.getStartTime());
    }
}
