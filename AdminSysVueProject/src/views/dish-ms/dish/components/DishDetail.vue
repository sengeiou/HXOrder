<template>
   
  <el-card class="form-container" shadow="never">
    <span class="title">填写菜品表单: </span>
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="80px"
      class="form"
    >
      <el-form-item label-width="100px" label="菜品名称:" prop="name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>

      <el-form-item
        label-width="100px"
        label="菜品图片:"
        prop="img"
        v-loading="loading"
      >
        <el-input
          v-model="form.dishImg"
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

      <el-form-item label-width="100px" label="菜品单价:" prop="price">
        <el-input
          v-model.number="form.price"
          placeholder="请填写一个小数或整数，单位是元"
        ></el-input>
      </el-form-item>

      <el-form-item label-width="100px" label="菜品描述:" prop="desc">
        <el-input
          type="textarea"
          :rows="3"
          placeholder="请简要描述该菜品"
          v-model="form.desc"
        >
        </el-input>
      </el-form-item>

      <el-form-item label-width="100px" label="菜品主料:" prop="mainIngredient">
        <el-input v-model="form.mainIngredient"></el-input>
      </el-form-item>

      <el-form-item label-width="100px" label="菜品辅料:" prop="ingredient">
        <el-input v-model="form.ingredient"></el-input>
      </el-form-item>

      <el-form-item label-width="100px" label="菜品分类:" prop="kind">
        <el-checkbox-group v-model="form.classificationIds" >
          <el-checkbox v-for="c in cs" :label="c.id" :key="c.id">{{
            c.name
          }}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label-width="100px" label="制作方法:" prop="makeMethod">
        <el-input
          type="textarea"
          :rows="3"
          placeholder="请简要描述菜品制作方法"
          v-model="form.makeMethod"
        >
        </el-input>
      </el-form-item>

      <el-form-item label-width="100px" label="菜品重量:" prop="weight">
        <el-input
          v-model.number="form.weight"
          placeholder="请输入一个整数或小数，单为是克(g)"
        ></el-input>
      </el-form-item>

      <el-form-item label-width="100px" label="制作时间:" prop="makeTime">
        <el-input v-model="form.makeTime"></el-input>
      </el-form-item>

      <el-form-item label-width="100px" label="是否推荐: ">
        <el-checkbox-group v-model="form.marketingType" @change="test">
          <el-checkbox label="新品推荐" ></el-checkbox>
          <el-checkbox label="菜品推荐" ></el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label-width="100px" label="推荐理由:" prop="makeMethod">
        <el-input
          type="textarea"
          :rows="3"
          placeholder="请简要描述推荐理由(选填，当您选择菜品推荐时可选择填写此项，如果您不填写此项，则默认选择菜品描述作为原因)"
          v-model="form.reason"
        >
        </el-input>
      </el-form-item>

      <el-form-item label-width="100px" label="菜品标签:" prop="tags">
        <el-input
          type="textarea"
          :rows="3"
          placeholder="多个标签请以空格或换行分隔，如：养生菜 下酒菜 适合小孩子吃"
          v-model="form.tags"
        >
        </el-input>
      </el-form-item>

      <el-form-item label="优惠方式: " label-width="100px">
        <el-radio-group v-model="form.discount.type" size="medium">
          <el-radio-button border label=0>无优惠</el-radio-button>
          <el-radio-button border label=1>折扣优惠</el-radio-button>
          <el-radio-button border label=2>立减优惠</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item
        v-if="form.discount.type !== 0"
        label="优惠力度: "
        label-width="100px"
        prop="discountVal"
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
      </el-form-item>

      <el-form-item
        v-if="form.discount.type !== 0"
        label="次数限制: "
        label-width="100px"
        prop="discountVal"
      >
        <el-input
          v-if="form.discount.type != 0"
          v-model.number="form.discount.count"
          placeholder="请输入用户对于该菜品每日最多可享受优惠的次数(次数每日凌晨两点刷新)，如果为 -1 表示不限制"
        ></el-input>
      </el-form-item>

      <el-form-item style="margin-left: 20px">
        <el-button type="primary" @click="finishCommit">立即创建</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
