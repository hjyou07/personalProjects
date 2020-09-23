package automation;
  import java.io.BufferedReader;
  import java.io.BufferedWriter;
  import java.io.File;
  import java.io.FileNotFoundException;
  import java.io.FileReader;
  import java.io.FileWriter;
  import java.io.IOException;
  import java.nio.file.Files;
  import java.nio.file.Paths;
  import java.util.HashMap;
  import java.util.List;
  import java.util.regex.Matcher;
  import java.util.regex.Pattern;

/**
 * This class calls reads the CSV file and populates the template accordingly whether it's an
 * email or a letter template or both. Uses the CSV file headers as the placeholder matchers to
 * the template files. Finally, outputs the email and/or letter template according to each person
 */
public class TemplateReplacer {

  private String output_dir;
  private String csv_file;
  private String template1;
  private String template2;
  List<String> emailTemplate;
  List<String> letterTemplate;

  /**
   * Constructor an instance of the automation.TemplateReplace class. This instance is based on the
   * production of just an email template or a letter template input
   *
   * @param output_dir refers to the string of the output directory provided
   * @param csv_file   refers to the string of the CVC file being read
   * @param template1  is the letter template or email template provided
   */
  public TemplateReplacer(String output_dir, String csv_file, String template1) {
    this.output_dir = output_dir;
    this.csv_file = csv_file;
    constructorCheck(template1);
  }

  /**
   * Constructor an instance of the automation.TemplateReplace class. This instance is based on the
   * production of both an email template and a letter template input
   *
   * @param output_dir      refers to the string of the output directory provided
   * @param csv_file        refers to the string of the CVC file being read
   * @param email_template  is the email template provided
   * @param letter_template is the letter template provided
   */
  public TemplateReplacer(String output_dir, String csv_file, String email_template,
      String letter_template) {
    this.output_dir = output_dir;
    this.csv_file = csv_file;
    this.template1 = email_template;
    this.template2 = letter_template;
  }

  /**
   * Helper method to check the logic of the constructor's template based off what is provided
   *
   * @param template checks the string of what can be passed into the constructor
   */
  private void constructorCheck(String template) {
    if (template.contains("email")) {
      this.template1 = template;
      this.template2 = null;
    } else if (template.contains("letter")) {
      this.template2 = template;
      this.template1 = null;
    }
  }

  /**
   * This private helper method reads all the lines of the template file. If the file is not found,
   * and exception will be thrown
   * @param template is the template passed in to be read
   * @return a list of strings
   * @throws IOException if the template file isn't correctly passed in by the user
   */
  private List<String> ReadTemplateFile(String template) throws IOException {
    List<String> templateFileLines = null;
    try  {
      templateFileLines = Files.readAllLines(Paths.get(template));
    } catch (FileNotFoundException fnfe) {
      System.out.println("*** OOPS! Template file was not found : " + fnfe.getMessage());
      fnfe.printStackTrace();
    }
    return templateFileLines;
  }

  /**
   * This method reads the CSV file, calls the populate template method, and outputs a final
   * template for each specific individual
   *
   * @throws IOException
   */
  public void readFromCSV() throws IOException {
    if (this.template1 != null) {
      this.emailTemplate = ReadTemplateFile(this.template1);
    }
    if (this.template2 != null) {
      this.letterTemplate = ReadTemplateFile(this.template2);
    }
    try (BufferedReader inputFile = new BufferedReader(new FileReader(this.csv_file));) {
      String line;
      int counter = 0;
      HashMap<String, Integer> headerMap = null;
      String[] csvLine;

      while ((line = inputFile.readLine()) != null) {
        // System.out.println("Read: " + line);
        if (counter == 0) {
          //System.out.println("We reached the if block.");
          headerMap = CSVParser.CreateHeaderIndex(line);
        } else {
          //System.out.println("We reached the else block.");
          csvLine = CSVParser.ParseCSVLine(line);
          String supporterName = csvLine[headerMap.get("first_name")];
          String outputFileName = String.format("/%d %s.out.txt", counter, supporterName);
          // call the helper method to populate the template
          populateTemplate(outputFileName, headerMap, csvLine);
        }
        counter++;
      }
    } catch (FileNotFoundException fnfe) {
      System.out.println("*** OOPS! A file was not found : " + fnfe.getMessage());
      fnfe.printStackTrace();
      System.exit(0);
    } catch (IOException ioe) {
      System.out.println("Something went wrong! : " + ioe.getMessage());
      ioe.printStackTrace();
      System.exit(0);
    }
  }

