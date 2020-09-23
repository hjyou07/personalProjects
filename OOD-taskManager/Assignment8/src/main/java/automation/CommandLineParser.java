package automation;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.*;

// takes in a String array (will be inputted later from command arguments)
// then create one whole string to find a particular regex combo needed
// By doing this, we believe that this would allow us to
// 1. not be dependent on the order of the command line arguments,
// 2. reduce potential redundancy by being able to run
//    different regex search on a single variable,
// 3. Still let us work with one variable if there were more arguments/options are added
//    e.g. some other communication is added other than email and letter

/**
 * Creates an instance of the CommandLineParser class which accepts a String array representing the
 * arguments that were passed via the command line. Contains fields including a String array of the
 * commands, a String representing a modified concatenation of the commands with '|' as the
 * delimiter, a String representing the outputFolder pathway, and a String representing the
 * csvfile pathway (to read from). Contains methods to get the csvfile pathway, the output folder
 * pathway, an email template pathway, and letter template pathway.
 */
public class CommandLineParser {
  private String[] commands;
  private String allCommands;
  private String outputFolder;
  private String csvPath;
  private static final Integer MIN_ARGUMENTS = 7;
  private static final String[] TEMPLATE_TYPES = {"--email", "--letter"};
  private static final String DELIMITER = "|";

  /**
   * Constructs an instance of the automation.CommandLineParser class. Accepts a String array of commands
   * as its only parameter. Throws Exception if arguments do not satisfy requirements.
   * @param commands - String array representing the command line arguments to be processed.
   * @throws InvalidInputCombinationException - Thrown when the command line arguments are null or
   * do not satisfy the minimum length requirement, or have an invalid combination, such as missing
   * required arguments or having mismatched templates.
   */
  public CommandLineParser(String[] commands) throws InvalidInputCombinationException {
    this.checkNullAndLength(commands);
    this.allCommands = this.concatenateArgs(commands);
    this.commands = commands;
    this.outputFolder = "";
    this.csvPath = "";
    this.validateArgs();
  }

  /**
   * Returns a String array representing the commands.
   * @return a String array representing the commands.
   */
  public String[] getCommands() {
    return this.commands;
  }

  /**
   * Helper method.
   * Checks that the command line arguments have all of the required arguments and matching
   * template-types to templates. Throws an exception otherwise.
   * @throws InvalidInputCombinationException - thrown when the command line arguments are
   * missing a required argument or have an unmatched template-type or template.
   */
  public void validateArgs() throws InvalidInputCombinationException {
    if (!(this.checkCSVArgument() && this.checkOutputArgument())) {
      throw new InvalidInputCombinationException("Missing required arguments.");
    }
    for (String templateType : TEMPLATE_TYPES) {
      if (!this.checkMatchingTemplate(templateType)) {
        throw new InvalidInputCombinationException("At least one template is missing/unmatched.");
      }
    }
  }

  /**
   * Helper method
   * Throws an exception if the provided String array is null or less than required length.
   * @param commands - String array representing commands; might be null.
   * @throws InvalidInputCombinationException - Thrown if String array is null or less than the
   * required length.
   */
  private void checkNullAndLength(String[] commands) throws InvalidInputCombinationException {
    if (commands == null || commands.length < MIN_ARGUMENTS) {
      throw new InvalidInputCombinationException("Cannot be null and requires at least " +
          MIN_ARGUMENTS.toString() + " command line inputs.");
    }
  }

  /**
   * Helper method.
   * Concatenates and returns the commands line arguments as a string with '|' as the delimiter.
   * @return - returns the commands line arguments as a string with '|' as the delimiter.
   */
  private String concatenateArgs(String[] commands) {
    String fullString = commands[0];
    for (int i = 1; i < commands.length; i++) {
      fullString = fullString + DELIMITER + commands[i];
    }
    return fullString + DELIMITER;
  }

