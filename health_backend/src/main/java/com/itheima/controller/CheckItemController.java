package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.itheima.constant.MessageConstant.*;

/**
 * @author zeyu
 * @date 2022/05/13
 **/

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            System.out.println(checkItem);

            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, ADD_CHECKITEM_SUCCESS);
        }
        return new Result(true, ADD_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.pageQuery(queryPageBean);
        return pageResult;
    }



    //删除

    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            checkItemService.deleteById(id);
        } catch (Exception e) {
            return new Result(false, DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, DELETE_CHECKITEM_SUCCESS);
    }


    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem) {

        try {
            checkItemService.edit(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, EDIT_CHECKITEM_FAIL);
        }

        return new Result(true, EDIT_CHECKITEM_SUCCESS);
    }
    @RequestMapping("/findById")
    public Result findById(Integer id) {


        try {

            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true, QUERY_CHECKITEM_SUCCESS, checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, QUERY_CHECKITEM_FAIL);
        }

    }
    @RequestMapping("/findAll")
    public Result findAll() {

        try {
            List<CheckItem> list = checkItemService.findAll();

            return new Result(true, QUERY_CHECKITEM_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, QUERY_CHECKITEM_FAIL);
        }
    }




}
