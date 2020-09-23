package inputvalidation;

import java.util.Objects;

/**
   * This class assesses the free text input.
   */
  public class FreeTextValidator implements IValidator<String> {
    private Integer numLines;
    private Integer numChar;

  /**
   * Constructs an instance of the FreeTextValidator class that accepts two integers representing a
   * number of lines and number of characters per line values that an input should have.
   * @param numLines is an integer that represents the number of lines allowed
   * @param numChar is an integer that represents the number of characters allowed per line
   */
    public FreeTextValidator(Integer numLines, Integer numChar) {
      this.numLines = numLines;
      this.numChar = numChar;
    }

    /**
     * This method calculates the total max space available based on the number of characters and lines
     * @return an integer of the max space possible
     */
    private Integer maxSpace() {
      int maxSpace;
      maxSpace = this.numLines * this.numChar;
      return maxSpace;
    }

    /**
     * This method calculates the size of the input via length
     * @param input is the string being assessed
     * @return an int representing the length of input
     */
    private Integer size(String input) {
      return input.length();
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
    return (size(input) <= maxSpace());
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
    FreeTextValidator that = (FreeTextValidator) o;
    return Objects.equals(this.numLines, that.numLines) &&
        Objects.equals(this.numChar, that.numChar);
  }

  /**
   * Returns an int representing the hashcode of this object.
   * @return an int representing the hashcode of this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.numLines, this.numChar);
  }

  /**
   * Returns a string representation of this object.
   * @return a string representation of this object.
   */
  @Override
  public String toString() {
    return "FreeTextValidator{" +
        "numLines=" + this.numLines +
        ", numChar=" + this.numChar +
        '}';
  }
}
