package edu.brown.cs.student.mocks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.ACSData.Caching.BroadbandData;
import edu.brown.cs.student.main.ACSData.Caching.BroadbandDatasource;
import edu.brown.cs.student.main.CSVData.CSVData;
import edu.brown.cs.student.main.CSVData.CSVDatasource;
import edu.brown.cs.student.main.Handlers.BroadbandHandler;
import edu.brown.cs.student.main.Handlers.LoadHandler;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestBroadbandHandlerMocked {

  @BeforeAll
  public static void setupOnce() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);
  }

  private final Type mapStringObject =
      Types.newParameterizedType(Map.class, String.class, Object.class);
  private JsonAdapter<Map<String, Object>> adapter;
  private JsonAdapter<BroadbandData> broadbandDataAdapter;
  LocalDateTime dateAndTime = LocalDateTime.now();
  DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @BeforeEach
  public void setup() {
    BroadbandDatasource mockedSource =
        new MockedCensusAPI(new BroadbandData("Marin County, California", "94.0", "06", "041",this.getTime()));
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
  private String getTime() {
    LocalDateTime dateAndTime = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return dateAndTime.format(format);
  }

  @Test
  public void testWithEmptyParameters() throws IOException {
    HttpURLConnection connection = tryRequest("broadband?county=Marin");
    assertEquals(200, connection.getResponseCode());
    Map<String, Object> responseBody =
        this.adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
    assertEquals("error_bad_request", responseBody.get("result"));
    connection.disconnect();
  }

  @Test
  public void testWithNoParameters() throws IOException {
    HttpURLConnection connection = tryRequest("broadband");
    assertEquals(200, connection.getResponseCode());
    Map<String, Object> responseBody =
        this.adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
    assertEquals("error_bad_request", responseBody.get("result"));
    connection.disconnect();
  }

}
