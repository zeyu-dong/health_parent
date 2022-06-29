package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.RedisMessageConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")

public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/submit")
    //因为提交的是json，所以必须进行封装
    public Result submit(@RequestBody Map map) {
        //从redis中获取保存的验证码，进行比对
        String telephone = (String) map.get("telephone");
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCodeFromUser = (String) map.get("validateCode");

        if (
                validateCodeInRedis != null &&
                        validateCodeFromUser != null &&
                        validateCodeFromUser.equals(validateCodeFromUser)
        ) {
            //预约类型
            map.put("orderType", Order.ORDERTYPE_WEIXIN);//设置预约类型

            Result result = null;//dubbo远程调用服务
            try {
                result = orderService.order(map);
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            if (result.isFlag()) {
                //预约成功，可以发短信
                /*您成功预约*/

            }
            return result;
        } else {
            //不成功
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    @RequestMapping("findById")
    public Result findById(Integer id) {
        try {
            Map map =  orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }

    }
}
