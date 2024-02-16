package edu.brown.cs.student.mocks;

import edu.brown.cs.student.main.Datasource.BroadbandData;
import edu.brown.cs.student.main.State.BroadbandDatasource;

public class MockedCensusAPI implements BroadbandDatasource {
  private final BroadbandData data;

  public MockedCensusAPI(BroadbandData data) {
    this.data = data;
  }

  @Override
  public BroadbandData getBroadband(String state, String county) {
    return this.data;
  }
}
