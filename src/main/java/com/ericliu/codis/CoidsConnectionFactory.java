package com.ericliu.codis;

import io.codis.jodis.JedisResourcePool;
import io.codis.jodis.RoundRobinJedisPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import redis.clients.jedis.Jedis;

import java.io.IOException;

//import org.springframework.lang.Nullable;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:2018/1/5
 */
public class CoidsConnectionFactory implements InitializingBean, DisposableBean, RedisConnectionFactory {

    Logger logger = LoggerFactory.getLogger(CoidsConnectionFactory.class);

    private String zkServer;
    private Integer timeout = 30000;
    private String zkProxyDir;
    private Integer database = 0;
    private String password;

    private boolean convertPipelineAndTxResults = true;
    private JedisResourcePool jedisResourcePool;

    @Override
    public RedisConnection getConnection() {
        JedisConnection connection;
        Jedis jedis = null;
        jedis = jedisResourcePool.getResource();
        connection = new CodisConnection(jedis);
        return connection;
    }

    public int getDatabase() {
        return database;
    }

    @Override
    public RedisClusterConnection getClusterConnection() {
        logger.warn("CoidsConnectionFactory not suppourt getClusterConnection");
        return null;
    }

    @Override
    public boolean getConvertPipelineAndTxResults() {
        logger.warn("CoidsConnectionFactory not suppourt getConvertPipelineAndTxResults default true");
        return true;
    }

    public void setConvertPipelineAndTxResults(boolean convertPipelineAndTxResults) {
        this.convertPipelineAndTxResults = convertPipelineAndTxResults;
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        logger.warn("CoidsConnectionFactory not suppourt getSentinelConnection");
        return null;
    }

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException e) {
        logger.warn("CoidsConnectionFactory not suppourt translateExceptionIfPossible");
        return null;
    }

    @Override
    public void destroy() throws Exception {
        try {
            jedisResourcePool.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jedisResourcePool = null;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        jedisResourcePool = RoundRobinJedisPool.create()
                .curatorClient(zkServer, timeout)
                .zkProxyDir(zkProxyDir)
                .database(database)
                .password(password)
                .build();
    }

    public void setZkServer(String zkServer) {
        this.zkServer = zkServer;
    }

    public String getZkServer() {
        return zkServer;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getZkProxyDir() {
        return zkProxyDir;
    }

    public void setZkProxyDir(String zkProxyDir) {
        this.zkProxyDir = zkProxyDir;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
