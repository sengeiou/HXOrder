package com.happysnaker.service.impl;

import com.happysnaker.configuration.RedisCacheManager;
import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.ApiTable;
import com.happysnaker.pojo.Menu;
import com.happysnaker.pojo.Role;
import com.happysnaker.pojo.User;
import com.happysnaker.service.AuthService;
import com.happysnaker.service.base.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/2
 * @email happysnaker@foxmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AuthServiceImpl extends BaseService implements AuthService {
    @Override
    public List<Role> getUserRole(int userId) {
        return authMapper.getUserRole(userId);
    }

    private Menu dfs(List<Menu> menus, Menu cur) {
        int id = cur.getId();
        List<Menu> children = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getParentId() == cur.getId()) {
                children.add(dfs(menus, menu));
            }
        }
        cur.setChildren(children);
        return cur;
    }

    @Override
    public List<Menu> getRoleMenu(int roleId) {
        List<Menu> menus = authMapper.getRoleMenu(roleId);
        return menus;
    }

    @Override
    public List<Menu> getMenuTree() {
        List<Menu> menus = authMapper.queryMenuList();
        Set<Menu> ans = new HashSet<>();
        for (Menu menu : menus) {
            if (menu.getParentId() == 0) {
                ans.add(dfs(menus, menu));
            }
        }
        List<Menu> ret = new ArrayList(ans);
        Collections.sort(ret, (a, b)-> {
            return a.getId() - b.getId();
        });
        return ret;
    }

    @Override
    public List<ApiTable> getRoleApi(int roleId) {
        List<Menu> menus = getRoleMenu(roleId);
        return getMenuApi(menus);
    }

    @Override
    public List<ApiTable> getRoleApi(String roleName) {
        List<Menu> menus = getRoleMenu(authMapper.getRoleByRoleName(roleName).getId());
        return getMenuApi(menus);
    }

    @Override
    public List<ApiTable> getMenuApi(List<Menu> menus) {
        Set<ApiTable> apiTables = new HashSet<>();
        for (Menu menu : menus) {
            List<ApiTable> api = authMapper.getMenuApi(menu.getId());
            apiTables.addAll(api);
        }
        System.out.println("APITABLES === " + apiTables);
        return new ArrayList<>(apiTables);
    }

    @Override
    public List<Integer> getRoleStore(int roleId) {
        return authMapper.getRoleStore(roleId);
    }

    @Override
    public List<Integer> getRoleStore(String roleName) {
        return authMapper.getRoleStore(authMapper.getRoleByRoleName(roleName).getId());
    }

    @Override
    public boolean hasUser(String username) {
        return userMapper.getUser(username) != null;
    }

    @Override
    public Map getUserInfo(String username) {
        User user = userMapper.getUser(username);
        List<Role> roles = getUserRole(user.getId());
        List<String> roleName = (List<String>) roles.stream().map((role)-> {
            return role.getName();
        }).distinct().collect(Collectors.toList());;
        List<Menu> menus = getUserMenu(user.getId());
        Map map = new HashMap(3);
        map.put("menus", menus);
        map.put("username", username);
        map.put("nackname", user.getNickName());
        map.put("roles", roleName);
        map.put("avatar", user.getAvatar());
        return map;
    }

    @Override
    public void log(String username, Timestamp ts) {
        userMapper.log(username, ts);
    }

    @Override
    public boolean getUserLoginStatus(int uid) {
        try {
            if (!redisUtils.hasKey(RedisCacheManager.USER_LOGIN_STATUS_KEY)) {
                redisUtils.initRedisUserLoginStatusCache(userMapper.getUserList());
            }
            return redisUtils.getBit(RedisCacheManager.USER_LOGIN_STATUS_KEY, (long)uid);
        } catch (Exception e) {
            e.printStackTrace();
            return getUserLoginStatus(uid);
        }
    }

    @Override
    public boolean getUserLoginStatus(String username) {
        System.out.println("get==" + username + userMapper.getUser(username));
        return getUserLoginStatus(userMapper.getUser(username).getId());
    }

    @Override
    public void setUserLoginStatus(String username, boolean status) {
        redisUtils.setBit(RedisCacheManager.USER_LOGIN_STATUS_KEY, userMapper.getUser(username).getId(), status);
    }

    @Override
    public List<Role> getRoleByPagination(int pageNum, int pageSize, String keyword) {
        int offset = (pageNum - 1) * pageSize;
        if (keyword != null) {
            keyword = "%" + keyword + "%";
        }
        return authMapper.queryRoleByPagination(offset, pageSize, keyword).stream().map((role)-> {
            role.setAdminCount(authMapper.getUserCount(role.getId()));
            return role;
        }).collect(Collectors.toList());
    }

    @Override
    public int getRoleSize() {
        return authMapper.getRoleSize();
    }

    @Override
    public void addRole(Role role) {
        authMapper.insertRole(role);
    }

    @Override
    public void deleteRole(int id) {
        authMapper.deleteRole(id);
        redis.delete(RedisCacheManager.getRoleStoreListCacheKey(authMapper.getRoleName(id)));
        redis.delete(RedisCacheManager.getRoleApiListCacheKey(authMapper.getRoleName(id)));
    }

    @Override
    public void addRoleMenu(int roleId, List<Integer> menuIds, List<Integer> storeIds) {
        authMapper.deleteRoleMenu(roleId);
        for (Integer menuId : menuIds) {
            System.out.println(roleId + " == " + menuId);
            authMapper.insertRoleMenu(roleId, menuId);
        }
        authMapper.deleteRoleStore(roleId);
        for (Integer storeId : storeIds) {
            authMapper.insertRoleStore(roleId, storeId);
        }
        redis.delete(RedisCacheManager.getRoleStoreListCacheKey(authMapper.getRoleName(roleId)));
        redis.delete(RedisCacheManager.getRoleApiListCacheKey(authMapper.getRoleName(roleId)));
    }

    @Override
    public void updateRole(Role role) throws UpdateException {
        int row = authMapper.updateRole(role);
        if (row == 0) {
            throw new UpdateException("不存在该角色请检查: " + role);
        }
        redis.delete(RedisCacheManager.getRoleStoreListCacheKey(role.getName()));
        redis.delete(RedisCacheManager.getRoleApiListCacheKey(role.getName()));
    }

    @Override
    public void addUserRole(int userId, List<Integer> roleIds) {
        authMapper.deleteUserRole(userId);
        for (Integer roleId : roleIds) {
            authMapper.insertUserRole(userId, roleId);
        }
    }

}
