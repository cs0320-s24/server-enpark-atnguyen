package edu.brown.cs.student.main.CSVData;

import java.util.ArrayList;
import java.util.List;

/**
 * An interface that declares the methods needed to share the CSV file's data between the load,
 * search, and view handlers.
 */
public interface CSVDatasource {

  /**
   * Gets the current CSV
   *
   * @return
   */
  List<ArrayList<String>> getCurrentCSV();
  //      throws DatasourceException, IllegalArgumentException;

  /**
   * Sets the current CSV
   *
   * @param parsedCSV
   */
  void setCurrentCSV(List<ArrayList<String>> parsedCSV);

  /**
   * Gets the CSV headers
   *
   * @return
   */
  ArrayList<String> getCSVHeaders();
  //      throws DatasourceException, IllegalArgumentException;

  /**
   * Sets the CSV headers
   *
   * @param headers
   */
  void setCSVHeaders(ArrayList<String> headers);
}
