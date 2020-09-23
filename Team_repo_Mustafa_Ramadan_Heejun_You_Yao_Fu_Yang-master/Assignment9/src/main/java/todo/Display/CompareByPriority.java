package todo.Display;

import todo.Model.Todo;

/**
 * Creates an instance of the CompareByPriority class that implements Comparator,
 * and compares the two Todos according to their priority value.
 */
public class CompareByPriority implements IComparator {

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
    // return positive integer if o1's priority value is greater than that of o2's,
    return o1.getPriority() - o2.getPriority();
  }
}
