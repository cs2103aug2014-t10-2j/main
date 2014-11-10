package task;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Task Class for ZombieTask. Holds information for each class.
 * @author nil
 * @author A0119480
 * @author jellymac
 */

public class Task implements Comparable<Task> {	
	/*
	 * CLASS ATTRIBUTES
	 */
	
	private static String MESSAGE_TIME_CONFLICT = "START TIME AFTER END TIME";
	private static String MESSAGE_NO_SUBTASK = "THERE IS NO SUBTASK";
	
	/*
	 * INSTANCE ATTRIBUTES
	 */
	
	private String taskName = null;
	private Calendar endTime = null;
	private Calendar startTime = null;
	private ArrayList<String> tags = new ArrayList<String>();
	private ArrayList<Task> subtasks = new ArrayList<Task>();
	private String location = null;
	private boolean completed = false;
	private boolean deleted = false;
	
	/*
	 * CONSTRUCTORS
	 */

	/**
	 * Constructor for task
	 * @param taskName String identifier for Task
	 */
	
	public Task(String taskName){
		setTaskName(taskName);
	}
	
	/**
	 * Constructor for task
	 * @param taskName String identifier for Task
	 * @param endTime Calendar denoting end time
	 * @throws Exception 
	 */
	
	public Task(String taskName, Calendar endTime) throws Exception{
		setTaskName(taskName);
		setEndTime(endTime);
	}
	
	/**
	 * Constructor for Task
	 * @param taskName String identifier for Task
	 * @param endTime Calendar denoting end time
	 * @param startTime Calendar denoting start time
	 * @throws Exception 
	 */
	
	public Task(String taskName, Calendar endTime, Calendar startTime) throws Exception{
		setTaskName(taskName);
		setEndTime(endTime);
		setStartTime(startTime);
	}
	
	public Task(String taskName, Calendar endTime, Calendar startTime, String location) throws Exception{
		setTaskName(taskName);
		setEndTime(endTime);
		setStartTime(startTime);
		setLocation(location);
	}
	
	/*
	 * TASKNAME MUTATORS
	 */
	
	/**
	 * Sets new task name
	 * @param taskName String new task name
	 */
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	/**
	 * Gets task name from Task
	 * @return String task name of Task
	 */
	
	public String getTaskName() {
		return this.taskName;
	}
	
	/*
	 * CALENDAR MUTATORS
	 */
	
	/**
	 * Checks existence of endTime
	 * @return Boolean value indicating presence of end time
	 */
	
	public boolean hasEndTime(){
		return this.endTime != null;
	}
	
	/**
	 * Checks existence of startTime
	 * @return Boolean value indicating presence of end startTime
	 */
	
	public boolean hasStartTime(){
		return this.startTime != null;
	}
	
	/**
	 * Sets end time
	 * @param endTime new end time
	 * @throws Exception 
	 */
	
	public void setEndTime(Calendar endTime) throws Exception {
		this.endTime = endTime;
		checkTimeSanity();
	}
	
	/**
	 * Sets start time
	 * @param startTime new start time
	 * @throws Exception 
	 */
	
	public void setStartTime(Calendar startTime) throws Exception{
		this.startTime = startTime;
		checkTimeSanity();
	}
	
	/**
	 * Returns end time
	 * @return end time
	 */
	
	public Calendar getEndTime() {
		return this.endTime;
	}
	
	/**
	 * Returns start time
	 * @return start time
	 */
	
	public Calendar getStartTime() {
		return this.startTime;
	}
	
	/**
	 * Returns whether Task is overdue.
	 * @return boolean value that indicates whether end time has passed
	 */
	
	public boolean isOverdue() {
		if (this.isFloatingTask()){
			return false;
		}
		return this.endTime.before(Calendar.getInstance());
	}
	
	/**
	 * Returns whether Task is a floating task
	 * @return boolean value that indicates whether end time has passed
	 */
	
	public boolean isFloatingTask() {
		return this.endTime == null && this.startTime == null;
	}
	
	public boolean isDeadlineTask() {
		return (this.startTime == null && this.endTime != null);
	}
	
	public boolean isTimedTask() {
		return (this.startTime != null && this.endTime != null);
	}
	
	/*
	 * TAG MUTATORS
	 */
	
	/**
	 * Adds a string tag to a Task
	 * @param tag String
	 */
	
