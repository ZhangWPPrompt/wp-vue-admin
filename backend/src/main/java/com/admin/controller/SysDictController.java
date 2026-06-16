package com.admin.controller;

import com.admin.dto.PageResult;
import com.admin.entity.SysDict;
import com.admin.entity.SysDictItem;
import com.admin.service.SysDictService;
import com.admin.util.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dicts")
public class SysDictController {

    private final SysDictService sysDictService;

    public SysDictController(SysDictService sysDictService) {
        this.sysDictService = sysDictService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('sys:dict:list')")
    public Result<PageResult<SysDict>> list(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @RequestParam(required = false) String keyword) {
        return Result.ok(sysDictService.page(page, pageSize, keyword));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:dict:list')")
    public Result<SysDict> getById(@PathVariable Long id) {
        return Result.ok(sysDictService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('sys:dict:add')")
    public Result<?> create(@RequestBody SysDict dict) {
        sysDictService.create(dict);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:dict:edit')")
    public Result<?> update(@PathVariable Long id, @RequestBody SysDict dict) {
        dict.setId(id);
        sysDictService.update(dict);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:dict:delete')")
    public Result<?> delete(@PathVariable Long id) {
        sysDictService.delete(id);
        return Result.ok();
    }

    @GetMapping("/code/{dictCode}")
    public Result<List<SysDictItem>> getDictItems(@PathVariable String dictCode) {
        return Result.ok(sysDictService.getDictItems(dictCode));
    }

    @PostMapping("/{dictId}/items")
    @PreAuthorize("hasAuthority('sys:dict:add')")
    public Result<?> createItem(@PathVariable Long dictId, @RequestBody SysDictItem item) {
        item.setDictId(dictId);
        sysDictService.createDictItem(item);
        return Result.ok();
    }

    @PutMapping("/{dictId}/items/{itemId}")
    @PreAuthorize("hasAuthority('sys:dict:edit')")
    public Result<?> updateItem(@PathVariable Long dictId, @PathVariable Long itemId,
                                @RequestBody SysDictItem item) {
        item.setId(itemId);
        item.setDictId(dictId);
        sysDictService.updateDictItem(item);
        return Result.ok();
    }

    @DeleteMapping("/{dictId}/items/{itemId}")
    @PreAuthorize("hasAuthority('sys:dict:delete')")
    public Result<?> deleteItem(@PathVariable Long dictId, @PathVariable Long itemId) {
        sysDictService.deleteDictItem(itemId);
        return Result.ok();
    }
}
