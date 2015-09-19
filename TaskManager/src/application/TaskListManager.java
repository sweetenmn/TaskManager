package application;


public class TaskListManager {
	
	private TaskList incompleteList;
	private TaskList completeList;
	
	public TaskListManager(TaskList incomplete, TaskList complete){
		incompleteList = incomplete;
		completeList = complete;
	}
	
	public void completeTask(int taskIndex){
		completeList.addTask(incompleteList.getTaskAt(taskIndex));
		incompleteList.deleteTaskAt(taskIndex);
	}
	
	public TaskList getIncompleteList(){
		return incompleteList;
	}
	
	public TaskList getCompleteList(){
		return completeList;
	}
	
	

}
