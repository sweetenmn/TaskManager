package application;

import java.time.format.DateTimeFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
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
	ScrollPane incompleteScrollPane;
	@FXML
	ScrollPane completeScrollPane;
	@FXML
	Button incompleteTasksButton;
	@FXML
	Button completeTasksButton;
	@FXML
	Button addNewTaskButton;
	@FXML
	Button saveNewTaskButton;
	@FXML
	Button editButton;
	@FXML
	TextField taskInput;
	@FXML
	TextField notesInput;
	@FXML
	DatePicker dueDatePicker;
	
	Boolean hasDate;
	DateTimeFormatter formatter;
	TaskList incompleteTasks;
	TaskList completeTasks;
	TaskListManager taskListManager;
	ArrayList<Integer> incompleteColumns;
	ArrayList<Integer> completeColumns;	
	
	
	@FXML
	public void initialize(){
		formatter = DateTimeFormatter.ofPattern("MM/dd/uu");
		hasDate = false;
		incompleteTasks = new TaskList("IncompleteTasks");
		completeTasks = new TaskList("CompleteTasks");
		taskListManager = new TaskListManager(incompleteTasks, completeTasks);
		incompleteColumns = new ArrayList<Integer>();
		addIncompleteSizes(incompleteColumns);
		updateViews();
	}
	
	public void addIncompleteSizes(ArrayList<Integer> list){
		list.add(150);
		list.add(230);
		list.add(55);
		list.add(20);
		list.add(20);
	}
	
	public void updateViews(){
		updateIncompleteView();
		updateCompleteView();
	}
	
	private String getCurrentDate(){
		   DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		   Calendar cal = Calendar.getInstance();
		   return dateFormat.format(cal.getTime());
	}
	
	@FXML
	public void addNewTask(){
		if (newTaskPane.isVisible()){
			newTaskPane.setVisible(false);
		} else {
			newTaskPane.setVisible(true);
		}			
	}

	@FXML
	void dateInputted(){hasDate = true;}
	
	@FXML
	public void saveNewTask(){
		Task task = new Task(taskInput.getText(), notesInput.getText(), 
				formattedDate());
		incompleteTasks.addTask(task);
		updateViews();
		clearNewTask();
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
	
	public void updateCompleteView(){
		completeGridHolder.getChildren().clear();
		GridPane compGrid = new GridPane();
		compGrid.setGridLinesVisible(true);
		compGrid.setLayoutX(5);
		compGrid.setLayoutY(5);
		compGrid.getColumnConstraints().add(new ColumnConstraints(150));
		compGrid.getColumnConstraints().add(new ColumnConstraints(230));
		compGrid.getColumnConstraints().add(new ColumnConstraints(75));
		for (int taskIndex = 0; taskIndex < completeTasks.getSize(); taskIndex++){
			Text compTaskText = new Text();
			compTaskText.setText(completeTasks.getTaskTextAt(taskIndex));
			compGrid.add(compTaskText, 0, taskIndex);
			Text compNoteText = new Text();
			compNoteText.setText(completeTasks.getNoteAt(taskIndex));
			compGrid.add(compNoteText, 1, taskIndex);
			Text compDate = new Text();
			compDate.setText(getCurrentDate());
			compGrid.add(compDate, 2, taskIndex);
		}
		
		completeGridHolder.getChildren().add(compGrid);
	}

	public void updateIncompleteView(){
		incompleteGridHolder.getChildren().clear();
		GridPane incGrid = new GridPane();
		setUp(incGrid, incompleteColumns);
		for (int taskIndex = 0; taskIndex < incompleteTasks.getSize(); taskIndex++){
			Task task = incompleteTasks.getTaskAt(taskIndex);
			TextField newTaskText = new TextField();
			TextField newNoteText = new TextField();
			Text newDateText = new Text();
			newTaskText.setEditable(false);
			newNoteText.setEditable(false);
			newTaskText.setPrefSize(150, 25);
			newNoteText.setPrefSize(225, 25);
			newTaskText.setText(task.getTaskText());
			newNoteText.setText(task.getNoteText());
			newDateText.setText(task.getDueDate());
			incGrid.add(newTaskText, 0, taskIndex);
			incGrid.add(newNoteText, 1, taskIndex);
			incGrid.add(newDateText, 2, taskIndex);
			Text deleteText = new Text();
			deleteText.setText("  X");
			incGrid.add(deleteText, 3, taskIndex);
			final int index = taskIndex;
			deleteText.setOnMouseClicked(k -> deleteTaskAt(index));
			CheckBox checker = new CheckBox();
			incGrid.add(checker, 4, taskIndex);
			checker.setOnAction(k -> completeTaskAt(index));
		}
		incompleteGridHolder.getChildren().add(incGrid);	
	}
	
	public void setUp(GridPane grid, ArrayList<Integer> columnSizes){
		grid.setGridLinesVisible(true);
		grid.setLayoutX(5);
		grid.setLayoutY(5);
		for (int i = 0; i < columnSizes.size(); i++){
			grid.getColumnConstraints().add(new ColumnConstraints(columnSizes.get(i)));
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
	
	void completeTaskAt(int taskIndex){
		taskListManager.completeTask(taskIndex);
		updateViews();
	}
	
	void deleteTaskAt(int taskIndex){
		incompleteTasks.deleteTaskAt(taskIndex);
		updateViews();
	}
}
