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

          <el-form-item label="套餐名称：">
            <el-input style="width: 203px" v-model="listQuery.keyword" placeholder="套餐名称"></el-input>
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

          

        </el-form>
      </div>
    </el-card>

    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button
        class="btn-add"
        @click="handleAddCombo()"
        size="mini">
        添加套餐
      </el-button>
    </el-card>

    <!-- 展示部分 -->
    <!-- 对象数组是 :data list，这是 El元素的 table  -->
    <div class="table-container">
      <el-table ref="productTable"
                :data="list"
                style="width: 100%"
                @selection-change="handleSelectionChange"
                :default-sort = "{prop: 'id', order: 'ascending'}"
                v-loading="listLoading"
                border>
        <el-table-column type="selection" width="60" align="center"></el-table-column>

        <el-table-column label="编号" width="100" align="center" prop="id" sortable="">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>

        <el-table-column label="套餐名称" align="center">
          <template slot-scope="scope">{{scope.row.name}}</template>
        </el-table-column>

        <el-table-column label="价格" width="120" align="center" sortable prop="price">
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
          </template>
        </el-table-column>

        <el-table-column label="套餐配置" width="100" align="center">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" @click="handleShowcomboDishDialog(scope.$index, scope.row)" circle></el-button>
          </template>
        </el-table-column>

        <el-table-column label="销量" width="100" align="center" sortable prop="sale">
          <template slot-scope="scope">{{scope.row.sale}}</template>
        </el-table-column>
        
        <el-table-column label="操作" width="160" align="center">
          <template slot-scope="scope">
            <p>
              <el-button
                size="mini"
                @click="handleUpdateCombo(scope.$index, scope.row)">编辑具体信息
              </el-button>
            </p>
            <p>
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除套餐记录
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
      title="编辑套餐具体配置"
      :visible.sync="comboDish.dialogVisible"
      width="40%">
      <span>套餐编号：</span>
      <span>{{comboDish.comboId}}</span>

      <el-table style="width: 100%;margin-top: 20px"
                :data="comboDish.comboDishList"
                border>
        <el-table-column
          label="菜品编号"
          align="center">
          <template slot-scope="scope">
            <el-input :disabled="true" v-model="scope.row.dishId"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          label="菜品名称"
          align="center">
          <template slot-scope="scope">
            <el-input :disabled="true" v-model="scope.row.dishName"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          label="菜品数量"
          align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.dishNum"></el-input>
          </template>
        </el-table-column>
      </el-table>

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleEditcomboDishConfirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>





<script>
  import {
    fetchList,
    updateDeleteStatus,
    updatePublishStatus,
    fetchComboDishList,
    updateComboDishList,
  } from '@/api/combo'


  const defaultListQuery = {
    keyword: null,
    pageNum: 1,
    pageSize: 5,
    publishStatus: null,
    
  };
  
  export default {
    name: "productList",
    data() {
      return {
        //编辑店铺库存
        comboDish: { 
          //是否显示
          dialogVisible: false,
          comboId: null,
          storeName: null,
          comboDishList: [],
        },
        //批量操作
        operates: [
          {
            label: "套餐上架",
            value: "publishOn"
          },
          {
            label: "套餐下架",
            value: "publishOff"
          },
          {
            label: "一键删除",
            value: "recycle"
          }
        ],
        operateType: null,
        listQuery: Object.assign({}, defaultListQuery),
        list: null,
        total: null,
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
        
      }
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

      }
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
      getProductSkuSp(row, index) {
        let spData = JSON.parse(row.spData);
        if(spData!=null&&index<spData.length){
          return spData[index].value;
        }else{
          return null;
        }
      },

      
      getList() {
        console.log("调用getList()");
        console.log(this.listQuery);
        this.listLoading = true;
        fetchList(this.listQuery).then(response => {
          this.listLoading = false;
          this.list = response.data.arrays;
          this.total = response.data.total;
        });
      },
      handleShowcomboDishDialog(index, row){
        this.comboDish.dialogVisible=true;
        this.comboDish.comboId=row.id;
        fetchComboDishList(row.id).then(res=> {
          console.log(res);
          this.comboDish.comboDishList = res.data.arrays;
        }).catch(err=> {
          console.log(err);
        })
      },
      handleEditcomboDishConfirm(){
        if(this.comboDish.comboDishList==null||this.comboDish.comboDishList.length<=0){
          this.$message({
            message: '暂无信息',
            type: 'warning',
            duration: 1000
          });
          return
        }
        this.$confirm('是否要进行修改', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          console.log(this.comboDish.comboDishList);
          updateComboDishList(this.comboDish.comboDishList).then(response=>{
            this.$message({
              message: '修改成功',
              type: 'success',
              duration: 1000
            });
            this.comboDish.dialogVisible=false;
          });
        });
      },
      handleSearchList() {
        this.listQuery.pageNum = 1;
        this.getList();
      },
      handleAddCombo() {
        this.$router.push({path:'/dish-ms/addCombo'});
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
        this.updateRecommendStatus(row.recommandStatus, ids);
      },
      //重置
      handleResetSearch() {
        this.selectProductCateValue = [];
        this.listQuery = Object.assign({}, defaultListQuery);
      },
      handleDelete(index, row){
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
      handleUpdateCombo(index, row){
        this.$router.push({path:'/dish-ms/updateCombo',query:{id:row.id}});
      },
      updatePublishStatus(publishStatus, ids) {
        let params = new URLSearchParams();
        params.append('comboIds', ids);
        params.append('publishStatus', publishStatus);
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
        params.append('ids', ids);
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
        params.append('ids', ids);
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
        let params = new URLSearchParams();
        params.append('comboIds', ids);
        params.append('deleteStatus', deleteStatus);
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


