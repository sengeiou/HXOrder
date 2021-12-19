package com.happysnaker.service;

import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.User;

import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/2
 * @email happysnaker@foxmail.com
 */
public interface UserService {

    /**
     * 获取用户
     * @param username
     * @return
     */
    User getUser(String username);

    /**
     * 分页查询，无聊的重复造轮子
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @return
     */
    List<User> getDishListByPagination(int pageNum, int pageSize, String keyword,  Integer status);

    /**
     * 更改启用状态
     * @param id
     * @param status
     */
    void updateStatus(int id, int status);

    void deleteUser(int id) throws UpdateException;

    int addUser(User user);

    void updateUser(User user) throws UpdateException;

    default void deleteUser(List<Integer> ids) throws UpdateException {
        for (Integer id : ids) {
            this.deleteUser(id);
        }
    }

    int getSize();
}
