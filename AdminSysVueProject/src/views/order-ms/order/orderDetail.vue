<template>
   
  <div class="detail-container">
    <div>
      <el-steps :active="getStatus()" finish-status="success" align-center>
        <el-step
          v-for="(item, i) in order.steps"
          :key="i"
          :title="item"
        ></el-step>
      </el-steps>
    </div>
    <el-card shadow="never" style="margin-top: 15px">
      <div class="operate-container">
        <i class="el-icon-warning color-danger" style="margin-left: 20px"></i>
        <span class="color-danger"
          >当前订单状态：{{ order.orderType | formatOrderType }}</span
        >
        <div class="operate-button-container">
          <el-button size="mini" @click="showMessageDialog"
            >发送站内信</el-button
          >
        </div>
      </div>

      <div style="margin-top: 20px">
        <svg-icon icon-class="marker" style="color: #606266"></svg-icon>
        <span class="font-small">基本信息</span>
      </div>
      <div class="table-layout">
        <el-row>
          <el-col :span="8" class="table-cell-title">订单编号</el-col>
          <el-col :span="8" class="table-cell-title">提交时间</el-col>
          <el-col :span="8" class="table-cell-title">完成时间</el-col>
        </el-row>
        <el-row>
          <el-col :span="8" class="table-cell">{{ order.id }}</el-col>
          <el-col :span="8" class="table-cell">{{
            order.createTime | formatCreateTime
          }}</el-col>
          <el-col :span="8" class="table-cell">{{
            order.finishTime | formatCreateTime
          }}</el-col>
        </el-row>
        <el-row>
          <el-col :span="8" class="table-cell-title">用户ID</el-col>
          <el-col :span="8" class="table-cell-title">缴纳金</el-col>
          <el-col :span="8" class="table-cell-title">消费门店编号</el-col>
        </el-row>
        <el-row>
          <el-col :span="8" class="table-cell">{{ order.userId }}</el-col>
          <el-col :span="8" class="table-cell">{{ order.margin }}</el-col>
          <el-col :span="8" class="table-cell">{{
            order.storeId | formatStoreName(stores)
          }}</el-col>
        </el-row>
        <el-row>
          <el-col :span="8" class="table-cell-title">桌号</el-col>
          <el-col :span="8" class="table-cell-title">消费方式</el-col>
          <el-col :span="8" class="table-cell-title">口味</el-col>
        </el-row>
        <el-row>
          <el-col :span="8" class="table-cell">{{
            order.table == -1 ? "暂无数据" : order.table
          }}</el-col>
          <el-col :span="8" class="table-cell">{{
            order.consumeType | formatConsumeType
          }}</el-col>
          <el-col :span="8" class="table-cell">{{
            order.taste | formatTaste
          }}</el-col>
        </el-row>

        <el-row>
          <el-col :span="8" class="table-cell-title">备注</el-col>
          <el-col :span="8" class="table-cell-title">预约时间</el-col>
          <el-col :span="8" class="table-cell-title">支付方式</el-col>
        </el-row>
        <el-row>
          <el-col :span="8" class="table-cell">{{ order.remark }}</el-col>
          <el-col :span="8" class="table-cell">{{
            order.expectedTime | formatCreateTime
          }}</el-col>
          <el-col :span="8" class="table-cell">{{ order.payType }}</el-col>
        </el-row>
      </div>

      <div style="margin-top: 20px" v-if="order.address">
        <svg-icon icon-class="marker" style="color: #606266"></svg-icon>
        <span class="font-small">收货人信息</span>
      </div>
      <div class="table-layout" v-if="order.address">
        <el-row>
          <el-col :span="12" class="table-cell-title">收货人ID</el-col>
          <el-col :span="12" class="table-cell-title">收货人姓名</el-col>
        </el-row>
        <el-row>
          <el-col :span="12" class="table-cell">{{
            order.address.userId
          }}</el-col>
          <el-col :span="12" class="table-cell">{{
            order.address.phone
          }}</el-col>
        </el-row>
        <el-row>
          <el-col :span="12" class="table-cell-title">手机号码</el-col>
          <el-col :span="12" class="table-cell-title">收货地址</el-col>
        </el-row>
        <el-row>
          <el-col :span="12" class="table-cell">{{
            order.address.phone
          }}</el-col>
          <el-col :span="12" class="table-cell">{{
            order.address.address
          }}</el-col>
        </el-row>
      </div>

      <div style="margin-top: 20px">
        <svg-icon icon-class="marker" style="color: #606266"></svg-icon>
        <span class="font-small">菜品信息</span>
        <br />
        <span style="color: red; font-size: 13px">
          注：加菜意味着这是否是用户在店内用餐时所加
        </span>
      </div>
      <el-table
        ref="orderItemTable"
        :data="order.dishOrders"
        style="width: 100%; margin-top: 20px"
        border
      >
        <el-table-column label="菜品ID" width="120" align="center">
          <template slot-scope="scope">
            {{ scope.row.dishId }}
          </template>
        </el-table-column>
        <el-table-column label="菜品名称" align="center">
          <template slot-scope="scope">
            {{ scope.row.dishName }}
          </template>
        </el-table-column>
        <el-table-column label="菜品价格" width="120" align="center">
          <template slot-scope="scope">
            {{ scope.row.dishPrice }}
          </template>
        </el-table-column>
        <el-table-column label="选择数量" width="120" align="center">
          <template slot-scope="scope">
            {{ scope.row.dishNum }}
          </template>
        </el-table-column>
        <el-table-column label="是否为加菜" width="120" align="center" >
          <template slot-scope="scope">
            {{ scope.row.is_add ? "是" : "否" }}
          </template>
        </el-table-column>
        <el-table-column label="小计" width="120" align="center">
          <template slot-scope="scope">
            ￥{{ scope.row.dishNum * scope.row.dishPrice }}
          </template>
        </el-table-column>
      </el-table>
      <div style="float: right; margin: 20px">
        合计：<span class="color-danger">￥{{ order.originalPrice }}</span>
      </div>

      <div style="margin-top: 60px">
        <svg-icon icon-class="marker" style="color: #606266"></svg-icon>
        <span class="font-small">费用信息</span>
      </div>

      <div class="table-layout">
        <el-row>
          <el-col :span="4" class="table-cell-title">菜品合计</el-col>
          <el-col :span="4" class="table-cell-title">折扣优惠</el-col>
          <el-col :span="4" class="table-cell-title">使用优惠券</el-col>
          <el-col :span="4" class="table-cell-title">其他费用</el-col>
          <el-col :span="4" class="table-cell-title">已支付保证金</el-col>
          <el-col :span="4" class="table-cell-title">应/实付款金额</el-col>
        </el-row>
        <el-row>
          <el-col :span="4" class="table-cell"
            >￥{{ order.originalPrice }}</el-col
          >
          <el-col :span="4" class="table-cell"
            >-￥{{ order.shopDiscount }}</el-col
          >
          <el-col :span="4" class="table-cell"
            >-￥{{ order.couponDiscount }}</el-col
          >
          <el-col :span="4" class="table-cell">￥{{ order.otherFee }}</el-col>
          <el-col :span="4" class="table-cell">￥{{ order.margin }}</el-col>
          <el-col :span="4" class="table-cell">
            ￥{{
              order.otherFee -
              order.shopDiscount -
              order.couponDiscount +
              order.originalPrice - order.margin
            }}</el-col
          >
        </el-row>
      </div>
    </el-card>

    <el-dialog
      title="发送站内信"
      :visible.sync="messageDialogVisible"
      width="40%"
    >
      <el-form :model="message" ref="receiverInfoForm" label-width="150px">
        <el-form-item label="标题：">
          <el-input v-model="message.title" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="内容：">
          <el-input v-model="message.content" type="textarea" rows="3">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="messageDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSendMessage">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { getOrderDetail, sendMessage } from "@/api/order";
