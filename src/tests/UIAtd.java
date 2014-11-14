package tests;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Ignore;
import org.junit.Test;

import task.Task;
import task.TaskUIFormat;
import ui.UI;
/**
//@author a0108553h
 */
public class UIAtd {

	@Test
	public void testGetFormat() {
		assertEquals(UI.AGENDA, UI.getFormat("agenda"));
	}
	
	@Test
	public void testGetOverdueTasks() throws Exception {
		TaskUIFormat input = new TaskUIFormat();
		TaskUIFormat xoutput = new TaskUIFormat();
		Task t1 = new Task("one");
		Task t2 = new Task("two", rollCalendar('h', 1));
		Task t3 = new Task("three", rollCalendar('h', -1));
		Task t4 = new Task("four", rollCalendar('h', -3), rollCalendar('d', -1));
		input.addTask(t1);
		input.addTask(t2);
		input.addTask(t3);
		input.addTask(t4);
		xoutput.addTask(t3);
		xoutput.addTask(t4);
		Method method = UI.class.getDeclaredMethod("getOverdueTasks", TaskUIFormat.class);
		method.setAccessible(true);
		UI ui = new UI();
		TaskUIFormat output = (TaskUIFormat) method.invoke(ui, input);
		assertEquals(xoutput.getDeadlineTasks(), output.getDeadlineTasks());
		assertEquals(xoutput.getTimedTasks(), output.getTimedTasks());
	}
	
	@Test
	public void testDelimitTime() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Method method = UI.class.getDeclaredMethod("delimitTime", Calendar.class);
		method.setAccessible(true);
		UI ui = new UI();
		Calendar c = (Calendar) method.invoke(ui, Calendar.getInstance());
		assertEquals(0, c.get(Calendar.MILLISECOND));
		assertEquals(0, c.get(Calendar.SECOND));
		assertEquals(0, c.get(Calendar.MINUTE));
	}
	
	@Ignore
	public void testSortTasks() throws Exception {
		Task t1 = new Task("one");
		Task t2 = new Task("two", rollCalendar('h', 1));
		Task t3 = new Task("three", rollCalendar('h', -1));
		Task t4 = new Task("four", rollCalendar('h', -3), rollCalendar('d', -1));
		Task t5 = new Task("five");
		Task t6 = new Task("six", rollCalendar('h', 2));
		Task t7 = new Task("seven", rollCalendar('h', -2), rollCalendar('d', -2));
		ArrayList<Task> input = new ArrayList<Task>();
		ArrayList<Task> xoutput = new ArrayList<Task>();
		input.add(t1);
		input.add(t2);
		input.add(t3);
		input.add(t4);
		input.add(t5);
		input.add(t6);
		input.add(t7);
		xoutput.add(t4);
		xoutput.add(t7);
		xoutput.add(t3);
		xoutput.add(t2);
		xoutput.add(t6);
		xoutput.add(t1);
		xoutput.add(t5);
		assertEquals(xoutput, UI.sortTasks(input));
	}
	
	@Test
	public void testWithinTimePeriod() {
		assertTrue(UI.withinTimePeriod(Calendar.getInstance(), rollCalendar('h', -1), rollCalendar('h', 1)));
		assertFalse(UI.withinTimePeriod(Calendar.getInstance(), rollCalendar('h', 1), rollCalendar('h', 2)));
		assertFalse(UI.withinTimePeriod(Calendar.getInstance(), rollCalendar('h', -2), rollCalendar('h', -1)));
		assertFalse(UI.withinTimePeriod(Calendar.getInstance(), rollCalendar('h', 1), rollCalendar('h', -1)));
	}
	
	private Calendar rollCalendar(char field, int amt) {
		Calendar c = Calendar.getInstance();
		switch(field) {
		case 'n' :	c.add(Calendar.MINUTE, amt);
					break;
		case 'h' :	c.add(Calendar.HOUR, amt);
					break;
		case 'd' :	c.add(Calendar.DATE, amt);
					break;
		case 'm' :	c.add(Calendar.MONTH, amt);
					break;
		case 'y' :	c.add(Calendar.YEAR, amt);
					break;
		default :	break;
		}
		return c;
	}
}
