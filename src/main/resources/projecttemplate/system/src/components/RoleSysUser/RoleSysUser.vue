<template>
  <el-row class="sys-user">
    <div class="top-operation">
      <div class="item">
        <el-input v-model="querySysUser.sysUserPhone" placeholder="用户手机号" />
      </div>
      <div class="item">
        <el-input v-model="querySysUser.sysUserName" placeholder="用户名"></el-input>
      </div>
      <div class="item">
        <el-button class="select-btn" type="primary" @click="queryTablePageChange">搜索</el-button>
      </div>
    </div>
    <el-table ref="table" v-loading="tableLoading" border stripe :data="tableData" highlight-current-row @current-change="handleCurrentChange">
      <el-table-column fixed align="center" prop="sysUserName" label="用户名" />
    </el-table>
    <el-pagination
      background
      layout="prev, pager, next"
      :total="tableTotal+1"
      :current-page.sync="currentPage"
      :page-size.sync="pageSize"
      @current-change="tablePageChange"
    />
    <el-button type="primary" @click="confirmData">确定</el-button>
  </el-row>
</template>

<script>
import { validUsername } from '@/utils/validate'
import { mapGetters } from 'vuex'

export default {
  name: 'RoleSysUser',
  props: {
    activeRole: {
      type: Object,
      default: function() {
        return {}
      }
    }
  },
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('请输入正确的手机号'))
      } else {
        callback()
      }
    }
    return {
      title: '添加**',
      state: 0,
      SysUser: {
        sysUserId: '',
        sysUserPhone: '',
        sysUserName: '',
        password: '',
        salt: '',
        sysRoleId: ''
      },
      querySysUser: {
        sysUserPhone: '',
        sysUserName: ''
      },
      SysRoleDetail: {},
      rule4SysUserform: {
        sysUserPhone: [
          { required: true, trigger: 'blur', validator: validateUsername }
        ],
        sysUserName: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      },
      addModal: false,
      tableData: [],
      currentPage: 1,
      pageSize: 12,
      tableTotal: 0,
      tableLoading: false
    }
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ])
  },
  watch: {
    activeRole(newData) {
      this.activeRole = newData
      this.gettabledata()
    }
  },
  /* 初始化 */
  created() {
    this.tablePageChange()
    const _this = this
    document.onkeydown = function() {
      if (window.event.keyCode === 13) {
        _this.gettabledata()
      }
    }
  },
  methods: {
    /* 查询 */
    gettabledata(start = 0, length = this.pageSize) {
      const sysRoleIds = this.userInfo.sysRoleId[0]
      this.$http.sysUserAllPages({
        'start': start,
        'length': length,
        'sysRoleId': this.activeRole.sysRoleId ? this.activeRole.sysRoleId : sysRoleIds,
        ...this.querySysUser
      }).then((res) => {
        this.tableLoading = false
        if (res.result) {
          this.tableData = res.obj.data
          this.tableTotal = res.obj.recordsTotal
        } else {
          this.$message.error(res.description)
        }
      }).catch(() => {
        this.tableLoading = false
      })
    },
    /* 分页 */
    queryTablePageChange() {
      this.currentPage = 1
      this.gettabledata(0, this.pageSize)
    },
    /* 分页 */
    tablePageChange() {
      this.gettabledata((this.currentPage - 1) * this.pageSize, this.pageSize)
    },
    handleCurrentChange(data) {
      this.SysUser = data
    },
    confirmData() {
      if (!this.SysUser.sysUserId) {
        this.$message.error('请选择人员')
        return false
      }
      this.$emit('sysUserInfo', this.SysUser)
    }
  }
}
</script>
<style lang="scss" scoped>
  .sys-user {
    /deep/.el-table__fixed::before {
      display: none;
    }
    .add-btn {
      margin-bottom: 25px;
      width: 140px;
      border-color: #f7b538;
      background-color: #f7b538;
    }
    /deep/ .el-dialog {
      .el-dialog__footer {
        text-align: center;
        .el-button {
          width: 150px;
          &.el-button--primary {
            background-color: #f7b538;
            border-color: #f7b538;
          }
        }
      }
      @media (min-width: 992px) {
        .addPeo {
          margin: 0 auto;
          width: 60%;
        }
      }
      @media (max-width: 992px) {
        .addPeo {
          .el-form-item {
            position: relative;
            height: 40px;
            overflow: hidden;
            .el-form-item__label {
              display: none;
            }
            .el-form-item__content {
              width: 100%;
              position: absolute;
              right: 0;
              top: 0;
            }
          }
        }
      }
    }

  }
</style>
