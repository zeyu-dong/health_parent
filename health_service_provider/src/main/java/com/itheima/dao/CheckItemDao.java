package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zeyu
 * @date 2022/05/13
 **/

public interface CheckItemDao {
    //基于mybatis
    public void add(CheckItem checkItem);

    public Page<CheckItem> selectByCondition(String queryString);

    public Long findCountByCheckItemId(Integer id);

    public void deleteById(Integer id);
    public void edit(CheckItem checkItem);

    public  CheckItem findById(Integer id);

    public List<CheckItem> findAll();


}
