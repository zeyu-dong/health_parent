package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.List;
import java.util.Map;

/**
 * @author zeyu
 * @date 2022/05/13
 **/

public interface CheckGroupDao {
    public void add(CheckGroup checkGroup);

    public void setCheckGroupAndCheckItem(Map map);

    public Page<CheckGroup> findByCondition(String queryString);


    public CheckGroup findById(Integer id);

    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    public void edit(CheckGroup checkGroup);

    public void deleteAssociation(Integer id);

    List<CheckGroup> findAll();

    

}
