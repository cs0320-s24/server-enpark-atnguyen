package edu.brown.cs.student.main.Handlers;

import edu.brown.cs.student.main.Datasource.DataConvertor;
import edu.brown.cs.student.main.JSONAdaptors.Serializer;
import edu.brown.cs.student.main.State.BroadbandDatasource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * A class that handles queries related to broadband.
 */
public class BroadbandHandler implements Route {
  private BroadbandDatasource state;

  /**
   * The constructor of the BroadbandHandler class that initializes the state.
   * @param state
   */
  public BroadbandHandler(BroadbandDatasource state) {
    this.state = state;

  }

  /**
   * A method that handles broadband queries and puts the API response into a JSON to be returned to the user.
   * @param request: the request made by the user
   * @param response
   * @return: a JSON that holds the data to be shown to the user
   */
  @Override
  public Object handle(Request request, Response response) {
    String state = request.queryParams("state");
    String county = request.queryParams("county");
    DataConvertor convertor = new DataConvertor(this.state);
    Map<String, Object> responseMap = new HashMap<>();
    try {
      String state_code = convertor.convertState(state);
      List<List<String>> data = this.state.getBroadband(state_code,
          convertor.convertCounty(state_code, county));
      responseMap.put("broadband", data);
    } catch (Exception e) {
      e.printStackTrace();
      responseMap.put("result", "error");
    }
    return new Serializer().createJSON(responseMap);
  }
}
