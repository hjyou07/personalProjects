package todo.Controller;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import todo.Controller.Util.InvalidInputCombinationException;

public class RunTaskTest {
  private RunTask controller;
  private CommandLineParser cmdLP;

  @Before
  public void setUp() throws InvalidInputCombinationException {
    String[] commands1 = new String[]{"--csv-file", "./todos.csv", "--add-todo", "--todo-text",
        "Do HW9", "--due", "04/04/2020", "--priority", "2", "--category", "school",
        "--complete-todo", "1", "--complete-todo", "3", "--display", "--show-incomplete",
        "--show-category", "school", "--sort-by-priority"};
    cmdLP = new CommandLineParser(commands1);
    controller = new RunTask(cmdLP);
  }

  @Test
  public void testRunTask() throws IOException, InvalidInputCombinationException {
    // Test Load Existing To-do
    controller.loadExistingTodo();
    String expected1 = "RunTask{todoList=, commandParser=CommandLineParser{allCommands='--csv-file|"
        + "./todos.csv|--add-todo||--todo-text|Do HW9|--due|04/04/2020|--priority|2|--category|"
        + "school|--complete-todo|1|--complete-todo|3|--display||--show-incomplete||--show-category|"
        + "school|--sort-by-priority|',"
        + " pairedCommands={--show-category=school, --csv-file=./todos.csv, --due=04/04/2020,"
        + " --priority=2, --category=school, --todo-text=Do HW9}, completedIDs=[1, 3]}}";
    assertEquals(controller.toString(), expected1);

    // Test adding new To-do.
    controller.addNewTodo();
    String expected2 = "RunTask{todoList=\"1\",\"Do HW9\",\"false\",\"04/04/2020\",\"2\",\"school\""
        + System.lineSeparator()
        + ", commandParser=CommandLineParser{allCommands='--csv-file|./todos.csv|--add-todo||"
        + "--todo-text|Do HW9|--due|04/04/2020|--priority|2|--category|school|--complete-todo|1|"
        + "--complete-todo|3|--display||--show-incomplete||--show-category|school|"
        + "--sort-by-priority|',"
        + " pairedCommands={--show-category=school, --csv-file=./todos.csv, --due=04/04/2020,"
        + " --priority=2, --category=school, --todo-text=Do HW9}, completedIDs=[1, 3]}}";
    assertEquals(controller.toString(), expected2);

    controller.updateCompletedTodo();
    String expected3 = "RunTask{todoList=\"1\",\"Do HW9\",\"true\",\"04/04/2020\",\"2\",\"school\""
        + System.lineSeparator()
        + ", commandParser=CommandLineParser{allCommands='--csv-file|./todos.csv|--add-todo||"
        + "--todo-text|Do HW9|--due|04/04/2020|--priority|2|--category|school|--complete-todo|1|"
        + "--complete-todo|3|--display||--show-incomplete||--show-category|school|"
        + "--sort-by-priority|', pairedCommands={--show-category=school, --csv-file=./todos.csv,"
        + " --due=04/04/2020, --priority=2, --category=school, --todo-text=Do HW9},"
        + " completedIDs=[1, 3]}}";
    assertEquals(controller.toString(), expected3);

    controller.displayFilteredTodo();
    // Not sure if I should include this as this only allows the test to run correctly once.
    controller.writeToCSV();
  }
}