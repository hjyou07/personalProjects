package todo.Controller.Util;

/**
 * This Exception class is thrown when an inappropriate combination of inputs occur in the
 * command line
 */
public class InvalidInputCombinationException extends Exception {
  /**
   * Constructs a new exception with the specified detail message.  The cause is not initialized, and
   * may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the {@link
   *                #getMessage()} method.
   */
  public InvalidInputCombinationException(String message) {
    super(message);
  }
}