package com.mayj.demo.utils;

import org.apache.catalina.security.SecurityUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author mayunjie
 * @Date 2022/7/21 15:54
 **/
public class Helper {

    private static LinkedBlockingDeque<Runnable> queue = new LinkedBlockingDeque<>();
    private static Pattern INTEGER_PATTERN = Pattern.compile("^[-\\+]?[\\d]+$");
    private static Pattern DOUBLE_PATTERN = Pattern.compile("^[-\\+]?[\\d]*\\.?[\\d]+$");
    private static Pattern MOBILE_VALID_PATTERN = Pattern.compile("^1[0-9]{10}$");
    private static Pattern PASSWD_VALID_ALL_NUMBER_PATTERN = Pattern.compile("^\\d+$");
    private static Pattern PASSWD_VALID_ALL_CHAR_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    private static Pattern PASSWD_VALID_HAS_SPACE_PATTERN = Pattern.compile("\\w*\\s+\\w*");
    private static Pattern PASSWD_VALID_ALL_PUNCTUATION_PATTERN = Pattern.compile("^[-_+=!@#$%^&*?.,]+$");
    private static Pattern PASSWD_VALID_PATTERN = Pattern.compile("^[0-9a-zA-Z-_+=!@#$%^&*?.,]+$");
    private static Pattern USERNAME_VALID_START_SLIDE_LINE_PATTERN = Pattern.compile("^_\\w*");
    private static Pattern USERNAME_VALID_END_SLIDE_LINE_PATTERN = Pattern.compile("\\w*_$");
    private static Pattern USERNAME_VALID_HAS_SPACE_PATTERN = Pattern.compile("\\w*\\s+\\w*");
    private static Pattern USERNAME_VALID_DOUBLE_PUNCTUATION_PATTERN = Pattern.compile("[\\w\\W]*(\\.\\.|__|--)[\\w\\W]*");
    private static Pattern USERNAME_VALID_PATTERN = Pattern.compile("^[0-9a-zA-Z._-]+$");
    private static Pattern EMAIL_VALID_PATTERN = Pattern.compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\"
            + ".[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");
    private static Pattern CARD_NUM_VALID_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");
    private static Pattern AZ_PATTERN = Pattern.compile("[A-Z]");
    private static Pattern UNDERLINE_PATTERN = Pattern.compile("_(\\w)");
    private static Pattern DEVICE_INFO_PATTERN = Pattern.compile("\\([\\s\\S]*?\\)");
    private static Pattern FILTER_IPHONE_OS_VERSION_PATTERN = Pattern.compile("iPhone OS (\\S*) like");
    private static Pattern FILTER_ANDROID_PATTERN = Pattern.compile("; ([^;]*) Build");
    private static Pattern FILTER_ANDROID_OS_VERSION_PATTERN = Pattern.compile("Android (\\S*);");
    private static Pattern FILTER_IPAD_OS_VERSION_PATTERN = Pattern.compile("OS (\\S*) like");
    private static Pattern REMOVE_SPECIAL_CHAR = Pattern.compile("\t|\r|\n");
    private static String CURRENCY_FEN_REGEX = "\\-?[0-9]+";
    private static Pattern LINE_PATTERN = Pattern.compile("_(\\w)");

    private static ThreadFactory threadFactory = r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    };

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            128, 512,
            128, TimeUnit.SECONDS,
            queue,
            threadFactory
    );

    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> x) {
        return CompletableFuture.supplyAsync(x, executor);
    }

    public static CompletableFuture<Void> runAsync(Runnable x) {
        return CompletableFuture.runAsync(x, executor);
    }

    public static String generateToken(int length) {
        if (length <= 0) {
            length = 12;
        }
        if (length > 10 && length < 20) {
            String randomString = Helper.randomString(length, 0);
            Long time = System.currentTimeMillis() / 1000;
            return randomString + time;
        } else {
            return "";
        }
    }

    public static String randomString(int length, int isNum) {
        if (length <= 0) {
            length = 12;
        }
        if (isNum != 1) {
            isNum = 0;
        }
        String string = "";
        Random random = new Random();
        if (isNum == 1) {
            for (int i = 0; i < length; i++) {
                string += random.nextInt(10);
            }
        } else {
            String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            for (int i = 0; i < length; i++) {
                string += chars.charAt(random.nextInt(62));
            }
        }
        return string;
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean empty(String str) {
        return null == str || "".equals(str);
    }

    public static String getString(String str) {
        return empty(str) ? "" : str;
    }

    public static String strOrNull(String str) {
        return empty(str) ? null : str;
    }

    public static String getLogString(Map<String, String> map) {
        StringBuilder res = new StringBuilder("");
        for (String key : map.keySet()) {
            res.append(key);
            res.append("->");
            res.append(map.get(key));
            res.append("|");
        }
        return res.toString();
    }

    public static Map<String, String> turnIntoTreeMap(Map map) {
        Map<String, String> res = new TreeMap<>();
        for (Object key : map.keySet()) {
            res.put(key.toString(), map.get(key).toString());
        }
        return res;
    }

    public static String getDataLogString(Map<String, String> map) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("aliuid", getString(map.get("aliuid")));
        params.put("appkey", getString(map.get("appkey")));
        params.put("source", getString(map.get("source")));
        params.put("data_id", getString(map.get("data_id")));
        params.put("sports_class", getString(map.get("sports_class")));
        params.put("sports_type", getString(map.get("sports_type")));
        params.put("num", getString(map.get("num")));
        params.put("unit", getString(map.get("unit")));
        params.put("calorie", getString(map.get("calorie")));
        params.put("sub_num", getString(map.get("sub_num")));
        params.put("sub_unit", getString(map.get("sub_unit")));
        params.put("aver_speed", getString(map.get("aver_speed")));
        params.put("max_speed", getString(map.get("max_speed")));
        params.put("aver_heartrate", getString(map.get("aver_heartrate")));
        params.put("max_heartrate", getString(map.get("max_heartrate")));
        params.put("min_heartrate", getString(map.get("min_heartrate")));
        params.put("trail", getString(map.get("trail")));
        params.put("stime", getString(map.get("stime")));
        params.put("etime", getString(map.get("etime")));
        params.put("device_type", getString(map.get("device_type")));
        params.put("device_name", getString(map.get("device_name")));
        params.put("device_model", getString(map.get("device_model")));
        params.put("match", getString(map.get("match")));
        params.put("distance", getString(map.get("distance")));
        params.put("time", getString(map.get("time")));
        params.put("country", getString(map.get("country")));
        params.put("province", getString(map.get("province")));
        params.put("city", getString(map.get("city")));
        params.put("start_point", getString(map.get("start_point")));
        params.put("end_point", getString(map.get("end_point")));
        params.put("sync_time", getString(map.get("sync_time")));
        params.put("age", getString(map.get("age")));
        params.put("height", getString(map.get("height")));
        params.put("weight", getString(map.get("weight")));
        params.put("heart_rate", getString(map.get("heart_rate")));
        params.put("fat", getString(map.get("fat")));
        params.put("muscle", getString(map.get("muscle")));
        params.put("water", getString(map.get("water")));
        params.put("bone", getString(map.get("bone")));
        params.put("protein", getString(map.get("protein")));
        params.put("metabolize", getString(map.get("metabolize")));
        params.put("bmi", getString(map.get("bmi")));
        params.put("steps", getString(map.get("steps")));
        params.put("all_time", getString(map.get("all_time")));
        params.put("deep_time", getString(map.get("deep_time")));
        params.put("shallow_time", getString(map.get("shallow_time")));
        params.put("sober_time", getString(map.get("sober_time")));
        params.put("match_type", getString(map.get("match_type")));
        params.put("ranking", getString(map.get("ranking")));
        params.put("name", getString(map.get("name")));
        params.put("gender", getString(map.get("gender")));
        params.put("type", getString(map.get("type")));
        params.put("card_type", getString(map.get("card_type")));
        params.put("card_id", getString(map.get("card_id")));
        params.put("mobile", getString(map.get("mobile")));
        params.put("birthday", getString(map.get("birthday")));
        params.put("match_group", getString(map.get("match_group")));
        params.put("score", getString(map.get("score")));
        params.put("match_time", getString(map.get("match_time")));
        params.put("gunshot_score", getString(map.get("gunshot_score")));
        params.put("gunshot_ranking", getString(map.get("gunshot_ranking")));
        params.put("speed", getString(map.get("speed")));
        params.put("project", getString(map.get("project")));
        params.put("sub_score", getString(map.get("sub_score")));
        params.put("sub_point", getString(map.get("sub_point")));
        return getLogString(params);
    }

    /**
     * 判断字符串是否为数字
     *
     * @param str string
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        return isInteger(str) || isDouble(str);
    }

    /**
     * 判断字符串是否为整型数(包括int 和 long)字符串
     *
     * @param str string
     * @return boolean
     */
    public static boolean isInteger(String str) {
        return !empty(str) && INTEGER_PATTERN.matcher(str).matches();
    }

    /**
     * 判断字符串是否为浮点数型(包括float 和 double)字符串
     *
     * @param str string
     * @return boolean
     */
    public static boolean isDouble(String str) {
        return !empty(str) && DOUBLE_PATTERN.matcher(str).matches();
    }

    /**
     * 将时间转换为时间戳(精确到秒)
     *
     * @param str string
     * @return Integer
     */
    public static Integer dateToTime(String str) {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = simpleDateFormat.parse(str);
        } catch (ParseException e) {
            //e.printStackTrace();
            return 0;
        }
        if (null == date) {
            return 0;
        }
        return Integer.valueOf(date.getTime() / 1000 + "");
    }

    /**
     * 将时间戳(精确到秒)转换为时间
     *
     * @param str string
     * @return String
     */
    public static String timeToDate(String str) {
        if (str.length() == 10) {
            str = str + "000";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(str);
        Date date = new Date(lt);
        return simpleDateFormat.format(date);
    }

    /**
     * 将时间戳(精确到秒)转换为时间(支持自定义格式)
     *
     * @param str string
     * @return String
     */
    public static String timeToDate(String str, String format) {
        if (str.length() == 10) {
            str = str + "000";
        }
        SimpleDateFormat simpleDateFormat;
        if (!empty(format)) {
            simpleDateFormat = new SimpleDateFormat(format);
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        long lt = new Long(str);
        Date date = new Date(lt);
        return simpleDateFormat.format(date);
    }

    /**
     * 将时间入参转为Date
     *
     * @param str
     * @return
     */
    public static Date strToDate(String str, String format) {
        if (empty(str)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat(format);
        Date date;
        try {
            date = simpleDateFormat.parse(str);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

    public static Date strToDate(String str) {
        return strToDate(str, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return Integer
     */
    public static Integer time() {
        long time = System.currentTimeMillis();
        return Integer.parseInt((time / 1000) + "");
    }

    /**
     * 验证身份证是否有效
     *
     * @param idCard string
     * @return int 0成功,1位数有误,2格式错误,3校验码错误,4地区错误
     */
    public static Integer idCardValid(String idCard) {
        if (empty(idCard)) {
            return 2;
        }
        Map<String, String> area = new HashMap<String, String>(16);
        area.put("11", "北京");
        area.put("12", "天津");
        area.put("13", "河北");
        area.put("14", "山西");
        area.put("15", "内蒙古");
        area.put("21", "辽宁");
        area.put("22", "吉林");
        area.put("23", "黑龙江");
        area.put("31", "上海");
        area.put("32", "江苏");
        area.put("33", "浙江");
        area.put("34", "安徽");
        area.put("35", "福建");
        area.put("36", "江西");
        area.put("37", "山东");
        area.put("41", "河南");
        area.put("42", "湖北");
        area.put("43", "湖南");
        area.put("44", "广东");
        area.put("45", "广西");
        area.put("46", "海南");
        area.put("50", "重庆");
        area.put("51", "四川");
        area.put("52", "贵州");
        area.put("53", "云南");
        area.put("54", "西藏");
        area.put("61", "陕西");
        area.put("62", "甘肃");
        area.put("63", "青海");
        area.put("64", "宁夏");
        area.put("65", "新疆");
        area.put("71", "台湾");
        area.put("81", "香港");
        area.put("82", "澳门");
        area.put("91", "国外");
        //地区校验
        if (!area.containsKey(idCard.substring(0, 2))) {
            return 4;
        }
        //身份号码位数及格式检验
        Integer year = 0;
        String ereg = "";
        Pattern pattern = null;
        Integer res = 0;
        switch (idCard.length()) {
            case 15:
                year = Integer.parseInt(idCard.substring(6, 8)) + 1900;
                if (year % 4 == 0 || (year % 100 == 0 && year % 4 == 0)) {
                    ereg = "^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$";
                } else {
                    ereg = "^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}";
                }
                pattern = Pattern.compile(ereg);
                if (!pattern.matcher(idCard).matches()) {
                    res = 2;
                }
                break;
            case 18:
                //18位身份号码检测
                //出生日期的合法性检查
                //闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
                //平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
                year = Integer.parseInt(idCard.substring(6, 10));
                if (year % 4 == 0 || (year % 100 == 0 && year % 4 == 0)) {
                    ereg = "^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$";//闰年出生日期的合法性正则表达式
                } else {
                    ereg = "^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$";//平年出生日期的合法性正则表达式
                }
                pattern = Pattern.compile(ereg);
                if (!pattern.matcher(idCard).matches()) {
                    res = 2;
                } else {
                    //计算校验位
                    Integer s = (Integer.parseInt(idCard.charAt(0) + "") + Integer.parseInt(idCard.charAt(10) + "")) * 7 + (Integer.parseInt(idCard.charAt(1) + "") + Integer.parseInt(idCard.charAt(11) + "")) * 9 + (Integer.parseInt(idCard.charAt(2) + "") + Integer.parseInt(idCard.charAt(12) + "")) * 10 + (Integer.parseInt(idCard.charAt(3) + "") + Integer.parseInt(idCard.charAt(13) + "")) * 5 + (Integer.parseInt(idCard.charAt(4) + "") + Integer.parseInt(idCard.charAt(14) + "")) * 8 + (Integer.parseInt(idCard.charAt(5) + "") + Integer.parseInt(idCard.charAt(15) + "")) * 4 + (Integer.parseInt(idCard.charAt(6) + "") + Integer.parseInt(idCard.charAt(16) + "")) * 2 + Integer.parseInt(idCard.charAt(7) + "") + Integer.parseInt(idCard.charAt(8) + "") * 6 + Integer.parseInt(idCard.charAt(9) + "") * 3;
                    Integer y = s % 11;
                    String m = "F";
                    String jym = "10X98765432";
                    m = jym.substring(y, y + 1);
                    if (!m.equals(idCard.charAt(17) + "")) {
                        res = 3;
                    }
                }
                break;
            default:
                res = 1;
                break;
        }
        return res;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url     string 发送请求的URL
     * @param timeout int 超时时间，单位秒
     * @param args    map 请求参数
     * @return string 所代表远程资源的响应结果
     */
    public static String doGet(String url, Map<String, String> args, int timeout) {
        String param;
        if (args == null) {
            param = "";
        } else {
            Map<String, String> tmp = new HashMap<>();
            for (String key : args.keySet()) {
                if (!Helper.empty(args.get(key))) {
                    tmp.put(key, args.get(key));
                }
            }
            param = httpBuildQuery(tmp);
        }
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString;
            if ("".equals(param)) {
                urlNameString = url;
            } else {
                urlNameString = url + "?" + param;
            }
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "close");
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;");
            httpURLConnection.setReadTimeout(timeout * 1000);
            httpURLConnection.setConnectTimeout(3000);
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }
            in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            return "";
            //e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                return "";
                //e2.printStackTrace();
            }
        }
        return result.toString();
    }

    public static String doPost(String url, Map<String, String> args, int timeout) {
        return doPost(url, args, timeout, false);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url      string 发送请求的 URL
     * @param timeout  int 超时时间，单位秒
     * @param args     map 请求参数
     * @param jsonPost boolean 是否是post传json格式数据
     * @return String 所代表远程资源的响应结果
     */
    public static String doPost(String url, Map<String, String> args, int timeout, boolean jsonPost) {
        String param;
        if (args == null) {
            param = "";
        } else {
            param = jsonPost ? JsonUtil.toJson(args) : httpBuildQuery(args);
        }
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        OutputStreamWriter out = null;
        try {
//            SecurityUtil.startSSRFNetHookChecking(url);
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "close");
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", jsonPost ? "application/json" : "application/x-www-form-urlencoded");
            httpURLConnection.setReadTimeout(timeout * 1000);
            httpURLConnection.setConnectTimeout(3000);
            out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write(param);
            out.flush();
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }
            in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            System.out.println("doPost Exception" + url + "," + e);
            return "";
        } finally {
//            SecurityUtil.stopSSRFNetHookChecking();
            try {
                if (null != out) {
                    out.close();
                }
                if (null != in) {
                    in.close();
                }
            } catch (IOException ex) {
                //ex.printStackTrace();
                return "";
            }
        }
        return result.toString();
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url      string 发送请求的 URL
     * @param timeout  int 超时时间，单位秒
     * @param args     map 请求参数
     * @param jsonPost boolean 是否是post传json格式数据
     * @return String 所代表远程资源的响应结果
     */
    public static String doPostUtf8(String url, Map<String, String> args, int timeout, boolean jsonPost) {
        String param;
        if (args == null) {
            param = "";
        } else {
            param = jsonPost ? JsonUtil.toJson(args) : httpBuildQuery(args);
        }
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        OutputStreamWriter out = null;
        try {
//            SecurityUtil.startSSRFNetHookChecking(url);
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "close");
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", jsonPost ? "application/json" : "application/x-www-form-urlencoded;charset=UTF-8");
            httpURLConnection.setReadTimeout(timeout * 1000);
            httpURLConnection.setConnectTimeout(3000);
            out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write(param);
            out.flush();
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }
            in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            // System.out.println("发送 POST 请求出现异常！"+e);
            // e.printStackTrace();
            return "";
        } finally {
//            SecurityUtil.stopSSRFNetHookChecking();
            try {
                if (null != out) {
                    out.close();
                }
                if (null != in) {
                    in.close();
                }
            } catch (IOException ex) {
                // ex.printStackTrace();
                // System.out.println("异常ex = " + ex);
                return "";
            }
        }
        return result.toString();
    }

    /**
     * httpBuildQuery map转query字符串
     *
     * @param params map
     * @return string
     */
    public static String httpBuildQuery(Map<String, String> params) {
        if (null != params && params.size() > 0) {
            StringBuilder result = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append("&");
                result.append(entry.getKey());
                result.append("=");
                result.append(entry.getValue());
            }
            return result.toString().substring(1);
        }
        return "";
    }

    /**
     * 验证手机号格式
     *
     * @param mobile string 手机号
     * @return boolean 手机号格式是否正确
     */
    public static boolean isMobile(String mobile) {
        return !empty(mobile) && MOBILE_VALID_PATTERN.matcher(mobile).matches();
    }

    /**
     * 验证密码格式
     *
     * @param passwd string 密码
     * @return boolean 密码格式是否正确
     */
    public static boolean passwdValid(String passwd) {
        if (empty(passwd)) {
            return false;
        } else if (passwd.length() > 20 || passwd.length() < 6) {//长度限制
            return false;
        } else if (PASSWD_VALID_ALL_NUMBER_PATTERN.matcher(passwd).matches()) {//不能为纯数字
            return false;
        } else if (PASSWD_VALID_ALL_CHAR_PATTERN.matcher(passwd).matches()) {//不能为纯字符
            return false;
        } else if (PASSWD_VALID_HAS_SPACE_PATTERN.matcher(passwd).matches()) {//不能包含空格
            return false;
        } else if (PASSWD_VALID_ALL_PUNCTUATION_PATTERN.matcher(passwd).matches()) {//不能全为标点
            return false;
        }
        return PASSWD_VALID_PATTERN.matcher(passwd).matches();
    }

    /**
     * 验证用户登录名格式
     *
     * @param username string 用户登录名
     * @return boolean 用户登录名格式是否正确
     */
    public static boolean usernameValid(String username) {
        if (empty(username)) {
            return false;
        } else if (username.length() > 50 || username.length() < 1) {//长度限制
            return false;
        } else if (USERNAME_VALID_START_SLIDE_LINE_PATTERN.matcher(username).matches()) {//不能以下划线开始
            return false;
        } else if (USERNAME_VALID_END_SLIDE_LINE_PATTERN.matcher(username).matches()) {//不能以下划线结束
            return false;
        } else if (USERNAME_VALID_HAS_SPACE_PATTERN.matcher(username).matches()) {//不能包含空格
            return false;
        } else if (USERNAME_VALID_DOUBLE_PUNCTUATION_PATTERN.matcher(username).matches()) {//不能包含连续..或__或--
            return false;
        }
        return USERNAME_VALID_PATTERN.matcher(username).matches();
    }

    /**
     * 验证邮箱格式
     *
     * @param email string 邮箱
     * @return boolean 邮箱格式是否正确
     */
    public static boolean emailValid(String email) {
        return EMAIL_VALID_PATTERN.matcher(email).matches();
    }

    /**
     * 手机号脱敏
     *
     * @param mobile string 手机号
     * @return 脱敏结果
     */
    public static String safeMobile(String mobile) {
        if (!isMobile(mobile)) {
            return "";
        }
        return mobile.substring(0, 3) + "******" + mobile.substring(9);
    }

    /**
     * 支付宝账号脱敏
     *
     * @param alipayAccount string 支付宝账号
     * @return 脱敏结果
     */
    public static String safeAlipayAccount(String alipayAccount) {
        if (emailValid(alipayAccount)) {
            return safeEmail(alipayAccount, false);
        } else {
            return safeMobile(alipayAccount);
        }
    }

    /**
     * 淘宝账号脱敏
     *
     * @param taobaoAccount string 淘宝账号
     * @return 脱敏结果
     */
    public static String safeTaobaoAccount(String taobaoAccount) {
        if (empty(taobaoAccount)) {
            return "";
        }
        int length = StringUtils.length(taobaoAccount);
        int index = 1;
        String str = "*";
        if (length >= 5) {
            index = 2;
            str = "**";
        }
        return StringUtils.left(taobaoAccount, index).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(taobaoAccount, index), StringUtils.length(taobaoAccount), "*"), str));
    }

    /**
     * 证件号脱敏
     *
     * @param idCard string 证件号
     * @return boolean 脱敏结果
     */
    public static String safeIdCard(String idCard) {
        if (empty(idCard)) {
            return "";
        }
        return idCard.substring(0, 1) + strPad("*", idCard.length() - 2) + idCard.substring(idCard.length() - 1);
    }

    /**
     * 昵称脱敏
     *
     * @param nick 昵称
     * @return boolean 脱敏结果
     */
    public static String safeNick(String nick) {
        if (empty(nick)) {
            return "";
        }
        return nick.substring(0, 1) + "**" + nick.substring(nick.length() - 1);
    }

    /**
     * 字符串填充
     *
     * @param padString string 填充字符串
     * @param padLength int 填充次数
     * @return string 填充结果
     */
    public static String strPad(String padString, int padLength) {
        if (empty(padString) || padLength <= 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < padLength; i++) {
            result.append(padString);
        }
        return result.toString();
    }

    /**
     * 邮箱脱敏
     *
     * @param email string 邮箱
     * @param sms   boolean 条件
     * @return string 脱敏结果
     */
    public static String safeEmail(String email, boolean sms) {
        if (!emailValid(email)) {
            return "";
        }
        String[] arr = email.split("@");
        if (sms) {
            String[] host = arr[1].split("\\.");
            return (arr[0].length() < 3 ? arr[0] + "***@" : arr[0].substring(0, 3) + "***@") + (host[0].length() <= 7 ? host[0] + ".*" : host[0].substring(0, 7) + "*");
        } else {
            return (arr[0].length() < 3 ? arr[0] + "***@" : arr[0].substring(0, 3) + "***@") + arr[1];
        }
    }

    /**
     * 获取跳转之后的链接地址
     *
     * @param srcUrl string 跳转前的url
     * @return string 跳转后的url
     */
    public static String getDirectUrl(String srcUrl) {
        try {
            URL url = new URL(srcUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.getResponseCode();
            return conn.getURL().toString();
        } catch (Exception e) {
            //e.printStackTrace();
            return "";
        }
    }

    /**
     * 解析替换模版内容
     *
     * @param content string 模版内容
     * @param input   map 替换内容
     * @return string 结果
     */
    public static String parseTemplate(String content, Map<String, String> input) {
        if (empty(content) || null == input || input.size() < 1) {
            return "";
        }
        for (String key : input.keySet()) {
            content = content.replace("#" + key + "#", input.get(key));
        }
        return content;
    }

    /**
     * 解析belongto
     *
     * @param belongto string 格式如： aaa:1,bbb:2,ccc:3
     * @return map 结果
     */
    public static Map<String, String> getBelongtoMeaning(String belongto) {
        Map<String, String> map = new HashMap<String, String>(16);
        if (null == belongto || "".equals(belongto)) {
            return map;
        }
        String[] kvArray = belongto.split(",");
        for (String val : kvArray) {
            String[] tmpArray = val.split(":");
            map.put(tmpArray[0], tmpArray[1]);
        }
        return map;
    }

    /**
     * 设置code倒数第digit个字符为value
     *
     * @param code  string
     * @param digit int
     * @param value char
     * @return string code
     */
    public static String setSubcode(String code, int digit, char value) {
        if (empty(code) || digit < 1) {
            return code;
        }
        int length = code.length();
        if (length < digit) {
            code = strPad("0", digit - length) + code;
            length = digit;
        }
        char[] arr = code.toCharArray();
        arr[length - digit] = value;
        return String.copyValueOf(arr);
    }

    /**
     * 获取code倒数第digit个字符
     *
     * @param code  string
     * @param digit int
     * @return char
     */
    public static char getSubcode(String code, int digit) {
        if (empty(code) || digit < 1 || digit > code.length()) {
            return '0';
        }
        return code.charAt(code.length() - digit);
    }

    /**
     * 去除url中指定参数
     *
     * @param url             string url
     * @param reservedKeyword string[] 保留关键字数组
     * @return string
     */
    public static String replaceUrlParams(String url, String[] reservedKeyword) {
        if (empty(url) || null == reservedKeyword) {
            return "";
        }
        String query;
        try {
            query = new URL(url).getQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        if (Helper.empty(query)) {
            return url;
        }
        List<String> list = Arrays.asList(reservedKeyword);
        String[] queryParts = query.split("&");
        StringBuilder replaceQuery = new StringBuilder();
        for (String val : queryParts) {
            String[] tmp = val.split("=");
            if (!list.contains(tmp[0])) {
                replaceQuery.append("&" + val);
            }
        }
        return url.replace(query, replaceQuery.toString().substring(1));
    }

    /**
     * 拼接url
     *
     * @param url string
     * @param map map
     * @return string
     */
    public static String combineURL(String url, Map<String, Object> map) {
        if (map == null) {
            return url;
        }
        StringBuilder query = new StringBuilder();
        for (String key : map.keySet()) {
            query.append("&" + key + "=" + map.get(key).toString());
        }
        String slug = url.contains("?") ? "&" : "?";
        return url + slug + query.substring(1);
    }

    /**
     * 判断卡号中是否包含中文
     *
     * @param cardNum string 卡号
     * @return boolean
     */
    public static boolean cardNumValid(String cardNum) {
        return !CARD_NUM_VALID_PATTERN.matcher(cardNum).find();
    }

    /**
     * 码商千里码 外部接口加密规则
     *
     * @param map treemap 入参
     * @return string sign
     */
    public static String apiSign(TreeMap<String, String> map) {
        if (null == map || null == map.get("app_secret")) {
            return "";
        }
        String secret = map.get("app_secret");
        map.remove("app_secret");
        map.remove("sign");
        StringBuilder paramstr = new StringBuilder();
        for (String key : map.keySet()) {
            paramstr.append(key + map.get(key));
        }
        return DigestUtils.md5Hex(secret + paramstr).toUpperCase();
    }

    /**
     * 去除包括全角半角及网页空格的正则匹配
     *
     * @param string string
     * @return string
     */
    public static String removeSpace(String string) {
        return string.replaceAll("[\\s\\p{Zs}]+", "").replace("&nbsp;", "");
    }

    /**
     * appEncode
     *
     * @param data   string
     * @param key    string
     * @param secret string
     * @return string
     */
    public static String appEncode(String data, String key, String secret) {
        return PassportSign.appEncode(data, key, secret.substring(0, 16));
    }

    /**
     * appDecode
     *
     * @param data   string
     * @param key    string
     * @param secret string
     * @return string
     */
    public static String appDecode(String data, String key, String secret) {
        return PassportSign.appDecode(data, key, secret.substring(0, 16));
    }

    /**
     * 去除字符串左边特定的字符串
     *
     * @param str      string 被操作字符串
     * @param charList string 需要去除的特定字符串
     * @return string
     */
    public static String ltrim(String str, String charList) {
        if (empty(str) || empty(charList)) {
            return str;
        }
        int index = str.indexOf(charList);
        if (index == 0) {
            return ltrim(str.substring(index + charList.length()), charList);
        }
        return str;
    }

    /**
     * 去除字符串右边特定的字符串
     *
     * @param str      string 被操作字符串
     * @param charList string 需要去除的特定字符串
     * @return string
     */
    public static String rtrim(String str, String charList) {
        if (empty(str) || empty(charList)) {
            return str;
        }
        int index = str.lastIndexOf(charList);
        if (index != -1 && str.substring(index).equals(charList)) {
            return rtrim(str.substring(0, index), charList);
        }
        return str;
    }

    /**
     * 去除字符串两侧特定的字符串
     *
     * @param str      string 被操作字符串
     * @param charList string 需要去除的特定字符串
     * @return string
     */
    public static String trim(String str, String charList) {
        if (empty(str) || empty(charList)) {
            return str;
        }
        return ltrim(rtrim(str, charList), charList);
    }

    /**
     * 字符串 驼峰转下划线
     *
     * @param str string
     * @return string
     */
    public static String underline(String str) {
        Matcher matcher = AZ_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if (matcher.find()) {
            sb = new StringBuffer();
            //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            //正则之前的字符和被替换的字符
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
            //把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        } else {
            return sb.toString();
        }
        return underline(sb.toString());
    }

    /**
     * 字符串下划线转驼峰
     *
     * @param str string
     * @return string
     */
    public static String camel(String str) {
        //利用正则删除下划线，把下划线后一位改成大写
        Matcher matcher = UNDERLINE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if (matcher.find()) {
            sb = new StringBuffer();
            //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            //正则之前的字符和被替换的字符
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            //把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        } else {
            return sb.toString();
        }
        return camel(sb.toString());
    }

    /**
     * 根据user-agent 获取用户移动设备信息
     *
     * @param userAgent string
     * @return map
     */
    public static Map<String, String> getUserAgent(String userAgent) {
        if (empty(userAgent)) {
            HttpServletRequest request = RequestHelper.getRequest();
            userAgent = request.getHeader("user-agent");
            if (empty(userAgent)) {
                return new HashMap<>();
            }
        }
        Matcher matcher = DEVICE_INFO_PATTERN.matcher(userAgent);
        Map<String, String> res = new HashMap<>();
        if (matcher.find()) {
            String deviceInfo = matcher.group(0);
            if (deviceInfo.contains("iPhone")) {
                matcher = FILTER_IPHONE_OS_VERSION_PATTERN.matcher(deviceInfo);
                if (!matcher.find()) {
                    return res;
                }
                res.put("user_agent", "1");
                res.put("device_type", "iPhone");
                res.put("os_version", matcher.group(1).replace('_', '.'));
            } else if (deviceInfo.contains("Android")) {
                res.put("user_agent", "2");
                matcher = FILTER_ANDROID_PATTERN.matcher(deviceInfo);
                if (!matcher.find()) {
                    return new HashMap<>();
                }
                res.put("device_type", matcher.group(1));
                matcher = FILTER_ANDROID_OS_VERSION_PATTERN.matcher(deviceInfo);
                if (!matcher.find()) {
                    return new HashMap<>();
                }
                res.put("os_version", matcher.group(1));
            } else if (deviceInfo.contains("iPad")) {
                res.put("user_agent", "1");
                res.put("device_type", "iPad");
                matcher = FILTER_IPAD_OS_VERSION_PATTERN.matcher(deviceInfo);
                if (!matcher.find()) {
                    return new HashMap<>();
                }
                res.put("os_version", matcher.group(1));
            }
        }
        return res;
    }

    /**
     * URL编码
     *
     * @param url string
     * @return string
     */
    public static String urlencode(String url) {
        if (empty(url)) {
            return url;
        }
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (Exception e) {
            return url;
        }
    }

    /**
     * URL解码
     *
     * @param url string
     * @return string
     */
    public static String urldecode(String url) {
        if (empty(url)) {
            return url;
        }
        try {
            return URLDecoder.decode(url, "utf-8");
        } catch (Exception e) {
            return url;
        }
    }

    /**
     * URL重定向跳转
     *
     * @param url string
     * @return string
     */
    public static String urlRedirect(HttpServletResponse response, String url) {
        try {
            //if (CheckSafeUrl.getDefaultInstance().inWhiteList(url)) {//检查域名是否在集团白名单，防止自己随便跳转
            response.sendRedirect(url);
            //}
            return "";
        } catch (Exception r) {
            return "";
        }
    }

    /**
     * URL参数转Map
     *
     * @param param string
     * @return string
     */
    public static Map urlParamToMap(String param) {
        Map<String, String> map = new HashMap();
        if (empty(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * 判断一个类中的所有字段是不是都为null或则0
     *
     * @param obj
     * @return
     */
    public static boolean isObjectAllFieldsNull(Object obj) {
        if (obj == null) {
            return true;
        }
        try {
            for (Field f : obj.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(obj) != null) {
                    return false;
                }
            }
        } catch (IllegalAccessException e) {
            return true;
        }
        return true;
    }

    public static Map objectToMap(Object obj) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        if (obj == null) {
            return null;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                try {
                    Field f = obj.getClass().getDeclaredField(fields[i].getName());
                    f.setAccessible(true);
                    Object o = f.get(obj);
                    reMap.put(fields[i].getName(), o);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return reMap;
    }

    public static Map<String, String> objectToStringMap(Object obj) {
        Map<String, String> reMap = new HashMap<>();
        if (obj == null) {
            return null;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                try {
                    Field f = obj.getClass().getDeclaredField(fields[i].getName());
                    f.setAccessible(true);
                    String o = String.valueOf(f.get(obj));
                    reMap.put(fields[i].getName(), o);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return reMap;
    }

    public static String getDefaultNick(Long alisportsid, String aesKey) {
        return "ALI_" + UnixCrypt.crypt(String.valueOf(alisportsid), aesKey).toUpperCase().replace('.', '6').replace('/', '9');
    }


    //获取一个整形数组的所有子集。
    public static ArrayList<ArrayList<String>> getAllSubset(ArrayList<String> L) {
        if (L.size() > 0) {
            ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
            for (int i = 0; i < Math.pow(2, L.size()); i++) {// 集合子集个数=2的该集合长度的乘方
                ArrayList<String> subSet = new ArrayList<String>();
                int index = i;// 索引从0一直到2的集合长度的乘方-1
                for (int j = 0; j < L.size(); j++) {
                    // 通过逐一位移，判断索引那一位是1，如果是，再添加此项
                    if ((index & 1) == 1) {// 位与运算，判断最后一位是否为1
                        subSet.add(L.get(j));
                    }
                    index >>= 1;// 索引右移一位
                }
                result.add(subSet); // 把子集存储起来
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * 求两个字符串数组的差集
     *
     * @param m
     * @param n
     * @return
     */
    public static String[] diffArray(String[] m, String[] n) {
        // 将较长的数组转换为set
        Set<String> set = new HashSet<String>(Arrays.asList(m.length > n.length ? m : n));

        // 遍历较短的数组，实现最少循环
        for (String i : m.length > n.length ? n : m) {
            // 如果集合里有相同的就删掉，如果没有就将值添加到集合
            if (set.contains(i)) {
                set.remove(i);
            } else {
                set.add(i);
            }
        }

        String[] arr = {};
        return set.toArray(arr);
    }

    /**
     * 用来处理aes加密后数据字符串 以便php也能正常解密处理
     *
     * @param dataLength int 加密前数据字符串的长度 16的整数倍 如果加密前数据字符串长度不够16整数倍 则需要补齐
     * @param data       string 加密后的数据字符串
     * @return string 处理结果
     */
    public static String handleEncodeData(int dataLength, String data) {
        if (dataLength <= 0 || empty(data)) {
            return "";
        }
        int x = dataLength / 16;
        int y;
        int z = 0;
        switch (x % 3) {
            case 1:
                y = x / 3 + 1;
                z = 24 + 64 * (y - 1) - 2;
                break;
            case 2:
                y = x / 3 + 1;
                z = 44 + 64 * (y - 1) - 1;
                break;
            case 0:
                y = x / 3;
                z = 64 * y;
        }
        return data.substring(0, z);
    }

    /**
     * 获取远程访问用户的Ip地址
     *
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return getIp(request);
    }

    public static String getToday() {
        String time;
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        time = new SimpleDateFormat("yyyyMMdd").format(date);
        return time;
    }

    public static String getYesterday() {
        String pattern = "yyyy-MM-dd";
        return Helper.getYesterday(pattern);
    }

    public static String getYesterday(String pattern) {
        String time;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();
        time = new SimpleDateFormat(pattern).format(date);
        return time;
    }

    public static String getYesterdayBeforeDay(String pattern) {
        String time;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        Date date = cal.getTime();
        time = new SimpleDateFormat(pattern).format(date);
        return time;
    }

    public static String getYesterdayMonth() {
        String time;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();
        time = new SimpleDateFormat("yyyy-MM").format(date);
        return time;
    }

    public static String getFirstDayOfMonth(String month) {
        String[] a = month.split("-");
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(a[0]));
        calendar.set(Calendar.MONTH, Integer.valueOf(a[1]));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dft.format(calendar.getTime());
    }

    public static String getLastDayOfMonth(String month) {
        String[] a = month.split("-");
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(a[0]));
        calendar.set(Calendar.MONTH, Integer.valueOf(a[1]));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return dft.format(calendar.getTime());
    }

    /**
     * Map按KEY排序
     *
     * @return
     */
    public static <K extends Comparable<? super K>, V> Map<K, V> sortMapByKey(Map<K, V> map, String type) {
        Map<K, V> result = new LinkedHashMap<>();
        if ("desc".equals(type)) {
            map.entrySet().stream()
                    .sorted(Map.Entry.<K, V>comparingByKey()
                            .reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));//升降序由reversed控制
        } else {
            map.entrySet().stream()
                    .sorted(Map.Entry.<K, V>comparingByKey()
                    ).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }

    /**
     * Map按VALUE排序
     *
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(Map<K, V> map, String type) {
        Map<K, V> result = new LinkedHashMap<>();
        if ("desc".equals(type)) {
            map.entrySet().stream()
                    .sorted(Map.Entry.<K, V>comparingByValue()
                            .reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        } else {
            map.entrySet().stream()
                    .sorted(Map.Entry.<K, V>comparingByValue()
                    ).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }


    public static void goLog(String index, Boolean res, String content) {
        System.out.println(index + " save " + (res ? "successed" : "failed") + "!!!");
        Map<String, String> result = new HashMap<>();
        result.put("index", index);
        result.put("data", content);
//        Log.log("admin", "datacenter script", JsonUtil.toJson(result), res ? "SUCCESS" : "FAILED", "INFO");
    }

    /**
     * 姓名脱敏(两个字的隐藏第一个字，三个字以上的只显示最后两个字)
     *
     * @param name
     * @return
     */
    public static String safeName(String name) {
        if (empty(name)) {
            return "";
        }
        int length = StringUtils.length(name);
        return length < 3 ? StringUtils.leftPad(name.substring(length - 1), length, '*') : StringUtils.leftPad(name.substring(length - 2), length, '*');
    }

    /**
     * 获取今年年份
     *
     * @return
     */
    public static int getCurrentYear() {
        Calendar a = Calendar.getInstance();
        return a.get(Calendar.YEAR);
    }

    public static String getTradeRequest(String type, Map<String, String> params) {
        String method = "";
        if (type.equals("operate")) {
            method = "alibaba.alisports.trade.statistic.datacenter.operate";
        } else if (type.equals("spread")) {
            method = "alibaba.alisports.trade.statistic.datacenter.spread";
        } else {
            return null;
        }
        params.put("alisp_api", method);
//        params.put("alisp_sign", ParameterSignUtil.sign(params, AppIni.DATACENTER_SECRET));
        return doGet("http://gw-alisports.taobao.com/rest", params, 10);
    }

    public static String removeSpecialChar(String str) {
        return empty(str) ? "" : REMOVE_SPECIAL_CHAR.matcher(str).replaceAll("");
    }

    //压测流量判断方法
    public static boolean isCurrentThreadInPressTest() {
        boolean t = false;
//        try {
//            String t1 = EagleEye.getUserData("t");
//            if (t1 != null) {
//                t = t1.equals("1");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return t;
    }

    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(String amount) {
        if (!amount.matches(CURRENCY_FEN_REGEX)) {
            return null;
        }
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
    }

    /**
     * 计算两个日期的差值
     *
     * @param oldDate
     * @param nowDate
     * @return
     */
    public static int comparePastDate(String oldDate, String nowDate, SimpleDateFormat sdf) {
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        Calendar calendar = Calendar.getInstance();
        Date old;
        int day = 0;
        try {
            old = sdf.parse(oldDate);
            calendar.setTime(old);
            Long oTime = calendar.getTimeInMillis();

            Date now = sdf.parse(nowDate);
            calendar.setTime(now);
            Long nTime = calendar.getTimeInMillis();

            day = (int) ((nTime - oTime) / (3600F * 1000 * 24));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return day;
    }

    /**
     * 获取当前应该统计的日期：yyyyMMdd
     */
    public static String getStatDay() {
        Date nowDate = new Date();
        SimpleDateFormat sp = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        int hh = calendar.get(Calendar.HOUR_OF_DAY);
        if (hh < 5) {
            calendar.add(Calendar.DATE, -2);
            return sp.format(calendar.getTime());
        } else {
            calendar.add(Calendar.DATE, -1);
            return sp.format(calendar.getTime());
        }
    }

    /**
     * 根据系统订单号生成支付订单号
     *
     * @param orderId
     * @return
     */
    public static String generateOutOrderId(String orderId) {
        return orderId + String.valueOf(time()) + randomString(4, 1);
    }

    /**
     * 从外部系统的订单号获取系统的订单号
     *
     * @param outOrderId
     * @return
     */
    public static String getOrderIdFromOut(String outOrderId) {
        return outOrderId.length() > 15 ? outOrderId.substring(0, outOrderId.length() - 14) : outOrderId;
    }

    /**
     * 下划线转get+驼峰格式。
     *
     * @param str
     * @return
     */
    public static String lineToGetHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = LINE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return "get" + sb.toString().substring(0, 1).toUpperCase() + sb.toString().substring(1);
    }

    /**
     * 保留n位小数
     *
     * @param num
     * @param n
     * @return
     */
    public static double getDoubleRoundN(double num, int n) {
        NumberFormat nf = NumberFormat.getInstance();
        try {
            DecimalFormat df = (DecimalFormat) nf;
            df.applyPattern("####.000");
            df.setRoundingMode(RoundingMode.DOWN);
            df.setMaximumFractionDigits(n);
            String resString = df.format(num);
            return Double.valueOf(resString);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getBeforeDay(String day, Integer beforeDay, String pattern) {
        SimpleDateFormat dft = new SimpleDateFormat(pattern);
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dft.parse(day));
            cal.add(Calendar.DATE, -beforeDay);
            Date newDate = cal.getTime();
            String time = new SimpleDateFormat(pattern).format(newDate);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Integer diffDay(String day1, String day2, String pattern) {
        SimpleDateFormat dft = new SimpleDateFormat(pattern);
        try {
            Date date1 = dft.parse(day1);
            Date date2 = dft.parse(day2);
            return date1.compareTo(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * HH:mm:ss 转second
     *
     * @param time
     * @return
     */
    public static int timeToSecond(String time) {
        int duration = 0;
        String[] units = time.split(":");
        if (units.length == 3) {
            int hour = Integer.parseInt(units[0]);
            int minutes = Integer.parseInt(units[1]);
            int seconds = Integer.parseInt(units[2]);
            duration = 3600 * hour + 60 * minutes + seconds;
        }
        return duration;
    }


    /**
     * second 转 HH”mm”ss
     *
     * @param duration
     * @return
     */
    public static String secondToTime(Integer duration, String regex, boolean needHour) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (duration <= 0) {
            return needHour ? "00" + regex + "00" + regex + "00" : "00" + regex + "00";
        }
        minute = duration / 60;
        if (minute < 60) {
            second = duration % 60;
            return needHour ? "00" + regex + unitFormat(minute) + regex + unitFormat(second) : unitFormat(minute) + regex + unitFormat(second);
        }
        hour = minute / 60;
        if (hour > 99) {
            return "99" + regex + "59" + regex + "59";
        }
        minute = minute % 60;
        second = duration - hour * 3600 - minute * 60;
        return unitFormat(hour) + regex + unitFormat(minute) + regex + unitFormat(second);
    }


    /**
     * 将传入的值进行转换
     *
     * @param i
     * @return
     */
    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }


}
