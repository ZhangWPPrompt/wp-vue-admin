package com.admin.service;

import com.admin.dto.LoginRequest;
import com.admin.dto.LoginResponse;
import com.admin.dto.PageResult;
import com.admin.entity.SysUser;

import java.util.List;

public interface SysUserService {
    LoginResponse login(LoginRequest request);
    SysUser getUserByUsername(String username);
    PageResult<SysUser> page(Integer page, Integer pageSize, String keyword);
    SysUser getById(Long id);
    void create(SysUser user);
    void update(SysUser user);
    void delete(Long id);
    void assignRoles(Long userId, List<Long> roleIds);
    List<Long> getUserRoleIds(Long userId);
}
