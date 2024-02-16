package edu.brown.cs.student.main.Handlers;

import edu.brown.cs.student.main.JSONAdaptors.Serializer;
import edu.brown.cs.student.main.SearchCSV.Searcher;
import edu.brown.cs.student.main.State.CSVDatasource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    Map<String, Object> responseMap = new HashMap<>();
    String value = request.queryParams("value");
    String column = request.queryParams("column");
    // put requested parameters into the response map
    responseMap.put("requested_value", value);
    if (column != null) {
      responseMap.put("requested_column", column);
    } else {
      responseMap.put("requested_column", "none_specified");
    }

    if (this.state.getCurrentCSV().size() == 0) {
      responseMap.put("result", "error_no_csv_loaded");
    } else {
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

      if (column == null) {
        List<ArrayList<String>> foundRows = searcher.search(value, hasHeaders);
        if (foundRows.size() == 0) {
          responseMap.put("found", "value not found");
        } else {
          responseMap.put("found", searcher.search(value, hasHeaders));
        }
      } else {
        try {
          List<ArrayList<String>> foundRows = searcher.search(value, column, hasHeaders);
          if (foundRows.size() == 0) {
            responseMap.put("found", "value not found");
          } else {
            responseMap.put("found", foundRows);
          }
          // thrown when an invalid column is entered
        } catch (IllegalArgumentException e) {
          responseMap.put("result", "error_bad_request");
        }
      }
    }
    return new Serializer().createJSON(responseMap);
  }
}
