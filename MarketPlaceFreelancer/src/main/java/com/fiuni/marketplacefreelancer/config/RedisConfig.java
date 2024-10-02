package com.fiuni.marketplacefreelancer.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // TTL de 1 hora para "project"
        cacheConfigurations.put("project", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1)));

        // TTL de 30 minutos para "price"
        cacheConfigurations.put("price", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30)));

        // TTL de 10 minutos para "tag"
        cacheConfigurations.put("tag", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .withInitialCacheConfigurations(cacheConfigurations)
                .cacheDefaults(redisCacheConfiguration())
                .build();
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}
