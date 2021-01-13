<template>
  <div class="roles-set">
    <div class="default" @click="defaultRole">
      <i class="el-icon-setting"></i>
      <span>控制台权限</span>
    </div>
    <el-tree
      v-if="activeRole"
      ref="routerTree"
      class="nav-choose"
      :data="asyncRoutes"
      :props="defaultProps"
      node-key="name"
      :expand-on-click-node="false"
      :default-checked-keys="chooseData"
      check-strictly
      show-checkbox
      @node-click="chooseItem"
    />
  </div>
</template>

<script>
export default {
  name: 'RolesSet',
  props: {
    activeRole: {
      type: Object
    }
  },
  data() {
    return {
      asyncRoutes: [],
      defaultProps: {
        children: 'children',
        label: this.chooseTitle
      },
      chooseData: []
    }
  },
  watch: {
    activeRole(newData) {
      this.activeRole = newData
      this.getDefaultNav()
    }
  },
  methods: {
    chooseTitle(data, node) {
      return data.meta.title
    },
    chooseItem(data) {
      this.$emit('savename', data.name)
    },
    defaultRole() {
      this.$emit('savename', 'Dashboard')
    },
    getDefaultNav() {
      this.$store.dispatch('roles/getRoleNav', this.activeRole).then(res => {
        this.asyncRoutes = res.asyncRoutes
        this.chooseData = res.routerRolers
        this.$refs.routerTree.setCheckedNodes(res.routerRolers)
      })
    },
    getRouterRoules() {
      return [...this.$refs.routerTree.getHalfCheckedKeys(), ...this.$refs.routerTree.getCheckedKeys()]
    }
  }
}
</script>

<style lang="scss" scoped>
  .roles-set {
    .default {
      padding: 0 15px;
      height: 40px;
      line-height: 40px;
      font-size: 16px;
      color: #606266;
      cursor: pointer;
      i {
        margin-right: 4px;
      }
      &:hover {
        background-color: rgb(254, 247, 235);
      }
    }
    /deep/ .nav-choose {
      .el-checkbox {
        width: 14px;
        height: 14px;
        padding: 0;
        border-radius: 4px;
        min-width: 14px;
        .el-checkbox__input {
          display: block;
        }
      }
      .el-tree-node:focus>.el-tree-node__content{
        background-color: #fef7eb;
      }
      .el-tree-node__content {
        position: relative;
        padding: 0 30px;
        height: 40px;
        line-height: 40px;
        .el-tree-node__label {
          margin-left: 2px;
          font-size: 16px;
          color: #545454;
        }
        &:hover {
          background-color: #fef7eb;
          span.cz {
            display: block;
          }
        }
      }
      .el-tree-node__children {
        .el-tree-node__label {
          margin-left: 4px;
          font-size: 15px;
          color: #8e8e8e;
        }
      }
    }
  }

</style>
