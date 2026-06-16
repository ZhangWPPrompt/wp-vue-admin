package com.admin.service.impl;

import com.admin.dto.LoginRequest;
import com.admin.dto.LoginResponse;
import com.admin.dto.PageResult;
import com.admin.entity.*;
import com.admin.mapper.*;
import com.admin.security.JwtTokenProvider;
import com.admin.security.SysUserDetails;
import com.admin.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final SysRoleMapper sysRoleMapper;
    private final SysUserRoleMapper sysUserRoleMapper;

    public SysUserServiceImpl(AuthenticationManager authenticationManager,
                              JwtTokenProvider jwtTokenProvider,
                              PasswordEncoder passwordEncoder,
                              SysRoleMapper sysRoleMapper,
                              SysUserRoleMapper sysUserRoleMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.sysRoleMapper = sysRoleMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SysUserDetails userDetails = (SysUserDetails) authentication.getPrincipal();
        SysUser user = userDetails.getUser();

        List<String> roles = sysRoleMapper.getRoleCodesByUserId(user.getId());
        Set<String> permissions = sysRoleMapper.getPermissionsByUserId(user.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("nickname", user.getNickname());

        String token = jwtTokenProvider.generateToken(user.getUsername(), claims);

        return new LoginResponse(token, user.getUsername(), user.getNickname(),
                user.getAvatar(), roles, permissions);
    }

    @Override
    public SysUser getUserByUsername(String username) {
        return getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }

    @Override
    public PageResult<SysUser> page(Integer page, Integer pageSize, String keyword) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(SysUser::getUsername, keyword)
                   .or()
                   .like(SysUser::getNickname, keyword);
        }
        wrapper.orderByDesc(SysUser::getCreateTime);

        Page<SysUser> result = baseMapper.selectPage(new Page<>(page, pageSize), wrapper);
        result.getRecords().forEach(u -> u.setPassword(null));
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public SysUser getById(Long id) {
        SysUser user = super.getById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    @Override
    @Transactional
    public void create(SysUser user) {
        if (getUserByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
    }

    @Override
    @Transactional
    public void update(SysUser user) {
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        updateById(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
        removeById(id);
    }

    @Override
    @Transactional
    public void assignRoles(Long userId, List<Long> roleIds) {
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
        if (roleIds != null && !roleIds.isEmpty()) {
            roleIds.forEach(roleId -> {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                sysUserRoleMapper.insert(ur);
            });
        }
    }

    @Override
    public List<Long> getUserRoleIds(Long userId) {
        return sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId))
                .stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }
}
