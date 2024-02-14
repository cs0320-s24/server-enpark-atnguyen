package edu.brown.cs.student.main.Datasource;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Moshi.Builder;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.State.BroadbandDatasource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okio.Buffer;
import okio.BufferedSource;

public class DataConvertor {

  private BroadbandDatasource state;

  public DataConvertor(BroadbandDatasource state) {
    this.state = state;


  }
  public String convertData(String state) {
    HashMap<String, String> map = this.createMap();
    return map.get(state);
  }
  private static HttpURLConnection connect(URL requestURL) throws IOException {
    URLConnection urlConnection = requestURL.openConnection();
    if (!(urlConnection instanceof HttpURLConnection)) {
      System.out.println("error");
    }
    HttpURLConnection clientConnection = (HttpURLConnection) urlConnection;
    clientConnection.connect(); // GET
    if(clientConnection.getResponseCode() != 200)
      System.out.println("error");
    return clientConnection;
  }

  public List<List<String>> getCodes() throws IOException {
    List<List<String>> list = new ArrayList<>();
    HashMap<String, String> map = new HashMap<>();
    URL requestURL = new URL("https", "api.census.gov",
          "/data/2010/dec/sf1?get=NAME&for=state:*");
    HttpURLConnection clientConnection = connect(requestURL);
    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(List.class, List.class, String.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(type);
    list = adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    clientConnection.disconnect();

    return list;

  }
  public HashMap<String, String> createMap() {
    HashMap<String, String> map = new HashMap<>();
    try {
      List<List<String>> list = this.getCodes();
      for (List<String> row : list) {
        String state = row.get(0);
        String code = row.get(1);
        map.put(state, code);
      }
      return map;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return map;
  }

}
