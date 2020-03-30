package cn.phlos.util.twitter;

import java.util.Date;

/**
 * 生成雪花算法
 */
public class SnowflakeIdUtils {
	private static SnowflakeIdWorker idWorker;
	static {
		// 使用静态代码块初始化 SnowflakeIdWorker
		idWorker = new SnowflakeIdWorker(1, 1);
	}

	public static String nextId() {
		return idWorker.nextId() + "";
	}

	public static String orderId(){
		//生成订单号，如2020/03/30,生成为20200330xxxxxxxxxxxxxxx
		String date = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
		return date+idWorker.nextId();
	}

	public static void main(String[] args) {
		String s = orderId();
		System.out.println("s = " + s);
	}
}
