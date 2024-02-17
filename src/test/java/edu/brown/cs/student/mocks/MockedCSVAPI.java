package edu.brown.cs.student.mocks;

import edu.brown.cs.student.main.ACSData.Caching.BroadbandData;
import edu.brown.cs.student.main.ACSData.Caching.BroadbandDatasource;
import edu.brown.cs.student.main.CSVData.CSVDatasource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MockedCSVAPI implements CSVDatasource {

  private List<ArrayList<String>> csv;
  private ArrayList<String> headers;

  public MockedCSVAPI(List<ArrayList<String>> csv) {
    this.csv = csv;
  }


  @Override
  public List<ArrayList<String>> getCurrentCSV() {
    return null;
  }

  @Override
  public void setCurrentCSV(List<ArrayList<String>> parsedCSV) {
    this.csv = parsedCSV;
  }

  @Override
  public ArrayList<String> getCSVHeaders() {
    return null;
  }

  @Override
  public void setCSVHeaders(ArrayList<String> headers) {
    this.headers = headers;
  }
}
