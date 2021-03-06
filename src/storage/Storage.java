package storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import task.Task;
import task.TaskEndTimeComparator;
import task.TaskStartTimeComparator;
import task.TaskUIFormat;
import ui.UI;
import zombietask.ZombieTask;

/**
 * Storage module for ZombieTask
 * 
//@author A0119480
 */

public class Storage {
	
	/*
	 * CLASS ATTRIBUTES
	 */
	
	private static final String MESSAGE_TIME_CONFLICT = "START TIME AFTER END TIME";
	private static final String MESSAGE_INDEX_TOO_LARGE = "GIVEN INDEX IS TOO LARGE";
	private static final String DEBUG_INVALID_IO = "INVALID IO STRING: %s";
	private static final String DEBUG_HASNT_SORTED = "HAS NOT SORTED - HAS TO OPERATE FROM INSTANCE";
	
	private static final Pattern VALID_PATTERN = Pattern.compile("([a-zA-Z])([0-9]+)");
	
	private static final String FLOATING_ID = "f";
	private static final String DEADLINE_ID = "d";
	private static final String TIMED_ID = "t";
	
	private static final Comparator<Task> startTimeComparator = new TaskStartTimeComparator();
	private static final Comparator<Task> endTimeComparator = new TaskEndTimeComparator();
	
	public static final ArrayList<Task> EMPTY_LIST = new ArrayList<Task>();
	
	/*
	 * INSTANCE ATTRIBUTES
	 */
	
	private ArrayList<Task> floatingTasks;
	private ArrayList<Task> deadlineTasks;
	private ArrayList<Task> timedTasks;
	private Level loggingLevel;
	private static Logger logger = ZombieTask.getLogger();
	
	/*
	 * CONSTRUCTORS
	 */
	
	public Storage(){
		floatingTasks = new ArrayList<Task>();
		deadlineTasks = new ArrayList<Task>();
		timedTasks = new ArrayList<Task>();
	}
	
	/*
	 * Setter and getter methods
	 */
	
	/*
	public ArrayList<Task> getTaskList(){
		return this.taskList;
	}
	*/
	
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

	public void addTask(Task newTask){
		if(newTask.isDeadlineTask()){
			this.deadlineTasks.add(newTask);
		}else if (newTask.isFloatingTask()){
			this.floatingTasks.add(newTask);
		}else if (newTask.isTimedTask()){
			this.timedTasks.add(newTask);
		}else{
			logger.log(Level.SEVERE, newTask.toString());
		}
		sortTimedTasks();
		sortDeadlineTasks();
		//this.taskList.add(newTask);
	}
	public void addTask(TaskUIFormat newTaskList){
		for(Task task : newTaskList.getDeadlineTasks()){
			this.deadlineTasks.add(task);
		}
		for(Task task : newTaskList.getFloatingTasks()){
			this.floatingTasks.add(task);
		}
		for(Task task : newTaskList.getTimedTasks()){
			this.timedTasks.add(task);
		}
	}
	
	private void sortTimedTasks(){
		Collections.sort(timedTasks, startTimeComparator);
	}
	
	private void sortDeadlineTasks(){
		Collections.sort(deadlineTasks, endTimeComparator);
	}
	
	public void removeTask(Task searchTask){
		
		if(searchTask.isDeadlineTask()){
			this.deadlineTasks.remove(searchTask);
		}else if (searchTask.isFloatingTask()){
			this.floatingTasks.remove(searchTask);
		}else if (searchTask.isTimedTask()){
			this.timedTasks.remove(searchTask);
		}else{
			logger.log(Level.SEVERE, searchTask.toString());
		}
		//this.taskList.remove(searchTask);
	}
	
	public void removeTask(TaskUIFormat deletedTaskList){
		for(Task task : deletedTaskList.getDeadlineTasks()){
			this.removeTask(task);
		}
		for(Task task : deletedTaskList.getFloatingTasks()){
			this.removeTask(task);
		}
		for(Task task : deletedTaskList.getTimedTasks()){
			this.removeTask(task);
		}
	}
	
	public void toggleComplete(TaskUIFormat taskList){
		for(Task task : taskList.getDeadlineTasks()){
			task.toggleComplete();
		}
		for(Task task : taskList.getFloatingTasks()){
			task.toggleComplete();
		}
		for(Task task : taskList.getTimedTasks()){
			task.toggleComplete();
		}
	}
	
	public void updateTask(Task originalTask, Task newTask){
		removeTask(originalTask);
		addTask(newTask);
	}
	
	public TaskUIFormat searchTask(Calendar startTime, Calendar endTime) throws Exception{
		if (startTime.after(endTime)){
			Calendar tempTime = startTime;
			startTime = endTime;
			endTime = tempTime;
			//throw new Exception(MESSAGE_TIME_CONFLICT);
		}
		
		ArrayList<Task> searchDeadlineList = new ArrayList<Task> ();
		ArrayList<Task> searchTimedList = new ArrayList<Task> ();
		
		for(Task task : deadlineTasks){
			if (task.getEndTime().compareTo(endTime)<=0 && task.getEndTime().compareTo(startTime)>=0){
				searchDeadlineList.add(task);
			}
		}
		
		for(Task task : timedTasks){
			if (task.getEndTime().compareTo(endTime)<=0 && task.getStartTime().compareTo(startTime)>=0){
				searchTimedList.add(task);
			}
		}
		
		return new TaskUIFormat(EMPTY_LIST, searchDeadlineList, searchTimedList);
	}
	
