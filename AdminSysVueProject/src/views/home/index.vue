<template>
  <div class="app-container">
    <div class="select_shop">
      <template>
        <el-select
          @change="shopChange"
          v-model="queryList.storeId"
          placeholder="全部门店"
        >
          <el-option
            v-for="item in options"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </template>
    </div>

    <!-- 总览 -->
    <div class="total-layout">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="total-frame">
            <img :src="img_home_order" class="total-icon" />
            <div class="total-title">今日订单总数</div>
            <div class="total-value">{{data.todayOrderCount}}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="total-frame">
            <img :src="img_home_today_amount" class="total-icon" />
            <div class="total-title">今日销售总额</div>
            <div class="total-value">￥{{data.todayAmount}}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="total-frame">
            <svg-icon icon-class="total-week" class="total-icon"> </svg-icon>
            <div class="total-title">近7天销售总额</div>
            <div class="total-value">￥{{data.lastWeekAmount}}</div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="un-handle-layout">
      <div class="layout-title">当前订单状态</div>

      <!-- //0待支付保证金, 1待支付, 2确认中，3备餐中，4待用餐，5待取餐，6配送中，7已完成，8取消确认中 -->
      <div class="un-handle-content">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">待支付订单<a href="#">(处理)</a></span>
              <span style="float: right" class="color-danger">{{data.orderNum['1']}}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">确认中订单<a href="#">(处理)</a></span>
              <span style="float: right" class="color-danger">{{data.orderNum['2']}}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">备餐中订单<a href="#">(处理)</a></span>
              <span style="float: right" class="color-danger">{{data.orderNum['3']}}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">待取餐订单<a href="#">(处理)</a></span>
              <span style="float: right" class="color-danger">{{data.orderNum['5']}}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">配送中订单<a href="#">(处理)</a></span>
              <span style="float: right" class="color-danger">{{data.orderNum['6']}}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">申请取消中<a href="#">(处理)</a></span>
              <span style="float: right" class="color-danger">{{data.orderNum['8']}}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">待用餐订单</span>
              <span style="float: right" class="color-danger">{{data.orderNum['4']}}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">已取消订单</span>
              <span style="float: right" class="color-danger">{{data.orderNum['9']}}</span>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">已完成订单</span>
              <span style="float: right" class="color-danger">{{data.orderNum['7']}}</span>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div class="overview-layout">
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="out-border">
            <div class="layout-title">商品总览</div>
            <div style="padding: 40px">
              <el-row>
                <el-col :span="12" class="color-danger overview-item-value"
                  >100</el-col 
                >
                <el-col :span="12" class="color-danger overview-item-value"
                  >500</el-col
                >
              </el-row>
              <el-row class="font-medium">
                <el-col :span="12" class="overview-item-title">已上架</el-col>
                <el-col :span="12" class="overview-item-title">全部</el-col>
              </el-row>
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="out-border">
            <div class="layout-title">用户总览</div>
            <div style="padding: 40px">
              <el-row>
                <el-col :span="12" class="color-danger overview-item-value"
                  >100</el-col
                >
                <el-col :span="12" class="color-danger overview-item-value"
                  >200</el-col
                >
              </el-row>
              <el-row class="font-medium">
                <el-col :span="12" class="overview-item-title">昨日新增</el-col>
                <el-col :span="12" class="overview-item-title">用户总数</el-col>
              </el-row>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="statistics-layout">
      <div class="layout-title">订单统计</div>
      <el-row>
        <el-col :span="4">
          <div style="padding: 20px">
            <div>
              <div style="color: #909399; font-size: 14px">本月订单总数</div>
              <div style="color: #606266; font-size: 24px; padding: 10px 0">
                10000
              </div>
              <div>
                <span style="color: #c0c4cc; font-size: 14px">同比上月</span>
                <span class="color-success" style="font-size: 14px"> +10%</span>     
              </div>
            </div>
            <div style="margin-top: 20px">
              <div style="color: #909399; font-size: 14px">本周订单总数</div>
              <div style="color: #606266; font-size: 24px; padding: 10px 0">
                1000
              </div>
              <div>
                <span style="color: #c0c4cc; font-size: 14px">同比上周</span>
                <span class="color-danger" style="font-size: 14px"> -10%</span>    
              </div>
            </div>
            <div style="margin-top: 20px">
              <div style="color: #909399; font-size: 14px">本月销售总额</div>
              <div style="color: #606266; font-size: 24px; padding: 10px 0">
                100000
              </div>
              <div>
                <span style="color: #c0c4cc; font-size: 14px">同比上月</span>
                <span class="color-success" style="font-size: 14px"> +10%</span>
              </div>
            </div>
            <div style="margin-top: 20px">
              <div style="color: #909399; font-size: 14px">本周销售总额</div>
              <div style="color: #606266; font-size: 24px; padding: 10px 0">
                50000
              </div>
              <div>
                <span style="color: #c0c4cc; font-size: 14px">同比上周</span>
                <span class="color-danger" style="font-size: 14px"> -10%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="20">
          <div style="padding: 10px; border-left: 1px solid #dcdfe6">
            <el-date-picker
              style="float: right; z-index: 1"
              size="small"
              v-model="orderCountDate"
              type="daterange"
              align="right"
              unlink-panels
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              @change="handleDateChange"
              :picker-options="pickerOptions"
            >
            </el-date-picker>
            <div>
              <ve-line
                :data="chartData"
                :legend-visible="false"
                :loading="loading"
                :data-empty="dataEmpty"
                :settings="chartSettings"
              ></ve-line>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import {getHomeData as getData} from "@/api/home";
