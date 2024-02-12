package edu.brown.cs.student.main.State;

import java.util.ArrayList;
import java.util.List;

public interface CSVDatasource {

  List<ArrayList<String>> getCurrentCSV();
  //      throws DatasourceException, IllegalArgumentException;

  void setCurrentCSV(List<ArrayList<String>> parsedCSV);
}
