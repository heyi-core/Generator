<template>
  <div class="page-list-search">
    <el-form label-width="100px" :model="searchData">
      <div v-for="item in listLine" :key="item.id" class="list-margin">
        <div class="line-name">{{ item.name }}</div>
        <div class="line-content">
          <div v-if="item.isUnlimited" class="label-radio unlimited-style" :class="{'active': searchData[item.id] === ''}">
            <span @click="selectUnlimited(item.id)">不限</span>
          </div>
          <div v-for="(dict, index) in item.isDictionaries ? getDicts(item.id, true) : item.itemList" :key="index" class="label-radio unlimited-style" :class="{'active': searchData[item.id] === dict.dictCode}">
            <el-tooltip v-if="dict.dictState && dict.dictState === 3" content="该员工已离职" placement="top" effect="light">
              <span style="text-decoration:line-through;text-decoration-color: red;" @click="selectData(item.id, dict.dictCode)">{{ dict.dictName }}</span>
            </el-tooltip>
            <span v-else @click="selectData(item.id, dict.dictCode)">{{ dict.dictName }}</span>
          </div>
        </div>
      </div>
      <div v-if="isSortScreening" class="list-margin">
        <div class="line-name">排序筛选</div>
        <div class="line-content sort-form">
          <div class="label-radio unlimited-style" :class="{'active': searchData.orderField === ''}" style="float: left">
            <span @click="changeOrderType('')">默认</span>
          </div>
          <div class="sort-item" :class="[{'active': searchData.orderField === 'create_time'}, [ searchData.orderField === 'create_time' ? searchData.orderType : '' ]]">
            <div class="btn" @click="changeOrderType('create_time')">
              <span>时间排序</span>
              <span class="right-icon">
                <i class="el-icon-caret-top"></i>
                <i class="el-icon-caret-bottom"></i>
              </span>
            </div>
          </div>
          <div class="sort-item" :class="[{'active': searchData.orderField === 'shop_price'}, [ searchData.orderField === 'shop_price' ? searchData.orderType : '' ]]">
            <div class="btn" @click="changeOrderType('shop_price')">
              <span>价格排序</span>
              <span class="right-icon">
                <i class="el-icon-caret-top"></i>
                <i class="el-icon-caret-bottom"></i>
              </span>
            </div>
          </div>
        </div>
      </div>
      <div v-if="listLineItem.length > 0" class="list-margin">
        <div class="line-name" style="margin-top: 3px">其他筛选</div>
        <div v-for="(item, index) in listLineItem" :key="index" class="label-input">
          <template v-if="item.type === 'input'">
            <el-input v-model="searchData[item.id]" clearable :placeholder="'请输入' + item.name" @clear="clearData" @keyup.enter.native="queryTableData"></el-input>
          </template>
          <template v-if="item.type === 'datepicker'">
            <el-date-picker v-model="searchData[item.id]" clearable type="date" :placeholder="'请选择' + item.name" @change="changeDate(searchData[item.id])" @keyup.enter.native="queryTableData"></el-date-picker>
          </template>
          <template v-if="item.type === 'cascader'">
            <el-cascader v-model="searchData[item.id]" :options="item.options" clearable :placeholder="'请输入' + item.name" @change="changeCascader"></el-cascader>
          </template>
        </div>
        <el-button type="primary" class="query-btn" @click="queryTableData">搜索</el-button>
      </div>
    </el-form>
  </div>
</template>
<script>
export default {
  name: 'PageListSearch',
  props: {
    searchList: {
      type: Array
    },
    searchData: {
      type: Object
    },
    isShopCategory: {
      type: Boolean,
      default: false
    },
    shopCategoryName: {
      type: String
    },
    isSortScreening: {
      type: Boolean,
      default: false
    },
    isShopTrademarkClassify: {
      type: Boolean,
      default: false
    },
    shopTrademarkClassifyName: {
      type: String
    }
  },
  data() {
    return {
      heightBody: '',
      listLine: [],
      listLineItem: [],
      shopCategories: [],
      oneLevelCategory: [],
      publicShopCategory: [],
      publicShopTrademarkClassify: [],
      tableIsShow: true
    }
  },
  created() {
    this.getViewList()
  },
  methods: {
    // 整理传进来的数据
    getViewList() {
      this.searchList.forEach(item => {
        item.type === 'radio' ? this.listLine.push(item) : this.listLineItem.push(item)
      })
    },
    // 选择单选
    selectData(propsName, propsValue) {
      if (propsName === 'myCustomer') {
        this.searchData[propsName] = !this.searchData[propsName]
      } else {
        this.searchData[propsName] === '' || this.searchData[propsName] !== propsValue ? this.searchData[propsName] = propsValue : this.searchData[propsName] = ''
      }
      this.queryTableData()
    },
    // 选择不限
    selectUnlimited(propsName) {
      if (this.searchData[propsName] !== '') {
        this.searchData[propsName] = ''
        this.queryTableData()
      }
    },
    // 清空input框数据,触发查询
    clearData() {
      this.queryTableData()
    },
    // 清空时间,触发查询
    changeDate(value) {
      if (!value) {
        this.queryTableData()
      }
    },
    changeCascader(data) {
      this.queryTableData()
    },
    // 触发父页面的查询
    queryTableData() {
      this.$emit('queryTablePageChange')
    },
    // 网店类目选择一级类目
    selectShopCategory(value) {
      this.oneLevelCategory = []
      this.oneLevelCategory = value.child
    },
    // 删除已选类目
    removeDomain(index) {
      this.publicShopCategory.splice(index, 1)
    },
    changeOrderType(orderField) {
      if (this.searchData.orderField || orderField) {
        if (orderField) {
          if (this.searchData.orderField === orderField) {
            this.searchData.orderType = this.searchData.orderType === 'desc' ? 'asc' : 'desc'
          } else {
            this.searchData.orderField = orderField
            this.searchData.orderType = 'desc'
          }
        } else {
          this.searchData.orderField = ''
          this.searchData.orderType = ''
        }
      }
      this.queryTableData()
    }
  }
}
</script>

