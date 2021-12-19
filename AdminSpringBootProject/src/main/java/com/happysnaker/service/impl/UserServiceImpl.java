package com.happysnaker.service.impl;

import com.happysnaker.configuration.RedisCacheManager;
import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.User;
import com.happysnaker.service.UserService;
import com.happysnaker.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/2
 * @email happysnaker@foxmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends BaseService implements UserService {
    @Autowired
    private BCryptPasswordEncoder encoder;

    private List<User> getStatusFromRedis(List<User> users) {
        if (!redisUtils.hasKey(RedisCacheManager.USER_ENABLE_STATUS_KEY)) {
            redisUtils.initRedisUserEnableStatusCache(userMapper.getUserList());
        }
        try {
            for (User user : users) {
                System.out.println(user.getId() + " --> redis --> " + redisUtils.getBit(RedisCacheManager.USER_ENABLE_STATUS_KEY, user.getId()));
                user.setEnable(redisUtils.getBit(RedisCacheManager.USER_ENABLE_STATUS_KEY, user.getId()));
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            //重新尝试
            return getStatusFromRedis(users);
        }
    }

    @Override
    public User getUser(String username) {
        return userMapper.getUser(username);
    }

    @Override
    public List<User> getDishListByPagination(int pageNum, int pageSize, String keyword, Integer status) {
        int offset = (pageNum - 1)* pageSize;
        if (keyword != null) {
            keyword = "%" + keyword + "%";
        }
        //状态以缓存中为主，因此我们不从数据库中读 status
        List<User> users = getStatusFromRedis(userMapper.queryUserByPagination(offset, pageSize, keyword));
        //如果用户确实查询了状态，则根据缓存中上架状态进行过滤
        System.out.println("status == " + status);
        if (status != null) {
            users = users.stream().filter((u)-> {
                return redisUtils.getBit(
                        RedisCacheManager.USER_ENABLE_STATUS_KEY,
                        u.getId()
                ) == status;
            }).collect(Collectors.toList());
        }
        return users;
    }

    @Override
    public void updateStatus(int id, int status) {
        redisUtils.setBit(RedisCacheManager.USER_ENABLE_STATUS_KEY, id, status);
    }

    @Override
    public void deleteUser(int id) throws UpdateException {
        int row = userMapper.deleteUser(id);
        if (row == 0) {
            throw new UpdateException("不存在 id 为 " + id + " 的用户，删除失败");
        }
        redis.delete(RedisCacheManager.USER_ENABLE_STATUS_KEY);
    }

    @Override
    public int addUser(User user) {
        //对密码进行加密
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        userMapper.insertUser(user);
        redis.delete(RedisCacheManager.USER_ENABLE_STATUS_KEY);
        return userMapper.getUser(user.getUsername()).getId();
    }

    @Override
    public void updateUser(User user) throws UpdateException {
        this.deleteUser(user.getId());
        this.addUser(user);
    }

    @Override
    public int getSize() {
        return userMapper.getSize();
    }
}
