package ui;
/**
 * UI.java
 * Author: Lian Jie Nicholas
 * First Updated: 26/09/2014
 * Last Updated: 22/10/2014
 * 
 * UPDATE 16/10/2014:
 * 1. Renamed all references to yearly into annual.
 * 2. Method sortTasks to sort tasks by deadline before printing.
 * 2. All times set at 0min are allocated beneath hourly increments rather than before it.
 * 	  thus, deadlines set on 16/10/2014 00:00h are found within 16/10/2014 as opposed to
 * 	  15/10/2014.
 * 3. Removed printCalendar() method.
 * UPDATE 19/10/2014:
 * 1. Refactored hasOverdue() functionality
 * UPDATE 22/10/2014:
 * 1. Adapted to handle prefixes and getEndTime()
 * 2. Implemented ANSI Console color output
 */

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import task.Task;
import task.TaskUIFormat;
import zombietask.ZombieTask;
import ext.jansi.AnsiConsole;

public class UI
{
	/*
	 * Class Constants
	 * 
	 * Color Standardization
	 * 
	 * @Names
	 * 
	 * Red - Overdue
	 * Blue - Floating Tasks
	 * Yellow - Deadline Tasks
	 * Green - Timed Tasks
	 * 
	 * @Tags
	 * 
	 * White - all
	 * 
	 * @SubTasks
	 * 
	 * Purple - all
	 */
	
	public static final String HEADER_AGENDA = "(Agenda)<br><br>";
	public static final String HEADER_DAILY = "(Daily)<br><br>";
	public static final String HEADER_WEEKLY = "(Weekly)<br><br>";
	public static final String HEADER_MONTHLY = "(Monthly)<br><br>";
	public static final String HEADER_ANNUAL = "(Annual)<br><br>";
	public static final String HEADER_TODAY = "Current Time: ";
	public static final String HEADER_OVERDUE = "Overdue Tasks\n";
	public static final String HEADER_FLOATING_TASKS = "Floating Tasks\n";
	public static final String HEADER_SCHEDULED_TASKS = "Scheduled Tasks\n";
	public static final String HEADER_LINE_DOUBLE = "\n\n";
	public static final String HEADER_LINE_SINGLE = "\n";
	
	public static final String FORMAT_NAME_DATE = "%s%s[%s] %s%s%s%s\n"; // Tabs | Color | TaskID | Color | TaskName | Date | Color_Reset
	public static final String FORMAT_TAGS = "%s%sTags: [%s]%s\n"; // Tabs | Color | Tags | Color_Reset
	public static final String TAG_SEPARATOR = "; ";
	public static final String BORDER = "----------------------------------------";
	
	public static final String LABEL_FORMAT = "<html><font>%s</font></html>";
	
	public static final int VERBOSITY_NAME_ONLY = 0;
	public static final int VERBOSITY_TAG_INCLUSIVE = 1;
	public static final int VERBOSITY_SUBTASK_INCLUSIVE = 2;
	public static final int VERBOSITY_DECREMENT = 2;
	public static final int TAB_INCREMENT = 1;
	
	public static final FORMAT AGENDA = FORMAT.AGENDA;
	public static final FORMAT DAILY = FORMAT.DAILY;
	public static final FORMAT WEEKLY = FORMAT.WEEKLY;
	public static final FORMAT MONTHLY = FORMAT.MONTHLY;
	public static final FORMAT ANNUAL = FORMAT.ANNUAL;
	public static final FORMAT INVALID = FORMAT.INVALID;
	
	private static final Format FORMAT_DATETIME = new SimpleDateFormat("dd/MM/yy HH:mm");
	//private static final Format FORMAT_DATEONLY = new SimpleDateFormat("dd/MM/yy");
	//private static final Format FORMAT_DAYDATE = new SimpleDateFormat("EEEEE, dd MMMMM yyyy");
	private static final Format FORMAT_TODAY = new SimpleDateFormat("dd MMMMM yyyy, HH:mm");
	//private static final Format FORMAT_WEEKNUM = new SimpleDateFormat("ww");
	
	private static final ArrayList<Task> EMPTY_LIST = new ArrayList<Task>();
	
	/*
	 * Class variables
	 */
	
	//private static Logger logger = ZombieLogger.getLogger();
	private static boolean uiInit = false;
	private static GUI window = null;
	
	/*
	 *  PRIMARY METHODS
	 */
	
	public static void printResponse(String response) {
		window.modifyUpperLabel(String.format(LABEL_FORMAT, response));
	}
	
