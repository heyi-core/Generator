<template>
  <el-tooltip style="float: right;font-size: 20px" effect="light" placement="left-end">
    <div slot="content" style="font-size: 18px">
      <slot></slot>
    </div>
    <i class="el-icon-info" style="color: grey"></i>
  </el-tooltip>
</template>

<script >
export default {
  name: 'Tips',
  data() {
    return {

    }
  }
}

</script>
