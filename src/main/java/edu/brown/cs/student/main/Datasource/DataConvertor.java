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
import java.util.HashMap;
import java.util.List;
import okio.Buffer;

public class DataConvertor {

  private BroadbandDatasource state;

  public DataConvertor(BroadbandDatasource state) {
    this.state = state;
  }

  public String convertState(String state) {
    HashMap<String, String> map = this.createStateMap();
    return map.get(state);
  }

  public String convertCounty(String state, String county) {
    HashMap<String, String> map = this.createCountyMap(state);
    return map.get(county);
  }

  private static HttpURLConnection connect(URL requestURL) throws IOException {
    URLConnection urlConnection = requestURL.openConnection();
    if (!(urlConnection instanceof HttpURLConnection)) {
      System.out.println("error");
    }
    HttpURLConnection clientConnection = (HttpURLConnection) urlConnection;
    clientConnection.connect(); // GET
    if (clientConnection.getResponseCode() != 200) System.out.println("error");
    return clientConnection;
  }

  public List<List<String>> getStateCodes() throws IOException {
    HashMap<String, String> map = new HashMap<>();
    URL requestURL = new URL("https", "api.census.gov", "/data/2010/dec/sf1?get=NAME&for=state:*");
    HttpURLConnection clientConnection = connect(requestURL);
    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(List.class, List.class, String.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(type);
    List<List<String>> list =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    clientConnection.disconnect();
    return list;
  }

  private List<List<String>> getCountyCodes(String state) throws IOException {
    URL requestURL =
        new URL(
            "https",
            "api.census.gov",
            "/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + state);
    HttpURLConnection clientConnection = connect(requestURL);
    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(List.class, List.class, String.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(type);
    List<List<String>> list =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    clientConnection.disconnect();
    return list;
  }

  public HashMap<String, String> createStateMap() {
    HashMap<String, String> map = new HashMap<>();
    try {
      List<List<String>> list = this.getStateCodes();
      for (List<String> row : list) {
        String state = row.get(0);
        String code = row.get(1);
        map.put(state.toLowerCase(), code);
      }
      return map;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return map;
  }

  public HashMap<String, String> createCountyMap(String state) {
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