  /**
   * This private helper method write the template by creating a pattern that will establish a
   * matcher group to match against the line of the template method (i.e. placeholders). If found,
   * then the line's placeholder will be replaced with the new value (i.e. the value of the hashmap
   * of strings generated)
   * @param templateText represents the string of lines to be read
   * @param headerMap represents the hashmap of strings as keys, and integers as values (index's)
   * @param csvLine represents the line in the CSV line
   * @param outputFile represents the final output file generated after the template is written
   *                   according to the specific individuals
   * @throws IOException if the templatetext isn't corrctly passed by the user
   */
  private void WriteTemplate(List<String> templateText, HashMap<String, Integer> headerMap,
      String[] csvLine, BufferedWriter outputFile) throws IOException {
    for (String line : templateText) {
      String pattern1 = "\\[\\[(\\S*)]]";
      Pattern pattern = Pattern.compile(pattern1, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(line);
      while (matcher.find()) {
        String placeholder = matcher.group(1);  // Save target value inside placeholder here.
        int index = headerMap.get(placeholder);  // Should always have the key.
        String newValue = csvLine[index];
        line = line.replaceFirst("\\[\\[" + placeholder + "]]",
            newValue);
      }
      outputFile.write(line + "\n");
    }
  }

  /**
   * Helper method to singularly populate the email template
   * @param outputFileName is the name of the output file
   * @param outputSubDirectory is the subdirectory the output file will be written in.
   * @param headerMap is the hashmap representing the keys and values of the headers from the CSV
   * @param csvLine is the string array of the CSV line
   * @throws IOException occurs when the output file for emails can't be opened
   */
  private void populateSpecificTemplate(List<String> generalTemplate, String outputFileName,
      String outputSubDirectory, HashMap<String, Integer> headerMap, String[] csvLine)
      throws IOException {
    String outputPath = this.output_dir + File.separator + outputSubDirectory;
    File path = new File(outputPath); // output path = string
    if (!path.exists()) {
      path.mkdirs();
    }
    try (BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputPath + outputFileName))) {
      WriteTemplate(generalTemplate, headerMap, csvLine, outputFile);
      /*if (outputSubDirectory.equals("email")) {
        WriteTemplate(emailTemplate, headerMap, csvLine, outputFile);
      } else if (outputSubDirectory.equals("letter")) {
        WriteTemplate(letterTemplate, headerMap, csvLine, outputFile);
      }*/
    } catch (IOException ioe) {
      System.out.println("The output file can't be opened. Check if the path exists");
      System.exit(0);
    }
  }

  /**
   * This method populates the template given from the CSV parsed headers
   *
   * @param outputFileName is the output file specific to that line of CSV being parsed
   * @param headerMap      allows up to access the keys (the parsed headers from the CSV file and
   *                       its corresponding value (index)
   * @throws IOException if the template can't be understood as an email and/or letter template
   */
  private void populateTemplate(String outputFileName, HashMap<String, Integer> headerMap,
      String[] csvLine) throws IOException {
    if (this.template1 != null) {
      populateSpecificTemplate(this.emailTemplate, outputFileName, "email", headerMap, csvLine);
    }
    if (this.template2 != null) {
      populateSpecificTemplate(this.letterTemplate, outputFileName, "letter", headerMap, csvLine);
      }
    }
  }