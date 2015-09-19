package application;

public class Task {
	private String taskText;
	private String noteText;
	private String dueDate;
	public Task(String taskInput, String noteInput, String dueInput){
		taskText = taskInput;
		noteText = noteInput;
		dueDate = dueInput;		
	}
	
	public String getTaskText(){return taskText;}
	public String getNoteText(){return noteText;}
	public String getDueDate(){return dueDate;}

}
