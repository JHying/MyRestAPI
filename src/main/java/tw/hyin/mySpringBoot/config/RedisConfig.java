package tw.hyin.mySpringBoot.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.time.Duration;

/*
`maxInactiveIntervalInSeconds`: session的過期時間，預設是1800秒，如果設定了此屬性，專案中的`server.session.timeout`屬性將失效
`redisNamespace`: 設定redis 的名稱空間，就是設定資料儲存到哪裡(相當於關係型資料庫中的庫)
`redisFlushMode`: redis 操作模式，是否立即重新整理到redis資料庫中，預設的是不會的，系統並不是在剛設定就重新整理，而是選擇在某個時間點重新整理到資料庫中
*/
@Configuration
@EnableRedisHttpSession(redisNamespace = "mySpringSession", maxInactiveIntervalInSeconds = 60 * 60)
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        //This gives us more control over the default configuration
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        //customize the default serialization strategy for in-flight cache creation
        return (builder) -> builder
                .withCacheConfiguration("questionCache",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(60)))
                .withCacheConfiguration("questionnaireCache",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30)));
    }

}
