package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class Grid {
	List<Integer> columnSizes;	
	GridPane grid;
	TaskListManager taskListManager;
	TaskList incompleteList;
	TaskList completeList;
	public Grid(ArrayList<Integer> columns, GridPane pane, TaskListManager listManager){
		columnSizes = columns;
		grid = pane;
		taskListManager = listManager;
		incompleteList = taskListManager.getIncompleteList();
		completeList = taskListManager.getCompleteList();
	}
	
	public void setUp(){
		grid.setGridLinesVisible(true);
		grid.setLayoutX(5);
		grid.setLayoutY(5);
		for (int i = 0; i < columnSizes.size(); i++){
			grid.getColumnConstraints().add(new ColumnConstraints(columnSizes.get(i)));
		}
	}
	
	public void addIncompleteTaskView(){
		for (int taskIndex = 0; taskIndex < incompleteList.getSize(); taskIndex++){
			Task task = incompleteList.getTaskAt(taskIndex);
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
			grid.add(newTaskText, 0, taskIndex);
			grid.add(newNoteText, 1, taskIndex);
			grid.add(newDateText, 2, taskIndex);
			Text deleteText = new Text();
			deleteText.setText("  X");
			grid.add(deleteText, 3, taskIndex);
			final int index = taskIndex;
			deleteText.setOnMouseClicked(k -> deleteTaskAt(index));
			CheckBox checker = new CheckBox();
			grid.add(checker, 4, taskIndex);
			checker.setOnAction(k -> completeTaskAt(index));
		}
	}
	
	void deleteTaskAt(int taskIndex){
		incompleteList.deleteTaskAt(taskIndex);
	}	
	void completeTaskAt(int taskIndex){
		taskListManager.completeTask(taskIndex);
	}

}
