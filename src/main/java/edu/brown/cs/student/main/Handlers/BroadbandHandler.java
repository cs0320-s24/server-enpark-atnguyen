package edu.brown.cs.student.main.Handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.Datasource.BroadbandData;
import edu.brown.cs.student.main.Datasource.DataConvertor;
import edu.brown.cs.student.main.JSONAdaptors.Serializer;
import edu.brown.cs.student.main.State.BroadbandDatasource;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {
  private BroadbandDatasource state;

  public BroadbandHandler(BroadbandDatasource state) {
    this.state = state;

  }
  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();
    String state = request.queryParams("state");
    String county = request.queryParams("county");
    // add requested parameters to the response map
    responseMap.put("state", state);
    responseMap.put("county", county);

    DataConvertor convertor = new DataConvertor(this.state);
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
