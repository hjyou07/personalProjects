package todo.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import todo.Controller.Util.InvalidInputCombinationException;

/**
 * This class creates a data structure for what a TodoObject includes. These include all of the
 * fields that make a TodoObject a TodoObject
 */
public class Todo {
  private static final int LOWEST_PRIORITY = 3;
  private static final int HIGHEST_PRIORITY = 1;
  private Integer id;
  private boolean idFixed;
  private String text;
  private boolean completed;
  private Date dueDate;
  private Integer priority;
  private String category;

  /**
   * Thie constructor created the TodoObject based on the fields that are either required or
   * optional
   * @param text is a string that is required. Will never be null
   * @param completed is a boolean that determined if that individual TodoObject is complete or not
   * @param due is a string that represents when the TodoObject is due
   * @param priority is a string that represents the severity of the todoObject
   * @param category is a string representing what type of todoObject it is
   * @throws InvalidInputCombinationException
   */
  public Todo(String text, boolean completed, String due, String priority, String category)
      throws InvalidInputCombinationException {
    this.id = null;
    this.idFixed = false;
    this.text = text;
    this.completed = completed;
    this.dueDate = validateDueDate(due);
    this.priority = validatePriority(priority);
    this.category = category;
  }


  /**
   * This method sets the completed value as true. This method will be called if the user commands
   * --complete-todo <id>
   */
  public void setCompleted() {
    this.completed = true;
  }

  /**
   * Getter method to retrieve the text
   * @return a string for text
   */
  public String getText() {
    return this.text;
  }

  public Integer getId() {
    return id;
  }

  /**
   * Getter method to retrieve the completed status
   * @return a boolean for completed
   */
  public boolean isCompleted() {
    return this.completed;
  }

  /**
   * Getter method to retrieve the due date
   * @return a string for due date
   */
  public Date getDueDate() {
    return this.dueDate;
  }

  /**
   * Getter method to retrieve the integer version of the priority
   * @return an Integer representing the priority.
   */
  public Integer getPriority() {
    return this.priority;
  }

  /**
   * Getter method to retrieve the category
   * @return a string for category
   */
  public String getCategory() {
    return this.category;
  }

  /**
   * This method sets the ID argument for the individual object being created.
   * An integer ID that represents the count passed by the todoList wrapper class. There,
   * the count is set equal to the todolist.size() of the list of these objects.
   * @param count is the id number to be set passed in, from the TodoList class.
   */
  protected void setId(Integer count) {
    if (!idFixed) {
      this.id = count;
      this.idFixed = true;
    } else {
      System.out.println("there's already an id for this Todo. can't modify it");
      // if idFixed is true, can't set the id
    }
  }

  /**
   * Given a String representing a potential due date, returns a Date object representing the date
   * if the input was valid and throws an exception otherwise. Returns null if input was null.
   * @param due - String representing a possible due date.
   * @return a Date object representing the date or null if input was null.
   * @throws InvalidInputCombinationException - Thrown when the input was not null and is not a
   * date of the proper format.
   */
  private Date validateDueDate(String due) throws InvalidInputCombinationException {
    if (due != null) {
      Date dueDate;
      try {
        dueDate = new SimpleDateFormat("MM/dd/yyyy").parse(due);
        return dueDate;
      } catch (ParseException e) {
        throw new InvalidInputCombinationException("Due date format should be mm/dd/yyyy");
      }
    }
    return null;
  }

  /**
   * Given a String representing the priority of the to-do task, returns an Integer representing
   * the priority. Returns a default priority of 3 if value is null; throws an exception if the
   * String is not a valid number or not within the priority range.
   * @param priority - String representing the potential priority of the task.
   * @return an Integer representing the priority or a default priority of 3 if input is null.
   * @throws InvalidInputCombinationException - thrown when the String is not a valid number or
   * not within the expected priority range.
   */
  private Integer validatePriority(String priority) throws InvalidInputCombinationException,
      NumberFormatException {
    if (priority == null) {
      return LOWEST_PRIORITY + 1; // assign even lower priority, internally. User won't know priority of 4 exists
    }
    int intPriority = 0;
    // Check that the priority can be converted to a number.
    try {
      intPriority = Integer.parseInt(priority);
    } catch (NumberFormatException e) {
      throw new InvalidInputCombinationException("A specified priority must be between 1-3.");
    }
    // Check that the number is between 1-3, throws Exception otherwise.
    if (intPriority < HIGHEST_PRIORITY || intPriority > LOWEST_PRIORITY) {
      System.out.println("A specified priority must be between 1-3");
      // System.exit(0);
      // if I exit before throwing exception, isn't this not reachable..?
      throw new InvalidInputCombinationException("A specified priority must be between 1-3.");
    }
    return intPriority;
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
    Todo todo = (Todo) o;
    return this.isCompleted() == todo.isCompleted() &&
        Objects.equals(this.getText(), todo.getText()) &&
        Objects.equals(this.getDueDate(), todo.getDueDate()) &&
        Objects.equals(this.getPriority(), todo.getPriority()) &&
        Objects.equals(this.getCategory(), todo.getCategory());
  }

  /**
   * Returns a hashCode representation  of this object.
   * @return a hashCode representation  of this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.getText(), this.isCompleted(), this.getDueDate(), this.getPriority(),
        this.getCategory());
  }

  /**
   * This helper method formats the object Date to '?' if null and 'MM/dd/YYYY/ if not null
   * @return a string that fits this pattern
   */
  private String dateFormatting() {
    Date date = this.dueDate;
    return (date == null ? "?" : new SimpleDateFormat("MM/dd/YYYY").format(date));
  }

  /**
   * This helper method formats the priority to '?' if null and a priority.toString() if not null
   * @return a string that fits this pattern
   */
  private String priorityFormatting() {
    Integer priority = this.priority;
    return (priority > LOWEST_PRIORITY ? "?" : priority.toString());
  }

  /**
   * This helper method formats the category to '?' if null and a category if not null
   * @return a string that fits this pattern
   */
  private String categoryFormatting() {
    String category = this.category;
    return (category == null ? "?" : category);
  }

  /**
   * Returns a String representation of the object.
   * @return a String representation of the object.
   */
  @Override
  public String toString() {
    return '\"' + String.valueOf(this.id) + "\"," + // toString initializes with id = 35, and even if I setId(1), it prints 35.. what the fuck?
        '\"' + this.text + "\"," +
        '\"' + this.completed + "\"," +
        '\"' + this.dateFormatting() + "\"," +
        '\"' + this.priorityFormatting() + "\"," +
        '\"' + this.categoryFormatting() + "\"";
  }

}