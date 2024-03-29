package edu.brown.cs.student.main.ACSData;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.ACSData.Caching.BroadbandData;
import edu.brown.cs.student.main.ACSData.Caching.BroadbandDatasource;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import okio.Buffer;

/**
 * A datasource for broadband queries via Census API. This class uses the real API to return results
 * back to the proxy server.
 */
public class CensusAPI implements BroadbandDatasource {

  /**
   * A method that returns the response given by ACS and properly uses the data in the cache or
   * loads it into the cache if it isn't there.
   *
   * @param state the state to be searches
   * @param county the county to be searched
   * @return the broadband data of the state and county
   * @throws IOException invalid input/output
   */
  @Override
  public BroadbandData getBroadband(String state, String county) throws IOException {
    return getBroadBandPercentage(state, county);
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
    //    if(! (urlConnection instanceof HttpURLConnection))
    //      throw new DatasourceException("unexpected: result of connection wasn't HTTP");
    HttpURLConnection clientConnection = (HttpURLConnection) urlConnection;
    clientConnection.connect(); // GET
    if (clientConnection.getResponseCode() != 200) {
      System.out.println("connection error");
    }
    //      throw new DatasourceException("unexpected: API connection not success status
    // "+clientConnection.getResponseMessage());
    return clientConnection;
  }

  /**
   * A method that interacts with the real API to get the data of the requested state and county.
   *
   * @param state the state to be searched
   * @param county the county to be searched
   * @return the data converted into a BroadbandData record
   * @throws IOException invalid input/output
   */
  private static BroadbandData getBroadBandPercentage(String state, String county)
      throws IOException {
    URL requestURL =
        new URL(
            "https",
            "api.census.gov",
            "/data/2021/acs/acs1/subject/variables?"
                + "get=NAME,S2802_C03_022E&for=county:"
                + county
                + "&in=state:"
                + state);
    HttpURLConnection clientConnection = connect(requestURL);
    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(List.class, List.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(type);
    List<List<String>> data =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    clientConnection.disconnect();
    return new BroadbandData(
        data.get(1).get(0), data.get(1).get(1), data.get(1).get(2), data.get(1).get(3), getTime());
  }

  private static String getTime() {
    LocalDateTime dateAndTime = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return dateAndTime.format(format);
  }
}
