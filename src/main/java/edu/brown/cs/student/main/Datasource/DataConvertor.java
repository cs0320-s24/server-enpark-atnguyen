package edu.brown.cs.student.main.Datasource;

import edu.brown.cs.student.main.State.BroadbandDatasource;

public class DataConvertor {

  private BroadbandDatasource state;

  public DataConvertor(BroadbandDatasource state) {
    this.state = state;
  }

  private void convertData() {
    String state = this.state.getState();
    String county = this.state.getCounty();

  }

}
