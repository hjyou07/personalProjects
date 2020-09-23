package inputvalidation;

import java.util.Objects;

public class Field<T> implements IFields<T> {
  private String label;
  private T value;
  private boolean required;
  private IValidator<T> validator;

  /**
   * Constructs a generic Field class that can be used for an online form.
   * @param label a label associated with the field. e.g "password", "checkbox"
   * @param required indicates whether a particular field must be completed
   *                 before the form can be submitted.
   * @param validator an object that can perform input validation.
   */
  public Field(String label, boolean required, IValidator<T> validator) {
    this.label = label;
    this.value = null;
    this.required = required;
    this.validator = validator;
  }

  /**
   * A helper method for updateValue, checks the validity of the input according to the validator.
   * @param input the user input
   * @return true if the input is valid, false otherwise.
   */
  private boolean isValid(T input) {
    return validator.isValid(input);
  }

  /**
   * Update the Field's value if the input is valid according to the validator.
   *
   * @param input the user input
   */
  public void updateValue(T input) throws InvalidInputException {
    // we need to determine how we're going to dispatch the method isValid()
    // of the correct validator e.g. PasswordValidator, CheckBoxValidator
    // I don't fucking know how to use double dispatch. Who would? Bill Gates?
    if (isValid(input)) {
      this.value = input;
    } else {
      throw new InvalidInputException("Invalid input");
    }
  }

  /**
   * Checks if the Field has been properly filled out.
   *
   * @return true if the Field has been filled out. A Field will be considered filled when:
   * 1) the Field is required and its value meets the requirements specified by its validator, or
   * 2) the Field is optional (it doesn't matter what the Field's value is in this case).
   */
  public boolean isFilled() {
    return !this.required || isValid(this.value);
  }

  /**
   * Checks whether some other object is equal to this object.
   * @param o other object to compare
   * @return true if the other object has the same fields as this object.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Field<?> field = (Field<?>) o;
    return this.required == field.required &&
        Objects.equals(this.label, field.label) &&
        Objects.equals(this.value, field.value) &&
        Objects.equals(this.validator, field.validator);
  }

  /**
   * Generates a hashcode for the class
   * @return an integer value of hashcode
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.label, this.value, this.required, this.validator);
  }

  /**
   * Provides a string representation of the class
   * @return a string representation of the class
   */
  @Override
  public String toString() {
    return "Field{" +
        "label='" + this.label + '\'' +
        ", value=" + this.value +
        ", required=" + this.required +
        '}';
  }
}
