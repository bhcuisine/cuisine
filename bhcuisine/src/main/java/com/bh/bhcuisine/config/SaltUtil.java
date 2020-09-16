package com.bh.bhcuisine.config;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;

/**
 * 生成盐工具类
 */
public class SaltUtil {
    public static String getSalt() {
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        return salt;
    }
}
