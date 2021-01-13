<template>
  <div class="sidebar-logo-container" :class="{'collapse':collapse}">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link ico" to="/dashboard">
        <img src="../../../assets/images/home_image/logoyuan.svg" class="sidebar-logo-ico" alt="">
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link sidebar-logo-name" to="/dashboard">
        <span class="name">${project.artifactName}后台管理</span>
        <!--        <img src="../../../assets/images/home_image/logo01.svg" class="sidebar-logo-ico" alt="" width="100%">-->
      </router-link>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      title: '金贝街后台管理'
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  padding: 20px 10px;
  background: #061025;
  overflow: hidden;
  box-sizing: border-box;
  & .sidebar-logo-link {
    height: 100%;
    width: 100%;
    font-size: 0;
    img {
      display: inline-block;
    }
    &.ico {
      .sidebar-logo-ico {
        width: 20px;
      }
    }
    &.sidebar-logo-name{
      height: 80px;
      line-height: 80px;
      border-bottom: 1px solid #020406;
      text-align: center;
      .name{
        color: #ffffff;
        font-size: 20px;
        font-weight: bold;
      }
    }
  }
  &.collapse {
    padding: 20px 0 10px 0;
    text-align: center;
  }
}
</style>
