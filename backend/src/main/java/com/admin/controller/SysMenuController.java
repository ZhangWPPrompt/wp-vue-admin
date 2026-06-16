package com.admin.controller;

import com.admin.entity.SysMenu;
import com.admin.service.SysMenuService;
import com.admin.util.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class SysMenuController {

    private final SysMenuService sysMenuService;

    public SysMenuController(SysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public Result<List<SysMenu>> tree() {
        return Result.ok(sysMenuService.listTree());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public Result<SysMenu> getById(@PathVariable Long id) {
        return Result.ok(sysMenuService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('sys:menu:add')")
    public Result<?> create(@RequestBody SysMenu menu) {
        sysMenuService.create(menu);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:menu:edit')")
    public Result<?> update(@PathVariable Long id, @RequestBody SysMenu menu) {
        menu.setId(id);
        sysMenuService.update(menu);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public Result<?> delete(@PathVariable Long id) {
        sysMenuService.delete(id);
        return Result.ok();
    }
}
