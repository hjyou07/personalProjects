package todo.Controller;

import java.io.IOException;
import todo.Controller.Util.InvalidInputCombinationException;
import todo.Controller.Util.ProperInputInstructions;

/**
 * A driver class where provides the entrance for the user to this program.
 * Utilizes the controller class, RunTask to invoke operations.
 */
public class Main {

  public static void main(String[] args) throws IOException, InvalidInputCombinationException {
    CommandLineParser CLPParser = null;
    try {
      CLPParser = new CommandLineParser(args);
    } catch (InvalidInputCombinationException e) {
      ProperInputInstructions.printInputInstructions();
      System.exit(0);
    }
    RunTask controller = new RunTask(CLPParser);
    // sequential loading occurs
    controller.loadExistingTodo();
    try {
      controller.addNewTodo();
    } catch (InvalidInputCombinationException e) {
      System.out.println("this is supposed to check either valid Date or Priority format");
      System.exit(0);
    }
    controller.updateCompletedTodo();
    // these method contains the --display command
    try {
      controller.displayFilteredTodo();
    } catch (InvalidInputCombinationException e) {
      System.out.println("Cannot sort by both data and priority.\nPlease provide only one sorting criteria");
      System.exit(0);
    }
    controller.writeToCSV();

  }
}
