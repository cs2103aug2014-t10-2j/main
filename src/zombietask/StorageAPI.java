package zombietask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

import json.JSONArray;
import json.JSONObject;



public class StorageAPI {
	
	/*
	 * Constants
	 */
	
	private static final String MESSAGE_TASKNAME ="taskName";
	private static final String MESSAGE_DEADLINE ="deadline";
	private static final String MESSAGE_DEADLINE2 = "deadline2";
	private static final String MESSAGE_TAGS ="tags";
	private static final String MESSAGE_SUBTASKS ="subtasks";
	
	/*
	 * Instance variables
	 */
	
	private static boolean hasRead = false;
	private static String filename;
	private static File file;
	private static BufferedReader br;
	private static BufferedWriter bw;
	private static JSONArray jsonTaskList ;
	private static String tempTaskList;
	ArrayList<Task> taskList;
	
	
	/**
	 * Constructor for storage module
	 */
	
	public StorageAPI(){
		filename = "ZombieStorage.txt";
		file = null;
		br = null;
		bw = null;
		jsonTaskList = new JSONArray();
		taskList = null;
	}
	
	public StorageAPI(String newFilename){
		if (newFilename != null) {
			filename = newFilename;
		}else{
			filename = "ZombieStorage.txt";
		};
		file = null;
		br = null;
		bw = null;
		jsonTaskList = new JSONArray();
		taskList = null;
	}

/*	+add(): Task(newTask)
		add(Task): 									Task(newTask)
		add(ArrayList<Task>):						ArrayList<Task> taskList(newTask)
*/
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
	
/*	+search(): ArrayList<Task> taskList(searched task)
		search(String keyword): 					ArrayList<Task> taskList(searched task)
		search(Calendar date, dates): 				ArrayList<Task> taskList(searched task)
		search(Calendar date):						ArrayList<Task> taskList(searched task)
*/
	public Task search(int taskID) throws IOException{
		readFileOnce();
		Task searchTask= null;
		
		searchTask = convertJSONToTask((JSONObject)jsonTaskList.get(taskID));
		
		return searchTask;
	}
	
	
	
	public ArrayList<Task> search(String keyword) throws IOException{
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
	public ArrayList<Task> search(Calendar date1, Calendar date2) throws IOException{
		readFileOnce();
		ArrayList<Task> searchTaskList = new ArrayList<Task> ();
		Task temp;
		for(int i=0;i<jsonTaskList.length();i++){
			
			temp = convertJSONToTask((JSONObject)jsonTaskList.get(i));
			if(convertJSONToTask((JSONObject)jsonTaskList.get(i)).hasDeadline2()){
				if(temp.getDeadline().compareTo(date1)>=0 && temp.getDeadline2().compareTo(date2)<=0){
					searchTaskList.add(temp);
				}
			}else if(convertJSONToTask((JSONObject)jsonTaskList.get(i)).getDeadline()!=null){
				if(temp.getDeadline().compareTo(date1)>=0 && temp.getDeadline().compareTo(date2)<=0){
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
	
	
	
/*	-searchName(String):							ArrayList<Task> taskList(searched task)
 * 	-searchTag(String):								ArrayList<Task> taskList(searched task)
 * 	-searchSubtask(String):							ArrayList<Task> taskList(searched task)
*/
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
	
	
/*
 * +displayAll(): ArrayList<Task>	
 */
	public ArrayList <Task> displayAll() throws IOException{
		readFileOnce();
		ArrayList <Task> displayTaskList = new ArrayList <Task>();
		for(int i =0; i <jsonTaskList.length(); i++){
			displayTaskList.add(convertJSONToTask((JSONObject)jsonTaskList.get(i)));
		}
		
		return displayTaskList;
	}
	
	
	
/*	+delete(): ArrayList<Task> taskList(deleted)
		delete(ArrayList<Task>):					ArrayList<Task> taskList(deleted)
		delete(Task):								Task(original)
*/
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
/*	
	+update(): ArrayList<Task> taskList(updated)
		update(Task):								Task(original)
		update(ArrayList<Task>):					ArrayList<Task> taskList(original)
		
*/		
	public Task update(int taskID, Task newTask) throws IOException{
		readFileOnce();
		Task originalTask = search(taskID);
		update(originalTask, newTask);
		return originalTask;
	}
	
	public Task update(Task originalTask, Task newTask) throws IOException{
		readFileOnce();
		delete(originalTask);
		add(newTask);
		return originalTask;
	}
	public ArrayList<Task> update(ArrayList<Task> originalTaskList,ArrayList<Task> newTaskList) throws IOException{
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
		if(tempTask.getDeadline()!=null){
			JSONTempTask.put(MESSAGE_DEADLINE, tempTask.getDeadline());
		}
		if(tempTask.hasDeadline2()){
			JSONTempTask.put(MESSAGE_DEADLINE2, tempTask.getDeadline2());
		}
		if(tempTask.getTags()!=null){
			JSONTempTask.put(MESSAGE_TAGS, tempTask.getTags());
		}
		if(tempTask.getSubtask()!=null){
			JSONTempTask.put(MESSAGE_SUBTASKS, tempTask.getSubtask());
		}
		return JSONTempTask;
	}
	private static Task convertJSONToTask(JSONObject JSONTempTask){
		Task tempTask = new Task(JSONTempTask.getString(MESSAGE_TASKNAME));
		if(JSONTempTask.has(MESSAGE_DEADLINE)){
			tempTask.setDeadline((Calendar)JSONTempTask.get(MESSAGE_DEADLINE));
		}
		if(JSONTempTask.has(MESSAGE_DEADLINE2)){
			tempTask.setDeadline2((Calendar)JSONTempTask.get(MESSAGE_DEADLINE2));
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
			//if(tempTask.getDeadline().equals((Calendar)jsonTask.get(MESSAGE_DEADLINE))){
				result = true;
			//}
		}
		return result;
	}

	
	
	
	
	
	
	public void setFile(String newFileName) throws IOException{
		filename = newFileName;
	}
	public File createFile() throws IOException{
		file = new File(filename);
		if(!file.exists()){
			file.createNewFile();
		}
		return file;
	}
	public String getFileName(){
		return file.getName();
	}
	
	
}