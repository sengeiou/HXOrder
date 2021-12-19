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
          size="small">
          查询
        </el-button>
        <el-button
          style="float: right;margin-right: 15px"
          @click="handleResetSearch()"
          size="small">
          重置
        </el-button>
      </div>

      <div style="margin-top: 15px">
        <el-form :inline="true" :model="listQuery" size="small" label-width="140px">

          <el-form-item label="菜品名称：">
            <el-input style="width: 203px" v-model="listQuery.keyword" placeholder="菜品名称"></el-input>
          </el-form-item>

          <el-form-item label="上架状态：">
            <el-select v-model="listQuery.publishStatus" placeholder="全部" clearable>
              <el-option
                v-for="item in publishStatusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="推荐状态：">
            <el-select v-model="listQuery.recommendStatus" placeholder="全部" clearable>
              <el-option
                v-for="item in recommendStatusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="新品状态：">
            <el-select v-model="listQuery.newStatus" placeholder="全部" clearable>
              <el-option
                v-for="item in newStatusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>

        </el-form>
      </div>
    </el-card>

    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button
        class="btn-add"
        @click="handleAddProduct()"
        size="mini">
        添加菜品
      </el-button>
    </el-card>

    <!-- 展示部分 -->
    <!-- 对象数组是 :data list，这是 El元素的 table  -->
    <div class="table-container">
      <el-table ref="productTable"
                :data="list"
                style="width: 100%"
                @selection-change="handleSelectionChange"
                v-loading="listLoading"
                :default-sort = "{prop: 'id', order: 'ascending'}"
                border>
        <el-table-column type="selection" width="60" align="center"></el-table-column>

        <el-table-column label="编号" width="100" align="center" prop="id" sortable>
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>

        <el-table-column label="菜品图片" width="120" align="center">
          <template slot-scope="scope"><img style="height: 80px" :src="scope.row.dishImg"></template>
        </el-table-column>

        <el-table-column label="菜品名称" align="center">
          <template slot-scope="scope">{{scope.row.name}}</template>
        </el-table-column>

        <el-table-column label="价格" sortable="" width="120" align="center">
          <template slot-scope="scope">价格：￥{{scope.row.price}}</template>
        </el-table-column>

        <el-table-column label="标签" width="140" align="center">
          <template slot-scope="scope">
              <el-switch
                @change="handlePublishStatusChange(scope.$index, scope.row)"
                active-text="上架"
                :active-value="1"
                :inactive-value="0"
                v-model="scope.row.publishStatus">
              </el-switch>
              <el-switch
                @change="handleRecommendStatusChange(scope.$index, scope.row)"
                active-text="推荐"
                :active-value="1"
                :inactive-value="0"
                v-model="scope.row.recommendStatus">
              </el-switch>
              <el-switch
                @change="handleNewStatusChange(scope.$index, scope.row)"
                active-text="新品"
                :active-value="1"
                :inactive-value="0"
                v-model="scope.row.newStatus">
              </el-switch>
          </template>
        </el-table-column>

        <el-table-column label="店铺库存" width="100" align="center">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" @click="handleShowStoreStockDialog(scope.$index, scope.row)" circle></el-button>
          </template>
        </el-table-column>

        <el-table-column sortable label="销量" width="100" align="center" >
          <template slot-scope="scope">{{scope.row.sale}}</template>
        </el-table-column>
        
        <el-table-column label="操作" width="160" align="center">
          <template slot-scope="scope">
            <p>
              <el-button
                size="mini"
                @click="handleUpdateDish(scope.$index, scope.row)">编辑具体信息
              </el-button>
            </p>
            <p>
              <el-button
                size="mini"
                type="danger"
                @click="handleDeleteDish(scope.$index, scope.row)">删除菜品记录
              </el-button>
            </p>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="batch-operate-container">
      <el-select
        size="small"
        v-model="operateType" placeholder="批量操作">
        <el-option
          v-for="item in operates"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-button
        style="margin-left: 20px"
        class="search-button"
        @click="handleBatchOperate()"
        type="primary"
        size="small">
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
        :page-sizes="[5,10,20,40,80]"
        :current-page.sync="listQuery.pageNum"
        :total="total">
      </el-pagination>
    </div>

    <el-dialog
      title="编辑店铺库存"
      :visible.sync="storeStock.dialogVisible"
      v-loading="dialogLoading"
      width="40%">
      <span>菜品编号：</span>
      <span>{{storeStock.dishId}}</span>
      <el-table style="width: 100%;margin-top: 20px"
                :data="storeStock.storeStockList"
                border>
        <el-table-column
          label="店铺编号"
          align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.storeId" disabled></el-input>
          </template>
        </el-table-column>
        <el-table-column
          label="店铺名称"
          align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.storeName" disabled></el-input>
          </template>
        </el-table-column>
        <el-table-column
          label="菜品库存"
          align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.stock"></el-input>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="storeStock.dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleEditStoreStockConfirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>





