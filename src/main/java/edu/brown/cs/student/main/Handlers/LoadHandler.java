package edu.brown.cs.student.main.Handlers;

import edu.brown.cs.student.main.CreatorFromRowClasses.ArrayListCreator;
import edu.brown.cs.student.main.CreatorFromRowClasses.FactoryFailureException;
import edu.brown.cs.student.main.SearchCSV.CSVParser;
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
  private CSVParser<ArrayList<String>> parser;


  public LoadHandler() {
  }

  @Override
  public Object handle(Request request, Response response) {
    String file = request.queryParams("file");
    String hasHeaders = request.queryParams("headers");
    //object will be class that represents JSON
    Map<String, Object> responseMap = new HashMap<>();

    try {
      this.parser = new CSVParser<>(new FileReader(file), new ArrayListCreator());
      List<ArrayList<String>> parsedCSV = this.parser.parse(convertHeaderResponse(hasHeaders));
      responseMap.put("CSV", parsedCSV);
      responseMap.put("result", "success");
    } catch (FactoryFailureException | IOException e) {
      responseMap.put("result", "error");


    }



    return null;
  }

  private boolean convertHeaderResponse(String hasHeaders) {
    if (hasHeaders.toLowerCase().equals("yes")) {
      return true;
    }
    else if (hasHeaders.toLowerCase().equals("no")) {
      return false;
    } else {
      return false;
   //   TODO: figure out error handling
    }
  }
}
