package todo.Controller;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import todo.Controller.Util.InvalidInputCombinationException;
import todo.Display.FilterCommands;
import todo.Model.Todo;

public class CommandLineParserTest {
  private CommandLineParser test1;
  private CommandLineParser test2;
  private CommandLineParser test3;
  private CommandLineParser test4;
  private CommandLineParser test5;
  private CommandLineParser test6;
  private CommandLineParser test7;

  @Before
  public void setUp() throws InvalidInputCombinationException {
    String[] commands1 = new String[]{"--csv-file", "output.txt", "--add-todo", "--todo-text",
        "Do",  "HW9", "--completed", "--due", "04/04/2020", "--priority", "2", "--category", "school",
        "--complete-todo", "1", "--display", "--show-incomplete", "--show-category", "school",
        "--sort-by-date"};

    String[] commands2 = new String[]{"--csv-file", "output.txt", "--add-todo", "--todo-text",
        "Do", "HW9", "--completed", "--due", "04/04/2020", "--priority", "2", "--category", "school",
        "--complete-todo", "1"};

    String[] commands3 = new String[]{"--csv-file", "output.txt", "--complete-todo", "1",
        "--complete-todo", "4", "--complete-todo", "3", "--display", "--show-incomplete",
        "--show-category", "school", "--sort-by-date",};

    String[] commands4 = new String[]{"--csv-file", "output.txt", "--add-todo", "--todo-text",
        "Do", "all", "of", "HW9", "--completed", "--due", "04/04/2020", "--priority", "2",
        "--category", "school", "--complete-todo", "1", "--display", "--show-incomplete",
        "--show-category", "school", "--sort-by-priority"};

    String[] commands5 = new String[]{"--csv-file", "output.txt", "--add-todo", "--todo-text",
        "Do", "HW9", "--completed", "--priority", "2", "--category", "school",
        "--complete-todo", "1", "--display", "--show-incomplete", "--show-category", "school",
        "--sort-by-date"};

    String[] commands6 = new String[]{"--csv-file", "output.txt", "--add-todo", "--todo-text",
        "Do", "HW9", "--completed", "--due", "04/04/2020", "--priority", "2", "--category", "school",
        "--complete-todo", "2", "--display", "--show-incomplete", "--show-category", "school",
        "--sort-by-date"};

    test1 = new CommandLineParser(commands1);
    test2 = new CommandLineParser(commands1);
    test3 = new CommandLineParser(commands2);
    test4 = new CommandLineParser(commands3);
    test5 = new CommandLineParser(commands4);
    test6 = new CommandLineParser(commands5);
    test7 = new CommandLineParser(commands6);
  }

  // Test Null Case.
  @Test(expected=InvalidInputCombinationException.class)
  public void checkNullArgument() throws InvalidInputCombinationException {
    new CommandLineParser(null);
  }

  // Test missing matching keyword Case.
  @Test(expected=InvalidInputCombinationException.class)
  public void checkDuplicateKeywords() throws InvalidInputCombinationException {
    String[] commands = new String[]{"--csv-file", "output.txt", "--add-todo", "--todo-text",
        "Do", "HW9", "--todo-text", "Do", "HW9", "--completed", "--due", "04/04/2020", "--priority",
        "2", "--category", "school", "--complete-todo", "1", "--display", "--show-incomplete",
        "--show-category", "school", "--sort-by-date"};
    new CommandLineParser(commands);
  }

  // Test missing matching keyword Case.
  @Test(expected=InvalidInputCombinationException.class)
  public void checkMatchingKeywords() throws InvalidInputCombinationException {
    String[] commands = new String[]{"--csv-file", "output.txt", "--todo-text", "hi",
        "--completed", "--priority", "1", "--category", "school", "--complete-todo", "2"};
    new CommandLineParser(commands);
  }

  // Test missing matching keyword Case.
  @Test(expected=InvalidInputCombinationException.class)
  public void checkMatchingArguments() throws InvalidInputCombinationException {
    String[] commands = new String[]{"--csv-file", "output.txt", "--add-todo", "--todo-text", "hi",
        "--completed", "--due", "--priority", "1", "--category", "school", "--complete-todo", "2"};
    new CommandLineParser(commands);
  }

  // Test missing matching keyword Case.
  @Test(expected=InvalidInputCombinationException.class)
  public void checkRequiredKeywords() throws InvalidInputCombinationException {
    String[] commands = new String[]{"output.txt", "--add-todo", "--todo-text", "Do", "HW9",
        "--completed", "--due", "04/04/2020", "--priority", "2", "--category", "school",
        "--complete-todo", "1", "--display", "--show-incomplete", "--show-category", "school",
        "--sort-by-date"};
    new CommandLineParser(commands);
  }

  @Test
  public void getCompletedIDs() {
    assertEquals(test4.getCompletedIDs().toString(), "[1, 4, 3]");
  }

  @Test
  public void getPairedCommands() {
    Set<String> keySet = test1.getPairedCommands().keySet();
    String[] input = new String[]{"--show-category", "--csv-file", "--due", "--priority",
        "--category", "--todo-text"};
    Set<String> expected  = new HashSet<>(Arrays.asList(input));
    assertEquals(keySet, expected);

  }

  @Test
  public void getNewTodo() throws InvalidInputCombinationException {
    Todo expected = new Todo("Do HW9", true, "04/04/2020", "2", "school");
    assertEquals(test1.getNewTodo().toString(), expected.toString());

    // Check case where no new to-do was commanded.
     assertNull(test4.getNewTodo());
  }

  @Test
  public void getFilterCommands() throws InvalidInputCombinationException {
    FilterCommands expected = new FilterCommands(true, "school", true, false);
    assertEquals(test1.getFilterCommands().toString(), expected.toString());

    // Check case where no new display was not commanded.
     assertNull(test3.getFilterCommands());
  }

  @Test
  public void testEquals() {
    assertEquals(test1, test1);
    assertEquals(test1, test2);

    // Testing fields.
    assertNotEquals(test1, test5);
    assertNotEquals(test1, test6);
    assertNotEquals(test1, test7);

    assertNotEquals(test1, null);
    assertNotEquals(test1, 0.0);
  }

  @Test
  public void testHashCode() {
    assertEquals(test1.hashCode(), test2.hashCode());
  }

  @Test
  public void testToString() {
    String expected = "CommandLineParser{allCommands='--csv-file|output.txt|--add-todo||--todo-text"
        + "|Do HW9|--completed||--due|04/04/2020|--priority|2|--category|school|--complete-todo|1"
        + "|--display||--show-incomplete||--show-category|school|--sort-by-date|',"
        + " pairedCommands={--show-category=school, --csv-file=output.txt, --due=04/04/2020,"
        + " --priority=2, --category=school, --todo-text=Do HW9}, completedIDs=[1]}";
    assertEquals(test1.toString(), expected);
  }
}