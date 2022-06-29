package com.itheima.dao;


import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;

import java.util.Set;

public interface PermissionDao {
    public Set<Permission> findByRoleId(Integer id);
}
