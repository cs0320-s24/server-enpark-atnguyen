package edu.brown.cs.student.UnitTests;

import edu.brown.cs.student.main.CSVData.CSVData;
import edu.brown.cs.student.main.CSVFunctions.CSVParser;
import edu.brown.cs.student.main.CSVFunctions.CreatorFromRowClasses.ArrayListCreator;
import edu.brown.cs.student.main.CSVFunctions.CreatorFromRowClasses.FactoryFailureException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test suite that ensure shared states work properly between different handlers.
 */
public class TestStates {

  private String csvFile;
  private CSVData csvDataState;

  @Before
  public void setUp() {
    this.csvFile = "data/census/ri_city_town_income.csv";
    this.csvDataState = new CSVData();
  }

  /**
   * Tests the CSVData class's getter and setter methods in conjunction.
   * @throws IOException
   * @throws FactoryFailureException
   */
  @Test
  public void testSetGetCurrentCSV() throws IOException, FactoryFailureException {
    CSVParser parser = new CSVParser(new FileReader(this.csvFile), new ArrayListCreator());
    List<ArrayList<String>> parsedCSV = parser.parse();
    this.csvDataState.setCurrentCSV(parsedCSV);
    Assert.assertEquals(parsedCSV, this.csvDataState.getCurrentCSV());
  }

}

