package com.admin.controller;

import com.admin.dto.PageResult;
import com.admin.entity.SysUser;
import com.admin.service.SysUserService;
import com.admin.util.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class SysUserController {

    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Result<PageResult<SysUser>> list(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @RequestParam(required = false) String keyword) {
        return Result.ok(sysUserService.page(page, pageSize, keyword));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.ok(sysUserService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:add')")
    public Result<?> create(@RequestBody SysUser user) {
        sysUserService.create(user);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:edit')")
    public Result<?> update(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        sysUserService.update(user);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result<?> delete(@PathVariable Long id) {
        sysUserService.delete(id);
        return Result.ok();
    }

    @GetMapping("/{userId}/roles")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Result<List<Long>> getUserRoleIds(@PathVariable Long userId) {
        return Result.ok(sysUserService.getUserRoleIds(userId));
    }

    @PutMapping("/{userId}/roles")
    @PreAuthorize("hasAuthority('sys:user:edit')")
    public Result<?> assignRoles(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        sysUserService.assignRoles(userId, roleIds);
        return Result.ok();
    }
}
