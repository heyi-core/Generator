package com.heyi.${project.dataBase}.baseutil;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * AES加密解密工具类
 *
 * @author M-Y
 */
public class PasswordUtil {

    /**
     * 内容加密
     */
    public static String encrypt(String data, String key, String iv) throws Exception {
        //"算法/模式/补码方式"NoPadding PkcsPadding
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        int blockSize = cipher.getBlockSize();

        byte[] dataBytes = data.getBytes();
        int plaintextLength = dataBytes.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }

        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        byte[] encrypted = cipher.doFinal(plaintext);

        return new Base64().encodeToString(encrypted);

    }

    /**
     * 内容解密
     */
    public static String desEncrypt(String data, String key, String iv) throws Exception {
        byte[] encrypted1 = new Base64().decode(data);

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

        byte[] original = cipher.doFinal(encrypted1);
        return new String(original);
    }

    /*------------------------------------------------------------------------------------------------------------------------------*/
    /*生成密码*/
    public static String encryptPassword(String password, String salt) {
        return SecureUtil.md5(password + salt);
    }

    /*生成盐*/
    public static String generateSalt() {
        return RandomUtil.randomString(6);
    }

    /*------------------------------------------------------------------------------------------------------------------------------*/
    /*解析token*/
    public static Claims parseJWT(String user, String tokenKey) {
        return Jwts.parser().setSigningKey(tokenKey).parseClaimsJws(user).getBody();
    }

    /*生成token*/
    public static String createJWT(String user, String jwtTimeout, String tokenKey) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date();
        JwtBuilder builder = Jwts.builder()
                .setId("JWT")
                .setIssuedAt(now)
                .setSubject(user)
                .signWith(SignatureAlgorithm.HS256, tokenKey);
        long lwtLong = Long.valueOf(jwtTimeout);
        if (lwtLong >= 0) {
            long expMillis = nowMillis + lwtLong;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }
}