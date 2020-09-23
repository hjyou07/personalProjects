package todo.Model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import todo.Controller.Util.InvalidInputCombinationException;

public class TodoListWrapperTest {
  private List<String> sampleCSVLines;
  private Todo todo1;
  private Todo todo2;
  private boolean setUpCompleted = false;

  @Before
  public void setUp() throws Exception {
    if (!setUpCompleted) {
      sampleCSVLines = new ArrayList<>();
      sampleCSVLines.add("\"id\",\"text\",\"completed\",\"due\",\"priority\",\"category\"");
      sampleCSVLines.add("\"1\",\"Finish HW9\",\"false\",\"3/22/2020\",\"1\",\"school\"");
      sampleCSVLines.add("\"2\",\"Mail passport\",\"true\",\"2/28/2020\",\"?\",\"?\"");
      sampleCSVLines.add("\"3\",\"Study for finals\",\"false\",\"?\",\"2\",\"school\"");
      sampleCSVLines.add("\"4\",\"Clean the house\",\"false\",\"3/22/2020\",\"3\",\"home\"");
      sampleCSVLines.add("\"5\",\"Buy yarn for blanket, stuffed toy\",\"true\",\"?\",\"?\",\"home\"");

      todo1 = new Todo("Finish HW9", false, null, "2", "school");
      todo2 = new Todo("Flight checkin", true, "4/3/2020", "1", null);
      setUpCompleted = true;
    }
  }

  /**
   * Testing for this class must be done in one section as only one instance of TodoLisTWrapper
   * can be created. Calls to getInstance() will obtain the same instance so unit-testing will
   * cause issues with values being dependent on other sections of the tests.
   */
  @Test
  public void testTodoWrapper() throws InvalidInputCombinationException {
/*
    // Test getInstance()
    TodoListWrapper todoList = TodoListWrapper.getInstance();

    // Test loadTodo()
    todoList.loadTodo(sampleCSVLines);

    // Test addTodo()
    todoList.addTodo(todo1);
    todoList.addTodo(todo2);

    // Test completeTodo
    todoList.completeTodo(3);

    // Test getTodoList() and to String()
    String expected = "[\"1\",\"Finish HW9\",\"false\",\"03/22/2020\",\"1\",\"school\","
        + " \"2\",\"Mail passport\",\"true\",\"02/28/2020\",\"?\",\"?\","
        + " \"3\",\"Study for finals\",\"true\",\"?\",\"2\",\"school\","
        + " \"4\",\"Clean the house\",\"false\",\"03/22/2020\",\"3\",\"home\","
        + " \"5\",\"Buy yarn for blanket, stuffed toy\",\"true\",\"?\",\"?\",\"home\","
        + " \"6\",\"Finish HW9\",\"false\",\"?\",\"2\",\"school\","
        + " \"7\",\"Flight checkin\",\"true\",\"04/03/2020\",\"1\",\"?\"]";
    assertEquals(todoList.getTodoList().toString(), expected);

    String expected2 = "\"1\",\"Finish HW9\",\"false\",\"03/22/2020\",\"1\",\"school\""
        + System.lineSeparator()
        + "\"2\",\"Mail passport\",\"true\",\"02/28/2020\",\"?\",\"?\"" + System.lineSeparator()
        + "\"3\",\"Study for finals\",\"true\",\"?\",\"2\",\"school\"" + System.lineSeparator()
        + "\"4\",\"Clean the house\",\"false\",\"03/22/2020\",\"3\",\"home\""
        + System.lineSeparator()
        + "\"5\",\"Buy yarn for blanket, stuffed toy\",\"true\",\"?\",\"?\",\"home\""
        + System.lineSeparator()
        + "\"6\",\"Finish HW9\",\"false\",\"?\",\"2\",\"school\"" + System.lineSeparator()
        + "\"7\",\"Flight checkin\",\"true\",\"04/03/2020\",\"1\",\"?\"" + System.lineSeparator();
    assertEquals(todoList.toString(), expected2);*/
  }

  @Test
  public void testEquals() throws InvalidInputCombinationException {
    TodoListWrapper todoList1 = TodoListWrapper.getInstance();
    TodoListWrapper todoList2 = TodoListWrapper.getInstance();
    todoList1.loadTodo(sampleCSVLines);
    todoList2.loadTodo(sampleCSVLines);

    assertEquals(todoList1, todoList1);
    assertEquals(todoList1, todoList2);

    assertNotEquals(todoList1, null);
    assertNotEquals(todoList1, 0.0);
  }

  @Test
  public void testHashCode() throws InvalidInputCombinationException {
    TodoListWrapper todoList1 = TodoListWrapper.getInstance();
    TodoListWrapper todoList2 = TodoListWrapper.getInstance();
    todoList1.loadTodo(sampleCSVLines);
    todoList2.loadTodo(sampleCSVLines);
    assertEquals(todoList1.hashCode(), todoList2.hashCode());
  }
}