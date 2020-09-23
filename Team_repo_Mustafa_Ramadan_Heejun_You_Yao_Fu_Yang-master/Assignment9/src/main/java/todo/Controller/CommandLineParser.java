package todo.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import todo.Display.FilterCommands;
import todo.Controller.Util.InvalidInputCombinationException;
import todo.Model.Todo;

/**
 * This class represents the command line parser which takes in the values passed in from the
 * client, and parses them out into their appropriate location so that they can further processed
 * and validated across the program.
 */

public class CommandLineParser {
  private static final String DELIMITER = "|";
  // this groups the commands that comes with a value (path to the file, description, date, etc)
  private static final ArrayList<String> COMMANDS_WITH_PAIR = new ArrayList<>(Arrays.asList(
      "--csv-file", "--todo-text", "--due", "--priority", "--category", "--show-category",
      "--complete-todo"));
  // this groups the commands that have to come together. if one command exists, the other must exist
  private static final String[] PAIRED_KEYWORDS = {"--add-todo", "--todo-text"};
  // this groups the commands that MUST exist in the argument
  // one element for now, we can simply add to this list if we want to add more commands in this category
  private static final String[] REQUIRED_ARGUMENTS = {"--csv-file"};
  // this groups the commands that allows multiple invocation, allows extensibility
  private static final String[] DUPLICATE_COMMANDS = {"--complete-todo"};
  private static final String[] TODO_COMMANDS = {"--todo-text", "--completed", "--due",
      "--priority", "--category"};
  private static final String[] DISPLAY_COMMANDS = {"--show-incomplete", "--show-category",
      "--sort-by-date", "--sort-by-priority"};
  private String allCommands;
  private HashMap<String, String> pairedCommands;
  private String duplicateCommands;
  private ArrayList<String> completedIDs;
  private String[] allPairedCommands;

  /**
   * Constructs an instance of the CommandLineParser class that...
   * @param commands - String array representing the command line arguments.
   * @throws InvalidInputCombinationException - Thrown when there is malformed input from a command
   * line perspective.
   */
  public CommandLineParser(String[] commands) throws InvalidInputCombinationException {
    this.checkNull(commands);
    this.duplicateCommands = this.concatenateArgs(DUPLICATE_COMMANDS);
    this.allCommands = this.concatenateArgs(commands);
    this.completedIDs = new ArrayList<>();
    this.pairedCommands = new HashMap<>();
    this.allPairedCommands = new String[commands.length];
    this.extractAllPairedArguments(commands);
    this.validateArgs();
  }

  /**
   * Checks the command line arguments and throws an exception for malformed input combinations.
   * @throws InvalidInputCombinationException - thrown when the command arguments are malformed.
   */
  private void validateArgs() throws InvalidInputCombinationException {
    // Check required matching keywords first.
    final int INCREMENT = 2;
    for (int i = 0; i < PAIRED_KEYWORDS.length; i += INCREMENT) {
      if (!this.checkMatchingKeywords(PAIRED_KEYWORDS[i], PAIRED_KEYWORDS[i + 1])) {
        throw new InvalidInputCombinationException("Missing required matching keyword.");
      }
    }

    // Check duplicates and get paired mapping.
    int index = 0;
    String word = this.allPairedCommands[index++];
    while (word != null) {
      this.checkPairedArguments(word);
      word = this.allPairedCommands[index++];
    }

    // Check required argument.
    for (String keyword : REQUIRED_ARGUMENTS) {
      if (!this.pairedCommands.containsKey(keyword)) {
        throw new InvalidInputCombinationException("Missing required keyword argument.");
      }
    }
  }

  /**
   * Returns an ArrayList<\String> representing the completed todoIds.
   * @return an ArrayList<\String> representing the completed todoIds.
   */
  public ArrayList<String> getCompletedIDs() {
    return this.completedIDs;
  }

