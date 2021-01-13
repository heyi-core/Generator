<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb class="breadcrumb-container" />
<#if project.login = true>
    <div class="right-menu flex-row flex-y-center">
      <div v-if="device === 'desktop'" class="avatar-container">
        <div class="avatar-wrapper">
          <span class="user-role">{{ userInfo.sysUserName }}</span>
          <!--          <span class="user-avatar" :style="{'background-image': 'url(' + userInfo.teacherAvatar +')'}"></span>-->
          <router-link to="/user">
            <i class="icon-setup_fill"></i>
          </router-link>
          <i class="i-line"></i>
          <i class="icon-sign_out" @click="logout"></i>
        </div>
      </div>

      <el-dropdown v-if="device === 'mobile'" class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <span class="user-role">{{ userInfo.sysUserName }}</span>
          <!--          <span class="user-avatar">{{ userInfo.teacherAvatar }}</span>-->
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown" style="overflow: hidden;">
          <!--勿动魏波修改-->
          <el-dropdown-item divided style="padding: 0">
            <router-link to="/user" style="display:block; padding: 0 20px;">
              个人资料
            </router-link>
          </el-dropdown-item>
          <el-dropdown-item divided style="padding: 0">
            <span style="display:block; padding: 0 20px;" @click="logout">退出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
</#if>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'

export default {
  components: {
    Breadcrumb,
    Hamburger
  },
  data() {
    return {
    }
  },
  computed: {
    ...mapGetters([
      'sidebar'<#if project.login = true>,</#if>
    <#if project.login = true>
      'avatar',
      'userInfo'
    </#if>
    ])
  },
  created() {
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${r'${'}this.$route.fullPath}`)
    }
  }
}
</script>

<style lang="scss" scoped>
  .navbar {
    height: 50px;
    position: relative;
    background: #fff;
    z-index: 1;
    box-shadow: 0 1px 6px #d8dbe1;

    .hamburger-container {
      line-height: 50px;
      height: 100%;
      float: left;
      cursor: pointer;
      transition: background .3s;
      -webkit-tap-highlight-color:transparent;

      &:hover {
        background: rgba(0, 0, 0, .025)
      }
    }

    .breadcrumb-container {
      float: left;
    }

    .right-menu {
      float: right;
      height: 100%;

      &:focus {
        outline: none;
      }

      .right-menu-item {
        display: inline-block;
        padding: 0 8px;
        height: 100%;
        font-size: 18px;
        color: #5a5e66;
        vertical-align: text-bottom;

        &.hover-effect {
          cursor: pointer;
          transition: background .3s;

          &:hover {
            background: rgba(0, 0, 0, .025)
          }
        }
      }

      .avatar-container {
        margin-right: 30px;
        .avatar-wrapper {
          display: flex;
          align-items: center;
          -webkit-box-align: center;

          .user-avatar {
            cursor: pointer;
            width: 38px;
            height: 40px;
            line-height: 40px;
            border-radius: 50%;
            vertical-align: middle;
            text-align: center;
            color: #fff;
            font-size: 14px;
            font-weight: bold;
            font-family: "Microsoft YaHei", serif;
            background-size: cover;
            background-repeat: no-repeat;
            background-position: center;
          }
          .icon-setup_fill {
            font-size: 20px;
            color: #999999;
            margin: 0 20px;
          }

          .i-line {
            display: inline-block;
            background-color: rgb(196, 198, 207);
            width: 1px;
            height: 20px;
          }

          .icon-sign_out {
            font-size: 20px;
            color: #999999;
            margin-left: 20px;
          }

          span {
            display: inline-block;
            margin-left: 17px;
            font-size: 16px;
            color: #333333;
            vertical-align: middle;
            font-weight: bold;
            font-family: "Microsoft YaHei";
          }

          .el-icon-caret-bottom {
            cursor: pointer;
            position: absolute;
            right: -20px;
            top: 15px;
            font-size: 12px;
          }
        }
      }
    }
  }

  @media (max-width: 485px) {
    /deep/ .user-role {
      display: none!important;
    }

    /deep/ .user-avatar {
      margin-left: 0!important;
    }

    /deep/ .el-breadcrumb__separator {
      margin: 0 5px;
    }

    /deep/ .hamburger-container {
      padding: 0 10px!important;
    }
  }

  @media (max-width: 350px) {
    /deep/.el-breadcrumb {
      font-size: 12px!important;
    }
  }
</style>
