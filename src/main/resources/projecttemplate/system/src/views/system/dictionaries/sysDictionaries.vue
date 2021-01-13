<template>
  <div class="SysDictionary app-container">
    <el-button type="primary" @click="openModel">添加</el-button>
    <PageListSearch :searchList="searchList" :searchData="listQuery" @queryTablePageChange="handleFileter"></PageListSearch>
    <div class="shop-table">
      <el-table :data="SysDictionaryList" border stripe>
        <el-table-column fixed align="center" prop="dictType" label="字典类型" />
        <el-table-column fixed align="center" prop="dictCode" label="系统识别名称" />
        <el-table-column fixed align="center" prop="dictName" label="业务显示名称" />
        <el-table-column fixed align="center" label="操作">
          <template slot-scope="scope">
            <span class="btn" @click="openedit(scope.row)">编辑</span>
            <span class="btn" @click="deleteItem(scope.row)">删除</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-pagination v-show="tableTotal>0" background layout="prev, pager, next" :total="tableTotal+1" :current-page.sync="currentPage" :page-size.sync="pageSize" @current-change="tablePageChange"></el-pagination>
    <el-dialog :visible.sync="showModal" :title="title" width="30%">
      <el-form ref="SysDictionaryform" :model="SysDictionary" label-width="150px" :rules="rule4SysDictionaryform">
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="SysDictionary.dictType" placeholder="请输入字典类型" />
        </el-form-item>
        <el-form-item label="系统识别名称" prop="dictCode">
          <el-input v-model="SysDictionary.dictCode" placeholder="请输入系统识别名称" />
        </el-form-item>
        <el-form-item label="业务显示名称" prop="dictName">
          <el-input v-model="SysDictionary.dictName" placeholder="请输入业务显示名称" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="default" size="large" @click="showModal=false">取消</el-button>
        <el-button type="primary" size="large" @click="addoredit">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import PageListSearch from '@/components/ListScreeningSearch/ListScreeningSearch'
export default {
  name: 'SysDictionary',
  components: {
    PageListSearch
  },
  data() {
    return {
      SysDictionaryList: [],
      currentPage: 1,
      pageSize: 10,
      tableTotal: 0,
      showModal: false,
      modalType: '',
      title: '',
      searchList: [
        { id: 'dictType', name: '字典类型', type: 'input' },
        { id: 'dictName', name: '业务名称', type: 'input' }
      ],
      listQuery: {
        StudentName: ''
      },
      SysDictionary: {
        dictType: '',
        dictCode: '',
        dictName: '',
        dictDescription: ''
      },
      rule4SysDictionaryform: {
        dictType: [
          { required: true, message: '请输入字典类型', trigger: 'blur' }
        ],
        dictCode: [
          { required: true, message: '请输入系统识别名称', trigger: 'blur' }
        ],
        dictName: [
          { required: true, message: '请输入业务展示名称', trigger: 'blur' }
        ],
        dictDescription: [
          { required: true, message: '请输入描述', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getTableData()
  },
  methods: {
    /* 查询 */
    getTableData(start = 0, length = this.pageSize) {
      this.$http.sysDictionaryPages({
        ...this.listQuery,
        start: start,
        length: length
      }).then((res) => {
        if (res.result) {
          this.SysDictionaryList = res.obj
          this.tableTotal = res.records
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
        this.$http.sysDictionarydelete(item).then(res => {
          if (res.result) {
            this.$message.success(res.description)
            this.tablePageChange()
          } else {
            this.$message.error(res.description)
          }
        })
      })
    },
    /* 分页查询 */
    tablePageChange() {
      this.getTableData(this.currentPage, this.pageSize)
    },
    /* 修改 */
    openedit(item) {
      this.showModal = true
      this.title = '修改字典'
      this.state = 1
      this.SysDictionary = JSON.parse(JSON.stringify(item))
    },
    addoredit() {
      if (this.state === 0) {
        this.additem()
      } else {
        this.edititem()
      }
    },
    additem() {
      this.$refs['SysDictionaryform'].validate((valid) => {
        if (valid) {
          this.$http.sysDictionariesadd(this.SysDictionary).then((res) => {
            if (res.result) {
              this.$message.success(res.description)
              this.showModal = false
              this.tablePageChange()
            } else {
              this.$message.error(res.description)
            }
          })
        } else {
          this.$message.error('请检查提交信息')
        }
      })
    },
    edititem() {
      this.$refs['SysDictionaryform'].validate((valid) => {
        if (valid) {
          this.$http.sysDictionariesupdate(this.SysDictionary).then((res) => {
            if (res.result) {
              this.$message.success(res.description)
              this.showModal = false
              this.tablePageChange()
            } else {
              this.$message.error(res.description)
            }
          })
        } else {
          this.$message.error('请检查提交信息')
        }
      })
    },
    openModel() {
      this.SysDictionary = {
        dictType: '',
        dictCode: '',
        dictName: '',
        dictDescription: ''
      }
      this.showModal = true
      this.title = '添加字典'
      if (this.$refs.SysDictionaryform) {
        this.$refs['SysDictionaryform'].resetFields()
      }
      this.state = 0
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
  .app-container{
    margin: 20px;
    padding: 20px;
    background-color: #ffffff;
    .shop-table{
      padding-top: 10px;
    }
  }
</style>
