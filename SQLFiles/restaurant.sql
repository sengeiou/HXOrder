/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : restaurant

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 19/12/2021 12:26:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for auth_admin_role_relation
-- ----------------------------
DROP TABLE IF EXISTS `auth_admin_role_relation`;
CREATE TABLE `auth_admin_role_relation`  (
  `admin_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`admin_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_admin_role_relation
-- ----------------------------
INSERT INTO `auth_admin_role_relation` VALUES (1, 1);
INSERT INTO `auth_admin_role_relation` VALUES (125, 1);
INSERT INTO `auth_admin_role_relation` VALUES (126, 4);
INSERT INTO `auth_admin_role_relation` VALUES (126, 5);
INSERT INTO `auth_admin_role_relation` VALUES (128, 6);

-- ----------------------------
-- Table structure for auth_api
-- ----------------------------
DROP TABLE IF EXISTS `auth_api`;
CREATE TABLE `auth_api`  (
  `id` int NOT NULL,
  `uri` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_api
-- ----------------------------
INSERT INTO `auth_api` VALUES (1, '/store/list', '获取店铺信息');
INSERT INTO `auth_api` VALUES (2, '/dish/list', '获取菜品信息');
INSERT INTO `auth_api` VALUES (3, '/dish/update_publish_status', '更改菜品状态');
INSERT INTO `auth_api` VALUES (4, '/dish/update_recommend_status', '更改菜品状态');
INSERT INTO `auth_api` VALUES (5, '/dish/update_new_status', '更改菜品状态');
INSERT INTO `auth_api` VALUES (6, '/dish/dish_stock_list', '获取菜品库存信息');
INSERT INTO `auth_api` VALUES (7, '/dish/update_dish_stock', '更新菜品库存信息');
INSERT INTO `auth_api` VALUES (8, '/dish/add', '添加菜品');
INSERT INTO `auth_api` VALUES (9, '/dish/classification_list', '获取菜品分类');
INSERT INTO `auth_api` VALUES (10, '/dish/delete', '删除菜品');
INSERT INTO `auth_api` VALUES (11, '/dish/get', '获取某一菜品');
INSERT INTO `auth_api` VALUES (12, '/dish/update', '更新菜品');
INSERT INTO `auth_api` VALUES (13, '/combo/add', '添加套餐');
INSERT INTO `auth_api` VALUES (14, '/combo/list', '套餐列表');
INSERT INTO `auth_api` VALUES (15, '/combo/update_publish_status', '更改套餐状态');
INSERT INTO `auth_api` VALUES (16, '/combo/combo_dish_list', '套餐菜品列表');
INSERT INTO `auth_api` VALUES (17, '/combo/update_combo_dish', '更改套餐菜品列表');
INSERT INTO `auth_api` VALUES (18, '/combo/get_combo', '更改套餐');
INSERT INTO `auth_api` VALUES (19, '/combo/update', '更改套餐');
INSERT INTO `auth_api` VALUES (20, '/combo/delete_combo_dish', '更改套餐');
INSERT INTO `auth_api` VALUES (21, '/combo/delete', '删除套餐');
INSERT INTO `auth_api` VALUES (22, '/store/list', '店铺列表');
INSERT INTO `auth_api` VALUES (23, '/store/add', '增加店铺');
INSERT INTO `auth_api` VALUES (24, '/store/update', '更新店铺');
INSERT INTO `auth_api` VALUES (25, '/store/delete', '删除店铺');
INSERT INTO `auth_api` VALUES (26, '/store/update_working_status', '更改状态');
INSERT INTO `auth_api` VALUES (27, '/store/update_takeout_status', '更改状态');
INSERT INTO `auth_api` VALUES (28, '/store/get', '获取店铺');
INSERT INTO `auth_api` VALUES (29, '/marketing/discount_list', '菜品折扣');
INSERT INTO `auth_api` VALUES (31, '/marketing/add_discount', '添加折扣');
INSERT INTO `auth_api` VALUES (32, '/marketing/delete_discount', '删除折扣');
INSERT INTO `auth_api` VALUES (33, '/marketing/update_discount', '更新折扣');
INSERT INTO `auth_api` VALUES (34, '/upload_img', '上传图片');
INSERT INTO `auth_api` VALUES (35, '/admin/list', '用户列表');
INSERT INTO `auth_api` VALUES (36, '/admin/update', '更新');
INSERT INTO `auth_api` VALUES (37, '/admin/delete', '删除用户');
INSERT INTO `auth_api` VALUES (38, '/admin/add', '添加用户');
INSERT INTO `auth_api` VALUES (39, '/admin/updateStatus', '');
INSERT INTO `auth_api` VALUES (40, '/role/list', '角色列表');
INSERT INTO `auth_api` VALUES (41, '/admin/role', '角色列表');
INSERT INTO `auth_api` VALUES (42, '/role/update', '角色列表');
INSERT INTO `auth_api` VALUES (43, '/role/add', '角色列表');
INSERT INTO `auth_api` VALUES (44, '/role/delete', '角色列表');
INSERT INTO `auth_api` VALUES (45, '/menu/treeList', '菜单列表');
INSERT INTO `auth_api` VALUES (46, '/role/listMenu', '菜单列表');
INSERT INTO `auth_api` VALUES (47, '/role/allocMenu', '菜单分配');
INSERT INTO `auth_api` VALUES (48, '/admin/alloc_role', '菜单分配');
INSERT INTO `auth_api` VALUES (49, '/role/listStore', '菜单分配');
INSERT INTO `auth_api` VALUES (50, '/order/list', '订单');
INSERT INTO `auth_api` VALUES (51, '/order/update_type', '订单状态');
INSERT INTO `auth_api` VALUES (52, '/message/send', '发送站内信');
INSERT INTO `auth_api` VALUES (53, '/order/reject_confirm', '发送站内信');
INSERT INTO `auth_api` VALUES (54, '/order/delete', '删除订单');
INSERT INTO `auth_api` VALUES (55, '/order/get', '查看订单');
INSERT INTO `auth_api` VALUES (56, '/order/handle_to_be_paid_order', '支付订单');
INSERT INTO `auth_api` VALUES (57, '/home', '支付订单');

-- ----------------------------
-- Table structure for auth_menu
-- ----------------------------
DROP TABLE IF EXISTS `auth_menu`;
CREATE TABLE `auth_menu`  (
  `id` int NOT NULL,
  `parent_id` int NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '事实上 name, title 等这些信息在项目中都没有用上, 这是因为我们在前端写死了, 如果允许用户添加菜单的话,前端就要动态获取后端数据并蛇者',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hidden` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_menu
-- ----------------------------
INSERT INTO `auth_menu` VALUES (1, 0, 'home', '首页', 'home', 0);
INSERT INTO `auth_menu` VALUES (2, 0, 'dish-ms', '菜品', 'dish', 0);
INSERT INTO `auth_menu` VALUES (3, 2, 'dish', '菜品列表', 'dish-list', 0);
INSERT INTO `auth_menu` VALUES (4, 2, 'addDish', '添加菜品', 'dish-add', 0);
INSERT INTO `auth_menu` VALUES (5, 2, 'updateDish', '修改菜品', 'dish-add', 1);
INSERT INTO `auth_menu` VALUES (6, 2, 'combo', '套餐列表', 'dish-list', 0);
INSERT INTO `auth_menu` VALUES (7, 2, 'addCombo', '添加套餐', 'dish-add', 0);
INSERT INTO `auth_menu` VALUES (8, 2, 'updateCombo', '修改套餐', 'dish-add', 1);
INSERT INTO `auth_menu` VALUES (9, 0, 'order-ms', '订单', 'order', 0);
INSERT INTO `auth_menu` VALUES (10, 9, 'order', '订单列表', 'dish-list', 0);
INSERT INTO `auth_menu` VALUES (11, 9, 'toBePaidOrderList', '待支付订单', 'dish-list', 0);
INSERT INTO `auth_menu` VALUES (12, 9, 'toBeConfirmedOrderList', '待确认订单', 'dish-list', 0);
INSERT INTO `auth_menu` VALUES (13, 9, 'ealPreparationOrderList', '备餐中订单', 'dish-list', 0);
INSERT INTO `auth_menu` VALUES (14, 9, 'inDistributionOrderList', '配送中订单', 'dish-list', 0);
INSERT INTO `auth_menu` VALUES (15, 9, 'toBeDinedOrderList', '待用餐订单', 'dish-list', 0);
INSERT INTO `auth_menu` VALUES (16, 9, 'cancelOrderList', '取消中订单', 'dish-list', 0);
INSERT INTO `auth_menu` VALUES (17, 9, 'mealWaitingOrderList', '待取餐订单', 'dish-list', 0);
INSERT INTO `auth_menu` VALUES (18, 9, 'orderDetail', '订单详情', '', 1);
INSERT INTO `auth_menu` VALUES (19, 9, 'handleToBePaidOrder', '处理支付订单', NULL, 1);
INSERT INTO `auth_menu` VALUES (20, 9, 'handleCancelOrder', '处理取消订单', NULL, 1);
INSERT INTO `auth_menu` VALUES (21, 0, 'store-ms', '店铺', 'dish', 0);
INSERT INTO `auth_menu` VALUES (22, 21, 'store', '店铺列表', 'dish-list', 0);
INSERT INTO `auth_menu` VALUES (23, 21, 'addStore', '添加店铺', 'dish-add', 0);
INSERT INTO `auth_menu` VALUES (24, 21, 'updateStore', '修改店铺', 'dish-add', 1);
INSERT INTO `auth_menu` VALUES (25, 0, 'marketing-ms', '营销', 'mms', 0);
INSERT INTO `auth_menu` VALUES (26, 25, 'dishDiscont', '菜品折扣', 'sms-dscount', 0);
INSERT INTO `auth_menu` VALUES (27, 25, 'dishRecommend', '菜品推荐', 'sms-hot', 0);
INSERT INTO `auth_menu` VALUES (28, 25, 'dishNew', '新品推荐', 'sms-new', 0);
INSERT INTO `auth_menu` VALUES (29, 0, 'ums', '权限', 'ums', 0);
INSERT INTO `auth_menu` VALUES (30, 29, 'admin', '用户列表', 'ums-admin', 0);
INSERT INTO `auth_menu` VALUES (31, 29, 'role', '角色列表', 'ums-role', 0);
INSERT INTO `auth_menu` VALUES (32, 29, 'allocMenu', '分配菜单', '', 1);

-- ----------------------------
-- Table structure for auth_menu_api_relation
-- ----------------------------
DROP TABLE IF EXISTS `auth_menu_api_relation`;
CREATE TABLE `auth_menu_api_relation`  (
  `menu_id` int NOT NULL,
  `api_id` int NOT NULL,
  PRIMARY KEY (`menu_id`, `api_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_menu_api_relation
-- ----------------------------
INSERT INTO `auth_menu_api_relation` VALUES (1, 1);
INSERT INTO `auth_menu_api_relation` VALUES (1, 57);
INSERT INTO `auth_menu_api_relation` VALUES (3, 2);
INSERT INTO `auth_menu_api_relation` VALUES (3, 3);
INSERT INTO `auth_menu_api_relation` VALUES (3, 4);
INSERT INTO `auth_menu_api_relation` VALUES (3, 5);
INSERT INTO `auth_menu_api_relation` VALUES (3, 6);
INSERT INTO `auth_menu_api_relation` VALUES (3, 7);
INSERT INTO `auth_menu_api_relation` VALUES (4, 8);
INSERT INTO `auth_menu_api_relation` VALUES (4, 9);
INSERT INTO `auth_menu_api_relation` VALUES (4, 10);
INSERT INTO `auth_menu_api_relation` VALUES (4, 11);
INSERT INTO `auth_menu_api_relation` VALUES (4, 12);
INSERT INTO `auth_menu_api_relation` VALUES (5, 8);
INSERT INTO `auth_menu_api_relation` VALUES (5, 9);
INSERT INTO `auth_menu_api_relation` VALUES (5, 34);
INSERT INTO `auth_menu_api_relation` VALUES (6, 14);
INSERT INTO `auth_menu_api_relation` VALUES (6, 15);
INSERT INTO `auth_menu_api_relation` VALUES (6, 16);
INSERT INTO `auth_menu_api_relation` VALUES (6, 17);
INSERT INTO `auth_menu_api_relation` VALUES (6, 21);
INSERT INTO `auth_menu_api_relation` VALUES (7, 2);
INSERT INTO `auth_menu_api_relation` VALUES (7, 13);
INSERT INTO `auth_menu_api_relation` VALUES (8, 2);
INSERT INTO `auth_menu_api_relation` VALUES (8, 18);
INSERT INTO `auth_menu_api_relation` VALUES (8, 19);
INSERT INTO `auth_menu_api_relation` VALUES (8, 20);
INSERT INTO `auth_menu_api_relation` VALUES (10, 22);
INSERT INTO `auth_menu_api_relation` VALUES (10, 50);
INSERT INTO `auth_menu_api_relation` VALUES (10, 51);
INSERT INTO `auth_menu_api_relation` VALUES (10, 52);
INSERT INTO `auth_menu_api_relation` VALUES (10, 53);
INSERT INTO `auth_menu_api_relation` VALUES (10, 54);
INSERT INTO `auth_menu_api_relation` VALUES (10, 55);
INSERT INTO `auth_menu_api_relation` VALUES (11, 22);
INSERT INTO `auth_menu_api_relation` VALUES (11, 50);
INSERT INTO `auth_menu_api_relation` VALUES (11, 51);
INSERT INTO `auth_menu_api_relation` VALUES (11, 52);
INSERT INTO `auth_menu_api_relation` VALUES (11, 54);
INSERT INTO `auth_menu_api_relation` VALUES (11, 55);
INSERT INTO `auth_menu_api_relation` VALUES (12, 22);
INSERT INTO `auth_menu_api_relation` VALUES (12, 50);
INSERT INTO `auth_menu_api_relation` VALUES (12, 51);
INSERT INTO `auth_menu_api_relation` VALUES (12, 52);
INSERT INTO `auth_menu_api_relation` VALUES (12, 53);
INSERT INTO `auth_menu_api_relation` VALUES (12, 54);
INSERT INTO `auth_menu_api_relation` VALUES (12, 55);
INSERT INTO `auth_menu_api_relation` VALUES (13, 22);
INSERT INTO `auth_menu_api_relation` VALUES (13, 50);
INSERT INTO `auth_menu_api_relation` VALUES (13, 51);
INSERT INTO `auth_menu_api_relation` VALUES (13, 52);
INSERT INTO `auth_menu_api_relation` VALUES (13, 54);
INSERT INTO `auth_menu_api_relation` VALUES (13, 55);
INSERT INTO `auth_menu_api_relation` VALUES (14, 22);
INSERT INTO `auth_menu_api_relation` VALUES (14, 50);
INSERT INTO `auth_menu_api_relation` VALUES (14, 51);
INSERT INTO `auth_menu_api_relation` VALUES (14, 52);
INSERT INTO `auth_menu_api_relation` VALUES (14, 54);
INSERT INTO `auth_menu_api_relation` VALUES (14, 55);
INSERT INTO `auth_menu_api_relation` VALUES (15, 22);
INSERT INTO `auth_menu_api_relation` VALUES (15, 50);
INSERT INTO `auth_menu_api_relation` VALUES (15, 51);
INSERT INTO `auth_menu_api_relation` VALUES (15, 52);
INSERT INTO `auth_menu_api_relation` VALUES (15, 54);
INSERT INTO `auth_menu_api_relation` VALUES (15, 55);
INSERT INTO `auth_menu_api_relation` VALUES (16, 22);
INSERT INTO `auth_menu_api_relation` VALUES (16, 50);
INSERT INTO `auth_menu_api_relation` VALUES (16, 51);
INSERT INTO `auth_menu_api_relation` VALUES (16, 52);
INSERT INTO `auth_menu_api_relation` VALUES (16, 54);
INSERT INTO `auth_menu_api_relation` VALUES (16, 55);
INSERT INTO `auth_menu_api_relation` VALUES (17, 22);
INSERT INTO `auth_menu_api_relation` VALUES (17, 50);
INSERT INTO `auth_menu_api_relation` VALUES (17, 51);
INSERT INTO `auth_menu_api_relation` VALUES (17, 52);
INSERT INTO `auth_menu_api_relation` VALUES (17, 54);
INSERT INTO `auth_menu_api_relation` VALUES (17, 55);
INSERT INTO `auth_menu_api_relation` VALUES (19, 56);
INSERT INTO `auth_menu_api_relation` VALUES (22, 22);
INSERT INTO `auth_menu_api_relation` VALUES (22, 25);
INSERT INTO `auth_menu_api_relation` VALUES (22, 26);
INSERT INTO `auth_menu_api_relation` VALUES (22, 27);
INSERT INTO `auth_menu_api_relation` VALUES (23, 23);
INSERT INTO `auth_menu_api_relation` VALUES (23, 28);
INSERT INTO `auth_menu_api_relation` VALUES (23, 34);
INSERT INTO `auth_menu_api_relation` VALUES (24, 24);
INSERT INTO `auth_menu_api_relation` VALUES (26, 2);
INSERT INTO `auth_menu_api_relation` VALUES (26, 29);
INSERT INTO `auth_menu_api_relation` VALUES (26, 31);
INSERT INTO `auth_menu_api_relation` VALUES (26, 32);
INSERT INTO `auth_menu_api_relation` VALUES (26, 33);
INSERT INTO `auth_menu_api_relation` VALUES (27, 2);
INSERT INTO `auth_menu_api_relation` VALUES (27, 5);
INSERT INTO `auth_menu_api_relation` VALUES (28, 2);
INSERT INTO `auth_menu_api_relation` VALUES (28, 6);
INSERT INTO `auth_menu_api_relation` VALUES (30, 35);
INSERT INTO `auth_menu_api_relation` VALUES (30, 36);
INSERT INTO `auth_menu_api_relation` VALUES (30, 37);
INSERT INTO `auth_menu_api_relation` VALUES (30, 38);
INSERT INTO `auth_menu_api_relation` VALUES (30, 39);
INSERT INTO `auth_menu_api_relation` VALUES (30, 40);
INSERT INTO `auth_menu_api_relation` VALUES (30, 41);
INSERT INTO `auth_menu_api_relation` VALUES (30, 48);
INSERT INTO `auth_menu_api_relation` VALUES (31, 40);
INSERT INTO `auth_menu_api_relation` VALUES (31, 41);
INSERT INTO `auth_menu_api_relation` VALUES (31, 42);
INSERT INTO `auth_menu_api_relation` VALUES (31, 43);
INSERT INTO `auth_menu_api_relation` VALUES (31, 44);
INSERT INTO `auth_menu_api_relation` VALUES (31, 48);
INSERT INTO `auth_menu_api_relation` VALUES (31, 49);
INSERT INTO `auth_menu_api_relation` VALUES (32, 22);
INSERT INTO `auth_menu_api_relation` VALUES (32, 45);
INSERT INTO `auth_menu_api_relation` VALUES (32, 46);
INSERT INTO `auth_menu_api_relation` VALUES (32, 47);

-- ----------------------------
-- Table structure for auth_role
-- ----------------------------
DROP TABLE IF EXISTS `auth_role`;
CREATE TABLE `auth_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_name_index`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_role
-- ----------------------------
INSERT INTO `auth_role` VALUES (1, '超级管理员', '什么都能做，无所不能');
INSERT INTO `auth_role` VALUES (5, '权限管理员', '强大的管理员，掌控权限的管理员');
INSERT INTO `auth_role` VALUES (6, '中国店订单管理员', '管理中国店的订单');

-- ----------------------------
-- Table structure for auth_role_menu_relation
-- ----------------------------
DROP TABLE IF EXISTS `auth_role_menu_relation`;
CREATE TABLE `auth_role_menu_relation`  (
  `role_id` int NOT NULL,
  `menu_id` int NOT NULL,
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE,
  INDEX `xxx2`(`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_role_menu_relation
-- ----------------------------
INSERT INTO `auth_role_menu_relation` VALUES (1, 1);
INSERT INTO `auth_role_menu_relation` VALUES (5, 1);
INSERT INTO `auth_role_menu_relation` VALUES (1, 2);
INSERT INTO `auth_role_menu_relation` VALUES (3, 2);
INSERT INTO `auth_role_menu_relation` VALUES (1, 3);
INSERT INTO `auth_role_menu_relation` VALUES (3, 3);
INSERT INTO `auth_role_menu_relation` VALUES (1, 4);
INSERT INTO `auth_role_menu_relation` VALUES (3, 4);
INSERT INTO `auth_role_menu_relation` VALUES (1, 5);
INSERT INTO `auth_role_menu_relation` VALUES (3, 5);
INSERT INTO `auth_role_menu_relation` VALUES (1, 6);
INSERT INTO `auth_role_menu_relation` VALUES (3, 6);
INSERT INTO `auth_role_menu_relation` VALUES (1, 7);
INSERT INTO `auth_role_menu_relation` VALUES (3, 7);
INSERT INTO `auth_role_menu_relation` VALUES (1, 8);
INSERT INTO `auth_role_menu_relation` VALUES (3, 8);
INSERT INTO `auth_role_menu_relation` VALUES (1, 9);
INSERT INTO `auth_role_menu_relation` VALUES (4, 9);
INSERT INTO `auth_role_menu_relation` VALUES (6, 9);
INSERT INTO `auth_role_menu_relation` VALUES (1, 10);
INSERT INTO `auth_role_menu_relation` VALUES (4, 10);
INSERT INTO `auth_role_menu_relation` VALUES (6, 10);
INSERT INTO `auth_role_menu_relation` VALUES (1, 11);
INSERT INTO `auth_role_menu_relation` VALUES (4, 11);
INSERT INTO `auth_role_menu_relation` VALUES (6, 11);
INSERT INTO `auth_role_menu_relation` VALUES (1, 12);
INSERT INTO `auth_role_menu_relation` VALUES (4, 12);
INSERT INTO `auth_role_menu_relation` VALUES (6, 12);
INSERT INTO `auth_role_menu_relation` VALUES (1, 13);
INSERT INTO `auth_role_menu_relation` VALUES (4, 13);
INSERT INTO `auth_role_menu_relation` VALUES (6, 13);
INSERT INTO `auth_role_menu_relation` VALUES (1, 14);
INSERT INTO `auth_role_menu_relation` VALUES (4, 14);
INSERT INTO `auth_role_menu_relation` VALUES (6, 14);
INSERT INTO `auth_role_menu_relation` VALUES (1, 15);
INSERT INTO `auth_role_menu_relation` VALUES (4, 15);
INSERT INTO `auth_role_menu_relation` VALUES (6, 15);
INSERT INTO `auth_role_menu_relation` VALUES (1, 16);
INSERT INTO `auth_role_menu_relation` VALUES (4, 16);
INSERT INTO `auth_role_menu_relation` VALUES (6, 16);
INSERT INTO `auth_role_menu_relation` VALUES (1, 17);
INSERT INTO `auth_role_menu_relation` VALUES (4, 17);
INSERT INTO `auth_role_menu_relation` VALUES (6, 17);
INSERT INTO `auth_role_menu_relation` VALUES (1, 18);
INSERT INTO `auth_role_menu_relation` VALUES (4, 18);
INSERT INTO `auth_role_menu_relation` VALUES (6, 18);
INSERT INTO `auth_role_menu_relation` VALUES (1, 19);
INSERT INTO `auth_role_menu_relation` VALUES (4, 19);
INSERT INTO `auth_role_menu_relation` VALUES (6, 19);
INSERT INTO `auth_role_menu_relation` VALUES (1, 20);
INSERT INTO `auth_role_menu_relation` VALUES (4, 20);
INSERT INTO `auth_role_menu_relation` VALUES (6, 20);
INSERT INTO `auth_role_menu_relation` VALUES (1, 21);
INSERT INTO `auth_role_menu_relation` VALUES (1, 22);
INSERT INTO `auth_role_menu_relation` VALUES (1, 23);
INSERT INTO `auth_role_menu_relation` VALUES (1, 24);
INSERT INTO `auth_role_menu_relation` VALUES (1, 25);
INSERT INTO `auth_role_menu_relation` VALUES (1, 26);
INSERT INTO `auth_role_menu_relation` VALUES (1, 27);
INSERT INTO `auth_role_menu_relation` VALUES (1, 28);
INSERT INTO `auth_role_menu_relation` VALUES (1, 29);
INSERT INTO `auth_role_menu_relation` VALUES (5, 29);
INSERT INTO `auth_role_menu_relation` VALUES (1, 30);
INSERT INTO `auth_role_menu_relation` VALUES (5, 30);
INSERT INTO `auth_role_menu_relation` VALUES (1, 31);
INSERT INTO `auth_role_menu_relation` VALUES (5, 31);
INSERT INTO `auth_role_menu_relation` VALUES (1, 32);
INSERT INTO `auth_role_menu_relation` VALUES (5, 32);

-- ----------------------------
-- Table structure for auth_role_store_relation
-- ----------------------------
DROP TABLE IF EXISTS `auth_role_store_relation`;
CREATE TABLE `auth_role_store_relation`  (
  `role_id` int NOT NULL,
  `store_id` int NOT NULL,
  PRIMARY KEY (`role_id`, `store_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_role_store_relation
-- ----------------------------
INSERT INTO `auth_role_store_relation` VALUES (1, 1);
INSERT INTO `auth_role_store_relation` VALUES (1, 2);
INSERT INTO `auth_role_store_relation` VALUES (4, 1);
INSERT INTO `auth_role_store_relation` VALUES (6, 1);

-- ----------------------------
-- Table structure for dms_classification
-- ----------------------------
DROP TABLE IF EXISTS `dms_classification`;
CREATE TABLE `dms_classification`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '1, 2默认为我的收藏和我的待选, 该字段不可被菜品表中引用',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `icon` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100004 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_classification
-- ----------------------------
INSERT INTO `dms_classification` VALUES (1, '我的收藏', 'https://tse4-mm.cn.bing.net/th/id/OIP-C.0WftkWURoF3pVG6lQjgsHwHaHa?https://s1.aigei.com/src/img/png/9a/9aa6f2d02d65422480512c2d12bf604a.png?imageMogr2/auto-orient/thumbnail/!237x237r/gravity/Center/crop/237x237/quality/85/&e=1735488000&token=P7S2Xpzfz11vAkASLTkfHN7Fw-oOZBecqeJaxypL:UxZeAuNHZv-https://img.icons8.com/dusk/2x/adobe-acrobat.png');
INSERT INTO `dms_classification` VALUES (2, '我的待选', 'https://tse4-mm.cn.bing.net/th/id/OIP-C.0WftkWURoF3pVG6lQjgsHwHaHa?pid=ImgDet&rs=1');
INSERT INTO `dms_classification` VALUES (3, '热销之王', 'https://tse1-mm.cn.bing.net/th?id=OIP-https://img.icons8.com/office/2x/collectibles.png');
INSERT INTO `dms_classification` VALUES (4, '今日爆款', 'https://tse1-mm.cn.bing.net/th?id=OIP-https://img.icons8.com/office/2x/logo.png');
INSERT INTO `dms_classification` VALUES (6, '每日必点', 'https://tse1-mm.cn.bing.net/th?id=OIP-https://img.icons8.com/office/2x/butting-in.png');
INSERT INTO `dms_classification` VALUES (7, '下酒菜', 'https://img.icons8.com/office/2x/meeting-room.png');
INSERT INTO `dms_classification` VALUES (8, '主食', 'https://img.icons8.com/office/2x/video-conference.png');
INSERT INTO `dms_classification` VALUES (9, '蔬菜', 'https://img.icons8.com/office/2x/jake.png');
INSERT INTO `dms_classification` VALUES (10, '荤菜', 'https://img.icons8.com/office/2x/ninja-turtle.png');
INSERT INTO `dms_classification` VALUES (11, '汤', 'https://img.icons8.com/office/2x/sons-of-anarchy.png');
INSERT INTO `dms_classification` VALUES (12, '凉拌', 'https://img.icons8.com/office/2x/the-flash-head.png');
INSERT INTO `dms_classification` VALUES (100000, '单人套餐', NULL);
INSERT INTO `dms_classification` VALUES (100001, '双人套餐', NULL);
INSERT INTO `dms_classification` VALUES (100002, '3~4人套餐', NULL);
INSERT INTO `dms_classification` VALUES (100003, '多人套餐', NULL);
INSERT INTO `dms_classification` VALUES (100004, '十人全家桶套餐', NULL);

-- ----------------------------
-- Table structure for dms_combo
-- ----------------------------
DROP TABLE IF EXISTS `dms_combo`;
CREATE TABLE `dms_combo`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '从 100000 开始计数, 严格区分菜品',
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `price` float NOT NULL,
  `sale` int NULL DEFAULT 0,
  `like_num` int NULL DEFAULT 0,
  `publish_status` tinyint(1) NULL DEFAULT NULL COMMENT '上架状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `combo_name_index2`(`name`) USING BTREE,
  FULLTEXT INDEX `combo_name_index`(`name`) WITH PARSER `ngram`
) ENGINE = InnoDB AUTO_INCREMENT = 100006 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_combo
-- ----------------------------
INSERT INTO `dms_combo` VALUES (100000, '单人套餐B', 58, 0, 23, 1);
INSERT INTO `dms_combo` VALUES (100001, '单人套餐A', 8, 0, 1, 1);
INSERT INTO `dms_combo` VALUES (100006, '双人套餐A', 88, 0, 0, 0);

-- ----------------------------
-- Table structure for dms_combo_dish
-- ----------------------------
DROP TABLE IF EXISTS `dms_combo_dish`;
CREATE TABLE `dms_combo_dish`  (
  `combo_id` int NOT NULL,
  `dish_id` int NOT NULL,
  `dish_num` int NOT NULL DEFAULT 0,
  `is_add` tinyint(1) NOT NULL DEFAULT 0 COMMENT '这个字段表示菜品是否是新加的',
  INDEX `combo_id`(`combo_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_combo_dish
-- ----------------------------
INSERT INTO `dms_combo_dish` VALUES (100000, 8, 2, 0);
INSERT INTO `dms_combo_dish` VALUES (100006, 8, 2, 0);
INSERT INTO `dms_combo_dish` VALUES (100006, 6, 1, 0);
INSERT INTO `dms_combo_dish` VALUES (100006, 4, 1, 0);
INSERT INTO `dms_combo_dish` VALUES (100001, 5, 1, 0);

-- ----------------------------
-- Table structure for dms_dish
-- ----------------------------
DROP TABLE IF EXISTS `dms_dish`;
CREATE TABLE `dms_dish`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `price` double NOT NULL,
  `publish_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否上架',
  `recommend_status` tinyint(1) NULL DEFAULT 0 COMMENT '是否菜品推荐,事实上这个字段完全没有,我们在recommend表中存储这些信息,写在这里只是为了增加可读性',
  `new_status` tinyint(1) NULL DEFAULT 0 COMMENT '是否新品推荐',
  `sale` int NOT NULL DEFAULT 0,
  `like_num` int NOT NULL DEFAULT 0,
  `desc` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dish_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `main_ingredient` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主料',
  `ingredient` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配料',
  `make_method` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `weight` double NULL DEFAULT NULL COMMENT '重量',
  `make_time` double NULL DEFAULT NULL COMMENT '制作时间(分)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `dish_name_index1`(`name`) USING BTREE,
  INDEX `sell_num`(`sale`) USING BTREE,
  FULLTEXT INDEX `dish_name_index`(`name`) WITH PARSER `ngram`
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci MAX_ROWS = 100000 ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_dish
-- ----------------------------
INSERT INTO `dms_dish` VALUES (1, '凉拌木耳', 25, 1, 0, 0, 88, 77, '治疗感冒很有用哦!', 'https://tse1-mm.cn.bing.net/th/id/R-C.0df05ec81c8c21dfd8508e3f493becdd?rik=uOc8Nm9hIFmEDQ&riu=http%3a%2f%2fpic9.nipic.com%2f20100823%2f5539726_120433029033_2.jpg&ehk=yemlk22y9Ye%2f1k6umy7MKkYGRsZ6pBiunDaYh10qCuA%3d&risl=&pid=ImgRaw&r=0', '主料就是核心价值观', '暂无辅料', '我怎么知道制作方法', 5, 25);
INSERT INTO `dms_dish` VALUES (4, '凉拌猪脑', 38, 1, 1, 1, 88, 88, '这道菜口感丰富，层次鲜明，搭配的味道非常淡香，吃在嘴里有一种幸福的感觉', 'https://tse1-mm.cn.bing.net/th/id/R-C.471d815cb1a3d75f7dd044301161cb3d?rik=eAbMJ3yXF83EWQ&riu=http%3a%2f%2fseopic.699pic.com%2fphoto%2f50042%2f5731.jpg_wh1200.jpg&ehk=qa1Ew7mPEIhg71JlNRq7hn7OEKalJU1mAfb9RkjdNas%3d&risl=&pid=ImgRaw&r=0', '主料就是核心价值观', '暂无辅料', '我怎么知道制作方法', 5, 5);
INSERT INTO `dms_dish` VALUES (5, '好吃蒸包', 8, 1, 1, 1, 88, 90, '纤手搓来玉色匀，碧油煎出嫩黄深。夜来春睡知轻重，压扁佳人缠臂金。', 'https://tse1-mm.cn.bing.net/th/id/R-C.eca12549a3f927a4f706e1ea2900f566?rik=xtR3QlOsycKJbA&riu=http%3a%2f%2fpic6.nipic.com%2f20100413%2f4229075_090019041246_2.jpg&ehk=PTy0r6McbkaceD%2bqPaRXzqFPFl7GjnXWrv5Mr1sO6lU%3d&risl=&pid=ImgRaw&r=0', '主料就是核心价值观', '暂无辅料', '我怎么知道制作方法', 5, 2.5);
INSERT INTO `dms_dish` VALUES (6, '牛肉丸子', 30, 1, 1, 1, 88, 23, '纤小牛肉，个小，皮薄，只要往开水中一捞，就能盛入碗中，吃上一口，好鲜美！', 'https://tse1-mm.cn.bing.net/th/id/R-C.fd4b7ef86cd3ad1d0fc1367f7bee60a4?rik=fIMJs7SYdqS6rA&riu=http%3a%2f%2fpic6.nipic.com%2f20100416%2f4229075_090725069388_2.jpg&ehk=oemcL7ZTrC7oJTT%2bIUur3FkaL1nq5a6%2bniONTr19C2k%3d&risl=&pid=ImgRaw&r=0', '主料就是核心价值观', '暂无辅料', '我怎么知道制作方法', 5, 14);
INSERT INTO `dms_dish` VALUES (7, '红烧鸡块', 32, 1, 1, 1, 88, 111, '初游唐安饭薏米，炊成不减雕胡美。大如苋实白如玉，滑欲流匙香满屋。', 'https://tse1-mm.cn.bing.net/th/id/R-C.3bfbe2ea2de094d3f5fcc0d3ed9c0bb9?rik=hMV8vGpRe2Baxw&riu=http%3a%2f%2fpic4.nipic.com%2f20090728%2f78273_095123081_2.jpg&ehk=W3pJEGxCJdzOz1HgxTGcZFr6MJnd7EDJt%2b%2bhz4OtBIs%3d&risl=&pid=ImgRaw&r=0', '主料就是核心价值观', '暂无辅料', '我怎么知道制作方法', 5, 2);
INSERT INTO `dms_dish` VALUES (8, '蛋羹', 10, 0, 1, 1, 88, 231, '饭前甜点，真的香！！', 'https://img.zcool.cn/community/0136865b5ac064a801215c8f5f44b0.jpg@1280w_1l_2o_100sh.jpg', '主料就是核心价值观', '暂无辅料', '我怎么知道制作方法', 5, 2.5);
INSERT INTO `dms_dish` VALUES (17, '剁椒鱼头(辣)', 48, 1, 0, 1, 88, 23, '够辣够开胃，本店最受欢迎的菜品之一', 'https://tse1-mm.cn.bing.net/th/id/R-C.53f50a391551472acbc9f5de96630a1b?rik=GmvuyXkBuinqcQ&riu=http%3a%2f%2fpic46.nipic.com%2f20140815%2f2221083_170059246493_2.jpg&ehk=ReNG7VdPCP0oqE27ooh3KlhvhqdMeG%2bjQVTm7HT4CWI%3d&risl=&pid=ImgRaw&r=0', '主料就是核心价值观', '暂无辅料', '我怎么知道制作方法', 5, 25);
INSERT INTO `dms_dish` VALUES (18, '肉末茄子', 15.2, 0, 1, 1, 87, 0, '很好吃的，快来吃吧', 'https://img.zcool.cn/community/01b70e5b5ac3cca801215c8f1ae499.jpg@1280w_1l_2o_100sh.jpg', NULL, NULL, '爆炒', 28, 8);
INSERT INTO `dms_dish` VALUES (19, '123456', 12, 0, 1, 1, 87, 0, '1312312', '23', '32', '13', '31232', 0, 21);
INSERT INTO `dms_dish` VALUES (21, 'ttt', 3, 0, 0, 0, 87, 0, 'ew', 'ttt', 'ew', 'wer', 'wer', 3, 3);

-- ----------------------------
-- Table structure for dms_dish_classification
-- ----------------------------
DROP TABLE IF EXISTS `dms_dish_classification`;
CREATE TABLE `dms_dish_classification`  (
  `dish_id` int NOT NULL,
  `classification_id` int NOT NULL,
  PRIMARY KEY (`dish_id`, `classification_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_dish_classification
-- ----------------------------
INSERT INTO `dms_dish_classification` VALUES (13, 3);
INSERT INTO `dms_dish_classification` VALUES (13, 4);
INSERT INTO `dms_dish_classification` VALUES (13, 6);
INSERT INTO `dms_dish_classification` VALUES (14, 3);
INSERT INTO `dms_dish_classification` VALUES (14, 4);
INSERT INTO `dms_dish_classification` VALUES (14, 6);
INSERT INTO `dms_dish_classification` VALUES (15, 3);
INSERT INTO `dms_dish_classification` VALUES (17, 3);
INSERT INTO `dms_dish_classification` VALUES (18, 3);
INSERT INTO `dms_dish_classification` VALUES (18, 4);
INSERT INTO `dms_dish_classification` VALUES (18, 6);
INSERT INTO `dms_dish_classification` VALUES (21, 3);
INSERT INTO `dms_dish_classification` VALUES (21, 4);
INSERT INTO `dms_dish_classification` VALUES (21, 6);
INSERT INTO `dms_dish_classification` VALUES (100000, 100001);
INSERT INTO `dms_dish_classification` VALUES (100000, 100002);
INSERT INTO `dms_dish_classification` VALUES (100006, 100001);
INSERT INTO `dms_dish_classification` VALUES (100006, 100002);

-- ----------------------------
-- Table structure for dms_dish_sale
-- ----------------------------
DROP TABLE IF EXISTS `dms_dish_sale`;
CREATE TABLE `dms_dish_sale`  (
  `sell_date` datetime NOT NULL,
  `store_id` int NOT NULL,
  `sale` int NULL DEFAULT NULL,
  PRIMARY KEY (`sell_date`, `store_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_dish_sale
-- ----------------------------

-- ----------------------------
-- Table structure for dms_dish_stock
-- ----------------------------
DROP TABLE IF EXISTS `dms_dish_stock`;
CREATE TABLE `dms_dish_stock`  (
  `store_id` int NOT NULL,
  `dish_id` int NOT NULL,
  `stock` int NOT NULL,
  PRIMARY KEY (`store_id`, `dish_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_dish_stock
-- ----------------------------
INSERT INTO `dms_dish_stock` VALUES (1, 1, 60);
INSERT INTO `dms_dish_stock` VALUES (1, 2, 16);
INSERT INTO `dms_dish_stock` VALUES (1, 3, 33);
INSERT INTO `dms_dish_stock` VALUES (1, 4, 1);
INSERT INTO `dms_dish_stock` VALUES (1, 5, 18);
INSERT INTO `dms_dish_stock` VALUES (1, 6, 33);
INSERT INTO `dms_dish_stock` VALUES (1, 7, 99);
INSERT INTO `dms_dish_stock` VALUES (1, 8, 7);
INSERT INTO `dms_dish_stock` VALUES (1, 17, 21);
INSERT INTO `dms_dish_stock` VALUES (2, 1, 25);
INSERT INTO `dms_dish_stock` VALUES (2, 2, 8);
INSERT INTO `dms_dish_stock` VALUES (2, 3, 11);
INSERT INTO `dms_dish_stock` VALUES (2, 4, 25);
INSERT INTO `dms_dish_stock` VALUES (2, 5, 24);
INSERT INTO `dms_dish_stock` VALUES (2, 6, 28);
INSERT INTO `dms_dish_stock` VALUES (2, 7, 12);
INSERT INTO `dms_dish_stock` VALUES (2, 8, 19);
INSERT INTO `dms_dish_stock` VALUES (2, 17, 31);

-- ----------------------------
-- Table structure for dms_dish_tag
-- ----------------------------
DROP TABLE IF EXISTS `dms_dish_tag`;
CREATE TABLE `dms_dish_tag`  (
  `dish_id` int NOT NULL,
  `tag` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  INDEX `dish_id`(`dish_id`) USING BTREE,
  FULLTEXT INDEX `tag_index`(`tag`) WITH PARSER `ngram`
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_dish_tag
-- ----------------------------
INSERT INTO `dms_dish_tag` VALUES (1, '治疗感冒');
INSERT INTO `dms_dish_tag` VALUES (1, '超级好吃');
INSERT INTO `dms_dish_tag` VALUES (6, '饭后美食');
INSERT INTO `dms_dish_tag` VALUES (4, '下酒菜');
INSERT INTO `dms_dish_tag` VALUES (2, '长个子、好吃');
INSERT INTO `dms_dish_tag` VALUES (1, '治疗感冒');
INSERT INTO `dms_dish_tag` VALUES (1, '超级好吃');
INSERT INTO `dms_dish_tag` VALUES (6, '饭后美食');
INSERT INTO `dms_dish_tag` VALUES (4, '下酒菜');
INSERT INTO `dms_dish_tag` VALUES (2, '长个子、好吃');
INSERT INTO `dms_dish_tag` VALUES (13, '');
INSERT INTO `dms_dish_tag` VALUES (14, 'A B C');
INSERT INTO `dms_dish_tag` VALUES (15, '1');
INSERT INTO `dms_dish_tag` VALUES (16, '123');
INSERT INTO `dms_dish_tag` VALUES (17, '长个子、好吃 长个子、好吃 ');
INSERT INTO `dms_dish_tag` VALUES (18, '好吃 \n养生');
INSERT INTO `dms_dish_tag` VALUES (19, '12');
INSERT INTO `dms_dish_tag` VALUES (19, '12 ');
INSERT INTO `dms_dish_tag` VALUES (19, '12 12  ');
INSERT INTO `dms_dish_tag` VALUES (18, '');
INSERT INTO `dms_dish_tag` VALUES (18, '');
INSERT INTO `dms_dish_tag` VALUES (18, '好吃 \n养生   ');
INSERT INTO `dms_dish_tag` VALUES (18, '好吃 \n养生   好吃 \n养生    ');
INSERT INTO `dms_dish_tag` VALUES (100000, '  ');
INSERT INTO `dms_dish_tag` VALUES (100006, 'A');
INSERT INTO `dms_dish_tag` VALUES (100006, 'B');
INSERT INTO `dms_dish_tag` VALUES (100006, 'C');
INSERT INTO `dms_dish_tag` VALUES (100006, 'D');
INSERT INTO `dms_dish_tag` VALUES (21, '');
INSERT INTO `dms_dish_tag` VALUES (100001, '没有烦恼，不用思考');
INSERT INTO `dms_dish_tag` VALUES (100001, '没有烦恼，不用思考');

-- ----------------------------
-- Table structure for dms_recommend_dish
-- ----------------------------
DROP TABLE IF EXISTS `dms_recommend_dish`;
CREATE TABLE `dms_recommend_dish`  (
  `dish_id` int NOT NULL,
  `stars` double NULL DEFAULT NULL COMMENT '推荐指数',
  `reason` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '推荐理由',
  PRIMARY KEY (`dish_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_recommend_dish
-- ----------------------------
INSERT INTO `dms_recommend_dish` VALUES (2, 4.5, '很酷');

-- ----------------------------
-- Table structure for mms_coupon
-- ----------------------------
DROP TABLE IF EXISTS `mms_coupon`;
CREATE TABLE `mms_coupon`  (
  `id` int NOT NULL COMMENT '优惠券表, 暂不支持此功能',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mms_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for mms_discount
-- ----------------------------
DROP TABLE IF EXISTS `mms_discount`;
CREATE TABLE `mms_discount`  (
  `dish_id` int NOT NULL,
  `type` tinyint NOT NULL DEFAULT 0 COMMENT '0 表示无优惠, 1 表示折扣, 2 表示立减',
  `val` int NOT NULL DEFAULT 0 COMMENT '取决去type, val表示立减优惠或者折扣力度',
  `count` int NOT NULL DEFAULT 0 COMMENT '用户一天(或一周)最多可享受的次数',
  `unit` tinyint NULL DEFAULT 0 COMMENT '这个字段指示count字段的单位是天或周或月, 默认的0代表天,这个字段留以以后扩展使用',
  PRIMARY KEY (`dish_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mms_discount
-- ----------------------------
INSERT INTO `mms_discount` VALUES (0, 1, 4, -1, 0);
INSERT INTO `mms_discount` VALUES (1, 1, 8, 1, 0);
INSERT INTO `mms_discount` VALUES (2, 1, 7, 2, 0);
INSERT INTO `mms_discount` VALUES (3, 2, 1, 5, 0);
INSERT INTO `mms_discount` VALUES (4, 1, 5, 1, 0);
INSERT INTO `mms_discount` VALUES (5, 2, 2, 2, 0);
INSERT INTO `mms_discount` VALUES (7, 1, 5, 1, 0);
INSERT INTO `mms_discount` VALUES (21, 0, 0, 0, 0);
INSERT INTO `mms_discount` VALUES (100000, 2, 5, 0, 0);
INSERT INTO `mms_discount` VALUES (100001, 1, 9, -1, 0);
INSERT INTO `mms_discount` VALUES (100006, 1, 7, 0, 0);

-- ----------------------------
-- Table structure for oms_cancel_order_apply
-- ----------------------------
DROP TABLE IF EXISTS `oms_cancel_order_apply`;
CREATE TABLE `oms_cancel_order_apply`  (
  `order_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `reason` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `old_order_type` int NULL DEFAULT NULL COMMENT '拒绝前订单的状态',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_cancel_order_apply
-- ----------------------------
INSERT INTO `oms_cancel_order_apply` VALUES ('20211112195853114oxlOilyx', 'olDyO5OQmI2oXBR_8oNlF7xAeicc', '10086', '你管得着？', 1);
INSERT INTO `oms_cancel_order_apply` VALUES ('20211129202856545QBBFylo2', 'olDyO5OQmI2oXBR_8oNlF7xAeicc', '11111111111', '1', 3);
INSERT INTO `oms_cancel_order_apply` VALUES ('20211130193725371_AD', 'olDyO5OQmI2oXBR_8oNlF7xAeicc', '1234567890`', '12', 2);
INSERT INTO `oms_cancel_order_apply` VALUES ('20211216185345895VV1', 'olDyO5OQmI2oXBR_8oNlF7xAeicc', '12345678901', '不想要了', 2);
INSERT INTO `oms_cancel_order_apply` VALUES ('2021121619023569Tdu', 'olDyO5OQmI2oXBR_8oNlF7xAeicc', '13726541232', '避孕药了', 0);
INSERT INTO `oms_cancel_order_apply` VALUES ('20211216194407191iAL', 'olDyO5OQmI2oXBR_8oNlF7xAeicc', '12365478952', '1', 2);

-- ----------------------------
-- Table structure for oms_fetch_meal_code
-- ----------------------------
DROP TABLE IF EXISTS `oms_fetch_meal_code`;
CREATE TABLE `oms_fetch_meal_code`  (
  `order_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_fetch_meal_code
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order
-- ----------------------------
DROP TABLE IF EXISTS `oms_order`;
CREATE TABLE `oms_order`  (
  `id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `order_type` tinyint NOT NULL,
  `store_id` int NOT NULL,
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pay_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `consume_type` tinyint NOT NULL,
  `table` tinyint NOT NULL DEFAULT -1,
  `create_time` datetime NOT NULL,
  `final_time` datetime NULL DEFAULT NULL,
  `original_price` double NOT NULL,
  `shop_discount` double NULL DEFAULT 0,
  `coupon_discount` double NULL DEFAULT 0,
  `address_id` int NULL DEFAULT NULL,
  `expected_time` datetime NULL DEFAULT NULL COMMENT '预约时间',
  `taste` tinyint NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `other_fee` double NULL DEFAULT 0,
  `delete_flag` int NOT NULL DEFAULT 0 COMMENT '0代表未被删除,1代表客户删除,2代表管理员删除,3代表都删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order_dish
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_dish`;
CREATE TABLE `oms_order_dish`  (
  `order_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dish_id` int NOT NULL COMMENT '存储id是为了防止用户继续加餐的情况, 此时需要菜品ID',
  `dish_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '冗余的存储这些信息是因为订单是过时的, 菜品信息可能会被改变',
  `dish_num` int NOT NULL,
  `dish_price` double NOT NULL,
  `is_add` int NULL DEFAULT NULL COMMENT '是否为继续加餐添加的才',
  INDEX `form_id`(`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_dish
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order_margin
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_margin`;
CREATE TABLE `oms_order_margin`  (
  `order_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `margin` double NULL DEFAULT NULL COMMENT '保证金',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_margin
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order_pay
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_pay`;
CREATE TABLE `oms_order_pay`  (
  `order_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pay_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_pay
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order_payment_status
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_payment_status`;
CREATE TABLE `oms_order_payment_status`  (
  `order_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `payment_status` int NULL DEFAULT NULL COMMENT '表示订单是否可支付',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_payment_status
-- ----------------------------

-- ----------------------------
-- Table structure for sms_reserve_table
-- ----------------------------
DROP TABLE IF EXISTS `sms_reserve_table`;
CREATE TABLE `sms_reserve_table`  (
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `store_id` int NOT NULL,
  `table` int NOT NULL DEFAULT 0,
  `time` datetime NOT NULL,
  PRIMARY KEY (`user_id`, `store_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_reserve_table
-- ----------------------------

-- ----------------------------
-- Table structure for sms_store
-- ----------------------------
DROP TABLE IF EXISTS `sms_store`;
CREATE TABLE `sms_store`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `support_takeout` tinyint(1) NOT NULL DEFAULT 0,
  `contact_phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `working` tinyint(1) NOT NULL COMMENT '是否正在营业',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `store_name_index1`(`name`) USING BTREE,
  FULLTEXT INDEX `store_index`(`name`) WITH PARSER `ngram`
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_store
-- ----------------------------
INSERT INTO `sms_store` VALUES (1, '中国银行店', '江西省南昌市青山湖区上海北路619号', '../../images/PUS.png', '10:00 - 22:00', 0, '10086', 1);
INSERT INTO `sms_store` VALUES (2, '北京店', '北京天安门广场', '../../images/PUS.png', '10:00 - 22:00', 0, '1008611', 1);

-- ----------------------------
-- Table structure for sms_store_table
-- ----------------------------
DROP TABLE IF EXISTS `sms_store_table`;
CREATE TABLE `sms_store_table`  (
  `store_id` int NOT NULL,
  `table` int NOT NULL,
  `occupation_status` int NOT NULL DEFAULT 0,
  `capacity` int NOT NULL COMMENT '桌子最多允许几人吃',
  PRIMARY KEY (`store_id`, `table`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_store_table
-- ----------------------------
INSERT INTO `sms_store_table` VALUES (2, 1, 0, 6);

-- ----------------------------
-- Table structure for ums_address
-- ----------------------------
DROP TABLE IF EXISTS `ums_address`;
CREATE TABLE `ums_address`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_address
-- ----------------------------

-- ----------------------------
-- Table structure for ums_admin
-- ----------------------------
DROP TABLE IF EXISTS `ums_admin`;
CREATE TABLE `ums_admin`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `mail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `last_login_time` datetime NULL DEFAULT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 1,
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `avatar` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `login_status` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name_index`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 128 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_admin
-- ----------------------------

-- ----------------------------
-- Table structure for ums_collected_dish
-- ----------------------------
DROP TABLE IF EXISTS `ums_collected_dish`;
CREATE TABLE `ums_collected_dish`  (
  `user_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  `dish_id` int NOT NULL COMMENT '菜品ID',
  UNIQUE INDEX `user_id`(`user_id`, `dish_id`) USING BTREE,
  INDEX `id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMPRESSION = 'NONE' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_collected_dish
-- ----------------------------

-- ----------------------------
-- Table structure for ums_collected_store
-- ----------------------------
DROP TABLE IF EXISTS `ums_collected_store`;
CREATE TABLE `ums_collected_store`  (
  `user_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `store_id` int NOT NULL,
  UNIQUE INDEX `user_id`(`user_id`, `store_id`) USING BTREE,
  INDEX `id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_collected_store
-- ----------------------------

-- ----------------------------
-- Table structure for ums_favorite_dish
-- ----------------------------
DROP TABLE IF EXISTS `ums_favorite_dish`;
CREATE TABLE `ums_favorite_dish`  (
  `user_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dish_id` int NOT NULL,
  UNIQUE INDEX `user_id`(`user_id`, `dish_id`) USING BTREE,
  INDEX `id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_favorite_dish
-- ----------------------------

-- ----------------------------
-- Table structure for ums_message
-- ----------------------------
DROP TABLE IF EXISTS `ums_message`;
CREATE TABLE `ums_message`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `type` tinyint NOT NULL COMMENT '消息类型, 有系统消息和店内信等',
  `store_id` int NULL DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息类型, 有系统消息和店内信等',
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19815 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_message
-- ----------------------------

-- ----------------------------
-- Table structure for ums_user
-- ----------------------------
DROP TABLE IF EXISTS `ums_user`;
CREATE TABLE `ums_user`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rank` int NOT NULL DEFAULT 0 COMMENT '会员积分',
  `integral` int NOT NULL DEFAULT 0 COMMENT '会员等级',
  `wallet` int NOT NULL DEFAULT 0 COMMENT '会员钱包',
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user
-- ----------------------------

-- ----------------------------
-- Table structure for ums_user_login_log
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_login_log`;
CREATE TABLE `ums_user_login_log`  (
  `user_id` int NOT NULL,
  `last_login_time` datetime NOT NULL,
  `last_login_ip` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for ums_user_msg_num
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_msg_num`;
CREATE TABLE `ums_user_msg_num`  (
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `num` int NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user_msg_num
-- ----------------------------

-- ----------------------------
-- Table structure for ums_user_used_discount_count
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_used_discount_count`;
CREATE TABLE `ums_user_used_discount_count`  (
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dish_id` int NOT NULL,
  `count` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`user_id`, `dish_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user_used_discount_count
-- ----------------------------

-- ----------------------------
-- Table structure for ums_willbuy_dish
-- ----------------------------
DROP TABLE IF EXISTS `ums_willbuy_dish`;
CREATE TABLE `ums_willbuy_dish`  (
  `user_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dish_id` int NOT NULL,
  UNIQUE INDEX `user_id`(`user_id`, `dish_id`) USING BTREE,
  INDEX `id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_willbuy_dish
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
