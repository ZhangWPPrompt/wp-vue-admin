package com.admin.service.impl;

import com.admin.dto.PageResult;
import com.admin.entity.SysOperationLog;
import com.admin.mapper.SysOperationLogMapper;
import com.admin.service.SysOperationLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog>
        implements SysOperationLogService {

    @Override
    public void saveLog(SysOperationLog log) {
        baseMapper.insert(log);
    }

    @Override
    public PageResult<SysOperationLog> page(Integer page, Integer pageSize, String keyword) {
        LambdaQueryWrapper<SysOperationLog> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(SysOperationLog::getUsername, keyword)
                   .or()
                   .like(SysOperationLog::getOperation, keyword);
        }
        wrapper.orderByDesc(SysOperationLog::getCreateTime);

        Page<SysOperationLog> result = baseMapper.selectPage(new Page<>(page, pageSize), wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public SysOperationLog getById(Long id) {
        return baseMapper.selectById(id);
    }
}
