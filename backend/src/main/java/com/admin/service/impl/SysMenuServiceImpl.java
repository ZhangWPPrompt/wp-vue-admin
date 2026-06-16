package com.admin.service.impl;

import com.admin.entity.SysMenu;
import com.admin.entity.SysRoleMenu;
import com.admin.mapper.SysMenuMapper;
import com.admin.mapper.SysRoleMenuMapper;
import com.admin.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysRoleMenuMapper sysRoleMenuMapper;

    public SysMenuServiceImpl(SysRoleMenuMapper sysRoleMenuMapper) {
        this.sysRoleMenuMapper = sysRoleMenuMapper;
    }

    @Override
    public List<SysMenu> getUserMenus(Long userId) {
        List<SysMenu> menus = baseMapper.getMenusByUserId(userId);
        return buildTree(menus);
    }

    @Override
    public List<SysMenu> listTree() {
        List<SysMenu> menus = baseMapper.selectList(
                new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSortOrder));
        return buildTree(menus);
    }

    @Override
    public SysMenu getById(Long id) {
        return super.getById(id);
    }

    @Override
    @Transactional
    public void create(SysMenu menu) {
        save(menu);
    }

    @Override
    @Transactional
    public void update(SysMenu menu) {
        updateById(menu);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getMenuId, id));
        removeById(id);
        remove(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id));
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        List<SysMenu> menus = baseMapper.getMenusByUserId(userId);
        return menus.stream()
                .map(SysMenu::getPermission)
                .filter(p -> p != null && !p.isEmpty())
                .collect(Collectors.toList());
    }

    private List<SysMenu> buildTree(List<SysMenu> menus) {
        Map<Long, List<SysMenu>> childrenMap = new HashMap<>();
        List<SysMenu> roots = new ArrayList<>();

        for (SysMenu menu : menus) {
            childrenMap.put(menu.getId(), new ArrayList<>());
        }

        for (SysMenu menu : menus) {
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                roots.add(menu);
            } else {
                List<SysMenu> children = childrenMap.get(menu.getParentId());
                if (children != null) {
                    children.add(menu);
                }
            }
        }

        sortAndAssign(roots, childrenMap);
        return roots;
    }

    private void sortAndAssign(List<SysMenu> nodes, Map<Long, List<SysMenu>> childrenMap) {
        nodes.sort(Comparator.comparingInt(SysMenu::getSortOrder));
        for (SysMenu node : nodes) {
            List<SysMenu> children = childrenMap.get(node.getId());
            if (children != null && !children.isEmpty()) {
                node.setChildren(children);
                sortAndAssign(children, childrenMap);
            }
        }
    }
}
