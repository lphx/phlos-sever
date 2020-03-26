package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016101700711422";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCxdgn+i4OqcX7YV5QRcjTMJtfkFuDvSE/LRE5+aS08dhB8ftIdsr+N6SXnN6UljOvsp/EtvexOJkKaV8WEQyJ7ywhQZpFPv8iCnmlum3koUgln+763lDKXMIWe8twikxDzo7bMs/nPp0E11Vmz9uuGj+Dg3A/d1+Vm2BeTMzkqFlJlOsh9jPyoA3cJI7HYhroB2G/lsMNojkEJbiRHrwHTtVJxQOaNoDlvF+ypbI0dHJAyHEfQ7A1CtIisGWnuUYA4C3HksZXhTL3g02tfBXu97jWwIYqvR8tclBgtXu9dkhxbhg6qUHA5GaZ7w6cEwhn6opk5wTFygJ4asOuo+WwxAgMBAAECggEADTpEMnIJxWmiT9PvWIGPx+vZScoeyBc2w2r7AtcqJQiT4AO/+7C4MIbBaYOvTqY7a690/bjbZVPawRBry5kJd/rBSuTqTex4sKvIJhN2q3e59cNBCwQD5VsbiqaFnBokOFTqqkV8CGtqI+4cOprcIXKWC4y7rgiKs+jFkRZPW4CgRQf+bbv+a3WKVTyjs9uGFp1bOfynWbsln0F0iR7/49v3CZitKu5fLF3rXffhMHgH+4+w+55JVAW66B3TZjEqpv7CaFzMKWiefXIv+4hUV6XpksmbkwRUCcNwcpMwU6N7XefZvXe3CG59bmV8bwEBC+N5MIPCLZ9Tnktc8tkOAQKBgQD5tvL3lBd24R/bYzYFq1Iv3Oqs6X0t8ZvoRsfjpG4Hfp9UDM+YE8nnYV6mufC8cTQz4w4V99vR+bEnog/TbJdr6dI5LGgEmT8ajjg7Chl9sTZ0+KL7pBgEu2urHCmqVIrR2lEHFxYUOev2euSXNkB1LWH6cxae04PQMxA+exdt4QKBgQC17YUbLz2mganrrslOcyeLwvW8T8tU/GWes0plmwzwVJHHzxo4pLqXtsdHg3aeZYQIe014ydoMGq8QB1OOpR+LT0ADXWfh+Y/EBSsE2ZiEawi1GTiGux3MSNR2L1w2RBlsSSypzij8qfCkeu17a+i47lhofBij16ni8G7GHPSoUQKBgCBu4p/FXEbUdo7IE/wohEQzLUfIC7or51QD3XJzqNbQW2FPSKc36sFipCiDfgD7swwKMYoo5yQeiiPbqLKNKB2cAPHsEpaGUOviDOueJDFvEuOeZ4zHg1caAKH4xnCZCm63LE7/cJ6v5zXcQFRjd3vAqTkeOZHnZvdjqnhzBFgBAoGBAKX15EwVP09Kz8ChdSCsqfG0S0mbHSkNCoLImeCwnhALheVZh1kO5O0LnyUcxzPd9aFrIhRDRzdtjC+6Dz58YZdLF2E6b07Uw5KVCFZnTdvWbF4QgOFfhuJZuf+KWrtiSjLfWfAZkohTWqQhQ80odmdlfp7qPewRC/hrTxJJTeLRAoGBAPNjlqQammXCM4j0a+160x/HFIx3RA2qtpDR8nTjN4PBQSdVccksZjX5onEI6ueedguz5NJsOx2amm/R7QrCZ7Hkpnk6o3ZySIqNVAHdJ1JLRIJkiB6LHY5UW+JGNIgyWanMidFSqsCUTOsbZBVv7d7YrTmuo97Dtf8wFNaTAjBO";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlHVOCLW/MbLNogfhhPRv3KEELsZ4uQkqWji7aSAvvWwuB/eLcU9cq2IwECjZXLcP/JySDnu5m4TPS5fsD0nH8KH1naGre6akbonHJiKyD9+VFcP9ChZ+7Y3xlYJtEC8oDOuXqFSj2oFD2lfOkARk2FVmoHp9HcVAj+JE9uR7MrrnAaO3sy+7FDxFdCD/o/F+dOSBAmCI4hMG7LrOfnK/dzZZW3gBLOarpLamTLBpSs4HJRgE/igpHSl2MxCWbDVRYDthVd1/xZjT6+C/xMHRMh/JEe75JXzunGigTpCreapdUN/jzm60Rjoom5rRQCAJ1zHJ+BIBa/lWkf8U+lxQ4wIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://c28a519884.zicp.vip:52533/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://c28a519884.zicp.vip:52533/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "D:/ali/log";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

