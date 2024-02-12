package edu.brown.cs.student.main.State;

import java.util.ArrayList;
import java.util.List;

public class CSVData implements CSVDatasource {

  private List<ArrayList<String>> parsedCSV;

  @Override
  public List<ArrayList<String>> getCurrentCSV() {
    return new ArrayList<>(this.parsedCSV);
  }

  @Override
  public void setCurrentCSV(List<ArrayList<String>> parsedCSV) {
    this.parsedCSV = parsedCSV;
  }
}
