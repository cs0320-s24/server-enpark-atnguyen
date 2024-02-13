package edu.brown.cs.student.main.Handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.State.BroadbandDatasource;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
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
  public Object handle(Request request, Response response) throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    // Replies will be Maps from String to Object. This isn't ideal; see reflection...
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    Map<String, Object> responseMap = new HashMap<>();

    String state = request.queryParams("state");
    String county = request.queryParams("county");
    responseMap.put("state", state);
    responseMap.put("county", county);
    this.state.setState(state);
    this.state.setCounty(county);

    responseMap.put("broadband", this.state.getBroadband(state, county));

    return responseMap;
  }
}
