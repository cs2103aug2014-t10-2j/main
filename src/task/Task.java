package task;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * 
 * Task Class for ZombieTask. Holds information for each class.
 * 
 * @author nil
 * @author ljiy369
 * @author jellymac
 *
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
		if (isFloatingTask()){
			return false;
		}
		return this.endTime.before(Calendar.getInstance());
	}
	
	/**
	 * Returns whether Task is a floating task
	 * @return boolean value that indicates whether end time has passed
	 */
	
	public boolean isFloatingTask() {
		return this.endTime == null;
	}
	
	public boolean isDeadline() {
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
			if (currentTag.equalsIgnoreCase(tag)){
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
	
	/*
	 * REDUNDANT METHODS
	 */
	
	/*
	
	//setter and getter for deadline2
	public void setDeadline2(Calendar deadline2) {
		this.deadline2 = deadline2;	}
	public void setDeadline2(int year, int mth, int date, int hour, int min)
	{
		deadline2 = Calendar.getInstance();
		deadline2.set(year, mth - 1, date, hour, min, 0);
	}
	public Calendar getDeadline2() {
		return deadline2;	}
	public boolean isOverdue2() {
		return deadline2.before(Calendar.getInstance());	}
	public boolean isFloatingTask2() {
		return (deadline2 == null) ? true : false;	}
		
		*/
	
	/*
	public void setDeadline(int year, int mth, int date, int hour, int min)
	{
		deadline = Calendar.getInstance();
		deadline.set(year, mth - 1, date, hour, min, 0);
	}
	*/
	
	/*
	public String getTag(int index) {
		return (tags.isEmpty()) ? "" : tags.get(index);
	}
	*/
	
	
	/*
	 * Data / Time Formats
	 */
	
	/*
	public void addSubtask(String subtask) {
		subtasks.add(new Task(subtask));	}
	public void addSubtask(String subtask, Calendar date) {
		subtasks.add(new Task(subtask, date));
	}
	*/
	
	/*
	
	private static final Format FORMAT_DATETIME = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
	private static final Format FORMAT_DATEONLY = new SimpleDateFormat("dd/MM/yyyy");
	private static final Format FORMAT_TIME12H = new SimpleDateFormat("hh:mm a");
	private static final Format FORMAT_TIME24H = new SimpleDateFormat("HH:mm");
	
	*/
	
	/*
	public String subtasksToString()
	{
		String str = "";
		if(subtasks.isEmpty())	return "No Subtasks!";
		else
		{
			int i = 0;
			for(Task task: subtasks)	str += i++ + ": " +task.toString() + "\n";
		}
		return str;
	}
	*/
	
	/*
	
	//////////////////// MISCELLENEOUS ////////////////////
	public String toString()
	{
		return taskName + " [" + getDeadlineString() + "]";
	}
	public String getDeadlineString()
	{
		return (deadline == null) ? "" : FORMAT_DATETIME.format(deadline.getTime());
	}
	public String getDayString()
	{
		return (deadline == null) ? "" : FORMAT_DATEONLY.format(deadline.getTime());
	}
	public String getTime12HourString()
	{
		return (deadline == null) ? "" : FORMAT_TIME12H.format(deadline.getTime());
	}
	public String getTime24HourString()
	{
		return (deadline == null) ? "" : FORMAT_TIME24H.format(deadline.getTime());
	}
	public String postpone(int field, int num)
	{
		deadline.roll(field, num);
		return getDayString();
	}
	public String postpone(int num)	{	return postpone(Calendar.DATE, num);	}
	public String postponeTomorrow()	{	return postpone(Calendar.DATE, 1);	}
	public String postponeNextWeek()	{	return postpone(Calendar.DATE, 7);	}
	public String postponeNextMonth()	{	return postpone(Calendar.MONTH, 7);	}
	public int getYear()
	{
		return (deadline == null) ? 0 : deadline.get(Calendar.YEAR);
	}
	public int getMonth()
	{
		return (deadline == null) ? 0 : deadline.get(Calendar.MONTH);
	}
	public int getDate()
	{
		return (deadline == null) ? 0 : deadline.get(Calendar.DATE);
	}
	
	*/
}