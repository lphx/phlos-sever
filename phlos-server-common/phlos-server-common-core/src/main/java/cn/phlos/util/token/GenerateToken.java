package cn.phlos.util.token;

import java.util.UUID;


import cn.phlos.util.core.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class GenerateToken {
	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 生成令牌
	 * 
	 * @param keyPrefix
	 *            令牌key前缀
	 * @param redisValue
	 *            redis存放的值
	 * @return 返回token
	 */
	public String createToken(String keyPrefix, String redisValue) {
		return createToken(keyPrefix, redisValue, null);
	}

	/**
	 * 生成令牌
	 * 
	 * @param keyPrefix
	 *            令牌key前缀
	 * @param redisValue
	 *            redis存放的值
	 * @param time
	 *            有效期
	 * @return 返回token
	 */
	public String createToken(String keyPrefix, String redisValue, Long time) {
		if (StringUtils.isEmpty(redisValue)) {
			new Exception("redisValue Not nul");
		}
		String token = keyPrefix + UUID.randomUUID().toString().replace("-", "");
		redisUtil.setString(token, redisValue, time);
		return token;
	}

	/**
	 * 根据token获取redis中的value值
	 * 
	 * @param token
	 * @return
	 */
	public String getToken(String token) {
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		String value = redisUtil.getString(token);
		return value;
	}

	/**
	 * 移除token
	 * 
	 * @param token
	 * @return
	 */
	public Boolean removeToken(String token) {
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		return redisUtil.delKey(token);

	}

}