  /**
   * Returns A HashMao<\String, String> with command line keywords as keys, and their required
   * second argument as the values.
   * @return A HashMao<\String, String> with command line keywords as keys, and their required
   * second argument as the values.
   */
  public HashMap<String, String> getPairedCommands() {
    return this.pairedCommands;
  }

  /**
   * Returns a new instance of the To-do class if commanded; returns false otherwise.
   * @return a new instance of the To-do class if commanded; returns false otherwise.
   * @throws InvalidInputCombinationException - thrown when the arguments passed to the constructor for
   * a To-do object are invalid.
   */
  public Todo getNewTodo() throws InvalidInputCombinationException {
    if (!this.allCommands.contains("--add-todo")) {
      return null;
    }
    int index = 0;
    return new Todo(this.pairedCommands.get(TODO_COMMANDS[index++]),
        this.inArgs(TODO_COMMANDS[index++]), this.getFromMap(TODO_COMMANDS[index++]),
        this.getFromMap(TODO_COMMANDS[index++]), this.getFromMap(TODO_COMMANDS[index]));
  }

  /**
   * Returns an instance of the FilterCommands object if commanded; false otherwise.
   * @return an instance of the FilterCommands object if commanded; false otherwise.
   * @throws InvalidInputCombinationException - Thrown when the arguments passed to the constructor
   * of FilterCommands are invalid.
   */
  public FilterCommands getFilterCommands() throws InvalidInputCombinationException {
    if (!this.allCommands.contains("--display")) {
      return null;
    }
    int index = 0;
    return new FilterCommands(
        this.inArgs(DISPLAY_COMMANDS[index++]), this.getFromMap(DISPLAY_COMMANDS[index++]),
        this.inArgs(DISPLAY_COMMANDS[index++]), this.inArgs(DISPLAY_COMMANDS[index]));
  }

  /**
   * Helper method
   * Throws an exception if the provided String array is null.
   * @param commands - String array representing commands; might be null.
   * @throws InvalidInputCombinationException - Thrown if String array is null.
   */
  private void checkNull(String[] commands) throws InvalidInputCombinationException {
    if (commands == null) {
      throw new InvalidInputCombinationException("Cannot have a null argument collection.");
    }
  }

  /**
   * Helper method. Returns the command line arguments as a String with '|' as the delimiter.
   * @return - returns the commands line arguments as a String with '|' as the delimiter.
   */
  private String concatenateArgs(String[] commands) {
    String fullString = commands[0] + DELIMITER;
    for (int i = 1; i < commands.length - 1; i++) {
      String segment = commands[i];
      if (segment.contains("--")) {  // A keyword, should have delimiter.
        fullString = fullString + DELIMITER + commands[i] + DELIMITER;
      } else if (commands[i + 1].contains("--")) {  // Next word is a keyword, avoid trailing space.
        fullString = fullString + segment;
      } else {  // Next word is not a keyword, can include trailing space.
        fullString = fullString + segment + " ";
      }
    }

    // Dealing with the last case since above code accesses the next element over (avoid IndexOutOfBounds)
    if (commands.length != 1) {
      String finalSegment = commands[commands.length - 1];
      if (finalSegment.contains("--")) {  // Last arg = keyword
        fullString = fullString + DELIMITER + finalSegment + DELIMITER;
      } else {
        fullString = fullString + finalSegment + DELIMITER;
      }
    }
    return fullString;
  }

  /**
   * Helper method to extract all the arguments that are expecting a secondary argument. Includes
   * all of the duplicates to enable validation method to check and throw exception properly.
   * @param collection - String[] representing the collection of arguments.
   */
  private void extractAllPairedArguments(String[] collection) {
    int index = 0;
    for (String argument : collection) {
      if (COMMANDS_WITH_PAIR.contains(argument)) {
        this.allPairedCommands[index++] = argument;
      }
    }
  }

