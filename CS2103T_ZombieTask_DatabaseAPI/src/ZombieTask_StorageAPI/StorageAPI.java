package ZombieTask_StorageAPI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


public class StorageAPI {
	private static File file;
	private static String MESSAGE_FILENAME ="ZombieStorage";
	private BufferedReader br;
	private BufferedWriter bw;
	ArrayList<Task> taskList;


/*	+add(): Task(newTask)
		add(Task): 									Task(newTask)
		add(ArrayList<Task>):						ArrayList<Task> taskList(newTask)
*/
	public Task add(Task newTask){
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
	
	
	
	private static void createFile() throws IOException{
		file = new File(MESSAGE_FILENAME);
		if(!file.exists()){
			file.createNewFile();
		}
	}
	
	
	
}
