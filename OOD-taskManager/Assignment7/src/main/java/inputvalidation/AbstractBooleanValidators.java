package inputvalidation;

/**
 * This class represents the AbstractBooleanValidator implementing the generic iValidator interface.
 */
public abstract class AbstractBooleanValidators implements IValidator<Boolean> {
  /**
   * Given a input of type Boolean, always returns true.
   * Boolean will either be true, false or null.
   *
   * @param input - A Boolean, representing the client input. The input is just whether or not the
   *              button is selected.
   * @return true
   */
  @Override
  public Boolean isValid(Boolean input) {
    return Boolean.TRUE;
  }

}
