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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import ext.jansi.Ansi;
import ext.jansi.AnsiConsole;
import static ext.jansi.Ansi.ansi;
import logger.ZombieLogger;
import storage.Storage;
import storage.StorageAPI;
import task.Task;
import task.TaskUIFormat;
import zombietask.ZombieTask;



public class UI
{
	
	
	public static final Ansi.Color COLOR_BLACK = Ansi.Color.BLACK;
	public static final Ansi.Color COLOR_RED = Ansi.Color.RED;
	public static final Ansi.Color COLOR_GREEN = Ansi.Color.GREEN;
	public static final Ansi.Color COLOR_YELLOW = Ansi.Color.YELLOW;
	public static final Ansi.Color COLOR_BLUE = Ansi.Color.BLUE;
	public static final Ansi.Color COLOR_MAGENTA = Ansi.Color.MAGENTA;
	public static final Ansi.Color COLOR_CYAN = Ansi.Color.CYAN;
	public static final Ansi.Color COLOR_WHITE = Ansi.Color.WHITE;
	
	public static final String HEADER_AGENDA = "(Agenda)\n\n";
	public static final String HEADER_DAILY = "(Daily)\n";
	public static final String HEADER_WEEKLY = "(Weekly)\n";
	public static final String HEADER_MONTHLY = "(Monthly)\n";
	public static final String HEADER_ANNUAL = "(Annual)\n";
	public static final String HEADER_TODAY = "Current Time: ";
	public static final String HEADER_OVERDUE = "Overdue Tasks\n";
	public static final String HEADER_FLOATING_TASKS = "Floating Tasks\n";
	public static final String HEADER_SCHEDULED_TASKS = "Scheduled Tasks\n";
	public static final String HEADER_LINE_DOUBLE = "\n\n";
	public static final String HEADER_LINE_SINGLE = "\n";
	
	public static final String FORMAT_NAME_DATE = "%s%s[%s] %s%s%s%s\n"; // Tabs | Color | TaskID | Color | TaskName | Date | Color_Reset
	public static final String FORMAT_TAGS = "%s%sTags: [%s]%s\n"; // Tabs | Color | Tags | Color_Reset
	public static final String TAG_SEPARATOR = "; ";
	
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
	private static final Format FORMAT_DATEONLY = new SimpleDateFormat("dd/MM/yy");
	private static final Format FORMAT_DAYDATE = new SimpleDateFormat("EEEEE, dd MMMMM yyyy");
	private static final Format FORMAT_TODAY = new SimpleDateFormat("dd MMMMM yyyy, HH:mm");
	private static final Format FORMAT_WEEKNUM = new SimpleDateFormat("ww");
	
	private static final ArrayList<Task> EMPTY_LIST = Storage.EMPTY_LIST;
	
	
	/*
	 * Class variables
	 */
	
	private static Logger logger = ZombieLogger.getLogger();
	private static StorageAPI storage = ZombieTask.getStorage();
	private static boolean uiInit = false;
	
	/*
	 *  PRIMARY METHODS
	 */
	
	public static void printResponse(String response) {
		System.out.println(response);
	}
	
	
	/*
	public static void printPerspective(FORMAT format, ArrayList<Task> tasks)
		throws Exception {
		if(tasks.isEmpty()) {
			System.out.println("No tasks within time period." + format);
			return;
		}
		tasks = sortTasks(tasks);
		String str;
		switch(format) {
			case AGENDA:	str = printAgenda(tasks);	break;
			case DAILY:		str = printDaily(tasks);	break;
			case WEEKLY:	str = printWeekly(tasks);	break;
			case MONTHLY:	str = printMonthly(tasks);	break;
			case ANNUAL:	str = printAnnual(tasks);	break;
			default:	return;
		}
		System.out.println(str);
	}
	*/
	
	public static void printPerspective(FORMAT format, TaskUIFormat tasks)
			throws Exception {
			if(tasks.isEmpty()) {
				System.out.println("No tasks within time period." + format);
				return;
			}
			String str;
			switch(format) {
				case AGENDA:	str = printAgenda(tasks);	break;
				/*
				 * Temporary
				 * 
				case DAILY:		str = printDaily(tasks);	break;
				case WEEKLY:	str = printWeekly(tasks);	break;
				case MONTHLY:	str = printMonthly(tasks);	break;
				case ANNUAL:	str = printAnnual(tasks);	break;
				*/
				default:	return;
			}
			System.out.println(str);
		}
	
