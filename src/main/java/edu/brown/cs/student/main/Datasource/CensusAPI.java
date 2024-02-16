package edu.brown.cs.student.main.Datasource;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.State.BroadbandDatasource;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import okio.Buffer;

public class CensusAPI implements BroadbandDatasource {
  private String state;
  private String county;

  @Override
  public void setState(String state) {
    this.state = state;
  }

  @Override
  public String getState() {
    return this.state;
  }

  @Override
  public void setCounty(String county) {
    this.county = county;
  }

  @Override
  public String getCounty() {
    return this.state;
  }

  @Override
  public List<List<String>> getBroadband(String state, String county) throws IOException {
    return getBroadBandPercentage(state, county);
  }

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

  private static List<List<String>> getBroadBandPercentage(String state, String county)
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
    Type type = Types.newParameterizedType(List.class, List.class, String.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(type);
    List<List<String>> list =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    clientConnection.disconnect();
    return list;
  }
}