const form = {
  name: null,
  dishImg: null,
  price: null,
  desc: null,
  mainIngredient: null,
  ingredient: null,
  makeMethod: null,
  weight: null,
  makeTime: null,
  marketingType: [],
  // discount: "无优惠",
  // discountVal: null,
  // discountCnt: null,
  discount: {
    type: 0,
    val: 0,
    count: 0
  },
  classificationIds: [],
  tags: null,
};

import {
  updateDish,
  createDish,
  fetchClassificationList,
  getDish,
} from "@/api/dish";

export default {
  name: "DishDetail",
  props: {
    isEdit: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      url: process.env.BASE_API + '/upload_img',
      loading: false,
      cs: [
      ],
      rules: {
        name: [
          { required: true, message: "请输入菜品名称", trigger: "blur" },
          {
            min: 1,
            max: 20,
            message: "长度为 1 到 20 个字符",
            trigger: "blur",
          },
        ],
        dishImg: [
          { required: true, message: "请输入菜品图片URL", trigger: "blur" },
        ],
        price: [
          { required: true, message: "请输入菜品单价", trigger: "blur" },
          { type: "number", message: "必须为整数或者小数", trigger: "blur" },
        ],
        weight: [
          // { required: true, message: "请输入菜品单价", trigger: "blur" },
          { type: "number", message: "必须为整数或者小数", trigger: "blur" },
        ],
        desc: [{ required: true, message: "请输入内容", trigger: "blur" }],
      },
      form: Object.assign({}, form),
    };
  },
  //初始化函数
  created() {
    //如果是选择更新，则查询菜品信息
    this.getClassificationList();
    if (this.isEdit) {
      this.getDishFromServer(this.$route.query.id).then((response) => {
        this.productParam = response.data;
      });
    }
  },
  methods: {
    removeImg() {
      this.form.img = null
    },
    test() {
      console.log("test", this.form.classificationIds, this.form.marketingType);
    },
    getDishFromServer(id) {
      getDish(id)
        .then((res) => {
          console.log(res.data);
          let tags = "";
          let marketingType = [];
          res.data.tags.forEach(
            (item) => (tags += item.replace(",", "") + " ")
          );
          if (res.data.recommendStatus == 1) marketingType.push("菜品推荐");
          if (res.data.newStatus == 1) marketingType.push("新品推荐");
          if (res.data.discount == null) {
            res.data.discount = {type: 0, val: 0, count: 0}
          }
          res.data.marketingType = marketingType;
          this.form = res.data;    
          this.form.tags = tags;
          console.log("form == ", this.form);
          console.log("获取成功");
        })
        .catch((err) => {
          console.log("获取失败");
        });
    },
    handleImgChange() {},
    finishCommit() {
      console.log(this.form);
      this.$refs["form"].validate((valid) => {
        console.log(valid);
        if (valid) {
          let isEdit = this.isEdit;
          this.$confirm("是否要提交该产品", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }).then(() => {
            let dish = this.form;
            console.log(dish.tags);
            dish.tags = dish.tags.split("/s+/");
            if (isEdit) {
              updateDish(JSON.stringify(dish)).then((response) => {
                this.$message({
                  type: "success",
                  message: "提交成功",
                  duration: 1000,
                });
                this.$router.back();
              }).catch((err) => {
                  dish.tags = "";
                });;
            } else {
              createDish(JSON.stringify(dish))
                .then((response) => {
                  this.$message({
                    type: "success",
                    message: "提交成功",
                    duration: 1000,
                  });
                  location.reload();
                })
                .catch((err) => {
                  dish.tags = "";
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
      return isJPG && isLt2M;
    },

    getClassificationList() {
      fetchClassificationList()
        .then((res) => {
          let arr = res.data.arrays.filter((item) => {
            return item.id < 100000;
          });
          this.cs = arr;
          console.log("cs == ", JSON.stringify(this.cs));
        })
        .catch((err) => {
          console.log("获取Cshivering" + err);
        });
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


