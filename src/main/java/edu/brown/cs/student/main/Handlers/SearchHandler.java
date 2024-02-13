package edu.brown.cs.student.main.Handlers;

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
    String value = request.queryParams("value");
    String column = request.queryParams("column");
    Map<String, Object> responseMap = new HashMap<>();

    if (this.state.getCurrentCSV().size() == 0) {
      responseMap.put("result", "error: no csv loaded");
    } else {
      boolean hasHeaders = true;
      ArrayList<String> headers = this.state.getCSVHeaders();
      if (headers.size() == 0) {
        hasHeaders = false;
      }

      Searcher searcher;
      if (hasHeaders) {
        System.out.println("here");
        searcher = new Searcher(this.state.getCurrentCSV(), headers);
      } else {
        searcher = new Searcher(this.state.getCurrentCSV());
      }
      if (column == null) {
        System.out.println("hi");
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
        } catch (IllegalArgumentException e) {
          responseMap.put("result", "exception");
        }
      }
    }
    return responseMap;
  }
}
