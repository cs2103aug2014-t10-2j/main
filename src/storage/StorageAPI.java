package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

import task.Task;
import task.TaskUIFormat;





public class StorageAPI {
	
	/*
	 * CLASS ATTRIBUTES
	 */
	
	private static final String DEFAULT_FILE = "ZombieStorage.txt";
	
	/*
	 * INSTANCE ATTRIBUTES
	 */
	
	private static boolean hasRead = false;
	private static String filename = DEFAULT_FILE;
	private static File file = null;
	private static BufferedReader br = null;
	private static BufferedWriter bw = null;
	private static StringBuilder sb = null;
	private static String jsonTaskList = null;
	private static Storage storage = null;
	
	/**
	 * Constructor for storage module
	 */
	
	public StorageAPI(){
		readFileOnce();
	}
	
	/**
	 * Constructor for storage module
	 * 
	 * @param newFilename new file name
	 */
	
	public StorageAPI(String newFilename){
		if (newFilename != null) {
			filename = newFilename;
		}
		readFileOnce();
	}
	
	/*
	 * FILE / FILENAME MUTATORS
	 */
	
	/**
	 * Sets filename
	 * @param newFileName String for new filename
	 * @throws IOException
	 */
	
	public void setFile(String newFileName) throws IOException{
		filename = newFileName;
		hasRead = false;
		readFileOnce();
	}
	
	/**
	 * Creates file
	 * @return
	 * @throws IOException
	 */
	
	public static void createFile() throws IOException{
		file = new File(filename);
		if(!file.exists()){
			file.createNewFile();
		}
	}
	
	public File getFile(){
		return file;
	}
	
	/**
	 * Get file name
	 * @return String representation of file name
	 */
	
	public String getFileName(){
		return file.getName();
	}
	
	/**
	 * Ensures file has been read once.
	 * 
	 * Defaults to new storage instance when uninitialized or during errors.
	 */
	
	private static void readFileOnce(){
		if (hasRead != false) {return; }
		try{
			createFile();
			br = new BufferedReader(new FileReader(file));
			sb = new StringBuilder();
			String line;
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			jsonTaskList = sb.toString();
			if (jsonTaskList != null) {
				storage = StorageJSONHandler.convertToStorage(jsonTaskList);
				if (storage == null){
					storage = new Storage();
				}
			}else{
				storage = new Storage();
			}
		}catch (Exception err){
			storage = new Storage();
			ui.UI.printResponse(err.getMessage());
		}
		hasRead = true;
	}
	
	/**
	 * Writes storage into file
	 * 
	 * @throws IOException
	 */
	
	private static void writeFile() throws IOException {
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getName()), "utf-8"));
		bw.write(StorageJSONHandler.convertToJSON(storage));
		bw.flush();
		bw.close();
	}
	
	/*
	 * Storage mutators
	 */
	
	/**
	 * Adds task to file
	 * 
	 * @param newTask
	 * @return
	 * @throws IOException
	 */
	
	public Task add(Task newTask) throws IOException{
		storage.addTask(newTask);
		writeFile();
		return newTask;
	}
	
	/**
	 * Removes task from file
	 * 
	 * @param task
	 * @return
	 * @throws IOException
	 */
	
	public Task delete(Task task) throws IOException{
		storage.removeTask(task);
		writeFile();
		return task;
	}
	
	/**
	 * support to remove many tasks at the same time(remove tasks between two times, remove tasks with the certain tag, location or name)
	 * 
	 * @param deletedTaskList
	 * @return TaskUIFormat
	 * @throws IOException
	 */
	
	public TaskUIFormat delete(TaskUIFormat deletedTaskList) throws IOException{
		storage.removeTask(deletedTaskList);
		return deletedTaskList;
	}
	/**
	 * Returns array list of all tasks
	 * @return
	 */
	
	public TaskUIFormat getAllTasks(){
		return storage.getAllTasks();
	}
	
	/**
	 * Returns an arraylist of all tasks between start time and end time.
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	
	public TaskUIFormat search(Calendar startTime, Calendar endTime) throws Exception{
		return storage.searchTask(startTime, endTime);
	}
	
	/**
	 * Returns task of a particular index
	 * 
	 * @param index
	 * @return
	 * @throws Exception
	 */
	
	public Task search(String searchString) throws Exception{
		return storage.search(searchString);
	}
	
	/**
	 * search task with a particular name
	 * @param taskName
	 * @return Task with particular name
	 * @throws Exception
	 */
	
	public TaskUIFormat searchName(String taskName) throws Exception{
		return storage.searchName(taskName);
	}
	
	/**
	 * search task with particular tag
	 * @param tagName
	 * @return a Task ArrayList which contains all tasks that has the particular tag
	 * @throws Exception
	 */
	
	public TaskUIFormat searchTag(String tagName) throws Exception{
		return storage.searchTag(tagName);
	}
	
	/**
	 * search tasks with particular location
	 * @param location
	 * @return a TaskUIFormat which contains all task that has a particular location
	 * @throws Exception
	 */
	
	public TaskUIFormat searchLocation(String location) throws Exception{
		return storage.searchLocation(location);
	}
	
	/**
	 * Returns index of task with respect to the data inside
	 * storage
	 * 
	 * @param task
	 * @return
	 */
	
	public String indexOf(Task task){
		return storage.indexOf(task);
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
		return storage.taskClash(newTask);
	}
	
	public void toggleComplete(Task task) throws IOException{
		task.toggleComplete();
		writeFile();
	}
	
	public void toggleDelete(Task task) throws IOException{
		task.toggleDelete();
		writeFile();
	}
	
}