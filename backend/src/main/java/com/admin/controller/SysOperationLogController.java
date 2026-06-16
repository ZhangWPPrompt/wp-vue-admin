package com.admin.controller;

import com.admin.dto.PageResult;
import com.admin.entity.SysOperationLog;
import com.admin.service.SysOperationLogService;
import com.admin.util.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class SysOperationLogController {

    private final SysOperationLogService sysOperationLogService;

    public SysOperationLogController(SysOperationLogService sysOperationLogService) {
        this.sysOperationLogService = sysOperationLogService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('sys:log:list')")
    public Result<PageResult<SysOperationLog>> list(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                    @RequestParam(required = false) String keyword) {
        return Result.ok(sysOperationLogService.page(page, pageSize, keyword));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:log:list')")
    public Result<SysOperationLog> getById(@PathVariable Long id) {
        return Result.ok(sysOperationLogService.getById(id));
    }
}
