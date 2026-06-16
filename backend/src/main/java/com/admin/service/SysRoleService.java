package com.admin.service;

import com.admin.dto.PageResult;
import com.admin.entity.SysRole;

import java.util.List;

public interface SysRoleService {
    PageResult<SysRole> page(Integer page, Integer pageSize, String keyword);
    List<SysRole> list();
    SysRole getById(Long id);
    void create(SysRole role);
    void update(SysRole role);
    void delete(Long id);
    void assignMenus(Long roleId, List<Long> menuIds);
    List<Long> getRoleMenuIds(Long roleId);
}
