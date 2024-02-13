package edu.brown.cs.student.main.Handlers;

import edu.brown.cs.student.main.SearchCSV.Searcher;
import edu.brown.cs.student.main.State.CSVDatasource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchHandler implements Route {
  private CSVDatasource state;

  // main = Server class, which will make instances of handlers
  // search handler make instance of searcher
  public SearchHandler(CSVDatasource state) {
    this.state = state;
  }

  @Override
  public Object handle(Request request, Response response) {
    String value = request.queryParams("value");
    String column = request.queryParams("column");
    boolean hasHeaders = true;
    ArrayList<String> headers = this.state.getCSVHeaders();
    if (headers.size() == 0) {
      hasHeaders = false;
    }

    Searcher searcher;
    if (hasHeaders) {
      searcher = new Searcher(this.state.getCurrentCSV(), headers);
    } else {
      searcher = new Searcher(this.state.getCurrentCSV());
    }

    Map<String, Object> responseMap = new HashMap<>();
    if (column == null) {
      responseMap.put("data", searcher.search(value, hasHeaders));
    } else {
      try {
        responseMap.put("data", searcher.search(value, column, hasHeaders));
      } catch (IllegalArgumentException e) {
        responseMap.put("result", "error_bad_request");
      }
    }

    return responseMap;
  }
}