<script>
  import {
    fetchList,
    updateDeleteStatus,
    updateNewStatus,
    updateRecommendStatus,
    updatePublishStatus,
    getDishStock,
    updateDishStock,
  } from '@/api/dish'

  const defaultListQuery = {
    keyword: null,
    pageNum: 1,
    pageSize: 10,
    publishStatus: null,
    recommendStatus: null,
    newStatus: null
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
            label: "菜品上架",
            value: "publishOn"
          },
          {
            label: "菜品下架",
            value: "publishOff"
          },
          {
            label: "设为推荐",
            value: "recommendOn"
          },
          {
            label: "取消推荐",
            value: "recommendOff"
          },
          {
            label: "设为新品",
            value: "newOn"
          },
          {
            label: "取消新品",
            value: "newOff"
          },
          // {
          //   label: "一键删除",
          //   value: "recycle"
          // }
        ],
        operateType: null,
        listQuery: Object.assign({}, defaultListQuery),
        list: null,
        total: null,
        dialogLoading: false,
        listLoading: true,
        selectProductCateValue: null,
        multipleSelection: [],
        publishStatusOptions: [{
          value: 1,
          label: '上架'
        }, {
          value: 0,
          label: '下架'
        }],
        recommendStatusOptions: [{
          value: 1,
          label: '菜品推荐中'
        }, {
          value: 0,
          label: '未推荐'
        }],
        newStatusOptions: [{
          value: 1,
          label: '新品推荐中'
        }, {
          value: 0,
          label: '未新品'
        }],
      }
    },
    //初始化函数, created 和 mounted 都是页面加载初始化的函数
    created() {
      console.log("页面初始化");
      this.getList();
    },

    filters: {
      verifyStatusFilter(value) {
        if (value === 1) {
          return '审核通过';
        } else {
          return '未审核';
        }
      }
    },
    methods: { 
      getList() {
        console.log("调用getList()");
        console.log(this.listQuery);
        this.listLoading = true;
        fetchList(this.listQuery).then(response => {
          console.log(response);
          this.listLoading = false;
          this.list = response.data.arrays;
          this.total = response.data.total;
        }).catch(err=> {
          console.log("错误" + err);
        });
      },
      handleShowStoreStockDialog(index, row){
        this.storeStock.dialogVisible=true;
        this.storeStock.dishId=row.id;
        console.log("dishId" + this.storeStock.dishId);
        getDishStock(row.id).then(res=> {
          console.log("成功！！" + res);
          this.storeStock.storeStockList = res.data.arrays;
        }).catch(err=> {
          console.log(err);
        })
      },
      handleEditStoreStockConfirm(){
        console.log("yyds", this.storeStock.storeStockList);
        if(this.storeStock.storeStockList==null||this.storeStock.storeStockList.length<=0){
          this.$message({
            message: '暂无更改信息',
            type: 'warning',
            duration: 1000
          });
          return
        }
        console.log("修改1");
        this.$confirm('是否要进行修改', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          this.dialogLoading = true;
          console.log("到这里额了", this.storeStock.storeStockList);
          let params = new URLSearchParams();
          params.append("dishStock", JSON.stringify(this.storeStock.storeStockList));
          updateDishStock(params).then(res=> {
            console.log(res);
            this.dialogLoading = false;
            this.storeStock.dialogVisible = false;
            this.$message('操作成功');
          }).catch(err=> {
            console.log("粗我u" + err);
          });
        });
      },
      handleSearchList() {
        this.listQuery.pageNum = 1;
        this.getList();
      },
      handleAddProduct() {
        this.$router.push({path:'/dish-ms/addDish'});
      },
      //批量删除操作
      handleBatchOperate() {
        if(this.operateType==null){
          this.$message({
            message: '请选择操作类型',
            type: 'warning',
            duration: 1000
          });
          return;
        }
        if(this.multipleSelection==null||this.multipleSelection.length<1){
          this.$message({
            message: '请选择要操作的商品',
            type: 'warning',
            duration: 1000
          });
          return;
        }
        this.$confirm('是否要进行该批量操作?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids=[];
          for(let i = 0; i < this.multipleSelection.length; i++){
            ids.push(this.multipleSelection[i].id);
          }
          switch (this.operateType) {
            case this.operates[0].value:
              this.updatePublishStatus(1,ids);
              break;
            case this.operates[1].value:
              this.updatePublishStatus(0,ids);
              break;
            case this.operates[2].value:
              this.updateRecommendStatus(1,ids);
              break;
            case this.operates[3].value:
              this.updateRecommendStatus(0,ids);
              break;
            case this.operates[4].value:
              this.updateNewStatus(1,ids);
              break;
            case this.operates[5].value:
              this.updateNewStatus(0,ids);
              break;
            case this.operates[6].value:
              this.updateDeleteStatus(1,ids);
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
      handlePublishStatusChange(index, row) {
        let ids = [];
        ids.push(row.id);
        this.updatePublishStatus(row.publishStatus, ids);
      },
      handleNewStatusChange(index, row) {
        let ids = [];
        ids.push(row.id);
        this.updateNewStatus(row.newStatus, ids);
      },
      handleRecommendStatusChange(index, row) {
        let ids = [];
        ids.push(row.id);
        console.log("resc", row.recommendStatus);
        this.updateRecommendStatus(row.recommendStatus, ids);
      },
      //重置
      handleResetSearch() {
        this.selectProductCateValue = [];
        this.listQuery = Object.assign({}, defaultListQuery);
      },
      handleDeleteDish(index, row){
        this.$confirm('是否要进行删除操作?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids = [];
          ids.push(row.id);
          this.updateDeleteStatus(1,ids);
        });
      },
      handleUpdateDish(index, row){
        this.$router.push({path:'/dish-ms/updateDish',query:{id:row.id}});
      },
      updatePublishStatus(publishStatus, ids) {
        let params = new URLSearchParams();
        params.append('dishIds', ids);
        params.append('publishStatus', publishStatus);
        console.log('update');
        updatePublishStatus(params).then(response => {
          this.$message({
            message: '修改成功',
            type: 'success',
            duration: 1000
          });
        });
      },
      updateNewStatus(newStatus, ids) {
        let params = new URLSearchParams();
        params.append('dishIds', ids);
        params.append('newStatus', newStatus);
        updateNewStatus(params).then(response => {
          this.$message({
            message: '修改成功',
            type: 'success',
            duration: 1000
          });
        });
      },
      updateRecommendStatus(recommendStatus, ids) {
        let params = new URLSearchParams();
        params.append('dishIds', ids);
        params.append('recommendStatus', recommendStatus);
        updateRecommendStatus(params).then(response => {
          this.$message({
            message: '修改成功',
            type: 'success',
            duration: 1000
          });
        });
      },
      updateDeleteStatus(deleteStatus, ids) {
        console.log("yyds");
        let params = new URLSearchParams();
        params.append('dishIds', ids);
        updateDeleteStatus(params).then(response => {
          this.$message({
            message: '删除成功',
            type: 'success',
            duration: 1000
          });
          this.getList();
        });
      }
    }
  }
</script>
<style></style>


