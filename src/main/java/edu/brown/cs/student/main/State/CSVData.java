package edu.brown.cs.student.main.State;

import java.util.ArrayList;
import java.util.List;

public class CSVData implements CSVDatasource {

  private List<ArrayList<String>> parsedCSV = new ArrayList<>();
  private ArrayList<String> csvHeaders;

  @Override
  public List<ArrayList<String>> getCurrentCSV() {
    return new ArrayList<>(this.parsedCSV);
  }

  @Override
  public void setCurrentCSV(List<ArrayList<String>> parsedCSV) {
    this.parsedCSV = parsedCSV;
  }

  @Override
  public ArrayList<String> getCSVHeaders() {
    return new ArrayList<>(this.csvHeaders);
  }

  @Override
  public void setCSVHeaders(ArrayList<String> headers) {
    this.csvHeaders = headers;
  }
}
