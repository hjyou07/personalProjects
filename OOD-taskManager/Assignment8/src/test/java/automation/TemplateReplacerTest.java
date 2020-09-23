package automation;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class TemplateReplacerTest {

  private TemplateReplacer test1;
  private TemplateReplacer test2;
  private TemplateReplacer test3;
  private String outputPath;

  @Before
  public void setUp() throws Exception {
    outputPath = "Users" + File.separator;
    test1 = new TemplateReplacer(outputPath,
        "nonprofit-supporters.csv","email-template.txt");
    test2 = new TemplateReplacer(outputPath,
        "nonprofit-supporters.csv", "letter-template.txt");
    test3 = new TemplateReplacer(outputPath,
        "nonprofit-supporters.csv",
        "email-template.txt", "letter-template.txt");
  }

  @Test
  public void readFromCSV() throws IOException {
    test1.readFromCSV();
    File path = new File(outputPath + File.separator + "email" + File.separator + "1 James.out.txt");
    assertTrue(path.exists());
    test2.readFromCSV();
    File path2 = new File(outputPath + File.separator + "email" + File.separator + "500 Chauncey.out.txt");
    assertTrue(path2.exists());
    test3.readFromCSV();
    File path3 = new File(outputPath + File.separator + "email" + File.separator + "333 Leonora.out.txt");
    File path4 = new File(outputPath + File.separator + "email" + File.separator + "165 Kerry.out.txt");
    assertTrue(path3.exists());
    assertTrue(path4.exists());
  }

  @Test (expected=FileNotFoundException.class)
  public void readFromCSVError() throws IOException {
    TemplateReplacer invalidPath = new TemplateReplacer(outputPath,
        "starter_code/Invalid.csv", "email-template.txt");
    try {
      invalidPath.readFromCSV();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}