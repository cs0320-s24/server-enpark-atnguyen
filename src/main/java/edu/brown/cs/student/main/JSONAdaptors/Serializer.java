package edu.brown.cs.student.main.JSONAdaptors;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.Map;

public class Serializer {

  private final JsonAdapter<Map<String, Object>> adapter;

  public Serializer() {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    this.adapter = moshi.adapter(mapStringObject);
  }

  public String createJSON(Map<String, Object> map) {
    return this.adapter.toJson(map);
  }
}
