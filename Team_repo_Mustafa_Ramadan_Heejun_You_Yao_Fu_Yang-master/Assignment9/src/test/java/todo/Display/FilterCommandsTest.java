package todo.Display;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import todo.Controller.Util.InvalidInputCombinationException;

public class FilterCommandsTest {
  private FilterCommands test1;
  private FilterCommands test2;
  private FilterCommands test3;
  private FilterCommands test4;
  private FilterCommands test5;
  private FilterCommands test6;
  private FilterCommands test7;

  @Before
  public void setUp() throws Exception {
    test1 = new FilterCommands(true, null, false, false);
    test2 = new FilterCommands(true, null, false, false);
    test3 = new FilterCommands(false, null, false, false);
    test4 = new FilterCommands(false, "school", false, false);
    test5 = new FilterCommands(true, null, true, false);
    test6 = new FilterCommands(true, null, false, true);
    test7 = new FilterCommands(false, "home", false, false);
  }

  @Test(expected= InvalidInputCombinationException.class)
  public void checkConflictingSorts() throws InvalidInputCombinationException {
    new FilterCommands(true, null, true, true);
  }

  @Test
  public void isIncompleteOnly() {
    assertTrue(test1.isIncompleteOnly());
  }

  @Test
  public void getCategory() {
    assertNull(test1.getCategory());
    assertEquals(test4.getCategory(), "school");
  }

  @Test
  public void isByDate() {
    assertFalse(test1.isByDate());
  }

  @Test
  public void isByPriority() {
    assertFalse(test1.isByPriority());
  }

  @Test
  public void testEquals() {
    assertEquals(test1, test1);
    assertEquals(test1, test2);

    // Test fields.
    assertNotEquals(test1, test3);
    assertNotEquals(test7, test4);
    assertNotEquals(test1, test5);
    assertNotEquals(test1, test6);

    // Check null and different classes.
    assertNotEquals(test1, null);
    assertNotEquals(test1, 0.0);
  }

  @Test
  public void testHashCode() {
    assertEquals(test1.hashCode(), test2.hashCode());
  }

  @Test
  public void testToString() {
    String expected = "DisplayFilter{incompleteOnly=true, category='null', byDate=false,"
        + " byPriority=false}";
    assertEquals(test1.toString(), expected);
  }
}