package todo.Display;

import todo.Model.Todo;

/**
 * This class creates an instance of the CompareByDate class that implements Comparator,
 * and compares the two Todos according to their due dates.
 */
public class CompareByDate implements IComparator {

  /**
   * Compares its two arguments for order.  Returns a negative integer, zero, or a positive integer
   * as the first argument is less than, equal to, or greater than the second.
   *
   * @param o1 the first object to be compared.
   * @param o2 the second object to be compared.
   * @return a negative integer, zero, or a positive integer as the first argument is less than,
   * equal to, or greater than the second.
   * @throws NullPointerException if an argument is null and this comparator does not permit null
   *                              arguments
   * @throws ClassCastException   if the arguments' types prevent them from being compared by this
   *                              comparator.
   */
  @Override
  public int compare(Todo o1, Todo o2) {
    // return positive if date1 is AFTER date2, negative if date1 is BEFORE date 2, 0 if equal
    if (o1.getDueDate() != null && o2.getDueDate() != null) {
      return o1.getDueDate().compareTo(o2.getDueDate());
    } else if (o1.getDueDate() == null && o2.getDueDate() != null) {
      return 1;
    } else if (o1.getDueDate() != null && o2.getDueDate() == null) {
      return -1;
    } else {
      return 0;
    }
  }
}
