package automation;

import static org.junit.Assert.*;

import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;

public class CSVParserTest {
  String headerLine;
  String dataLine;
  String[] dataArray;
  HashMap<String, Integer> headerMap;
  @Before
  public void setUp() throws Exception {
    headerLine = "\"first_name\",\"last_name\",\"company_name\",\"address\",\"city\","
        + "\"county\",\"state\",\"zip\",\"phone1\",\"phone2\",\"email\",\"web\"";
    dataLine =  "\"James\",\"Butt\",\"Benton, John B Jr\",\"6649 N Blue Gum St\","
        + "\"New Orleans\",\"Orleans\",\"LA\",\"70116\",\"504-621-8927\",\"504-845-1427\","
        + "\"jbutt@gmail.com\",\"http://www.bentonjohnbjr.com\"";
    dataArray = CSVParser.ParseCSVLine(dataLine);
    headerMap = CSVParser.CreateHeaderIndex(headerLine);
  }

  @Test
  public void parseCSVLine() {
    String expectedFirstToken = "James";
    String expectedSecondToken = "Butt";
    String expectedThirdToken = "Benton, John B Jr";
    assertEquals(expectedFirstToken, dataArray[0]);
    assertEquals(expectedSecondToken, dataArray[1]);
    assertEquals(expectedThirdToken, dataArray[2]);
  }

  @Test
  public void createHeaderIndex() {
    Integer expectedIndexFirstName = 0;
    Integer expectedIndexZip = 7;
    Integer expectedIndexWeb = 11;
    assertEquals(expectedIndexFirstName, headerMap.get("first_name"));
    assertEquals(expectedIndexZip, headerMap.get("zip"));
    assertEquals(expectedIndexWeb, headerMap.get("web"));
  }
}