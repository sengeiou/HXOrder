<template>
  <div class="detail-container">
    <el-card shadow="never">
      <span class="font-title-medium">取消订单信息</span>
      <el-table
        border
        class="standard-margin"
        ref="dishTable"
        :data="order.dishOrders"
      >
        <el-table-column label="菜品编号" align="center">
          <template slot-scope="scope">
            <span class="font-small">{{ scope.row.dishId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="菜品名称" align="center">
          <template slot-scope="scope">
            <span class="font-small">{{ scope.row.dishName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="菜品单价" align="center">
          <template slot-scope="scope">
            <span class="font-small">￥{{ scope.row.dishPrice }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数量" align="center">
          <template slot-scope="scope">{{ scope.row.dishNum }}</template>
        </el-table-column>
        <el-table-column label="小计" align="center">
          <template slot-scope="scope"
            >￥{{ scope.row.dishNum * scope.row.dishPrice }}</template
          >
        </el-table-column>
      </el-table>
      <div style="float: right; margin-top: 15px; margin-bottom: 15px">
        <span class="font-title-medium">合计：</span>
        <span class="font-title-medium color-danger"
          >￥{{ order.originalPrice }}</span
        >
      </div>
    </el-card>

    <el-card shadow="never" class="standard-margin">
      <span class="font-title-medium">服务单信息</span>
      <div class="form-container-border">
        <el-row>
          <el-col :span="6" class="form-border form-left-bg font-small"
            >订单编号</el-col
          >
          <el-col class="form-border font-small" :span="18">{{
            order.id
          }}</el-col>
        </el-row>

        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6"
            >申请状态</el-col
          >
          <el-col class="form-border font-small" :span="18">正在申请</el-col>
        </el-row>

        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6"
            >申请时间</el-col
          >
          <el-col class="form-border font-small" :span="18">{{
            order.createTime | formatTime
          }}</el-col>
        </el-row>

        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6"
            >申请人ID</el-col
          >
          <el-col class="form-border font-small" :span="18">{{
            order.applyTable.userId
          }}</el-col>
        </el-row>

        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6"
            >联系电话</el-col
          >
          <el-col class="form-border font-small" :span="18">{{
            order.applyTable.phone
          }}</el-col>
        </el-row>

        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6"
            >退货原因</el-col
          >
          <el-col class="form-border font-small" :span="18">{{
            order.applyTable.reason
          }}</el-col>
        </el-row>
      </div>

      <div class="form-container-border">
        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6"
            >订单总额</el-col
          >
          <el-col class="form-border font-small" :span="18"
            >￥{{ order.originalPrice }}</el-col
          >
        </el-row>

        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6"
            >店铺折扣</el-col
          >
          <el-col class="form-border font-small" :span="18"
            >￥{{ order.shopDiscount }}</el-col
          >
        </el-row>

        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6"
            >使用优惠券</el-col
          >
          <el-col class="form-border font-small" :span="18"
            >￥{{ order.couponDiscount }}</el-col
          >
        </el-row>

        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6"
            >已支付保证金</el-col
          >
          <el-col class="form-border font-small" :span="18"
            >￥{{ order.margin }}</el-col
          >
        </el-row>

        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6"
            >其他费用</el-col
          >
          <el-col class="form-border font-small" :span="18"
            >￥{{ order.otherFee }}</el-col
          >
        </el-row>

        <el-row>
          <el-col class="form-border form-left-bg font-small" :span="6"
            >实际支付</el-col
          >
          <el-col class="form-border font-small" :span="18"
            >￥{{
              order.auctualPay
            }}</el-col
          >
        </el-row>

        <el-row>
          <el-col
            class="form-border form-left-bg font-small"
            :span="6"
            style="height: 52px; line-height: 32px"
          >
            确认退款金额
          </el-col>
          <el-col
            class="form-border font-small"
            style="height: 52px"
            :span="18"
          >
            ￥
            <el-input
              size="small"
              style="width: 200px; margin-left: 10px"
              disabled
              :placeholder="
                order.auctualPay
              "
            >
            </el-input>
          </el-col>
        </el-row>
      </div>

      <div style="margin-top: 15px; text-align: center">
        <el-button type="primary" size="small" @click="handleY"
          >同意该申请</el-button
        >
        <el-button
          type="danger"
          size="small"
          @click="messageDialogVisible = true"
          >拒绝该申请</el-button
        >
      </div>
    </el-card>

    <el-dialog
      title="填写拒绝理由"
      :visible.sync="messageDialogVisible"
      width="40%"
    >
      <el-form :model="message" ref="receiverInfoForm" label-width="150px">
        <el-form-item label="标题：">
          <el-input
            v-model="message.title"
            style="width: 200px"
            disabled
          ></el-input>
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
import { getOrderDetail, updateOrderType, sendMessage } from "@/api/order";
import { formatDate } from "@/utils/date";

const defaultUpdateStatusParam = {};
const defaultorder = {
  receiveNote: null,
};
export default {
  name: "handleCancelOrder",
  data() {
    return {
      message: {
        title: "拒绝退货申请通知",
        content: null,
        type: 1,
        userId: null,
        storeId: null,
      },
      id: null,
      order: Object.assign({}, defaultorder),
      dishList: null,
      messageDialogVisible: false,
    };
  },
  created() {
    this.id = this.$route.query.id;
    this.getDetail(this.id);
  },
  computed: {
    totalAmount() {
      if (this.order != null) {
        return this.order.dishRealPrice * this.order.dishCount;
      } else {
        return 0;
      }
    },
  },
  filters: {
    formatTime(time) {
      if (time == null || time === "") {
        return "暂无数据";
      }
      let date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
  },
  methods: {
    handleSendMessage() {
      this.message.userId = this.order.userId;
      this.message.storeId = this.order.storeId;
      //先恢复订单原本状态
      updateOrderType(
        this.order.id,
        -1,
        this.order.applyTable.oldOrderType
      ).then((res) => {
        //成功后发送站内信
        this.$message({
          type: "success",
          message: "操作成功!",
        });
        sendMessage(this.message)
          .then((res) => {
            this.$router.back();
          })
          .catch((e) => {
            this.$router.back();
          });
      });
    },
    getDetail(id) {
      getOrderDetail(id).then((res) => {
        console.log("order == ", res.data);
        this.order = res.data;
        this.order.auctualPay = this.getAuctualPay(this.order);
      });
    },
    getAuctualPay(order) {
      let ap =
        order.otherFee +
        order.originalPrice -
        order.shopDiscount -
        order.couponDiscount;
      console.log("app", ap);
      let cp = order.consumeType,  ps = order.paymentStatus;
      if (cp == 1) {
        return order.margin;
      } else {
        return ps ? ap : 0;
      }
    },
    handleViewOrder() {
      this.$router.push({
        path: "/order-ms/orderDetail",
        query: { id: this.order.orderId },
      });
    },
    handleY() {
      let o = this.order;
      this.$confirm("是否要确认?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        updateOrderType(o.id, o.consumeType, o.orderType).then((res) => {
          this.$message({
            type: "success",
            message: "操作成功!",
          });
          this.$router.back();
        });
      });
    },
  },
};
</script>
<style scoped>
.detail-container {
  width: 1080px;
  padding: 35px 35px 15px 35px;
  margin: 20px auto;
}

.standard-margin {
  margin-top: 15px;
}
.form-border {
  border-right: 1px solid #dcdfe6;
  border-bottom: 1px solid #dcdfe6;
  padding: 10px;
}

.form-container-border {
  border-left: 1px solid #dcdfe6;
  border-top: 1px solid #dcdfe6;
  margin-top: 15px;
}

.form-left-bg {
  background: #f2f6fc;
}
</style>


