<template>
  <el-row class="sys-user">
    <el-table id="tableId" ref="table" v-loading="tableLoading" border stripe :data="tableData">
      <el-table-column fixed align="center" prop="departmentName" label="部门" />
      <el-table-column fixed align="center" width="110px" prop="sysUserPhone" label="手机号" />
      <el-table-column fixed align="center" prop="sysUserName" label="用户名" />
      <el-table-column fixed align="center" prop="sysRoleName" label="角色" />
      <el-table-column fixed align="center" label="操作">
        <template slot-scope="scope">
          <span class="btn" @click="transferRights(scope.row)">转移</span>
        </template>
      </el-table-column>
    </el-table>
    <div v-if="transfersList.length > 0 || transferNameList.length > 0">
      <div class="transfer-title">
        是否转移被离职人员的所有资料(例如店铺、店铺订单、商标、商标订单、店铺合同、商标合同、客户信息资料)给当前选择人？
      </div>
      <div class="transfer-box">
        <el-tag
          v-for="(transfer, index) in transferNameList"
          :key="index"
          closable
          type="warning"
          class="transfer-item"
          @close="removeTransfer(transfer)"
        >
          {{ transfer }}
        </el-tag>
        <div class="determine-transfer" @click="determineTransfer">确定</div>
      </div>
    </div>
    <el-pagination
      background
      layout="prev, pager, next"
      :total="tableTotal+1"
      :current-page.sync="currentPage"
      :page-size.sync="pageSize"
      @current-change="tablePageChange"
    />
  </el-row>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'LeaveSysUser',
  props: {
    activeRole: {
      type: Object,
      default: function() {
        return {}
      }
    },
    leaveData: {
      type: [Array, Object]
    }
  },
  data() {
    return {
      isShowResignation: false,
      sysRoleIds: '',
      transfersList: [],
      transferNameList: [],
      searchList: [
        { id: 'sysUserPhone', name: '手机号', type: 'input' },
        { id: 'sysUserName', name: '系统用户名', type: 'input' }
      ],
      searchData: {
        sysUserPhone: '',
        sysUserName: ''
      },
      isRegistered: false,
      title: '添加**',
      state: 0,
      SysUser: {
        sysUserId: '',
        sysUserPhone: '',
        sysUserName: '',
        password: '',
        workPhone: '',
        email: '',
        qq: '',
        salt: '',
        departmentName: '',
        ddUserId: '',
        photo: '',
        sysRoleName: ''
      },
      SysRoleDetail: {},
      rule4SysUserform: {
        sysUserPhone: this.vphone,
        sysUserName: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: this.vpasswoed,
        departmentName: [
          { required: true, message: '请输入部门', trigger: 'blur' }
        ],
        email: this.vemail
      },
      addModal: false,
      tableData: [],
      currentPage: 1,
      pageSize: 10,
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
    this.transfersList = []
    this.transferNameList = []
    this.gettabledata()
    var lett = this
    document.onkeydown = function(e) {
      if (window.event.keyCode === 13) {
        lett.gettabledata()
      }
    }
  },
  methods: {
    closeDialog() {
      this.sysRoleIds = ''
      this.transfersList = []
      this.transferNameList = []
    },
    // 添加到数组
    transferRights(item) {
      if (this.transfersList.indexOf(item.sysUserId) === -1 || this.transferNameList.indexOf(item.sysUserName) === -1) {
        this.transfersList.push(item.sysUserId)
        this.transferNameList.push(item.sysUserName)
      } else {
        this.$message({
          message: '当前人员已添加',
          type: 'warning'
        })
      }
    },
    removeTransfer(tag) {
      this.transfersList.splice(this.transfersList.indexOf(tag), 1)
      this.transferNameList.splice(this.transferNameList.indexOf(tag), 1)
    },
    determineTransfer() {
      this.$http.sysUserManyPeopleTransfer({
        sysUserId: this.leaveData.sysUserId,
        transferUsers: this.transfersList
      }).then((res) => {
        if (res.result) {
          this.$message({
            type: 'success',
            message: res.description
          })
          this.$emit('reloadData')
          this.$emit('closeDialog')
        } else {
          this.$message({ type: 'warning', message: res.description })
        }
      })
    },
    // 打开离职交接弹窗
    openResignation() {
      this.isShowResignation = true
    },
    /* 查询 */
    gettabledata(start = 0, length = this.pageSize) {
      this.sysRoleIds = this.userInfo.sysRoleId[0]
      this.$http.sysUserallPages({
        'start': start,
        'length': length,
        'sysRoleId': this.activeRole.sysRoleId ? this.activeRole.sysRoleId : this.sysRoleIds,
        ...this.searchData
      }).then((res) => {
        this.tableLoading = false
        if (res.result) {
          this.tableData = res.obj.data
          this.tableTotal = res.obj.recordsTotal
        } else {
          this.$message.error(res.description)
        }
      })
    },
    /* 分页 */
    tablePageChange() {
      this.gettabledata((this.currentPage - 1) * this.pageSize, this.pageSize)
      if (document.body.clientWidth < 1000) {
        document.getElementById('tableId').scrollIntoView()
      }
    }
  }
}
</script>
<style lang="scss" scoped>
  .sys-user {
    .transfer-title {
      margin-top: 10px;
      font-size: 16px;
      color: #333;
    }
    .transfer-box {
      margin-top: 10px;
      .transfer-item {
        margin-right: 14px;
        margin-bottom: 10px;
      }
    }
    .determine-transfer {
      padding: 8px 20px;
      max-width: 100px;
      background-color: #f7b538;
      text-align: center;
      border-radius: 4px;
      cursor: pointer;
      color: #ffffff;
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
    }
  }
  /deep/ .wrapper.right-wrapper {
    padding: 0;
  }
</style>

