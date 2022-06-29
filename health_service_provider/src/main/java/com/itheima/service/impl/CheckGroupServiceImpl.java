package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zeyu
 * @date 2022/05/14
 **/

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    //新增检查组，同时需要关联检查项
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //新增检查组，操作t_checkgroup数据
        checkGroupDao.add(checkGroup);

        //设置检查组和检查项 关联关系，多对多的关系
        //首先获取
        Integer checkGroupId = checkGroup.getId();
//        if (checkitemIds != null && checkitemIds.length >0) {
//            for (Integer id : checkitemIds) {
//                Map<String, Integer> map = new HashMap<>();
//                map.put("checkgroupId", checkGroupId);
//                map.put("checkitemId", id);
//                checkGroupDao.setCheckGroupAndCheckItem(map);
//
//            }
//        }
        this.setCheckGroupAndCheckItem(checkGroupId, checkitemIds);
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        String queryString = queryPageBean.getQueryString();
        Integer pageSize = queryPageBean.getPageSize();
        Integer currentPage = queryPageBean.getCurrentPage();
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());

    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //可能有去掉某些勾选，然后进行一些新的勾选，所以对中间关系表可以直接先全部删除然后再进行检查项的勾选
        //修改检查组基本信息
        checkGroupDao.edit(checkGroup);
        //清理关联关系
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //新建关联信息
        Integer checkGroupId = checkGroup.getId();
        this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length >0) {
            for (Integer id : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroupId", checkGroupId);
                map.put("checkitemId", id);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }


}
