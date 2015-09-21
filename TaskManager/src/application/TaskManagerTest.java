package application;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;


public class TaskManagerTest {
	
	public static class StartToolkit extends Application {
	    @Override
	    public void start(Stage primaryStage) throws Exception {
	    	//start stage in order to test TaskRow
	    }
	}

	@BeforeClass
	public static void launchJavaFX() {
	    Thread taskRowTest = new Thread("TaskRow testing") {
	        public void run() {
	            Application.launch(StartToolkit.class, new String[0]);
	        }
	    };
	    taskRowTest.start();
	}
	
	GridPane grid;
	TaskList testList;
	
	public TaskManagerTest(){
		try {
			testList = new TaskList("test");
			grid = new GridPane();
		} catch (Exception e) {
			System.out.println("test.txt not found");
		}
	}

	@Test
	public void taskTest() {
		Task task = new Task("a", "b", "c");
		textCheck(task, "a", "b", "c");
		updateTextTest(task);
	}
	
	public void textCheck(Task task, String expectedTask, String expectedNote, 
			String expectedDate){
		assertEquals(task.getTaskText(), expectedTask);
		assertEquals(task.getNoteText(), expectedNote);
		assertEquals(task.getDateText(), expectedDate);
	}
	
	public void updateTextTest(Task task){
		String oldText = task.getTaskText();
		task.updateTaskText("new task");
		task.updateNoteText("new note");
		task.addCompletedDateText();
		assertFalse(oldText.equals(task.getTaskText()));	
		assertEquals(task.getCurrentDate(), task.getDateText());
		assertEquals("new note", task.getNoteText());
	}
	
	@Test
	public void testRow(){
		Task task = new Task("a", "b", "c");
		TaskRow row = new TaskRow(grid, task, 1);
		grid.getChildren().clear();
		testAddingToRow(row);
		testEditing(row);
		testGetFromRow(row);
	}
	
	public void testAddingToRow(TaskRow row){
		assertTrue(grid.getChildren().isEmpty());
		row.addToGrid();
		assertFalse(grid.getChildren().isEmpty());
	}
	
	public void testEditing(TaskRow row){
		row.toggleEditable();
		rowIsEditable(row);
		row.toggleEditable();
		rowNotEditable(row);
	}

	public void rowNotEditable(TaskRow row){
		assertFalse(row.getTaskField().isEditable());
		assertFalse(row.getDateField().isEditable());
		assertFalse(row.getNoteField().isEditable());
	}
	
	public void rowIsEditable(TaskRow row){
		assertTrue(row.getTaskField().isEditable());
		assertTrue(row.getDateField().isEditable());
		assertTrue(row.getNoteField().isEditable());
	}
	
	public void testGetFromRow(TaskRow row){
		assertEquals("a", row.getTaskFieldText());
		assertEquals("b", row.getNoteFieldText());
		assertEquals("c", row.getDateFieldText());
	}
	
	@Test
	public void fileTest() throws Exception{
		Task task = new Task("a", "b", "c");
		testList.addTask(task);
		testList.addTask(task);
		assertEquals(countLines(), testList.getSize());
	}
	public int countLines() throws Exception{
    	FileReader reader = new FileReader("test.txt");
    	BufferedReader bufferedReader = new BufferedReader(reader);
    	String line;
    	int count = 0;
    	while ((line = bufferedReader.readLine()) != null) {
    		if (!line.isEmpty()){
    			count++;
    		}
    	}
    	reader.close();
    	return count;
	}
	
	@Test
	public void deleteTest() throws Exception{
		testList.clear();
		testList.clearDoc();
		Task task = new Task("a", "b", "c");
		testList.addTask(task);
		assertEquals(1, countLines());
		testList.deleteTaskAt(0);
		assertEquals(0, countLines());
	}
	
	
	@Test
	public void updateDocTest() throws Exception{
		Task task = new Task("a", "b", "c");
		testList.clear();
		testList.clearDoc();
		testList.addTask(task);
		textCheck(task, "a", "b", "c");
		checkUpdate(task);
		
	}
	
	public void checkUpdate(Task task){
		testList.updateTask(task, "2");
		testList.updateNote(task, "3");
		testList.updateDate(task, "4");
		textCheck(task, "2", "3", "4");
	}
	
	@Test
	public void getFromListTest() throws Exception{
		testList.clear();
		testList.clearDoc();
		Task task = new Task("1", "2", "3");
		testList.addTask(task);
		assertEquals(task, testList.getTaskAt(0));
		int index = testList.getIndexOf(task);
		taskListTextTest(index, "1", "2", "3");
	}
	
	public void taskListTextTest(int index, String expectedTask, String expectedNote,
			String expectedDate){
		assertEquals(expectedTask, testList.getTaskTextAt(index));
		assertEquals(expectedNote, testList.getNoteAt(index));
		assertEquals(expectedDate, testList.getDateAt(index));
		
	}
	
	@Test
	public void updateTaskListTest() throws Exception{
		testList.clear();
		testList.clearDoc();
		grid.getChildren().clear();
		ArrayList<TaskRow> rows = new ArrayList<TaskRow>();
		Task task = new Task("1", "2", "3");
		testList.addTask(task);
		TaskRow row = new TaskRow(grid, task, 0);
		row.addToGrid();
		rows.add(row);
		testChanged(task, row);
		testList.updateTaskList(rows);
		assertEquals(task.getTaskText(), row.getTaskFieldText());
		
	}
	
	public void testChanged(Task task, TaskRow row){
		assertEquals(task.getTaskText(), row.getTaskFieldText());
		row.getTaskField().setText("changed");
		assertFalse(task.getTaskText().equals(row.getTaskFieldText()));
	}
	

}
