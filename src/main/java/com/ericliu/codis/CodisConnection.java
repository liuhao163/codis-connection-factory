package com.ericliu.codis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

/**
 * @Author: liuhaoeric
 * Create time: 2018/02/26
 * Description:
 */
public class CodisConnection extends JedisConnection {

    protected Jedis jedis;

    public CodisConnection(Jedis jedis) {
        super(jedis);
        this.jedis=jedis;
    }

    public CodisConnection(Jedis jedis, Pool<Jedis> pool, int dbIndex) {
        super(jedis,pool,dbIndex);
        this.jedis=jedis;
    }


    @Override
    public void close() throws DataAccessException {
        super.close();
        jedis.close();
    }
}
