<template>
  <div class="login-container">
    <div class="login-bg">
      <div class="login-modal"></div>
    </div>
    <div class="login-wrapper" :class="{'mobile': device === 'mobile'}">
      <el-form
        v-show="loginType === 1"
        ref="loginForm"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        auto-complete="on"
        label-position="left"
      >
        <div class="title-container">
          <img src="../../assets/images/login_image/login_title.png">
          <div class="name">${project.artifactName}后台管理系统</div>
        </div>
        <el-form-item prop="sysUserPhone">
          <span class="svg-container">
            <i class="icon-user"></i>
          </span>
          <el-input
            ref="username"
            v-model.trim="loginForm.sysUserPhone"
            placeholder="请输入登录用户名"
            name="username"
            type="text"
            tabindex="1"
            auto-complete="on"
          />
        </el-form-item>

        <el-form-item prop="password">
          <span class="svg-container">
            <i class="icon-password"></i>
          </span>
          <el-input
            :key="passwordType"
            ref="password"
            v-model.trim="loginForm.password"
            :type="passwordType"
            placeholder="密码"
            name="password"
            tabindex="2"
            auto-complete="on"
            @keyup.enter.native="handleLogin"
          />
          <span class="show-pwd" @click="showPwd">
            <i :class="passwordType === 'password' ? 'icon-eye' : 'icon-eye-open'"></i>
          </span>
        </el-form-item>
        <el-button
          :loading="loading"
          @click.native.prevent="handleLogin"
        >登录</el-button>
      </el-form>
    </div>
  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'

export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('请输入正确的手机号'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码长度不能少于6位'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        sysUserPhone: '',
        password: ''
      },
      loginRules: {
        sysUserPhone: [
          { required: true, trigger: 'blur', validator: validateUsername }
        ],
        password: [
          { required: true, trigger: 'blur', validator: validatePassword }
        ]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined,
      loginType: 1
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  beforeMount() {
    window.addEventListener('message', this.handleMessage)
  },
  beforeDestroy() {
    window.removeEventListener('message', this.handleMessage)
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm).then(res => {
            if (res.result) {
              this.$router.push({ path: this.redirect || '/' })
              this.loading = false
            } else {
              this.loading = false
            }
          }).catch(() => {
            this.loading = false
          })
        } else {
          return false
        }
      })
    },
    changeLoginType() {
      this.loginType = this.loginType === 1 ? 2 : 1
    }
  }
}
</script>

<style lang="scss">
  /* 修复input 背景不协调 和光标变色 */
  /* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

  $bg: #ffffff;
  $light_gray: #333;
  $cursor: #333;

  @supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    .login-container .el-input input {
      color: $cursor;
    }
  }

  /* reset heyi-ui css */
  .login-container {
    .el-input {
      display: inline-block;
      height: 39px;
      width: 80%;

      input {
        background: transparent;
        border: 0;
        -webkit-appearance: none;
        padding: 12px 5px 12px 0;
        color: $light_gray;
        height: 39px;
        &:-webkit-autofill {
          box-shadow: 0 0 0 1000px $bg inset !important;
          -webkit-text-fill-color: $cursor !important;
        }
      }
    }
    .el-button {
      display: block;
      margin: 30px auto 0 auto;
      height: 39px;
      border-radius: 5px;
      font-size: 16px;
      width: 268px;
      color: #000000;
      font-weight: bold;
      background-color: #FFDD00;
      border-color: #FFDD00;
    }
    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 50px;
      color: #454545;
      margin-bottom: 20px;
    }
  }
</style>

