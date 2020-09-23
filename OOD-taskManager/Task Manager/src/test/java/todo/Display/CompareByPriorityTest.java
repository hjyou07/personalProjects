package todo.Display;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import todo.Model.Todo;

public class CompareByPriorityTest {
  CompareByPriority test;
  Todo testTodo1;
  Todo testTodo2;
  Todo testTodo3;
  Todo testTodo4;
  Todo testTodo5;
  Todo testTodo6;

  @Before
  public void setUp() throws Exception {
    test = new CompareByPriority();
    testTodo1 = new Todo("Homework", false, null, "2", null);
    testTodo2 = new Todo("Homework", false, null, null, null);
    testTodo3 = new Todo("Homework", false, null, "2", null);
    testTodo4 = new Todo("Homework", false, null, "3", null);
    testTodo5 = new Todo("Homework", false, null, "1", null);
    testTodo6 = new Todo("Homework", false, null, null, null);
  }

  @Test
  public void compare() {
    assertTrue(test.compare(testTodo1, testTodo2) < 0);
    assertEquals(0, test.compare(testTodo1, testTodo3));
    assertTrue(test.compare(testTodo1, testTodo4) < 0);
    assertTrue(test.compare(testTodo1, testTodo5) > 0);
    assertEquals(0, test.compare(testTodo2, testTodo6));
  }
}