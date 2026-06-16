package com.admin.mapper;

import com.admin.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("SELECT DISTINCT m.permission FROM sys_role_menu rm " +
            "JOIN sys_menu m ON rm.menu_id = m.id " +
            "JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.permission IS NOT NULL AND m.permission != ''")
    Set<String> getPermissionsByUserId(@Param("userId") Long userId);

    @Select("SELECT DISTINCT r.role_code FROM sys_role r " +
            "JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> getRoleCodesByUserId(@Param("userId") Long userId);
}
