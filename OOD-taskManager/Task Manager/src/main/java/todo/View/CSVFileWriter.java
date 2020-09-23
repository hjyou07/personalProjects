package todo.View;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import todo.Model.TodoListWrapper;

/**
 * This class writes a CSV MasterCopy from the TodoListWrapper class while adding in the headers
 * associated with this list. Every time this method is called, a new overwitten CSV mastercopy
 * will be written
 */

public final class CSVFileWriter {

  /**
   * Empty constructor
   */
  private CSVFileWriter() {
  }

  /**
   * Helper method that returns a String representation of all of the headers.
   * @return a String representation of all of the headers.
   */
  private static String headerString() {
    return '\"' + "id" + "\"," +
        '\"' + "text" + "\"," +
        '\"' + "completed" + "\"," +
        '\"' + "due" + "\"," +
        '\"' + "priority" + "\"," +
        '\"' + "category" + "\"";
  }

/**
 * Writes a MasterCSV copy to an output file that is created based off the output directory
 * that is determined. This MasterCSV includes the header as well as the Todolist object
 * @throws IOException if the writing of the MasterCSV does occur properly
*/
  public static void writeMasterCSV(TodoListWrapper todoList, String filePath) throws IOException {
    File path = new File(filePath);
    if (!path.exists()) {
      path.getParentFile().mkdirs();
    }
    FileWriter fileWriter = new FileWriter(filePath);
    try (BufferedWriter outputFile = new BufferedWriter(fileWriter)) {
      List<String> contentsOfTodoList;
      List<String> fullTodoList = new ArrayList<>();
      // Adds the header list
      fullTodoList.addAll(Collections.singletonList(headerString()));
      // Adds the contents of the todoList
      contentsOfTodoList = Collections.singletonList(System.lineSeparator() + todoList.toString());
      fullTodoList.addAll(contentsOfTodoList);
      for (String todo : fullTodoList) {
        outputFile.write(todo);
      }
    } catch (IOException ioe) {
      System.out.println("Could not write into the file : " + ioe.getMessage());
      ioe.printStackTrace();
      System.exit(0);
    }
  }

}