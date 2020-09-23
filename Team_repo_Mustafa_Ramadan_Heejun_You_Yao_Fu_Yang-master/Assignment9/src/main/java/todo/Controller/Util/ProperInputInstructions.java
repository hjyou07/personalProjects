package todo.Controller.Util;

/**
 * A final class with a static method that prints out proper input instructions.
 * For utility use only.
 */
public final class ProperInputInstructions {

  /**
   * Empty constructor for a static class and method.
   */
  private ProperInputInstructions() {
  }

  /**
   * This static class prints out the instructions to the client illustrating what is expected
   * from the client when passing in arguments, and what makes an argument valid or not
   */
  public static void printInputInstructions() {
    String instructions = "--csv-file <path/to/file>\n"
        + "    The CSV file containing the todos. Required argument.\n"
        + "--add-todo\n"
        + "    Add a new todo. If this option is provided, then must also provide --todo-text \n"
        + "--todo-text <description of todo>\n"
        + "    A description of the todo.\n"
        + "--completed (Optional)\n"
        + "    Sets the completed status of a new todo to true.\n"
        + "--due <due date> (Optional)\n"
        + "    Sets the due date of a new todo. Must be this format: MM/DD/YYYY\n"
        + "--priority <1, 2, or 3> (Optional)\n"
        + "    Sets the priority of a new todo. The value can be 1, 2, or 3.\n"
        + "--category <a category name> (Optional)\n"
        + "    Sets the category of a new todo. The value can be any String.\n"
        + "    Categories do not need to be pre-defined.\n"
        + "--complete-todo <id>\n"
        + "    Mark the Todo with the provided ID as complete.\n"
        + "--display\n"
        + "    Display todos. If none of the following optional arguments are provided,\n"
        + "    displays all todos.\n"
        + "--show-incomplete (Optional)\n"
        + "    If --display is provided, only incomplete todos will be displayed.\n"
        + "--show-category <category> (Optional)\n"
        + "    If --display is provided, only displays todos with the given category.\n"
        + "--sort-by-date (Optional)\n"
        + "    If --display is provided, sort the todos by ascending date order.\n"
        + "    Cannot be combined with --sort-by-priority.\n"
        + "--sort-by-priority (Optional)\n"
        + "    If --display is provided, sort the todos by ascending priority order.\n"
        + "    Cannot be combined with --sort-by-date.\n"
        + "<> Operators indicate a required second argument.\n";
    System.out.println(instructions);
  }
}
