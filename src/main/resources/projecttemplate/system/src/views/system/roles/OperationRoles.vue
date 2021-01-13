<template>
  <div class="operation-roles">
    <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" class="role-whole-select" @change="btnCheckAll">全选</el-checkbox>
    <el-checkbox-group v-model="btnRoules" class="role-checkbox" @change="changeData">
      <el-checkbox v-for="(btn, index) in allBtnRoule" :key="index" :label="btn.code">
        {{ btn.name }}
      </el-checkbox>
    </el-checkbox-group>
  </div>
</template>

<script>
export default {
  name: 'OperationRoles',
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
      checkAll: true,
      allBtnRoule: [],
      btnRoules: [],
      isIndeterminate: false
    }
  },
  watch: {
    activeName(newName) {
      this.activeName = newName
      this.getBtnRoles(newName)
    },
    activeRole(newData) {
      this.activeRole = newData
    }
  },
  created() {
  },
  methods: {
    btnCheckAll(val) {
      const allBtns = []
      if (this.allBtnRoule && this.allBtnRoule.length > 0) {
        this.allBtnRoule.forEach(item => {
          allBtns.push(item.code)
        })
      }
      this.apiRoules = val ? this.changeData(allBtns, true) : this.changeData([], true)
    },
    getBtnRoles(name) {
      this.$store.dispatch('roles/getBtnRoles', { name: name, activeRole: this.activeRole }).then(res => {
        this.allBtnRoule = res.allBtnRoule
        this.btnRoules = res.btnRoules
        this.$nextTick(() => {
          this.checkAll = this.isCheckedAll(res.btnRoules, res.allBtnRoule)
        })
      })
    },
    changeData(data, need) {
      const defaultBtn = this.activeRole.button ? JSON.parse(this.activeRole.button) : []
      const allBtnRoule = this.allBtnRoule
      const chooseBtn = data
      allBtnRoule.forEach(btn => {
        if (chooseBtn.indexOf(btn.code) !== -1) {
          if (defaultBtn.indexOf(btn.code) === -1) {
            defaultBtn.push(btn.code)
          }
        } else {
          if (defaultBtn.indexOf(btn.code) !== -1) {
            defaultBtn.splice(defaultBtn.findIndex(item => item === btn.code), 1)
          }
        }
      })
      this.$nextTick(() => {
        need ? this.btnRoules = defaultBtn : []
      })
      this.$emit('saveBtn', defaultBtn)
    },
    isCheckedAll(roules, allRoule) {
      let result = true
      allRoule.forEach(item => {
        if (roules.indexOf(item.code) === -1) {
          result = false
        }
      })
      return result
    }
  }
}
</script>

<style lang="scss" scoped>
  .operation-roles {
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
