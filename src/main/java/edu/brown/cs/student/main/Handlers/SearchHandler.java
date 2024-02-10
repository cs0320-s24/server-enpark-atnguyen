package edu.brown.cs.student.main.Handlers;

import edu.brown.cs.student.main.Searcher;
import edu.brown.cs.student.main.Utility;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchHandler implements Route {
  private Utility utility;

//main = Server class, which will make instances of handlers
  //search handler make instance of utility
  public SearchHandler() {

  }
  @Override
  public Object handle(Request request, Response response) throws Exception {
    String value = request.queryParams("value");
    String header = request.queryParams("header");
    Map<String, Object> responseMap = new HashMap<>();

    return null;
  }
}
