package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import storage.StorageAPI;
import task.Task;
import task.TaskUIFormat;

public class StorageAPITest {
	
	StorageAPI testStorage = new StorageAPI();
	String filename = "ZombieStorage.txt";
	static String filenameTest = "Text.txt";
	File file = null;
	
	String expected = null;
	String result = null;
	String taskName = "tempTask";
	String updateName = "updateName";
	
	int  testID = 0;
	
	Calendar deadline; 
	Calendar date1 = new GregorianCalendar(2000,1,1) ;
	Calendar date2 = new GregorianCalendar(2000,1,2) ;
	
	
	Task task1 = null;
	Task task2 = null; 
	Task task3 = null; 
	Task task4 = null; 
	Task deletedTask;
	/**
	 *  clear the file and set 4 Tasks which will be used later
	 * @throws Exception
	 */
	@Before
	
	public void setUp() throws Exception {
		StorageAPI.createFile();
		file = testStorage.getFile();
		
	    	FileWriter fw = new FileWriter(file,false);
			fw.write("");
			fw.flush();
			fw.close();
			task1 = new Task(taskName, date1);
			task2 = new Task(updateName, date2);
			task3 = new Task(updateName, deadline);
			task4 = new Task(taskName);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		/*
		 * Delete all used files
		 */
		
		ZombieTaskAtd.deleteFile(filenameTest);
	}
	
	/**
	 * testing for addTask(Task)
	 * @throws IOException
	 */
	@Test
	
	public void testAddTask() throws IOException {
		
		testStorage.add(task4);
		BufferedReader br = new BufferedReader(new FileReader(file));
		result = br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine();
		br.close();
		expected = "{  \"taskList\": [    {      \"taskName\": \"tempTask\",      \"endTime\": null,      \"startTime\": null,      \"tags\": [],      \"subtasks\": []    }  ]}";
		testStorage.delete(task4);
		assertEquals("problems:",expected, result);
		
		//fail("Not yet implemented");
	}

	/**
	 * testing for deleteTask(Task)
	 * @throws IOException
	 */
	@Test
	
	public void testDeleteTask() throws IOException {
		testStorage.add(deletedTask);
		testStorage.delete(deletedTask);
		BufferedReader br = new BufferedReader(new FileReader(file));
		result = br.readLine()+ br.readLine()+ br.readLine();
		br.close();
		expected = "{  \"taskList\": []}";
		assertEquals("problems:",expected, result);
		//fail("Not yet implemented");
	}
	
	/**
	 * testing for getAllTasks()
	 * @throws Exception
	 */
	@Test
	public void testDisplayAll() throws Exception{
		
		testStorage.add(task4);
		testStorage.add(task3);
		ArrayList <Task> expectedTest = new ArrayList <Task> ();
		ArrayList<Task> floatingTasks = testStorage.getAllTasks().getFloatingTasks();
		ArrayList<Task> deadlineTasks = testStorage.getAllTasks().getDeadlineTasks();
		
		expectedTest.add(task4);
		expectedTest.add(task3);
		
		assertEquals(expectedTest.get(0).getSubtask(), floatingTasks.get(0).getSubtask());
		assertEquals(expectedTest.get(0).getEndTime(), floatingTasks.get(0).getEndTime());
		assertEquals(expectedTest.get(0).getTags(), floatingTasks.get(0).getTags());
		assertEquals(expectedTest.get(0).getTaskName(), floatingTasks.get(0).getTaskName());
		assertEquals(expectedTest.get(1).getSubtask(), deadlineTasks.get(0).getSubtask());
		assertEquals(expectedTest.get(1).getEndTime(), deadlineTasks.get(0).getEndTime());
		assertEquals(expectedTest.get(1).getTags(), deadlineTasks.get(0).getTags());
		assertEquals(expectedTest.get(1).getTaskName(), deadlineTasks.get(0).getTaskName());
		testStorage.delete(task4);
		testStorage.delete(task3);
	}
	
	/**
	 * testing for search(int)
	 * @throws Exception
	 */
	@Test
	
