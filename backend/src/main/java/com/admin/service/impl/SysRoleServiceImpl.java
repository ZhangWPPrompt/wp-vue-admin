package com.admin.service.impl;

import com.admin.dto.PageResult;
import com.admin.entity.SysRole;
import com.admin.entity.SysRoleMenu;
import com.admin.entity.SysUserRole;
import com.admin.mapper.SysRoleMapper;
import com.admin.mapper.SysRoleMenuMapper;
import com.admin.mapper.SysUserRoleMapper;
import com.admin.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysUserRoleMapper sysUserRoleMapper;

    public SysRoleServiceImpl(SysRoleMenuMapper sysRoleMenuMapper, SysUserRoleMapper sysUserRoleMapper) {
        this.sysRoleMenuMapper = sysRoleMenuMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
    }

    @Override
    public PageResult<SysRole> page(Integer page, Integer pageSize, String keyword) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(SysRole::getRoleName, keyword)
                   .or()
                   .like(SysRole::getRoleCode, keyword);
        }
        wrapper.orderByDesc(SysRole::getCreateTime);

        Page<SysRole> result = baseMapper.selectPage(new Page<>(page, pageSize), wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public List<SysRole> list() {
        return baseMapper.selectList(new LambdaQueryWrapper<SysRole>().orderByDesc(SysRole::getCreateTime));
    }

    @Override
    public SysRole getById(Long id) {
        return super.getById(id);
    }

    @Override
    @Transactional
    public void create(SysRole role) {
        save(role);
    }

    @Override
    @Transactional
    public void update(SysRole role) {
        updateById(role);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, id));
        removeById(id);
    }

    @Override
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        if (menuIds != null && !menuIds.isEmpty()) {
            menuIds.forEach(menuId -> {
                SysRoleMenu rm = new SysRoleMenu();
                rm.setRoleId(roleId);
                rm.setMenuId(menuId);
                sysRoleMenuMapper.insert(rm);
            });
        }
    }

    @Override
    public List<Long> getRoleMenuIds(Long roleId) {
        return sysRoleMenuMapper.selectList(
                new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId))
                .stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
    }
}
