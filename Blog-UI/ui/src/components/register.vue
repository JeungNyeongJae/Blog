<template>
  <div class="login_bg">
    <div class="login-page">
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item>
          <h5>Register</h5>
          <span>Join to our community now !</span>
        </el-form-item>
        <el-form-item label-width="0px" prop="checkPhone">
          <el-row>
            <el-col :span="2">
              <i style="font-size: 30px;" class="el-icon-phone"></i>
            </el-col>
            <el-col :span="22">
              <el-input class="input" placeholder="请输入手机号码" type="phone" v-model="ruleForm.checkPhone" autocomplete="off"></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label-width="0px" prop="code">
          <el-row>
            <el-col :span="2">
              <i style="font-size: 30px;" class="el-icon-news"></i>
            </el-col>
            <el-col :span="14">
              <el-input class="input" placeholder="请输入验证码" type="code" v-model="ruleForm.code" autocomplete="off"></el-input>
            </el-col>
            <el-col :span="8">
              <el-button class="waves-effect" :disabled='isDisabled' @click="sendCode" type="primary">{{buttonText}}</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item
          label-width="0px"
          prop="email"
          :rules="[
      { required: true, message: '请输入邮箱地址', trigger: 'blur' },
      { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
    ]"
        >
          <el-row>
            <el-col :span="2">
              <i style="font-size: 30px;" class="el-icon-message"></i>
            </el-col>
            <el-col :span="22">
              <el-input class="input" placeholder="请输入电子邮箱" type="mail" v-model="ruleForm.email" autocomplete="off"></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label-width="0px" prop="pass">
          <el-row>
            <el-col :span="2">
              <i style="font-size: 30px;" class="el-icon-lock"></i>
            </el-col>
            <el-col :span="22">
              <el-input class="input" placeholder="请输入密码" type="phone" v-model="ruleForm.pass" autocomplete="off"></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label-width="0px" prop="checkPass">
          <el-row>
            <el-col :span="2">
              <i style="font-size: 30px;" class="el-icon-lock"></i>
            </el-col>
            <el-col :span="22">
              <el-input class="input" placeholder="确认密码" type="phone" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label-width="0px">
          <el-button class="waves-effect" type="primary" @click="submitForm('ruleForm')" plain>提交</el-button>
          <el-button class="waves-effect" type="primary" @click="resetForm('ruleForm')" >重置</el-button>
          <p class="login" @click="gotoLogin">已有账号？立即登录</p>
        </el-form-item>
      </el-form>
      <!--<div class="row">-->
      <!--<form class="col s12 demo-ruleForm">-->
      <!--<div class="opacity">-->
      <!--<h5>Register</h5>-->
      <!--<span>Join to our community now !</span>-->
      <!--&lt;!&ndash;<p>Join to our community now !</p>&ndash;&gt;-->
      <!--</div>-->
      <!--<div class="row">-->
      <!--<div class="input-field col s12">-->
      <!--<i class="material-icons prefix pt-2 opacity">local_phone_outline</i>-->
      <!--<input id="phone" type="password" class="validate">-->
      <!--<label for="phone">phone</label>-->
      <!--<span class="helper-text" data-error="wrong" data-success="right">Helper text</span>-->
      <!--</div>-->
      <!--</div>-->
      <!--<div class="row">-->
      <!--<div class="input-field col s8">-->
      <!--<i class="material-icons prefix pt-2 opacity">perm_phone_msg_outline</i>-->
      <!--<input id="code" type="text" class="validate">-->
      <!--<label for="code">Code</label>-->
      <!--</div>-->
      <!--<div class="input-field col s4">-->
      <!--<a class="waves-effect waves-light btn-small">获取验证码</a>-->
      <!--</div>-->
      <!--</div>-->
      <!--<div class="row">-->
      <!--<div class="input-field col s12">-->
      <!--<i class="material-icons prefix pt-2 opacity">lock_outline</i>-->
      <!--<input id="password" type="password" class="validate">-->
      <!--<label for="password">Password</label>-->
      <!--</div>-->
      <!--</div>-->
      <!--<div class="row">-->
      <!--<div class="input-field col s12">-->
      <!--<i class="material-icons prefix pt-2 opacity">lock_outline</i>-->
      <!--<input id="Confirm password" type="password" class="validate">-->
      <!--<label for="password">Confirm Password</label>-->
      <!--</div>-->
      <!--</div>-->
      <!--<div class="row">-->
      <!--<div class="input-field col s12">-->
      <!--<i class="material-icons prefix pt-2 opacity">mail_outline</i>-->
      <!--<input id="email" type="email" class="validate">-->
      <!--<label for="email">Email</label>-->
      <!--</div>-->
      <!--</div>-->
      <!--<div class="row">-->
      <!--<div class="input-field col s12">-->
      <!--<a class="waves-effect waves-light btn col s12">REGISTER</a>-->
      <!--</div>-->
      <!--</div>-->
      <!--<div class="opacity">-->
      <!--&lt;!&ndash;<div class="input-field col s12">&ndash;&gt;-->
      <!--<router-link :to="'login'"><a href="#"><p>Already have an account? Login</p></a></router-link>-->
      <!--&lt;!&ndash;</div>&ndash;&gt;-->
      <!--</div>-->
      <!--</form>-->
      <!--</div>-->
    </div>
  </div>
  <!--<div class="login_bg">-->
  <!--</div>-->
