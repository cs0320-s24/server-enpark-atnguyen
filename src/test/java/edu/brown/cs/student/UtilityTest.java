package edu.brown.cs.student;

import edu.brown.cs.student.main.CreatorFromRowClasses.FactoryFailureException;
import edu.brown.cs.student.main.Utility;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UtilityTest {

  private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private String csv1;

  @Before
  public void setUp() {
    System.setOut(new PrintStream(this.outputStream));
    this.csv1 = "data/census/dol_ri_earnings_disparity.csv";
  }

  /**
   * Test to ensure that the Utility class calls search and prints results properly
   *
   * @throws FactoryFailureException
   * @throws IOException
   */
  @Test
  public void testAllCols() throws FactoryFailureException, IOException {
    Utility utility1 = new Utility(this.csv1, true);
    utility1.run("RI", "all");
    Assert.assertEquals(
        "Rows that contain 'RI':\n"
            + "\t[RI, White, \" $1,058.47 \", $1.,  $1.00 , 75%]\n"
            + "\t[RI, Black,  $770.26 , 30424.80376,  $0.73 , 6%]\n"
            + "\t[RI, Native American/American Indian,  $471.07 , 2315.505646,  $0.45 , 0%]\n"
            + "\t[RI, Asian-Pacific Islander, \" $1,080.09 \", 18956.71657,  $1.02 , 4%]\n"
            + "\t[RI, Hispanic/Latino,  $673.14 , 74596.18851,  $0.64 , 14%]\n"
            + "\t[RI, Multiracial,  $971.89 , 8883.049171,  $0.92 , 2%]",
        this.outputStream.toString().trim());
  }

  /**
   * Tests if the user just presses return instead of specifying a column
   *
   * @throws FactoryFailureException
   * @throws IOException
   */
  @Test
  public void testNoColSpecified() throws FactoryFailureException, IOException {
    Utility utility1 = new Utility(this.csv1, true);
    utility1.run("RI", "");
    Assert.assertEquals(
        "Rows that contain 'RI':\n"
            + "\t[RI, White, \" $1,058.47 \", $1.,  $1.00 , 75%]\n"
            + "\t[RI, Black,  $770.26 , 30424.80376,  $0.73 , 6%]\n"
            + "\t[RI, Native American/American Indian,  $471.07 , 2315.505646,  $0.45 , 0%]\n"
            + "\t[RI, Asian-Pacific Islander, \" $1,080.09 \", 18956.71657,  $1.02 , 4%]\n"
            + "\t[RI, Hispanic/Latino,  $673.14 , 74596.18851,  $0.64 , 14%]\n"
            + "\t[RI, Multiracial,  $971.89 , 8883.049171,  $0.92 , 2%]",
        this.outputStream.toString().trim());
  }

  /**
   * Tests the utility running search with a column index
   *
   * @throws FactoryFailureException
   * @throws IOException
   */
  @Test
  public void testColIndex() throws FactoryFailureException, IOException {
    Utility utility1 = new Utility(this.csv1, true);
    utility1.run("RI", "0");
    Assert.assertEquals(
        "Rows that contain 'RI':\n"
            + "\t[RI, White, \" $1,058.47 \", $1.,  $1.00 , 75%]\n"
            + "\t[RI, Black,  $770.26 , 30424.80376,  $0.73 , 6%]\n"
            + "\t[RI, Native American/American Indian,  $471.07 , 2315.505646,  $0.45 , 0%]\n"
            + "\t[RI, Asian-Pacific Islander, \" $1,080.09 \", 18956.71657,  $1.02 , 4%]\n"
            + "\t[RI, Hispanic/Latino,  $673.14 , 74596.18851,  $0.64 , 14%]\n"
            + "\t[RI, Multiracial,  $971.89 , 8883.049171,  $0.92 , 2%]",
        this.outputStream.toString().trim());
  }

  /**
   * Tests the Utility running search with a column header
   *
   * @throws FactoryFailureException
   * @throws IOException
   */
  @Test
  public void testColHeader() throws FactoryFailureException, IOException {
    Utility utility1 = new Utility(this.csv1, true);
    utility1.run("RI", "STATE");
    Assert.assertEquals(
        "Rows that contain 'RI':\n"
            + "\t[RI, White, \" $1,058.47 \", $1.,  $1.00 , 75%]\n"
            + "\t[RI, Black,  $770.26 , 30424.80376,  $0.73 , 6%]\n"
            + "\t[RI, Native American/American Indian,  $471.07 , 2315.505646,  $0.45 , 0%]\n"
            + "\t[RI, Asian-Pacific Islander, \" $1,080.09 \", 18956.71657,  $1.02 , 4%]\n"
            + "\t[RI, Hispanic/Latino,  $673.14 , 74596.18851,  $0.64 , 14%]\n"
            + "\t[RI, Multiracial,  $971.89 , 8883.049171,  $0.92 , 2%]",
        this.outputStream.toString().trim());
  }

  /**
   * Test to make sure that files that are not in the data/ directory throw a FileNotFoundException
   * to ensure file and data safety.
   *
   * @throws IOException
   * @throws FactoryFailureException
   */
  @Test
  public void testInvalidDirectory() throws IOException, FactoryFailureException {
    String validFile1 = "data/census/dol_ri_earnings_disparity.csv";
    String validFile2 = "census/dol_ri_earnings_disparity.csv";
    String invalidFile1 = "dol_ri_earnings.disparity.csv";
    String invalidFile2 = "inaccessible_test_file.csv";
    String invalidFile3 = "../../../main/java/edu.brown.cs.student.main/ArrayListCreator.java";

    Utility utility1 = new Utility(validFile1, true);
    Utility utility2 = new Utility(validFile2, true);
    Utility utility3 = new Utility(invalidFile1, true);
    Utility utility4 = new Utility(invalidFile2, false);
    Utility utility5 = new Utility(invalidFile3, false);

    // these two should not cause errors and print normally
    utility1.run("RI", "all");
    utility2.run("RI", "all");

    Assert.assertThrows(
        FileNotFoundException.class,
        () -> {
          utility3.run("anything", "all");
        });
    Assert.assertThrows(
        FileNotFoundException.class,
        () -> {
          utility4.run("anything", "all");
        });
    Assert.assertThrows(
        FileNotFoundException.class,
        () -> {
          utility5.run("anything", "all");
        });
  }

  /** Tests the case of invalid columns entered */
  @Test
  public void testInvalidColumn() {
    Assert.assertThrows(
        IllegalArgumentException.class,
        () -> {
          Utility utility = new Utility(this.csv1, true);
          utility.run("RI", "no column");
        });
    Assert.assertThrows(
        IllegalArgumentException.class,
        () -> {
          Utility utility = new Utility(this.csv1, true);
          utility.run("RI", "25");
        });
  }
}
