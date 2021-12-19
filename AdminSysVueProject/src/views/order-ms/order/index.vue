

<template>
   
  <div class="app-container">
    <div class="select_shop">
      <template>
        <el-select
          @change="shopChange"
          v-model="listQuery.storeId"
          placeholder="全部门店"
        >
          <el-option
            v-for="item in options"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
            {{ item.name }}
          </el-option>
        </el-select>
      </template>
    </div>

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
          搜索
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
          <el-form-item label="输入搜索：">
            <el-input
              v-model="listQuery.keyword"
              class="input-width"
              placeholder="订单编号"
            ></el-input>
          </el-form-item>
          <el-form-item label="提交时间：">
            <el-date-picker
              class="input-width"
              v-model="listQuery.createTime"
              value-format="yyyy-MM-dd"
              format="yyyy-MM-dd"
              type="date"
              placeholder="请选择时间"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="订单状态：">
            <el-select
              v-model="listQuery.orderType"
              class="input-width"
              placeholder="全部"
              clearable
            >
              <el-option
                v-for="item in orderTypeOptions"
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
      <span>订单列表</span>
    </el-card>

    <div class="table-container">
      <el-table
        ref="orderTable"
        :data="list"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="listLoading"
        border
        :default-sort = "{prop: 'id', order: 'ascending'}"
      >
        <el-table-column
          type="selection"
          width="60"
          align="center"
        ></el-table-column>

        <el-table-column label="订单编号" width="100" align="center" prop="id" sortable>
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>

        <el-table-column label="提交时间" width="150" align="center" prop="date" sortable>
          <template slot-scope="scope">{{
            scope.row.createTime | formatCreateTime
          }}</template>
        </el-table-column>

        <el-table-column label="用户ID" align="center" sortable prop="userId">
          <template slot-scope="scope">{{ scope.row.userId }}</template>
        </el-table-column>

        <el-table-column label="消费门店 / 桌号" width="80" align="center">
          <template slot-scope="scope"
            >{{ scope.row.storeId | formatStoreName(options) }}/
            {{ scope.row.table + "桌" }}
          </template>
        </el-table-column>

        <el-table-column label="消费方式" width="80" align="center">
          <template slot-scope="scope"
            >{{ scope.row.consumeType | formatConsumeType }}
          </template>
        </el-table-column>

        <el-table-column label="待支付/支付金额(实)" width="120" align="center" prop="qian" sortable>
          <template slot-scope="scope"
            >￥{{ scope.row.otherFee +
                  scope.row.originalPrice -
                  scope.row.shopDiscount -
                  scope.row.couponDiscount }}</template
          >
        </el-table-column>

        <el-table-column label="取餐号" align="center" sortable>
          <template slot-scope="scope">{{
            scope.row.fetchMealCode ? scope.row.fetchMealCode : "无"
          }}</template>
        </el-table-column>

        <el-table-column label="订单状态" width="120" align="center">
          <template slot-scope="scope">{{
            scope.row.orderType | formatOrderType
          }}</template>
        </el-table-column>

        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <p>
              <el-button
                size="mini"
                @click="handleViewOrder(scope.$index, scope.row)"
                >查看详情</el-button
              >
            </p>
            <!-- 0待点餐(保留以后使用), 1待支付, 2确认中，3备餐中，4待用餐，5待取餐，
