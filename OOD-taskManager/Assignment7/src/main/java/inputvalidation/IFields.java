package inputvalidation;

/**
 * This interface represents a field of a form that accept different types of input from users.
 * @param <T> type of the input value from the user. e.g. String, Boolean
 */
public interface IFields<T> {

  /**
   * Update the Field's value if the input is valid according to the validator.
   * @param input the user input
   */
  void updateValue(T input) throws InvalidInputException;

  /**
   * Checks if the Field has been properly filled out.
   * @return true if the Field has been filled out. A Field will be considered filled when:
   * 1) the Field is required and its value meets the requirements specified by its validator, or
   * 2) the Field is optional (it doesn't matter what the Field's value is in this case).
   */
  boolean isFilled();

}
