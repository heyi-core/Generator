<template>
  <div class="app-container">
    <el-row class="save-line">
      <el-col :span="24">
        <el-button class="save-btn" @click="savRole">保存</el-button>
        <my-tips>
          <ul>
            <li>
              角色分上下级
            </li>
            <li>
              不同的角色拥有不同的权限，看到的内容不一样
            </li><li>
              只有选中功能模块或者按钮才可以被看到
            </li>
            <li>
              只有选中API才可以有请求权限
            </li>
            <li>
              只有删除下级所有角色跟人员才可以删除
            </li>
            <li>
              新增角色不要忘记修改名称
            </li>
            <li>
              编辑好后不要忘记保存
            </li>
          </ul>
        </my-tips>
      </el-col>
    </el-row>
    <el-row class="role-wrapper" :gutter="18">
      <el-col :sm="8" class="left">
        <div class="model model-tree">
          <div class="modal-title">组织结构图</div>
          <div class="content">
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
            >
              <span slot-scope="{ node, data }" class="tree-line">
                <span v-show="!node.isEdit" class="name">
                  <span>{{ node.label }}</span>
                </span>
                <span v-show="node.isEdit">
                  <el-input
                    :ref="'slotTreeInput'+data.id"
                    v-model="data.sysRoleName"
                    class="slot-t-input"
                    size="mini"
                    autofocus
                    @click.stop.native=""
                    @blur.stop="NodeBlur(node, data)"
                    @keyup.enter.native="NodeBlur(node, data)"
                  />
                </span>
                <span class="cz">
                  <span class="btn" @click.stop="append(node, data)"><i class="el-icon-plus"></i></span>
                  <span class="btn" @click.stop="editItem(node, data)"><i class="el-icon-edit"></i></span>
                  <span class="btn" @click.stop="deleteItem(node, data)"><i class="el-icon-delete"></i></span>
                </span>
              </span>
            </el-tree>
          </div>
        </div>
      </el-col>
      <el-col :sm="8" class="center">
        <div v-show="activeRole.sysRoleId" class="model model-router">
          <div class="modal-title">功能模块</div>
          <div class="content">
            <roles-set ref="routerSet" :active-role="activeRole" @savename="savename" />
          </div>
        </div>
      </el-col>
      <el-col :sm="8" class="right">
        <div v-show="activeRole.sysRoleId && activeName" class="model model-btn">
          <div class="modal-title">按钮</div>
          <div class="content">
            <operation-roles :active-role="activeRole" :active-name="activeName" @saveBtn="saveBtn" />
          </div>
        </div>
        <!--        <div v-show="activeRole.sysRoleId && activeName" class="model model-api">-->
        <!--          <div class="modal-title">API</div>-->
        <!--          <div class="content">-->
        <!--            <api-roles :active-role="activeRole" :active-name="activeName" @saveApi="saveApi" />-->
        <!--          </div>-->
        <!--        </div>-->
      </el-col>
    </el-row>
  </div>
</template>

<script>
import RolesSet from './rolesSet'
import OperationRoles from './OperationRoles'

