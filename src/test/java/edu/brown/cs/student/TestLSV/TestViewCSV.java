package edu.brown.cs.student.TestLSV;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.ACSData.Caching.BroadbandData;
import edu.brown.cs.student.main.CSVData.CSVData;
import edu.brown.cs.student.main.Handlers.LoadHandler;
import edu.brown.cs.student.main.Handlers.SearchHandler;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestViewCSV {

  @BeforeAll
  public static void setupOnce() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);
  }

  private final Type mapStringObject =
      Types.newParameterizedType(Map.class, String.class, Object.class);
  private JsonAdapter<Map<String, Object>> adapter;
  private JsonAdapter<BroadbandData> broadbandDataAdapter;
  private CSVData data;

  @BeforeEach
  public void setup() {
    this.data = new CSVData();
    Spark.get("load", new LoadHandler(this.data));
    Spark.get("view", new SearchHandler(this.data));
    Spark.awaitInitialization();
    Moshi moshi = new Moshi.Builder().build();
    this.adapter = moshi.adapter(this.mapStringObject);
    this.broadbandDataAdapter = moshi.adapter(BroadbandData.class);
  }

  @AfterEach
  public void teardown() {
    Spark.unmap("load");
    Spark.unmap("view");
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
  public void testSimpleView() throws IOException {
    HttpURLConnection connection =
        tryRequest("load?file=data/census/dol_ri_earnings_disparity.csv&headers=yes");
    assertEquals(200, connection.getResponseCode());
    Map<String, Object> responseBody =
        this.adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
    assertEquals("success", responseBody.get("result"));

    HttpURLConnection connection2 =
        tryRequest("view");
    assertEquals(200, connection2.getResponseCode());
    Map<String, Object> responseBody2 =
        this.adapter.fromJson(new Buffer().readFrom(connection2.getInputStream()));
    assertEquals("success", responseBody2.get("result"));
    connection.disconnect();
  }

  @Test
  public void testViewNoFile() throws IOException {
    HttpURLConnection connection =
        tryRequest("view");
    assertEquals(200, connection.getResponseCode());
    Map<String, Object> responseBody2 =
        this.adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
    assertEquals("error_bad_request", responseBody2.get("result"));
    connection.disconnect();
  }

}
