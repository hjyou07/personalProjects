package todo.Display;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import todo.Controller.Util.InvalidInputCombinationException;
import todo.Model.Todo;

public class FilteredDisplayTest {
  FilterCommands commands1;
  FilterCommands commands2;
  FilterCommands commands3;
  FilterCommands commands4;
  FilterCommands commands5;
  FilterCommands commands6;
  FilterCommands commands7;

  Todo todo1;
  Todo todo2;
  Todo todo3;
  Todo todo4;
  Todo todo5;
  ArrayList<Todo> todoList;

  FilteredDisplay test1;
  FilteredDisplay test2;
  FilteredDisplay test3;
  FilteredDisplay test4;
  FilteredDisplay test5;
  FilteredDisplay test6;
  FilteredDisplay test7;
  FilteredDisplay test8;

  @Before
  public void setUp() throws InvalidInputCombinationException {
    // Construct the To-do objects and puts them into a list for testing.
    todo1 = new Todo("Todo1", false, "3/31/2020", "2", "school");
    todo2 = new Todo("Todo2", false, "3/30/2020", null, "home");
    todo3 = new Todo("Todo3", true, "4/1/2020", "1", "school");
    todo4 = new Todo("Todo4", false, "3/31/2020", "3", null);
    todo5 = new Todo("Todo5", false, null, "2", "school");
    todoList = new ArrayList<>(Arrays.asList(todo1, todo2, todo3, todo4, todo5));

    // Build the commands.
    commands1 = new FilterCommands(false, null, false, false);  // Base case, should be all of it.
    commands2 = new FilterCommands(true, null, false, false);  // Should exclude completed ones.
    commands3 = new FilterCommands(false, "school", false, false);  // Should exclude school ones.
    commands4 = new FilterCommands(false, null, true, false);  // Sorts by date
    commands5 = new FilterCommands(false, null, false, true);  // Sorts by priority
    commands6 = new FilterCommands(true, "home", false, true);  // Filters and sorts.
    commands7 = new FilterCommands(true, "home", true, false);  // Filters and sorts.

    // Create tests for different filters/sorts.
    test1 = new FilteredDisplay(commands1, todoList);
    test2 = new FilteredDisplay(commands2, todoList);
    test3 = new FilteredDisplay(commands3, todoList);
    test4 = new FilteredDisplay(commands4, todoList);
    test5 = new FilteredDisplay(commands5, todoList);
    test6 = new FilteredDisplay(commands1, todoList);
    test7 = new FilteredDisplay(commands6, todoList);
    test8 = new FilteredDisplay(commands7, todoList);
  }

  @Test
  public void getTodoDisplayList() {
    String expected1 = "[\"null\",\"Todo1\",\"false\",\"03/31/2020\",\"2\",\"school\","
        + " \"null\",\"Todo2\",\"false\",\"03/30/2020\",\"?\",\"home\","
        + " \"null\",\"Todo3\",\"true\",\"04/01/2020\",\"1\",\"school\","
        + " \"null\",\"Todo4\",\"false\",\"03/31/2020\",\"3\",\"?\","
        + " \"null\",\"Todo5\",\"false\",\"?\",\"2\",\"school\"]";
    assertEquals(test1.getTodoDisplayList().toString(), expected1);

    String expected2 = "[\"null\",\"Todo1\",\"false\",\"03/31/2020\",\"2\",\"school\","
        + " \"null\",\"Todo2\",\"false\",\"03/30/2020\",\"?\",\"home\","
        + " \"null\",\"Todo4\",\"false\",\"03/31/2020\",\"3\",\"?\","
        + " \"null\",\"Todo5\",\"false\",\"?\",\"2\",\"school\"]";
    assertEquals(test2.getTodoDisplayList().toString(), expected2);

    String expected3 = "[\"null\",\"Todo1\",\"false\",\"03/31/2020\",\"2\",\"school\","
        + " \"null\",\"Todo3\",\"true\",\"04/01/2020\",\"1\",\"school\","
        + " \"null\",\"Todo5\",\"false\",\"?\",\"2\",\"school\"]";
    assertEquals(test3.getTodoDisplayList().toString(), expected3);

    String expected4 = "[\"null\",\"Todo2\",\"false\",\"03/30/2020\",\"?\",\"home\","
        + " \"null\",\"Todo1\",\"false\",\"03/31/2020\",\"2\",\"school\","
        + " \"null\",\"Todo4\",\"false\",\"03/31/2020\",\"3\",\"?\","
        + " \"null\",\"Todo3\",\"true\",\"04/01/2020\",\"1\",\"school\","
        + " \"null\",\"Todo5\",\"false\",\"?\",\"2\",\"school\"]";
    assertEquals(test4.getTodoDisplayList().toString(), expected4);

    String expected5 = "[\"null\",\"Todo3\",\"true\",\"04/01/2020\",\"1\",\"school\","
        + " \"null\",\"Todo1\",\"false\",\"03/31/2020\",\"2\",\"school\","
        + " \"null\",\"Todo5\",\"false\",\"?\",\"2\",\"school\","
        + " \"null\",\"Todo4\",\"false\",\"03/31/2020\",\"3\",\"?\","
        + " \"null\",\"Todo2\",\"false\",\"03/30/2020\",\"?\",\"home\"]";
    assertEquals(test5.getTodoDisplayList().toString(), expected5);

    String expected7 = "[\"null\",\"Todo2\",\"false\",\"03/30/2020\",\"?\",\"home\"]";
    assertEquals(test7.getTodoDisplayList().toString(), expected7);

    String expected8 = "[\"null\",\"Todo2\",\"false\",\"03/30/2020\",\"?\",\"home\"]";
    assertEquals(test8.getTodoDisplayList().toString(), expected8);
  }

  @Test
  public void testEquals() {
    assertEquals(test1, test1);
    assertEquals(test1, test6);

    assertNotEquals(test1, test2);
    assertNotEquals(test1, test3);
    assertNotEquals(test1, test4);
    assertNotEquals(test1, test5);

    assertNotEquals(test1, null);
    assertNotEquals(test1, 0.0);
  }

  @Test
  public void testHashCode() {
    assertEquals(test1.hashCode(), test6.hashCode());
  }

  @Test
  public void testToString() {
    String expected = "FilteredDisplay{comparator=null,"
        + " commands=DisplayFilter{incompleteOnly=false,"
        + " category='null', byDate=false, byPriority=false},"
        + " todoList=[\"null\",\"Todo1\",\"false\",\"03/31/2020\",\"2\",\"school\","
        + " \"null\",\"Todo2\",\"false\",\"03/30/2020\",\"?\",\"home\","
        + " \"null\",\"Todo3\",\"true\",\"04/01/2020\",\"1\",\"school\","
        + " \"null\",\"Todo4\",\"false\",\"03/31/2020\",\"3\",\"?\","
        + " \"null\",\"Todo5\",\"false\",\"?\",\"2\",\"school\"],"
        + " copyList=[\"null\",\"Todo1\",\"false\",\"03/31/2020\",\"2\",\"school\","
        + " \"null\",\"Todo2\",\"false\",\"03/30/2020\",\"?\",\"home\","
        + " \"null\",\"Todo3\",\"true\",\"04/01/2020\",\"1\",\"school\","
        + " \"null\",\"Todo4\",\"false\",\"03/31/2020\",\"3\",\"?\","
        + " \"null\",\"Todo5\",\"false\",\"?\",\"2\",\"school\"]}";
    assertEquals(test1.toString(), expected);
  }
}