	public static void printPerspective(FORMAT format, TaskUIFormat tasks)
			throws Exception {
			if(tasks.isEmpty()) {
				window.modifyUpperLabel("No tasks within time period." + format);
				return;
			}
			String str;
			switch(format) {
				case AGENDA:	str = printAgenda(tasks);	break;
				case DAILY:		str = printDaily(tasks);	break;
				case WEEKLY:	str = printWeekly(tasks);	break;
				/*
				 * Temporary
				 * 
				case MONTHLY:	str = printMonthly(tasks);	break;
				case ANNUAL:	str = printAnnual(tasks);	break;
				*/
				default:	return;
			}
			window.modifyLabelText(String.format(LABEL_FORMAT, str));
		}
	
	/*
	 * FORMAT METHODS
	 */
	
	/**
	 * Prints an agenda, a list of tasks according to type of task.
	 * First up, all overdue tasks.
	 * Secondly, all tasks, sorted chronologically.
	 * 
	 * @param tasks
	 * @return String to be printed
	 * @throws Exception
	 */
	
	private static String printAgenda(TaskUIFormat tasks) throws Exception {
		
		String str = new String(HEADER_AGENDA);
		/*
		 * Add overdue Tasks
		 */
		TaskUIFormat overdueTasks = getOverdueTasks(tasks);
		if (!overdueTasks.isEmpty()){
			str = str.concat(HEADER_OVERDUE);
			for (Task task : overdueTasks.getDeadlineTasks()){
				str += TaskPrinter.printTask(task, 1, 0);
			}
			for (Task task : overdueTasks.getTimedTasks()){
				str += TaskPrinter.printTask(task, 1, 0);
			}
			str = str.concat(HEADER_LINE_SINGLE);
		}
		/*
		 * Add floating Tasks
		 */
		if (!tasks.getFloatingTasks().isEmpty()){
			str = str.concat(HEADER_FLOATING_TASKS);
			for (Task task : tasks.getFloatingTasks()){
				str += TaskPrinter.printTask(task, 1, 0);
			}
			str = str.concat(HEADER_LINE_SINGLE);
		}
		/*
		 * Add non-floating Tasks
		 */
		if (!tasks.getScheduledTasks().isEmpty()){
			str = str.concat(HEADER_SCHEDULED_TASKS);
			for (Task task : tasks.getScheduledTasks())
				if(!task.isOverdue())	str += TaskPrinter.printTask(task, 1, 0);
		}
		assert (str != null);
		return str + BORDER;
	}
	
	private static String printDaily(TaskUIFormat tasks) throws Exception {
		String str = new String(HEADER_DAILY);
		TaskUIFormat processedTasks = new TaskUIFormat(null, processTasks(tasks), null);
		for(Task task : getOverdueTasks(processedTasks).getDeadlineTasks())
			str += task.getTaskName() + HEADER_LINE_SINGLE;
		str += "Today, " + FORMAT_TODAY.format(Calendar.getInstance().getTime()) + HEADER_LINE_SINGLE;
		Calendar start = delimitTime(Calendar.getInstance());
		Calendar end = delimitTime(Calendar.getInstance());
		end.add(Calendar.HOUR_OF_DAY, 1);
		System.out.println(FORMAT_DATETIME.format(start.getTime()));
		for(int i = 0; i < 24; i++) {
			str += "|- " + FORMAT_DATETIME.format(start.getTime()) + " : ";
			for(Task task : processedTasks.getDeadlineTasks()) {
				if(!task.isOverdue() && withinTimePeriod(task.getEndTime(), start, end))
					str += task.getTaskName();
			}
			start.add(Calendar.HOUR_OF_DAY, 1);
			end.add(Calendar.HOUR_OF_DAY, 1);
			str += HEADER_LINE_SINGLE;
		}
		return str + BORDER;
	}
	private static String printWeekly(TaskUIFormat tasks) throws Exception {
		String str = new String(HEADER_WEEKLY);
		TaskUIFormat processedTasks = new TaskUIFormat(null, processTasks(tasks), null);
		for(Task task : getOverdueTasks(processedTasks).getDeadlineTasks())
			str += task.getTaskName() + HEADER_LINE_SINGLE;
		str += "Today, " + FORMAT_TODAY.format(Calendar.getInstance().getTime()) + HEADER_LINE_SINGLE;
		Calendar start = delimitTime(Calendar.getInstance());
		Calendar end = delimitTime(Calendar.getInstance());
		end.add(Calendar.HOUR_OF_DAY, 6);
		System.out.println(FORMAT_DATETIME.format(start.getTime()));
		for(int i = 0; i < 28; i++) {
			str += "|- " + FORMAT_DATETIME.format(start.getTime()) + " : ";
			for(Task task : processedTasks.getDeadlineTasks()) {
				if(!task.isOverdue() && withinTimePeriod(task.getEndTime(), start, end))
					str += task.getTaskName();
			}
			start.add(Calendar.HOUR_OF_DAY, 6);
			end.add(Calendar.HOUR_OF_DAY, 6);
			str += HEADER_LINE_SINGLE;
		}
		return str + BORDER;
	}
	
