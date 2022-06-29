package com.itheima.controller;


import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.RedisMessageConstant;
import com.itheima.utils.TencentMessage;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode/")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    //体检预约发送验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {

        //生成4位验证码
        Integer integer = ValidateCodeUtils.generateValidateCode(4);

        String validateCode = String.valueOf(integer);
        //短信发送
        try {
            TencentMessage.sendMessage(telephone, validateCode);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

        //将验证码保存都redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 60 * 5, validateCode);
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
    @RequestMapping("/send4Login")//用户手机快速登录，建议用6位比较保险
    public Result send4Login(String telephone) {

        //生成4位验证码
        Integer integer = ValidateCodeUtils.generateValidateCode(4);

        String validateCode = String.valueOf(integer);
        //短信发送
        try {
            TencentMessage.sendMessage(telephone, validateCode);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

        //将验证码保存都redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 60 * 5, validateCode);
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }


}
