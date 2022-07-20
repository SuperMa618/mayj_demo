package com.mayj.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 短信发送
 *
 * @Author mayunjie
 * @Date 2022/7/20 19:18
 **/
@RestController
public class SmsController {


//    @Autowired
//    private AliyunSendSmsService aliyunSendSmsService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${aliyun.sms.templateCode}")
    private String templateCode;

    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.sms.accessKeySecret}")
    private String accessKeySecret;
    /**
     * 短信发送
     *
     * @param phone
     * @return
     */
    @GetMapping("/send/{phone}")
    public String sendCode(@PathVariable("phone") String phone) {

        // 根据手机号从redis中拿验证码
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return phone + " : " + code + "已经存在，还没有过期！";
        }

        // 如果redis 中根据手机号拿不到验证码，则生成4位随机验证码
        code = UUID.randomUUID().toString().substring(0, 4);

        // 验证码存入codeMap
        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put("code", code);

        // 调用aliyunSendSmsService发送短信
        Boolean bool = this.sendMessage(phone, templateCode, codeMap);

        if (bool) {
            // 如果发送成功，则将生成的4位随机验证码存入redis缓存,5分钟后过期
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return phone + " ： " + code + "发送成功！";
        } else {
            return phone + " ： " + code + "发送失败！";
        }
    }

    /**
     * 发送短信验证码
     *
     * @param phone        接收短信的手机号
     * @param templateCode 短信模板CODE
     * @param codeMap      验证码map 集合
     * @return
     */
    public Boolean sendMessage(String phone, String templateCode, Map<String, Object> codeMap) {

        /**
         * 连接阿里云：
         *
         * 三个参数：
         * regionId 不要动，默认使用官方的
         * accessKeyId 自己的用户accessKeyId
         * accessSecret 自己的用户accessSecret
         */
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        // 构建请求：
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");

        // 自定义参数：
        request.putQueryParameter("PhoneNumbers", phone);// 手机号
        request.putQueryParameter("SignName", "CSP网上商城");// 短信签名
        request.putQueryParameter("TemplateCode", templateCode);// 短信模版CODE

        // 构建短信验证码
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(codeMap));

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
