package todo.Display;

import java.util.Objects;
import todo.Controller.Util.InvalidInputCombinationException;

/**
 * Creates an instance of the DisplayFilter class that contains four fields: a boolean representing
 * to display incomplete to-dos only, a String representing to display by a particular category
 * only, a boolean representing to sort the to-dos by date, or a boolean representing to sort the
 * to-dos by priority. Contains getters for all four fields.
 */
public class FilterCommands {
  private boolean incompleteOnly;
  private String category;
  private boolean byDate;
  private boolean byPriority;

  /**
   * Constructs an instance of the FilterCommands class with the following fields.
   * @param incompleteOnly - boolean representing to display only incomplete tasks.
   * @param category - String representing to display only the tasks of a category.
   * @param byDate - boolean representing to sort by date.
   * @param byPriority - boolean representing to sort by priority.
   * @throws InvalidInputCombinationException - Thrown when both byDate and byPriority are true.
   */
  public FilterCommands(boolean incompleteOnly, String category, boolean byDate,
      boolean byPriority) throws InvalidInputCombinationException {
    this.checkArgs(byDate, byPriority);
    this.incompleteOnly = incompleteOnly;
    this.category = category;
    this.byDate = byDate;
    this.byPriority = byPriority;
  }

  /**
   * Helper function to check constructor parameters. Throws exception if input is invalid.
   * @param date - boolean representing to sort by date.
   * @param priority - boolean representing to sort by priority.
   * @throws InvalidInputCombinationException - thrown when both date and priority are true.
   */
  private void checkArgs(boolean date, boolean priority) throws InvalidInputCombinationException {
    if (date && priority) {
     throw new InvalidInputCombinationException("can't provide more than one sorting criteria");
    }
  }

  /**
   * Returns a boolean representing the field incompleteOnly
   * @return a boolean representing the field incompleteOnly
   */
  public boolean isIncompleteOnly() {
    return this.incompleteOnly;
  }

  /**
   * Returns a String representing the field category.
   * @return a String representing the field category.
   */
  public String getCategory() {
    return this.category;
  }

  /**
   * Returns a boolean representing the field byDate
   * @return a boolean representing the field byDate
   */
  public boolean isByDate() {
    return this.byDate;
  }

  /**
   * Returns a boolean representing the field byPriority
   * @return a boolean representing the field byPriority
   */
  public boolean isByPriority() {
    return this.byPriority;
  }

  /**
   * Returns true if the object being compared is equal to this object; false otherwise.
   * @param o - The other object being compared.
   * @return true if the object being compared is equal to this object; false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    FilterCommands that = (FilterCommands) o;
    return this.isIncompleteOnly() == that.isIncompleteOnly() &&
        this.isByDate() == that.isByDate() &&
        this.isByPriority() == that.isByPriority() &&
        Objects.equals(this.getCategory(), that.getCategory());
  }

  /**
   * Returns a hashCode representation  of this object.
   * @return a hashCode representation  of this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(isIncompleteOnly(), getCategory(), isByDate(), isByPriority());
  }

  /**
   * Returns a String representation of the object.
   * @return a String representation of the object.
   */
  @Override
  public String toString() {
    return "DisplayFilter{" +
        "incompleteOnly=" + this.incompleteOnly +
        ", category='" + this.category + '\'' +
        ", byDate=" + this.byDate +
        ", byPriority=" + this.byPriority +
        '}';
  }
}
