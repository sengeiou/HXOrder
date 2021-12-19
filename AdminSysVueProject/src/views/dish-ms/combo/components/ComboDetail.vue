<template>
   
  <el-card class="form-container" shadow="never" v-loading="loading">
    <span class="title">填写套餐表单: </span>
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="80px"
      class="form"
    >
      <el-form-item label-width="100px" label="套餐名称:" prop="name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>

      <el-form-item label-width="100px" label="具体配置:" prop="detail"  >
        <el-button size="mini" @click="handleAdd()">选择添加</el-button>
        <el-table
          highlight-current-row
          @current-change="handleCurrentChange"
          :data="form.comboDish"
          height="350"
          border
          style="width: 100%"
          :row-class-name="tableRowClassName"
        >
          <el-table-column prop="dishId" label="菜品编号" width="180">
            <template slot-scope="scope">
              <el-input
              
                v-model="scope.row.dishId"
                disabled
              ></el-input>
            </template>
          </el-table-column>

          <el-table-column prop="dishName" label="菜品名称" width="180">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.dishName"
                disabled
              ></el-input>
            </template>
          </el-table-column>

          <el-table-column prop="dishNum" label="菜品数量">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.dishNum"
                @input="scope.row.update = true"
                @change="numChange"
              ></el-input>
            </template>
          </el-table-column>

          <el-table-column label="操作">
            <template slot-scope="scope">
              <!-- <p>
                <el-button
                  size="mini"
                  v-if="!scope.row.success"
                  :disabled="scope.row.success"
                  @click="handleAdd(scope.$index, scope.row)"
                  >添加</el-button
                >
              </p>
              <p>
                <el-button
                  size="mini"
                  type="primary"
                  v-if="!scope.row.success"
                  :disabled="scope.row.success"
                  @click="handleSelect(scope.$index, scope.row)"
                  >选择</el-button
                >
              </p> -->
              <p>
                <el-button
                  size="mini"
                  type="danger"
                  @click="handleDelete(scope.$index, scope.row)"
                  >移除</el-button
                >
              </p>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>

      <el-form-item label-width="100px" label="套餐单价:" prop="price">
        <el-input
          disabled
          v-model.number="form.price"
          placeholder="请填写套餐具体配置"
        ></el-input>
      </el-form-item>

      <el-form-item label-width="100px" label="套餐分类:" prop="kind">
        <el-checkbox-group v-model="form.classificationIds">
          <el-checkbox v-for="c in cs" :label="c.id" :key="c.id">{{
            c.name
          }}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label-width="100px" label="套餐标签:" prop="tags">
        <el-input
          type="textarea"
          :rows="3"
          placeholder="多个标签请以空格或换行分隔，如：养生菜 下酒菜 适合小孩子吃"
          v-model="form.tag"
        >
        </el-input>
      </el-form-item>

      <el-form-item label="优惠方式: " label-width="100px">
        <el-radio-group v-model="form.discount.type" size="medium" @change="test">
          <el-radio-button border label="0">无优惠</el-radio-button>
          <el-radio-button border label="1">折扣优惠</el-radio-button>
          <el-radio-button border label="2">立减优惠</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item
        v-if="form.discount.type !== 0"
        label="优惠力度: "
        label-width="100px"
      >
        <el-input
          v-if="form.discount.type == 1"
          v-model.number="form.discount.val"
          placeholder="请输入折扣力度，输入应为一个 0 ~ 10 之间的数，例如输入 9 表示九折"
        ></el-input>
        <el-input
          v-if="form.discount.type == 2"
          v-model="form.discount.val"
          placeholder="请输入立减价格，输入应为一个整数或小数，单位为元"
        ></el-input>
        <span style="color:red; font-size=15px;"
          >注：套餐作为长期营销手段，应当长期稳定提供优惠，不应设置限制</span
        >
      </el-form-item>

      <el-form-item style="margin-left: 20px">
        <el-button type="primary" @click="finishCommit('form')"
          >立即创建</el-button
        >
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>

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
        :row-class-name="tableRowClassNameOfDialog"
        border
        @row-click="rowClick"
      >
        <el-table-column label="菜品ID" align="center">
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>
        <el-table-column label="菜品名称" width="160" align="center">
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>
        <el-table-column label="菜品价格" width="160" align="center">
          <template slot-scope="scope">￥{{ scope.row.price }}</template>
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
  </el-card>
</template>
<script>
import { updateCombo, createCombo, getCombo, deleteComboDish } from "@/api/combo";

import { fetchClassificationList, fetchList } from "@/api/dish";
const form = {
  id: null,
  name: null,
  price: 0,
  classificationIds: [],
  discount: {
    type: null,
    val: null,
    count: -1,
  },
  tag: null,
  comboDish: [],
};

