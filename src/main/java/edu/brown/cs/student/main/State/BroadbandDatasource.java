package edu.brown.cs.student.main.State;


import java.io.IOException;

public interface BroadbandDatasource {

  void setState(String state);
  String getState();
  void setCounty(String county);
  String getCounty();
  String getBroadband(String state, String county) throws IOException;




}