<style lang="scss" scoped>
  .page-list-search{
    margin-bottom: 10px;
    width: 100%;
    top: 100px;
    right: 0;
    padding: 5px 40px 20px 40px;
    border-radius: 8px;
    z-index: 1500;
    box-sizing: border-box;
    background-color: #fff;
    .list-top-operation{
      display:flex;
      justify-content: space-between;
      align-items: center;
      .table-isShow {
        color: #f7b538;
        font-size: 14px;
        &:hover {
          cursor: pointer;
        }
      }
    }
    .list-margin{
      margin-top: 10px;
      /*&:nth-of-type(1) {*/
      /*  margin-top: 0;*/
      /*}*/
    }
    .line-name {
      float: left;
      min-width: 70px;
      height: 25px;
      line-height: 25px;
      font-size: 14px;
      color: #333333;
      font-weight: bold;
      & + .line-content {
        margin-left: 70px;
      }
    }
    .line-content {
      font-size: 0;
      .unlimited-style{
        &:nth-of-type(1){
          padding-left: 10px;
        }
      }
      .label-radio {
        display: inline-block;
        min-width: 90px;
        height: 25px;
        line-height: 25px;
        >span {
          display: inline-block;
          padding: 0 12px;
          font-size: 14px;
          color: #666666;
          border-radius: 4px;
          &:hover{
            cursor: pointer;
          }
        }
        &.active {
          >span {
            background-color: #f7b538;
            color: #fff;
          }
        }
      }
      .item-shopCategory{
        margin-left: 90px;
      }
    }
    /deep/ .label-input{
      display: inline-block;
      margin-left: 10px;
      .el-input{
        .el-input__inner{
          width: 160px;
          height: 32px;
        }
        /deep/ .el-input_suffix{
          margin-right: 5px;
        }
      }
    }
    /deep/ .el-date-editor.el-input, .el-date-editor.el-input__inner{
      width: 160px;
      .el-input__icon{
        line-height: 0;
      }
    }
    .el-button{
      width: 100px;
      height: 32px;
      padding: 0 20px;
      &.active{
        border: 1px solid #ffac0d;
        color: #ffac0d;
        background-color: #fff;
      }
      &.query-btn{
        margin-left: 5px;
      }
    }
    .sort-form {
      .sort-item {
        display: inline-block;
        font-size: 14px;
        color: #5c5c5c;
        margin-left: 10px;
        &:nth-of-type(3){
          margin-left: 20px;
        }
        .btn {
          height: 24px;
          line-height: 24px;
          cursor: pointer;
          .right-icon {
            display: inline-block;
            vertical-align: top;
            position: relative;
            font-size: 12px;
            width: 12px;
            height: 24px;
            color: #ccc;
            .el-icon-caret-top {
              position: absolute;
              top: 3px;
              left: 0;
            }
            .el-icon-caret-bottom {
              position: absolute;
              bottom: 3px;
              left: 0;
            }
          }
        }
        &::after {
          content: '';
          display: block;
          position: absolute;
          height: 16px;
          width: 1px;
          background-color: #bfbfbf;
          top: 50%;
          right: 0;
          transform: translate(0, -50%);
        }
        &:nth-of-type(1) {
          padding-left: 0;
        }
        &:last-of-type{
          &::after {
            display: none;
          }
          padding-right: 0;
        }
        &.active {
          color: #f7b538;
        }
        &.desc {
          .el-icon-caret-bottom {
            color: #f7b538;
          }
        }
        &.asc {
          .el-icon-caret-top {
            color: #f7b538;
          }
        }
      }
    }
  }
</style>
