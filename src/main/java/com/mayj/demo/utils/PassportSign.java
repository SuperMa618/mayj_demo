package com.mayj.demo.utils;

import gnu.crypto.mode.IMode;
import gnu.crypto.mode.ModeFactory;
import gnu.crypto.pad.IPad;
import gnu.crypto.pad.PadFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Author mayunjie
 * @Date 2022/7/21 15:59
 **/
public class PassportSign {

    private static final ArrayBlockingQueue<IPad> PADDING_QUEUE = new ArrayBlockingQueue<IPad>(1024);
    private static final ArrayBlockingQueue<IMode> MODE_QUEUE = new ArrayBlockingQueue<IMode>(1024);
    private static Base64 BASE64 = new Base64();

    /**
     * 解密
     *
     * @param enumConfig PassportSignKeyEnum
     * @param data       String
     * @return String
     */
    @SuppressWarnings("restriction")
    public static String decryptSign(PassportSignKeyEnum enumConfig, String data) {
        if (Helper.empty(data)) {
            return "";
        }
        try {
            byte[] ivByte = enumConfig.getSecret();
            byte[] keyByte = enumConfig.getKey();
            byte[] dataByte = BASE64.decode(data.replaceAll("-", "+").replaceAll("_", "/"));
            return decryptString(dataByte, keyByte, ivByte).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 加密
     *
     * @param enumConfig PassportSignKeyEnum
     * @param data       String
     * @return String
     */
    @SuppressWarnings("restriction")
    public static String encryptSign(PassportSignKeyEnum enumConfig, String data) {
        if (Helper.empty(data)) {
            return "";
        }
        try {
            int tmp = data.length() % 16;
            data = StringUtils.rightPad(data, tmp == 0 ? data.length() : data.length() + 16 - tmp, '\u0000');
            byte[] ivByte = enumConfig.getSecret();
            byte[] keyByte = enumConfig.getKey();

            byte[] encrypts = encryptString(data.getBytes(), keyByte, ivByte);
            String dataString = BASE64.encodeAsString(encrypts).replaceAll("/", "_").replaceAll("\\+", "-")
                    .trim();
            return Helper.handleEncodeData(data.length(), dataString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static IPad getPadding(int num) {
        IPad padding = PADDING_QUEUE.poll();
        if (padding == null) {
            padding = PadFactory.getInstance("PKCS7");
        } else {
            padding.reset();
        }
        padding.init(num);
        return padding;
    }

    private static IMode getMode() {
        IMode mode = MODE_QUEUE.poll();
        if (mode == null) {
            mode = ModeFactory.getInstance("CBC", "AES", 16);
        } else {
            mode.reset();
        }
        return mode;
    }

    /**
     * 加密 Block=128
     *
     * @param source String 源字符
     * @param mykey  byte[] Key
     * @param iv     byte[] 向量
     * @return byte[] 加密后的byte
     */
    private static byte[] encryptString(byte[] source, byte[] mykey, byte[] iv) {
        byte[] ct = null;
        IPad padding = getPadding(8);
        IMode mode = getMode();
        try {
            Map<String, Object> attributes = new HashMap<>();
            byte[] pad = padding.pad(source, 0, source.length);
            byte[] pt = null;
            //判断是否要补空
            if (pad.length == 16) {
                pt = new byte[source.length];
                System.arraycopy(source, 0, pt, 0, source.length);
            } else {
                pt = new byte[source.length + pad.length];
                System.arraycopy(source, 0, pt, 0, source.length);
                System.arraycopy(pad, 0, pt, source.length, pad.length);
            }
            ct = new byte[pt.length];
            attributes.put(IMode.KEY_MATERIAL, mykey);
            attributes.put(IMode.CIPHER_BLOCK_SIZE, 16);
            attributes.put(IMode.IV, iv);
            attributes.put(IMode.STATE, IMode.ENCRYPTION);
            mode.init(attributes);
            for (int i = 0; i + 16 <= pt.length; i += 16) {
                mode.update(pt, i, ct, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (padding != null) {
                    PADDING_QUEUE.put(padding);
                }
                if (mode != null) {
                    MODE_QUEUE.put(mode);
                }
            } catch (InterruptedException ignore) {

            }
        }

        return ct;
    }

    /**
     * 解密
     *
     * @param source byte[] 加密后的byte[]
     * @param mykey  byte[] Key
     * @param iv     byte[] 向量
     * @return String 加密前的byte[]
     */
    private static String decryptString(byte[] source, byte[] mykey, byte[] iv) {
        String decrypt = null;
        IPad padding = getPadding(16);
        IMode mode = getMode();
        try {
            Map<String, Object> attributes = new HashMap<>();
            byte[] ct = new byte[source.length];
            attributes.put(IMode.KEY_MATERIAL, mykey);
            attributes.put(IMode.CIPHER_BLOCK_SIZE, 16);
            attributes.put(IMode.IV, iv);
            attributes.put(IMode.STATE, IMode.DECRYPTION);
            mode.init(attributes);
            for (int i = 0; i + 16 <= source.length; i += 16) {
                mode.update(source, i, ct, i);
            }
            byte[] out;
            try {
                int unpad = padding.unpad(ct, 0, ct.length);
                out = new byte[ct.length - unpad];
                System.arraycopy(ct, 0, out, 0, out.length);
            } catch (Exception e) {
                out = new byte[ct.length];
                System.arraycopy(ct, 0, out, 0, out.length);
            }
            decrypt = new String(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (padding != null) {
                    PADDING_QUEUE.put(padding);
                }
                if (mode != null) {
                    MODE_QUEUE.put(mode);
                }
            } catch (InterruptedException ignore) {

            }
        }
        return decrypt;
    }

    /**
     * appEncode
     * @param data string
     * @param key string
     * @param secret string
     * @return string
     */
    @SuppressWarnings("restriction")
    public static String appEncode(String data, String key, String secret) {
        try {
            data = StringUtils.rightPad(data, 18, '\u0000');
            byte[] ivByte = secret.getBytes();
            byte[] keyByte = hexToString(DigestUtils.md5Hex(key) + DigestUtils.md5Hex(key + "2016"));
            byte[] encrypts = encryptString(data.getBytes(), keyByte, ivByte);
            return BASE64.encodeAsString(encrypts).replaceAll("/", "_").replaceAll("\\+", "-").trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * appDecode
     * @param data string
     * @param key string
     * @param secret string
     * @return string
     */
    @SuppressWarnings("restriction")
    public static String appDecode(String data, String key, String secret) {
        try {
            byte[] ivByte = secret.getBytes();
            byte[] keyByte = hexToString(DigestUtils.md5Hex(key) + DigestUtils.md5Hex(key + "2016"));
            byte[] dataByte = BASE64.decode(data.replaceAll("-", "+").replaceAll("_", "/"));
            return decryptString(dataByte, keyByte, ivByte).trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
}