export default {
  name: "comboDish",

  props: {
    isEdit: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      loading: false,
      selectDialogVisible: false,
      dialogSelectRow: null,
      cs: ["yyds"],
      dialogData: {
        list: null,
        total: null,
        listQuery: {
          keyword: null,
          pageNum: 1,
          pageSize: 8,
        },
      },
      rules: {
        name: [
          { required: true, message: "请输入套餐名称", trigger: "blur" },
          {
            min: 1,
            max: 20,
            message: "长度为 1 到 20 个字符",
            trigger: "blur",
          },
        ],
      },
      form: Object.assign({}, form),
    };
  },
  //初始化函数
  created() {
    this.getClassificationList();
    //如果是选择更新，则查询套餐信息
    if (this.isEdit) {
      getCombo(this.$route.query.id)
        .then((response) => {
          console.log(response.data);
          let tag = "";
          response.data.tags.forEach(
            (item) => (tag += item.replace(",", "") + " ")
          );
          if (response.data.discount == null) {
            response.data.discount = { type: 0, val: null, count: 0 };
          }
          response.data.comboDish.forEach(item=>item.flag = true)
          response.data.tag = tag;
          this.form = response.data;
        
          console.log("this.form = ", this.form);
        })
        .catch((err) => {
          console.log(err);
        });
    }
  },
  methods: {
    test() {
      console.log("ee", this.form.discount);
    },
    getClassificationList() {
      fetchClassificationList()
        .then((res) => {
          let arr = res.data.arrays.filter((item) => {
            return item.id >= 100000;
          });
          this.cs = arr;
          console.log("cs == ", JSON.stringify(this.cs));
        })
        .catch((err) => {
          console.log("获取Cshivering" + err);
        });
    },
    handleAdd() {
      this.selectDialogVisible = true;
      this.dialogData.listQuery.keyword = null;
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
        this.form.comboDish.unshift({
          dishId: this.dialogSelectRow.id,
          dishName: this.dialogSelectRow.name,
          dishPrice: this.dialogSelectRow.price,
        });
        this.selectDialogVisible = false;
        this.dialogSelectRow = null;

        console.log("确定", this.form);
      });
    },
    numChange() {
      console.log("roworow");
      let list = this.form.comboDish;
      let price = 0;
      console.log(list, "lilili");
      list.forEach((item) => {
        price += item.dishPrice * item.dishNum;
      });
      this.form.price = price;
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
    rowClick(row, event, column) {
      console.log("点击事件触发" + column, row);
      this.dialogSelectRow = row;
    },
    tableRowClassNameOfDialog({ row, rowIndex }) {
      if (this.dialogSelectRow && row.id === this.dialogSelectRow.id) {
        return "success-row";
      }
      return "";
    },
    handleSelect(index, row) {
      this.form.price += row.dishNum * 5;
      row.success = true;
      row.update = false;
      this.comboDish.unshift({
        dishId: null,
        dishName: null,
        dishNum: null,
        success: false,
        update: false,
      });
    },
    handleUpdate(index, row) {
      row.update = false;
    },

    handleDelete(index, row) {
      this.$confirm("此操作将删除该项, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        console.log("没？？");
          if (!row.flag) {
            this.form.comboDish.splice(index, 1);
            this.$message({
              duration: 1500,
              type: "success",
              message: "删除成功!",
            });
            this.numChange();
          } else {
            this.loading = true;
            deleteComboDish(row).then(res=> {
               this.loading = false;
               this.form.comboDish.splice(index, 1);
               this.$message({
                duration: 3000,
                type: "info",
                message: "成功删除",
              });
              this.numChange();
            }).catch(err=> {
              this.$message({
                duration: 3000,
                type: "info",
                message: "网络出错",
              });
              this.loading = false;
            })
          }
        }) .catch(() => {
          this.$message({
            duration: 3000,
            type: "info",
            message: "已取消删除",
          });
        });
    },

    handleCurrentChange(val) {
      this.currentRow = val;
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.success) return "success-row";
      return "";
    },

    finishCommit(formName) {
      this.numChange();
      console.log(formName);
      console.log("this.form ===== " , this.form);
      this.$refs[formName].validate((valid) => {
        console.log(valid);
        if (valid) {
          let isEdit = this.isEdit;
          this.form.tags = this.form.tag == null ? [] : this.form.tag.split("\\s+");
          console.log("this.form == ", this.form);
          this.$confirm("是否要提交该产品", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }).then(() => {
            if (isEdit) {
              updateCombo(this.form).then((response) => {
                this.$message({
                  type: "success",
                  message: "提交成功",
                  duration: 1000,
                });
                this.$router.back();
              });
            } else {
              createCombo(this.form).then((response) => {
                this.$message({
                  type: "success",
                  message: "提交成功",
                  duration: 1000,
                });
                location.reload();
              });
            }
          });
        } else {
          console.log("error submit!!");
          alert("您填写的表单有误，请重新填写");
          return false;
        }
      });
    },
  },
};
</script>
<style>
.title {
  font: medium;
  font-weight: bolder;
}

.form-container {
  width: 800px;
  padding: 0;
}

.form {
  margin-top: 15px;
}

.el-table .success-row {
  background: #f0f9eb;
}
</style>


