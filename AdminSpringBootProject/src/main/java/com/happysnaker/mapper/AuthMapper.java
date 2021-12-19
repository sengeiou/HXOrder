package com.happysnaker.mapper;

import com.happysnaker.pojo.ApiTable;
import com.happysnaker.pojo.Menu;
import com.happysnaker.pojo.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/2
 * @email happysnaker@foxmail.com
 */
@Mapper
public interface AuthMapper {

    @Select("SELECT name FROM `auth_role` WHERE id = #{id}")
    String getRoleName(int id);

    @Select("SELECT count(*) FROM `auth_admin_role_relation` WHERE role_id = #{id}")
    int getUserCount(int id);

    @Insert("INSERT INTO `auth_role`(`id`,`name`,`remark`) SELECT #{id},#{name},#{remark}")
    int insertRole(Role role);

    @Delete("DELETE FROM `auth_role` WHERE id = #{id}")
    int deleteRole(int id);

    @Update("UPDATE`auth_role` set `id` = #{id}, " +
            "`name` = #{name}," +
            "`remark` = #{remark} WHERE id = #{id}")
    int updateRole(Role role);

    @Insert("INSERT INTO `auth_role_menu_relation` SELECT #{rid}, #{mid}")
    int insertRoleMenu(@Param("rid") int roleId, @Param("mid") int menuId);

    @Delete("DELETE FROM `auth_role_menu_relation` WHERE role_id = #{roleId}")
    int deleteRoleMenu(int roleId);

    @Insert("INSERT INTO `auth_admin_role_relation` SELECT #{uid}, #{rid}")
    int insertUserRole(@Param("uid") int userId, @Param("rid") int roleId);

    @Delete("DELETE FROM `auth_admin_role_relation` WHERE admin_id = #{uid}")
    int deleteUserRole(@Param("uid") int userId);

    @Insert("INSERT INTO `auth_role_store_relation` SELECT #{rid}, #{sid}")
    int insertRoleStore(@Param("rid") int roleId, @Param("sid") int storeId);

    @Delete("DELETE FROM `auth_role_store_relation` WHERE role_id = #{rid}")
    int deleteRoleStore(@Param("rid") int roleId);

    @Select("SELECT * FROM `auth_menu`")
    List<Menu> queryMenuList();

    @Select("SELECT count(*) FROM `auth_role`")
    int getRoleSize();

    List<Role> queryRoleByPagination(@Param("offset") int offset, @Param("num") int num, @Param("keyword") String keyword);

    List<Menu> getRoleMenu(int roleId);


    List<Role> getUserRole(int userId);


    List<ApiTable> getMenuApi(int menuId);


    @Select("SELECT store_id FROM `auth_role_store_relation` " +
            "WHERE role_id = #{roleId}")
    List<Integer> getRoleStore(int roleId);

    @Select("SELECT * FROM `auth_role` WHERE name = #{role}")
    Role getRoleByRoleName(String role);
}
