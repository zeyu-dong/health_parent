package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author zeyu
 * @date 2022/05/15
 **/


public interface SetmealService {

    public void add(Setmeal setmeal, Integer[] ids);

    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);


    public List<Setmeal> findAll();

    public Setmeal findById(int id);

    public List<Map<String, Object>> findSetmealCount();



}
