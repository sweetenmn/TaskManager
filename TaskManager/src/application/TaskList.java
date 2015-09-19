package application;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;

public class TaskList {
	
	public ArrayList<Task> taskList = new ArrayList<Task>();
	private String documentName;
	
	public TaskList(String listName){
		documentName = listName + ".txt";
		getSavedTasks();
	}
	
	public int getSize(){
		return taskList.size();
	}
	
	public String getTaskTextAt(int index){
		return taskList.get(index).getTaskText();
	}
	public Task getTaskAt(int index){
		return taskList.get(index);
	}
	
	public String getNoteAt(int index){
		return taskList.get(index).getNoteText();
	}
	public String getDateAt(int index){
		return taskList.get(index).getDueDate();
	}
	
	public void addTask(Task task){
		taskList.add(task);
		
	}
	
	public void deleteTaskAt(int index){
		taskList.remove(index);
		updateTextDoc();
	}
	
	public void updateTextDoc(){
        clearDoc();
        for (Task t: taskList){
           addToDoc(t);
        }

	}
	
	public void clearDoc(){
		 try {
			 FileWriter writer = new FileWriter(documentName);
			 writer.close();
			 }
		 catch (IOException e) {
			 e.printStackTrace();
			 }
	}
	public void addToDoc(Task task) {
		try {
			FileWriter writer = new FileWriter(documentName,
					true);
			writer.write("\r\n");
			writer.write("&/&" + task.getTaskText() + " ");
			writer.write("*/*" + task.getNoteText() + " ");
			writer.write("!/!" + task.getDueDate());
			writer.close();
			} 
		catch (IOException e) {
			e.printStackTrace();
			}
	 
	    }
	
	public void getSavedTasks(){
	    try {
	    	FileReader reader = new FileReader(documentName);
	    	BufferedReader bufferedReader = new BufferedReader(reader);
	    	String line;
	    	while ((line = bufferedReader.readLine()) != null) {
	    		readSaved(line);
	    		}
	    	reader.close();
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	public int getIndexOf(Task task){
		return taskList.indexOf(task);
	}
	
	//break this up-2 levels 
	public void readSaved(String line){
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
