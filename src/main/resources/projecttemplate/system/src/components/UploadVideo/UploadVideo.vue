<template>
  <div class="upload-box">
    <Video
      v-if="url && url.length"
      class="review-video"
      :src="url"
      controls
      preload="auto"
      webkit-playsinline
      playsinline
      x-webkit-airplay="allow"
      x5-video-player-type="h5"
      x5-video-player-fullscreen
      x5-video-orientation="landscape"
    ></Video>
    <el-upload
      ref="uploading"
      v-loading="uploading"
      class="el-upload-demo"
      action="string"
      accept=".mp4, .3gp, .m3u8"
      :file-list="fileList"
      :http-request="upload"
      :show-file-list="!uploading && !!fileList.length"
      :before-remove="handleBeforeRemove"
      :on-remove="handleRemove"
      :multiple="false"
      :limit="1"
      :disabled="!needUpload"
    >
      <el-button v-if="!fileList.length" type="primary">上传文件</el-button>
    </el-upload>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'UploadVideo',
  components: {},
  model: {
    prop: 'url',
    event: 'input'
  },
  props: {
    url: {
      type: String,
      default: ''
    },
    needUpload: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      fileList: [],
      uploading: false
    }
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  watch: {
  },
  created() {
    this.initData()
  },
  methods: {
    // 数据初始化方法
    initData() {
      if (this.url) {
        this.fileList = [
          {
            name: this.getFileName(this.url),
            url: this.url
          }
        ]
      }
    },
    handleCrop() {
      console.log(2333333)
    },
    // 文件删除前回调
    handleBeforeRemove(file) {
      this.$emit('input', '')
      this.$emit('delVideo', file.url)
      // return new Promise((resolve, reject) => {
      //   this.$confirm('此操作将永久删除该视频, 是否继续?', '提示', {
      //     confirmButtonText: '确定',
      //     cancelButtonText: '取消',
      //     type: 'warning'
      //   }).then(() => {
      //     const formData = new FormData()
      //     formData.append('filePath', file.url)
      //     this.$http.deleteFile(formData).then(res => {
      //       if (res.result) {
      //         this.$message.success('删除成功')
      //         this.$emit('input', '')
      //         // this.$emit('deleteVideoUpdate')
      //         resolve(res)
      //       } else {
      //         if (res.description === '文件不存在') {
      //           this.$message.success('删除成功')
      //           this.$emit('input', '')
      //           resolve(res)
      //         } else {
      //           this.$message.error('删除失败')
      //           reject(res)
      //         }
      //       }
      //     })
      //   }).catch((err) => {
      //     reject(err)
      //   })
      // })
    },
    // 移除文件的会回调
    handleRemove(file, fileList) {
      this.fileList = fileList
    },
    // 上传方法
    upload(data) {
      // 自定义upload事件
      this.uploading = true
      var fileObj = data.file
      const formData = new FormData()
      formData.append('file', fileObj)
      this.$http.uploadFile(formData).then(res => {
        this.uploading = false
        if (res.result) {
          // 上传成功将照片传回父组件
          this.$emit('input', res.obj)
          this.fileList = [
            {
              name: this.getFileName(res.obj),
              url: res.obj
            }
          ]
          this.$message({ message: res.description, type: 'success', customClass: 'messageIndex' })
        } else {
          this.$message.error({ message: res.description, type: 'error', customClass: 'messageIndex' })
        }
      }).catch(() => {
        this.uploading = false
        this.$message.error({ message: '上传失败', type: 'error', customClass: 'messageIndex' })
      })
    },
    // 或且链接文件名称
    getFileName(url) {
      const urlArr = url.split('/')
      return urlArr[urlArr.length - 1]
    }
  }
}
</script>

<style scoped lang="scss">
  .upload-box {
    display: flex;
    flex-direction: column;
    .review-video {
      width: 600px;
      height: 340px;
    }
    .el-upload-demo{
      display: inline-block;
      .upload{
        width: 110px;
        height: 30px;
        background-color: #1c2c50;
        color: #ffffff;
        text-align: center;
        line-height: 30px;
        border-radius: 5px;
        font-size: 12px;
        border: 1px solid blue;
      }
      /deep/ .el-upload{
        display: inline-block;
        vertical-align: middle;
      }
      /deep/ .el-upload-list{
        display: inline-block;
        vertical-align: middle;
        padding-left: 10px;
      }
      /deep/ .el-upload-list__item:first-child{
        margin-top: 0;
      }
    }
  }
</style>