	public Task search(String lineCode){
	
		
		Matcher matcher = VALID_PATTERN.matcher(lineCode);
		if (!matcher.find()){
			return null;
		}
		
		/*DEBUG
		UI.printResponse("LC: " + lineCode + "\n");
		UI.printResponse("GC: " + matcher.groupCount() + "\n");
		UI.printResponse("G0: " + matcher.group(0) + "\n");
		UI.printResponse("G1: " + matcher.group(1) + "\n");
		UI.printResponse("G2: " + matcher.group(2) + "\n");
		*/
		
		if (matcher.groupCount() < 2) {
			logger.log(Level.SEVERE, String.format(DEBUG_INVALID_IO, lineCode),
					new IOException());
		}
		
		String taskType = matcher.group(1).toLowerCase();
		int index = new Integer(matcher.group(2));
		
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
			e.printStackTrace();
		}
		return null;
	}
	
	public TaskUIFormat searchName(String taskName) throws Exception{
		ArrayList<Task> searchFloatingList = new ArrayList<Task> ();
		ArrayList<Task> searchDeadlineList = new ArrayList<Task> ();
		ArrayList<Task> searchTimedList = new ArrayList<Task> ();
		for(Task task : floatingTasks){
			if(task.getTaskName().contains(taskName)){
				searchFloatingList.add(task);
			}
		}
		for(Task task : deadlineTasks){
			if(task.getTaskName().contains(taskName)){
				searchDeadlineList.add(task);
			}
		}
		for(Task task : timedTasks){
			if(task.getTaskName().contains(taskName)){
				searchTimedList.add(task);
			}
		}
		return new TaskUIFormat(searchFloatingList, searchDeadlineList, searchTimedList);
	}
	
	public TaskUIFormat searchTag(String tagName) throws Exception{
		ArrayList<Task> searchFloatingList = new ArrayList<Task> ();
		ArrayList<Task> searchDeadlineList = new ArrayList<Task> ();
		ArrayList<Task> searchTimedList = new ArrayList<Task> ();
		for(Task task: floatingTasks){
			if(task.hasTag(tagName)){
				searchFloatingList.add(task);
			}
		}
		
		for(Task task: deadlineTasks){
			if(task.hasTag(tagName)){
				searchDeadlineList.add(task);
			}
		}
		
		for(Task task: timedTasks){
			if(task.hasTag(tagName)){
				searchTimedList.add(task);
			}
		}
		return new TaskUIFormat(searchFloatingList, searchDeadlineList, searchTimedList);
	}
	
	public TaskUIFormat searchLocation(String location){
		ArrayList<Task> searchFloatingList = new ArrayList<Task> ();
		ArrayList<Task> searchDeadlineList = new ArrayList<Task> ();
		ArrayList<Task> searchTimedList = new ArrayList<Task> ();
		for(Task task: floatingTasks){
			if(task.isLocation(location)){
				searchFloatingList.add(task);
			}
		}
		
		for(Task task: deadlineTasks){
			if(task.isLocation(location)){
				searchDeadlineList.add(task);
			}
		}
		
		for(Task task: timedTasks){
			if(task.isLocation(location)){
				searchTimedList.add(task);
			}
		}
		return new TaskUIFormat(searchFloatingList, searchDeadlineList, searchTimedList);
	}
	
	public TaskUIFormat getAllTasks(){
		return new TaskUIFormat(floatingTasks, deadlineTasks, timedTasks);
	}
	
	/**
	 * Returns index of task with respect to the data inside
	 * storage
	 * 
	 * @param task
	 * @return
	 */
	
	public String indexOf(Task task){
		if (task.isFloatingTask()){
			return "f".concat(String.valueOf(floatingTasks.indexOf(task)));
		}
		if (task.isDeadlineTask()){
			return "d".concat(String.valueOf(deadlineTasks.indexOf(task)));
		}
		if (task.isTimedTask()){
			return "t".concat(String.valueOf(timedTasks.indexOf(task)));
		}
		return null;
	}
	
	/**
	 * Returns true if task clashes if existing tasks in storage
	 * 
	 * Floating Tasks always returns false.
	 * 
	 * Dateline Tasks returns true when it clashes with other dateline tasks
	 * or timed tasks.
	 * 
	 * @param newTask
	 * @return boolean value
	 */
	
	public TaskUIFormat taskClash(Task newTask){
		
		TaskUIFormat clashedTasks = new TaskUIFormat();
		
		if (newTask.isFloatingTask()){
			return clashedTasks;
		}
		
		for (Task oldTask: timedTasks){
			if (!newTask.equals(oldTask)){
				if (newTask.taskClash(oldTask)){
					clashedTasks.addTask(oldTask);
				}
			}
		}
		
		for (Task oldTask: deadlineTasks){
			if (!newTask.equals(oldTask)){
				if (newTask.taskClash(oldTask)){
					clashedTasks.addTask(oldTask);
				}
			}
		}
		
		return clashedTasks;
	}
	
	public Level getLoggingLevel() {
		return loggingLevel;
	}

	public void setLoggingLevel(Level loggingLevel) {
		this.loggingLevel = loggingLevel;
	}

}
