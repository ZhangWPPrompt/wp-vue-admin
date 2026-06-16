package com.admin.controller;

import com.admin.dto.LoginRequest;
import com.admin.dto.LoginResponse;
import com.admin.security.SysUserDetails;
import com.admin.service.SysMenuService;
import com.admin.service.SysUserService;
import com.admin.util.Result;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SysUserService sysUserService;
    private final SysMenuService sysMenuService;

    public AuthController(SysUserService sysUserService, SysMenuService sysMenuService) {
        this.sysUserService = sysUserService;
        this.sysMenuService = sysMenuService;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = sysUserService.login(request);
        return Result.ok(response);
    }

    @GetMapping("/userinfo")
    public Result<Map<String, Object>> getUserInfo(@AuthenticationPrincipal SysUserDetails userDetails) {
        Map<String, Object> info = new HashMap<>();
        info.put("username", userDetails.getUser().getUsername());
        info.put("nickname", userDetails.getUser().getNickname());
        info.put("avatar", userDetails.getUser().getAvatar());
        info.put("permissions", userDetails.getPermissions());

        List<Map<String, Object>> menus = convertMenus(
                sysMenuService.getUserMenus(userDetails.getUser().getId()));
        info.put("menus", menus);

        return Result.ok(info);
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        return Result.ok();
    }

    private List<Map<String, Object>> convertMenus(List<com.admin.entity.SysMenu> menus) {
        if (menus == null) return java.util.Collections.emptyList();
        return menus.stream().map(menu -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", menu.getId());
            m.put("parentId", menu.getParentId());
            m.put("name", menu.getMenuName());
            m.put("path", menu.getPath());
            m.put("component", menu.getComponent());
            m.put("icon", menu.getIcon());
            m.put("type", menu.getMenuType());
            m.put("sortOrder", menu.getSortOrder());
            m.put("permission", menu.getPermission());
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                m.put("children", convertMenus(menu.getChildren()));
            }
            return m;
        }).toList();
    }
}
