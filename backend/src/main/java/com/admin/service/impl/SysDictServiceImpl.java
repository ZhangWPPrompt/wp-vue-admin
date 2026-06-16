package com.admin.service.impl;

import com.admin.dto.PageResult;
import com.admin.entity.SysDict;
import com.admin.entity.SysDictItem;
import com.admin.mapper.SysDictItemMapper;
import com.admin.mapper.SysDictMapper;
import com.admin.service.SysDictService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    private final SysDictItemMapper sysDictItemMapper;

    public SysDictServiceImpl(SysDictItemMapper sysDictItemMapper) {
        this.sysDictItemMapper = sysDictItemMapper;
    }

    @Override
    public PageResult<SysDict> page(Integer page, Integer pageSize, String keyword) {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(SysDict::getDictName, keyword)
                   .or()
                   .like(SysDict::getDictCode, keyword);
        }
        wrapper.orderByDesc(SysDict::getCreateTime);

        Page<SysDict> result = baseMapper.selectPage(new Page<>(page, pageSize), wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public SysDict getById(Long id) {
        return super.getById(id);
    }

    @Override
    @Transactional
    public void create(SysDict dict) {
        save(dict);
    }

    @Override
    @Transactional
    public void update(SysDict dict) {
        updateById(dict);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        sysDictItemMapper.delete(new LambdaQueryWrapper<SysDictItem>().eq(SysDictItem::getDictId, id));
        removeById(id);
    }

    @Override
    public List<SysDictItem> getDictItems(String dictCode) {
        SysDict dict = getOne(new LambdaQueryWrapper<SysDict>().eq(SysDict::getDictCode, dictCode));
        if (dict == null) {
            return List.of();
        }
        return sysDictItemMapper.selectList(
                new LambdaQueryWrapper<SysDictItem>()
                        .eq(SysDictItem::getDictId, dict.getId())
                        .eq(SysDictItem::getStatus, 1)
                        .orderByAsc(SysDictItem::getSortOrder));
    }

    @Override
    @Transactional
    public void createDictItem(SysDictItem item) {
        sysDictItemMapper.insert(item);
    }

    @Override
    @Transactional
    public void updateDictItem(SysDictItem item) {
        sysDictItemMapper.updateById(item);
    }

    @Override
    @Transactional
    public void deleteDictItem(Long id) {
        sysDictItemMapper.deleteById(id);
    }
}
