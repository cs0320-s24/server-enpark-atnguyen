package edu.brown.cs.student.main.Handlers;

import edu.brown.cs.student.main.CreatorFromRowClasses.ArrayListCreator;
import edu.brown.cs.student.main.CreatorFromRowClasses.FactoryFailureException;
import edu.brown.cs.student.main.SearchCSV.CSVParser;
import edu.brown.cs.student.main.State.CSVDatasource;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoadHandler implements Route {

  private final CSVDatasource state;

  public LoadHandler(CSVDatasource state) {
    this.state = state;
  }

  @Override
  public Object handle(Request request, Response response) {
    String file = request.queryParams("file");
    String hasHeaders = request.queryParams("headers");
    // object will be class that represents JSON
    Map<String, Object> responseMap = new HashMap<>();

    try {
      CSVParser parser = new CSVParser<>(new FileReader(file), new ArrayListCreator());
      List<ArrayList<String>> parsedCSV = parser.parse(convertHeaderResponse(hasHeaders));
      this.state.setCurrentCSV(parsedCSV);
      responseMap.put("CSV", parsedCSV);
      responseMap.put("result", "success");
    } catch (FactoryFailureException | IOException e) {
      responseMap.put("result", "error");
    }

    return responseMap;
  }

  private boolean convertHeaderResponse(String hasHeaders) {
    if (hasHeaders.toLowerCase().equals("yes")) {
      return true;
    } else if (hasHeaders.toLowerCase().equals("no")) {
      return false;
    } else {
      return false;
      //   TODO: figure out error handling
    }
  }
}
