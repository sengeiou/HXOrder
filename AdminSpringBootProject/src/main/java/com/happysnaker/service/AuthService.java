package com.happysnaker.service;

import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.ApiTable;
import com.happysnaker.pojo.Menu;
import com.happysnaker.pojo.Role;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/2
 * @email happysnaker@foxmail.com
 */
public interface AuthService {

    /**
     * 获取用户菜单
     * @param userId
     * @return
     */
    default List<Menu> getUserMenu(int userId) {
        List<Menu> menus = new ArrayList<>();
        for (Role role : this.getUserRole(userId)) {
            menus.addAll(this.getRoleMenu(role.getId()));
        }
        return menus;
    }

    /**
     * 获取用户角色
     * @param userId
     * @return
     */
    List<Role> getUserRole(int userId);

    /**
     * 获取角色菜单
     * @param roleId
     * @return
     */
    List<Menu> getRoleMenu(int roleId);

    /**
     * 生产菜单树
     * @return
     */
    List<Menu> getMenuTree();

    /**
     * 获取角色可操控api
     * @param roleId
     * @return
     */
    List<ApiTable> getRoleApi(int roleId);

    /**
     * 获取角色可操控api
     * @param roleName
     * @return
     */
    List<ApiTable> getRoleApi(String roleName);

    /**
     * 获取菜单对应的API
     * @param menus
     * @return
     */
    List<ApiTable> getMenuApi(List<Menu> menus);

    /**
     * 获取角色可访问店铺
     * @param roleId
     * @return
     */
    List<Integer> getRoleStore(int roleId);

    List<Integer> getRoleStore(String roleName);

    boolean hasUser(String username);

    /**
     * 获取用户信息
     * @param username
     * @return
     */
    Map getUserInfo(String username);

    /**
     * 记录用户登录
     * @param username
     * @param ts
     */
    void log(String username, Timestamp ts);

    /**
     * 获取用户登录状态
     * @param uid
     * @return
     */
    boolean getUserLoginStatus(int uid);
    /**
     * 获取用户登录状态
     * @param uid
     * @return
     */
    boolean getUserLoginStatus(String username);

    void setUserLoginStatus(String username, boolean status);

    List<Role> getRoleByPagination(int pageNum, int pageSize, String keyword);

    int getRoleSize();

    void addRole(Role role);

    void deleteRole(int id);

    void addRoleMenu(int roleId, List<Integer> menuIds, List<Integer> storeIds);

    void updateRole(Role role) throws UpdateException;

    void addUserRole(int userId, List<Integer> roleIds);
}
