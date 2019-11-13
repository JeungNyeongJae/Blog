<template>
  <div class="login_bg">
    <div class="login-page">
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item>
          <h5>Login</h5>
          <span>Join to our community now !</span>
        </el-form-item>
        <el-form-item label-width="0px" prop="checkPhone">
          <el-row>
            <el-col :span="2">
              <i style="font-size: 30px;" class="el-icon-phone"></i>
            </el-col>
            <el-col :span="22">
              <el-input class="input" placeholder="请输入手机号码" type="phone" v-model="ruleForm.phone" autocomplete="off"></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label-width="0px" prop="pass">
          <el-row>
            <el-col :span="2">
              <i style="font-size: 30px;" class="el-icon-lock"></i>
            </el-col>
            <el-col :span="22">
              <el-input class="input" placeholder="请输入密码" type="phone" v-model="ruleForm.password" autocomplete="off"></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label-width="0px">
          <el-button class="waves-effect" type="primary" @click="submitForm('ruleForm')" plain>提交</el-button>
          <el-button class="waves-effect" type="primary" @click="resetForm('ruleForm')" >重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
    export default {
        name: "login",
        data() {
            const validatePhone = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('手机号不能为空'));
                } else if (!this.checkMobile(value)) {
                    callback(new Error('手机号码不合法'))
                } else {
                    this.queryMobile(value, callback);
                }
            };

            const validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    callback();
                }
            };

            return {
                ruleForm: {
                    password: '',
                    phone: ''
                },
                count: null,
                isDisabled: false, // 是否禁止点击发送验证码按钮
                flag: true,
                rules: {
                    password: [
                        { validator: validatePass, trigger: ['blur', 'change'] }
                    ],
                    phone: [
                        { validator: validatePhone, trigger: ['blur', 'change'] }
                    ]
                }
            };
        },
        methods: {
            // 验证手机号
            checkMobile(str) {
                let re = /^1[3456789]\d{9}$/;
                // console.log(re.test(str))
                return re.test(str);
            },

            // 登录
            submitForm(formName) {
                this.$refs[formName].validate(valid => {
                    if (valid ) {
                        this.$axios.post("/api/auth-service/login" , {
                            'userPassword' : this.ruleForm.password,
                            'userMobile' : this.ruleForm.phone
                        }).then( res => {
                            if( res.data.data.errno === 0 ) {
                                alert('登录成功');
                            } else {
                                alert('登陆失败');
                            }
                        });
                    } else {
                        // console.log("error submit!!");
                        return false;
                    }
                })
            },
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
        .input{
          border-bottom: 1px solid #9e9e9e !important;
        }
        .opacity{
          opacity: 0.7;
        }
        .row{
          margin-bottom: 0;
        }
        .waves-effect{
          border-radius: 150px;
          background-image:linear-gradient(45deg, #8e24aa, #ff6e40);
        }
        .el-form-item>.el-form-item__content{
          margin-left: 0 !important;
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
