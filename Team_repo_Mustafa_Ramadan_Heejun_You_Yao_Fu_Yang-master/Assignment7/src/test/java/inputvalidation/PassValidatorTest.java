package inputvalidation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PassValidatorTest {

  private IValidator<String> testValidator1;
  private IValidator<String> testValidator2;
  private IValidator<String> testValidator3;
  private IValidator<String> testValidator4;
  private IValidator<String> testValidator6;


  @Before
  public void setUp() {
    testValidator1 = new PassValidator(0, 4, null, null, null);
    testValidator2 = new PassValidator(0, 4, 1, null, null);
    testValidator3 = new PassValidator(0, 4, null, 1, null);
    testValidator4 = new PassValidator(0, 4, null, null, 1);
    testValidator6 = new PassValidator(2, 4, null, null, null);

  }

  @Test
  public void isValid() {
    // test contains space, THIS PASSES
    assertFalse(testValidator1.isValid(" "));
    assertFalse(testValidator1.isValid("a l"));

//    test for length of minimum and maximum
    assertFalse(testValidator6.isValid("a"));
    assertFalse(testValidator6.isValid("aA0aa"));

    // test for minimum lowercase
    assertTrue(testValidator2.isValid("aa"));
    assertFalse(testValidator2.isValid("AA"));

    //test for minimum uppercase
     assertTrue(testValidator3.isValid("AA"));
    assertFalse(testValidator3.isValid("aa"));

    // test for minimum digits
    assertTrue(testValidator4.isValid("99"));
    assertFalse(testValidator4.isValid("aa"));

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
    String expected = "PassValidator{minimum=0, maximum=4, minimum_lowercase=0, minimum_uppercase=0, minimum_digits=1}";
    assertEquals(testValidator4.toString(), expected);
  }
}
