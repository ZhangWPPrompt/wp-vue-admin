package com.admin.service;

import com.admin.dto.PageResult;
import com.admin.entity.SysOperationLog;

public interface SysOperationLogService {
    void saveLog(SysOperationLog log);
    PageResult<SysOperationLog> page(Integer page, Integer pageSize, String keyword);
    SysOperationLog getById(Long id);
}
