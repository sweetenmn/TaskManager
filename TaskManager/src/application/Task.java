package application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Task {
	private String taskText;
	private String noteText;
	private String dateText;
	public Task(String taskInput, String noteInput, String dueInput){
		taskText = taskInput;
		noteText = noteInput;
		dateText = dueInput;		
	}
	
	public String getTaskText(){return taskText;}
	public String getNoteText(){return noteText;}
	public String getDateText(){return dateText;}
	
	public void updateTaskText(String newText){taskText = newText;}
	
	public void updateNoteText(String newText){noteText = newText;}
	
	public void updateDateText(String newText){dateText = newText;}
	
	public void addCompletedDateText(){updateDateText(getCurrentDate());}
	
	public String getCurrentDate(){
		   DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		   Calendar cal = Calendar.getInstance();
		   return dateFormat.format(cal.getTime());
	}

}
