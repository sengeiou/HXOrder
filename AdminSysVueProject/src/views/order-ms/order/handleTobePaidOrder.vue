<template>
   
  <div class="detail-container">
    <span style="font-weight: 700">订单ID：{{ order.id }}</span>

    <el-card shadow="never" style="margin-top: 15px">
      <div class="operate-container" v-if="order.paymentStatus == 0">
        <i class="el-icon-warning color-danger" style="margin-left: 20px"></i>
        <span class="color-danger">当前订单支付状态：不可支付</span>
      </div>

      <div class="operate-container" v-if="order.paymentStatus == 1">
        <i class="el-icon-check" style="margin-left: 20px; color: #67c23a"></i>
        <span style="color: #67c23a">当前订单支付状态：可支付</span>
      </div>

      <div style="margin-top: 20px">
        <svg-icon icon-class="marker" style="color: #606266"></svg-icon>
        <span class="font-small">菜品信息</span>

        <p style="color: red; font-size: 13px">
          注：当您删除菜品时，请即时更改折扣金额；如您需要增加菜品，您可以将费用填写至其他费用一栏
        </p>
      </div>
      <el-table
        ref="orderItemTable"
        :data="order.dishOrders"
        style="width: 100%; margin-top: 20px"
        border
      >
        <el-table-column label="菜品ID" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.dishId }}
          </template>
        </el-table-column>

        <el-table-column label="菜品名称" align="center">
          <template slot-scope="scope">
            {{ scope.row.dishName }}
          </template>
        </el-table-column>

        <el-table-column label="菜品单价" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.dishPrice }}
          </template>
        </el-table-column>

        <el-table-column label="选择数量" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.dishNum }}
          </template>
        </el-table-column>

        <el-table-column label="小计" width="100" align="center">
          <template slot-scope="scope">
            ￥{{ scope.row.dishNum * scope.row.dishPrice }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <p>
              <el-button
                size="mini"
                type="primary"
                @click="handleUpdate(scope.$index, scope.row)"
                >编辑行
              </el-button>
            </p>
            <p>
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)"
                >删除行
              </el-button>
            </p>
          </template>
        </el-table-column>
      </el-table>

      <div style="width: 50%; margin-top: 50px">
        <el-form :model="order">
          <el-form-item label="折扣费用:" label-width="100px">
            <el-input
              v-model="order.shopDiscount"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="其他费用:" label-width="100px">
            <el-input v-model="order.otherFee" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
      </div>

      <div>
        合计：<span class="color-danger"
          >￥{{
            order.otherFee -
            order.shopDiscount -
            order.couponDiscount +
            order.originalPrice
          }}</span
        >
      </div>

      <div class="btn">
        <el-row>
          <el-button type="success" @click="s1">设置为可支付</el-button>
          <el-button type="warning" @click="s2">设置为已完成</el-button>
          <el-button type="danger" @click="s3">设置为不可支付</el-button>
        </el-row>
      </div>
    </el-card>

    <el-dialog title="操作行" :visible.sync="dialogFormVisible">
      <el-form :model="dialogData.data">
        <el-form-item label="菜品ID" label-width="100px">
          <el-input
            v-model="dialogData.data.dishId"
            autocomplete="off"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="菜品名称" label-width="100px">
          <el-input
            v-model="dialogData.data.dishName"
            autocomplete="off"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="菜品单价" label-width="100px">
          <el-input
            v-model="dialogData.data.dishPrice"
            autocomplete="off"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="菜品数量" label-width="100px">
          <el-input
            v-model="dialogData.data.dishNum"
            @input="numChange"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmDiaLog">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  getOrderDetail,
  hadnleToBePaidOrder,
  updateOrderType,
} from "@/api/order";
import VDistpicker from "v-distpicker";

