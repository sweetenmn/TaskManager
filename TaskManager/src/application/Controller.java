package application;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Controller {
	
	@FXML
	Pane completePane;
	@FXML
	Pane incompletePane;
	@FXML
	Pane newTaskPane;
	@FXML
	AnchorPane incompleteGridHolder;
	@FXML
	AnchorPane completeGridHolder;
	@FXML
	Button incompleteTasksButton, completeTasksButton, addNewTaskButton, saveNewTaskButton,
		editButton, saveChangesButton, clearComplete;
	@FXML
	TextField taskInput, notesInput;
	@FXML
	DatePicker dueDatePicker;
	
	Boolean hasDate;
	DateTimeFormatter formatter;
	TaskList incompleteTasks;
	TaskList completeTasks;
	ArrayList<TaskRow> incompleteTaskRows;
	ArrayList<TaskRow> completeTaskRows;
	ArrayList<CheckBox> checkBoxes;
	ArrayList<Text> deleters;
	JOptionPane dialogue;
	
	@FXML
	public void initialize(){
		formatter = DateTimeFormatter.ofPattern("MM/dd/uu");
		hasDate = false;
		incompleteTaskRows = new ArrayList<TaskRow>();
		completeTaskRows = new ArrayList<TaskRow>();
		checkBoxes = new ArrayList<CheckBox>();
		deleters = new ArrayList<Text>();
		try {
			incompleteTasks = new TaskList("IncompleteTasks");
			completeTasks = new TaskList("CompleteTasks");
			updateViews();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Task file not found. Previously saved tasks have been lost.");
		}
	}
	
	@FXML
	void dateInputted(){hasDate = true;}

	@FXML
	public void addNewTask(){
		if (newTaskPane.isVisible()){
			newTaskPane.setVisible(false);
		} else {
			newTaskPane.setVisible(true);
		}			
	}
	
	@FXML
	public void saveNewTask(){
		try {
			Task task = new Task(taskInput.getText(), notesInput.getText(), 
					formattedDate());
			incompleteTasks.addTask(task);
			updateViews();
			clearNewTask();
		} catch (Exception e) {
			showErrorMessage();
		}
	}
	
	public String formattedDate(){
		if (!hasDate || dueDatePicker.getValue().equals(null)){
			return "";
		} else {
			return dueDatePicker.getValue().format(formatter);
		}
	}
	
	public void clearNewTask(){
		taskInput.clear();
		notesInput.clear();
		dueDatePicker.setValue(null);
		hasDate=false;
	}
	
	@FXML
	public void clearCompleteTasks(){
		try {
			completeTasks.clear();
			completeTasks.clearDoc();
			updateViews();
		} catch (Exception e) {
			showErrorMessage();
		}
	}
	@FXML
	public void viewCompleteTasks(){
		clearNewTask();
		incompletePane.setVisible(false);
		newTaskPane.setVisible(false);
		completePane.setVisible(true);
	}
	
	@FXML
	public void viewIncompleteTasks(){
		completePane.setVisible(false);
		incompletePane.setVisible(true);
	}
	
	@FXML
	public void saveEdited(){
		try {
			toggleEditing(); 
			incompleteTasks.updateTaskList(incompleteTaskRows);
		} catch (Exception e) {
			showErrorMessage();
		}
	}
	
	@FXML
	public void toggleEditing(){
		saveChangesButton.setVisible(!saveChangesButton.isVisible());
		for (TaskRow t: incompleteTaskRows){
			t.toggleEditable();
		}
		toggleCheckable();
		toggleDeleteable();
		clearNewTask();
		newTaskPane.setVisible(false);
		addNewTaskButton.setVisible(!addNewTaskButton.isVisible());
	}
	
	public void toggleCheckable(){
		for (CheckBox c: checkBoxes){
			c.setVisible(!c.isVisible());
		}
	}
	
	public void toggleDeleteable(){
		for (Text t: deleters){
			t.setVisible(!t.isVisible());
		}
	}
	
	
	public void completeTaskAt(int taskIndex){
		try {
			incompleteTasks.setDateOfCompletion(taskIndex);
			completeTasks.addTask(incompleteTasks.getTaskAt(taskIndex));
			incompleteTasks.deleteTaskAt(taskIndex);
			updateViews();
		} catch (Exception e) {
			showErrorMessage();
		}
	}
	
	public void deleteTaskAt(int taskIndex){
		try {
			incompleteTasks.deleteTaskAt(taskIndex);
			updateViews();
		} catch (Exception e) {
			showErrorMessage();

		}
	}
	
	public void updateViews() throws Exception{
		updateIncompleteView();
		updateCompleteView();
	}
	public void updateCompleteView(){
		completeGridHolder.getChildren().clear();
		completeTaskRows.clear();
		GridPane compGrid = new GridPane();
		setUp(compGrid);
		for (int taskIndex = 0; taskIndex < completeTasks.getSize(); taskIndex++){
			TaskRow taskRow = new TaskRow(compGrid, completeTasks.getTaskAt(taskIndex), taskIndex);
			taskRow.addToGrid();
			completeTaskRows.add(taskRow);
		}
		completeGridHolder.getChildren().add(compGrid);
	}

	public void updateIncompleteView() throws Exception{
		incompleteGridHolder.getChildren().clear();
		incompleteTaskRows.clear();
		checkBoxes.clear();
		deleters.clear();
		GridPane incGrid = new GridPane();
		setUp(incGrid);
		for (int taskIndex = 0; taskIndex < incompleteTasks.getSize(); taskIndex++){
			TaskRow taskRow = new TaskRow(incGrid, incompleteTasks.getTaskAt(taskIndex), taskIndex);
			taskRow.addToGrid();
			incompleteTaskRows.add(taskRow);
			addDeleter(incGrid, taskIndex);
			addCheckBox(incGrid, taskIndex);
		}
		incompleteGridHolder.getChildren().add(incGrid);	
	}
	
	public void setUp(GridPane grid){
		grid.setGridLinesVisible(true);
		grid.setLayoutX(5);
		grid.setLayoutY(5);
	}
	
	public void addCheckBox(GridPane grid, int index) throws Exception{
		CheckBox checker = new CheckBox();
		checker.setOnAction(k -> completeTaskAt(index));
		checker.setTranslateX(2);
		grid.add(checker, 4, index);
		checkBoxes.add(checker);
	}
	
	public void addDeleter(GridPane grid, int index) throws Exception{
		Text deleteText = new Text();
		deleteText.setText(" X ");
		deleteText.setUnderline(true);
		deleteText.setOnMouseClicked(k -> deleteTaskAt(index));
		grid.add(deleteText, 3, index);
		deleters.add(deleteText);
	}
	
	public void showErrorMessage(){
		JOptionPane.showMessageDialog(null, "Task file not found. Please locate correct .txt file.");
	}

}
