<template>
  <el-container>
    <el-header class="header">
      <!--<el-row type="flex" justify="center">-->
        <!--<el-col :span="20">-->
          <!--<div class="grid-content bg-purple">-->
            <el-menu :default-active="activeIndex"
                     class="el-menu-demo"
                     background-color="rgba(255,255,255,0)"
                     text-color="#ffffff"
                     active-text-color="#e91e63"
                     mode="horizontal"
                     @select="handleSelect">
              <el-menu-item index="1">首页</el-menu-item>
              <el-menu-item index="2">生活</el-menu-item>
              <el-menu-item index="3">运动</el-menu-item>
              <el-menu-item index="4">情感</el-menu-item>
              <el-menu-item index="5">娱乐</el-menu-item>
            </el-menu>
          <!--</div>-->
        <!--</el-col>-->
        <!--<el-col :span="4">-->
          <!--<div class="grid-content bg-purple-light">-->
          <el-dropdown>
          <!--<el-avatar :size="size" :src="circleUrl"></el-avatar>-->
            <el-avatar icon="el-icon-user-solid"></el-avatar>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>查看</el-dropdown-item>
              <el-dropdown-item>新增</el-dropdown-item>
              <el-dropdown-item>删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <span class="LogRes"><router-link :to="{path:'login'}">登录/</router-link><router-link :to="{path:'register'}">注册</router-link></span>
          <!--</div>-->
        <!--</el-col>-->
      <!--</el-row>-->
    </el-header>
  </el-container>
</template>

<script>
    export default {
        name: "homepage",
      data () {
        return {
          circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          activeIndex: '1',
        }
      },
      methods: {
        handleSelect(key, keyPath) {
          console.log(key, keyPath);
        }
      }
    }
</script>

<style lang="scss">
  .header{
    vertical-align: middle;
    background-image: linear-gradient(45deg, rgb(48, 63, 159), rgb(123, 31, 162));
    .grid-content{
      vertical-align: middle;
    }
    .LogRes a{
      color: #ffffff;
    }
  }

</style>
