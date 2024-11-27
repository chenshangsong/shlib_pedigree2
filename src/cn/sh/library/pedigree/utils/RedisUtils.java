package cn.sh.library.pedigree.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import cn.sh.library.pedigree.common.onlineInfomation.RequestFilter;

/**
 * chenss 20230322
 * 
 * @author chenss
 *
 */

@Component
public class RedisUtils {

	public static String key_work = "key_work_";
	public static String key_work_bm = "key_work_bm_";// 编目系统用 20240911
	public static String key_work_convertImg = "key_work_cimg_";
//内网全文地址
	public static String key_fulltextIn_pdf = "key_fulltextIn_pdf_";
	public static String key_work_view = "key_work_view_";
	public static String key_family = "key_family_";// 姓氏
	@Resource
	public RedisTemplate<String, Object> redisTemplate;

	// 每分钟限流访问次数 30次
	private static final Integer MAX_REQUESTS_PER_MINUTE = 30;

	public boolean ifLimitVisit(Integer vistCount, Integer outTime) {
		vistCount = vistCount == null ? MAX_REQUESTS_PER_MINUTE : vistCount;
		outTime = outTime == null ? 1 : outTime;
		String clientIp = IPUtils.getIpAddr();
		String redisKey = "rate_limit_" + clientIp;
		//如果是外网IP，则1分钟只让访问20次
		if(!IPUtils.isPrivateIP(true)){
			vistCount=20;
		}
		Integer requestCount = StringUtilC.getInteger(this.get(redisKey)) == null ? 0
				: StringUtilC.getInteger(this.get(redisKey));
		if (requestCount == null || requestCount < 1) {
			this.set(redisKey, 1);
		} else {
			requestCount = requestCount + 1;
			this.set(redisKey, requestCount);

		}
		//设置过期时间，1分钟。1分钟后，会自动重置
		redisTemplate.expire(redisKey, outTime, TimeUnit.MINUTES);
		//如果1分钟内，缓存中的数据大于最大量，则返回错误。
		if (requestCount > vistCount) {
			return false;
		}

		return true;
	}

