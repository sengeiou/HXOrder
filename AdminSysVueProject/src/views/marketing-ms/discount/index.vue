<template>
   
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float: right"
          type="primary"
          @click="handleSearchList()"
          size="small"
        >
          查询搜索
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
              v-model="listQuery.keyword"
              class="input-width"
              placeholder="菜品名称"
            ></el-input>
          </el-form-item>

          <el-form-item label="促销类型：">
            <el-select
              v-model="listQuery.discountType"
              placeholder="全部"
              clearable
              class="input-width"
            >
              <el-option
                v-for="item in recommendOptions"
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
      <el-button size="mini" class="btn-add" @click="handleSelectAdd()"
        >选择添加</el-button
      >
    </el-card>

    <div class="table-container">
      <el-table
        ref="newDiscountTable"
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

        <el-table-column label="菜品编号" width="120" align="center">
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>

        <el-table-column label="菜品名称" align="center">
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>

        <el-table-column label="折扣类型" width="160" align="center">
          <template slot-scope="scope">
            {{ scope.row.discount.type == 2 ? "立减优惠" : "折扣优惠" }}
          </template>
        </el-table-column>

        <el-table-column label="折扣力度" width="160" align="center">
          <template slot-scope="scope">
            {{
              (scope.row.discount.type == 2 ? "立减 " : "") +
              scope.row.discount.val +
              (scope.row.discount.type == 2 ? " 元" : " 折")
            }}
          </template>
        </el-table-column>

        <el-table-column label="折扣限制" align="center">
          <template slot-scope="scope">{{
            scope.row.discount.count
          }} 次/日</template>
        </el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleUpdate(scope.$index, scope.row)"
              >编辑
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
              >删除
            </el-button>
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

    <div class="pagination-container">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        layout="total, sizes,prev, pager, next,jumper"
        :page-size="listQuery.pageSize"
        :page-sizes="[5, 10, 15]"
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
        @row-click="rowClick"
      >
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

    <el-dialog title="设置折扣" :visible.sync="setDialogVisible">
      <el-form :model="discountForm">
        <el-form-item label="菜品ID" :label-width="formLabelWidth">
          <el-input
            disabled
            v-model="discountForm.dishId"
            autocomplete="off"
            :placeholder="discountForm.dishId"
            >form.dishId</el-input
          >
        </el-form-item>

        <el-form-item label="菜品名称" :label-width="formLabelWidth">
          <el-input
            disabled
            v-model="discountForm.name"
            autocomplete="off"
          ></el-input>
        </el-form-item>

        <el-form-item
          label="菜品类型"
          width="160"
          :label-width="formLabelWidth"
        >
          <el-input
            disabled
            autocomplete="off"
            :placeholder="dishId >= 100000 ? '套餐' : '菜品'"
          ></el-input>
        </el-form-item>

        <el-form-item label="促销类型" :label-width="formLabelWidth">
          <el-select v-model="discountForm.type" placeholder="请选择促销类型">
            <el-option label="折扣优惠" value="1"></el-option>
            <el-option label="立减优惠" value="2"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="折扣力度" width="160" align="center">
          <el-input
            v-model="discountForm.val"
            autocomplete="off"
            placeholder="请填写折扣力度或立减额度"
          ></el-input>
        </el-form-item>

        <el-form-item label="限制每日使用次数" width="160" align="center">
          <el-input
            v-model="discountForm.count"
            autocomplete="off"
            placeholder="请填写该菜品折扣用户每日最多可享受的次数"
          ></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="setDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmSetDialog">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  fetchDiscountList as fetchList,
  addDiscount,
  updateDiscount,
  deleteDiscount,
} from "@/api/marketing";
import { fetchList as fetchDishList } from "@/api/dish";
import { Message, MessageBox } from "element-ui";
const defaultListQuery = {
  pageNum: 1,
  pageSize: 5,
  name: null,
  discountType: null,
};
const defaultRecommendOptions = [
  {
    label: "立减优惠",
    value: 2,
  },
  {
    label: "折扣优惠",
    value: 1,
  },
];
export default {
  name: "homeDiscountList",
  data() {
    return {
      discountForm: {
        dishId: null,
        name: null,
        val: null,
        count: null,
        type: null,
      },
      form: {
        dishId: null,
        dishName: null,
        discountType: null,
        discountVal: null,
      },
      dialogSelectRow: null,
      listQuery: Object.assign({}, defaultListQuery),
      recommendOptions: Object.assign({}, defaultRecommendOptions),
      list: null,
      total: null,
      listLoading: false,
      operates: [
        {
          label: "一键删除",
          value: 0,
        },
      ],
      multipleSelection:[],
      operateType: null,
      selectDialogVisible: false,
      dialogData: {
        list: null,
        total: null,
        listQuery: {
          keyword: null,
          pageNum: 1,
          pageSize: 5,
        },
      },
      setDialogVisible: false,
    };
  },
  created() {
    this.getList();
  },

  methods: {
    confirmSetDialog() {
      if (this.discountForm.type == null || this.discountForm.val == null) {
        Message({
          message: "请正确填写表单",
          type: "error",
          duration: 3 * 1000,
        });
        return;
      }
      this.$confirm("确定操作?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.setDialogVisible = false;
        console.log("this.form ==", this.form);
        if (!this.discountForm.update) {
          
          addDiscount(this.discountForm)
            .then((res) => {

              Message({
                message: "添加成功",
                type: "success",
                duration: 3 * 1000,
              });
              this.getList();
            })
            .catch((err) => {
              console.log(err);
            });
        } else {
          updateDiscount(this.discountForm).then((res) => {
              console.log(res);
              Message({
                message: "更新成功",
                type: "success",
                duration: 3 * 1000,
              });
            })
            .catch((err) => {
              console.log(err);
            });
        }
      });
      
    },
    rowClick(row, event, column) {
      console.log("点击事件触发" + column, row);
      this.dialogSelectRow = row;
    },
    tableRowClassName({ row, rowIndex }) {
      if (this.dialogSelectRow && row.id === this.dialogSelectRow.id) {
        return "success-row";
      }
      return "";
    },
    handleResetSearch() {
      this.listQuery = Object.assign({}, defaultListQuery);
    },
    handleSearchList() {
      this.listQuery.pageNum = 1;
      this.getList();
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleSizeChange(val) {
      this.listQuery.pageNum = 1;
      this.listQuery.pageSize = val;
      this.getList();
    },
    handleCurrentChange(val) {
      this.listQuery.pageNum = val;
      this.getList();
    },
    handleUpdate(index, row) {
      this.setDialogVisible = true;
      this.discountForm = row.discount;
      this.discountForm.dishId = row.id;
      this.discountForm.update = true;
      this.discountForm.name = row.name;
      // this.updateDiscount(row);
    },
    handleDelete(index, row) {
      this.deleteDiscount(row.id);
    },
    handleBatchOperate() {
      if (this.multipleSelection < 1) {
        this.$message({
          message: "请选择一条记录",
          type: "warning",
          duration: 1000,
        });
        return;
      }
      let ids = [];
      for (let i = 0; i < this.multipleSelection.length; i++) {
        ids.push(this.multipleSelection[i].id);
      }
      if (this.operateType === 0) {
        //删除
        this.deleteDiscount(ids);
      }
    },
    handleSelectAdd() {
      this.dialogSelectRow = null;
      this.selectDialogVisible = true;
      this.dialogData.listQuery.keyword = null;
      this.getDialogList();
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
    handleSelectDialogConfirm() {
      if (!this.dialogSelectRow) {
        this.$message({
          message: "请选择一条记录",
          type: "warning",
          duration: 1000,
        });
        return;
      }
      this.$confirm("确定选择?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.setDialogVisible = true;
        this.discountForm = this.dialogSelectRow;
        this.discountForm.dishId = this.discountForm.id;
      });
    },
    handleEditSort(index, row) {
      this.sortDialogVisible = true;
      this.sortDialogData.sort = row.sort;
      this.sortDialogData.id = row.id;
    },
    getList() {
      this.listLoading = true;
      fetchList(this.listQuery).then((response) => {
        this.listLoading = false;
        this.list = response.data.arrays;
        this.total = response.data.total;
      });
    },
    deleteDiscount(ids) {
      this.$confirm("是否要删除，删除不会删除菜品，仅会删除菜品对应折扣记录?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        let params = new URLSearchParams();
        params.append("dishIds", ids);
        deleteDiscount(params).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          this.getList();
        });
      });
    },
    getDialogList() {
      fetchDishList(this.dialogData.listQuery).then((response) => {
        console.log("调用成功");
        this.dialogData.list = response.data.arrays;
        this.dialogData.total = response.data.total;
      });
    },
  },
};
</script>
<style>
.el-table .success-row {
  background: #f0f9eb;
}
</style>
