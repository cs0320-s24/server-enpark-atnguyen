package edu.brown.cs.student.main.ACSData;

import edu.brown.cs.student.main.ACSData.Caching.BroadbandDatasource;
import edu.brown.cs.student.main.JSONAdaptors.Deserializer;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import okio.Buffer;

/**
 * A class that handles querying the Census API more to obtain the codes corresponding to each state
 * and county.
 */
public class CodeConverter {

  private BroadbandDatasource state;
  private final HashMap<String, String> stateMap = new HashMap<>();
  private String lastCalledState = ""; // the current state whose counties are in countyMap
  private HashMap<String, String> countyMap = new HashMap<>();

  /**
   * The constructor of DataConvertor that defines the shared state.
   *
   * @param state the shared broadband state
   */
  public CodeConverter(BroadbandDatasource state) {
    this.state = state;
  }

  /**
   * A method that converts a state to its code.
   *
   * @param state the state to be converted
   * @return the code of that state
   */
  public String convertState(String state) throws IOException {
    if (this.stateMap.isEmpty()) {
      this.createStateMap();
    }
    String stateCode = this.stateMap.get(state);
    if (stateCode == null) {
      throw new IllegalArgumentException("invalid state entered");
    }
    return stateCode;
  }

  /**
   * A method that converts a county to its code. County map is populated with the last requested
   * state's counties, so if a user queries the same state multiple times, the API doesn't have to
   * create a new county map every time.
   *
   * @param state the state the county belongs in.
   * @param county the county to be converted.
   * @return the code of that county.
   */
  public String convertCounty(String state, String county) {
    if (!this.lastCalledState.equals(state)) {
      this.countyMap = this.createCountyMap(state);
    }
    String countyCode = this.countyMap.get(county);
    if (countyCode == null) {
      throw new IllegalArgumentException("invalid county entered");
    }
    return countyCode;
  }

  /**
   * Private helper method; throws IOException so different callers can handle differently if
   * needed.
   *
   * @param requestURL
   * @return
   * @throws IOException
   */
  private static HttpURLConnection connect(URL requestURL) throws IOException {
    URLConnection urlConnection = requestURL.openConnection();
    if (!(urlConnection instanceof HttpURLConnection)) {
      throw new IOException();
    }
    HttpURLConnection clientConnection = (HttpURLConnection) urlConnection;
    clientConnection.connect(); // GET
    if (clientConnection.getResponseCode() != 200) {
      throw new IOException();
    }
    return clientConnection;
  }

  /**
   * A method that gets the list of state codes by ACS and deserializes it into a list that we can
   * use.
   *
   * @return a list of a list of strings that contain the states and their codes
   * @throws IOException invalid input/output
   */
  private List<List<String>> getStateCodes() throws IOException {
    URL requestURL = new URL("https", "api.census.gov", "/data/2010/dec/sf1?get=NAME&for=state:*");
    HttpURLConnection clientConnection = connect(requestURL);
    Buffer response = new Buffer().readFrom(clientConnection.getInputStream());
    return new Deserializer().deserialize(response);
  }

  /**
   * A method that gets the list of county codes in a state by ACS and deserializes it into a list
   * that we can use.
   *
   * @param state the state where the county exists.
   * @return a list of a list of strings that contain the states and their codes.
   * @throws IOException invalid input/output
   */
  private List<List<String>> getCountyCodes(String state) throws IOException {
    URL requestURL =
        new URL(
            "https",
            "api.census.gov",
            "/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + state);
    HttpURLConnection clientConnection = connect(requestURL);

    Buffer response = new Buffer().readFrom(clientConnection.getInputStream());
    return new Deserializer().deserialize(response);
  }

  /**
   * A method that creates a hashmap from the list of states and their codes.
   *
   * @return a hashmap that maps each state to their code.
   */
  private void createStateMap() throws IOException {
    List<List<String>> list = this.getStateCodes();
    for (List<String> row : list) {
      String state = row.get(0);
      String code = row.get(1);
      this.stateMap.put(state.toLowerCase(), code);
    }
  }

  /**
   * A method that creates a hashmap from the list of counties in a state and their codes.
   *
   * @param state the state where the county exists in.
   * @return a hashmap that maps each county to their code.
   */
  private HashMap<String, String> createCountyMap(String state) {
    HashMap<String, String> map = new HashMap<>();
    try {
      List<List<String>> list = this.getCountyCodes(state);
      for (List<String> row : list) {
        String county = row.get(0);
        String code = row.get(2);
        map.put(county.toLowerCase().split(" ")[0], code);
      }
      return map;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return map;
  }
}
