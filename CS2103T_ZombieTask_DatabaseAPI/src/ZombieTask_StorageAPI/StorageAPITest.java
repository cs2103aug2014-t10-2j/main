package ZombieTask_StorageAPI;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class StorageAPITest {
	
	String filename = "ZombieStorage.txt";
	String filenameTest = "Text.txt";
	File file = null;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testAddTask() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearchIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteTask() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateTaskTask() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetFile() throws IOException {
		StorageAPI.setFile(filenameTest);
		file = StorageAPI.createFile();
		assertEquals(file.getName(),filenameTest);
		//fail("Not yet implemented");
	}

	@Test
	public void testCreateFile() throws IOException {
		file = StorageAPI.createFile();
		assertEquals(file.getName(),filename);
		//fail("Not yet implemented");
	}

}
