package edu.brown.cs.student.UnitTests;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.ACSData.Caching.BroadbandData;
import edu.brown.cs.student.main.ACSData.Caching.BroadbandDatasource;
import edu.brown.cs.student.main.CSVData.CSVData;
import edu.brown.cs.student.main.CSVFunctions.CSVParser;
import edu.brown.cs.student.main.CSVFunctions.CreatorFromRowClasses.ArrayListCreator;
import edu.brown.cs.student.main.Handlers.BroadbandHandler;
import edu.brown.cs.student.main.Handlers.LoadHandler;
import edu.brown.cs.student.mocks.MockedCSVAPI;
import edu.brown.cs.student.mocks.MockedCensusAPI;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import spark.Spark;

public class TestLoad {

  private LoadHandler loadHandler;
  private JsonAdapter<BroadbandData> broadbandDataAdapter;


  @BeforeAll
  public static void setupOnce() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);
  }

  private final Type mapStringObject =
      Types.newParameterizedType(Map.class, String.class, Object.class);
  private JsonAdapter<Map<String, Object>> adapter;

  @BeforeEach
  public void setUp() {
    this.loadHandler = new LoadHandler(new CSVData());
    BroadbandDatasource mockedSource =
        new MockedCSVAPI();
    Spark.get("broadband", new BroadbandHandler(mockedSource));
    Spark.awaitInitialization();
    Moshi moshi = new Moshi.Builder().build();
    this.adapter = moshi.adapter(this.mapStringObject);
    this.broadbandDataAdapter = moshi.adapter(BroadbandData.class);
  }

  @Test
  public void testInvalidFile() {
    String invalidFile1 = "ri_city_town_income.csv"; // exists, not in data folder
    String invalidFile2 = "not_a_file"; // doesn't exist
    CSVParser parser1 = new CSVParser(new FileReader(invalidFile1), new ArrayListCreator());

    this.loadHandler.handle
  }

}