  /**
   * Helper method. Given two Strings representing keywords, returns false if only one of the
   * keywords are found in the command arguments, and true otherwise.
   * @param keyword1 - String representing a keyword that requires a matching set.
   * @param keyword2 - String representing a keyword that requires a matching set.
   * @return false if only one keyword is found in the command arguments, and true otherwise.
   */
  private boolean checkMatchingKeywords(String keyword1, String keyword2) {
    // String delimiter = String.format("\\%s", DELIMITER);
    // String pattern = keyword2 + delimiter + "<?[^" + delimiter + "]+>?" + delimiter;
    Matcher m1 = Pattern.compile(keyword1).matcher(this.allCommands);
    Matcher m2 = Pattern.compile(keyword2).matcher(this.allCommands);
    return (m1.find() == m2.find());
  }

  /**
   * Helper method. Given a potential keyword, throws an exception if it does not have an
   * appropriate second argument in association.
   * @param keyword - String representing a keyword that requires an associated second argument.
   * @throws InvalidInputCombinationException - Thrown when the keyword does not have an associated
   * second argument.
   */
  private void checkPairedArguments(String keyword) throws InvalidInputCombinationException {
    String delimiter = String.format("\\%s", DELIMITER);
    String pattern = keyword + delimiter + "<?([^-" + delimiter + "]+)>?" + delimiter;
    Matcher m1 = Pattern.compile(keyword).matcher(this.allCommands);
    Matcher m2 = Pattern.compile(pattern).matcher(this.allCommands);
    if (m1.find() && m2.find()) {  // Has both keyword and second argument.
      if (this.duplicateCommands.contains(keyword)) {  // Accepted duplicate command.
        String captureGroup = m2.group(1);  // Get fist case.
        for (int i = 0; i < this.completedIDs.size(); i++) {
          if (m2.find()) {  // If there are more duplicate commands, gets next available one.
            captureGroup = m2.group(1);
          }
        }
        this.completedIDs.add(captureGroup);  // Should add the next unique ID to ArrayList.
      } else if (this.pairedCommands.containsKey(keyword)) {  // Should not have duplicates.
        throw new InvalidInputCombinationException("Unsupported duplicate commands.");
      } else {  // Save to HashMap for later use.
        this.pairedCommands.put(keyword, m2.group(1));
      }
    } else {
      throw new InvalidInputCombinationException("Missing required first/second argument.");
    }
  }

  /**
   * Helper method. Given a String representing a potential command, returns true if this was passed
   * in the original command line args and false otherwise.
   * @param command - String representing a potential command line argument.
   * @return true if this was passed in the original command line args and false otherwise.
   */
  private boolean inArgs(String command) {
    return this.allCommands.contains(command);
  }

  /**
   * Helper method. Given a String argument representing a potential key in the map, returns
   * the value if there is a matching key. Returns null otherwise.
   * @param key - String representing a potential key.
   * @return the value if there is a matching key. Returns null otherwise.
   */
  private String getFromMap(String key) {
    return this.pairedCommands.getOrDefault(key, null);
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
    CommandLineParser that = (CommandLineParser) o;
    return Objects.equals(this.getPairedCommands(), that.getPairedCommands()) &&
        Objects.equals(this.getCompletedIDs(), that.getCompletedIDs()) &&
        Objects.equals(this.allCommands, that.allCommands);
  }

  /**
   * Returns a hashCode representation  of this object.
   * @return a hashCode representation  of this object.
   */
  @Override
  public int hashCode() {
    return Objects
        .hash(this.allCommands, this.getPairedCommands(), this.getCompletedIDs());
  }

  /**
   * Returns a String representation of the object.
   * @return a String representation of the object.
   */
  @Override
  public String toString() {
    return "CommandLineParser{" +
        "allCommands='" + this.allCommands + '\'' +
        ", pairedCommands=" + this.pairedCommands +
        ", completedIDs=" + this.completedIDs +
        '}';
  }
}