package todo.Display;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import todo.Model.Todo;

/**
 * This class represents displaying the filtered TodoList after it has been processed and filtered
 * based off the unique filter commands passed in by the user on the master TodoList.
 */
public class FilteredDisplay {
  private IComparator comparator;
  private FilterCommands commands;
  private List<Todo> todoList;
  private List<Todo> copyList;

  /**
   * The constructor creates an object by passing in the unique FilterCommands object as well
   * as the master list of TodoList.
   * @param commands the FilterCommands object that includes the filter inputs from the user
   * @param todoList represents the master copy of the List of Todos
   */
  public FilteredDisplay(FilterCommands commands, List<Todo> todoList) {
    this.commands = commands;
    this.comparator = setComparator();
    this.todoList = todoList;
    this.copyList = new ArrayList<>();
    this.copyList.addAll(todoList);
  }

  /**
   * Sets a comparator between unique filters that can be passed in by the user
   * (date and/or priority)
   * @return the interface Comparator which implements a compare method.
   */
  private IComparator setComparator() {
    if (commands.isByDate()) {
      return new CompareByDate();
    } else if (commands.isByPriority()) {
      return new CompareByPriority();
    }
    return null;
  }

  /**
   * When user populates a display command, will return a copy of the filtered list/ and sorted
   * @return a list of To-do objects after filtering and sorting, if indicated.
   */
  public List<Todo> getTodoDisplayList() {
    this.filter(this.commands.isIncompleteOnly(), this.commands.getCategory());
    this.sort();
    return this.copyList;
  }

  /**
   * This method filters out the todoList and manipulates the list so that is removed the
   * individual todoObjects that are not appropriate for the filter
   * @param filterNotCompleted recognizes if the filter has already been set or not
   * @param category represents what to filter by
   */
  private void filter(boolean filterNotCompleted, String category) {
    if (category == null) {
      category = "|";
    }
    for (Todo todo : this.todoList) {
      boolean completed = todo.isCompleted();
      String todoCategory = todo.getCategory();
      if (!category.equals("|") && !category.equals(todoCategory)) {
        this.copyList.remove(todo);
      }
      if (filterNotCompleted && completed) {
        this.copyList.remove(todo);
      }
    }
  }

  /**
   * Sorts the list of To-do objects either by date or priority.
   */
  private void sort() {
    if (this.comparator != null) {
      this.copyList.sort(this.comparator);
    }
    // if comparator is null(when there's no sort command provided), do nothing
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
    FilteredDisplay that = (FilteredDisplay) o;
    return Objects.equals(this.comparator, that.comparator) &&
        Objects.equals(this.commands, that.commands) &&
        Objects.equals(this.copyList, that.copyList);
  }

  /**
   * Returns a hashCode representation  of this object.
   * @return a hashCode representation  of this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.comparator, this.commands, this.copyList);
  }

  /**
   * Returns a String representation of the object.
   * @return a String representation of the object.
   */
  @Override
  public String toString() {
    return "FilteredDisplay{" +
        "comparator=" + this.comparator +
        ", commands=" + this.commands +
        ", todoList=" + this.todoList +
        ", copyList=" + this.copyList +
        '}';
  }
}
