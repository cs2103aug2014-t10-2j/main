package ui;
//UI.java
//Author: Lian Jie Nicholas
/**
 * First updated: 26/09/2014
 */

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import task.Task;

public class UI
{
	public static final FORMAT AGENDA = FORMAT.AGENDA;
	public static final FORMAT DAILY = FORMAT.DAILY;
	public static final FORMAT WEEKLY = FORMAT.WEEKLY;
	public static final FORMAT MONTHLY = FORMAT.MONTHLY;
	public static final FORMAT YEARLY = FORMAT.YEARLY;
	public static final FORMAT CALENDAR = FORMAT.CALENDAR;
	public static final FORMAT INVALID = FORMAT.INVALID;
	private static final Format FORMAT_DATEMTH = new SimpleDateFormat("dd/MM");
	private static final Format FORMAT_DATETIME = new SimpleDateFormat("dd/MM HH:mm");
	private static final Format FORMAT_DAYDATE = new SimpleDateFormat("EEEEE, dd MMMMM yyyy");
	private static final Format FORMAT_MONTHLY = new SimpleDateFormat("MMMMM yyyy");
	private static final Format FORMAT_TODAY = new SimpleDateFormat("dd MMMMM yyyy, HH:mm");
	private static final Format FORMAT_WEEKNUM = new SimpleDateFormat("ww");
	private static final int DAILY_LIMIT = 24;
	private static final String CALENDAR_HEADER =  "|S   |M   |T   |W   |T   |F   |S   |\n";
	private static final String CALENDAR_BREAKER = "------------------------------------\n";
	
