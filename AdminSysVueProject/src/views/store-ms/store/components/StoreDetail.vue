<template>
   
  <el-card class="form-container" shadow="never">
    <span class="title">填写店铺表单: </span>
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="80px"
      class="form"
    >
      <el-form-item label-width="100px" label="店铺名称:" prop="name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>

      <el-form-item label-width="100px" label="店铺图片:" prop="img">
        <el-input
          v-model="form.img"
          placeholder="请填写图片URL，如您需要上传本地图片，请点击下方按钮上传"
        ></el-input>
        <el-upload
          class="upload-demo"
          :action="url"
          :on-change="handleChange"
          :file-list="fileList"
          :on-success="handleImgUploadSuccess"
          :on-error="handleImgUploadFail"
          :before-upload="beforeImgUpload"
          :on-remove = "removeImg"
          :limit="1"
          :headers = "token"
          list-type="picture"
        >
          <el-button size="small" type="primary">上传本地图片</el-button>
        </el-upload>
      </el-form-item>

      <el-form-item label-width="100px" label="店铺桌位:">
        <el-input-number
          v-model="tableNum"
          @change="handleChange"
          :min="0"
          label="选择数量"
        ></el-input-number>
        <el-table :data="form.tables" stripe style="width: 100%" v-show="tableNum > 0">
          <el-table-column prop="date" label="桌号" width="180">
            <template slot-scope="scope">
              <el-input
                disabled
                v-model="scope.row.table"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="可容纳人数" width="180">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.capacity"
                placeholder="请输入内容"
              ></el-input>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>

      <el-form-item label-width="100px" label="店铺地址:" prop="price">
        <el-input
          v-model.number="form.address"
          placeholder="请填写店铺地址"
        ></el-input>
      </el-form-item>

      <el-form-item
        label-width="100px"
        label="店铺营业时间:"
        prop="description"
      >
        <el-input
          v-model.number="form.time"
          placeholder="请填写店铺营业时间"
        ></el-input>
      </el-form-item>

      <el-form-item label-width="100px" label="店铺联系电话:" prop="main">
        <el-input v-model="form.contactPhone"></el-input>
      </el-form-item>

      <el-form-item label-width="100px" label="开启外卖配送: ">
        <el-switch
          v-model="form.supportTakeout"
          active-value="1"
          inactive-value="0"
          active-text="开启"
          inactive-text="不开启"
        >
        </el-switch>
      </el-form-item>

      <el-form-item style="margin-left: 20px">
        <el-button type="primary" @click="finishCommit('form')"
          >立即创建</el-button
        >
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
const form = {
  name: null,
  img: null,
  address: null,
  contactPhone: null,
  time: null,
  supportTakeout: 0,
  tables: [],
};
import { fetchList, getStore, updateDeleteStatus, 
updateWorkingStatus, updateTakeoutStatus, 
createStore, updateStore } from "@/api/store";
import {
  getToken
} from '@/utils/auth'
export default {
  name: "StoreDetail",

  props: {
    isEdit: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      token: {
        Authorization: getToken()
      },
      fileList: [],
      url: process.env.BASE_API + '/upload_img',
      tableNum: 0,
      rules: {
        name: [
          { required: true, message: "请输入店铺名称", trigger: "blur" },
          {
            min: 1,
            max: 20,
            message: "长度为 1 到 20 个字符",
            trigger: "blur",
          },
        ],
        img: [
          { required: true, message: "请输入店铺图片URL", trigger: "blur" },
        ],
      },
      form: Object.assign({}, form),
    };
  },
  //初始化函数
  created() {
    //如果是选择更新，则查询店铺信息
    if (this.isEdit) {
      getStore(this.$route.query.id).then((response) => {
        this.form = response.data;
        this.tableNum = this.form.tables.length;
        this.handleChange();
        console.log("form == ", this.form);
      });
    }
  },
  methods: {
    removeImg() {
      this.form.img = null
    },
    handleChange() {
      let num = this.tableNum, cnt = Math.abs(num - this.form.tables.length);
      console.log(num, cnt);
      for (let i = cnt - 1; i >= 0; i--) {
        if (num > this.form.tables.length) 
          this.form.tables.push({table:num - i, capacity: null});
        else 
          this.form.tables.pop();
      }
    },
    finishCommit(formName) {
      console.log(formName);
      this.$refs[formName].validate((valid) => {
        console.log(valid);
        if (valid) {
          let isEdit = this.isEdit;
          this.$confirm("是否要提交该产品", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }).then(() => {
            if (isEdit) {
              updateStore(this.form).then(
                (response) => {
                  this.$message({
                    type: "success",
                    message: "提交成功",
                    duration: 1000,
                  });
                  this.$router.back();
                }
              );
            } else {
              createStore(this.form).then((response) => {
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

    beforeImgUpload(file) {
      const isJPG =
        file.type === "image/jpeg" ||
        file.type === "image/png" ||
        file.type === "image/jpg";
      const isLt2M = file.size < 10 * 1024 * 1024;
      if (!isJPG) {
        this.$message.error("上传图片只能是 JPG 或 PNG 格式");
      }
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 10MB");
      }
      this.form.img = "";
      // this.form.img = "www.7k7k.com";
      return isJPG && isLt2M;
    },

    handleImgUploadSuccess(e) {
      console.log("secuss", e);
      this.form.img = e;
    },

    handleImgUploadFail(e) {
      console.log("fail");
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
</style>


