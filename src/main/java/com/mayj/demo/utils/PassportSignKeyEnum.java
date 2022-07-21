package com.mayj.demo.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public enum PassportSignKeyEnum {
    ALIUID("ali718****lei", "lB6x69*******+IEl1w==");
    /**
     * 秘钥key
     */
    private byte[] key;
    /**
     * base64秘钥
     */
    private byte[] secret;

    /**
     * 原始key
     */
    private String okey;

    PassportSignKeyEnum(String key, String secret) {
        this.okey = key;
        Base64 base64 = new Base64();
        this.secret = base64.decode(secret);
        this.key = hexToString(DigestUtils.md5Hex(key) + DigestUtils.md5Hex(key + "2016"));
    }

    private static byte[] hexToString(String hex) {
        byte[] output = new byte[(hex.length() + 1) / 2];
        for (int i = hex.length() - 1; i >= 0; i -= 2) {
            int from = i - 1;
            if (from < 0) {
                from = 0;
            }
            String str = hex.substring(from, i + 1);
            output[i / 2] = (byte)Integer.parseInt(str, 16);
        }
        return output;
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] getSecret() {
        return secret;
    }

    public String getOkey() {
        return okey;
    }
}