	public void addTag(String tag)
	{
		this.tags.add(tag);
	}
	
	/**
	 * Gets an arraylist of tags
	 * @return ArrayList<String> tags
	 */
	
	public ArrayList<String> getTags() {
		return this.tags;
	}
	
	/**
	 * Checks whether tag exists in a Task
	 * @param tag A string that identifies the tag
	 * @return boolean value to indicate tag existence
	 */
	
	public boolean hasTag(String tag) {
		for (String currentTag : tags){
			if (currentTag.equalsIgnoreCase(tag) || currentTag.equalsIgnoreCase("#"+tag)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Deletes tag
	 * @param tag String (tag) to be removed from task
	 */
	
	public void deleteTag(String tag)
	{
		ArrayList<String> tempTags = new ArrayList<String>();
		for (String currentTag : tags){
			if (!currentTag.equalsIgnoreCase(tag)){
				tempTags.add(currentTag);
			}
		}
		this.tags = tempTags;
	}
	
	/*
	 * SUBTASK MUTATORS
	 */
	
	/**
	 * Gets subtasks
	 * @return ArrayList<Task> list of subtasks
	 */

	public ArrayList<Task> getSubtask(){
		return subtasks;
	}
	
	/**
	 * Adds subtask to Task
	 * @param subTask Task to be added to 
	 * @throws Exception 
	 */
	
	public void addSubtask(Task subTask) throws Exception{
		if (subTask == null) {
			throw new Exception(MESSAGE_NO_SUBTASK);
		};
		subtasks.add(subTask);
	}
	
	/*
	 * Comparators
	 */
	
	public static boolean compareTaskName(Task task1, Task task2){
		return task1.getTaskName().equalsIgnoreCase(task2.getTaskName());
	}
	
	/**
	 * Raises exception when start time is after end time
	 * @throws Exception
	 */
	
	private void checkTimeSanity() throws Exception{
		if (startTime != null && endTime != null){
			if (startTime.after(endTime)){
				throw new Exception(MESSAGE_TIME_CONFLICT);
			}
		}
	}
	
	/**
	 * Compares time values between tasks based on endtime.
	 * 
	 * Default Comparator - End Time
	 * 
	 * @param task
	 * @return 
	 */
	
	public int compareTo(Task task){
		return this.getEndTime().compareTo(task.getEndTime());
	}
	
	/**
	 * 
	 * Checks whether Task is supposed to be carried out at the time of invocation.
	 * 
	 * @return true if current time is within timeframe of task
	 */
	
	public boolean currentTimedTask(){
		if (!isTimedTask()){
			return false;
		}
		return this.endTime.after(Calendar.getInstance()) && this.startTime.before(Calendar.getInstance());
	}
	
	/**
	 * 
	 * Returns task location
	 * 
	 * @return String
	 */
	
	public String getLocation() {
		return location;
	}
	
	public boolean isLocation(String newLocation){
		if (location == null){
			return false;
		}
		return location.equalsIgnoreCase(newLocation) || location.equalsIgnoreCase("@" + newLocation);
	}
	
	/**
	 * 
	 * Sets task location
	 * 
	 * @param location String
	 * 
	 */
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public boolean taskClash(Task otherTask){
		if (this.equals(otherTask) || otherTask.isFloatingTask() || this.isFloatingTask()){
			return false;
		}
		
		if (otherTask.isDeadlineTask() && this.isDeadlineTask()){
			return this.getEndTime().equals(otherTask.getEndTime());
		}
		
		if (otherTask.isDeadlineTask() && this.isTimedTask()){
			return otherTask.getEndTime().before(this.getEndTime()) && otherTask.getEndTime().after(this.getStartTime());
		}
		
		if (otherTask.isTimedTask() && this.isDeadlineTask()){
			return otherTask.getStartTime().before(this.getEndTime()) && otherTask.getEndTime().after(this.getEndTime());
		}
		
		return !(this.getStartTime().after(otherTask.getEndTime()) || this.getEndTime().before(otherTask.getStartTime()));
	}
	
	/**
	 * Changes the status of completion in Task
	 */
	
	public void toggleComplete() {
		completed = !completed;
	}
	
	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	/**
	 * Changes the status of deletion in Task
	 */
	
	public void toggleDelete(){
		deleted = !deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}