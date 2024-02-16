package edu.brown.cs.student.main.Handlers;

import edu.brown.cs.student.main.Datasource.BroadbandData;
import edu.brown.cs.student.main.Datasource.DataConvertor;
import edu.brown.cs.student.main.JSONAdaptors.Serializer;
import edu.brown.cs.student.main.State.BroadbandDatasource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** A class that handles queries related to broadband. */
public class BroadbandHandler implements Route {
  private BroadbandDatasource state;

  /**
   * The constructor of the BroadbandHandler class that initializes the state.
   *
   * @param state
   */
  public BroadbandHandler(BroadbandDatasource state) {
    this.state = state;
  }

  /**
   * A method that handles broadband queries and puts the API response into a JSON to be returned to
   * the user.
   *
   * @param request: the request made by the user
   * @param response
   * @return: a JSON that holds the data to be shown to the user
   */
  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();
    String state = request.queryParams("state");
    String county = request.queryParams("county");
    DataConvertor convertor = new DataConvertor(this.state);
    try {
      String state_code = convertor.convertState(state.toLowerCase());
      BroadbandData data =
          this.state.getBroadband(
              state_code, convertor.convertCounty(state_code, county.toLowerCase()), this.getTime());
      responseMap.put("result", "success");
      responseMap.put("state", state);
      responseMap.put("county", county);
      responseMap.put("broadband", data);
      responseMap.put("current time", this.getTime());
    } catch (NullPointerException e) {
      responseMap.put("result", "missing parameter");
    }
    catch (Exception e) {
      e.printStackTrace();
      responseMap.put("result", "error");
    }
    return new Serializer().createJSON(responseMap);
  }

  /**
   * A helper method that gets the time of when the query was made
   *
   * @return the time in a string
   */
  private String getTime() {
    LocalDateTime dateAndTime = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return dateAndTime.format(format);
  }
}