6配送中，7已完成，8取消确认中，9已取消 -->
            <p>
              <el-button
                size="mini"
                type="primary"
                @click="handleOrderTypeEvent1(scope.$index, scope.row)"
                v-show="scope.row.orderType === 1&&scope.row.consumeType<2"
                >处理订单</el-button
              >
            </p>

            <p>
              <el-button
                size="mini"
                type="primary"
                @click="handleOrderTypeChange(scope.$index, scope.row)"
                v-show="scope.row.orderType === 2"
                >确认订单</el-button
              >
            </p>

            <p>
              <el-button
                size="mini"
                type="danger"
                @click="handleOrderTypeEvent2B(scope.$index, scope.row)"
                v-show="scope.row.orderType === 2"
                >拒绝服务</el-button
              >
            </p>

            <p>
              <el-button
                size="mini"
                type="primary"
                @click="handleOrderTypeChange(scope.$index, scope.row)"
                v-show="scope.row.orderType === 3"
                >备餐完成</el-button
              >
            </p>
            <p>
              <el-button
                size="mini"
                type="primary"
                @click="handleOrderTypeChange(scope.$index, scope.row)"
                v-show="scope.row.orderType === 4"
                >人已到店</el-button
              >
            </p>
            <p>
              <el-button
                size="mini"
                type="primary"
                @click="handleOrderTypeChange(scope.$index, scope.row)"
                v-show="scope.row.orderType === 5"
                >取餐成功</el-button
              >
            </p>

            <p>
              <el-button
                size="mini"
                type="primary"
                @click="handleOrderTypeChange(scope.$index, scope.row)"
                v-show="scope.row.orderType === 6"
                >配送完毕</el-button
              >
            </p>
            <p>
              <el-button
                size="mini"
                type="primary"
                @click="handleOrderTypeEvent8(scope.$index, scope.row)"
                v-show="scope.row.orderType === 8"
                >处理订单</el-button
              >
            </p>
            <p>
              <el-button
                size="mini"
                type="warning"
                @click="handleDeleteOrder(scope.$index, scope.row)"
                >删除记录</el-button
              >
            </p>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="batch-operate-container">
      <el-select size="small" v-model="operateType" placeholder="批量操作">
        <el-option
          v-for="item in operateOptions"
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
        :current-page.sync="listQuery.pageNum"
        :page-size="listQuery.pageSize"
        :page-sizes="[5, 10, 20, 40, 80]"
        :total="total"
      >
      </el-pagination>
    </div>

    <el-dialog
      title="拒绝订单"
      :visible.sync="rejectConfirmDialogVisible"
      width="40%"
    >
      <el-form :model="rejectReason" label-width="150px">
        <el-form-item label="拒绝理由：" label-width="100px">
          <el-input v-model="rejectReason" type="textarea" rows="5"> </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="rejectConfirmDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleRejectConfirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import {
  fetchList,
  deleteOrder,
  sendMessage,
  updateOrderType,
  rejectOrder,
} from "@/api/order";
import { fetchList as fetchStore } from "@/api/store";
import { formatDate } from "@/utils/date";
const defaultListQuery = {
  pageNum: 1,
  pageSize: 10,
  id: null,
  orderType: null,
  createTime: null,
  // -1 代表全部门店
  storeId: -1,
};
export default {
  name: "orderList",
  data() {
    return {
      rejectRow: null,
      rejectReason: null,
      rejectConfirmDialogVisible: false,
      options: [],
      value: "",
      listQuery: Object.assign({}, defaultListQuery),
      listLoading: true,
      list: null,
      total: null,
      operateType: null,
      multipleSelection: [],
      closeOrder: {
        dialogVisible: false,
        content: null,
        orderIds: [],
      },
      orderTypeOptions: [
        {
          label: "待点餐",
          value: 0,
        },
        {
          label: "待支付",
          value: 1,
        },
        {
          label: "确认中",
          value: 2,
        },
        {
          label: "备餐中",
          value: 3,
        },
        {
          label: "待用餐",
          value: 4,
        },
        {
          label: "待取餐",
          value: 5,
        },
        {
          label: "配送中",
          value: 6,
        },
        {
          label: "已完成",
          value: 7,
        },
        {
          label: "取消中",
          value: 8,
        },
        {
          label: "已取消",
          value: 9,
        },
      ],
      operateOptions: [
        {
          label: "删除订单",
          value: 0,
        },
      ],
      logisticsDialogVisible: false,
    };
  },
  created(e) {
    console.log("进入 order-index，路由是: ", this.$route.path);
    let routeMap = [
      "/order-ms/order",
      "/order-ms/toBePaidOrderList",
      "/order-ms/toBeConfirmedOrderList",
      "/order-ms/inDistributionOrderList",
      "/order-ms/mealWaitingOrderList",
      "/order-ms/ealPreparationOrderList",
      "/order-ms/toBeDinedOrderList",
      "/order-ms/cancelOrderList",
    ];
    //索引 0 是 订单列表，不指定查询、索引 1 是 待支付订单, orderType = 1
    // 索引 3 是配送中订单，orderType = 6，以此类推
    let map = [null, 1, 2, 6, 5, 3, 4, 8]
    for (let i = 0; i < routeMap.length; i++) {
      if (routeMap[i] == this.$route.path) {
        this.listQuery.orderType = map[i];
        break;
      }
    }
    this.getList();
    fetchStore({ pageNum: 1, pageSize: -1 }).then((res) => {
      this.options = res.data.arrays;
      this.options.push({
        name: "全部门店",
        id: -1,
      });
    });
  },
  filters: {
    formatStoreName(id, stores) {
      console.log("id==", id, "stors ==", stores);
      for (let i = 0; i < stores.length; i++) {
        if (stores[i].id == id) {
          return stores[i].name;
        }
      }
    },
    formatCreateTime(time) {
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
    handleRejectConfirm() {
      this.$confirm("是否要进行该操作?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        let order = this.rejectRow;
        console.log("order == ", order);
        rejectOrder(
          order.id,
          order.userId,
          order.storeId,
          this.rejectReason
        ).then((res) => {
          this.rejectRow = null;
          this.rejectReason = null;
          this.rejectConfirmDialogVisible = false;
          this.getList();
          this.$message({
            message: "操作成功！",
            type: "success",
            duration: 1000,
          });
        });
      });
    },
    shopChange(v) {
      console.log(v);
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
    handleViewOrder(index, row) {
      this.$router.push({
        path: "/order-ms/orderDetail",
        query: { id: row.id },
      });
    },
    // <!-- 0待点餐(保留以后使用), 1待支付, 2确认中，3备餐中，4待用餐，5待取餐，
    // 6配送中，7已完成，8取消确认中，9已取消 -->
    handleOrderTypeEvent1(index, row) {
      this.$router.push({
        path: "/order-ms/handleToBePaidOrder",
        query: { id: row.id },
      });
    },
    //确认
    handleOrderTypeChange(index, row) {
      console.log("rororo = ", row);
      this.$confirm("是否要进行该操作?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        updateOrderType(row.id, row.consumeType, row.orderType).then((res) => {
          this.$message({
            message: "操作成功！",
            type: "success",
            duration: 1000,
          });
          this.getList();
        });
      });
    },
    //拒绝服务
    handleOrderTypeEvent2B(index, row) {
      this.rejectRow = row;
      this.rejectReason = null;
      this.rejectConfirmDialogVisible = true;
    },
    handleOrderTypeEvent8(idex, row) {
      this.$router.push({
        path: "/order-ms/handleCancelOrder",
        query: { id: row.id },
      });
    },

    handleDeleteOrder(index, row) {
      let ids = [];
      ids.push(row.id);
      this.deleteOrder(ids);
    },
    handleBatchOperate() {
      if (this.multipleSelection == null || this.multipleSelection.length < 1) {
        this.$message({
          message: "请选择要操作的订单",
          type: "warning",
          duration: 1000,
        });
        return;
      }
      if (this.operateType === 0) {
        //删除订单
        let ids = [];
        for (let i = 0; i < this.multipleSelection.length; i++) {
          ids.push(this.multipleSelection[i].id);
        }
        console.log("ooo", ids);
        this.deleteOrder(ids);
      }
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
    getList() {
      this.listLoading = true;
      fetchList(this.listQuery).then((response) => {
        console.log("total = ", response.data.total);
        this.listLoading = false;
        this.list = response.data.arrays;
        this.total = response.data.total;
      });
    },
    deleteOrder(ids) {
      this.$confirm(
        "是否要进行该删除操作? 删除订单不会影响首页的总览数据与客户端中数据",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).then(() => {
        let params = new URLSearchParams();
        params.append("ids", ids);
        console.log(params, JSON.stringify(params), ids, JSON.stringify(ids));
        deleteOrder(params).then((response) => {
          this.$message({
            message: "删除成功！",
            type: "success",
            duration: 1000,
          });
          this.getList();
        });
      });
    },
  },
};
</script>
<style scoped>
.filter-container {
  margin-top: 15px;
}
.input-width {
  width: 203px;
}
</style>


