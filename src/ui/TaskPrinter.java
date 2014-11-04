package ui;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import logger.ZombieLogger;
import storage.StorageAPI;
import task.Task;
import zombietask.ZombieTask;

public class TaskPrinter {
	
	public static final String BLACK = "<font color='BLACK'>";
	public static final String BLUE = "<font color='BLUE'>";
	public static final String CYAN = "<font color='CYAN'>";
	public static final String GREEN = "<font color='GREEN'>";
	public static final String GRAY = "<font color='GRAY'>";
	public static final String MAGENTA = "<font color='MAGENTA'>";
	public static final String ORANGE = "<font color='ORANGE'>";
	public static final String RED = "<font color='RED'>";
	public static final String YELLOW = "<font color='YELLOW'>";
	public static final String DONE = "<font color='GRAY'>{D}";
	
	//private static final Format FORMAT_DATETIME = new SimpleDateFormat("dd/MM/yy HH:mm");
	private static final Format FORMAT_TIMEONLY = new SimpleDateFormat("HH:mm");
	public static final int VERBOSITY_DECREMENT = 2;
	public static final int TAB_INCREMENT = 1;
	
	public static final String FORMAT_NAME_DATE = "%s%s[%s] %s%s%s%s<br>"; // Tabs | Color | TaskID | Color | TaskName | Date | Color_Reset
	public static final String FORMAT_TAGS = "%s%sTags: [%s]%s<br>"; // Tabs | Color | Tags | Color_Reset
	public static final String TAG_SEPARATOR = "; ";
	public static final String HEADER_LINE_DOUBLE = "<br><br>";
	public static final String HEADER_LINE_SINGLE = "<br>";
	
	private static StorageAPI storage = ZombieTask.getStorage();
	private static Logger logger = ZombieLogger.getLogger();
	
	public TaskPrinter()	{}
	/**
	 * Returns a pretty printing version of Task for printing.
	 * 
	 * Verbosity increases the amount of information printed out.
	 * 	0 for name line only
	 * 	1 for name + tags + location + time only
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
			response += printColoredLocation(task);
			response = response.concat(HEADER_LINE_SINGLE);
			response += printDatesOfTask(task, tabs);
			return response;
		default:	return ".....";
		}
	}
	
	/**
	 * Returns a pretty printing version of Task for printing.
	 * 
	 * Verbosity increases the amount of information printed out.
	 * 	0 for name line only
	 * 	1 for name + tags + location + time only
	 * 
	 * Tab is used for subTask pretty printing.
	 * 
	 * @param task
	 * @param verbosity integer
	 * @param tab integer
	 * @param int 0 for start time, 1 for end time
	 * @return String
	 */
	
	public static String printTaskSingleLine(Task task, int verbosity, int tabs, int time) {
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
		default:	return ".....";
		}
	}
	
	// Accessory Methods
	public static String printColoredName(Task task, int tabs) {
		return generateTabs(tabs) + GRAY + "[" + storage.indexOf(task) + "] " + getTaskColor(task) + task.getTaskName();
	}

	public static String printColoredTags(Task task) {
		String tags = " ";
		for(String tag : task.getTags()) {
			tags += tag;
			if (task.getTags().indexOf(tag) != task.getTags().size() - 1)	tags += TAG_SEPARATOR;
		}
		return GRAY + tags;
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
	
	public static String printColoredLocation(Task task){
		String response = " ";
		if (task.getLocation() != null){
			response = response.concat(MAGENTA).concat(task.getLocation());
		}
		return response;
	}

	private static String generateTabs(int tabs) {
		return(tabs > 0) ? "\t" + generateTabs(tabs - 1) : "";
	}

	public static String printColoredDate(Task task, char dateType) {
		switch(dateType) {
		case 'S':	return CYAN + "Start: " + FORMAT_TIMEONLY.format(task.getStartTime().getTime());
		case 'E':	return CYAN + "End: " + FORMAT_TIMEONLY.format(task.getEndTime().getTime());
		default:	return "";
		}
	}

	private static String getTaskColor(Task task){
		if (task.isCompleted())		{	return DONE;	}
		if (task.isOverdue())		{	return RED;	}
		if (task.isFloatingTask())	{	return GREEN;	}
		if (task.isDeadlineTask())	{	return YELLOW;	}
		if (task.isFloatingTask())	{	return BLUE;	}
		return ORANGE;
	}
}