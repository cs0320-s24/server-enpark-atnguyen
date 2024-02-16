package edu.brown.cs.student.main.Caching;

 import com.google.common.cache.CacheBuilder;
 import com.google.common.cache.CacheLoader;
 import com.google.common.cache.LoadingCache;
 import java.util.concurrent.TimeUnit;

public class ACSCache {

  private int maxSize;
  private int expireMins;
  public ACSCache(int maxSize, int expireMins) {
    LoadingCache<String, String> cache = CacheBuilder.newBuilder()
        .maximumSize(maxSize)
        .expireAfterWrite(expireMins, TimeUnit.MINUTES)
        .build(
            new CacheLoader<String, String>();
  }

  cr

  public void buildCache(int )
}
