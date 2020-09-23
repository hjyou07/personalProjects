package automation;

import java.util.HashMap;

/**
 * CSV parser, given a String line, will parse and save the data as an array of tokens.
 * This class can also generate a hash so that we can later match the "placeholder"
 * to the actual "data" that pertains to it.
 */
public final class CSVParser extends Object {

  private CSVParser() {
  }

  /**
   * Parses a given String line and save the data as an array of tokens
   * @param line a String line from a CSV file
   * @return a String array that contains data tokens from the line
   */
  protected static String[] ParseCSVLine (String line) {
    String[] dataArray = line.split("\",\"");
    // if I parse with "," I need to slice the first token to eliminate the starting "
    // e.g. "first_name -> first_name
    dataArray[0] = dataArray[0].substring(1);
    // if I parse with ",", I need to slice the last token to eliminate the trailing "
    // e.g. last_name" -> last_name
    int lastElemIndex = dataArray.length - 1;
    int lastTokenLength = dataArray[lastElemIndex].length() - 1;
    dataArray[lastElemIndex] = dataArray[lastElemIndex].substring(0, lastTokenLength);
    return dataArray;
  }

  /**
   * Creates a hash map of headers as String keys and according indices as Integer values.
   * We can use this hash map to later replace the "placeholder"
   * to the actual "data" that pertains to it, by first grabbing the value of the key,
   * then accessing the [value]th element in the data array.
   * @param headerLine the very first String line from a CSV file
   * @return a hash map of headers and their indices
   */
  protected static HashMap<String, Integer> CreateHeaderIndex(String headerLine) {
    // call ParseCSV on the very first line of the data file (header line)
    String[] headers = ParseCSVLine(headerLine);
    HashMap<String, Integer> headerMap = new HashMap<>();
    int i;
    for (i = 0; i < headers.length; i++) {
      headerMap.put(headers[i], i);
    }
    return headerMap;
  }
}