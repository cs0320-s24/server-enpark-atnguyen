package edu.brown.cs.student;

import edu.brown.cs.student.main.CSVParser;
import edu.brown.cs.student.main.CreatorFromRowClasses.ArrayListCreator;
import edu.brown.cs.student.main.CreatorFromRowClasses.CreatorFromRow;
import edu.brown.cs.student.main.CreatorFromRowClasses.FactoryFailureException;
import edu.brown.cs.student.main.Searcher;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SearcherTest {

  private String csv1;
  private String csv2;
  private String csv3;
  private String malformed1;
  private String noHeaders1;
  private String noHeaders2;
  private String noHeaders3;
  private CreatorFromRow<ArrayList<String>> arrayListCreator;

  @Before
  public void setUp() {
    this.csv1 = "data/census/dol_ri_earnings_disparity.csv";
    this.csv2 = "data/census/income_by_race.csv";
    this.csv3 = "data/census/postsecondary_education.csv";
    this.malformed1 = "data/malformed/malformed_signs.csv";
    this.noHeaders1 = "data/census/no_headers_dol_ri_earnings_disparity.csv";
    this.noHeaders2 = "data/census/no_headers_income_by_race.csv";
    this.noHeaders3 = "data/census/no_headers_postsecondary_education.csv";
    this.arrayListCreator = new ArrayListCreator();
  }

  /**
   * This tests searching a CSV with headers. All tests search every column of the CSV. There is
   * also a test for when a user says the CSV has headers, and there aren't any.
   *
   * @throws IOException
   * @throws FactoryFailureException
   */
  @Test
  public void testSearchWithHeaders() throws IOException, FactoryFailureException {
    // test csv1 with input that doesn't match original capitalization
    CSVParser parser1 = new CSVParser(new FileReader(this.csv1), this.arrayListCreator);
    Searcher searcher1 = new Searcher(parser1);
    List<ArrayList<String>> found1 = searcher1.search("Ri", true);
    Assert.assertEquals(6, found1.size());

    // test csv2 with number
    CSVParser parser2 = new CSVParser(new FileReader(this.csv2), this.arrayListCreator);
    Searcher searcher2 = new Searcher(parser2);
    List<ArrayList<String>> found2 = searcher2.search("9", true);
    Assert.assertEquals(40, found2.size());

    // test csv3 with longer phrase
    CSVParser parser3 = new CSVParser(new FileReader(this.csv3), this.arrayListCreator);
    Searcher searcher3 = new Searcher(parser3);
    List<ArrayList<String>> found3 = searcher3.search("Black or African American", true);
    Assert.assertEquals(2, found3.size());

    /* Test for when user says headers and no headers are there. In this case, the CSV should parse
    out the header and be missing a line */
    CSVParser parser4 = new CSVParser(new FileReader(this.noHeaders1), this.arrayListCreator);
    Searcher searcher4 = new Searcher(parser4);
    List<ArrayList<String>> found4 = searcher4.search("RI", true);
    Assert.assertEquals(5, found4.size());
  }

  /**
   * This tests searching a CSV without headers. All tests search every column of the CSV. There is
   * also a test for when a user says the CSV doesn't have headers, but it does.
   *
   * @throws IOException
   * @throws FactoryFailureException
   */
  @Test
  public void testSearchWithoutHeaders() throws IOException, FactoryFailureException {
    CSVParser parser1 = new CSVParser(new FileReader(this.noHeaders1), this.arrayListCreator);
    Searcher searcher1 = new Searcher(parser1);
    List<ArrayList<String>> found1 = searcher1.search("RI", false);
    Assert.assertEquals(6, found1.size());

    // test second csv with an entry that has commas
    CSVParser parser2 = new CSVParser(new FileReader(this.noHeaders2), this.arrayListCreator);
    Searcher searcher2 = new Searcher(parser2);
    List<ArrayList<String>> found2 = searcher2.search("\"Newport County, RI\"", false);
    Assert.assertEquals(70, found2.size());

    // test with all caps
    CSVParser parser3 = new CSVParser(new FileReader(this.noHeaders3), this.arrayListCreator);
    Searcher searcher3 = new Searcher(parser3);
    List<ArrayList<String>> found3 = searcher3.search("WOMEN", false);
    Assert.assertEquals(8, found3.size());

    /* Test for when user says no headers, but there are headers. In this case, the search will
    search within the headers, too. */
    CSVParser parser4 = new CSVParser(new FileReader(this.csv3), this.arrayListCreator);
    Searcher searcher4 = new Searcher(parser4);
    List<ArrayList<String>> found4 = searcher4.search("year", false);
    Assert.assertEquals(1, found4.size());
  }

  /**
   * Test to ensure that CSVParser can parse different reader types, like StringReader and
   * FileReader.
   *
   * @throws IOException
   * @throws FactoryFailureException
   */
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
    Searcher searcher1 = new Searcher(parser1);
    List<ArrayList<String>> found1 = searcher1.search("RI", true);
    Assert.assertEquals(6, found1.size());

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
    Searcher searcher2 = new Searcher(parser2);
    List<ArrayList<String>> found2 = searcher2.search("-15.24144", false);
    Assert.assertEquals(1, found2.size());
  }

  /**
   * Tests searching a CSV with inconsistently filled columns. The searcher should warn the user
   * about the inconsistency but continue to search the CSV and return.
   *
   * @throws IOException
   * @throws FactoryFailureException
   */
  @Test
  public void testInconsistentCols() throws IOException, FactoryFailureException {
    String csv1 = "1,2,3,4\n" + "1,2,3\n" + "1,2,3,4\n";
    CSVParser parser1 = new CSVParser(new StringReader(csv1), this.arrayListCreator);
    Searcher searcher1 = new Searcher(parser1);
    List<ArrayList<String>> found1 = searcher1.search("1", false);
    Assert.assertEquals(3, found1.size());

    CSVParser parser2 = new CSVParser(new StringReader(csv1), this.arrayListCreator);
    Searcher searcher2 = new Searcher(parser2);
    List<ArrayList<String>> found2 = searcher2.search("4", "3", false);
    Assert.assertEquals(2, found2.size());

    // test with an empty row
    String csv2 = "1,2,3,4\n" + "\n" + "1,2,3,4,5\n";
    CSVParser parser3 = new CSVParser(new StringReader(csv2), this.arrayListCreator);
    Searcher searcher3 = new Searcher(parser3);
    List<ArrayList<String>> found3 = searcher3.search("1", "0", false);
    Assert.assertEquals(2, found3.size());

    CSVParser parser4 = new CSVParser(new StringReader(csv2), this.arrayListCreator);
    Searcher searcher4 = new Searcher(parser4);
    List<ArrayList<String>> found4 = searcher4.search("1", false);
    Assert.assertEquals(2, found4.size());

    CSVParser parser5 = new CSVParser(new FileReader(this.malformed1), this.arrayListCreator);
    Searcher searcher5 = new Searcher(parser5);
    List<ArrayList<String>> found5 = searcher5.search("Gemini", true);
    Assert.assertEquals(1, found5.size());
  }

  /**
   * Tests search on values that don't exist in the CSV
   *
   * @throws IOException
   * @throws FactoryFailureException
   */
  @Test
  public void testNonexistentValues() throws IOException, FactoryFailureException {
    CSVParser parser1 = new CSVParser(new FileReader(this.csv1), this.arrayListCreator);
    Searcher searcher1 = new Searcher(parser1);
    List<ArrayList<String>> found1 = searcher1.search("hello", true);
    Assert.assertEquals(0, found1.size());

    // test csv2 with a close match to "Newport County, RI"
    CSVParser parser2 = new CSVParser(new FileReader(this.csv2), this.arrayListCreator);
    Searcher searcher2 = new Searcher(parser2);
    List<ArrayList<String>> found2 = searcher2.search("Newport County, MA", true);
    Assert.assertEquals(0, found2.size());

    // test csv3 with ""
    CSVParser parser3 = new CSVParser(new FileReader(this.csv3), this.arrayListCreator);
    Searcher searcher3 = new Searcher(parser3);
    List<ArrayList<String>> found3 = searcher3.search("", true);
    Assert.assertEquals(0, found3.size());
  }

  /**
   * Tests cases where the value is present in the CSV but not in the specified column.
   *
   * @throws IOException
   * @throws FactoryFailureException
   */
  @Test
  public void testWrongColumn() throws IOException, FactoryFailureException {
    CSVParser parser1 = new CSVParser(new FileReader(this.csv1), this.arrayListCreator);
    Searcher searcher1 = new Searcher(parser1);
    List<ArrayList<String>> found1 = searcher1.search("RI", "1", true);
    Assert.assertEquals(0, found1.size());

    CSVParser parser2 = new CSVParser(new FileReader(this.csv2), this.arrayListCreator);
    Searcher searcher2 = new Searcher(parser2);
    List<ArrayList<String>> found2 = searcher2.search("2020", "Geography", true);
    Assert.assertEquals(0, found2.size());

    CSVParser parser3 = new CSVParser(new FileReader(this.csv3), this.arrayListCreator);
    Searcher searcher3 = new Searcher(parser3);
    List<ArrayList<String>> found3 = searcher3.search("2", "0", true);
    Assert.assertEquals(0, found3.size());
  }

  /**
   * Tests searching with a column index specified, where indexes start from 0. Also tests that an
   * IllegalArgumentException is thrown when an invalid column is entered.
   */
  @Test
  public void testSearchByColIndex() throws IOException, FactoryFailureException {
    CSVParser parser1 = new CSVParser(new FileReader(this.csv1), this.arrayListCreator);
    Searcher searcher1 = new Searcher(parser1);
    List<ArrayList<String>> found1 = searcher1.search("RI", "0", true);
    Assert.assertEquals(6, found1.size());

    CSVParser parser2 = new CSVParser(new FileReader(this.csv2), this.arrayListCreator);
    Searcher searcher2 = new Searcher(parser2);
    List<ArrayList<String>> found2 = searcher2.search("2020", "2", true);
    Assert.assertEquals(40, found2.size());

    // tests invalid column index
    Assert.assertThrows(
        IllegalArgumentException.class,
        () -> {
          CSVParser parser = new CSVParser(new FileReader(this.csv3), this.arrayListCreator);
          Searcher searcher = new Searcher(parser);
          List<ArrayList<String>> found3 = searcher.search("anything", "25", true);
        });
    Assert.assertThrows(
        IllegalArgumentException.class,
        () -> {
          CSVParser parser = new CSVParser(new FileReader(this.malformed1), this.arrayListCreator);
          Searcher searcher = new Searcher(parser);
          List<ArrayList<String>> found3 = searcher.search("anything", "-1", true);
        });
  }

  /**
   * Tests searching by a column's header name. Searching by header should not be case sensitive.
   *
   * @throws IOException
   * @throws FactoryFailureException
   */
  @Test
  public void testSearchByColHeader() throws IOException, FactoryFailureException {
    CSVParser parser1 = new CSVParser(new FileReader(this.csv1), this.arrayListCreator);
    Searcher searcher1 = new Searcher(parser1);
    List<ArrayList<String>> found1 = searcher1.search("RI", "STATE", true);
    Assert.assertEquals(6, found1.size());

    CSVParser parser2 = new CSVParser(new FileReader(this.csv2), this.arrayListCreator);
    Searcher searcher2 = new Searcher(parser2);
    List<ArrayList<String>> found2 = searcher2.search("2020", "year", true);
    Assert.assertEquals(40, found2.size());

    // tests nonexistent header
    Assert.assertThrows(
        IllegalArgumentException.class,
        () -> {
          CSVParser parser = new CSVParser(new FileReader(this.csv3), this.arrayListCreator);
          Searcher searcher = new Searcher(parser);
          List<ArrayList<String>> found3 = searcher.search("anything", "doesn't exist", true);
        });
  }

  /** Tests the helper method that counts the number of columns in a row. */
  @Test
  public void testFindNumCols() throws IOException, FactoryFailureException {
    CSVParser parser = new CSVParser(new FileReader(this.csv3), this.arrayListCreator);
    Searcher searcher = new Searcher(parser);
    List<String> row1 = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5"));
    List<String> row2 = new ArrayList<>();
    Assert.assertEquals(5, searcher.findNumCols(row1));
    Assert.assertEquals(0, searcher.findNumCols(row2));
  }
}
