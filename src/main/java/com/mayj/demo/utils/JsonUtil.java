package com.mayj.demo.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * @Author mayunjie
 * @Date 2022/7/21 15:55
 **/
public class JsonUtil {

    private static Log logger = LogFactory.getLog(JsonUtil.class);

    /**
     * 驼峰下划线互转
     * @param object 类的实例
     * @return JSON字符串
     */
    public static String toJsonSnakeCase(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            //throw new UtilException(e.getMessage());
            logger.warn("String to json faild : " + e.getMessage());
        }
        return null;
    }

    public static String toJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            //throw new UtilException(e.getMessage());
            logger.warn("String to json faild : " + e.getMessage());
        }
        return null;

    }

    /**
     * @param <T>   泛型声明
     * @param json  JSON字符串
     * @param clazz 要转换对象的类型
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (Helper.empty(json)) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        T t = null;
        try {
            t = mapper.readValue(json, clazz);
        } catch (IOException e) {
            //throw new UtilException(e.getMessage());
            logger.warn("json to object failed : " + e.getMessage());
        }
        return t;
    }

    /**
     * @param json JSON字符串
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, TypeReference<T> type) {
        if (Helper.empty(json)) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        T t = null;
        try {
            t = mapper.readValue(json, type);
        } catch (IOException e) {
            //throw new UtilException(e.getMessage());
            logger.warn("json to object failed : " + e.getMessage());
        }
        return t;
    }

    /**
     * @param <T>   泛型声明
     * @param json  JSON字符串
     * @param clazz 要转换对象的类型
     * @return
     */
    public static <T> T fromJsonSnakeCase(String json, Class<T> clazz) {
        if (Helper.empty(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        T t = null;
        try {
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            t = mapper.readValue(json, clazz);
        } catch (IOException e) {
            //throw new UtilException(e.getMessage());
            logger.warn("json to object failed : " + e.getMessage());
        }
        return t;
    }

    public static <T> T fromJsonSnakeCase(String json, TypeReference<T> type) {
        if (Helper.empty(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        T t = null;
        try {
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            t = mapper.readValue(json, type);
        } catch (IOException e) {
            //throw new UtilException(e.getMessage());
            logger.warn("json to object failed : " + e.getMessage());
        }
        return t;
    }

    /**
     * 判断字符串是否为json对象
     *
     * @param json
     * @return
     */
    public static Boolean isGoodJson(String json) {
        if (Helper.empty(json)) {
            return false;
        }
        try {
            JSONObject.parseObject(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断字符串是否为json数组
     *
     * @param json
     * @return
     */
    public static Boolean isGoodJsonArray(String json) {
        if (Helper.empty(json)) {
            return false;
        }
        try {
            JSONObject.parseArray(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
