package task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logger.ZombieLogger;

public class TaskUIFormat {
	
	/*
	 * Class Constants
	 */
	
	
	private static final Comparator<Task> startTimeComparator = new TaskStartTimeComparator();
	private static final Comparator<Task> endTimeComparator = new TaskEndTimeComparator();
	
	
	
	/*
	 * Class Variables
	 */
	
	private static Logger logger = ZombieLogger.getLogger();
	
	/*
	 * Instance Variables
	 */
	
	private ArrayList<Task> floatingTasks;
	private ArrayList<Task> deadlineTasks;
	private ArrayList<Task> timedTasks;
	private int floatingIndex = 0;
	private int deadlineIndex = 0;
	private int timedIndex = 0;
	
	/*
	 
	private boolean hasSorted = false;
	
	public TaskUIFormat(ArrayList<Task> taskList){
		
		for (Task task : taskList){
			if (task.isFloatingTask()){
				floatingTasks.add(task);
				continue;
			}
			
			if (task.hasStartTime()){
				timedTasks.add(task);
				continue;
			}
			
			//Only deadlineTasks left
			if (task.hasEndTime()){
				deadlineTasks.add(task);
				continue;
			}
		}
		sortTimedTasks();
		sortDeadlineTasks();
	}
	
	*/
	
	public TaskUIFormat(ArrayList<Task> newFloatingTasks, ArrayList<Task> newDeadlineTasks, ArrayList<Task> newTimedTasks){
		floatingTasks = newFloatingTasks;
		deadlineTasks = newDeadlineTasks;
		timedTasks = newTimedTasks;
	}
	
	public boolean isEmpty(){
		return floatingTasks.isEmpty() && deadlineTasks.isEmpty() && timedTasks.isEmpty();
	}

	public ArrayList<Task> getFloatingTasks() {
		return floatingTasks;
	}

	public void setFloatingTasks(ArrayList<Task> floatingTasks) {
		this.floatingTasks = floatingTasks;
	}

	public ArrayList<Task> getDeadlineTasks() {
		return deadlineTasks;
	}

	public void setDeadlineTasks(ArrayList<Task> deadlineTasks) {
		this.deadlineTasks = deadlineTasks;
	}

	public ArrayList<Task> getTimedTasks() {
		return timedTasks;
	}

	public void setTimedTasks(ArrayList<Task> timedTasks) {
		this.timedTasks = timedTasks;
	}
	
	public void removeTask(Task deletedTask){
		floatingTasks.remove(deletedTask);
		deadlineTasks.remove(deletedTask);
		timedTasks.remove(deletedTask);
	}
	
	public int size(){
		return floatingTasks.size() + deadlineTasks.size() + timedTasks.size();
	}
	
	public ArrayList<Task> getScheduledTasks(){
		ArrayList<Task> scheduledTasks = new ArrayList<Task>();
		scheduledTasks.addAll(deadlineTasks);
		scheduledTasks.addAll(timedTasks);
		Collections.sort(scheduledTasks, endTimeComparator);
		return scheduledTasks;
	}
	
	/**
	 * Passes the next Task for printing.
	 * Passes floating tasks first, followed by mixture of
	 * deadline and timed tasks in order of their endtime.
	 * 
	 * @return Task
	 */
	
	public Task nextTask(){
		Task tempTask = null;
		if (floatingTasks == null || floatingTasks.size() == floatingIndex &&
				deadlineTasks.size() == deadlineIndex && timedTasks.size() == timedIndex){
			;
		}else if (floatingTasks != null && !(floatingTasks.size() == floatingIndex)){
			tempTask = floatingTasks.get(floatingIndex);
			floatingIndex++;
		}else if (deadlineTasks.size() == deadlineIndex){
			tempTask = timedTasks.get(timedIndex);
			timedIndex++;
		}else if (timedTasks.size() == timedIndex){
			tempTask = deadlineTasks.get(deadlineIndex);
			deadlineIndex++;
		}else if (deadlineTasks.get(deadlineIndex).getEndTime().before(timedTasks.get(timedIndex).getEndTime())){
			tempTask = deadlineTasks.get(deadlineIndex);
			deadlineIndex++;
		}else {
			tempTask = timedTasks.get(timedIndex);
			timedIndex++;
		}
			return tempTask;
	}
	
	/*
	
	private void sortTimedTasks(){
		Collections.sort(timedTasks, startTimeComparator);
	}
	
	private void sortDeadlineTasks(){
		Collections.sort(deadlineTasks, endTimeComparator);
	}
	
	public Task getTask(String searchString){
		
		logger.log(Level.FINER, searchString);
		
		Matcher matcher = VALID_PATTERN.matcher(searchString);
		if (matcher.groupCount() < 2) {
			logger.log(Level.SEVERE, String.format(DEBUG_INVALID_IO, searchString),
					new IOException());
		}
		if (!hasSorted) {
			logger.log(Level.SEVERE, DEBUG_HASNT_SORTED,
					new IOException());
		}
		
		String taskType = matcher.group(0).toLowerCase();
		int index = new Integer(matcher.group(1));
		
		try {
			switch (taskType){
			case FLOATING_ID:
				return floatingTasks.get(index);
			case DEADLINE_ID:
				return deadlineTasks.get(index);
			case TIMED_ID:
				return timedTasks.get(index);
			}
		}catch (Exception e){
			logger.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}
	
	*/

}