	public void testSearchIntArray() throws Exception {
		testStorage.add(task4);
		Task testResult = testStorage.search("f"+testID);
		Task testExpected = task4;
		
		assertEquals(testExpected.getSubtask(),testResult.getSubtask());
		assertEquals(testExpected.getEndTime(),testResult.getEndTime());
		assertEquals(testExpected.getTags(),testResult.getTags());
		assertEquals(testExpected.getTaskName(),testResult.getTaskName());

		//fail("Not yet implemented");
	}
	
	/**
	 * testing for searchTwoDate(Calendar,Calendar)
	 * @throws Exception
	 */
	@Test
	
	public void testSearchTwoDate() throws Exception {
		
		testStorage.add(task1);
		testStorage.add(task2);
		TaskUIFormat tempTest = testStorage.search(date1, date2);
		ArrayList<Task> deadlineTasks = testStorage.getAllTasks().getDeadlineTasks();
		ArrayList<Task> expectedTest= new ArrayList<Task>();
		expectedTest.add(task1);
		expectedTest.add(task2);
		testStorage.delete(task2);
		testStorage.delete(task1);
		assertEquals(expectedTest.get(0).getSubtask(),deadlineTasks.get(0).getSubtask());
		assertEquals(expectedTest.get(0).getEndTime(),deadlineTasks.get(0).getEndTime());
		assertEquals(expectedTest.get(0).getTags(),deadlineTasks.get(0).getTags());
		assertEquals(expectedTest.get(0).getTaskName(),deadlineTasks.get(0).getTaskName());
		assertEquals(expectedTest.get(1).getSubtask(),deadlineTasks.get(1).getSubtask());
		assertEquals(expectedTest.get(1).getEndTime(),deadlineTasks.get(1).getEndTime());
		assertEquals(expectedTest.get(1).getTags(),deadlineTasks.get(1).getTags());
		assertEquals(expectedTest.get(1).getTaskName(),deadlineTasks.get(1).getTaskName());
	}
	
	/**
	 *  testing for searchName(String)
	 * @throws Exception
	 */
	@Test 
	
	public  void testSearchName() throws Exception {
		testStorage.add(task1);
		testStorage.add(task2);
		TaskUIFormat testResult = testStorage.searchName(taskName);
		Task testTask = testResult.getDeadlineTasks().get(0);
		Task testExpected = task1;
		assertEquals(testExpected.getSubtask(),testTask.getSubtask());
		assertEquals(testExpected.getEndTime(),testTask.getEndTime());
		assertEquals(testExpected.getTags(),testTask.getTags());
		assertEquals(testExpected.getTaskName(),testTask.getTaskName());
		testStorage.delete(task1);
		testStorage.delete(task2);
	}
	
	/**
	 *  testing for searchTag(String)
	 * @throws Exception
	 */
	@Test
	
	public void testSearchTag() throws Exception {
		testStorage.add(task1);
		testStorage.add(task2);
		task1.addTag("justForTest");
		TaskUIFormat testResultList = testStorage.searchTag("justForTest");
		Task testResult = testResultList.getDeadlineTasks().get(0);
		Task testExpected = task1;
		assertEquals(testExpected.getSubtask(),testResult.getSubtask());
		assertEquals(testExpected.getEndTime(),testResult.getEndTime());
		assertEquals(testExpected.getTags(),testResult.getTags());
		assertEquals(testExpected.getTaskName(),testResult.getTaskName());
		testStorage.delete(task1);
		testStorage.delete(task2);
	}

	

	/*
	
	@Test
	public void testtask3Task() throws Exception {
		
		testStorage.update(tempTask, task3);
		BufferedReader br = new BufferedReader(new FileReader(file));
		result = br.readLine();
		br.close();
		expected = "[{\"tags\":[],\"subtasks\":[],\"taskName\":\"updateName\"}]";
		assertEquals("problems:",expected, result);
		//fail("Not yet implemented");
	}
	
	*/

	

	@Test
	public void testSetFile() throws IOException {
		testStorage.setFile(filenameTest);
		StorageAPI.createFile();
		file = testStorage.getFile();
		
		assertEquals(file.getName(),filenameTest);
		//fail("Not yet implemented");
	}

	@Test
	public void testCreateFile() throws IOException {
		StorageAPI.createFile();
		file = testStorage.getFile();
		assertEquals(file.getName(),filename);
		//fail("Not yet implemented");
	}
	
	
	
	
}
