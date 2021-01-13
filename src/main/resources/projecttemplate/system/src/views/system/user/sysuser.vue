<template>
  <el-row class="sys-user">
    <ListScreeningSearch
      :searchList="searchList"
      :searchData="searchData"
      @queryTablePageChange="queryTablePageChange"
    ></ListScreeningSearch>
    <div class="top-operation">
      <div class="item add">
        <el-button type="primary" class="add-btn" @click="openadditem">添加人员</el-button>
      </div>
    </div>
    <el-table id="tableId" ref="table" v-loading="tableLoading" border stripe :data="tableData">
      <!--      <el-table-column fixed align="center" width="280px" label="微信客服">-->
      <!--        <template slot-scope="scope">-->
      <!--          <div v-if="currentMatchingKf(scope.row) !== undefined" class="wechat-box">-->
      <!--            <Img v-if="currentMatchingKf(scope.row).kf_headimgurl" :src="currentMatchingKf(scope.row).kf_headimgurl" class="avatar-i" alt="" />-->
      <!--            <div class="wechat-box-r">-->
      <!--              <div class="wechat-item">-->
      <!--                <span class="wechat-name">微信昵称：</span>-->
      <!--                <span class="wechat-name-r">{{ currentMatchingKf(scope.row).kf_nick }}</span>-->
      <!--              </div>-->
      <!--              <div class="wechat-item">-->
      <!--                <span class="wechat-name">微信号：</span>-->
      <!--                <span class="wechat-name-r">{{ currentMatchingKf(scope.row).kf_wx }}</span>-->
      <!--              </div>-->
      <!--              <span class="btn-text" @click="unBindWeChat(scope.row)">解除绑定</span>-->
      <!--            </div>-->
      <!--          </div>-->
      <!--          <div v-else class="bind-box">-->
      <!--            <span class="btn-text" @click="bindWeChatKf(scope.row)">绑定微信</span>-->
      <!--          </div>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column fixed align="center" prop="sysUserPhone" label="手机号" width="160" />
      <el-table-column fixed align="center" prop="sysUserName" label="用户名" />
      <el-table-column fixed align="center" prop="sysRoleName" label="角色" />
      <el-table-column fixed align="center" prop="deleteState" label="状态">
        <template slot-scope="scope">
          {{ $store.getters.getDict('deleteState', scope.row.deleteState) }}
        </template>
      </el-table-column>
      <el-table-column fixed align="center" label="操作" width="150px">
        <template slot-scope="scope">
          <span class="btn" @click="openedit(scope.row)">编辑</span>
          <span v-if="scope.row.deleteState === 2" class="btn" @click="lizhi(scope.row)">离职</span>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      layout="prev, pager, next"
      :total="tableTotal+1"
      :current-page.sync="currentPage"
      :page-size.sync="pageSize"
      @current-change="tablePageChange"
    />
    <el-dialog :visible.sync="addModal" :title="title">
      <el-form ref="SysUserform" class="addPeo" :model="SysUser" label-width="120px" :rules="rule4SysUserform">
        <el-form-item label="角色">
          <el-input :value="state === 1 ? SysRoleDetail.sysRoleName : activeRole.sysRoleName" placeholder="请输入角色" disabled />
        </el-form-item>
        <el-form-item label="用户名" prop="sysUserName">
          <el-input v-model="SysUser.sysUserName" placeholder="请输入用户名" :disabled="isRegistered" />
        </el-form-item>
        <el-form-item label="手机号" prop="sysUserPhone">
          <el-input v-model.number="SysUser.sysUserPhone" placeholder="请输入手机号" />
          <!--          <el-input v-model.number="SysUser.sysUserPhone" placeholder="请输入手机号" @change="checkPhone" />-->
        </el-form-item>
        <el-form-item v-if="state !== 1 && !isRegistered" label="用户密码" prop="sysUserPassword">
          <el-input v-model="SysUser.sysUserPassword" type="password" placeholder="请输入用户密码" :disabled="isRegistered" autocomplete="off" clearable />
        </el-form-item>
        <el-form-item label="头像" prop="sysUserAvatar">
          <UploadImages v-model="SysUser.sysUserAvatar" :imgUrl="SysUser.sysUserAvatar" :openCropper="true" />
        </el-form-item>
        <el-form-item label="客服昵称" prop="sysUserNickName">
          <el-input v-model="SysUser.sysUserNickName" placeholder="客服人员请输入昵称" :disabled="isRegistered" autocomplete="off" />
        </el-form-item>
        <el-form-item label="客服微信" prop="kfWx">
          <el-input v-model="SysUser.kfWx" placeholder="客服人员请输入微信号" :disabled="isRegistered" autocomplete="off" />
        </el-form-item>
        <el-form-item label="部门">
          <el-input v-model="SysUser.ddDepartmentName" placeholder="请输入部门" :disabled="isRegistered" autocomplete="off" />
        </el-form-item>
        <el-form-item label="工作手机号" prop="workPhone">
          <el-input v-model="SysUser.workPhone" placeholder="请输入工作手机号" :disabled="isRegistered" />
        </el-form-item>
        <el-form-item label="QQ">
          <el-input v-model="SysUser.qq" placeholder="请输入QQ号" :disabled="isRegistered" />
        </el-form-item>
        <el-form-item label="钉钉账号">
          <el-input v-model="SysUser.ddUserId" placeholder="请输入钉钉账号" :disabled="isRegistered" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="SysUser.email" placeholder="请输入邮箱" :disabled="isRegistered" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button v-if="!isRegistered" type="primary" size="large" @click="addoredit">确认</el-button>
        <el-button v-else type="primary" size="large" @click="changeRole">确认转岗</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="isShowResignation" width="890px" title="离职交接" @close="closeLizhi">
      <Resignation ref="resignation" :curLeaveData="curLeaveData" :isShowResignation.sync="isShowResignation" @reloadData="tablePageChange"></Resignation>
    </el-dialog>
  </el-row>
