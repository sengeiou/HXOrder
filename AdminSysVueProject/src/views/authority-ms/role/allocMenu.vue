<template>
  <el-card class="form-container" shadow="never">
    <el-tree
      :data="menuTreeList"
      show-checkbox
      default-expand-all
      node-key="id"
      ref="tree"
      highlight-current
      :props="defaultProps"
    >
    </el-tree>

    <div style="margin-top: 20px">
      <span style="font-size: 15px; font-weight: bold"
        >设定角色可访问的店铺信息:
      </span>
      <br /><br />
      <el-checkbox-group v-model="checkStores">
        <el-checkbox-button
          v-for="store in stores"
          :label="store.id"
          :key="store.id"
          >{{ store.name }}</el-checkbox-button
        >
      </el-checkbox-group>
    </div>

    <div style="margin-top: 20px" align="center">
      <el-button type="primary" @click="handleSave()">保存</el-button>
      <el-button @click="handleClear()">清空</el-button>
    </div>
  </el-card>
</template>

<script>
import { fetchTreeList } from "@/api/menu";
import { listMenuByRole, allocMenu, listStoreByRole } from "@/api/role";
import { fetchList } from "@/api/store";

export default {
  name: "allocMenu",
  data() {
    return {
      checkStores: [],
      stores: [1, 2, 3],
      menuTreeList: [],
      defaultProps: {
        children: "children",
        label: "title",
      },
      roleId: null,
    };
  },
  created() {
    this.roleId = this.$route.query.roleId;
    this.treeList();
    this.getRoleMenu(this.roleId);
    this.getRoleStore(this.roleId);
    this.getAllStoreList();
  },
  methods: {
    getAllStoreList() {
      fetchList({ pageNum: 1, pageSize: -1 }).then((res) => {
        this.stores = res.data.arrays;
      });
    },
    treeList() {
      fetchTreeList().then((response) => {
        console.log("收到回答", response.data);
        this.menuTreeList = response.data.arrays;
      });
    },
    getRoleStore(roleId) {
      listStoreByRole(roleId).then((res) => {
        console.log(res.data);
        this.checkStores = res.data.arrays;
      });
    },
    getRoleMenu(roleId) {
      listMenuByRole(roleId).then((response) => {
        let menuList = response.data.arrays;
        let checkedMenuIds = [];
        if (menuList != null && menuList.length > 0) {
          for (let i = 0; i < menuList.length; i++) {
            let menu = menuList[i];
            if (menu.parentId !== 0 || menu.id == 1) {
              checkedMenuIds.push(menu.id);
            }
          }
        }
        this.$refs.tree.setCheckedKeys(checkedMenuIds);
      });
    },
    handleSave() {
      let checkedNodes = this.$refs.tree.getCheckedNodes();
      console.log("保存信息", checkedNodes);
      let checkedMenuIds = new Set();
      if (checkedNodes != null && checkedNodes.length > 0) {
        for (let i = 0; i < checkedNodes.length; i++) {
          let checkedNode = checkedNodes[i];
          if (checkedNode.parentId !== 0 || checkedNode.id == 1) {
            checkedMenuIds.add(checkedNode.id);
            if (!checkedMenuIds.has(checkedNode.parentId) && checkedNode.parentId != 0) {
              checkedMenuIds.add(checkedNode.parentId);
            }
          }
        }
      }
      console.log("dianpu", this.checkStores);
      this.$confirm("是否分配权限？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        console.log("结果！！！", Array.from(checkedMenuIds));
        let params = {
          roleId: parseInt(this.roleId),
          menuIds: Array.from(checkedMenuIds),
          storeIds: Array.from(this.checkStores),
        };
        // params.append("roleId", this.roleId);
        // params.append("ids", Array.from(checkedMenuIds));
        allocMenu(JSON.stringify(params)).then((response) => {
          this.$message({
            message: "分配成功",
            type: "success",
            duration: 1000,
          });
          this.$router.back();
        });
      });
    },
    handleClear() {
      this.$refs.tree.setCheckedKeys([]);
    },
  },
};
</script>

<style scoped>
</style>
