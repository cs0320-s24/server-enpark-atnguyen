package edu.brown.cs.student.main.JSONAdaptors;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import okio.Buffer;

/** A class that handles deserializing objects from a JSON to a List. */
public class Deserializer {

  private final JsonAdapter<List<List<String>>> adapter;

  /** The constructor of Deserializer that sets up the functionality to deserialize a JSON. */
  public Deserializer() {
    Moshi moshi = new Moshi.Builder().build();
    Type listStringObject = Types.newParameterizedType(List.class, List.class, String.class);
    this.adapter = moshi.adapter(listStringObject);
  }

  /**
   * A method that creates a List<List<String>> from a JSON.
   *
   * @param json The json to deserialize and convert into a List
   * @return a String with the data from map in JSON form
   */
  public List<List<String>> deserialize(Buffer json) throws IOException {
    return this.adapter.fromJson(json);
  }
}
