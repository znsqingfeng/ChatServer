package chat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zhanggd on 2016/4/28.
 */
public class RedisClient {
    private static Logger logger = LoggerFactory.getLogger(RedisClient.class);
    private static JedisPool jedisPool = null;

    static {
        try {
            //获取配置
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("redis.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            String addr = properties.getProperty("addr");
            int port = Integer.parseInt(properties.getProperty("port"));
            String auth = properties.getProperty("auth");
            int maxIDLE = Integer.parseInt(properties.getProperty("max_idle"));
            int timeout = Integer.parseInt(properties.getProperty("timeout"));

            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(maxIDLE);
            jedisPool = new JedisPool(config, addr, port, timeout, auth);

        } catch (Exception e) {
            logger.error("error creating jedis pool");
        }
    }

    public static Jedis getJedis() {
        try {
            if (jedisPool != null)
                return jedisPool.getResource();
        } catch (Exception e) {
            logger.error("error create redis pool ",e);
        }
        return null;
    }
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }
}
