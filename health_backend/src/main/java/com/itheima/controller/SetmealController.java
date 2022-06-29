package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

import static com.itheima.constant.MessageConstant.*;

/**
 * @author zeyu
 * @date 2022/05/14
 **/

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    //使用jedispool

    @Autowired
    private JedisPool jedisPool;


    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){

        String originalFilename = imgFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String extention = originalFilename.substring(index - 1);

        //UUID产生一个随机的文件名称
        String fileName = UUID.randomUUID().toString() + extention;
        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);

            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);


        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, PIC_UPLOAD_SUCCESS, fileName);
    }


    @Reference
    private SetmealService setmealService;
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {

        try {
            setmealService.add(setmeal, checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, ADD_SETMEAL_FAIL);
        }

        return new Result(true, ADD_SETMEAL_SUCCESS);

    }
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = setmealService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

}