</template>

<script>
  export default {
    name: "register",
    data() {
      const validatePhone = (rule, value, callback) => {
        console.log(value)
        if (!value) {
          return callback(new Error('手机号不能为空'));
        } else if (!this.checkMobile(value)) {
          callback(new Error('手机号码不合法'))
        }
        else {
          return callback();
        }
      }
      //  <!--验证码是否为空-->
      let checkSmscode = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入手机验证码'))
        } else {
          callback()
        }
      }
      const validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.ruleForm.checkPass !== '') {
            this.$refs.ruleForm.validateField('checkPass');
          }
          callback();
        }
      };
      const validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.ruleForm.pass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        ruleForm: {
          pass: '',
          checkPass: '',
          checkPhone: '',
          code: ''
        },
        buttonText: '发送验证码',
        isDisabled: false, // 是否禁止点击发送验证码按钮
        flag: true,
        rules: {
          pass: [
            { validator: validatePass, trigger: ['blur', 'change'] }
          ],
          checkPass: [
            { validator: validatePass2, trigger: ['blur', 'change'] }
          ],
          checkPhone: [
            { validator: validatePhone, trigger: ['blur', 'change'] }
          ],
          code: [
            { validator: checkSmscode, trigger: ['blur', 'change'] }
          ]
        }
      };
    },
    methods: {
      // submitForm(formName) {
      //   this.$refs[formName].validate((valid) => {
      //     if (valid) {
      //       alert('submit!');
      //     } else {
      //       console.log('error submit!!');
      //       return false;
      //     }
      //   });
      // },
      // <!--提交注册-->
      submitForm(formName) {
        console.log(formName);
        this.$refs[formName].validate(valid => {
          if (valid) {
            setTimeout(() => {
              alert('注册成功')
            }, 400);
          } else {
            console.log("error submit!!");
            return false;
          }
        })
      },
      // <!--发送验证码-->
      sendCode () {
        let tel = this.ruleForm.checkPhone
        if (this.checkMobile(tel)) {
          console.log(tel)
          let time = 60
          this.buttonText = '已发送';
          this.isDisabled = true;
          if (this.flag) {
            this.flag = false;
            let timer = setInterval(() => {
              time--;
              this.buttonText = time + ' 秒'
              if (time === 0) {
                clearInterval(timer);
                this.buttonText = '重新获取'
                this.isDisabled = false
                this.flag = true;
              }
            }, 1000)
          }
        }
      },
      // 验证手机号
      checkMobile(str) {
        let re = /^1[3456789]\d{9}$/;
        if (re.test(str)) {
          return true;
        } else {
          return false;
        }
      },
      // <!--进入登录页-->
      gotoLogin() {},
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
  }
</script>
<style lang="scss" scoped>


  .login_bg {
    bottom: 0;
    direction: ltr;
    left: 0;
    overflow: hidden;
    position: absolute;
    right: 0;
    top: 0;
    background-image: url("https://pixinvent.com/materialize-material-design-admin-template/app-assets/images/gallery/flat-bg.jpg");
    background-size: cover; //100%和cover都是用于将图片扩大或者缩放来适应整个容器
    z-index: 1;
    user-select: none; //不能选中文本
    .login-page{
      position: relative;
      margin: 0;
      padding: 0;
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center; /*定义body的元素垂直居中*/
      justify-content: center; /*定义body的里的元素水平居中*/
      .demo-ruleForm{
        padding: 36px;
        box-shadow: 0 0 100px rgba(0, 0, 0, .08);
        background-color: rgba(255,255,255,0.8);
        margin: 0 auto;
        max-width: 600px;
        width: 440px;
        border-radius: 20px;
        padding: 0px 36px 0px 36px;
        //position: relative;
        //top: 50%; /*偏移*/
        //transform: translateY(-50%);
        .input{
          /*background: none;*/
          /*border: none;*/
          border-bottom: 1px solid #9e9e9e !important;
          /*border-radius: inherit;*/
        }
        .opacity{
          opacity: 0.7;
        }
        .row{
          margin-bottom: 0px;
        }
        .waves-effect{
          border-radius: 150px;
          background-image:linear-gradient(45deg, #8e24aa, #ff6e40);
        }
        .el-form-item>.el-form-item__content{
          margin-left: 0px !important;
        }
      }
    }
  }

</style>
<style  lang="scss">
  .el-form-item__error{
    left: 8.3333% !important;
  }
  .input input{
    border: none;
    background-color: rgba(255,255,255,0);
  }
</style>
