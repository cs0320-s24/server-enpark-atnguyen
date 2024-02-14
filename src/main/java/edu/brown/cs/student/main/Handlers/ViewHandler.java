package edu.brown.cs.student.main.Handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.JSONAdaptors.Serializer;
import edu.brown.cs.student.main.State.CSVData;
import edu.brown.cs.student.main.State.CSVDatasource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class ViewHandler implements Route {

  private final CSVDatasource state;
  public ViewHandler(CSVDatasource state) {
    this.state = state;
  }

  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();
    List<ArrayList<String>> csv = this.state.getCurrentCSV();
    if (csv.size() == 0) {
      responseMap.put("result", "error_no_csv_loaded");
    }
    else {
      ArrayList<String> headers = this.state.getCSVHeaders();
      if (headers.size() > 0) {
        csv.add(0, headers);
      }
      responseMap.put("result", "success");
      responseMap.put("data", csv);
    }
    return new Serializer().createJSON(responseMap);
  }
}
