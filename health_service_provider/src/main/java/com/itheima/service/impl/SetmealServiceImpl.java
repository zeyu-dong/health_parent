package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zeyu
 * @date 2022/05/15
 **/

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;


    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_put_path}")
    private String outPutPath;//从属性文件中读取要生成的配置文件

    //新增套餐信息，同时需要关联检查组
    public void add(Setmeal setmeal, Integer[] ids) {
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        this.setMealAndCheckGroup(setmealId, ids);

        //将图片名称保存到redis集合
        String img = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, img);

        //当生成套餐后，需要重新生成静态页面（套餐列表，套餐详情）

         generateMobileStaticMobile();

    }


    //生成当前方法所需静态页面
    public void generateMobileStaticMobile(){
        //查询数据
        List<Setmeal> setmealList = setmealDao.findAll();

        //需要生成套餐列表页面
        generateMobileSetmealListHtml(setmealList);

        //需要生成套餐详情静态页面
        generateMobileSetmealDetailListHtml(setmealList);
    }

    //生成套餐列表页面
    public void generateMobileSetmealListHtml(List<Setmeal> list){
        Map map = new HashMap();
        map.put("setmealList", list);


        generateHtml("mobile_seatmeal.ftl", "m_setmeal.html", map);

    }

    //生成套餐详情页面信息(可能有多个)
    public void generateMobileSetmealDetailListHtml(List<Setmeal> list) {
        for (Setmeal setmeal : list) {
            Map map = new HashMap();
            map.put("setmeal", setmealDao.findById(setmeal.getId()));
            generateHtml(
                    "mobile_setmeal_detail.ftl",
                    "setmeal_detail_"+setmeal.getId()+".html",
                    map);
        }
    }




    public void generateHtml(String templateName, String htmlPage, Map map) {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        try {
            Template template = configuration.getTemplate(templateName);
            Writer out = new FileWriter(new File(outPutPath + "/" + htmlPage));
            //输出文件
            template.process(map, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    public List<Setmeal> findAll() {
        List<Setmeal> all = setmealDao.findAll();
        return all;
    }

    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());

    }



    //设置套餐检查组多对多关系
    public void setMealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupId : checkgroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("setmealId", setmealId);
                map.put("checkgroupId", checkgroupId);
                setmealDao.setMealAndCheckGroup(map);
            }
        }
    }
}