export default {
  name: 'DemoRole',
  components: {
    RolesSet,
    OperationRoles
  },
  data() {
    return {
      activeRole: {
        api: '',
        button: '',
        createTime: '',
        creator: '',
        parentSysRoleId: '',
        router: '',
        sysRoleId: '',
        sysRoleName: ''
      },
      treeSet: {
        label: 'sysRoleName',
        children: 'children'
      },
      roleTree: [],
      activeSysRoleId: undefined,
      activeName: ''
    }
  },
  methods: {
    loadNode(node, resolve) {
      const pId = node.key ? node.key : '0'
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
    },
    openSet(data) {
      delete data.children
      this.activeName = ''
      this.$http.sysRoledetail({ sysRoleId: data.sysRoleId }).then(res => {
        this.activeRole = JSON.parse(JSON.stringify(res.obj))
      })
      return false
    },
    append(node, data) {
      // 首先展开节点
      this.$http.sysRoleadd({ api: '', button: '', router: '', parentSysRoleId: data.sysRoleId, sysRoleName: '新增角色' }).then(res => {
        if (res.result) {
          if (!node.loaded) {
            node.loadData()
          } else {
            this.$refs.oztree.append({
              ...res.obj,
              children: []
            }, node)
          }
          if (!node.expanded) {
            node.expanded = true
          }
        }
      })
    },
    NodeBlur(node, data) {
      if (node.isEdit) {
        this.$set(node, 'isEdit', false)
      }
      this.$http.sysRoleupdate({ ...data }).then(res => {
        if (res.result) {
          this.$message.success('修改成功')
        } else {
          this.$message.error('修改失败')
        }
      })
      this.$nextTick(() => {
        this.$refs['slotTreeInput' + data.id].$refs.input.focus()
      })
    },
    editItem(node, data) {
      this.$set(node, 'isEdit', true)
    },
    deleteItem(node, data) {
      this.$confirm('是否删除此节点？', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.sysRoledelete(data).then(res => {
          if (res.result) {
            const parent = node.parent
            const children = parent.childNodes || parent
            const index = children.findIndex(d => d.key === data.sysRoleId)
            children.splice(index, 1)
            this.$message.success('删除成功！')
          } else {
            this.$message.error(res.description)
          }
        })
      }).catch(() => {})
    },
    savename(name) {
      this.activeName = name
    },
    saveApi(allApi) {
      this.activeRole.api = JSON.stringify(allApi)
    },
    saveBtn(allBtn) {
      this.activeRole.button = JSON.stringify(allBtn)
    },
    savRole() {
      this.activeRole.router = this.$refs.routerSet.getRouterRoules().length > 0 ? JSON.stringify(this.$refs.routerSet.getRouterRoules()) : ''
      this.$nextTick(() => {
        if (this.activeRole.sysRoleId) {
          this.$http.sysRoleupdate(this.activeRole).then(res => {
            if (res.result) {
              this.$message.success(res.description)
            } else {
              this.$message.error(res.description)
            }
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .app-container {
    padding: 20px;
    .save-line {
      padding-bottom: 23px;
      .save-btn {
        width: 140px;
        background-color: #F7B538;
        color: #fff;
        border-color: #F7B538;
      }
    }
    .role-wrapper {
      .model {
        padding: 8px;
        background-color: #fff;
        border-radius: 5px;
        overflow: hidden;
        .modal-title {
          position: relative;
          padding: 0 30px;
          height: 52px;
          line-height: 56px;
          border-bottom: 1px solid #d8d8d8;
          font-size: 16px;
          color: #333;
          &::before {
            position: absolute;
            left: 12px;
            top: 22px;
            display: block;
            width: 4px;
            height: 12px;
            content: '';
            background-color: #f7b538;
          }
        }
        .content {
          min-height: 600px;
          padding: 18px 10px;
        }
        &.model-btn, &.model-api {
          .content {}
        }
        &.model-tree {
          /deep/ .org-tree {
            .el-tree-node:focus>.el-tree-node__content{
              background-color: #fef7eb;
            }
            .el-tree-node.is-current>.el-tree-node__content {
              background-color: #fef7eb;
            }
            .el-tree-node__content {
              position: relative;
              height: 40px;
              line-height: 40px;
              font-size: 16px;
              color: #888;
              span.cz {
                display: none;
                position: absolute;
                top: 0;
                right: 5px;
                .btn {
                  padding: 5px;
                  &:hover {
                    color: #f7b538;
                  }
                }
              }
              &:hover {
                background-color: #fef7eb;
                span.cz {
                  display: block;
                }
              }
            }
          }
        }
        &.model-api{
          margin-top: 10px;
        }
      }
    }
  }
  @media (max-width: 768px) {
    .app-container {
      .role-wrapper {
        .model {
          .content {
            min-height: auto;
          }
          &.model-btn, &.model-api {
            .content {
              min-height: auto;
            }
          }
        }
      }
    }
  }
</style>
