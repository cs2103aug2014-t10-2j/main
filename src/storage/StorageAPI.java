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
	 * Returns array list of all tasks
	 * @return
	 */
	
	public ArrayList<Task> getAllTasks(){
		return storage.getTaskList();
	}
	
	/**
	 * Returns an arraylist of all tasks between start time and end time.
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	
	public ArrayList<Task> search(Calendar startTime, Calendar endTime) throws Exception{
		return storage.searchTask(startTime, endTime);
	}
	
	/**
	 * Returns task of a particular index
	 * 
	 * @param index
	 * @return
	 * @throws Exception
	 */
	
	public Task search(int index) throws Exception{
		return storage.search(index);
	}
	
	/**
	 * search task with a particular name
	 * @param taskName
	 * @return Task with particular name
	 * @throws Exception
	 */
	
	public Task searchName(String taskName) throws Exception{
		return storage.searchName(taskName);
	}
	
	/**
	 * search task with particular tag
	 * @param tagName
	 * @return a Task ArrayList which contains all tasks that has the particular tag
	 * @throws Exception
	 */
	
	public ArrayList<Task> searchTag(String tagName) throws Exception{
		return storage.searchTag(tagName);
	}
	
	/*
	 * DEPRECIATED METHODS AND VARIABLES
	 */
	
	/*
	 
	
	private static final String MESSAGE_TASKNAME ="taskName";
	private static final String MESSAGE_DEADLINE ="deadline";
	private static final String MESSAGE_DEADLINE2 = "deadline2";
	private static final String MESSAGE_TAGS ="tags";
	private static final String MESSAGE_SUBTASKS ="subtasks";
	
	
	public Task add(Task newTask) throws IOException{
		readFileOnce();
		jsonTaskList.put(convertTaskToJSON(newTask));
		writeFile();
		return newTask;
	}
	public ArrayList<Task> add(ArrayList<Task> taskList) throws IOException{
		readFileOnce();
		int index =0;
		while(taskList.get(index)!=null){
			jsonTaskList.put(convertTaskToJSON(taskList.get(index)));
		}
		writeFile();
		return taskList;
	}
	
	public Task search(int taskID) throws Exception{
		readFileOnce();
		Task searchTask= null;
		
		searchTask = convertJSONToTask((JSONObject)jsonTaskList.get(taskID));
		
		return searchTask;
	}
	
	
	
	public ArrayList<Task> search(String keyword) throws Exception{
		readFileOnce();
		ArrayList<Task> searchTaskList= new ArrayList<Task>();
		int index=0;
		while(jsonTaskList.get(index)!=null){
			if(searchName(keyword,(JSONObject)jsonTaskList.get(index))){
				searchTaskList.add(convertJSONToTask((JSONObject)jsonTaskList.get(index)));
			}else if(searchTag(keyword,(JSONObject)jsonTaskList.get(index))){
				searchTaskList.add(convertJSONToTask((JSONObject)jsonTaskList.get(index)));
			}else if(searchSubtask(keyword,(JSONObject)jsonTaskList.get(index))){
				searchTaskList.add(convertJSONToTask((JSONObject)jsonTaskList.get(index)));
			}
			index++;
		}
		return searchTaskList;
	}
	public ArrayList<Task> search(Calendar date1, Calendar date2) throws Exception{
		readFileOnce();
		ArrayList<Task> searchTaskList = new ArrayList<Task> ();
		Task temp;
		for(int i=0;i<jsonTaskList.length();i++){
			
			temp = convertJSONToTask((JSONObject)jsonTaskList.get(i));
			if(convertJSONToTask((JSONObject)jsonTaskList.get(i)).hasStartTime()){
				if(temp.getEndTime().compareTo(date1)>=0 && temp.getStartTime().compareTo(date2)<=0){
					searchTaskList.add(temp);
				}
			}else if(convertJSONToTask((JSONObject)jsonTaskList.get(i)).getEndTime()!=null){
				if(temp.getEndTime().compareTo(date1)>=0 && temp.getEndTime().compareTo(date2)<=0){
					searchTaskList.add(temp);
				}
			}
			
		}
		if(searchTaskList.isEmpty()){
			return null;
		}
		return searchTaskList;
	}
	public ArrayList<Task> search(Calendar date) throws IOException{
		readFileOnce();
		return taskList;
	}
	
	
	

	private boolean searchName(String keyword,JSONObject searchedTask){
		boolean result=false; 
		if(searchedTask.getString(MESSAGE_TASKNAME).contains(keyword)){
			result = true;
		}
		return result;
	}
	private boolean searchTag(String keyword,JSONObject searchedTask){
		boolean result = false;
		int tagIndex = 0;
		String tagContent;
		while((tagContent=((ArrayList<String>)searchedTask.get(MESSAGE_TAGS)).get(tagIndex).toString())!=null){
			if(tagContent.contains(keyword)){
				result = true;
			}
			tagIndex++;
		}
		return result;
	}
	private boolean searchSubtask(String keyword,JSONObject searchedTask){
		boolean result =false;
		int index=0;
		JSONObject tempJSON ;
		while((tempJSON=(JSONObject) ((JSONArray)searchedTask.get(MESSAGE_SUBTASKS)).get(index))!=null){
			if(searchName(keyword,tempJSON)){
				result = true;
				break;
			}else if(searchTag(keyword,tempJSON)){
				result = true;
				break;
			}else if(searchSubtask(keyword,tempJSON)){
				result = true;
				break;
			}
			index++;
		}
		
		return result;
	}
	
	
	public ArrayList <Task> displayAll() throws Exception{
		readFileOnce();
		ArrayList <Task> displayTaskList = new ArrayList <Task>();
		for(int i =0; i <jsonTaskList.length(); i++){
			displayTaskList.add(convertJSONToTask((JSONObject)jsonTaskList.get(i)));
		}
		
		return displayTaskList;
	}
	
	
	
	public Task delete(Task tempTask) throws IOException{
		readFileOnce();
		int index=0;
		while(index < jsonTaskList.length()){
			if(compareTask(tempTask,(JSONObject)jsonTaskList.get(index))){
				jsonTaskList.remove(index);
				writeFile();
				return tempTask;
			}
			index++;
		}
		return null;
	}
	public ArrayList<Task> delete(ArrayList<Task> taskList) throws IOException{
		readFileOnce();
		int taskListIndex =0;
		int jsonListIndex =0;
		while(taskList.get(taskListIndex)!=null){
			while(jsonTaskList.get(jsonListIndex)!=null){
				if(compareTask(taskList.get(taskListIndex),(JSONObject)jsonTaskList.get(jsonListIndex))){
					jsonTaskList.remove(jsonListIndex);
				}
				jsonListIndex++;
			}
			taskListIndex++;
		}
		writeFile();
		return taskList;
	}
	
	public Task update(int taskID, Task newTask) throws Exception{
		readFileOnce();
		Task originalTask = search(taskID);
		update(originalTask, newTask);
		return originalTask;
	}
	
	public Task update(Task originalTask, Task newTask) throws Exception{
		readFileOnce();
		delete(originalTask);
		add(newTask);
		return originalTask;
	}
	public ArrayList<Task> update(ArrayList<Task> originalTaskList,ArrayList<Task> newTaskList) throws Exception{
		readFileOnce();
		delete(originalTaskList);
		add(newTaskList);
		return originalTaskList;
	}
	
	//opens file to write content into memory
	private static void writeFile() throws IOException {
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getName()), "utf-8"));
		bw.write(jsonTaskList.toString());
		bw.flush();
		bw.close();
	}
	
	
	private static void readFileOnce() throws IOException{
		if (hasRead != false) {return; }
		br = new BufferedReader(new FileReader(file));
		tempTaskList = br.readLine();
		if (tempTaskList != null) {
			jsonTaskList = new JSONArray(tempTaskList);
		}else{
			jsonTaskList = new JSONArray();
		}
		br.close();
		hasRead = true;
	}
	
	private static JSONObject convertTaskToJSON(Task tempTask){
		JSONObject JSONTempTask = new JSONObject();
		JSONTempTask.put(MESSAGE_TASKNAME, tempTask.getTaskName());
		if(tempTask.getEndTime()!=null){
			JSONTempTask.put(MESSAGE_DEADLINE, tempTask.getEndTime());
		}
		if(tempTask.getTags()!=null){
			JSONTempTask.put(MESSAGE_TAGS, tempTask.getTags());
		}
		if(tempTask.getSubtask()!=null){
			JSONTempTask.put(MESSAGE_SUBTASKS, tempTask.getSubtask());
		}
		return JSONTempTask;
	}
	private static Task convertJSONToTask(JSONObject JSONTempTask) throws Exception{
		Task tempTask = new Task(JSONTempTask.getString(MESSAGE_TASKNAME));
		if(JSONTempTask.has(MESSAGE_DEADLINE)){
			tempTask.setEndTime((Calendar)JSONTempTask.get(MESSAGE_DEADLINE));
		}
		if(JSONTempTask.has(MESSAGE_TAGS)){
			ArrayList<String> tempList = (ArrayList<String>)JSONTempTask.get(MESSAGE_TAGS);
			int index =0;
			while(index<tempList.size()){
				tempTask.addTag(tempList.get(index));
				index++;
			}
		}
		if(JSONTempTask.has(MESSAGE_SUBTASKS)){
			ArrayList<Task> tempList = (ArrayList<Task>)JSONTempTask.get(MESSAGE_SUBTASKS);
			int index = 0;
			while(index<tempList.size()){
				tempTask.addSubtask(tempList.get(index));
			}
		}
		return tempTask;
	}
	private static boolean compareTask(Task tempTask, JSONObject jsonTask){
		boolean result =false;
		if(tempTask.getTaskName().equals(jsonTask.getString(MESSAGE_TASKNAME))){
			//if(tempTask.getEndTime().equals((Calendar)jsonTask.get(MESSAGE_DEADLINE))){
				result = true;
			//}
		}
		return result;
	}

	
	
	
	
	*/
	
}