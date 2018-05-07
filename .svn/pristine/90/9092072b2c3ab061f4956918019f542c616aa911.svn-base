/** 
 * Project Name:spider-seeds-1229 
 * File Name:RedisProxyUtil.java 
 * Package Name:cn.jj.utils 
 * Date:2018年2月9日 上午10:20:13 
 * author 汤玉林
 */ 
package cn.jj.utils;

import cn.jj.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Description: TODO
 * @author 汤玉林
 * @date 2018年2月9日 上午10:20:13 
 */
public class RedisProxyUtil {

	private static JedisPool jedisPool = null;

	// redis中用于存储代理ip的队列
	public static final String PROXY_IP_PORT = "proxy.ip.port";

	/**
	 * 
	 * @Description 用于获取代理ip的池
	 * @author 汤玉林
	 * @return 
	 * @date 2018年2月9日 上午9:44:45
	 * @action getProxyJedisPool
	 */
	public static Jedis getProxyJedisPool() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(Config.REDIS_MAX_IDLE);
		poolConfig.setMaxTotal(Config.REDIS_MAX_TOTAL);
		poolConfig.setMaxWaitMillis(Config.REDIS_MAX_WAIT_MILLS);
		poolConfig.setTestOnBorrow(Config.REDIS_TEST_ON_BORROW);
		//jedisPool = new JedisPool(poolConfig, Config.REDIS_IP, Config.REDIS_PORT);
		jedisPool = new JedisPool(poolConfig, Config.PROXY_IP,  Config.REDIS_PORT, 2000, null, 0);
		Jedis jedis = jedisPool.getResource();
		return jedis;
	}
}
