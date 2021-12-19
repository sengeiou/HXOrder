<template>
   
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
          <el-form-item label="菜品名称：">
            <el-input
              style="width: 203px"
              v-model="listQuery.keyword"
              placeholder="菜品名称"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button class="btn-add" @click="handleAddDish()" size="mini">
        添加菜品
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
        :default-sort="{ prop: 'id', order: 'ascending' }"
        border
      >
        <el-table-column
          type="selection"
          width="60"
          align="center"
        ></el-table-column>

        <el-table-column
          label="编号"
          width="100"
          align="center"
          prop="id"
          sortable
        >
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>

        <el-table-column label="菜品图片" width="120" align="center">
          <template slot-scope="scope"
            ><img style="height: 80px" :src="scope.row.dishImg"
          /></template>
        </el-table-column>

        <el-table-column label="菜品名称" align="center">
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>

        <el-table-column label="价格" sortable="" width="120" align="center">
          <template slot-scope="scope">价格：￥{{ scope.row.price }}</template>
        </el-table-column>

        <el-table-column label="标签" width="140" align="center">
          <template slot-scope="scope">
            <el-switch
              @change="handleNewStatusChange(scope.$index, scope.row)"
              active-text="新品推荐"
              :active-value="1"
              :inactive-value="0"
              v-model="scope.row.newStatus"
            >
            </el-switch>
          </template>
        </el-table-column>

        <el-table-column sortable label="销量" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.sale }}</template>
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

    <el-dialog title="选择菜品" :visible.sync="selectDialogVisible" width="50%">
      <el-input
        v-model="dialogData.listQuery.keyword"
        style="width: 250px; margin-bottom: 20px"
        size="small"
        placeholder="菜品名称搜索"
      >
        <el-button
          slot="append"
          icon="el-icon-search"
          @click="handleSelectSearch()"
        ></el-button>
      </el-input>

      <el-table
        :data="dialogData.list"
        :row-class-name="tableRowClassName"
        border
         @selection-change="dialogHandleSelectionChange"
      >
      <el-table-column
          type="selection"
          width="60"
          align="center"
        ></el-table-column>
        <el-table-column label="菜品ID" align="center">
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>
        <el-table-column label="菜品名称" width="160" align="center">
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>
        <el-table-column label="菜品类型" width="160" align="center">
          <template slot-scope="scope">{{
            scope.row.id >= 100000 ? "套餐" : "菜品"
          }}</template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          background
          @size-change="handleDialogSizeChange"
          @current-change="handleDialogCurrentChange"
          layout="prev, pager, next"
          :current-page.sync="dialogData.listQuery.pageNum"
          :page-size="dialogData.listQuery.pageSize"
          :page-sizes="[5, 10, 15]"
          :total="dialogData.total"
        >
        </el-pagination>
      </div>
      <div style="clear: both"></div>

      <div slot="footer">
        <el-button size="small" @click="selectDialogVisible = false"
          >取 消</el-button
        >
        <el-button
          size="small"
          type="primary"
          @click="handleSelectDialogConfirm()"
          >确 定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>





<script>
import {
  fetchList,
  updateDeleteStatus,
  updateNewStatus,
  updatePublishStatus,
  getDishStock,
  updateDishStock,
} from "@/api/dish";

const defaultListQuery = {
  keyword: null,
  pageNum: 1,
  pageSize: 10,
  newStatus: 1,
};

export default {
  name: "productList",
  data() {
    return {
      selectDialogVisible: false,
      //批量操作
      operates: [
        {
          label: "全部取消新品推荐",
          value: "newOff",
        },
      ],
      operateType: null,
      listQuery: Object.assign({}, defaultListQuery),
      list: null,
      total: null,
      dialogLoading: false,
      listLoading: true,
      selectProductCateValue: null,
      multipleSelection: [],
      dialogData: {
        list: null,
        total: null,
        listQuery: {
          keyword: null,
          pageNum: 1,
          pageSize: 5,
          newStatus: 0,
        },
        multipleSelection: [],
      },
    };
  },
  //初始化函数, created 和 mounted 都是页面加载初始化的函数
  created() {
    console.log("页面初始化");
    this.getList();
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
      fetchList(this.listQuery)
        .then((response) => {
          console.log(response);
          this.listLoading = false;
          this.list = response.data.arrays;
          this.total = response.data.total;
        })
        .catch((err) => {
          console.log("错误" + err);
        });
    },

    handleSearchList() {
      this.listQuery.pageNum = 1;
      this.getList();
    },
    handleSelectDialogConfirm() {
      this.$confirm("是否要进行该批量操作?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        let ids = [];
        this.dialogData.multipleSelection.forEach(item=>ids.push(item.id));
        this.selectDialogVisible = false;
        this.updateNewStatus(1, ids);
      });
      
    },
    handleSelectSearch() {
      this.getDialogList();
    },
    handleDialogSizeChange(val) {
      this.dialogData.listQuery.pageNum = 1;
      this.dialogData.listQuery.pageSize = val;
      this.getDialogList();
    },
    handleDialogCurrentChange(val) {
      this.dialogData.listQuery.pageNum = val;
      this.getDialogList();
    },
    handleAddDish() {
      this.getDialogList();
      this.selectDialogVisible = true;
    },
    getDialogList() {
      fetchList(this.dialogData.listQuery).then((response) => {
        console.log("调用成功");
        this.dialogData.list = response.data.arrays;
        this.dialogData.total = response.data.total;
      });
    },
    //批量删除操作
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
            this.updateNewStatus(0, ids);
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
    dialogHandleSelectionChange(val) {
      this.dialogData.multipleSelection = val;
      console.log(val, this.dialogData.multipleSelection);
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      console.log(val, this.multipleSelection);
    },
    handleNewStatusChange(index, row) {
      let ids = [];
      ids.push(row.id);
      console.log("resc", row.newStatus);
      this.updateNewStatus(row.newStatus, ids);
    },
    //重置
    handleResetSearch() {
      this.selectProductCateValue = [];
      this.listQuery = Object.assign({}, defaultListQuery);
    },
    updateNewStatus(newStatus, ids) {
      let params = new URLSearchParams();
      params.append("dishIds", ids);
      params.append("newStatus", newStatus);
      updateNewStatus(params).then((response) => {
        this.$message({
          message: "修改成功",
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