	public static void printResponse(String response)
	{
		System.out.println(response);
	}
	public static void printPerspective(String formatString, ArrayList<Task> tasks)
	{
		printPerspective(getFormat(formatString), tasks);
	}
	public static void printPerspective(FORMAT format, ArrayList<Task> tasks)
	{
		String str = "";
		switch(format)
		{
			case AGENDA:	str = printAgenda(tasks);	break;
			case DAILY:		str = printDaily(tasks);	break;
			case WEEKLY:	str = printWeekly(tasks);	break;
			case MONTHLY:	str = printMonthly(tasks);	break;
			case YEARLY:	str = printYearly(tasks);	break;
			case CALENDAR:	str = printCalendar(tasks);	break;
			default:	return;
		}
		System.out.println(str);
	}
	public static FORMAT getFormat(String formatString)
	{
		switch(formatString.toLowerCase())
		{
			case "agenda":		return FORMAT.AGENDA;
			case "daily":		return FORMAT.DAILY;
			case "weekly":		return FORMAT.WEEKLY;
			case "monthly":		return FORMAT.MONTHLY;
			case "yearly":		return FORMAT.YEARLY;
			case "calendar":	return FORMAT.CALENDAR;
			default:			return FORMAT.INVALID;
		}
	}
	private static String printAgenda(ArrayList<Task> tasks)
	{
		if(tasks.isEmpty())	return "No ongoing tasks.";
		
		Calendar time = Calendar.getInstance();
		int index = 0;
		String str = "(Agenda)\n";
		if(tasks.get(0).getEndTime().before(time))
		{
			str += "!!! Overdue !!!\n";
			while(index < tasks.size()){
				if (tasks.get(index).isOverdue()){
					str += String.format("[%d]: %s\n", index, tasks.get(index++).toString());
				}
			}	
		}
		str += "Today, " + FORMAT_TODAY.format(time.getTime()) + "\n";
		time.set(Calendar.HOUR, 0);
		time.set(Calendar.MINUTE, 0);
		time.add(Calendar.DATE, 1);
		while(index < tasks.size())
		{
			if(time.before(tasks.get(index).getEndTime()))
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
		Calendar time = Calendar.getInstance();
		int index = 0;
		if(tasks.get(0).getEndTime().before(time))
		{
			str += "!!! Overdue !!!\n";
			while(tasks.get(index).isOverdue())
				str += String.format("[%d]%s\n", index, tasks.get(index++).getTaskName());
		}
		str += "Today, " + FORMAT_TODAY.format(time.getTime()) + "\n";
		time.set(Calendar.MINUTE, 0);
		for(int i = 0; i < DAILY_LIMIT; i++)
		{
			str += String.format("|- %sh:", FORMAT_DATETIME.format(time.getTime()));
			time.add(Calendar.HOUR_OF_DAY, 1);
			while(index < tasks.size() && tasks.get(index).getEndTime().before(time))
				str += String.format(" [%d]%s", index, tasks.get(index++).getTaskName());
			str += "\n";
		}
		return str;
	}
	private static String printWeekly(ArrayList<Task> tasks)
	{
		if(tasks.isEmpty())	return	"No tasks within 7 days.";
		String str = "(Weekly)\n";
		Calendar time = Calendar.getInstance();
		int index = 0;
		if(tasks.get(0).getEndTime().before(time))
		{
			str += "!!! Overdue !!!\n";
			while(tasks.get(index).isOverdue())
				str += String.format("[%d]%s\n", index, tasks.get(index++).getTaskName());
		}
		str += "Today, " + FORMAT_TODAY.format(time.getTime()) + "\n";
		time.set(Calendar.MINUTE, 0);
		for(int i = 0; i < 28; i++)
		{
			
			str += String.format("|- %sh:", FORMAT_DATETIME.format(time.getTime()));
			time.add(Calendar.HOUR_OF_DAY, 4);
			while(index < tasks.size() && tasks.get(index).getEndTime().before(time))
				str += String.format(" [%d]%s", index, tasks.get(index++).getTaskName());
			str += "\n";
		}
		return str;
	}
	private static String printMonthly(ArrayList<Task> tasks)
	{
		if(tasks.isEmpty())	return	"No tasks within 30 days.";
		String str = "(Monthly)\n";
		Calendar time = Calendar.getInstance();
		int index = 0;
		if(tasks.get(0).getEndTime().before(time))
		{
			str += "!!! Overdue !!!\n";
			while(tasks.get(index).isOverdue())
				str += String.format("[%d]%s\n", index, tasks.get(index++).getTaskName());
		}
		str += "Today, " + FORMAT_TODAY.format(time.getTime()) + "\n";
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.HOUR, 0);
		for(int i = 0; i < 30; i++)
		{
			str += String.format("|- %s:",FORMAT_DATEMTH.format(time.getTime()));
			time.add(Calendar.DATE, 1);
			while(index < tasks.size() && tasks.get(index).getEndTime().before(time))
				str += String.format(" [%d]%s", index, tasks.get(index++).getTaskName());
			str += "\n";
		}
		return str;
	}
	private static String printYearly(ArrayList<Task> tasks)
	{
		if(tasks.isEmpty())	return	"No tasks within 30 days.";
		String str = "(Monthly)\n";
		Calendar time = Calendar.getInstance();
		int index = 0;
		if(tasks.get(0).getEndTime().before(time))
		{
			str += "!!! Overdue !!!\n";
			while(tasks.get(index).isOverdue())
				str += String.format("[%d]%s\n", index, tasks.get(index++).getTaskName());
		}
		str += "Today, " + FORMAT_TODAY.format(time.getTime()) + "\n";
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.HOUR, 0);
		time.add(Calendar.DATE, 1 - time.get(Calendar.DAY_OF_WEEK));
		for(int i = 0; i < 52; i++)
		{
			str += String.format("|- Week %s:",FORMAT_WEEKNUM.format(time.getTime()));
			time.add(Calendar.DATE, 7);
			while(index < tasks.size() && tasks.get(index).getEndTime().before(time))
				str += String.format(" [%d]%s", index, tasks.get(index++).getTaskName());
			str += "\n";
		}
		return str;
	}
	// Stub
	private static String printCalendar(ArrayList<Task> tasks)
	{
		Calendar time = Calendar.getInstance();
		int dateNum = 1;
		String str = FORMAT_MONTHLY.format(time.getTime()) + "\n";
		str += CALENDAR_HEADER + CALENDAR_BREAKER + "|";
		time.set(Calendar.DATE, 1);
		for(int i = 1; i <= 7; i++)
		{
			if(i < time.get(Calendar.DAY_OF_WEEK))
				str += "    |";
			else
				str += String.format("%2d  |", dateNum++);
		}
		return str;
	}
}
