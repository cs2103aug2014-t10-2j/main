package storage;

import java.util.ArrayList;
import java.util.Calendar;

import zombietask.Task;

/**
 * Storage module for ZombieTask
 * 
 * @author ljiy369
 * @author jellymac
 */

public class Storage {
	
	/*
	 * CLASS ATTRIBUTES
	 */
	
	private static String MESSAGE_TIME_CONFLICT = "START TIME AFTER END TIME";
	private static String MESSAGE_INDEX_TOO_LARGE = "GIVEN INDEX IS TOO LARGE";
	
	/*
	 * INSTANCE ATTRIBUTES
	 */
	
	ArrayList<Task> taskList;
	
	/*
	 * CONSTRUCTORS
	 */
	
	public Storage(){
		taskList = new ArrayList<Task>();
	}
	
	/*
	 * Setter and getter methods
	 */
	
	public ArrayList<Task> getTaskList(){
		return this.taskList;
	}
	
	public void addTask(Task newTask){
		this.taskList.add(newTask);
	}
	
	public void removeTask(Task searchTask){
		this.taskList.remove(searchTask);
	}
	
	public void updateTask(Task originalTask, Task newTask){
		removeTask(originalTask);
		addTask(newTask);
	}
	
	public ArrayList<Task> searchTask(Calendar startTime, Calendar endTime) throws Exception{
		if (startTime.after(endTime)){
			throw new Exception(MESSAGE_TIME_CONFLICT);
		}
		ArrayList<Task> searchTaskList = new ArrayList<Task> ();
		for(Task task : taskList){
			if (task.getEndTime() == null){
				continue;
			}
			if (task.getStartTime() == null){
				if (task.getEndTime().compareTo(endTime)<=0 && task.getEndTime().compareTo(startTime)>=0){
					searchTaskList.add(task);
				}
				continue;
			}
			if (task.getEndTime().compareTo(endTime)<=0 && task.getStartTime().compareTo(endTime)<=0){
				searchTaskList.add(task);
			}
		}
		return searchTaskList;
	}
	
	public Task search(int index) throws Exception{
		if (index >= taskList.size()){
			throw new Exception(MESSAGE_INDEX_TOO_LARGE);
		}
		return taskList.get(index);
	}
	
	public Task searchName(String taskName) throws Exception{
		for(Task task : taskList){
			if(task.getTaskName()==taskName){
				return task;
			}
		}
		return null;
	}
	public ArrayList<Task> searchTag(String tagName) throws Exception{
		ArrayList<Task> searchTaskList = new ArrayList<Task> ();
		for(Task task: taskList){
			ArrayList<String> tagList= task.getTags();
			for(String tagContent : tagList){
				if(tagContent == tagName){
					searchTaskList.add(task);
					break;
				}
			}
		}
		if(searchTaskList.size()!=0){
			return searchTaskList;
		}else{
			return null;
		}
	}

}
