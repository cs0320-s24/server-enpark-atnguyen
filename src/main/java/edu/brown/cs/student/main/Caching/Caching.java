package edu.brown.cs.student.main.Caching;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import edu.brown.cs.student.main.Datasource.BroadbandData;
import edu.brown.cs.student.main.State.BroadbandDatasource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Caching implements BroadbandDatasource {
  private BroadbandDatasource toWrap;
  private Cache<String,BroadbandData> cache;

  public Caching(BroadbandDatasource toWrap, int size, int durationInMinutes) {
    this.toWrap = toWrap;
    this.cache =
        CacheBuilder.newBuilder()
            .maximumSize(size)
            .expireAfterWrite(durationInMinutes, TimeUnit.MINUTES)
            .recordStats()
            .build();
  }

  @Override
  public BroadbandData getBroadband(String state, String county) throws IOException {
    BroadbandData data = this.cache.getIfPresent(state + county);
    if (data == null) {
      data = toWrap.getBroadband(state,county);
      this.cache.put(state + county, data);
    }
    return data;
  }
}
