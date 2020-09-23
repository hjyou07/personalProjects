package inputvalidation;

import java.util.Objects;

/**
 * This class represents the RadioButtonValidator extending from AbstractBooleanValidator
 * Radiobutton cannot have a null input.
 */
public class RadioButtonValidator extends AbstractBooleanValidators {
  protected final Boolean isNullAllowed = Boolean.FALSE;
  /**
   * Given a input of type Boolean, returns false if the input is null and true otherwise.
   * Boolean will either be true, false or null. RadioButton needs to provide mutually exclusive
   * selection values, so it needs to know whether the button is selected or not,
   * hence the input can't be null - "unknown".
   *
   * @param input - A Boolean, representing the client input.
   *             The input is just whether or not the button is selected.
   * @return false if the input is null, true otherwise.
   */
  @Override
  public Boolean isValid(Boolean input) {
    return input != null;
  }

  /**
   * Returns a string representation of this object.
   * @return a string representation of this object.
   */
  @Override
  public String toString() {
    return "RadioButtonValidator{" +
        "IS_NULL_ALLOWED=" + isNullAllowed +
        "}";
  }

  /**
   * Given an object o, returns true if that object is equal to this object and false otherwise.
   * @param o - Instance of Object being compared to this object.
   * @return true if that object is equal to this object and false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RadioButtonValidator that = (RadioButtonValidator) o;
    return Objects.equals(isNullAllowed, that.isNullAllowed);
  }

  /**
   * Returns an int representing the hashcode of this object.
   * @return an int representing the hashcode of this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(isNullAllowed);
  }
}
