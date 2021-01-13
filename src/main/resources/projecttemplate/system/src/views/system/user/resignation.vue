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
      <el-col :md="15">
        <div class="wrapper right-wrapper">
          <LeaveSysUser ref="leaveSysUser" :activeRole="activeRole" :leaveData="leaveData" @reloadData="reloadData" @closeDialog="closeDialog" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import LeaveSysUser from './leaveSysUser'
import RoleTree from '@/components/RoleTree/RoleTree'

export default {
  name: 'Resignation',
  components: {
    LeaveSysUser,
    RoleTree
  },
  props: {
    curLeaveData: {
      type: [Array, Object]
    },
    isShowResignation: {
      type: Boolean
    }
  },
  data() {
    return {
      activeRole: {},
      leaveData: this.curLeaveData
    }
  },
  watch: {
    curLeaveData(val) {
      this.leaveData = val
    }
  },
  methods: {
    reloadData() {
      this.$emit('reloadData')
    },
    openSet(data) {
      this.activeRole = JSON.parse(JSON.stringify(data))
    },
    closeDialog() {
      this.$emit('update:isShowResignation', false)
      this.$refs.leaveSysUser.closeDialog()
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
        min-height: 400px;
        margin-bottom: 0;
      }
    }
  }
</style>
