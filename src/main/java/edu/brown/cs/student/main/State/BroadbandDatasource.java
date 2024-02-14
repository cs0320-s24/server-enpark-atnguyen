package edu.brown.cs.student.main.State;


import edu.brown.cs.student.main.Datasource.BroadbandData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface BroadbandDatasource {

  void setState(String state);
  String getState();
  void setCounty(String county);
  String getCounty();
  List<List<String>> getBroadband(String state, String county) throws IOException;




}