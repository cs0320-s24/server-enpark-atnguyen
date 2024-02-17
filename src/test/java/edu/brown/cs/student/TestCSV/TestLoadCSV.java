package edu.brown.cs.student.TestCSV;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.ACSData.Caching.BroadbandData;
import edu.brown.cs.student.main.CSVData.CSVData;
import edu.brown.cs.student.main.Handlers.LoadHandler;
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

/**
 * Testing suite for Load
 */
public class TestLoadCSV {

  /**
   * Setup method to start port
   */
    @BeforeAll
    public static void setupOnce() {
      Spark.port(0);
      Logger.getLogger("").setLevel(Level.WARNING);
    }

    private final Type mapStringObject =
        Types.newParameterizedType(Map.class, String.class, Object.class);
    private JsonAdapter<Map<String, Object>> adapter;
    private JsonAdapter<BroadbandData> broadbandDataAdapter;

  /**
   * Setup method before each test
   * @throws IOException
   */
  @BeforeEach
    public void setup() {
      Spark.get("load", new LoadHandler(new CSVData()));
      Spark.awaitInitialization();
      Moshi moshi = new Moshi.Builder().build();
      this.adapter = moshi.adapter(this.mapStringObject);
      this.broadbandDataAdapter = moshi.adapter(BroadbandData.class);
    }

  /**
   * Method called after each test
   */
  @AfterEach
    public void teardown() {
      Spark.unmap("load");
      Spark.awaitStop();
    }

  /**
   * Sets up request
   * @param apiCall the request to be made
   * @return the client connection
   * @throws IOException invalid input/output
   */
    private HttpURLConnection tryRequest(String apiCall) throws IOException {
      URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
      HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
      clientConnection.setRequestProperty("Content-Type", "application/json");
      clientConnection.setRequestProperty("Accept", "application/json");
      clientConnection.connect();
      return clientConnection;
    }

  /**
   * Tests simple load request
   * @throws IOException invalid input/output
   */
    @Test
    public void testSimpleLoad() throws IOException {
      HttpURLConnection connection =
          tryRequest("load?file=data/census/dol_ri_earnings_disparity.csv&headers=yes");
      assertEquals(200, connection.getResponseCode());
      Map<String, Object> responseBody =
          this.adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
      assertEquals("success", responseBody.get("result"));
      connection.disconnect();
    }

  /**
   * Tests when the file is wrong
   * @throws IOException invalid input/output
   */
    @Test
    public void testLoadDataSourceError() throws IOException {
      HttpURLConnection connection =
          tryRequest("load?headers=no&file=/Users/asianguyen/Desktop/Brown%20University/Sophomore%"
              + "20Courses/CS32/server-enpark-atnguyen/data/census/dol_ri_earnings_disparity.csv/Users/asianguyen/"
              + "Desktop/Brown%20University/Sophomore%20Courses/CS32/server-enpark-atnguyen/data/census/income_by_race.csv");
      assertEquals(200, connection.getResponseCode());
      Map<String, Object> responseBody =
          this.adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
      assertEquals("error_datasource", responseBody.get("result"));
      connection.disconnect();
    }

  /**
   * Tests when the file is outside the directory
   * @throws IOException invalid input/output
   */
    @Test
    public void testLoadDataFile() throws IOException {
      HttpURLConnection connection =
          tryRequest("load?headers=no&file=data/../dol_ri_earnings_disparity.csv");
      assertEquals(200, connection.getResponseCode());
      Map<String, Object> responseBody =
          this.adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
      assertEquals("error_invalid_file_directory", responseBody.get("result"));
      connection.disconnect();
    }

  /**
   * Tests loading nothing
   * @throws IOException invalid input/output
   */
    @Test
    public void testLoadNothing() throws IOException {
      HttpURLConnection connection =
          tryRequest("load");
      assertEquals(200, connection.getResponseCode());
      Map<String, Object> responseBody =
          this.adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
      assertEquals("error_bad_request", responseBody.get("result"));
      connection.disconnect();
    }

  /**
   * Tests that you can load a new file
   * @throws IOException invalid input/output
   */
    @Test
  public void testLoadDataFileTwice() throws IOException {
    HttpURLConnection connection =
        tryRequest("load?file=data/census/dol_ri_earnings_disparity.csv&headers=yes");
    Map<String, Object> responseBody =
        this.adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
    assertEquals("success", responseBody.get("result"));

    HttpURLConnection connectionError =
        tryRequest("load?headers=no&file=data/../dol_ri_earnings_disparity.csv");
    assertEquals(200, connectionError.getResponseCode());
    Map<String, Object> responseBodyError =
        this.adapter.fromJson(new Buffer().readFrom(connectionError.getInputStream()));
    assertEquals("error_invalid_file_directory", responseBodyError.get("result"));
    connection.disconnect();
  }


}