	/**
	 * 写入缓存
	 *
	 * @param key   redis键
	 * @param value redis值
	 * @return 是否成功
	 */
	public boolean set(final String key, Object value) {
		boolean result = false;
		try {
			ValueOperations<String, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 写入缓存设置时效时间
	 *
	 * @param key   redis键
	 * @param value redis值
	 * @return 是否成功
	 */
	public boolean set(final String key, String value, Long expireTime) {
		boolean result = false;
		try {
			ValueOperations<String, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 批量删除对应的键值对
	 *
	 * @param keys Redis键名数组
	 */
	public void removeByKeys(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 批量删除Redis key
	 *
	 * @param pattern 键名包含字符串（如：myKey*）
	 */
	public void removePattern(final String pattern) {
		Set<String> keys = redisTemplate.keys(pattern);
		if (keys != null && keys.size() > 0)
			redisTemplate.delete(keys);
	}

	/**
	 * 删除key,也删除对应的value
	 *
	 * @param key Redis键名
	 */
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 判断缓存中是否有对应的value
	 *
	 * @param key Redis键名
	 * @return 是否存在
	 */
	public Boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 读取缓存
	 *
	 * @param key Redis键名
	 * @return 是否存在
	 */
	public Object get(final String key) {
		Object result = null;
		ValueOperations<String, Object> operations = redisTemplate.opsForValue();
		result = operations.get(key);
		return result;
	}

	/**
	 * 哈希 添加
	 *
	 * @param key     Redis键
	 * @param hashKey 哈希键
	 * @param value   哈希值
	 */
	public void hmSet(String key, String hashKey, Object value) {
		HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
		hash.put(key, hashKey, value);
	}

	/**
	 * 哈希获取数据
	 *
	 * @param key     Redis键
	 * @param hashKey 哈希键
	 * @return 哈希值
	 */
	public String hmGet(String key, String hashKey) {
		HashOperations<String, String, String> hash = redisTemplate.opsForHash();
		return hash.get(key, hashKey);
	}

	/**
	 * 判断hash是否存在键
	 *
	 * @param key     Redis键
	 * @param hashKey 哈希键
	 * @return 是否存在
	 */
	public boolean hmHasKey(String key, String hashKey) {
		HashOperations<String, String, String> hash = redisTemplate.opsForHash();
		return hash.hasKey(key, hashKey);
	}

	/**
	 * 删除hash中一条或多条数据
	 *
	 * @param key      Redis键
	 * @param hashKeys 哈希键名数组
	 * @return 删除数量
	 */
	public long hmRemove(String key, String... hashKeys) {
		HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
		hash.delete(key, hashKeys);
		return 1;
	}

	/**
	 * 获取所有哈希键值对
	 *
	 * @param key Redis键名
	 * @return 哈希Map
	 */
	public Map<String, Object> hashMapGet(String key) {
		HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
		return hash.entries(key);
	}

	/**
	 * 保存Map到哈希
	 *
	 * @param key Redis键名
	 * @param map 哈希Map
	 */
	public void hashMapSet(String key, Map<String, Object> map) {
		HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
		hash.putAll(key, map);
	}

	/**
	 * 列表-追加值
	 *
	 * @param key   Redis键名
	 * @param value 列表值
	 */
	public void lPush(String key, String value) {
		ListOperations<String, Object> list = redisTemplate.opsForList();
		list.leftPush(key, value);
	}

	/**
	 * 列表-删除值
	 *
	 * @param key   Redis键名
	 * @param value 列表值
	 */
	public void lRemove(String key, String value) {
		ListOperations<String, Object> list = redisTemplate.opsForList();
		list.remove(key, 0, value);
	}

	/**
	 * 列表-获取指定范围数据
	 *
	 * @param key   Redis键名
	 * @param start 开始行号（start:0，end:-1查询所有值）
	 * @param end   结束行号
	 * @return 列表
	 */
	public List<Object> lRange(String key, long start, long end) {
		ListOperations<String, Object> list = redisTemplate.opsForList();
		return list.range(key, start, end);
	}

	/**
	 * 集合添加
	 *
	 * @param key   Redis键名
	 * @param value 值
	 */
	public void add(String key, Object value) {
		SetOperations<String, Object> set = redisTemplate.opsForSet();
		set.add(key, value);
	}

	/**
	 * 集合获取
	 *
	 * @param key Redis键名
	 * @return 集合
	 */
	public Set<Object> setMembers(String key) {
		SetOperations<String, Object> set = redisTemplate.opsForSet();
		return set.members(key);
	}

	/**
	 * 有序集合添加
	 *
	 * @param key   Redis键名
	 * @param value 值
	 * @param score 排序号
	 */
	public void zAdd(String key, String value, double score) {
		ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
		zSet.add(key, value, score);
	}

	/**
	 * 有序集合-获取指定范围
	 *
	 * @param key        Redis键
	 * @param startScore 开始序号
	 * @param endScore   结束序号
	 * @return 集合
	 */
	public Set<Object> rangeByScore(String key, double startScore, double endScore) {
		ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
		return zset.rangeByScore(key, startScore, endScore);
	}

	/**
	 * 模糊查询Redis键名
	 *
	 * @param pattern 键名包含字符串（如：myKey*）
	 * @return 集合
	 */
	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	// 序列化
	public static byte[] serialize(Object obj) {
		ObjectOutputStream obi = null;
		ByteArrayOutputStream bai = null;
		try {
			bai = new ByteArrayOutputStream();
			obi = new ObjectOutputStream(bai);
			obi.writeObject(obj);
			byte[] byt = bai.toByteArray();
			return byt;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 反序列化
	public static Object unserizlize(byte[] byt) {
		ObjectInputStream oii = null;
		ByteArrayInputStream bis = null;
		bis = new ByteArrayInputStream(byt);
		try {
			oii = new ObjectInputStream(bis);
			Object obj = oii.readObject();
			return obj;
		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}
}