package ui;

import static ext.jansi.Ansi.ansi;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import logger.ZombieLogger;
import ext.jansi.Ansi;
import storage.StorageAPI;
import task.Task;
import zombietask.ZombieTask;

public class TaskPrinter {
	
	public static final Ansi.Color COLOR_BLACK = Ansi.Color.BLACK;
	public static final Ansi.Color COLOR_RED = Ansi.Color.RED;
	public static final Ansi.Color COLOR_GREEN = Ansi.Color.GREEN;
	public static final Ansi.Color COLOR_YELLOW = Ansi.Color.YELLOW;
	public static final Ansi.Color COLOR_BLUE = Ansi.Color.BLUE;
	public static final Ansi.Color COLOR_MAGENTA = Ansi.Color.MAGENTA;
	public static final Ansi.Color COLOR_CYAN = Ansi.Color.CYAN;
	public static final Ansi.Color COLOR_WHITE = Ansi.Color.WHITE;
	
	private static final Format FORMAT_DATETIME = new SimpleDateFormat("dd/MM/yy HH:mm");
	public static final int VERBOSITY_DECREMENT = 2;
	public static final int TAB_INCREMENT = 1;
	
	public static final String FORMAT_NAME_DATE = "%s%s[%s] %s%s%s%s\n"; // Tabs | Color | TaskID | Color | TaskName | Date | Color_Reset
	public static final String FORMAT_TAGS = "%s%sTags: [%s]%s\n"; // Tabs | Color | Tags | Color_Reset
	public static final String TAG_SEPARATOR = "; ";
	public static final String HEADER_LINE_DOUBLE = "\n\n";
	public static final String HEADER_LINE_SINGLE = "\n";
	
	private static StorageAPI storage = ZombieTask.getStorage();
	private static Logger logger = ZombieLogger.getLogger();
	
	public TaskPrinter()	{}
	/**
	 * Returns a pretty printing version of Task for printing.
	 * 
	 * Verbosity increases the amount of information printed out.
	 * 	0 for name line only
	 * 	1 for name + tags + time only
	 *  2 for name + tags + time + Subtasks
	 * 
	 * Tab is used for subTask pretty printing.
	 * 
	 * @param task
	 * @param verbosity integer
	 * @param tab integer
	 * @return String
	 */
	
	// Main Method
	public static String printTask(Task task, int verbosity, int tabs) {
		String response = "";
		
		// Exit Condition for recursive call
		if (task == null || verbosity < 0)	{	return response;	}
		logger.log(Level.FINE, task.toString());
		
		switch(verbosity) {
		case 0:
			response += printColoredName(task, tabs);
			return response;
		case 1:
			response += printColoredName(task, tabs);
			response += printColoredTags(task);
			response = response.concat(HEADER_LINE_SINGLE);
			response += printDatesOfTask(task, tabs);
			return response;
		case 2:
			response += printColoredName(task, tabs);
			response += printColoredTags(task);
			response = response.concat(HEADER_LINE_SINGLE);
			response += printDatesOfTask(task, tabs);
			response += printColoredSubTasks(task, verbosity, tabs);	
			return response;
		default:	return ".....";
		}
	}
	// Accessory Methods
	public static String printColoredName(Task task, int tabs) {
		return ansi().a(generateTabs(tabs)).fg(COLOR_WHITE).a("[").a(storage.indexOf(task)).a("] ")
				.fg(getTaskColor(task)).a(task.getTaskName()).reset().toString();
	}

	public static String printColoredTags(Task task) {
		String tags = " ";
		for(String tag : task.getTags()) {
			tags += tag;
			if (task.getTags().indexOf(tag) != task.getTags().size() - 1)	tags += TAG_SEPARATOR;
		}
		return ansi().fg(COLOR_WHITE).a(tags).reset().toString();
	}
	
	public static String printDatesOfTask(Task task, int tabs) {
		String response = "";
		if(task.isDeadlineTask())
			response += generateTabs(tabs + TAB_INCREMENT).concat(printColoredDate(task, 'E'));
		
		if(task.isTimedTask()) {
			response += generateTabs(tabs + TAB_INCREMENT).concat(printColoredDate(task, 'S'));
			response = response.concat(HEADER_LINE_SINGLE);
			response += generateTabs(tabs + TAB_INCREMENT).concat(printColoredDate(task, 'E'));
		}
		return response.concat(HEADER_LINE_SINGLE);
	}
	public static String printColoredSubTasks(Task task, int verbosity, int tabs) {
		String response = "";
		for (Task subtask : task.getSubtask()) {
			response =  response.concat(HEADER_LINE_SINGLE).concat(
				printTask(subtask, verbosity - VERBOSITY_DECREMENT, tabs + TAB_INCREMENT));
		}
		return response;
	}

	private static String generateTabs(int tabs) {
		return(tabs > 0) ? "\t" + generateTabs(tabs - 1) : "";
	}

	public static String printColoredDate(Task task, char dateType) {
		switch(dateType) {
		case 'S':	return ansi().fg(COLOR_CYAN).a("Start: " + FORMAT_DATETIME.format(task.getStartTime().getTime())).reset().toString();
		case 'E':	return ansi().fg(COLOR_CYAN).a("End: " + FORMAT_DATETIME.format(task.getEndTime().getTime())).reset().toString();
		default:	return "";
		}
	}

	private static Ansi.Color getTaskColor(Task task){
		if (task.isOverdue())		{	return COLOR_RED;	}
		if (task.isFloatingTask())	{	return COLOR_GREEN;	}
		if (task.isDeadlineTask())	{	return COLOR_YELLOW;	}
		if (task.isFloatingTask())	{	return COLOR_BLUE;	}
		return COLOR_GREEN;
	}
}