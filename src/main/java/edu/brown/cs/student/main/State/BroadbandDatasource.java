package edu.brown.cs.student.main.State;

import edu.brown.cs.student.main.Datasource.BroadbandData;
import java.io.IOException;

public interface BroadbandDatasource {

  BroadbandData getBroadband(String state, String county) throws IOException;

}
