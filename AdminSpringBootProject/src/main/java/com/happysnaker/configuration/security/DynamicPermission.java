package com.happysnaker.configuration.security;

import com.happysnaker.configuration.RedisCacheManager;
import com.happysnaker.mapper.StoreMapper;
import com.happysnaker.pojo.ApiTable;
import com.happysnaker.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * 动态鉴权
 * @author happysnakers
 */
@Component
public class DynamicPermission {


    @Autowired
    private AuthService Service;

    @Qualifier("myRedis")
    @Autowired
    private RedisTemplate redis;

    @Autowired
    private RedisCacheManager redisUtils;


    @Autowired
    private StoreMapper storeMapper;
    private final String STORE_ID_PARAM = "storeId";
    private final String TEST_ACCOUNT = "guest";

    /**
     * 判断有访问API的权限
     * @param request
     * @param authentication
     */
    public boolean checkPermission(HttpServletRequest request,
                                   Authentication authentication)  {

        Object principal = authentication.getPrincipal();
        String storeId = request.getParameter(STORE_ID_PARAM);
        String urlApi = request.getRequestURI();


        if(principal instanceof UserDetails) {

            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            // 如果是测试账号，只允许发起 Get
            System.out.println("username == " + username + " met == " + request.getMethod());
            if (username.equals(TEST_ACCOUNT) && !request.getMethod().equalsIgnoreCase("GET")) {
                return false;
            }
            Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
            //只要有任意角色拥有对应权限，则通过
            for (GrantedAuthority role : roles) {



                if (hasPermission(storeId, urlApi, role.getAuthority())) {
                    return true;
                }
            }
            System.out.println("鉴权失败" + username + " 无权限访问 api: " + urlApi);
        }

        return false;
    }

    /**
     * 检查api与store权限，如果用户请求参数中带有 storeId，则检查用户是否拥有该店铺权限<br/>
     * 如果不带有 storeId，则用户未清求，不检查，此时 storeId = -1, storeIdstr = null*/
    private boolean hasPermission(String storeIdStr, String api, String role) {
        System.out.println("storeId == " + storeIdStr);
        Integer storeId = null;
        if (storeIdStr != null) {
            try {
                storeId = Integer.parseInt(storeIdStr);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        List authApis = null, authStores = null;

        redis.delete(RedisCacheManager.getRoleApiListCacheKey(role));
        redis.delete(RedisCacheManager.getRoleStoreListCacheKey(role));



        if (!redisUtils.hasKey(RedisCacheManager.getRoleApiListCacheKey(role))) {
            initRedisApiCache(role);
        }

        if (!redisUtils.hasKey(RedisCacheManager.getRoleStoreListCacheKey(role))) {
            initRedisStoreCache(role);
        }


        try {
            authApis = redis.opsForList().range(RedisCacheManager.getRoleApiListCacheKey(role), 0, -1);
            authStores = redis.opsForList().range(RedisCacheManager.getRoleStoreListCacheKey(role), 0, -1);
            System.out.println("角色 " + role + " 用有的 API 权限 = " + authApis);
            if (api == null || authApis.indexOf(api) == -1) {
                return false;
            }

            //用户必须请求带有 storeId 才验证，否则不验证
            if (storeId != null) {
                if (storeId == -1) {
                    // 需要全部权限
                    for (Integer id : storeMapper.queryAllStoreId()) {
                        if (authStores.indexOf(id) == -1) {
                            return false;
                        }
                    }
                } else if (authStores.indexOf(storeId) == -1) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return hasPermission(storeIdStr, api, role);
        }
    }

    private void initRedisApiCache(String role) {
        List<ApiTable> roleApis = Service.getRoleApi(role);
        redisUtils.initRedisApiCache(role, roleApis);
    }

    private void initRedisStoreCache(String role) {
        List<Integer> roleStores = Service.getRoleStore(role);
        System.out.println("role = " + role + " stores = " + roleStores);
        redisUtils.initRedisStoreCache(role, roleStores);
    }
}

























