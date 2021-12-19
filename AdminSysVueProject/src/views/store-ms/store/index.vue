`<template>
   
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float: right"
          @click="handleSearchList()"
          type="primary"
          size="small"
        >
          查询
        </el-button>
        <el-button
          style="float: right; margin-right: 15px"
          @click="handleResetSearch()"
          size="small"
        >
          重置
        </el-button>
      </div>

      <div style="margin-top: 15px">
        <el-form
          :inline="true"
          :model="listQuery"
          size="small"
          label-width="140px"
        >
          <el-form-item label="店铺名称：">
            <el-input
              style="width: 203px"
              v-model="listQuery.keyword"
              placeholder="店铺名称"
            ></el-input>
          </el-form-item>

          <el-form-item label="营业状态：">
            <el-select
              v-model="listQuery.publishStatus"
              placeholder="全部"
              clearable
            >
              <el-option
                v-for="item in publishStatusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button class="btn-add" @click="handleAddProduct()" size="mini">
        添加店铺
      </el-button>
    </el-card>

    <!-- 展示部分 -->
    <!-- 对象数组是 :data list，这是 El元素的 table  -->
    <div class="table-container">
      <el-table
        ref="productTable"
        :data="list"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="listLoading"
        border
      >
        <el-table-column
          type="selection"
          width="60"
          align="center"
        ></el-table-column>

        <el-table-column label="店铺编号" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>

        <el-table-column label="店铺图片" width="120" align="center">
          <template slot-scope="scope"
            ><img style="height: 80px" :src="scope.row.img"
          /></template>
        </el-table-column>

        <el-table-column label="店铺名称" align="center">
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>

        <el-table-column label="店铺地址" width="120" align="center">
          <template slot-scope="scope">{{ scope.row.address }}</template>
        </el-table-column>

        <el-table-column label="标签" width="140" align="center">
          <template slot-scope="scope">
            <p>
              <el-switch
                @change="handleStoreWork(scope.$index, scope.row)"
                active-text="店铺营业"
                :active-value="1"
                :inactive-value="0"
                v-model="scope.row.working"
              >
              </el-switch>
            </p>
            <p>
              <el-switch
                @change="handlesupportTakeout(scope.$index, scope.row)"
                active-text="开启外卖"
                :active-value="1"
                :inactive-value="0"
                v-model="scope.row.supportTakeout"
              >
              </el-switch>
            </p>   
          </template>
        </el-table-column>

        <el-table-column label="营业时间" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.time }}</template>
        </el-table-column>

        <el-table-column label="联系方式" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.contactPhone }}</template>
        </el-table-column>

        <el-table-column label="操作" width="160" align="center">
          <template slot-scope="scope">
            <p>
              <el-button
                size="mini"
                @click="handleUpdateProduct(scope.$index, scope.row)"
                >编辑具体信息
              </el-button>
            </p>
            <p>
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)"
                >删除店铺记录
              </el-button>
            </p>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="batch-operate-container">
      <el-select size="small" v-model="operateType" placeholder="批量操作">
        <el-option
          v-for="item in operates"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <el-button
        style="margin-left: 20px"
        class="search-button"
        @click="handleBatchOperate()"
        type="primary"
        size="small"
      >
        确定
      </el-button>
    </div>

    <!-- 分页  -->
    <div class="pagination-container">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        layout="total, sizes, prev, pager, next, jumper"
        :page-size="listQuery.pageSize"
        :page-sizes="[5, 10, 20, 40, 80]"
        :current-page.sync="listQuery.pageNum"
        :total="total"
      >
      </el-pagination>
    </div>
  </div>
</template>





<script>
import { fetchList, getStore, updateDeleteStatus, 
updateWorkingStatus, updateTakeoutStatus, 
createStore, updateStore } from "@/api/store";

const defaultListQuery = {
  keyword: null,
  pageNum: 1,
  pageSize: 10,
  publishStatus: null,
};

export default {
  name: "productList",
  data() {
    return {
      //编辑店铺库存
      storeStock: {
        //是否显示
        dialogVisible: false,
        dishId: null,
        storeName: null,
        stock: null,
        storeStockList: [],
      },
      //批量操作
      operates: [
        {
          label: "店铺营业",
          value: "publishOn",
        },
        {
          label: "店铺歇业",
          value: "publishOff",
        },
        {
          label: "支持外卖配送",
          value: "takeoutOn",
        },
        {
          label: "取消支持外卖",
          value: "takeoutOff",
        },
      ],
      operateType: null,
      listQuery: Object.assign({}, defaultListQuery),
      list: null,
      total: null,
      listLoading: true,
      selectProductCateValue: null,
      multipleSelection: [],
      publishStatusOptions: [
        {

          value: 1,
          label: "正在营业",
        },
        {
          value: 0,
          label: "歇业中",
        },
      ],
    };
  },
  //初始化函数, created 和 mounted 都是页面加载初始化的函数
  created() {
    console.log("页面初始化");
    this.getList();
  },
  watch: {
    selectProductCateValue: function (newValue) {
      if (newValue != null && newValue.length == 2) {
        this.listQuery.productCategoryId = newValue[1];
      } else {
        this.listQuery.productCategoryId = null;
      }
    },
  },
  filters: {
    verifyStatusFilter(value) {
      if (value === 1) {
        return "审核通过";
      } else {
        return "未审核";
      }
    },
  },
  methods: {

    getList() {
      console.log("调用getList()");
      console.log(this.listQuery);
      this.listLoading = true;
      fetchList(this.listQuery).then((response) => {
        this.listLoading = false;
        this.list = response.data.arrays;
        this.total = response.data.total;
      });
    },
    // getList() {
    //   fetchList({pageNum: 1, pageSize: 100}).then(response => {
    //     this.Options = [];
    //     let List = response.data.list;
    //     for (let i = 0; i < List.length; i++) {
    //       this.Options.push({label: List[i].name, value: List[i].id});
    //     }
    //   });
    // },
    // getProductCateList() {
    //   fetchListWithChildren().then(response => {
    //     let list = response.data;
    //     this.productCateOptions = [];
    //     for (let i = 0; i < list.length; i++) {
    //       let children = [];
    //       if (list[i].children != null && list[i].children.length > 0) {
    //         for (let j = 0; j < list[i].children.length; j++) {
    //           children.push({label: list[i].children[j].name, value: list[i].children[j].id});
    //         }
    //       }
    //       this.productCateOptions.push({label: list[i].name, value: list[i].id, children: children});
    //     }
    //   });
    // },


    handleSearchList() {
      this.listQuery.pageNum = 1;
      this.getList();
    },
    handleAddProduct() {
      this.$router.push({ path: "/store-ms/addStore" });
    },
    //批量操作
    handleBatchOperate() {
      if (this.operateType == null) {
        this.$message({
          message: "请选择操作类型",
          type: "warning",
          duration: 1000,
        });
        return;
      }
      if (this.multipleSelection == null || this.multipleSelection.length < 1) {
        this.$message({
          message: "请选择要操作的商品",
          type: "warning",
          duration: 1000,
        });
        return;
      }
      this.$confirm("是否要进行该批量操作?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        let ids = [];
        for (let i = 0; i < this.multipleSelection.length; i++) {
          ids.push(this.multipleSelection[i].id);
        }
        switch (this.operateType) {
          case this.operates[0].value:
            this.updateWorkingStatus(1, ids);
            break;
          case this.operates[1].value:
            this.updateWorkingStatus(0, ids);
            break;
          case this.operates[2].value:
            this.updateTakeoutStatus(1, ids);
            break;
          case this.operates[3].value:
            this.updateTakeoutStatus(0, ids);
            break;
          case this.operates[7].value:
            this.updateDeleteStatus(1, ids);
            break;
          default:
            break;
        }
        this.getList();
      });
    },
    //分页大小改变
    handleSizeChange(val) {
      this.listQuery.pageNum = 1;
      this.listQuery.pageSize = val;
      this.getList();
    },
    //当前页数改变
    handleCurrentChange(val) {
      this.listQuery.pageNum = val;
      this.getList();
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      console.log(val, this.multipleSelection);
    },
    handleStoreWork(index, row) {
      let ids = [];
      ids.push(row.id);
      this.updateWorkingStatus(row.working, ids);
    },
    handlesupportTakeout(index, row) {
      let ids = [];
      ids.push(row.id);
      this.updateTakeoutStatus(row.supportTakeout, ids);
    },
    //重置
    handleResetSearch() {
      this.selectProductCateValue = [];
      this.listQuery = Object.assign({}, defaultListQuery);
    },
    handleDelete(index, row) {
      this.$confirm("是否要进行删除操作?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        let ids = [];
        ids.push(row.id);
        this.updateDeleteStatus(1, ids);
      });
    },
    handleUpdateProduct(index, row) {
      this.$router.push({path:'/store-ms/updateStore',query:{id:row.id}});
    },
    updateWorkingStatus(publishStatus, ids) {
      let params = new URLSearchParams();
      params.append("storeIds", ids);
      params.append("working", publishStatus);
      updateWorkingStatus(params).then((response) => {
        this.$message({
          message: "修改成功",
          type: "success",
          duration: 1000,
        });
      });
    },
    updateTakeoutStatus(recommendStatus, ids) {
      let params = new URLSearchParams();
      params.append("storeIds", ids);
      params.append("takeoutStatus", recommendStatus);
      updateTakeoutStatus(params).then((response) => {
        this.$message({
          message: "修改成功",
          type: "success",
          duration: 1000,
        });
      });
    },
    updateDeleteStatus(deleteStatus, ids) {
      let params = new URLSearchParams();
      params.append("storeIds", ids);
      updateDeleteStatus(params).then((response) => {
        this.$message({
          message: "删除成功",
          type: "success",
          duration: 1000,
        });
        this.getList();
      });
      
    },
  },
};
</script>
<style></style>


`