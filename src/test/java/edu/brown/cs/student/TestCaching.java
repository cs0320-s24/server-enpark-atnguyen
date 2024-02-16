package edu.brown.cs.student;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.ACSData.Caching.BroadbandData;
import edu.brown.cs.student.main.ACSData.Caching.BroadbandDatasource;
import edu.brown.cs.student.main.ACSData.Caching.Caching;
import edu.brown.cs.student.main.Handlers.BroadbandHandler;
import edu.brown.cs.student.mocks.MockedCensusAPI;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestCaching {
  @BeforeAll
  public static void setupOnce() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);
  }

  private final Type mapStringObject =
      Types.newParameterizedType(Map.class, String.class, Object.class);
  private JsonAdapter<Map<String, Object>> adapter;
  private JsonAdapter<BroadbandData> broadbandDataAdapter;
  private Caching cache;

  @BeforeEach
  public void setup() {
    BroadbandDatasource mockedSource =
        new MockedCensusAPI(new BroadbandData("Marin County, California", "94.0", "06", "041"));
    this.cache = new Caching(mockedSource, 3,1);
    Spark.get("broadband", new BroadbandHandler(mockedSource));
    Spark.awaitInitialization();
    Moshi moshi = new Moshi.Builder().build();
    this.adapter = moshi.adapter(this.mapStringObject);
    this.broadbandDataAdapter = moshi.adapter(BroadbandData.class);
  }

  @AfterEach
  public void teardown() {
    Spark.unmap("broadband");
    Spark.awaitStop();
  }

  private HttpURLConnection tryRequest(String apiCall) throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestProperty("Content-Type", "application/json");
    clientConnection.setRequestProperty("Accept", "application/json");
    clientConnection.connect();
    return clientConnection;
  }

  @Test
  public void testIfCacheEliminates() throws IOException, InterruptedException {
    String state = "California";
    String county = "Marin";
    tryRequest("broadband?state=" + state + "&county=" + county);
    Thread.sleep(6000);
    Assert.assertFalse(this.cache.isValueInCache(state,county));
  }

  @Test
  public void testCache() throws IOException {
    String state = "California";
    String county = "Marin";

    HttpURLConnection connection = tryRequest("broadband?state=" + state + "&county=" + county);
    Map<String, Object> responseBody =
        this.adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
    HttpURLConnection connection2 = tryRequest("broadband?state=" + state + "&county=" + county);
    Map<String, Object> responseBody2 =
        this.adapter.fromJson(new Buffer().readFrom(connection2.getInputStream()));


    Assert.assertEquals(responseBody.get("current time"), responseBody2.get("current time"));
  }

}
