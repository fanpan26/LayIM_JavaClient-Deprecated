package util.cache;


import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/**
 * Created by pz on 16/11/24.
 */
public class EHCacheUtil {

    public <K,V> CacheManager getCacheManager(Class<K> kClass,Class<V> vClass,String configureName){
        CacheManager manager =  CacheManagerBuilder.newCacheManagerBuilder()
                .withCache(configureName,
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(kClass, vClass, ResourcePoolsBuilder.heap(10)))
                .build();
        manager.init();
        return manager;
    }

    public <K,V> Cache<K,V> getCache(Class<K> kClass,Class<V> vClass,String cacheName,String configureName,CacheManager manager) {

        Cache<K, V> cache =
                manager.getCache(configureName, kClass, vClass);
        /*Cache<K, V> cache = manager.createCache(cacheName,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(kClass, vClass, ResourcePoolsBuilder.heap(10)).build());*/
        return cache;
    }

    public void closeCache(CacheManager manager,String configureName){
        manager.removeCache(configureName);
        manager.close();
    }


}