const defaultForm = {};
export default {
  name: "orderDetail",
  components: { VDistpicker },
  data() {
    return {
      dialogFormVisible: false,
      dialogData: {
        index: null,
        data: {},
        type: null,
      },
      form: {},
      id: null,
      order: {},
    };
  },
  created() {
    this.id = this.list = this.$route.query.id;
    getOrderDetail(this.id).then((response) => {
      console.log("orderDetail", response.data);
      this.order = response.data;
    });
  },
  filters: {
    formatCreateTime(time) {
      let date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
    formatOrderType(value) {
      const map = [
        "待用餐",
        "待支付",
        "确认中",
        "备餐中",
        "待用餐",
        "待取餐",
        "配送中",
        "已完成",
        "取消确认中",
        "已取消",
      ];
      return map[value];
    },
    formatConsumeType(value) {
      const map = ["扫码点餐", "到店消费", "到店自取", "外卖配送"];
      return map[value];
    },
  },
  methods: {
    numChange(e) {
      console.log(e);
    },
    s1() {
      this.$confirm("此操作将设置订单为可支付, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then((res) => {
        this.order.paymentStatus = 1;
        hadnleToBePaidOrder(this.order).then((res) => {
          this.$message({
            type: "success",
            message: "设置成功",
          });
          location.reload();
        });
      });
    },
    s2() {
      this.$confirm("此操作将永久删除该项, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then((res) => {
        this.order.paymentStatus = 0;
        hadnleToBePaidOrder(this.order).then((res) => {
          updateOrderType(this.order.id, -1, 7)
            .then((res) => {
              this.order.paymentStatus = 0;
              this.$message({
                type: "success",
                message: "设置成功",
              });
              this.$router.back();
            })
            .catch((err) => {
              this.$message({
                type: "info",
                message: "操作失败，已将该订单暂定为不可支付状态",
                duration: 3500,
              });
              location.reload();
            });
        });
      });
    },
    s3() {
      this.$confirm("此操作将更新表单, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then((res) => {
        this.order.paymentStatus = 0;
        hadnleToBePaidOrder(this.order).then((res) => {
          this.$message({
            type: "success",
            message: "设置成功",
          });
          location.reload();
        });
      });
    },
    confirmDiaLog() {
      let i = this.dialogData.index;
      console.log(this.dialogData.data.dishNum, this.order.dishOrders[i].dishNum, this.order.dishOrders[i].dishPrice);
      this.order.originalPrice +=
        (this.dialogData.data.dishNum - this.order.dishOrders[i].dishNum) *
        this.order.dishOrders[i].dishPrice;
      this.order.dishOrders[i].dishNum = this.dialogData.data.dishNum;
      this.dialogFormVisible = false;
    },
    handleDelete(index, row) {
      this.$confirm("此操作将永久删除该项, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.order.dishOrders.splice(index, 1);
          this.$message({
            type: "success",
            message: "删除成功!",
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    handleUpdate(index, row) {
      this.dialogFormVisible = true;
      this.dialogData.data = JSON.parse(JSON.stringify(row));
      this.dialogData.index = index;
    },
    getStatus() {
      const map = [
        "待用餐",
        "待支付",
        "确认中",
        "备餐中",
        "待用餐",
        "待取餐",
        "配送中",
        "已完成",
        "取消确认中",
        "已取消",
      ];
      let type = map[this.order.orderType];
      let steps = this.order.steps;
      for (let i in steps) {
        if (steps[i] == type) {
          return parseInt(i);
        }
      }
      return 0;
    },
  },
};
</script>
<style scoped>
.detail-container {
  width: 80%;
  padding: 20px 20px 20px 20px;
  margin: 20px auto;
}

.operate-container {
  background: #f2f6fc;
  height: 80px;
  margin: -20px -20px 0;
  line-height: 80px;
}

.operate-button-container {
  float: right;
  margin-right: 20px;
}

.table-layout {
  margin-top: 20px;
  border-left: 1px solid #dcdfe6;
  border-top: 1px solid #dcdfe6;
}

.table-cell {
  height: 60px;
  border-right: 1px solid #dcdfe6;
  border-bottom: 1px solid #dcdfe6;
  padding: 10px;
  padding-top: 20px;
  font-size: 14px;
  color: #606266;
  text-align: center;
  overflow: auto;
}

.table-cell-title {
  border-right: 1px solid #dcdfe6;
  border-bottom: 1px solid #dcdfe6;
  padding: 10px;
  background: #f2f6fc;
  text-align: center;
  font-size: 14px;
  color: #303133;
}

.btn {
  margin-top: 15px;
  text-align: center;
}
</style>


