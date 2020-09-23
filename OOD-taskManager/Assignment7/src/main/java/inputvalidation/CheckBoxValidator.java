package inputvalidation;

import java.util.Objects;

/**
 * This class represents the CheckBoxValidator extending from AbstractBooleanValidator
 * Checkbox is considered to have a valid input even if it is null.
 */
public class CheckBoxValidator extends AbstractBooleanValidators {
  protected final Boolean isNullAllowed = Boolean.TRUE;

  /**
   * Given a input of type Boolean, always returns true.
   * Boolean will either be true, false or null. Each checkBox operates individually,
   * so it does not need to know whether the other checkboxes are selected or not,
   * hence the input can be null - "unknown".
   *
   * @param input - A Boolean, representing the client input.
   *             The input is just whether or not the button is selected.
   * @return true
   */
  @Override
  public Boolean isValid(Boolean input) {
    return super.isValid(input);
  }

  /**
   * Returns a string representation of this object.
   * @return a string representation of this object.
   */
  @Override
  public String toString() {
    return "CheckBoxValidator{" +
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
    CheckBoxValidator that = (CheckBoxValidator) o;
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
