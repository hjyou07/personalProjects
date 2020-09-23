package inputvalidation;

import java.util.Objects;

/**
 * Creates an instance of the NumberValidator class which implements the iValidator generic
 * interface with String type arguments. Fields include a double representing the minimum input
 * value, a double representing the maximum input value, and an int representing the maximum
 * decimal places. Methods include an isValid(input) method which returns true if the input is a
 * valid Number and false otherwise.
 */
public class NumberValidator implements IValidator<String> {

  private double minimumValue;
  private double maximumValue;
  private int maximumDecimalPlaces;
  private static final int DEFAULT_DECIMAL_PLACES = 0;

  /**
   * Constructs an instance of the NumberValidator class that accepts two doubles representing the
   * minimum and maximum values (inclusive) that an input should have. These doubles may be
   * specified out of order. The constructor also accepts an Integer representing the maximum
   * decimal places the input should have, defaulting to 0 if a negative value is passed.
   * @param minOrMaxOne - Double representing the max or min an input should have.
   * @param minOrMaxTwo - Double representing the max or min an input should have.
   * @param maxDecimalPlaces - Integer representing the maximum decimal places an input should
   *                             have. Defaults all negative values as 0.
   */
  public NumberValidator(Double minOrMaxOne, Double minOrMaxTwo, Integer maxDecimalPlaces) {
    this.minimumValue = Math.min(minOrMaxOne, minOrMaxTwo);
    this.maximumValue = Math.max(minOrMaxOne, minOrMaxTwo);
    this.maximumDecimalPlaces = maxDecimalPlaces >= 0 ? maxDecimalPlaces :
        Integer.valueOf(DEFAULT_DECIMAL_PLACES);
  }

  /**
   * Given a String instance, returns true if the input is a valid Number.
   * @param input - A String instance representing the client input.
   * @return returns true if the input is a valid Number.
   */
  @Override
  public Boolean isValid(String input) {
    double number;
    try {
      number = Double.parseDouble(input);
    } catch (NumberFormatException e) {
      return false;
    }
    return this.validateInput(number, input);
  }

  /**
   * Helper method.
   * Given a String representing the input and a double representing its value, returns true if the
   * input is valid and false otherwise.
   * @param number - double representing the input value.
   * @param input - String representing the input.
   * @return true if the input is valid and false otherwise.
   */
  private Boolean validateInput(double number, String input) {
    if (number < this.minimumValue || number > this.maximumValue) {
      return false;
    } else if (!input.contains(".")) {
      return true;
    } else {
      return input.length() - input.indexOf(".") - 1 <= this.maximumDecimalPlaces;
    }
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
    NumberValidator that = (NumberValidator) o;
    return Double.compare(that.minimumValue, this.minimumValue) == 0 &&
        Double.compare(that.maximumValue, this.maximumValue) == 0 &&
        this.maximumDecimalPlaces == that.maximumDecimalPlaces;
  }

  /**
   * Returns an int representing the hashcode of this object.
   * @return an int representing the hashcode of this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.minimumValue, this.maximumValue, this.maximumDecimalPlaces);
  }

  /**
   * Returns a string representation of this object.
   * @return a string representation of this object.
   */
  @Override
  public String toString() {
    return "NumberValidator{" +
        "minimumValue=" + this.minimumValue +
        ", maximumValue=" + this.maximumValue +
        ", maximumDecimalPlaces=" + this.maximumDecimalPlaces +
        '}';
  }
}
