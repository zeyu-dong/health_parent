package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.itheima.constant.MessageConstant.*;

/**
 * @author zeyu
 * @date 2022/05/14
 **/

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    private static final Logger log = LoggerFactory.getLogger(CheckGroupController.class);
    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds) {
        try {
            checkGroupService.add(checkGroup, checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, ADD_CHECKGROUP_SUCCESS);
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        return checkGroupService.pageQuery(queryPageBean);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {

        try {

            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true, QUERY_CHECKGROUP_SUCCESS, checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, QUERY_CHECKGROUP_FAIL) ;
        }
    }

    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id) {
        try {
            List<Integer> list = checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true, QUERY_CHECKITEM_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, QUERY_CHECKITEM_FAIL) ;
        }

    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds) {
        try {
            checkGroupService.edit(checkGroup, checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true, EDIT_CHECKGROUP_SUCCESS);
    }


    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<CheckGroup>list = checkGroupService.findAll();
            return new Result(true,QUERY_CHECKITEM_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, QUERY_CHECKGROUP_FAIL);
        }
    }

}
