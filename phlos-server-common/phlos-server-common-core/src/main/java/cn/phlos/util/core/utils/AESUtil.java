package cn.phlos.util.core.utils;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

/**
 * 　AES对称加密算法
 * @Author: Penghong Li
 * @Date: Create in 10:22 2020/4/2
 */

public class AESUtil {
    private static Key jdkKey = null;
    private static Key bcKey = null;
    private static Cipher cipher;
    
    static {

        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
    
    static {
         
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            //keyGenerator.init(128, new SecureRandom("seedseedseed".getBytes()));
            //使用上面这种初始化方法可以特定种子来生成密钥，这样加密后的密文是唯一固定的。
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();

            //Key转换
            jdkKey = new SecretKeySpec(keyBytes, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
       
    }
    
    static {
       

        try {
            //使用BouncyCastle 的DES加密
            Security.addProvider(new BouncyCastleProvider());
            //生成Key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES","BC");
            keyGenerator.getProvider();
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();

            //Key转换
            bcKey = new SecretKeySpec(keyBytes, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
       
    }


    public static String jdkAESEncryption(String password){
        //生成Key
        try {
            //加密
            cipher.init(Cipher.ENCRYPT_MODE, jdkKey);
            byte[] encodeResult = cipher.doFinal(password.getBytes());
            return Hex.toHexString(encodeResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String jdkDecode(String password){
        try {
           
            //解密
            byte[] decode = Hex.decode(password);
            cipher.init(Cipher.DECRYPT_MODE, jdkKey);
            byte[] decodeResult = cipher.doFinal(decode);
            return new String(decodeResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String bcDecode(String password){
        try {

            //解密
            byte[] decode = Hex.decode(password);
            cipher.init(Cipher.DECRYPT_MODE, bcKey);
            byte[] decodeResult = cipher.doFinal(decode);
            return new String(decodeResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String bcAESEncryption(String password){
        try {
            //加密
            cipher.init(Cipher.ENCRYPT_MODE, bcKey);
            byte[] encodeResult = cipher.doFinal(password.getBytes());
            return Hex.toHexString(encodeResult);

          
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } 
        return null;
    }

   

   


    public static void main(String[] args) {
        String helloWorld = jdkAESEncryption("helloWorld");
        System.out.println("helloWorld = " + helloWorld);
        String decode = jdkDecode(helloWorld);
        System.out.println("decode = " + decode);
        String li = bcAESEncryption("1580780206");
        System.out.println("1580780206 = " + li);
        String decode1 = bcDecode(li);
        System.out.println("decode1 = " + decode1);

    }

}
