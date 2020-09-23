package automation;

import java.io.IOException;

// this is the driver class that takes in the command arguments and call the other classes
public class Automation {

  public static void main(String[] args) throws InvalidInputCombinationException, IOException {
    String csvPath = null;
    String outputFolderPath = null;
    String emailTemplate = null;
    String letterTemplate = null;
    // get necessary info from command line
    try {
      CommandLineParser argsParser = new CommandLineParser(args);
      csvPath = argsParser.getCSVPath();
      outputFolderPath = argsParser.getOutputFolder();
      emailTemplate = argsParser.getTemplatePath("--email");
      letterTemplate = argsParser.getTemplatePath("--letter");
    } catch (InvalidInputCombinationException e) {
      e.printStackTrace();
      System.exit(0);
    }

    TemplateReplacer templateReplacer;
    // calls the right constructor for template replacer
    if (emailTemplate == null || letterTemplate == null) {
      // if one of them is null, set the template to the non-null template
      String template = (emailTemplate == null) ? letterTemplate : emailTemplate;
      templateReplacer = new TemplateReplacer(outputFolderPath, csvPath, template);
    } else {
      // if both templates are provided
      templateReplacer =
          new TemplateReplacer(outputFolderPath, csvPath, emailTemplate, letterTemplate);
    }
    // use the constructed templateReplacer to read from CSV and write files
    templateReplacer.readFromCSV();
  }
}
