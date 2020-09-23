package inputvalidation;

/**
 * Interface for the validator class. Accepts a generic parameter of either String of boolean type.
 * Contains a method
 * @param <T> - A generic type of either String or boolean data type.
 */
public interface IValidator<T> {

  /**
   * Given a generic input of type String or boolean, returns true if the input is valid according
   * to specific validation methods and false otherwise.
   * @param input - A generic object of either boolean or String type, representing the client input
   * @return true if the input is valid according to specific validation methods and false otherwise.
   */
  Boolean isValid(T input);
}