<style lang="scss" scoped>
  $bg: #ffffff;
  $dark_gray: #999999;
  $light_gray: #f7b538;

  .login-container {
    position: relative;
    min-height: 100%;
    width: 100%;
    overflow: hidden;
    .login-bg{
      position: absolute;
      top: 0;
      left: 0;
      height: 65%;
      width: 100%;
      background: url("../../assets/images/login_image/system_login.png") no-repeat center center !important;
      .login-modal{
        height: 100%;
        width: 100%;
        background-color: rgba(0, 0, 0, .7);
      }
    }
    .login-wrapper {
      width: 100%;
      padding: 55px 56px 43px;
      background-color: #FCF9FC;
      max-width: 380px;
      position: absolute;
      bottom: 27%;
      left: 50%;
      overflow: hidden;
      transform: translate(-50%, 0);
      box-shadow: 0 0 30px rgba(129, 126, 127, 0.3);
      .left-bg {
        .title {
          width: 80%;
          height: 100px;
          /*background: url("../../assets/images/login_image/login_title.png") no-repeat center center;*/
          /*background-size: contain;*/
          margin: 210px auto 0;
        }
        &::after {
          display: block;
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          width: 50%;
          height: 100%;
          /*background: url("../../assets/images/login_image/login_left_bg.png") no-repeat*/
          /*center center !important;*/
          /*background-size: cover;*/
          z-index: -1;
        }
      }
      .form-wrapper {
        position: relative;
        width: 100%;
        padding: 120px 0;
        background-color: rgba(253,253,253,0.85);
        box-sizing: border-box;
        .login-type-change {
          position: absolute;
          top: 0;
          right: 0;
          display: block;
          width: 85px;
          height: 85px;
          /*background: url("../../assets/images/login_image/login_ewm.png") no-repeat center center;*/
          /*background-size: 85px 80px;*/
          cursor: pointer;
          &.computer {
            /*background: url("../../assets/images/login_image/login_ewm_computer.png") no-repeat center center;*/
          }
        }
      }
      .login_ewm {
        .title-container {
          .title {
            margin-bottom: 0;
          }
        }
        .login_container {
          text-align: center;
        }
      }
      .login-form {
        max-width: 400px;
        margin: 0 auto;
        min-height: 313px;
        background-color: rgba(253,253,253,0);
        /deep/ .el-form-item {
          background-color: #ffffff;
          border-radius: 5px;
          border: 1px solid #E6E6E6;
          .el-form-item__content{
            height: 39px;
            line-height: normal;
            font-size: 0;
            .el-input input {
              display: block;
              width: 100%;
              font-size: 14px;
              color: #333;
            }
          }

        }
        &::after {
          position: absolute;
          left: -10px;
          bottom: -40px;
          display: block;
          padding: 0 10px;
          width: 100%;
          height: 80px;
          content: "";
          /*background: url("../../assets/images/login_image/shadow.png") bottom*/
          /*center;*/
          background-size: 100% auto;
          box-sizing: content-box;
          z-index: -1;
        }
      }
      .tips {
        font-size: 14px;
        color: #fff;
        margin-bottom: 10px;
        span {
          &:first-of-type {
            margin-right: 16px;
          }
        }
      }

      .svg-container {
        padding: 11px 0 0 12px;
        color: $dark_gray;
        vertical-align: top;
        width: 42px;
        display: inline-block;
      }

      .title-container {
        position: relative;
        text-align: center;
        .title {
          font-size: 36px;
          color: #333333;
          margin: 0 auto;
          text-align: center;
          font-weight: bold;
        }
        .name{
          font-size: 14px;
          color: #999999;
          text-align: center;
          margin: 20px auto;
        }
      }

      .show-pwd {
        position: absolute;
        right: 20px;
        top: 12px;
        font-size: 14px;
        color: $dark_gray;
        cursor: pointer;
        user-select: none;
        i {
          font-size: 16px;
        }
      }
      &.mobile {
        width: 90%;
        border-radius: 10px;
        .form-wrapper {
          padding: 60px 0;
        }
        .title-container {
          .title {
            font-size: 26px;
          }
        }
        .el-form-item-mobile {
          margin: 0 10px 25px 10px;
          box-sizing: border-box;
          background-color: #f0fbff;
          border-bottom: 1px solid #e1e1e1;
          width: auto;
          border-radius: 10px;
          /deep/ .el-form-item__error-m {
            right: unset;
            top: unset;
            left: 0;
            bottom: -20px;
          }
          /deep/ .show-pwd{
            top: 15px;
          }
          /deep/ .el-input input {
            font-size: 14px;
            color: #333;
          }
        }
      }
    }

  }
</style>
