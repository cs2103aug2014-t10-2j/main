package tests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
/**
//@author a0108553h
 */
public class UIAtd {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	
	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
	}
	@After
	public void cleanUpStreams() {
		System.setOut(null);
	}
	/*
	@Test
	public void testPrintResponse() {
		fail("Not yet implemented");
	}
	@Test
	public void testPrintPerspective() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFormat() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintTask() {
		fail("Not yet implemented");
	}

	@Test
	public void testInitUIOnce() {
		fail("Not yet implemented");
	}

	@Test
	public void testSortTasks() {
		fail("Not yet implemented");
	}

	@Test
	public void testWithinTimePeriod() {
		fail("Not yet implemented");
	}
*/
}
