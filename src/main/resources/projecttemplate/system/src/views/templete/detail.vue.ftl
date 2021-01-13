<template>
  <div class="${table.tableNameLowerCamel}-detail">
    <el-form ref="dataForm" :rules="modalType === 'view' ? {} : rules" :model="activeRow" label-width="100px" style="width: 500px; margin-left:50px;">
<#list table.columns as column>
      <el-form-item label="${column.columnComment}" prop="${column.columnNameLowerCamel}">
        <el-input v-model="activeRow.${column.columnNameLowerCamel}" placeholder="请输入${column.columnComment}" />
      </el-form-item>
</#list>
    </el-form>
    <div v-if="modalType === 'add' || modalType === 'edit' " class="dialog-footer">
      <el-button @click="closeDialog">取消</el-button>
      <el-button type="primary" :loading="addLoading" @click="modalType === 'add' ? createData() : updateData()">确认</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: '${table.tableNameUpperCamel}Detail',
  props: {
    modalType: {
      type: String
    },
    activeRow: {
      type: Object,
      default: () => {
        return {
<#list table.columns as column>
          ${column.columnNameLower}: '',
</#list>
          timestamp: new Date()
        }
      }
    }
  },
  data() {
    return {
      addLoading: false,
      rules: {
<#list table.columns as column>
<#if column_has_next>
        ${column.columnNameLower}: [{ required: true, message: '请输入${column.columnComment}', trigger: 'blur' }],
<#else>
        ${column.columnNameLower}: [{ required: true, message: '请输入${column.columnComment}', trigger: 'blur' }]
</#if>
</#list>
      }
    }
  },

  watch: {
    activeRow(data) {
      this.$nextTick(() => {
        this.resetPageData()
      })
    }
  },

  methods: {
    /* 重置页面数据 */
    resetPageData() {
      this.addLoading = false
      this.$refs.dataForm.clearValidate()
    },
    /* 添加 */
    createData() {
      this.addLoading = true
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$http.${table.tableNameLowerCamel}Add(this.activeRow).then((res) => {
            this.addLoading = false
            if (res.result) {
              this.$message.success(res.description)
              this.$emit('closeDialog')
              this.$emit('reloadData')
            } else {
              this.$message.error(res.description)
            }
          }).catch(err => {
            this.addLoading = false
            this.$message.error(err || '添加请求失败')
          })
        } else {
          this.addLoading = false
          this.$message.error('请检查提交信息')
        }
      })
    },
    /* 修改 */
    updateData() {
      this.addLoading = true
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$http.${table.tableNameLowerCamel}Update(this.activeRow).then((res) => {
            this.addLoading = false
            if (res.result) {
              this.$message.success(res.description)
              this.$emit('closeDialog')
              this.$emit('reloadData')
            } else {
              this.$message.error(res.description)
            }
          }).catch(err => {
            this.addLoading = false
            this.$message.error(err || '修改请求失败')
          })
        } else {
          this.addLoading = false
          this.$message.error('请检查提交信息')
        }
      })
    },
    /* 关闭父弹窗 */
    closeDialog() {
      this.$emit('closeDialog')
    }
  }
}
</script>

<style lang="scss" scoped>
  /deep/ .dialog-footer{
    text-align: center;
  }
</style>
