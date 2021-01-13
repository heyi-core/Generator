<template>
  <div>
    <el-tree
      ref="oztree"
      :props="treeSet"
      node-key="sysRoleId"
      :load="loadNode"
      lazy
      highlight-current
      :expand-on-click-node="false"
      class="org-tree"
      @node-click="openSet"
    />
  </div>
</template>

<script>
export default {
  name: 'RoleTreeItem',
  props: {
    sysRoleId: {
      type: [Number, String]
    }
  },
  data() {
    return {
      treeSet: {
        label: 'sysRoleName',
        children: 'children'
      }
    }
  },
  methods: {
    loadNode(node, resolve) {
      const pId = node.key ? node.key : this.sysRoleId
      if (node.key) {
        this.$http.getchildsByRoleId({ sysRoleId: pId }).then(res => {
          if (res.result) {
            res.obj.forEach(item => {
              item.children = []
            })
            return resolve(res.obj)
          } else {
            return resolve([])
          }
        })
      } else {
        const result = []
        this.$http.sysRoledetail({ sysRoleId: pId }).then(res => {
          if (res.result) {
            result.push(res.obj)
            result.children = []
            return resolve(result)
          } else {
            return resolve([])
          }
        })
      }
    },
    openSet(data) {
      this.$http.sysRoledetail({ sysRoleId: data.sysRoleId }).then(res => {
        this.$emit('saveRole', res.obj)
      })
    }
  }
}
</script>

<style scoped>

</style>