import VDistpicker from "v-distpicker";
import { formatDate } from "@/utils/date";
import { fetchList as fetchStore } from "@/api/store";
const defaultReceiverInfo = {
  orderId: null,
  receiverName: null,
  receiverPhone: null,
  receiverPostCode: null,
  receiverDetailAddress: null,
  receiverProvince: null,
  receiverCity: null,
  receiverRegion: null,
  status: null,
};
export default {
  name: "orderDetail",
  components: { VDistpicker },
  data() {
    return {
      stores: [],
      id: null,
      order: {},
      messageDialogVisible: false,
      message: { title: null, content: null },
      closeDialogVisible: false,
      closeInfo: { note: null, id: null },
      logisticsDialogVisible: false,
    };
  },
  created() {
    this.id = this.list = this.$route.query.id;
    getOrderDetail(this.id).then((response) => {
      console.log("orderDetail", response.data);
      this.order = response.data;
    });
    fetchStore({ pageNum: 1, pageSize: -1 }).then((res) => {
      this.stores = res.data.arrays;
    });
  },
  filters: {
    formatTaste(v) {
      let map = ["不辣", "微辣", "中辣", "辣"];
      return map[v];
    },
    formatStoreName(id, stores) {
      console.log("id==", id, "stors ==", stores);
      for (let i = 0; i < stores.length; i++) {
        if (stores[i].id == id) {
          return stores[i].name;
        }
      }
    },
    formatCreateTime(time) {
      if (!time) {
        return "暂无数据";
      }
      let date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
    formatOrderType(value) {
      const map = [
        "待支付保证金",
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
    getStatus() {
      const map = [
        "待支付保证金",
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
    showMessageDialog() {
      this.messageDialogVisible = true;
      this.message.title = null;
      this.message.content = null;
    },
    handleSendMessage() {
      this.$confirm("是否要发送站内信?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        //发送目标用户
        this.message.userId = this.order.userId;
        //发送人，设置为订单对应店铺管理员
        this.message.storeId = this.order.storeId;
        //创建时间
        this.message.createTime = new Date().getTime();
        //
        this.message.type = 1;
        sendMessage(this.message).then((res) => {
          this.$message({
            type: "success",
            message: "发送成功!",
          });
        });
        this.messageDialogVisible = false;
      });
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
</style>


