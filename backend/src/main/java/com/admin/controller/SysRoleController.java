package com.admin.controller;

import com.admin.dto.PageResult;
import com.admin.entity.SysRole;
import com.admin.service.SysRoleService;
import com.admin.util.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class SysRoleController {

    private final SysRoleService sysRoleService;

    public SysRoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result<PageResult<SysRole>> list(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @RequestParam(required = false) String keyword) {
        return Result.ok(sysRoleService.page(page, pageSize, keyword));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result<List<SysRole>> listAll() {
        return Result.ok(sysRoleService.list());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result<SysRole> getById(@PathVariable Long id) {
        return Result.ok(sysRoleService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('sys:role:add')")
    public Result<?> create(@RequestBody SysRole role) {
        sysRoleService.create(role);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:role:edit')")
    public Result<?> update(@PathVariable Long id, @RequestBody SysRole role) {
        role.setId(id);
        sysRoleService.update(role);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Result<?> delete(@PathVariable Long id) {
        sysRoleService.delete(id);
        return Result.ok();
    }

    @GetMapping("/{roleId}/menus")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result<List<Long>> getRoleMenuIds(@PathVariable Long roleId) {
        return Result.ok(sysRoleService.getRoleMenuIds(roleId));
    }

    @PutMapping("/{roleId}/menus")
    @PreAuthorize("hasAuthority('sys:role:edit')")
    public Result<?> assignMenus(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        sysRoleService.assignMenus(roleId, menuIds);
        return Result.ok();
    }
}
