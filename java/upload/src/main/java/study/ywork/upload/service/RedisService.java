package study.ywork.upload.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Service
public final class RedisService {
    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 指定缓存失效时间
     */
    public boolean expire(String key, long time) {
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }

        return true;
    }

    /**
     * 获取过期时间
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 获取缓存
     */
    public String get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置缓存
     */
    public boolean set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        return true;
    }

    /**
     * 设置缓存时间
     */
    public boolean set(String key, String value, long time) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            set(key, value);
        }

        return true;
    }

    /**
     * 设置缓存时间
     */
    public boolean set(String key, String value, long time, TimeUnit timeUnit) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, timeUnit);
        } else {
            set(key, value);
        }

        return true;
    }

    public Object hashGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    public boolean hashSet(String key, String item, Object value) {
        redisTemplate.opsForHash().put(key, item, value);
        return true;
    }

    public void hashDel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    public boolean hasHashKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }
}