import { str2Date } from "@/utils/date";
import img_home_order from "@/assets/images/home_order.png";
import img_home_today_amount from "@/assets/images/home_today_amount.png";
import img_home_yesterday_amount from "@/assets/images/home_yesterday_amount.png";
import {fetchList as fetchStoreList} from "@/api/store"
const DATA_FROM_BACKEND = {
  columns: ["date", "orderCount", "orderAmount"],
  rows: [
    { date: "2021-11-01", orderCount: 310, orderAmount: 109 },
    { date: "2021-11-02", orderCount: 210, orderAmount: 2093 },
    { date: "2021-11-03", orderCount: 120, orderAmount: 3093 },
    { date: "2021-12-04", orderCount: 410, orderAmount: 4093 },
    { date: "2021-11-05", orderCount: 510, orderAmount: 15093 },
    { date: "2021-12-06", orderCount: 10, orderAmount: 6093 },
  ],
};

const defaultQueryList = {
  storeId: -1,
  start: 1,
  end: 1,
};
export default {
  name: "home",
  data() {
    return {
      data: {
        todayOrderCount: null,
      },
      options: [
      ],
      value: "",
      pickerOptions: {
        shortcuts: [
          {
            text: "查看本月",
            onClick(picker) {
              const end = new Date();
              let start = new Date();
              start.setDate(0);
              picker.$emit("pick", [start, end]);
            },
          },
                    {
            text: "最近一月",
            onClick(picker) {
              const end = new Date();
              let start = new Date();
              if (start.getMonth() != 1){
                start.setMonth(start.getMonth() - 1);
              } else {
                start.setFullYear(start.getFullYear() - 1);
                start.setMonth(12);
              }
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近一年",
            onClick(picker) {
              const end = new Date();
              let start = new Date();
              start.setFullYear(start.getFullYear() - 1);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
      queryList: Object.assign({}, defaultQueryList),
      orderCountDate: "",
      chartSettings: {
        xAxisType: "time",
        area: true,
        axisSite: { right: ["orderAmount"] },
        labelMap: { orderCount: "订单数量", orderAmount: "订单金额" },
      },
      chartData: {
        columns: [],
        rows: [],
      },
      loading: false,
      dataEmpty: false,
      img_home_order,
      img_home_today_amount,
      img_home_yesterday_amount,
    };
  },
  created() {
    this.initOrderCountDate();
    this.getData();
    this.getStore();
    this.getHomeData();
  },
  methods: {
    getStore() {
      fetchStoreList({pageNum: 1,pageSize:-1}).then((res)=> {
        console.log("yyds",res);
        this.options = res.data.arrays;
        this.options.push({
          id: -1,
          name: "全部门店"
        })
      })
    },
    shopChange(v) {
      console.log(v);
      this.getHomeData();
    },
    handleDateChange() {
      this.getData();
    },
    getHomeData() {
      getData(this.queryList).then(res=> {
        this.data = res.data;
        console.log("daaewa" + this.data,
        this.data.todayOrderCount);
      })
    },
    initOrderCountDate() {
      let start = new Date();
      start.setDate(0);
      let end = new Date();
      this.queryList.start = start;
      this.queryList.end = end;
      this.orderCountDate = [start, end];
    },
    getData() {
      setTimeout(() => {
        this.chartData = {
          columns: ["date", "orderCount", "orderAmount"],
          rows: [],
        };
        for (let i = 0; i < DATA_FROM_BACKEND.rows.length; i++) {
          let item = DATA_FROM_BACKEND.rows[i];
          let currDate = str2Date(item.date);
          let start = this.orderCountDate[0];
          let end = this.orderCountDate[1];
          if (
            currDate.getTime() >= start.getTime() &&
            currDate.getTime() <= end.getTime()
          ) {
            this.chartData.rows.push(item);
          }
        }
        this.dataEmpty = false;
        this.loading = false;
      }, 1000);
    },
  },
};
</script>

<style scoped>
a {
  color: blue;
  text-decoration: blue;
}
a:link {
  font-size: 15px;
  color: blue;
  text-decoration: none;
}

a:hover {
  font-size: 15px;
  color: #999999;
  text-decoration: underline;
}

.app-container {
  margin-top: 40px;
  margin-left: 120px;
  margin-right: 120px;
}

.total-layout {
  margin-top: 20px;
}

.total-frame {
  border: 1px solid #dcdfe6;
  padding: 20px;
  height: 100px;
}

.total-icon {
  color: #409eff;
  width: 45px;
  height: 45px;
}

.total-title {
  position: relative;
  font-size: 12px;
  color: #909399;
  left: 50px;
  top: -40px;
}

.total-value {
  position: relative;
  font-size: 14px;
  color: #606266;
  left: 50px;
  top: -35px;
}

.un-handle-layout {
  margin-top: 20px;
  border: 1px solid #dcdfe6;
}

.layout-title {
  color: #606266;
  padding: 15px 20px;
  background: #f2f6fc;
  font-weight: bold;
}

.un-handle-content {
  padding: 20px 40px;
}

.un-handle-item {
  border-bottom: 1px solid #ebeef5;
  padding: 10px;
}

.overview-layout {
  margin-top: 20px;
}

.overview-item-value {
  font-size: 24px;
  text-align: center;
}

.overview-item-title {
  margin-top: 10px;
  text-align: center;
}

.out-border {
  border: 1px solid #dcdfe6;
}

.statistics-layout {
  margin-top: 20px;
  border: 1px solid #dcdfe6;
}
.mine-layout {
  position: absolute;
  right: 140px;
  top: 107px;
  width: 250px;
  height: 235px;
}
.address-content {
  padding: 20px;
  font-size: 18px;
}

.select_shop {
  width: 185px;
}
</style>
