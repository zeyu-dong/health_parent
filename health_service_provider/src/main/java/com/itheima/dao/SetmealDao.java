package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author zeyu
 * @date 2022/05/13
 **/

public interface SetmealDao {
    public void add(Setmeal setmeal);

    public void setMealAndCheckGroup(Map map);

    public Page<Setmeal> findByCondition(String queryString);

    public  List<Setmeal> findAll();

    public  Setmeal findById(int id);

    public List<Map<String,Object>> findSetmealCount();
}