	public static FORMAT getFormat(String formatString)
	{
		switch(formatString.toLowerCase())
		{
			case "agenda":		return FORMAT.AGENDA;
			case "daily":		return FORMAT.DAILY;
			case "weekly":		return FORMAT.WEEKLY;
			case "monthly":		return FORMAT.MONTHLY;
			case "annual":		return FORMAT.ANNUAL;
			case "calendar":	return FORMAT.CALENDAR;
			default:			return FORMAT.INVALID;
		}
	}
	
	private static TaskUIFormat getOverdueTasks(TaskUIFormat tasks){
		ArrayList<Task> searchDeadlineList = new ArrayList<Task>();
		ArrayList<Task> searchTimedList = new ArrayList<Task>();
		for (Task task : tasks.getDeadlineTasks()){
			if (task.isOverdue())	searchDeadlineList.add(task);
		}
		for (Task task : tasks.getTimedTasks()){
			if (task.isOverdue())	searchTimedList.add(task);
		}
		return new TaskUIFormat(EMPTY_LIST, searchDeadlineList, searchTimedList);
	}
	
	/*
	private static String printMonthly(ArrayList<Task> tasks) throws Exception {
		String str;
		str = "(Monthly)\n";
		ArrayList<Task> processedTasks = processTasks(tasks);
		for(Task task : processedTasks) {
			if(task.isOverdue())	str += task.getTaskName() + "\n";
		}
		str += "Today, " + FORMAT_TODAY.format(Calendar.getInstance().getTime()) + "\n";
		Calendar begin = delimitTime(Calendar.getInstance());
		Calendar end = delimitTime(Calendar.getInstance());
		begin.set(Calendar.HOUR_OF_DAY, 0);
		end.set(Calendar.HOUR_OF_DAY, 0);
		end.add(Calendar.DATE, 1);
		ArrayList<String> daily = new ArrayList<String>();
		for(int i = 0; i < MONTHLY_LIMIT; i++) {
			daily.add(String.format("|- %s:", FORMAT_DATEONLY.format(begin.getTime())));
			for(Task task : processedTasks) {
				if(withinTimePeriod(task.getEndTime(), begin, end))
					daily.set(daily.size() - 1, daily.get(daily.size() - 1) + " " + task.getTaskName());
			}
			begin.add(Calendar.DATE, 1);
			end.add(Calendar.DATE, 1);
		}
		for(String string : daily)	str += string + "\n";
		return str;
	}
	private static String printAnnual(ArrayList<Task> tasks) throws Exception
	{
		String str;
		str = "(Annual)\n";
		ArrayList<Task> processedTasks = processTasks(tasks);
		for(Task task : processedTasks) {
			if(task.isOverdue())	str += task.getTaskName() + "\n";
		}
		str += "Today, " + FORMAT_TODAY.format(Calendar.getInstance().getTime()) + "\n";
		Calendar begin = delimitTime(Calendar.getInstance());
		Calendar end = delimitTime(Calendar.getInstance());
		begin.set(Calendar.HOUR_OF_DAY, 0);
		end.set(Calendar.HOUR_OF_DAY, 0);
		begin.add(Calendar.DATE, 1 - begin.get(Calendar.DAY_OF_WEEK));
		end.add(Calendar.DATE, 8 - begin.get(Calendar.DAY_OF_WEEK));
		ArrayList<String> daily = new ArrayList<String>();
		for(int i = 0; i < ANNUAL_LIMIT; i++) {
			daily.add(String.format("|- Week %s:", FORMAT_WEEKNUM.format(begin.getTime())));
			for(Task task : processedTasks) {
				if(withinTimePeriod(task.getEndTime(), begin, end))
					daily.set(daily.size() - 1, daily.get(daily.size() - 1) + " " + task.getTaskName());
			}
			begin.add(Calendar.DATE, 7);
			end.add(Calendar.DATE, 7);
		}
		for(String string : daily)	str += string + "\n";
		return str;
	}
	
	*/
	
	/*
	 *  MISCELLENEOUS METHODS
	 */
	
