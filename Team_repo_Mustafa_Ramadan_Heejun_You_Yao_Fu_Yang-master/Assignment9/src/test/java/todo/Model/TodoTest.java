package todo.Model;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import todo.Controller.Util.InvalidInputCombinationException;

public class TodoTest {
  Todo test1;
  Todo test2;
  Todo test3;
  Todo test4;
  Todo test5;
  Todo test6;

  @Before
  public void setUp() throws InvalidInputCombinationException {
    test1 = new Todo("Test", false, "3/31/2020", "2", "school");
    test2 = new Todo("Test", false, "3/30/2020", null, "home");
    test3 = new Todo("Test", true, "4/1/2020", "1", "school");
    test4 = new Todo("Test", false, "3/31/2020", "3", null);
    test5 = new Todo("Test", false, "3/31/2020", "2", "home");
    test6 = new Todo("Test", false, "3/31/2020", "2", "school");
  }

  @Test(expected=InvalidInputCombinationException.class)
  public void checkImproperDate() throws InvalidInputCombinationException {
    new Todo("Todo1", false, "03/2020", "2", "school");
    // TODO (ALL): Need to consider cases of 3/32/2020 and 3/-22/2020 and whether we want our code
    // to accept these or not. Currently, it does.
  }

  @Test(expected=InvalidInputCombinationException.class)
  public void checkImproperLowPriority() throws InvalidInputCombinationException {
    new Todo("Todo1", false, "03/31/2020", "4", "school");
  }

  @Test(expected=InvalidInputCombinationException.class)
  public void checkImproperHighPriority() throws InvalidInputCombinationException {
    new Todo("Todo1", false, "03/31/2020", "0", "school");
  }

  @Test(expected=InvalidInputCombinationException.class)
  public void checkImproperPriorityString() throws InvalidInputCombinationException {
    new Todo("Todo1", false, "03/31/2020", "priority", "school");
  }

  @Test
  public void setCompleted() {
    test1.setCompleted();
    assertTrue(test1.isCompleted());
  }

  @Test
  public void getText() {
    String expected = "Test";
    assertEquals(test1.getText(), expected);
  }

  @Test
  public void getId() {
    assertNull(test1.getId());
  }

  @Test
  public void isCompleted() {
    assertFalse(test1.isCompleted());
  }

  @Test
  public void getDueDate() throws ParseException {
    Date dueDate =  new SimpleDateFormat("MM/dd/yyyy").parse("3/31/2020");
    assertEquals(test1.getDueDate(), dueDate);
  }

  @Test
  public void getPriority() {
    assertEquals(test1.getPriority(), Integer.valueOf(2));
  }

  @Test
  public void getCategory() {
    assertEquals(test1.getCategory(), "school");
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
    String expected = "\"null\",\"Test\",\"false\",\"03/31/2020\",\"2\",\"school\"";
    assertEquals(test1.toString(), expected);
  }
}