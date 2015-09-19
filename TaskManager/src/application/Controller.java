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
	
	private long FRAMES_PER_SEC = 30L;
	
	private long interval = 1000000000L;
	private long getInterval(){return interval / FRAMES_PER_SEC;}
	
	private AnimationTimer timer = new AnimationTimer() {
		long last = 0;
		
		@Override
		public void handle(long now) {
			
			if (now - last > getInterval()) { 
				updateViews();
			}
		}
		
	};
	
	
	@FXML
	public void initialize(){
		formatter = DateTimeFormatter.ofPattern("MM/dd/uu");
		hasDate = false;
		incompleteTasks = new TaskList("IncompleteTasks");
		completeTasks = new TaskList("CompleteTasks");
		taskListManager = new TaskListManager(incompleteTasks, completeTasks);
		incompleteColumns = new ArrayList<Integer>();
		addIncompleteSizes(incompleteColumns);
		timer.start();
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
		Grid grid = new Grid(incompleteColumns, incGrid, taskListManager);
		grid.setUp();
		grid.addIncompleteTaskView();
		incompleteGridHolder.getChildren().add(incGrid);	
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
/*	
	void completeTaskAt(int taskIndex){
		taskListManager.completeTask(taskIndex);
		updateCompleteView();
		updateIncompleteView();
	}
	
	void deleteTaskAt(int taskIndex){
		incompleteTasks.deleteTaskAt(taskIndex);
		updateIncompleteView();
	}*/
}
