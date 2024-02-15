package edu.brown.cs.student.main.State;

import java.util.ArrayList;
import java.util.List;

/**
 * An interface that declares the methods needed to share the CSV file's data between the load, search,
 * and view handlers.
 */
public interface CSVDatasource {

  List<ArrayList<String>> getCurrentCSV();
  //      throws DatasourceException, IllegalArgumentException;

  void setCurrentCSV(List<ArrayList<String>> parsedCSV);

  ArrayList<String> getCSVHeaders();
  //      throws DatasourceException, IllegalArgumentException;

  void setCSVHeaders(ArrayList<String> headers);
}
