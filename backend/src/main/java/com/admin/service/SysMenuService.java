package com.admin.service;

import com.admin.entity.SysMenu;

import java.util.List;

public interface SysMenuService {
    List<SysMenu> getUserMenus(Long userId);
    List<SysMenu> listTree();
    SysMenu getById(Long id);
    void create(SysMenu menu);
    void update(SysMenu menu);
    void delete(Long id);
    List<String> getUserPermissions(Long userId);
}
