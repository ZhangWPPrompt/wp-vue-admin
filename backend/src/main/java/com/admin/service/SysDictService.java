package com.admin.service;

import com.admin.dto.PageResult;
import com.admin.entity.SysDict;
import com.admin.entity.SysDictItem;

import java.util.List;

public interface SysDictService {
    PageResult<SysDict> page(Integer page, Integer pageSize, String keyword);
    SysDict getById(Long id);
    void create(SysDict dict);
    void update(SysDict dict);
    void delete(Long id);
    List<SysDictItem> getDictItems(String dictCode);
    void createDictItem(SysDictItem item);
    void updateDictItem(SysDictItem item);
    void deleteDictItem(Long id);
}
