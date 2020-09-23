package inputvalidation;

import java.util.Objects;

/**
 * Creates an instance of the PhoneValidator class which implements the generic interface
 * iValidator with String type arguments. Fields include an int representing the expected input
 * length to match exactly. Methods include isValid(input), which returns true if the input is a
 * valid Phone number and false otherwise.
 */
public class PhoneValidator implements IValidator<String> {
  private int length;

  /**
   * Creates a constructor
   * @param length - An Integer representing the expected input length (exactly).
   */
  public PhoneValidator(Integer length){
    this.length = length;
  }

  /**
   * Given a String representing an input, returns true if the input is a valid Phone number.
   * to specific validation methods and false otherwise.
   * @param input - A String object representing the client input.
   * @return true if the input is a valid Phone number.
   */
  @Override
  public Boolean isValid(String input) {
    try {
      Integer.parseInt(input);
    } catch (NumberFormatException e) {
      return false;
    }
    return input.length() == this.length;
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
    PhoneValidator that = (PhoneValidator) o;
    return this.length == that.length;
  }

  /**
   * Returns an int representing the hashcode of this object.
   * @return an int representing the hashcode of this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.length);
  }

  /**
   * Returns a string representation of this object.
   * @return a string representation of this object.
   */
  @Override
  public String toString() {
    return "PhoneValidator{" +
        "length=" + this.length +
        '}';
  }
}
