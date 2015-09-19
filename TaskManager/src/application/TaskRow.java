package application;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class TaskRow {
	private Task task;
	private TextField taskText;
	private TextField noteText;
	private TextField dateText;
	private int index;
	private GridPane grid;
	
	
	public TaskRow(GridPane gridPane, Task taskInput, int taskIndex){
		grid = gridPane;
		index = taskIndex;
		task = taskInput;
		taskText = new TextField();
		noteText = new TextField();
		dateText = new TextField();
	}
	
	public void addTaskNoteToGrid(){
		addTaskField();
		addNoteField();
	}
	
	private void addTaskField(){
		taskText.setEditable(false);
		taskText.setPrefSize(150, 25);
		taskText.setText(task.getTaskText());
		grid.add(taskText, 0, index);
		
	}
	
	private void addNoteField(){
		noteText.setEditable(false);
		noteText.setPrefSize(225, 25);
		noteText.setText(task.getNoteText());
		grid.add(noteText, 1, index);
		
	}
	
	public void addDateField(String date){
		dateText = new TextField();
		dateText.setEditable(false);
		dateText.setPrefSize(65, 25);
		dateText.setText(date);
		grid.add(dateText, 2, index);
	}
	
	public void toggleEditable(){
		taskText.setEditable(!taskText.isEditable());
		noteText.setEditable(!noteText.isEditable());
		dateText.setEditable(!dateText.isEditable());
	}
	
	public String getTaskFieldText(){return taskText.getText();}
	public String getNoteFieldText(){return noteText.getText();}
	public String getDateFieldText(){return dateText.getText();}
	

}
