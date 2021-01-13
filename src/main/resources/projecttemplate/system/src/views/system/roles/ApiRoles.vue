<template>
  <div class="api-roles">
    <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" class="role-whole-select" @change="btnCheckAll">全选</el-checkbox>
    <el-checkbox-group v-model="apiRoules" class="role-checkbox" @change="changeData">
      <el-checkbox v-for="(api, index) in allApiRoule" :key="index" :label="api" />
    </el-checkbox-group>
  </div>
</template>

<script>
export default {
  name: 'ApiRoles',
  props: {
    activeName: {
      type: String
    },
    activeRole: {
      type: Object
    }
  },
  data() {
    return {
      checkAll: false,
      allApiRoule: [],
      apiRoules: [],
      isIndeterminate: false
    }
  },
  watch: {
    activeName(newName) {
      this.activeName = newName
      this.getApiRoles(newName)
    },
    activeRole(newData) {
      this.activeRole = newData
    }
  },
  methods: {
    btnCheckAll(val) {
      const allApis = []
      for (const i in this.allApiRoule) {
        allApis.push(this.allApiRoule[i])
      }
      this.apiRoules = val ? this.changeData(allApis, true) : this.changeData([], true)
    },
    getApiRoles(name) {
      this.$store.dispatch('roles/getApiRoles', { name: name, activeRole: this.activeRole }).then(res => {
        this.allApiRoule = res.allApiRoule
        this.apiRoules = res.apiRoules
        this.$nextTick(() => {
          this.checkAll = this.isCheckedAll(res.apiRoules, res.allApiRoule)
        })
      })
    },
    changeData(data, need) {
      const defaultApi = this.activeRole.api ? JSON.parse(this.activeRole.api) : []
      const allApiRoule = this.allApiRoule
      const chooseApi = data
      for (const api in allApiRoule) {
        if (chooseApi.indexOf(allApiRoule[api]) !== -1) {
          if (defaultApi.indexOf(allApiRoule[api]) === -1) {
            defaultApi.push(allApiRoule[api])
          }
        } else {
          if (defaultApi.indexOf(allApiRoule[api]) !== -1) {
            defaultApi.splice(defaultApi.findIndex(item => item === allApiRoule[api]), 1)
          }
        }
      }
      this.$nextTick(() => {
        need ? this.apiRoules = defaultApi : ''
      })
      this.$emit('saveApi', defaultApi)
    },
    isCheckedAll(roules, allRoule) {
      let result = true
      for (const i in allRoule) {
        if (roules.indexOf(allRoule[i]) === -1) {
          result = false
        }
      }
      return result
    }
  }
}
</script>

<style lang="scss" scoped>
  .api-roles {
    .role-whole-select{
      margin-top: 0 !important;
    }
    /deep/ .el-checkbox {
      height: 14px;
      line-height: 14px;
      padding: 0;
      min-width: 14px;
      display: block;
      text-align: left;
      margin-top: 15px;
      .el-checkbox__input {
        display: inline-block;
      }
      .el-checkbox__label {
        padding-left: 10px;
      }
      &.is-checked {
        background-color: #fff;
        .el-checkbox__input.is-checked + .el-checkbox__label {
          color: #f7b538 !important;
        }
      }
    }
  }
</style>
