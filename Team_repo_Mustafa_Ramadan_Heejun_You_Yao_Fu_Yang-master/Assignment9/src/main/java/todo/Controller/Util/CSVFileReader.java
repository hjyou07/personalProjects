package todo.Controller.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * This class reads the CSV file that is provided and outputs a list of strings representing
 * the contents of the CSV file
 */
public final class CSVFileReader {

  private CSVFileReader() {
  }

  /**
   * This method reads from the CSV file that is provided. And returns the outputs of the file into
   * List of strings
   *
   * @return a list of strings representing a masterCopy of the Todos compilation
   * @throws IOException if the file is empty, and thus, will access another method to create a
   *                     filename and add the header to that file
   */
  public static List<String> readFromCSV(String filePath) throws IOException {
    List<String> existingTodo = null;
    try {
      existingTodo = Files.readAllLines(Paths.get(filePath));
    } catch (IOException ioe) {
      System.out.println("File not found");
    }
    // returns List<String> if file is read, returns null if file not found
    return existingTodo;
  }
}