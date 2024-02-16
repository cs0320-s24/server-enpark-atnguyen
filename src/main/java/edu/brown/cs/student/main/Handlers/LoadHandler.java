package edu.brown.cs.student.main.Handlers;

import edu.brown.cs.student.main.CreatorFromRowClasses.ArrayListCreator;
import edu.brown.cs.student.main.CreatorFromRowClasses.FactoryFailureException;
import edu.brown.cs.student.main.JSONAdaptors.Serializer;
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

/** A class that handles queries related to loading a CSV. */
public class LoadHandler implements Route {

  private final CSVDatasource state;
  private String file;
  /**
   * The constructor of the LoadHandler class that initializes the shared state.
   *
   * @param state the shared state between load, search, and view
   */
  public LoadHandler(CSVDatasource state) {
    this.state = state;
  }

  /**
   * A method that handles load queries and puts the loaded CSV into a JSON to be returned to the
   * user.
   *
   * @param request the request made by the user
   * @param response
   * @return: a JSON that holds the data to be shown to the user
   */
  @Override
  public Object handle(Request request, Response response) {
    this.file = request.queryParams("file");
    String hasHeaders = request.queryParams("headers");

    // object will be class that represents JSON
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("header", hasHeaders);

    try {
      // directly return the error message if the file is invalid
      if (!this.checkValidFile(responseMap)) {
        return new Serializer().createJSON(responseMap);
      }
      CSVParser parser = new CSVParser<>(new FileReader(this.file), new ArrayListCreator());
      List<ArrayList<String>> parsedCSV = parser.parse();
      boolean headers = this.convertHeaderResponse(hasHeaders);
      if (headers) {
        this.state.setCurrentCSV(parsedCSV.subList(1, parsedCSV.size() - 1));
        this.state.setCSVHeaders(parsedCSV.get(0));
      } else {
        this.state.setCurrentCSV(parsedCSV);
        this.state.setCSVHeaders(new ArrayList<>());
      }
      responseMap.put("result", "success");
      responseMap.put("file", this.file);
    } catch (FactoryFailureException e) {
      responseMap.put("result", "error_parse");
    } catch (IOException e) {
      responseMap.put("result", "error_datasource");
    }

    return new Serializer().createJSON(responseMap);
  }

  /**
   * A method that converts the user's response to if the file has headers or not into a boolean.
   *
   * @param hasHeaders the response to the headers parameter
   * @return
   */
  private boolean convertHeaderResponse(String hasHeaders) {
    if (hasHeaders == null || hasHeaders.isEmpty()) {
      return false;
    }
    if (hasHeaders.toLowerCase().equals("yes")) {
      return true;
    } else if (hasHeaders.toLowerCase().equals("no")) {
      return false;
    } else {
      return false;
      //   TODO: figure out error handling
    }
  }

  /**
   * Method that checks if the requested file is in the correct data/ directory by adding data/ to
   * the file if it is not already there, then making sure the next characters are not ../
   *
   * @param responseMap The map returned by handle() that holds the error message if the file is
   *     invalid.
   * @return True if file is valid, false if not.
   */
  private boolean checkValidFile(Map<String, Object> responseMap) {
    String rootPath = "data/";
    if (!this.file.substring(0, 5).equals(rootPath)) {
      this.file = rootPath + this.file;
    }
    // ensure user can't navigate up to a parent directory
    if (this.file.substring(5, 8).equals("../")) {
      responseMap.put("result", "error_invalid_file_directory");
      return false;
    }
    return true;
  }

}
