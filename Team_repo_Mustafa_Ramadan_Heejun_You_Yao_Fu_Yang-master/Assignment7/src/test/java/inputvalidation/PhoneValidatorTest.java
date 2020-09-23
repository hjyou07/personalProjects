package inputvalidation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PhoneValidatorTest {
  private IValidator<String> testValidator1;
  private IValidator<String> testValidator2;
  private IValidator<String> testValidator3;

  @Before
  public void setUp() {
    testValidator1 = new PhoneValidator(10);
    testValidator2 = new PhoneValidator(10);
    testValidator3 = new PhoneValidator(11);
  }

  @Test
  public void isValid() {
    assertTrue(testValidator1.isValid("0003334444"));
    assertFalse(testValidator1.isValid("00003334444"));
    assertFalse(testValidator1.isValid("@#@$%"));
  }

  @Test
  public void testEquals() {
    assertEquals(testValidator1, testValidator1);
    assertEquals(testValidator1, testValidator2);

    // Test fields
    assertNotEquals(testValidator1, testValidator3);

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
    String expected = "PhoneValidator{length=10}";
    assertEquals(testValidator1.toString(), expected);
  }
}