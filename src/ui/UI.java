package ui;
/**
 * UI.java
 * Author: Lian Jie Nicholas
 * First Updated: 26/09/2014
 * Last Updated: 19/10/2014
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
 */

import java.io.InvalidClassException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import zombietask.Task;

public class UI
{
	public static final FORMAT AGENDA = FORMAT.AGENDA;
	public static final FORMAT DAILY = FORMAT.DAILY;
	public static final FORMAT WEEKLY = FORMAT.WEEKLY;
	public static final FORMAT MONTHLY = FORMAT.MONTHLY;
	public static final FORMAT YEARLY = FORMAT.ANNUAL;
	public static final FORMAT INVALID = FORMAT.INVALID;
	private static final Format FORMAT_DATEMTH = new SimpleDateFormat("dd/MM");
	private static final Format FORMAT_DATETIME = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static final Format FORMAT_DAYDATE = new SimpleDateFormat("EEEEE, dd MMMMM yyyy");
	private static final Format FORMAT_TODAY = new SimpleDateFormat("dd MMMMM yyyy, HH:mm");
	private static final Format FORMAT_WEEKNUM = new SimpleDateFormat("ww");
	private static final int DAILY_LIMIT = 24;
	private static final int WEEKLY_LIMIT = 28;
	private static final int MONTHLY_LIMIT = 30;
	private static final int ANNUAL_LIMIT = 52;
	private static String str;
	private static int index;
	// PRIMARY USE METHODS
	public static void printResponse(String response)
	{
		System.out.println(response);
	}
	public static void printPerspective(FORMAT format, ArrayList<Task> tasks) throws InvalidClassException
	{
		if(tasks instanceof ArrayList<?> == false)
			throw new InvalidClassException("ArrayList error");
		if(tasks.get(0) instanceof Task == false)
			throw new InvalidClassException("Task error");
		if(tasks.isEmpty())
		{
			System.out.println("No tasks within time period." + format);
			return;
		}
		tasks = sortTasks(tasks);
		str = "";
		index = 0;
		switch(format)
		{
			case AGENDA:	printAgenda(tasks);	break;
			case DAILY:		printDaily(tasks);	break;
			case WEEKLY:	printWeekly(tasks);	break;
			case MONTHLY:	printMonthly(tasks);	break;
			case ANNUAL:	printAnnual(tasks);	break;
			default:	return;
		}
		System.out.println(str);
	}
	public static ArrayList<Task> sortTasks(ArrayList<Task> tasks)
	{
		for(int i = 1; i < tasks.size(); i++)
		for(int j = 0; j < tasks.size(); j++)
		if(tasks.get(i).getDeadline().before(tasks.get(j).getDeadline()))
			tasks.add(j, tasks.remove(i));
		return tasks;
	}
	// FORMAT METHODS
	private static void printAgenda(ArrayList<Task> tasks)
	{
		str = "(Agenda)\n";
		hasOverdue(tasks);
		str += "Today, " + FORMAT_TODAY.format(Calendar.getInstance().getTime()) + "\n";
		Calendar time = delimitTime(Calendar.getInstance());
		time.set(Calendar.HOUR_OF_DAY, 0);
		time.add(Calendar.DATE, 1);
		while(index < tasks.size())
		{
			if(time.before(tasks.get(index).getDeadline()))
			{
				str += FORMAT_DAYDATE.format(time.getTime()) + "\n";
				time.add(Calendar.DATE, 1);
			}
			else
			str += String.format("[%d]: %s\n", index, tasks.get(index++).toString());
		}
	}
	private static void printDaily(ArrayList<Task> tasks)
	{
		str = "(Daily)\n";
		hasOverdue(tasks);
		str += "Today, " + FORMAT_TODAY.format(Calendar.getInstance().getTime()) + "\n";
		Calendar time = delimitTime(Calendar.getInstance());
		for(int i = 0; i < DAILY_LIMIT; i++)
		{
			str += String.format("|- %sh:", FORMAT_DATETIME.format(time.getTime()));
			time.add(Calendar.HOUR_OF_DAY, 1);
			while(index < tasks.size() && tasks.get(index).getDeadline().before(time))
				str += String.format(" [%d]%s", index, tasks.get(index++).getTaskName());
			str += "\n";
		}
	}
	private static void printWeekly(ArrayList<Task> tasks)
	{
		str = "(Weekly)\n";
		hasOverdue(tasks);
		str += "Today, " + FORMAT_TODAY.format(Calendar.getInstance().getTime()) + "\n";
		Calendar time = delimitTime(Calendar.getInstance());
		for(int i = 0; i < WEEKLY_LIMIT; i++)
		{
			str += String.format("|- %sh:", FORMAT_DATETIME.format(time.getTime()));
			time.add(Calendar.HOUR_OF_DAY, 4);
			while(index < tasks.size() && tasks.get(index).getDeadline().before(time))
				str += String.format(" [%d]: %s", index, tasks.get(index++).getTaskName());
			str += "\n";
		}
	}
	private static void printMonthly(ArrayList<Task> tasks)
	{
		str = "(Monthly)\n";
		hasOverdue(tasks);
		str += "Today, " + FORMAT_TODAY.format(Calendar.getInstance().getTime()) + "\n";
		Calendar time = delimitTime(Calendar.getInstance());
		time.set(Calendar.HOUR_OF_DAY, 0);
		for(int i = 0; i < MONTHLY_LIMIT; i++)
		{
			str += String.format("|- %s:",FORMAT_DATEMTH.format(time.getTime()));
			time.add(Calendar.DATE, 1);
			while(index < tasks.size() && tasks.get(index).getDeadline().before(time))
				str += String.format(" [%d]: %s", index, tasks.get(index++).getTaskName());
			str += "\n";
		}
	}
	private static void printAnnual(ArrayList<Task> tasks)
	{
		str = "(Annual)\n";
		hasOverdue(tasks);
		str += "Today, " + FORMAT_TODAY.format(Calendar.getInstance().getTime()) + "\n";
		Calendar time = delimitTime(Calendar.getInstance());
		time.set(Calendar.HOUR_OF_DAY, 0);
		time.add(Calendar.DATE, 1 - time.get(Calendar.DAY_OF_WEEK));
		for(int i = 0; i < ANNUAL_LIMIT; i++)
		{
			str += String.format("|- Week %s:",FORMAT_WEEKNUM.format(time.getTime()));
			time.add(Calendar.DATE, 7);
			while(index < tasks.size() && tasks.get(index).getDeadline().before(time))
				str += String.format(" [%d]%s", index, tasks.get(index++).getTaskName());
			str += "\n";
		}
	}
	// MISCELLENEOUS METHODS
	public static String taskToString(Task task)
	{
		return String.format("%s [%s]", task.getTaskName(), FORMAT_DATETIME.format(task.getDeadline().getTime()));
	}
	// Standardizes Calendar.getInstance() by zeroing smaller values
	private static Calendar delimitTime(Calendar time)
	{
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		return time;
	}
	// If any overdue tasks exists, prints them in overdue column 
	private static void hasOverdue(ArrayList<Task> tasks)
	{
		if(tasks.get(0).isOverdue())
		{
			str += "!!! Overdue !!!\n";
			while(tasks.get(index).isOverdue() && index < tasks.size())
				str += String.format("[%d]: %s\n", index, tasks.get(index++).toString());
		}
	}
}