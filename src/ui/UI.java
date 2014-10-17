package ui;
/**
 * UI.java
 * Author: Lian Jie Nicholas
 * First Updated: 26/09/2014
 * Last Updated: 16/10/2014
 * 
 * UPDATE 16/10/2014:
 * 1. Renamed all references to yearly into annual.
 * 2. Method sortTasks to sort tasks by deadline before printing.
 * 2. All times set at 0min are allocated beneath hourly increments rather than before it.
 * 	  thus, deadlines set on 16/10/2014 00:00h are found within 16/10/2014 as opposed to
 * 	  15/10/2014.
 * 3. Removed printCalendar() and getFormat(String) methods.
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
	private static final Format FORMAT_DATETIME = new SimpleDateFormat("ddMMM HH:mm");
	private static final Format FORMAT_DAYDATE = new SimpleDateFormat("EEEEE, dd MMMMM yyyy");
	private static final Format FORMAT_TODAY = new SimpleDateFormat("dd MMMMM yyyy, HH:mm");
	private static final Format FORMAT_WEEKNUM = new SimpleDateFormat("ww");
	private static final int DAILY_LIMIT = 24;
	private static final int WEEKLY_LIMIT = 28;
	private static final int MONTHLY_LIMIT = 30;
	private static final int ANNUAL_LIMIT = 52;
	
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
		tasks = sortTasks(tasks);
		String str = "";
		System.out.println("in printPerspective");
		switch(format)
		{
			case AGENDA:	str = printAgenda(tasks);	break;
			case DAILY:		str = printDaily(tasks);	break;
			case WEEKLY:	str = printWeekly(tasks);	break;
			case MONTHLY:	str = printMonthly(tasks);	break;
			case ANNUAL:	str = printAnnual(tasks);	break;
			default:	return;
		}
		System.out.println(str);
	}
	public static ArrayList<Task> sortTasks(ArrayList<Task> tasks)
	{
		for(int i = 1; i < tasks.size(); i++)
		{
			for(int j = 0; j < tasks.size(); j++)
			{
				if(tasks.get(i).getDeadline().before(tasks.get(j).getDeadline()))
					tasks.add(j, tasks.remove(i));
			}
		}
		return tasks;
	}
	public static String taskToString(Task task)
	{
		return String.format("%s [%s]", task.getTaskName(), FORMAT_DATETIME.format(task.getDeadline()));
	}
	private static Calendar delimitTime(Calendar time)
	{
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		return time;
	}
	private static String printAgenda(ArrayList<Task> tasks)
	{
		if(tasks.isEmpty())	return "No ongoing tasks.";
		int index = 0;
		String str = "(Agenda)\n";
		if(tasks.get(0).isOverdue())
		{
			str += "!!! Overdue !!!\n";
			while(tasks.get(index).isOverdue() && index < tasks.size())
				str += String.format("[%d]: %s\n", index, tasks.get(index++).toString());
		}
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
		return str;
	}
	private static String printDaily(ArrayList<Task> tasks)
	{
		if(tasks.isEmpty())	return	"No tasks within 24 hours.";
		String str = "(Daily)\n";
		int index = 0;
		if(tasks.get(0).isOverdue())
		{
			str += "!!! Overdue !!!\n";
			while(tasks.get(index).isOverdue() && index < tasks.size())
				str += String.format("[%d]: %s\n", index, tasks.get(index++).toString());
		}
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
		return str;
	}
	private static String printWeekly(ArrayList<Task> tasks)
	{
		if(tasks.isEmpty())	return	"No tasks within 7 days.";
		String str = "(Weekly)\n";
		int index = 0;
		if(tasks.get(0).isOverdue())
		{
			str += "!!! Overdue !!!\n";
			while(tasks.get(index).isOverdue() && index < tasks.size())
				str += String.format("[%d]: %s\n", index, tasks.get(index++).toString());
		}
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
		return str;
	}
	private static String printMonthly(ArrayList<Task> tasks)
	{
		if(tasks.isEmpty())	return	"No tasks within 30 days.";
		String str = "(Monthly)\n";
		int index = 0;
		if(tasks.get(0).isOverdue())
		{
			str += "!!! Overdue !!!\n";
			while(tasks.get(index).isOverdue() && index < tasks.size())
				str += String.format("[%d]: %s\n", index, tasks.get(index++).toString());
		}
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
		return str;
	}
	private static String printAnnual(ArrayList<Task> tasks)
	{
		if(tasks.isEmpty())	return	"No tasks within 30 days.";
		String str = "(Annual)\n";
		int index = 0;
		if(tasks.get(0).isOverdue())
		{
			str += "!!! Overdue !!!\n";
			while(tasks.get(index).isOverdue() && index < tasks.size())
				str += String.format("[%d]: %s\n", index, tasks.get(index++).toString());
		}
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
		return str;
	}
}