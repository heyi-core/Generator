<template>
  <div class="app-container sys-user">
    <el-row :gutter="20">
      <el-col :md="8">
        <div class="wrapper left-wrapper">
          <div class="modal-title">组织结构图</div>
          <div class="content">
            <role-tree @saveRole="openSet" />
          </div>
        </div>
      </el-col>
      <el-col :md="16">
        <div class="wrapper right-wrapper">
          <sys-user :active-role="activeRole" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import SysUser from './sysuser'
import RoleTree from '@/components/RoleTree/RoleTree'

export default {
  name: 'UserRole',
  components: {
    SysUser,
    RoleTree
  },
  data() {
    return {
      activeRole: {
        api: '',
        button: '',
        createTime: '',
        creator: '',
        parentSysRoleId: '',
        router: '',
        sysRoleId: '',
        sysRoleName: ''
      }
    }
  },
  methods: {
    openSet(data) {
      this.activeRole = JSON.parse(JSON.stringify(data))
    }
  }
}
</script>
<style lang="scss" scoped>
  .app-container{
    padding: 20px;
  }
  .sys-user {
    .wrapper {
      margin-bottom: 10px;
      background-color: #fff;
      &.left-wrapper {
        padding: 7px;
        .modal-title {
          position: relative;
          padding: 0 30px;
          height: 52px;
          line-height: 56px;
          border-bottom: 1px solid #d8d8d8;
          font-size: 16px;
          color: #333;
          &::before {
            position: absolute;
            left: 12px;
            top: 22px;
            display: block;
            width: 4px;
            height: 12px;
            content: '';
            background-color: #f7b538;
          }
        }
        .content {
          padding: 12px;
        }
      }
      &.right-wrapper {
        padding: 20px;
      }
      @media (max-width: 992px) {
        &.right-wrapper {
          padding: 10px;
        }
      }
    }
    @media (min-width: 992px) {
      .wrapper {
        min-height: 700px;
        margin-bottom: 0;
      }
    }
  }
</style>