	/*
	 * FORMAT METHODS
	 */
	
	/**
	 * 
	 * Prints an agenda, a list of tasks according to type of task.
	 * 
	 * First up, all overdue tasks.
	 * 
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
				str += printTask(task, 0, 0);
			}
			for (Task task : overdueTasks.getTimedTasks()){
				str += printTask(task, 0, 0);
			}
			str = str.concat(HEADER_LINE_SINGLE);
		}
		
		/*
		 * Add floating Tasks
		 */
		
		if (!tasks.getFloatingTasks().isEmpty()){
			str = str.concat(HEADER_FLOATING_TASKS);
			for (Task task : tasks.getFloatingTasks()){
				str += printTask(task, 0, 0);
			}
			str = str.concat(HEADER_LINE_SINGLE);
		}
		
		
		/*
		 * Add non-floating Tasks
		 */
		if (!tasks.getFloatingTasks().isEmpty()){
			str = str.concat(HEADER_SCHEDULED_TASKS);
			for (Task task : tasks.getScheduledTasks()){
				str += printTask(task, 0, 0);
			}
		}
		
		assert (str != null);
		return str;
	}
	
	private static String printDaily(TaskUIFormat tasks) throws Exception {
		String str = new String(HEADER_DAILY);
		return str;
	}
	
	/**
	 * Returns a pretty printing version of Task for printing.
	 * 
	 * Verbosity increases the amount of information printed out.
	 * 	0 for name only
	 * 	1 for name + tags
	 *  2 for name + tags + Subtasks
	 * 
	 * Tab is used for subTask pretty printing.
	 * 
	 * @param task
	 * @param verbosity integer
	 * @param tab integer
	 * @return String
	 */
	
	public static String printTask(Task task, int verbosity, int tabs){
		
		String response = "";
		String tags = "";
		
		/*
		 * Exit Condition for recursive call
		 */
		
		if (task == null || verbosity < 0){
			return response;
		}
		
		logger.log(Level.FINE, task.toString());
		
		/*
		 * Print taskname first
		 */
		
		response = response.concat(ansi().a(generateTabs(tabs)).fg(COLOR_WHITE).a("[")
				.a(storage.indexOf(task)).a("] ").fg(getTaskColor(task)).a(task.getTaskName())
				.reset().toString());
		response = response.concat(HEADER_LINE_SINGLE);
		
		/*
		 * Print Dates
		 */
		
		if(task.isDeadlineTask()){
			response = response.concat(ansi().fg(COLOR_BLUE).a(generateTabs(tabs + TAB_INCREMENT))
					.a("End: " + FORMAT_DATETIME.format(task.getEndTime().getTime())).reset().toString());
		}
		
		if(task.isTimedTask()){
			response = response.concat(ansi().fg(COLOR_BLUE).a(generateTabs(tabs + TAB_INCREMENT))
					.a("Start: " + FORMAT_DATETIME.format(task.getStartTime().getTime())).reset().toString());
			response = response.concat(HEADER_LINE_SINGLE);
			response = response.concat(ansi().fg(COLOR_BLUE).a(generateTabs(tabs + TAB_INCREMENT))
					.a("End: " + FORMAT_DATETIME.format(task.getEndTime().getTime())).reset().toString());
		}
		
		response = response.concat(HEADER_LINE_SINGLE);
		
		if (verbosity > 0){
			for(String tag : task.getTags()){
				tags += tag;
				if (task.getTags().indexOf(tag) != task.getTags().size() - 1){
					tags += TAG_SEPARATOR;
				}
			}
			response = response.concat(ansi().a(generateTabs(tabs + TAB_INCREMENT)).fg(COLOR_WHITE).a(tags).reset().toString());
			response = response.concat(HEADER_LINE_SINGLE);
			
			for (Task subtask : task.getSubtask()){
				response =  response.concat(HEADER_LINE_SINGLE).concat(
						printTask(subtask, verbosity - VERBOSITY_DECREMENT, tabs + TAB_INCREMENT));
			}
		}
		
		assert(response != null);
		return response;
	}
	
	/**
	 * Returns the appropriate tab spacing for task display
	 * 
	 * @param tabs number of tabs
	 * @return String representation of tabs
	 */
	
	private static String generateTabs(int tabs){
		String response = "";
		for (int i = 0; i < tabs; i++){
			response = response.concat("\t");
		}
		return response;
	}
	
	private static Ansi.Color getTaskColor(Task task){
		if (task.isOverdue()){
			return COLOR_RED;
		}
		if (task.isFloatingTask()){
			return COLOR_GREEN;
		}
		if (task.isDeadlineTask()){
			return COLOR_YELLOW;
		}
		if (task.isFloatingTask()){
			return COLOR_CYAN;
		}
		return COLOR_GREEN;
	}
	
	private static TaskUIFormat getOverdueTasks(TaskUIFormat tasks){
		ArrayList<Task> searchDeadlineList = new ArrayList<Task>();
		ArrayList<Task> searchTimedList = new ArrayList<Task>();
		for (Task task : tasks.getDeadlineTasks()){
			if (task.isOverdue()){
				searchDeadlineList.add(task);
			}
		}
		for (Task task : tasks.getTimedTasks()){
			if (task.isOverdue()){
				searchTimedList.add(task);
			}
		}
		return new TaskUIFormat(EMPTY_LIST, searchDeadlineList, searchTimedList);
	}
	
	/*
	
	private static String printAgenda(ArrayList<Task> tasks) throws Exception {
		String str;
		str = "(Agenda)\n";
		ArrayList<Task> processedTasks = processTasks(tasks);
		for(Task task : processedTasks) {
			if(task.isOverdue())	str += task.getTaskName() + "\n";
		}
		str += "Today, " + FORMAT_TODAY.format(Calendar.getInstance().getTime()) + "\n";
		Calendar begin = delimitTime(Calendar.getInstance());
		begin.set(Calendar.HOUR_OF_DAY, 0);
		Calendar end = delimitTime(Calendar.getInstance());
		end.set(Calendar.HOUR_OF_DAY, 0);
		end.add(Calendar.DATE, 1);
		ArrayList<String> floatingStrings = new ArrayList<String>();
		for(Task task : processedTasks) {
			if(task.isDeadline() && task.isOverdue() == false) {
				while(withinTimePeriod(task.getEndTime(), begin, end) == false) {
					begin.add(Calendar.DATE, 1);
					end.add(Calendar.DATE, 1);
					str += FORMAT_DAYDATE.format(begin.getTime()) + "\n";
				}
				str += task.getTaskName() + "\n";
			}
			if(task.isFloatingTask())	floatingStrings.add(task.getTaskName());
		}
		if(floatingStrings.isEmpty() == false)
		{
			str += "<<<<< Floating Tasks >>>>>\n";
			for(String string : floatingStrings)	str += string + "\n";
		}
		return str;
	}
	
	private static String printDaily(ArrayList<Task> tasks) throws Exception {
		String str;
		str = "(Daily)\n";
		ArrayList<Task> processedTasks = processTasks(tasks);
		for(Task task : processedTasks) {
			if(task.isOverdue())	str += task.getTaskName() + "\n";
		}
		str += "Today, " + FORMAT_TODAY.format(Calendar.getInstance().getTime()) + "\n";
		Calendar begin = delimitTime(Calendar.getInstance());
		Calendar end = delimitTime(Calendar.getInstance());
		end.add(Calendar.HOUR_OF_DAY, 1);
		ArrayList<String> daily = new ArrayList<String>();
		for(int i = 0; i < DAILY_LIMIT; i++) {
			daily.add(String.format("|- %sh:", FORMAT_DATETIME.format(begin.getTime())));
			for(Task task : processedTasks) {
				if(withinTimePeriod(task.getEndTime(), begin, end))
					daily.set(daily.size() - 1, daily.get(daily.size() - 1) + " " + task.getTaskName());
			}
			begin.add(Calendar.HOUR_OF_DAY, 1);
			end.add(Calendar.HOUR_OF_DAY, 1);
		}
		for(String string : daily)	str += string + "\n";
		return str;
	}
	private static String printWeekly(ArrayList<Task> tasks) throws Exception {
		String str;
		str = "(Weekly)\n";
		ArrayList<Task> processedTasks = processTasks(tasks);
		for(Task task : processedTasks) {
			if(task.isOverdue())	str += task.getTaskName() + "\n";
		}
		str += "Today, " + FORMAT_TODAY.format(Calendar.getInstance().getTime()) + "\n";
		Calendar begin = delimitTime(Calendar.getInstance());
		Calendar end = delimitTime(Calendar.getInstance());
		end.add(Calendar.HOUR_OF_DAY, 6);
		ArrayList<String> daily = new ArrayList<String>();
		for(int i = 0; i < WEEKLY_LIMIT; i++) {
			daily.add(String.format("|- %sh:", FORMAT_DATETIME.format(begin.getTime())));
			for(Task task : processedTasks) {
				if(withinTimePeriod(task.getEndTime(), begin, end))
					daily.set(daily.size() - 1, daily.get(daily.size() - 1) + " " + task.getTaskName());
			}
			begin.add(Calendar.HOUR_OF_DAY, 6);
			end.add(Calendar.HOUR_OF_DAY, 6);
		}
		for(String string : daily)	str += string + "\n";
		return str;
	}
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
	
	/*
	
	public static String taskToString(String name, int index, char check, Date date)
	{
		char prefix = (check == 'S' || check == 'E') ? 'T' : check;
		String color = "";
		switch(index % 6) {
			case 0:	color = RED;	break;
			case 1:	color = GREEN;	break;
			case 2:	color = YELLOW;	break;
			case 3:	color = BLUE;	break;
			case 4:	color = PURPLE;	break;
			case 5:	color = CYAN;	break;
			default:	break;
		}
		String str = color + "(" + prefix + index + "): " + name;
		switch(check)
		{
			case 'S':	return str + " [" + FORMAT_DATETIME.format(date) + "][Start]" + RESET;
			case 'E':	return str +" [" + FORMAT_DATETIME.format(date) + "][End]" + RESET;
			case 'D':	return str + " [" + FORMAT_DATETIME.format(date) + "]" + RESET;
			case 'F':	return str + RESET;
			default:  return "!!!!!";
		}
	}
	
	*/
	// Standardizes Calendar.getInstance() by zeroing smaller values
	private static Calendar delimitTime(Calendar time)
	{
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		return time;
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
	
	public static void initUIOnce(){
		if (!uiInit){
			initUI();
		}
	}
	
	private static void initUI(){
		System.setProperty("jansi.passthrough", "true");
		AnsiConsole.systemInstall();
	}
	
	/*
	 * DEPRECIATED
	 */
	
	/*
	private static ArrayList<Task> processTasks(ArrayList<Task> tasks) throws Exception
	{
		ArrayList<Task> pTasks = new ArrayList<Task>();
		int index = 0;
		String str;
		for(Task task : tasks) {
			if(task.isTimedTask()) {
				str = taskToString(task.getTaskName(), index, 'S', task.getStartTime().getTime());
				pTasks.add(new Task(str, task.getStartTime()));
				str = taskToString(task.getTaskName(), index++, 'E', task.getEndTime().getTime());
				pTasks.add(new Task(str, task.getEndTime()));
			}
			else if(task.isDeadline()) {
				str = taskToString(task.getTaskName(), index++, 'D', task.getEndTime().getTime());
				pTasks.add(new Task(str, task.getEndTime()));
			}
			else if(task.isFloatingTask()) {
				str = taskToString(task.getTaskName(), index++, 'F', null);
				pTasks.add(new Task(str));
			}
			
		}
		return pTasks;
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
	
	
	public static boolean withinTimePeriod(Calendar time, Calendar begin, Calendar end)
	{
		if(time == null)	return false;
		return (time.compareTo(begin) >= 0 && time.compareTo(end) < 0) ? true : false;
	}
	
	*/
	
}
