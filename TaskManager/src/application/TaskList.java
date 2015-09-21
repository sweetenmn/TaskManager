package application;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class TaskList {
	
	private ArrayList<Task> taskList = new ArrayList<Task>();
	private String documentName;
	
	public TaskList(String listName) throws Exception{
		documentName = listName + ".txt";
		getSavedTasks();
	}
	
	public void clear(){
		taskList.clear();
	}
	public int getSize(){
		return taskList.size();
	}
	public Task getTaskAt(int index){
		return taskList.get(index);
	}
	public String getTaskTextAt(int index){
		return taskList.get(index).getTaskText();
	}
	public String getNoteAt(int index){
		return taskList.get(index).getNoteText();
	}
	public String getDateAt(int index){
		return taskList.get(index).getDateText();
	}
	public void addTask(Task task) throws Exception{
		taskList.add(task);
		updateTextDoc();
	}
	public void deleteTaskAt(int index) throws Exception{
		taskList.remove(index);
		updateTextDoc();
	}
	public int getIndexOf(Task task){
		return taskList.indexOf(task);
	}
	
	public void updateTextDoc() throws Exception{
        clearDoc();
        for (Task t: taskList){
           addToDoc(t);
        }
	}
	
	public void clearDoc() throws Exception{
		FileWriter writer = new FileWriter(documentName);
		writer.close();
	}
	
	public void updateTaskList(ArrayList<TaskRow> updatedList) throws Exception{
		for (int i = 0; i < updatedList.size(); i++){
			Task oldTask = getTaskAt(i);
			TaskRow newInput = updatedList.get(i);
			updateTask(oldTask, newInput.getTaskFieldText());
			updateNote(oldTask, newInput.getNoteFieldText());
			updateDate(oldTask, newInput.getDateFieldText());
		}
		updateTextDoc();
	}
	
	public void updateTask(Task task, String newText){
		String oldText = task.getTaskText();
		if (!newText.equals(oldText)){
			task.updateTaskText(newText);
		}
	}
	
	public void updateNote(Task task, String newText){
		String oldText = task.getNoteText();
		if (!newText.equals(oldText)){
			task.updateNoteText(newText);
		}
	}
	public void updateDate(Task task, String newText){
		String oldText = task.getDateText();
		if (!newText.equals(oldText)){
			task.updateDateText(newText);
		}
	}
	
	public void setDateOfCompletion(int index){
		taskList.get(index).addCompletedDateText();
	}
	
	public void addToDoc(Task task) throws Exception {
		FileWriter writer = new FileWriter(documentName,
				true);
		writer.write("\n");
		writer.write("&/&" + task.getTaskText());
		writer.write("*/*" + task.getNoteText());
		writer.write("!/!" + task.getDateText());
		writer.close();
	}
	
	public void getSavedTasks() throws Exception{
	    FileReader reader = new FileReader(documentName);
	    BufferedReader bufferedReader = new BufferedReader(reader);
	    String line;
	    while ((line = bufferedReader.readLine()) != null) {
	    	addTaskFromLine(line);
	    	}
	    reader.close();
	}
	
	public void addTaskFromLine(String line){
		if (line.startsWith("&/&")){
			int stopTask = line.indexOf("*/*");
			int stopNote = line.indexOf("!/!");
			String taskText = (line.substring(3, stopTask));
			String noteText = (line.substring(stopTask + 3,
					stopNote));
			String dateText = (line.substring(stopNote + 3));
			taskList.add(new Task(taskText, noteText, dateText));
		}
	}
	
}
