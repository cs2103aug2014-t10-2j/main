package ZombieTask_StorageAPI;
// Task.java
// Basic class for creating Task objects
// 23/09 Updated with Calendar and Vector to optimize code.
// 26/09 Removed spelling errors and resorted methods by data attribute.
// Author: Lian Jie Nicholas
// Last Updated: 26/09/2014

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class Task
{	
	private static final Format FORMAT_DATETIME = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
	private static final Format FORMAT_DATEONLY = new SimpleDateFormat("dd/MM/yyyy");
	private static final Format FORMAT_TIME12H = new SimpleDateFormat("hh:mm a");
	private static final Format FORMAT_TIME24H = new SimpleDateFormat("HH:mm");
	//////////////////// DATA ATTRIBUTES ////////////////////
	private String taskName = "";
	private Calendar deadline = null;
	private ArrayList<String> tags = new ArrayList<String>();
	private ArrayList<Task> subtasks = new ArrayList<Task>();
	//////////////////// CONSTRUCTORS ////////////////////
	public Task(String taskName)	{	setTaskName(taskName);	}
	public Task(String taskName, Calendar deadline)
	{
		setTaskName(taskName);
		setDeadline(deadline);
	}
	//////////////////// TASKNAME METHODS ////////////////////
	public void setTaskName(String taskName) {
		this.taskName = taskName;	}
	public String getTaskName() {
		return this.taskName;	}
	//////////////////// DEADLINE METHODS ////////////////////
	public void setDeadline(Calendar deadline) {
		this.deadline = deadline;	}
	public void setDeadline(int year, int mth, int date, int hour, int min)
	{
		deadline = Calendar.getInstance();
		deadline.set(year, mth - 1, date, hour, min, 0);
	}
	public Calendar getDeadline() {
		return deadline;	}
	public boolean isOverdue() {
		return deadline.before(Calendar.getInstance());	}
	public boolean isFloatingTask() {
		return (deadline == null) ? true : false;	}
	//////////////////// TAGS METHODS ////////////////////
	public void addTag(String tag)
	{
		tags.add(tag.toUpperCase());
		List<String> list = tags;
		Collections.sort(list);
		tags = (ArrayList<String>) list;
	}
	public String getTag(int index) {
		return (tags.isEmpty()) ? "" : tags.get(index);	}
	public ArrayList<String> getTags() {
		return tags;	}
	public boolean hasTag(String tag) {
		return tags.contains(tag.toUpperCase());	}
	public void deleteTag(String tag)
	{
		if(hasTag(tag.toUpperCase()))	tags.remove(tag.toUpperCase());
	}
	//////////////////// SUBTASKS METHODS ////////////////////
	
	public void addSubtask(String subtask) {
		subtasks.add(new Task(subtask));	}
	public void addSubtask(String subtask, Calendar date) {
		subtasks.add(new Task(subtask, date));
	}
	public String subtasksToString()
	{
		String str = "";
		if(subtasks.isEmpty())	return "No Subtasks!";
		else
		{
			int i = 0;
			for(Task task: subtasks)	str += i++ + ": " +task.toString() + "\n";
		}
		return str;
	}
	//////////////////// MISCELLENEOUS ////////////////////
	public String toString()
	{
		return taskName + " [" + getDeadlineString() + "]";
	}
	public String getDeadlineString()
	{
		return (deadline == null) ? "" : FORMAT_DATETIME.format(deadline.getTime());
	}
	public String getDayString()
	{
		return (deadline == null) ? "" : FORMAT_DATEONLY.format(deadline.getTime());
	}
	public String getTime12HourString()
	{
		return (deadline == null) ? "" : FORMAT_TIME12H.format(deadline.getTime());
	}
	public String getTime24HourString()
	{
		return (deadline == null) ? "" : FORMAT_TIME24H.format(deadline.getTime());
	}
	public String postpone(int field, int num)
	{
		deadline.roll(field, num);
		return getDayString();
	}
	public String postpone(int num)	{	return postpone(Calendar.DATE, num);	}
	public String postponeTomorrow()	{	return postpone(Calendar.DATE, 1);	}
	public String postponeNextWeek()	{	return postpone(Calendar.DATE, 7);	}
	public String postponeNextMonth()	{	return postpone(Calendar.MONTH, 7);	}
	public int getYear()
	{
		return (deadline == null) ? 0 : deadline.get(Calendar.YEAR);
	}
	public int getMonth()
	{
		return (deadline == null) ? 0 : deadline.get(Calendar.MONTH);
	}
	public int getDate()
	{
		return (deadline == null) ? 0 : deadline.get(Calendar.DATE);
	}
}