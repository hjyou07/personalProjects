package inputvalidation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NumberValidatorTest {

  private IValidator<String> testValidator1;
  private IValidator<String> testValidator2;
  private IValidator<String> testValidator3;
  private IValidator<String> testValidator4;
  private IValidator<String> testValidator5;

  @Before
  public void setUp() {
    testValidator1 = new NumberValidator(0.0, 5.0, 2);
    testValidator2 = new NumberValidator(5.0, 0.0, 2);
    testValidator3 = new NumberValidator(0.0, 5.0, -2);
    testValidator4 = new NumberValidator(2.0, 5.0, 2);
    testValidator5 = new NumberValidator(0.0, 3.0, 2);
  }

  @Test
  public void isValid() {
    // Test input conversion
    assertFalse(testValidator1.isValid("@#@$%"));
    assertTrue(testValidator1.isValid("04"));

    // Test value constraint
    assertFalse(testValidator1.isValid("-0.1"));
    assertFalse(testValidator1.isValid("5.1"));

    // Test decimal constraint
    assertTrue(testValidator1.isValid("0.21"));
    assertFalse(testValidator1.isValid("4.444"));
  }

  @Test
  public void testEquals() {
    assertEquals(testValidator1, testValidator1);
    assertEquals(testValidator1, testValidator2);

    // Test fields
    assertNotEquals(testValidator1, testValidator3);
    assertNotEquals(testValidator1, testValidator4);
    assertNotEquals(testValidator1, testValidator5);

    // Test object types
    assertNotEquals(testValidator1, 0.0);
    assertNotEquals(testValidator1, null);
  }

  @Test
  public void testHashCode() {
    assertEquals(testValidator1.hashCode(), testValidator2.hashCode());
  }

  @Test
  public void testToString() {
    String expected = "NumberValidator{minimumValue=0.0, maximumValue=5.0, maximumDecimalPlaces=2}";
    assertEquals(testValidator1.toString(), expected);
  }
}