package todo.Controller.Util;

/**
 * This class CSV parser, given a String line, will parse and save the data as an array of tokens.
 * Simulates a static utility class.
 */
public final class CSVParser {

  private CSVParser() {
  }

  /**
   * Parses a given String line and save the data as an array of tokens
   * @param line a String line from a CSV file
   * @return a String array that contains data tokens from the line
   */
  public static String[] ParseCSVLine (String line) {
    String[] dataArray = line.split("\",\"");
//    // if I parse with "," I need to slice the first token to eliminate the starting "
//    // e.g. "first_name -> first_name
    dataArray[0] = dataArray[0].substring(1);
//    // if I parse with ",", I need to slice the last token to eliminate the trailing "
//    // e.g. last_name" -> last_name
    int lastElemIndex = dataArray.length - 1;
    int lastTokenLength = dataArray[lastElemIndex].length() - 1;
    dataArray[lastElemIndex] = dataArray[lastElemIndex].substring(0, lastTokenLength);
    return dataArray;
  }
}