  /**
   * Helper method.
   * Given a String representing the template-type requiring a matching template. Returns true if
   * both the template-type and template exist, or neither exist. Returns false otherwise.
   * @param pattern1 - String representing the template-type requiring a matching template.
   * @return true if both the template-type and template exist, or neither exist. Returns false
   * otherwise.
   */
  private boolean checkMatchingTemplate(String pattern1) {
    String formattedDelimiter = String.format("\\%s", DELIMITER);
    String pattern2 = pattern1 + "-template" + formattedDelimiter + "<?([^" + formattedDelimiter +
        "]+)>?" + formattedDelimiter;
    Matcher m1 = Pattern.compile(pattern1 + formattedDelimiter).matcher(this.allCommands);
    Matcher m2 = Pattern.compile(pattern2).matcher(this.allCommands);
    return (m1.find() == m2.find());
  }

  /**
   * Returns true if the required CSV file path was provided, false otherwise.
   * @return true if the required CSV file path was provided, false otherwise.
   */
  private boolean checkCSVArgument() {
    String formattedDelimiter = String.format("\\%s", DELIMITER);
    String pattern = "--csv-file" + formattedDelimiter + "<?([^" + formattedDelimiter + "]+)>?"
        + formattedDelimiter;
    Matcher m = Pattern.compile(pattern).matcher(this.allCommands);
    if (m.find()) {
      this.csvPath = m.group(1);
      return true;
    }
    return false;
  }

  /**
   * Returns true if the required output folder path was provided, false otherwise.
   * @return true if the required output folder path was provided, false otherwise.
   */
  private boolean checkOutputArgument() {
    String formattedDelimiter = String.format("\\%s", DELIMITER);
    String pattern = "--output-dir" + formattedDelimiter + "<?([^" + formattedDelimiter + "]+)>?"
        + formattedDelimiter;
    Matcher m = Pattern.compile(pattern).matcher(this.allCommands);
    if (m.find()) {
      this.outputFolder = m.group(1);
      return true;
    }
    return false;
  }

  /**
   * Given a String representing the template-type requiring a matching template, returns a String
   * representing the file pathway of the template.
   * @param pattern - String representing the template-type requiring a matching template.
   * @return a String representing the file pathway of the template.
   */
  public String getTemplatePath(String pattern) {
    String formattedDelimiter = String.format("\\%s", DELIMITER);
    String template = "-template" + formattedDelimiter + "<?([^" + formattedDelimiter + "]+)>?"
        + formattedDelimiter;
    Matcher m = Pattern.compile(pattern + template).matcher(this.allCommands);
    if (m.find()) {
      return m.group(1);
    } else {
      return null;
    }
  }

  /**
   * Returns a String representing the output directory folder.
   * @return a String representing the output directory folder.
   */
  public String getOutputFolder() {
    return this.outputFolder;
  }

  /**
   * Returns a String representing the CSV file pathway.
   * @return a String representing the CSV file pathway.
   */
  public String getCSVPath() {
    return this.csvPath;
  }

  /**
   * Returns true if the object being compared is equivalent to this object, false otherwise.
   * @param o - instance of Object class being compared to this object.
   * @return true if the object being compared is equivalent to this object, false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommandLineParser that = (CommandLineParser) o;
    return Objects.equals(this.csvPath, that.csvPath) &&
        Objects.equals(this.getOutputFolder(), that.getOutputFolder()) &&
        Arrays.equals(this.getCommands(), that.getCommands());
  }

  /**
   * Returns an int hashCode representing the object.
   * @return an int hashCode representing the object.
   */
  @Override
  public int hashCode() {
    int result = Objects.hash(this.allCommands, getOutputFolder(), this.csvPath);
    result = 31 * result + Arrays.hashCode(getCommands());
    return result;
  }

  /**
   * Returns a String representation of the object.
   * @return a String representation of the object.
   */
  @Override
  public String toString() {
    return "CommandLineParser{" +
        "commands=" + Arrays.toString(this.commands) +
        ", allCommands='" + this.allCommands + '\'' +
        ", outputFolder='" + this.outputFolder + '\'' +
        ", csvPath='" + this.csvPath + '\'' +
        '}';
  }
}
