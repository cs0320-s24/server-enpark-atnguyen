package edu.brown.cs.student.main.Caching;

 import com.google.common.cache.CacheBuilder;
 import com.google.common.cache.CacheLoader;
 import com.google.common.cache.LoadingCache;
 import java.util.concurrent.TimeUnit;

public class ACSCache {

  private int maxSize;
  private int expireMins;
  public LoadingCache<String, String> cache;

  public ACSCache(int maxSize, int expireMins) {
    this.maxSize = maxSize;
    this.expireMins = expireMins;
  }



  public void buildCache() {
    // check maxSize and expireMins to make sure they are always at least 1
    if (this.maxSize < 1) {
      this.maxSize = 1;
    }
    if (this.expireMins < 1) {
      this.expireMins = 1;
    }
    this.cache = CacheBuilder.newBuilder()
        .maximumSize(maxSize)
        .expireAfterWrite(expireMins, TimeUnit.MINUTES).build(this.createCacheLoader());
  }

  private CacheLoader<String, String> createCacheLoader() {
    return new CacheLoader<>() {
      @Override
      public String load(String key)  {
        return createExpensiveGraph(key);
}
