package task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TaskUIFormat {
	
	/*
	 * Class Constants
	 */
	
	//private static final Comparator<Task> startTimeComparator = new TaskStartTimeComparator();
	private static final Comparator<Task> endTimeComparator = new TaskEndTimeComparator();
	
	/*
	 * Class Variables
	 */
	
	//private static Logger logger = ZombieLogger.getLogger();
	
	/*
	 * Instance Variables
	 */
	
	private ArrayList<Task> floatingTasks;
	private ArrayList<Task> deadlineTasks;
	private ArrayList<Task> timedTasks;
	private int floatingIndex = 0;
	private int deadlineIndex = 0;
	private int timedIndex = 0;
	
	public TaskUIFormat(ArrayList<Task> newFloatingTasks, ArrayList<Task> newDeadlineTasks, ArrayList<Task> newTimedTasks){
		floatingTasks = newFloatingTasks;
		deadlineTasks = newDeadlineTasks;
		timedTasks = newTimedTasks;
	}
	
	public TaskUIFormat(){
		floatingTasks = new ArrayList<Task>();
		deadlineTasks = new ArrayList<Task>();
		timedTasks = new ArrayList<Task>();
	}
	
	public void addTask(Task newTask){
		if (newTask.isFloatingTask()){
			floatingTasks.add(newTask);
		}
		if (newTask.isDeadlineTask()){
			deadlineTasks.add(newTask);
		}
		if (newTask.isTimedTask()){
			timedTasks.add(newTask);
		}
	}
	
	public boolean isEmpty(){
		return floatingTasksIsEmpty() && deadlineTasksIsEmpty() && timedTasksIsEmpty();
	}
	public boolean floatingTasksIsEmpty() {
		return(floatingTasks == null) ? true : floatingTasks.isEmpty();
	}
	public boolean deadlineTasksIsEmpty() {
		return(deadlineTasks == null) ? true : deadlineTasks.isEmpty();
	}
	public boolean timedTasksIsEmpty() {
		return(timedTasks == null) ? true : timedTasks.isEmpty();
	}
	public ArrayList<Task> getFloatingTasks() {
		return (floatingTasks == null) ? new ArrayList<Task>() : floatingTasks;
	}

	public void setFloatingTasks(ArrayList<Task> floatingTasks) {
		this.floatingTasks = floatingTasks;
	}

	public ArrayList<Task> getDeadlineTasks() {
		return (deadlineTasks == null) ? new ArrayList<Task>() : deadlineTasks;
	}

	public void setDeadlineTasks(ArrayList<Task> deadlineTasks) {
		this.deadlineTasks = deadlineTasks;
	}

	public ArrayList<Task> getTimedTasks() {
		return (timedTasks == null) ? new ArrayList<Task>() : timedTasks;
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
}