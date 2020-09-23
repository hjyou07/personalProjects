package inputvalidation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class FreeTextValidatorTest {
  private IValidator<String> testValidator1;
  private IValidator<String> testValidator2;
  private IValidator<String> testValidator3;
  private IValidator<String> testValidator4;

  @Before
  public void setUp()  {
    testValidator1 = new FreeTextValidator(0, 5);
    testValidator2 = new FreeTextValidator(5, 0);
    testValidator3 = new FreeTextValidator(1, 5);
    testValidator4 = new FreeTextValidator(2, 3);

  }

  @Test
  public void isValid() {
    // Tests the length of the string
    // Nothing can be true for validator1 (can't be less than 0 characters)
    assertFalse(testValidator1.isValid("12"));
    // Nothing can be true for validator2 (can't be less than 0 characters)
    assertFalse(testValidator2.isValid("12"));
    // Tests for the length of 5 characters
    assertTrue(testValidator3.isValid("4"));
    assertFalse(testValidator3.isValid("000000"));
    // Tests for the length of 6 characters
    assertTrue(testValidator4.isValid("8"));
    assertFalse(testValidator4.isValid("0000100"));
  }

    @Test
    public void testEquals() {
    assertEquals(testValidator1, testValidator1);
    assertEquals(testValidator2, testValidator2);
    assertNotEquals(testValidator1, testValidator2);
    assertNotEquals(testValidator1, testValidator3);
    assertNotEquals(testValidator1, testValidator4);

    assertNotEquals(testValidator1, 0);
    assertNotEquals(testValidator1, null);
    }

    @Test
    public void testHashCode() {
    assertEquals(testValidator1.hashCode(), testValidator1.hashCode());
    assertNotEquals(testValidator1.hashCode(), testValidator2.hashCode());
    }

    @Test
    public void testToString() {
    String expected = "FreeTextValidator{numLines=2, numChar=3}";
    assertEquals(testValidator4.toString(), expected);
    }


  }