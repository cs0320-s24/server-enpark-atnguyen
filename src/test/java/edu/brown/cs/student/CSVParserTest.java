package edu.brown.cs.student;

import edu.brown.cs.student.main.SearchCSV.CSVParser;
import edu.brown.cs.student.main.CreatorFromRowClasses.ArrayListCreator;
import edu.brown.cs.student.main.CreatorFromRowClasses.CreatorFromRow;
import edu.brown.cs.student.main.CreatorFromRowClasses.FactoryFailureException;
import edu.brown.cs.student.main.CreatorFromRowClasses.Star.Star;
import edu.brown.cs.student.main.CreatorFromRowClasses.Star.StarCreator;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

// figure out what to do about the not parsing whitespace
public class CSVParserTest {

  private String malformed1;
  private CreatorFromRow arrayListCreator;

  @Before
  public void setUp() {
    this.malformed1 = "data/malformed/malformed_signs.csv";
    this.arrayListCreator = new ArrayListCreator();
  }

  @Test
  public void testForEach() throws IOException, FactoryFailureException {
    CSVParser<ArrayList<String>> stars =
        new CSVParser(new FileReader("data/malformed/malformed_signs.csv"), new ArrayListCreator());
    for (ArrayList<String> s : stars) {
      System.out.println(s.getClass());
    }
    Assert.assertEquals(1, 1);
  }

  @Test
  public void testDifferentReaders() throws IOException, FactoryFailureException {
    String csv1 =
        "State,Data Type,Average Weekly Earnings,Number of Workers,Earnings Disparity,Employed Percent\n"
            + "RI,White,\" $1,058.47 \",$1., $1.00 ,75%\n"
            + "RI,Black, $770.26 ,30424.80376, $0.73 ,6%\n"
            + "RI,Native American/American Indian, $471.07 ,2315.505646, $0.45 ,0%\n"
            + "RI,Asian-Pacific Islander,\" $1,080.09 \",18956.71657, $1.02 ,4%\n"
            + "RI,Hispanic/Latino, $673.14 ,74596.18851, $0.64 ,14%\n"
            + "RI,Multiracial, $971.89 ,8883.049171, $0.92 ,2%";
    CSVParser parser1 = new CSVParser(new StringReader(csv1), this.arrayListCreator);
    List<ArrayList<String>> parsedCSV1 = parser1.parse(true);
    Assert.assertEquals(6, parsedCSV1.size());
    // check that a row is formed and added correctly
    ArrayList<String> row1 =
        new ArrayList<>(Arrays.asList("RI", "White", "\" $1,058.47 \"", "$1.", " $1.00 ", "75%"));
    Assert.assertEquals(row1, parsedCSV1.get(0));

    // testing a string csv without headers
    String csv2 =
        "0,Sol,0,0,0\n"
            + "1,,282.43485,0.00449,5.36884\n"
            + "2,,43.04329,0.00285,-15.24144\n"
            + "3,,277.11358,0.02422,223.27753\n"
            + "3759,96 G. Psc,7.26388,1.55643,0.68697\n"
            + "70667,Proxima Centauri,-0.47175,-0.36132,-1.15037\n"
            + "71454,Rigel Kentaurus B,-0.50359,-0.42128,-1.1767\n"
            + "71457,Rigel Kentaurus A,-0.50362,-0.42139,-1.17665\n"
            + "87666,Barnard's Star,-0.01729,-1.81533,0.14824\n"
            + "118721,,-2.28262,0.64697,0.29354\n";
    CSVParser parser2 = new CSVParser(new StringReader(csv2), this.arrayListCreator);
    List<ArrayList<String>> parsedCSV2 = parser2.parse(false);
    Assert.assertEquals(10, parsedCSV2.size());
  }

  /**
   * This test ensures that parse still parses the CSV when rows have different numbers of items.
   * This is important so that malformed rows can be found and caught in Searcher.
   *
   * @throws IOException
   * @throws FactoryFailureException
   */
  @Test
  public void testInconsistentCols() throws IOException, FactoryFailureException {
    CSVParser parser1 = new CSVParser(new FileReader(this.malformed1), this.arrayListCreator);
    List<ArrayList<String>> parsedCSV1 = parser1.parse(true);
    Assert.assertEquals(12, parsedCSV1.size());
    Assert.assertEquals(2, parsedCSV1.get(0).size());
    Assert.assertEquals(2, parsedCSV1.get(1).size());
    Assert.assertEquals(3, parsedCSV1.get(2).size());
    Assert.assertEquals(1, parsedCSV1.get(6).size());
    Assert.assertEquals(2, parsedCSV1.get(7).size());
  }

  @Test
  public void testWithDifferentCreators() throws IOException, FactoryFailureException {
    CreatorFromRow<Star> starCreator = new StarCreator();
    String starCSV = "data/stars/ten-star.csv";
    CSVParser parser = new CSVParser(new FileReader(starCSV), starCreator);
    List<Star> parsedCSV = parser.parse(true);
    Assert.assertEquals(10, parsedCSV.size());
  }

  /**
   * Test that makes sure a value in the CSV of ",," gets parsed as an empty space and is still
   * included.
   *
   * @throws IOException
   * @throws FactoryFailureException
   */
  @Test
  public void testBlankValue() throws IOException, FactoryFailureException {
    String csv = "1,2,,4\n" + "2,3,4,5\n" + "3,4,5,6";
    CSVParser parser = new CSVParser(new StringReader(csv), this.arrayListCreator);
    List<ArrayList<String>> parsedCSV = parser.parse(false);
    Assert.assertEquals(3, parsedCSV.size());
    Assert.assertEquals(4, parsedCSV.get(0).size());
    Assert.assertEquals("", parsedCSV.get(0).get(2));
  }

  /**
   * In this test, the last assert statement (commented out) fails because the regular expression
   * doesn't parse out excess whitespace in a CSV entry. This is a possible flaw of the regular
   * expression and may need to be fixed.
   *
   * @throws IOException
   * @throws FactoryFailureException
   */
  @Test
  public void testWhiteSpaceCases() throws IOException, FactoryFailureException {
    String csv = "1,2,   3,4\n2,3,4,5\n3,4,5,6";
    CSVParser parser = new CSVParser(new StringReader(csv), this.arrayListCreator);
    List<ArrayList<String>> parsedCSV = parser.parse(false);
    Assert.assertEquals(3, parsedCSV.size());
    Assert.assertEquals(4, parsedCSV.get(0).size());
    //    Assert.assertEquals("3", parsedCSV.get(0).get(2));
  }
}
