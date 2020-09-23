package inputvalidation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RadioButtonValidatorTest {

  IValidator<Boolean> testValidator1;
  IValidator<Boolean> testValidator2;

  @Before
  public void setUp() {
    this.testValidator1 = new RadioButtonValidator();
    this.testValidator2 = new RadioButtonValidator();
  }

  @Test
  public void isValid() {
    assertFalse(testValidator1.isValid(null));
    assertTrue(testValidator1.isValid(Boolean.TRUE));
    assertTrue(testValidator1.isValid(Boolean.FALSE));
  }

  @Test
  public void testToString() {
    String expected = "RadioButtonValidator{IS_NULL_ALLOWED=false}";
    assertEquals(testValidator1.toString(), expected);
  }

  @Test
  public void testEquals() {
    // reflexive
    assertTrue(testValidator1.equals(testValidator1));
    // testing against object types
    assertNotEquals(testValidator1,null);
    assertFalse(testValidator1.equals(0));
    // testing against fields, but this branch is not covered
    // because as long as this.getClass() = o.getClass(), they have the same final field
    assertTrue(testValidator1.equals(testValidator2));

  }

  @Test
  public void testHashCode() {
    assertEquals(testValidator1.hashCode(), testValidator2.hashCode());
  }
}