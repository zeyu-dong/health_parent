package com.itheima.service;


import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @author zeyu
 * @date 2022/05/13
 **/

//服务接口
public interface OrderSettingService {
    public void add(List<OrderSetting> data);

    public List<Map> getOrderSettingByMonth(String date);


    public void editNumberByDate(OrderSetting orderSetting);
}
