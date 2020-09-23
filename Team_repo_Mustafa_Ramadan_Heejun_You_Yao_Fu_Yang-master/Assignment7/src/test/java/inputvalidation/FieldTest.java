package inputvalidation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FieldTest {
  // phone and number fields
  private Field<String> testPhoneField1;
  private Field<String> testPhoneField2;
  private Field<String> testNumberField3;
  private Field<String> testPhoneField4;
  private Field<String> testPhoneField5;
  // checkbox and radiobutton fields
  private Field<Boolean> testCheckBoxField;
  private Field<Boolean> testCheckBoxField2;
  private Field<Boolean> testRadioButtonField;
  private Field<Boolean> testRadioButtonField2;
  // password and free text fields
  private Field<String> testPassField1;
  private Field<String> testPassField2;
  private Field<String> testFreeTextField1;
  private Field<String> testFreeTextField2;

  @Before
  public void setUp() {
    IValidator<String> phoneValidator = new PhoneValidator(5);
    IValidator<String> numberValidator = new NumberValidator(0.0, 1.0, 2);
    IValidator<Boolean> checkBoxValidator = new CheckBoxValidator();
    IValidator<Boolean> radioButtonValidator = new RadioButtonValidator();
    IValidator<String> passValidator = new PassValidator(0,4, 1, 1, 1);
    IValidator<String> freeTextValidator = new FreeTextValidator(2,3);

    // phone and number fields
    testPhoneField1 = new Field<>("phone", true, phoneValidator);
    testPhoneField2 = new Field<>("phone", true, phoneValidator);
    testNumberField3 = new Field<>("number", true, phoneValidator);
    testPhoneField4 = new Field<>("phone", false, phoneValidator);
    // this is the case where the client puts in the "wrong" validator
    testPhoneField5 = new Field<>("phone", true, numberValidator);

    // checkbox and radiobutton fields
    testCheckBoxField = new Field<>("checkbox",true, checkBoxValidator);
    testRadioButtonField = new Field<>("radiobutton",true, radioButtonValidator);
    testCheckBoxField2 = new Field<>("checkbox", false, checkBoxValidator);
    testRadioButtonField2 = new Field<>("radiobutton", false, radioButtonValidator);

    // password and free text fields
    testPassField1 = new Field<>("password", true, passValidator);
    testPassField2= new Field<>("password", false, passValidator);
    testFreeTextField1 = new Field<>("freetext", true, freeTextValidator);
    testFreeTextField2= new Field<>("freetext", false, freeTextValidator);
  }

  @Test(expected = InvalidInputException.class)
  public void updateInValidValue() throws InvalidInputException {
    testPhoneField1.updateValue("Hi");
    testRadioButtonField.updateValue(null);
    testPassField1.updateValue("aA");
  }

  @Test
  public void updateValue() throws InvalidInputException {
    testPhoneField1.updateValue("00000");
    // checkbox and radiobutton fields
    testCheckBoxField.updateValue(Boolean.FALSE);
    testCheckBoxField.updateValue(Boolean.TRUE);
    testCheckBoxField.updateValue(null);

    testRadioButtonField.updateValue(Boolean.TRUE);
    testRadioButtonField.updateValue(Boolean.FALSE);

    // password and freetext fields
    testPassField1.updateValue("aA1");
    testFreeTextField1.updateValue("alls");
  }

  @Test
  public void isFilled() throws InvalidInputException  {
    // phone and number fields
    assertTrue(testPhoneField4.isFilled());
    assertFalse(testPhoneField1.isFilled());

    testPhoneField1.updateValue("00000");
    assertTrue(testPhoneField1.isFilled());

    // checkbox and radiobutton fields
    assertTrue(testCheckBoxField.isFilled());
    assertFalse(testRadioButtonField.isFilled());
    assertTrue(testCheckBoxField2.isFilled());
    assertTrue(testRadioButtonField2.isFilled());

    testCheckBoxField.updateValue(null);
    // so the default value of null was considered valid, and I update with just null again,
    // for checkbox, it should still be valid.
    assertTrue(testCheckBoxField.isFilled());
    testRadioButtonField.updateValue(Boolean.TRUE);
    // now the radiobutton is filled with a valid input
    assertTrue(testRadioButtonField.isFilled());

    // password and free text fields
    assertTrue(testFreeTextField2.isFilled());
    assertTrue(testPassField2.isFilled());

    testPassField1.updateValue("Aa1");
    assertTrue(testPassField1.isFilled());

    testFreeTextField1.updateValue("alls");
    assertTrue(testFreeTextField1.isFilled());
  }

  @Test
  public void testEquals() throws InvalidInputException {
    assertEquals(testPhoneField1, testPhoneField1);
    assertEquals(testPhoneField1, testPhoneField2);
    testPhoneField2.updateValue("00000");

    // Test fields
    assertNotEquals(testPhoneField1, testPhoneField2);
    assertNotEquals(testPhoneField1, testNumberField3);
    assertNotEquals(testPhoneField1, testPhoneField4);
    assertNotEquals(testPhoneField1, testPhoneField5);

    // Test object types
    assertNotEquals(testPhoneField1, 0.0);
    assertNotEquals(testPhoneField1, null);
  }

  @Test
  public void testHashCode() {
    assertEquals(testPhoneField1.hashCode(), testPhoneField2.hashCode());
  }

  @Test
  public void testToString() {
    String expected = "Field{label='phone', value=null, required=true}";
    assertEquals(testPhoneField1.toString(), expected);
  }
}