</template>

<script>
import { userPassword, vstring, vphone } from '@/utils/validate'
import { mapGetters } from 'vuex'
import ListScreeningSearch from '@/components/ListScreeningSearch/ListScreeningSearch'
import Resignation from './resignation'
import UploadImages from '@/components/UploadImages/UploadImages'

export default {
  name: 'SysUser',
  components: {
    ListScreeningSearch,
    Resignation,
    UploadImages
  },
  props: {
    activeRole: {
      type: Object,
      default: function() {
        return {}
      }
    }
  },
  data() {
    return {
      curLeaveData: {},
      sysUserInfo: {},
      isShowResignation: false,
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
        sysUserPassword: '',
        workPhone: '',
        email: '',
        qq: '',
        salt: '',
        departmentName: '',
        ddUserId: '',
        photo: '',
        sysRoleName: '',
        kfWx: '',
        sysUserNickName: '${project.artifactName}客服'
      },
      SysRoleDetail: {},
      rule4SysUserform: {
        sysUserPhone: vphone,
        sysUserName: vstring,
        sysUserAvatar: vstring,
        sysUserPassword: userPassword,
        departmentName: vstring
      },
      addModal: false,
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      tableTotal: 0,
      tableLoading: false,
      allWeChatKfList: []
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
    this.gettabledata()
    var lett = this
    document.onkeydown = function(e) {
      if (window.event.keyCode === 13) {
        lett.gettabledata()
      }
    }
  },
  methods: {
    // 当前匹配客服
    currentMatchingKf(item) {
      return this.allWeChatKfList.find((_item, index) => {
        return item.kfWx === _item.kf_wx
      })
    },
    // 解绑微信客服
    unBindWeChat(item) {
      const unBindObj = this.allWeChatKfList.find((_item, index) => {
        return item.kfWx === _item.kf_wx
      })
      this.$http.wxkfDeleteWxkf({
        ...unBindObj
      }).then((res) => {
        if (res.result) {
          item.kfWx = null
          this.$http.sysUserupdate({ ...item }).then((res) => {
            if (res.result) {
              this.$message.success(res.description)
              this.tablePageChange()
            } else {
              console.log(res.description)
            }
          })
        } else {
          this.$message.error(res.description)
        }
      })
    },
    closeLizhi() {
      this.$refs.resignation.closeDialog()
      this.gettabledata()
    },
    closeAddwechatKf() {
      // this.$refs.resignation.closeDialog()
      this.gettabledata()
    },
    // 打开离职交接弹窗
    openResignation(item) {
      this.curLeaveData = item
      this.isShowResignation = true
    },
    changeData(e) {
      if (e) {
        this.gettabledata()
      }
    },
    /* 通过state判断添加还是修改 */
    addoredit() {
      if (this.state === 0) {
        this.isRegistered ? this.edititem() : this.additem()
      } else {
        this.edititem()
      }
    },
    /* 增加 */
    openadditem() {
      if (this.activeRole.sysRoleId === '') {
        this.$message.error('请选择角色')
        return false
      }
      if (this.$refs.SysUserform) {
        this.$refs.SysUserform.resetFields()
      }
      this.isRegistered = false
      this.SysUser = {
        sysUserId: '',
        sysUserPhone: '',
        sysUserName: '',
        workPhone: '',
        sysUserPassword: '',
        email: '',
        qq: '',
        salt: '',
        // departmentName: '',
        ddDepartmentName: '',
        ddUserId: '',
        photo: '',
        kfWx: '',
        sysUserNickName: '${project.artifactName}客服'
      }
      this.addModal = true
      this.title = '添加人员'
      this.state = 0
    },
    additem() {
      this.$refs['SysUserform'].validate((valid) => {
        if (valid) {
          const adduser = this.SysUser
          adduser.sysRoleId = this.activeRole.sysRoleId
          this.$http.sysUseradd(adduser).then((res) => {
            if (res.result) {
              this.$message.success(res.description)
              this.addModal = false
              this.tablePageChange()
              this.$nextTick(() => {
                this.$refs['SysUserform'].resetFields()
              })
            } else {
              this.$message.error(res.description)
            }
          })
        } else {
          this.$message.error('请检查提交信息')
        }
      })
    },
    /* 修改 */
    openedit(item) {
      if (this.$refs.SysUserform) {
        this.$refs.SysUserform.resetFields()
      }
      this.isRegistered = false
      this.addModal = true
      this.title = '修改信息'
      this.state = 1
      this.getSysRoleById(item.sysRoleId)
      this.$nextTick(() => {
        this.SysUser = JSON.parse(JSON.stringify(item))
      })
    },
    getSysRoleById(sysRoleId) {
      this.$http.sysRoledetail({ sysRoleId }).then(res => {
        if (res.result) {
          this.SysRoleDetail = JSON.parse(JSON.stringify(res.obj))
        }
      })
    },
    edititem() {
      this.$refs['SysUserform'].validate((valid) => {
        if (valid) {
          this.$http.sysUserupdate(this.SysUser).then((res) => {
            if (res.result) {
              this.$message.success(res.description)
              this.addModal = false
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
    changeRole() {
      this.$refs['SysUserform'].validate((valid) => {
        if (valid) {
          this.$http.sysUserupdateDefault({ ...this.SysUser, sysRoleId: this.activeRole.sysRoleId }).then((res) => {
            if (res.result) {
              this.$message.success(res.description)
              this.addModal = false
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
    /* 删除 */
    deleteitem(item) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.sysUserdelete({
          'sysUserId': item.sysUserId
        }).then((res) => {
          if (res.result) {
            this.$message.success(res.description)
            this.tablePageChange()
          } else {
            this.$message.error(res.description)
          }
        })
      })
    },
    // 人员离职方法
    lizhi(item) {
      this.$confirm('确认离职该员工吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        item.deleteState = 1
        this.$http.sysUserupdate({
          ...item
        }).then((res) => {
          if (res.result) {
            this.$message.success(res.description)
            this.tablePageChange()
          } else {
            this.$message.error(res.description)
          }
        })
      })
    },
    /* 查询 */
    gettabledata(start = 0, length = this.pageSize) {
      const sysRoleIds = this.userInfo.sysRoleId[0]
      this.$http.sysUserpages({
        'start': start,
        'length': length,
        'sysRoleId': this.activeRole.sysRoleId ? this.activeRole.sysRoleId : sysRoleIds,
        ...this.searchData,
        deleteState: 2
      }).then((res) => {
        this.tableLoading = false
        if (res.result) {
          this.tableData = res.obj
          this.tableTotal = res.records
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
    // tablePageChange() {
    //   this.gettabledata((this.currentPage - 1) * this.pageSize, this.pageSize)
    //   if (document.body.clientWidth < 1000) {
    //     document.getElementById('tableId').scrollIntoView()
    //   }
    // },
    tablePageChange() {
      this.gettabledata(this.currentPage, this.pageSize)
    }
  }
}
</script>
<style lang="scss" scoped>
  .sys-user {
    .add-btn {
      width: 140px;
      @media(max-width: 990px) {
        width: 130px;
        height: 32px;
        line-height: 32px;
        font-size: 14px;
        padding: 0;
        margin-left: 10px;
      }
    }
    .check-btn {
      width: 70px;
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
          width: 80%;
        }

        /deep/ .el-form-item__label {
          width: 84px!important;
        }

        /deep/ .el-form-item__content {
          margin-left: 84px!important;
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
    .wechat-box {
      display: flex;
      align-items: center;
      text-align: center;
    }
    .bind-box {
      text-align: center;
    }
    .wechat-item {
      display: flex;
      align-items: center;
    }
    .wechat-name-r {
      flex: 1;
    }
    .wechat-box-r {
      flex: 1;
      display: flex;
      flex-direction: column;
      padding-left: 20px;
      text-align: left;
    }
    .wechat-name {}
    .avatar-i {
      display: block;
      width: 60px;
      height: 60px;
    }
    .btn-text {
      color: #f7b538;
      cursor: pointer;
    }
  }
</style>
