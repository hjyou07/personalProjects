package automation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CommandLineParserTest {

  private CommandLineParser test1;
  private CommandLineParser test2;
  private CommandLineParser test3;
  private CommandLineParser test4;
  private CommandLineParser test5;
  private CommandLineParser test6;
  private String[] commands1;

  @Before
  public void setUp() throws InvalidInputCombinationException {
    commands1 = new String[]{"--email", "--email-template", "email-template.txt",
        "--output-dir", "email", "--csv-file", "customer.csv"};
    String[] commands2 = {"--letter", "--letter-template", "letter-template.txt",
        "--output-dir", "email", "--csv-file", "customer.csv"};
    String[] commands3 = {"--email", "--email-template", "email-template.txt", "--letter",
        "--letter-template", "letter-template.txt", "--output-dir", "email", "--csv-file",
        "customer.csv"};
    String[] commands4 = {"--email", "--email-template", "email-template.txt",
        "--output-dir", "emails", "--csv-file", "customer.csv"};
    String[] commands5 = {"--email", "--email-template", "email-template.txt",
        "--output-dir", "email", "--csv-file", "customers.csv"};

    test1 = new CommandLineParser(commands1);
    test2 = new CommandLineParser(commands1);
    test3 = new CommandLineParser(commands2);
    test4 = new CommandLineParser(commands3);
    test5 = new CommandLineParser(commands4);
    test6 = new CommandLineParser(commands5);
  }

  @Test(expected= InvalidInputCombinationException.class)
  public void unmatchedTemplate() throws InvalidInputCombinationException {
    String[] commands4 = {"--email", "--letter-template", "letter-template.txt",
        "--output-dir", "email", "--csv-file", "customer.csv"};
    new CommandLineParser(commands4);
  }

  @Test(expected= InvalidInputCombinationException.class)
  public void noCSVFile() throws InvalidInputCombinationException {
    String[] commands5 = {"--email", "--email-template", "email-template.txt", "--letter",
        "--letter-template", "letter-template.txt", "--output-dir", "email"};
    new CommandLineParser(commands5);
  }

  @Test(expected= InvalidInputCombinationException.class)
  public void noOutputFolder() throws InvalidInputCombinationException {
    String[] commands6 = {"--email", "--email-template", "email-template.txt", "--letter",
        "--letter-template", "letter-template.txt", "--csv-file", "customer.csv"};
    new CommandLineParser(commands6);
  }

  @Test(expected= InvalidInputCombinationException.class)
  public void lessThanMinimum() throws InvalidInputCombinationException {
    String[] commands7 = {"--email-template", "email-template.txt", "--letter-template",
        "letter-template.txt", "--output-dir", "email"};
    new CommandLineParser(commands7);
  }

  @Test(expected= InvalidInputCombinationException.class)
  public void isNull() throws InvalidInputCombinationException {
    new CommandLineParser(null);
  }

  @Test
  public void getCommands() {
    assertSame(test1.getCommands(), commands1);
  }


  @Test
  public void getTemplatePath() {
    String expected1 = "email-template.txt";
    String expected2 = "letter-template.txt";
    assertEquals(test1.getTemplatePath("--email"), expected1);
    assertNull(test1.getTemplatePath("--letter"));
    assertEquals(test3.getTemplatePath("--letter"), expected2);
    assertNull(test3.getTemplatePath("--email"));
    assertEquals(test4.getTemplatePath("--email"), expected1);
    assertEquals(test4.getTemplatePath("--letter"), expected2);
  }

  @Test
  public void getOutputFolder() {
    String expected1 = "email";
    assertEquals(test1.getOutputFolder(), expected1);

  }

  @Test
  public void getCSVPath() {
    String expected = "customer.csv";
    assertEquals(test1.getCSVPath(), expected);
  }

  @Test
  public void testEquals() {
    assertEquals(test1, test1);  // Reflexive
    assertEquals(test1, test2);  // Same parameters

    //Test fields
    assertNotEquals(test1, test3);
    assertNotEquals(test1, test4);
    assertNotEquals(test1, test5);
    assertNotEquals(test1, test6);

    // Test object types.
    assertNotEquals(test1, 0.0);
    assertNotEquals(test1, null);

  }

  @Test
  public void testHashCode() {
    assertEquals(test1.hashCode(), test2.hashCode());
  }

  @Test
  public void testToString() {
    String expected = "CommandLineParser{commands=[--email, --email-template, email-template.txt, "
        + "--output-dir, email, --csv-file, customer.csv], allCommands='--email|--email-template|"
        + "email-template.txt|--output-dir|email|--csv-file|customer.csv|', outputFolder='email',"
        + " csvPath='customer.csv'}";
    assertEquals(test1.toString(), expected);
  }
}