<template>
  <div class="app-container">
    <div class="btn-group">
      <el-button type="primary" @click="openModel('add')">添加</el-button>
    </div>
    <PageListSearch :searchList="searchList" :searchData="listQuery" @queryTablePageChange="handleFileter"></PageListSearch>
    <div class="table-view">
      <el-table :data="dataList" border stripe>
<#list table.columns as column>
        <el-table-column align="center" fixed label="${column.columnComment}" prop="${column.columnNameLowerCamel}"></el-table-column>
</#list>
      </el-table>
    </div>
    <el-pagination v-show="tableTotal>0" background layout="prev, pager, next" :total="tableTotal+1" :current-page.sync="currentPage" :page-size.sync="pageSize" @current-change="tablePageChange"></el-pagination>
    <el-dialog :title="textMap[modalType]" :visible.sync="showModal" width="950px">
      <${table.tableNameUpperCamel}Detail :activeRow="activeRow" :modalType="modalType" @closeDialog="closeDialog" @reloadData="tablePageChange"></${table.tableNameUpperCamel}Detail>
    </el-dialog>
  </div>
</template>

<script>
import PageListSearch from '@/components/ListScreeningSearch/ListScreeningSearch'
import ${table.tableNameUpperCamel}Detail from './${table.tableNameUpperCamel}Detail'

const defaultItemData = {
<#list table.columns as column>
  ${column.columnNameLowerCamel}: '',
</#list>
  timeStamp: new Date()
}

export default {
  name: 'CourseOrder',
  components: {
    PageListSearch,
    ${table.tableNameUpperCamel}Detail
  },
  data() {
    return {
      activeRow: defaultItemData,
      dataList: [],
      currentPage: 1,
      pageSize: 12,
      tableTotal: 0,
      showModal: false,
      modalType: '',
      textMap: {
        add: '添加${table.tableComment}',
        edit: '编辑${table.tableComment}',
        view: '查看${table.tableComment}'
      },
      searchList: [],
      listQuery: {}
    }
  },
  created() {
    this.getTableData()
  },
  methods: {
    /* 查询 */
    getTableData(start = 0, length = this.pageSize) {
      this.$http.${table.tableNameLowerCamel}Pages({
        ...this.listQuery,
        start: start,
        length: length
        // orderType: 'DESC',
        // orderField: 'create_time'
      }).then((res) => {
        if (res.result) {
          this.dataList = res.obj.data
          this.tableTotal = res.obj.recordsTotal
        } else {
          this.$message({
            message: res.description,
            type: 'error',
            customClass: 'messageIndex'
          })
        }
      })
    },
    /* 删除单条数据 */
    deleteItem(item) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.${table.tableNameLowerCamel}Delete(item).then(res => {
          if (res.result) {
            this.$message.success('删除成功')
            this.handleFileter()
          } else {
            this.$message.error('删除失败')
          }
        })
      })
    },
    /* 分页查询 */
    tablePageChange() {
      this.getTableData(this.currentPage, this.pageSize)
    },
    /* 打开弹窗 */
    openModel(operation, row = {}) {
      const rowData = JSON.parse(JSON.stringify(operation === 'add' ? defaultItemData : row))
      rowData.timestamp = new Date()
      this.showModal = true
      this.modalType = operation
      this.activeRow = rowData
    },
    /* 关闭弹窗 */
    closeDialog() {
      this.showModal = false
    },
    /* 搜索 */
    handleFileter() {
      this.currentPage = 1
      this.tablePageChange()
    }
  }
}
</script>

<style lang="scss" scoped>
  .app-container {
    margin: 20px;
    padding: 20px;
    background-color: #ffffff;
    .table-view {
      padding-top: 10px;
    }
  }
</style>
