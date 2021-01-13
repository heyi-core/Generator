<template>
  <div class="role-tree">
    <RoleTreeItem v-for="(role, index) in userInfo.sysRoleId" :key="index" :sysRoleId="role" @saveRole="openSet"></RoleTreeItem>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import RoleTreeItem from '../RoleTreeItem/RoleTreeItem'

export default {
  name: 'RoleTree',
  components: {
    RoleTreeItem
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ])
  },
  created() {
    const _this = this
    document.onkeydown = function() {
      if (window.event.keyCode === 13) {
        _this.gettabledata()
      }
    }
  },
  methods: {
    openSet(data) {
      this.$emit('saveRole', data)
    }
  }
}
</script>

<style lang="scss" scoped>
  .role-tree {
    /deep/ .el-tree {
      line-height: 40px;
      .el-tree-node {
        &:focus>.el-tree-node__content{
          background-color: #fef7eb;
        }
        &.is-current>.el-tree-node__content {
          background-color: #fef7eb;
        }
        .el-tree-node__content {
          height: 40px;
          line-height: 40px;
          font-size: 16px;
          color: #888;
          .el-tree-node__label {
            font-size: 16px;
          }
          &:hover {
            background-color: #fef7eb;
          }
        }
      }
    }
  }
</style>
