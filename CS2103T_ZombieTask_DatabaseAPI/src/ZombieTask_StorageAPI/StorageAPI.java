package ZombieTask_StorageAPI;

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
	
	private static String MESSAGE_FILENAME ="ZombieStorage";
	private static File file = new File(MESSAGE_FILENAME);
	private static BufferedReader br;
	private static BufferedWriter bw;
	private static JSONObject jsonTaskList =null ;
	private static String newTaskList,lastStep,tempTaskList;
	ArrayList<Task> taskList;
	
	


/*	+add(): Task(newTask)
		add(Task): 									Task(newTask)
		add(ArrayList<Task>):						ArrayList<Task> taskList(newTask)
*/
	public Task add(Task newTask) throws IOException{
		readFile();
		
		writeFile();
		return newTask;
	}
	public ArrayList<Task> add(ArrayList<Task> taskList){
		return taskList;
	}
	
/*	+search(): ArrayList<Task> taskList(searched task)
		search(String keyword): 					ArrayList<Task> taskList(searched task)
		search(Calendar date, dates): 				ArrayList<Task> taskList(searched task)
		search(Calendar date):						ArrayList<Task> taskList(searched task)
*/
	public ArrayList<Task> search(String keyword){
		return taskList;
	}
	public ArrayList<Task> search(Calendar date1, Calendar date2){
		return taskList;
	}
	public ArrayList<Task> search(Calendar date){
		return taskList;
	}
	
	
	
/*	+searchTag(String):								ArrayList<Task> taskList(searched task)
*/
	public ArrayList<Task> searchTag(String tagInfo){
		return taskList;
	}
	
	
/*	+delete(): ArrayList<Task> taskList(deleted)
		delete(ArrayList<Task>):					ArrayList<Task> taskList(deleted)
		delete(Task):								Task(original)
*/
	public Task delete(Task tempTask){
		return tempTask;
	}
	public ArrayList<Task> delete(ArrayList<Task> taskList){
		return taskList;
	}
/*	
	+update(): ArrayList<Task> taskList(updated)
		update(Task):								Task(original)
		update(ArrayList<Task>):					ArrayList<Task> taskList(original)
*/		
	public Task update(Task newTask){
		Task originalTask=null;
		return originalTask;
	}
	public ArrayList<Task> update(ArrayList<Task> taskList){
		return taskList;
	}
	
	//opens file to write content into memory
	private static void writeFile() throws IOException {
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(MESSAGE_FILENAME), "utf-8"));
		bw.write(jsonTaskList.toString());
		bw.flush();
		bw.close();
	}
	
	private static void readFile() throws IOException{
		br = new BufferedReader(new FileReader(file));
		tempTaskList = br.readLine();
		if (tempTaskList != null) {
			jsonTaskList = new JSONObject(tempTaskList);
		}else{
			jsonTaskList = new JSONObject();
		}
		br.close();
	}
	
	private static JSONObject convertTaskToJSON(Task tempTask){
		JSONObject JSONTempTask = new JSONObject();
		
		return JSONTempTask;
	}
	private static Task convertJSONToTask(JSONObject JSONTempTask){
		
		
		Task tempTask = new Task("");
		
		return tempTask;
	}
	
	
	
	
	
	
	
	
	
	public static void createFile() throws IOException{
		file = new File(MESSAGE_FILENAME);
		if(!file.exists()){
			file.createNewFile();
		}
	}
	
	
	
}
