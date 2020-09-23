package inputvalidation;

import java.util.Objects;

/**
   * This class assesses the validity of the password that is given as a string
   */
  public class PassValidator implements IValidator<String> {
    private Integer minimum;
    private Integer maximum;
    private Integer minimumLowercase;
    private Integer minimumUppercase;
    private Integer minimumDigits;

  /**
   * Constructs an instance of the PassValidator class that accepts five integers representing a
   * required minimum, required maximum, the minimum number of lowercase, minimum number of
   * uppercase and minimum number of digits allowed in a password. the latter 3 will be default
   * to 0 if no input is provided.
   * @param minimum is an integer that represents the required minimum number of characters
   * for the password
   * @param maximum is an integer that represents the required maximum number of characters
   * for the password
   * @param minimumLowercase is an integer that represents the minimum number of allowed
   * lowercase letter characters
   * for the password
   * @param minimumUppercase is an integer that represents the minimum number of allowed
   * uppercase letter characters
   * @param minimumDigits is an integer that represents the minimum number of allowed
   * digit characters
   */
    public PassValidator(Integer minimum, Integer maximum,
        Integer minimumLowercase, Integer minimumUppercase, Integer minimumDigits) {
      this.minimum = minimum;
      this.maximum = maximum;
      this.setMinimumLowercase(minimumLowercase);
      this.setMinimumUppercase(minimumUppercase);
      this.setMinimumDigits(minimumDigits);
    }


    /**
     * This method sets the minimum lowercase value of the password requirement.
     * If nothing is passed, set the default to 0
     * @param minimumLowercase is the integer of the minimum lowercase value
     */
    private void setMinimumLowercase(Integer minimumLowercase) {
      this.minimumLowercase = minimumLowercase != null ? minimumLowercase : Integer.valueOf(0);
    }

    /**
     * This method sets the minimum uppercase value of the password requirement.
     * If nothing is passed, set the default to 0
     * @param minimumUppercase is the integer of the minimum uppercase value
     */
    private void setMinimumUppercase(Integer minimumUppercase) {
      this.minimumUppercase = minimumUppercase != null ? minimumUppercase : Integer.valueOf(0);
    }

    /**
     * This method sets the minimum digits value of the password requirement.
     * If nothing is passed, set the default to 0
     * @param minimumDigits is the integer of the minimum digits value
     */
    private void setMinimumDigits(Integer minimumDigits) {
      this.minimumDigits = minimumDigits != null ? minimumDigits : Integer.valueOf(0);
    }

    /**
     * This method determines the size of the password string
     * @param input is the password string
     * @return an int representing the size of the input
     */
    private int size(String input) {
      return input.length();
    }

    /**
     * This method establishes if the input contains a space " "
     * @param input is the password being tested for a space
     * @return True is the input contains a space. Otherwise, return false
     */
    private boolean containsSpace(String input) {
      return (input.contains(" "));
    }

    /**
     * This method establishes an int array that creates the total number of lowercase
     * values, uppercase values and digit counts in the input. These values will be
     * found in the int array.
     * @param input is the string inpur being assessed.
     * @return the int array representing index 0- lowercaseCount, index1- uppercaseCount,
     * index2- digit count
     */
    private int[] amounts(String input) {
      char c;
      int lowerCount = 0;
      int upperCount = 0;
      int digitCount = 0;
      for (int i = 0; i < (input.length()); i++) {
        c = input.charAt(i);
        if (Character.isLowerCase(c)) {
          lowerCount++;
        } else if (Character.isUpperCase(c)) {
          upperCount++;
        } else if (Character.isDigit(c)) {
          digitCount++;
        }
      }
      return new int[]{digitCount, lowerCount, upperCount};
    }


    /**
     * Given a generic input of type String or boolean, returns true if the input is valid according
     * to specific validation methods and false otherwise.
     *
     * @param input - A generic object of either boolean or String type, representing the client
     *              input
     * @return true if the input is valid according to specific validation methods and false
     * otherwise.
     */
  @Override
  public Boolean isValid(String input) {
    int[] amounts = this.amounts(input);
    if (!containsSpace(input)) {
      if (((size(input) >= this.minimum) && (size(input) <= this.maximum))) {
        return (amounts[0] >= this.minimumDigits) && (amounts[1] >= this.minimumLowercase) && (
            amounts[2] >= this.minimumUppercase);
      }
      return false;
    }
    return false;
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
    PassValidator that = (PassValidator) o;
    return Objects.equals(this.minimum, that.minimum) &&
        Objects.equals(this.maximum, that.maximum) &&
        Objects.equals(this.minimumLowercase, that.minimumLowercase) &&
        Objects.equals(this.minimumUppercase, that.minimumUppercase) &&
        Objects.equals(this.minimumDigits, that.minimumDigits);
  }

  /**
   * Returns an int representing the hashcode of this object.
   * @return an int representing the hashcode of this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.minimum, this.maximum, this.minimumLowercase, this.minimumUppercase,
        this.minimumDigits);
  }

  /**
   * Returns a string representation of this object.
   * @return a string representation of this object.
   */
  @Override
  public String toString() {
    return "PassValidator{" +
        "minimum=" + this.minimum +
        ", maximum=" + this.maximum +
        ", minimum_lowercase=" + this.minimumLowercase +
        ", minimum_uppercase=" + this.minimumUppercase +
        ", minimum_digits=" + this.minimumDigits +
        '}';
  }
}