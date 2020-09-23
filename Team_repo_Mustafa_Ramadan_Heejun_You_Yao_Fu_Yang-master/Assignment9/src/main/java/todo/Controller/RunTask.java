package todo.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import todo.Controller.Util.CSVFileReader;
import todo.Controller.Util.InvalidInputCombinationException;
import todo.Display.FilterCommands;
import todo.Display.FilteredDisplay;
import todo.Model.Todo;
import todo.Model.TodoListWrapper;
import todo.View.CSVFileWriter;

/**
 * Class that acts as the controller for the whole program. This class will handle the synthesis
 * of all the functionality we want. It takes a CommandLineParser object and internally populates
 * the TodoListWrapper object, then performs requested tasks from the user by ensuring proper
 * communication between the command user provides and the data model being tracked.
 */
public class RunTask {
  TodoListWrapper todoList; // this will contain the master copy!
  // we have 2 copies of List<To-do> todoList : master copy in TodoListWrapper, and filtered copy in filteredDisplay
  CommandLineParser commandParser;

  /**
   * This constructor creates an instance of a singleton class TodoListWrapper, as well as a
   * commandParser that is passed in during the creation of the object.
   * @param commandParser an object from the CommandLineParser
   */
  public RunTask(CommandLineParser commandParser) {
    // Created instance of a singleton class TodoListWrapper, because only need to access the
    // actual List<To-do> todoList which is a field in TodoListWrapper
    this.todoList = TodoListWrapper.getInstance();
    this.commandParser = commandParser;

  }

  /**
   * Loads the existing todos already in the csv file provided from the user.
   * It reads from the file path inputted by the user in the command line,
   * then loads the existing data into our data model.
   * If the csv file is not found, it leaves the internal data structure empty.
   * @throws InvalidInputCombinationException
   * @throws IOException
   */
  public void loadExistingTodo() throws InvalidInputCombinationException, IOException {
    String filePath = this.commandParser.getPairedCommands().get("--csv-file");
    List<String> allLinesFromFile = CSVFileReader.readFromCSV(filePath);
    // above returns null if filePath doesn't exist
    this.todoList.loadTodo(allLinesFromFile);
    // above does nothing if allLinesFromFile is null

  }

  /**
   * Adds a todoObject to our data model.
   * It grabs the todoObject's specification from the user input in the command line,
   * then adds to the internal data structure, which is a list of todoObjects.
   */
  public void addNewTodo() throws InvalidInputCombinationException {
    Todo todo = this.commandParser.getNewTodo();
    this.todoList.addTodo(todo);
  }

  /**
   * This method updates the completed TodoObject when the completed command comes in. Thus,
   * assess the ID assoctaed with that TodoObject, and mark it as complete
   */
  public void updateCompletedTodo() {
    ArrayList<String> markCompleted = this.commandParser.getCompletedIDs();
    for (String id : markCompleted) {
      this.todoList.completeTodo(Integer.valueOf(id));
    }
  }

  /**
   * This method prints out the FilteredTodo list when that command is called in by the user.
   * The printout is specific to whatever filters are being commanded by the user
   * @throws InvalidInputCombinationException
   */
  public void displayFilteredTodo() throws InvalidInputCombinationException {
    FilterCommands filterCommands = this.commandParser.getFilterCommands();
    if (filterCommands != null) {
      FilteredDisplay filteredDisplay = new FilteredDisplay(filterCommands,
          this.todoList.getTodoList());
      List<Todo> filteredTodo = filteredDisplay.getTodoDisplayList();
      for (Todo todo : filteredTodo) {
        System.out.println(todo);
      }
    }
  }

  /**
   * Called the CSVFileWriter class's writeMasterCSV method to run with the paramaters of the
   * filtered todoList as well as the designated filePath where the file writing is to occur
   * @throws IOException
   */
  public void writeToCSV() throws IOException {
    String filePath = this.commandParser.getPairedCommands().get("--csv-file");
    CSVFileWriter.writeMasterCSV(this.todoList, filePath);
  }

  /**
   * To String method reprsenting the RunTask object
   * @return a string
   */
  @Override
  public String toString() {
    return "RunTask{" +
        "todoList=" + this.todoList +
        ", commandParser=" + this.commandParser +
        '}';
  }

}