	// Standardizes Calendar.getInstance() by zeroing smaller values
	private static Calendar delimitTime(Calendar time)
	{
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		return time;
	}
	
	public static String printTask(Task task, int verbosity, int tabs) {
		return TaskPrinter.printTask(task, verbosity, tabs);
	}
	
	public static void initUIOnce() {
		if (!uiInit){
			initUI();
			window = ZombieTask.getGUI();
		}
	}
	
	private static void initUI(){
		System.setProperty("jansi.passthrough", "true");
		AnsiConsole.systemInstall();
	}
	
	private static ArrayList<Task> processTasks(TaskUIFormat tasks) throws Exception
	{
		ArrayList<Task> pTasks = new ArrayList<Task>();
		for(Task task : tasks.getScheduledTasks()) {
			if(task.isTimedTask()) {
				pTasks.add(new Task(TaskPrinter.printTask(task, 0, 0) + " " + 
						TaskPrinter.printColoredDate(task, 'S'), task.getStartTime()));
				pTasks.add(new Task(TaskPrinter.printTask(task, 0, 0) + " " + 
						TaskPrinter.printColoredDate(task, 'E'), task.getEndTime()));
			}
			else if(task.isDeadlineTask())
				pTasks.add(new Task(TaskPrinter.printTask(task, 0, 0) + " " + 
						TaskPrinter.printColoredDate(task, 'E'), task.getEndTime()));
		}
		return sortTasks(pTasks);
	}
	
	public static ArrayList<Task> sortTasks(ArrayList<Task> tasks) {
		ArrayList<Task> floatingTasks = new ArrayList<Task>();
		for(int i = 1; i < tasks.size(); i++) {
			if(tasks.get(i).isFloatingTask())	floatingTasks.add(tasks.remove(i));
		}
		for(int j = 1; j < tasks.size(); j++)
		for(int k = 0; k < tasks.size(); k++)
		if(tasks.get(j).getEndTime().before(tasks.get(k).getEndTime()))
			tasks.add(k, tasks.remove(j));
		tasks.addAll(floatingTasks);
		return tasks;
	}
	/*
	 * DEPRECIATED
	 */
	
	/*
	public static String printTask(Task task, int verbosity, int tabs) {
		
		String response = "";
		String tags = " ";
		
		// Exit Condition for recursive call
		if (task == null || verbosity < 0)	{	return response;	}
		logger.log(Level.FINE, task.toString());
		
		// Print task name first
		response = response.concat(ansi().a(generateTabs(tabs)).fg(COLOR_WHITE).a("[")
				.a(storage.indexOf(task)).a("] ").fg(getTaskColor(task)).a(task.getTaskName())
				.reset().toString());
		// End method if verbosity == 0
		if(verbosity == 0)	return response;
		
		//Print tags
		for(String tag : task.getTags()) {
			tags += tag;
			if (task.getTags().indexOf(tag) != task.getTags().size() - 1) {
				tags += TAG_SEPARATOR;
			}
		}
		response = response.concat(ansi().fg(COLOR_WHITE).a(tags).reset().toString());
		response = response.concat(HEADER_LINE_SINGLE);
		
		// Print Dates		
		if(task.isDeadlineTask()) {
			response = response.concat(ansi().fg(COLOR_CYAN).a(generateTabs(tabs + TAB_INCREMENT))
					.a("End: " + FORMAT_DATETIME.format(task.getEndTime().getTime())).reset().toString());
		}
		
		if(task.isTimedTask()) {
			response = response.concat(ansi().fg(COLOR_CYAN).a(generateTabs(tabs + TAB_INCREMENT))
					.a("Start: " + FORMAT_DATETIME.format(task.getStartTime().getTime())).reset().toString());
			response = response.concat(HEADER_LINE_SINGLE);
			response = response.concat(ansi().fg(COLOR_CYAN).a(generateTabs(tabs + TAB_INCREMENT))
					.a("End: " + FORMAT_DATETIME.format(task.getEndTime().getTime())).reset().toString());
		}
		response = response.concat(HEADER_LINE_SINGLE);
		// End method if verbosity == 1
		if(verbosity == 1)	return response;
		
		for (Task subtask : task.getSubtask()) {
			response =  response.concat(HEADER_LINE_SINGLE).concat(
				printTask(subtask, verbosity - VERBOSITY_DECREMENT, tabs + TAB_INCREMENT));
		}	
		assert(response != null);
		return response;
	}
	*/
	public static boolean withinTimePeriod(Calendar time, Calendar begin, Calendar end)
	{
		if(time == null)	return false;
		return (time.compareTo(begin) >= 0 && time.compareTo(end) < 0);
	}
}
