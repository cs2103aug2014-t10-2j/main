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
import java.util.logging.Logger;

import logger.ZombieLogger;
import task.Task;
import task.TaskUIFormat;

public class UI
{
	
	/*
	 * WTF NO HARD CODED LIMITS HERE
	 */
	
	/*
	private static final int DAILY_LIMIT = 24;
	private static final int WEEKLY_LIMIT = 28;
	private static final int MONTHLY_LIMIT = 30;
	private static final int ANNUAL_LIMIT = 52;
	*/
	
	/*
	 * Class Constants
	 */
	
	public static final String RESET = "\u001B[0m";
	public static final String BLACK = "\u001B[30m";
	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String BLUE = "\u001B[34m";
	public static final String PURPLE = "\u001B[35m";
	public static final String CYAN = "\u001B[36m";
	public static final String WHITE = "\u001B[37m";
	
	public static final String HEADER_AGENDA = "(Agenda)\n";
	public static final String HEADER_DAILY = "(Daily)\n";
	public static final String HEADER_WEEKLY = "(Weekly)\n";
	public static final String HEADER_MONTHLY = "(Monthly)\n";
	public static final String HEADER_ANNUAL = "(Annual)\n";
	public static final String HEADER_TODAY = "Current Time: "; //"Today, "
	public static final String HEADER_LINE_DOUBLE = "\n\n";
	public static final String HEADER_LINE_SINGLE = "\n";
	
	public static final String PRINTOUT_STYLE = ""
	
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
	
	/*
	 * Class variables
	 */
	
	private static Logger logger = ZombieLogger.getLogger();
	
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
				case DAILY:		str = printDaily(tasks);	break;
				case WEEKLY:	str = printWeekly(tasks);	break;
				case MONTHLY:	str = printMonthly(tasks);	break;
				case ANNUAL:	str = printAnnual(tasks);	break;
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
	 * @param tasks
	 * @return String to be printed
	 * @throws Exception
	 */
	
	private static String printAgenda(TaskUIFormat tasks) throws Exception {
		
		String str = "(Agenda)\n";
		
		TaskUIFormat overdueTasks = getOverdueTasks(tasks);
		if overdueTasks.
		for (Task task : overdueTasks.getDeadlineTasks()){
			str += task.getTaskName() + "\n";
		}
		for (Task task : overdueTasks.getTimedTasks()){
			str += task.getTaskName() + "\n";
		}
		
		str += HEADER_TODAY + FORMAT_TODAY.format(Calendar.getInstance().getTime()) + "\n";
		
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
		
		if(!tasks.getFloatingTasks().isEmpty())
		{
			str += "<<<<< Floating Tasks >>>>>\n";
			for(Task floatingTask : tasks.getFloatingTasks()){
				str += floatingTask.getTaskName() + "\n";
			}
		}
		return str;
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
		return new TaskUIFormat(null, searchDeadlineList, searchTimedList);
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
