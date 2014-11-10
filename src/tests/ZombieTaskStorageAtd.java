package tests;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;

import zombietask.ZombieTask;

public class ZombieTaskStorageAtd {
	/**
	 * Integration test
//@author A0119480
	 * 
	 */
	File testfile = new File ("ZombieTest");
	String expected = null;
	String result = null;
	
	@Before
	public void deleteFile() {
		new File("ZombieTest").delete();
	}
	
	@Test
	public void testAddFloatingTask() throws Exception {
		ZombieTask.testCommand("add homework");
		BufferedReader br = new BufferedReader(new FileReader(testfile));
		result = br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine();
		br.close();
		expected = "{  \"floatingTasks\": [    {      \"taskName\": \"homework\",      \"endTime\": null,      \"startTime\": null,      \"tags\": [],      \"subtasks\": [],      \"location\": null    }  ],  \"deadlineTasks\": [],  \"timedTasks\": []}";
		assertEquals("problems:",expected, result);
	}
	
	@Test
	public void testAddDeadlineTask() throws Exception {
		ZombieTask.testCommand("add dinner 6pm");
		BufferedReader br = new BufferedReader(new FileReader(testfile));
		result = br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine();
		br.close();
		expected = "{  \"floatingTasks\": [],  \"deadlineTasks\": [    {      \"taskName\": \"dinner\",      \"endTime\": {        \"year\": 2014,        \"month\": 9,        \"dayOfMonth\": 30,        \"hourOfDay\": 18,        \"minute\": 0,        \"second\": 0      },      \"startTime\": null,      \"tags\": [],      \"subtasks\": [],      \"location\": null    }  ],  \"timedTasks\": []}";
		assertEquals("problems:",expected, result);
	}
	
	@Test
	public void testAddTimedTask() throws Exception {
		ZombieTask.testCommand("add dinner 6pm to 7pm");
		BufferedReader br = new BufferedReader(new FileReader(testfile));
		result = br.readLine()+br.readLine()+br.readLine()+br.readLine()+br.readLine()+br.readLine()+br.readLine()+br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine();
		br.close();
		expected = "{  \"floatingTasks\": [],  \"deadlineTasks\": [],  \"timedTasks\": [    {      \"taskName\": \"dinner\",      \"endTime\": {        \"year\": 2014,        \"month\": 9,        \"dayOfMonth\": 30,        \"hourOfDay\": 19,        \"minute\": 0,        \"second\": 0      },      \"startTime\": {        \"year\": 2014,        \"month\": 9,        \"dayOfMonth\": 30,        \"hourOfDay\": 18,        \"minute\": 0,        \"second\": 0      },      \"tags\": [],      \"subtasks\": [],      \"location\": null    }  ]}";
		assertEquals("problems:",expected, result);
	}
	
	@Test
	public void testDelete() throws Exception {
		ZombieTask.testCommand("add homework");
		ZombieTask.testCommand("delete f0");
		BufferedReader br = new BufferedReader(new FileReader(testfile));
		result = br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine();
		br.close();
		expected = "{  \"floatingTasks\": [],  \"deadlineTasks\": [],  \"timedTasks\": []}";
		assertEquals("problems:",expected, result);
	}
	
	@Test
	public void testUpdate() throws Exception {
		ZombieTask.testCommand("add homework");
		ZombieTask.testCommand("update f0 dinner 9pm");
		BufferedReader br = new BufferedReader(new FileReader(testfile));
		result = br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine();
		br.close();
		expected = "{  \"floatingTasks\": [    {      \"taskName\": \"dinner\",      \"endTime\": null,      \"startTime\": null,      \"tags\": [],      \"subtasks\": [],      \"location\": null    }  ],  \"deadlineTasks\": [],  \"timedTasks\": []}";
		assertEquals("problems:",expected, result);
	}
	
	@Test
	public void testUdoe() throws Exception {
		ZombieTask.testCommand("add homework");
		ZombieTask.testCommand("update f0 dinner 9pm");
		ZombieTask.testCommand("undo");
		BufferedReader br = new BufferedReader(new FileReader(testfile));
		result = br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine()+ br.readLine();
		br.close();
		expected = "{  \"floatingTasks\": [    {      \"taskName\": \"dinner\",      \"endTime\": null,      \"startTime\": null,      \"tags\": [],      \"subtasks\": [],      \"location\": null    },    {      \"taskName\": \"homework\",      \"endTime\": null,      \"startTime\": null,      \"tags\": [],      \"subtasks\": [],      \"location\": null    }  ],  \"deadlineTasks\": [],  \"timedTasks\": []}";
		assertEquals("problems:",expected, result);
	}
}
	
	
