<template>
  <div class="global-uploader">
    <uploader
      ref="uploader"
      :options="options"
      class="uploader-example"
      @file-added="onFileAdded"
      @file-success="onFileSuccess"
    >
      <uploader-unsupport></uploader-unsupport>
      <uploader-drop>
        <uploader-btn id="global-uploader-btn" ref="uploadBtn" :attrs="attrs">选择文件</uploader-btn>
      </uploader-drop>
      <uploader-list v-show="panelShow">
        <div slot-scope="props" class="file-panel" :class="{'collapse': collapse}">
          <div class="file-title">
            <h2>文件列表</h2>
            <div class="operate">
              <el-button type="text" :title="collapse ? '展开':'折叠' ">
                <i class="iconfont" :class="collapse ? 'inuc-fullscreen': 'inuc-minus-round'"></i>
              </el-button>
              <el-button type="text" title="关闭">
                <i class="iconfont icon-close"></i>
              </el-button>
            </div>
          </div>
          <ul class="file-list">
            <li v-for="file in props.fileList" :key="file.id">
              <uploader-file ref="files" :class="'file_' + file.id" :file="file" :list="true"></uploader-file>
            </li>
            <div v-if="!props.fileList.length" class="no-file"><i class="iconfont icon-empty-file"></i> 暂无待上传文件</div>
          </ul>
        </div>
      </uploader-list>
    </uploader>
  </div>
</template>
<script>
import { ACCEPT_CONFIG } from '../../store/modules/config'
import Bus from '../../store/modules/bus'
import SparkMD5 from 'spark-md5'
import { mapGetters } from 'vuex'
export default {
  components: {

  },
  data() {
    return {
      options: {
        target: 'http://localhost:9700/sapi/uploadoss',
        testChunks: true,
        headers: {
          Authorization: 'eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJKV1QiLCJpYXQiOjE1Njk2NTE3MTcsInN1YiI6IntcImFwaVwiOltdLFwiY3JlYXRlVGltZVwiOjE1Njk1NTU0NDIwMDAsXCJjcmVhdG9yXCI6MzIyNjYwMzkxNzczNzQxMDYwLFwibW9kaWZ5VGltZVwiOjE1Njk1NjMxNjMwMDAsXCJzYWx0XCI6XCJrZnhlbmxcIixcInN5c1VzZXJJZFwiOjMyMjY2MDM5MTc3Mzc0MTA2MCxcInRlYWNoZXJBZ2VcIjpcIjE4XCIsXCJ0ZWFjaGVyQXZhdGFyXCI6XCJodHRwczovL2ppbmJlaWppZXRlc3Qub3NzLWNuLXFpbmdkYW8uYWxpeXVuY3MuY29tLzMyMjY2MDM5MTc3Mzc0MTA1Ni9lczkxcWI2ZnBsZmhwcmVmLnBuZ1wiLFwidGVhY2hlckVtYWlsXCI6XCIxMjkyMjY0OTUxQHFxLmNvbVwiLFwidGVhY2hlcklkXCI6MzIyOTk2NTkxNjU1MDY3NjQ4LFwidGVhY2hlck5hbWVcIjpcIuW8oOS4iVwiLFwidGVhY2hlclBhc3N3b3JkXCI6XCJkYzIwZGMwY2JkODJlYjZlYzQwODMwMWNiODg5NzVlMlwiLFwidGVhY2hlclBob25lXCI6XCIxNTg5ODcxMjk1NVwiLFwidGVhY2hlclFxXCI6XCIxMjkyMjY0NTkxXCIsXCJ0ZWFjaGVyU3RhdGVcIjoxfSIsImV4cCI6MTU2OTY5NDkxN30.IWmMsHdISjttZ3dTKIWCryDma0TI89ClFXSuBxVn9Sw'
        }
      },
      panelShow: false,
      collapse: false,
      attrs: {
        accept: ACCEPT_CONFIG.getAll()
      }
    }
  },
  computed: {
    // Uploader实例
    uploader() {
      return this.$refs.uploader.uploader
    },
    ...mapGetters(['userInfo'])
  },
  watch: {},
  mounted() {
    Bus.$on('openUploader', query => {
      this.params = query || {}
      if (this.$refs.uploadBtn) {
        // $('#global-uploader-btn').click()
      }
    })
  },
  destroyed() {
    Bus.$off('openUploader')
  },
  methods: {
    onFileAdded(file) {
      this.panelShow = true
      this.computeMD5(file)
      Bus.$emit('fileAdded')
    },
    onFileSuccess(rootFile, file, response) {
      const res = JSON.parse(response)
      if (!res.result) {
        this.$message({ message: res.description, type: 'error' })
        return
      }
      this.$message.success(res.description)
    },
    /**
     * 计算md5，实现断点续传及秒传
     * @param file
     */
    computeMD5(file) {
      const fileReader = new FileReader()
      const time = new Date().getTime()
      const blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice
      let currentChunk = 0
      const chunkSize = 10 * 1024 * 1000
      const chunks = Math.ceil(file.size / chunkSize)
      const spark = new SparkMD5.ArrayBuffer()
      // 文件状态设为"计算MD5"
      // this.statusSet(file.id, 'md5')
      file.pause()
      loadNext()
      fileReader.onload = e => {
        spark.append(e.target.result)
        if (currentChunk < chunks) {
          currentChunk++
          loadNext()
          // 实时展示MD5的计算进度
          // this.$nextTick(() => {
          //   $(`.myStatus_${r'${'}file.id}`).text('校验MD5 ' + ((currentChunk / chunks) * 100).toFixed(0) + '%')
          // })
        } else {
          const md5 = spark.end()
          this.computeMD5Success(md5, file)
          console.log(`MD5计算完毕：${r'${'}file.name} \nMD5：${r'${'}md5} \n分片：${r'${'}chunks} 大小:${r'${'}file.size} 用时：${r'${'}new Date().getTime() - time} ms`)
        }
      }
      fileReader.onerror = function() {
        this.error(`文件${r'${'}file.name}读取出错，请检查该文件`)
        file.cancel()
      }
      function loadNext() {
        const start = currentChunk * chunkSize
        const end = ((start + chunkSize) >= file.size) ? file.size : start + chunkSize
        fileReader.readAsArrayBuffer(blobSlice.call(file.file, start, end))
      }
    },
    computeMD5Success(md5, file) {
      // 将自定义参数直接加载uploader实例的opts上
      Object.assign(this.uploader.opts, {
        query: {
          ...this.params
        }
      })
      file.uniqueIdentifier = md5
      file.resume()
      // this.statusRemove(file.id)
    }
    // fileListShow() {
    //   const $list = $('#global-uploader .file-list')
    //   if ($list.is(':visible')) {
    //     $list.slideUp()
    //     this.collapse = true
    //   } else {
    //     $list.slideDown()
    //     this.collapse = false
    //   }
    // },
    /**
     * 新增的自定义的状态: 'md5'、'transcoding'、'failed'
     * @param id
     * @param status
     */
    // statusSet(id, status) {
    //   const statusMap = {
    //     md5: {
    //       text: '校验MD5',
    //       bgc: '#fff'
    //     },
    //     merging: {
    //       text: '合并中',
    //       bgc: '#e2eeff'
    //     },
    //     transcoding: {
    //       text: '转码中',
    //       bgc: '#e2eeff'
    //     },
    //     failed: {
    //       text: '上传失败',
    //       bgc: '#e2eeff'
    //     }
    //   }
    //   this.$nextTick(() => {
    //     $(`<p class="myStatus_${r'${'}id}"></p>`).appendTo(`.file_${r'${'}id} .uploader-file-status`).css({
    //       'position': 'absolute',
    //       'top': '0',
    //       'left': '0',
    //       'right': '0',
    //       'bottom': '0',
    //       'zIndex': '1',
    //       'backgroundColor': statusMap[status].bgc
    //     }).text(statusMap[status].text)
    //   })
    // },
    // statusRemove(id) {
    //   this.$nextTick(() => {
    //     $(`.myStatus_${r'${'}id}`).remove()
    //   })
    // },
  }
}
</script>

<style lang="scss" scoped>
  #global-uploader {
    position: fixed;
    z-index: 20;
    right: 15px;
    bottom: 15px;
    .uploader-app {
      width: 520px;
    }
    .file-panel {
      background-color: #fff;
      border: 1px solid #e2e2e2;
      border-radius: 7px 7px 0 0;
      box-shadow: 0 0 10px rgba(0, 0, 0, .2);
      .file-title {
        display: flex;
        height: 40px;
        line-height: 40px;
        padding: 0 15px;
        border-bottom: 1px solid #ddd;
        .operate {
          flex: 1;
          text-align: right;
        }
      }
      .file-list {
        position: relative;
        height: 240px;
        overflow-x: hidden;
        overflow-y: auto;
        background-color: #fff;
        > li {
          background-color: #fff;
        }
      }
      &.collapse {
        .file-title {
          background-color: #E7ECF2;
        }
      }
    }
    .no-file {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      font-size: 16px;
    }
    /deep/.uploader-file-icon {
      &:before {
        content: '' !important;
      }
      &[icon=image] {
        /*background: url(./images/image-icon.png);*/
      }
      &[icon=video] {
        /*background: url(./images/video-icon.png);*/
      }
      &[icon=document] {
        /*background: url(./images/text-icon.png);*/
      }
    }
    /deep/.uploader-file-actions > span {
      margin-right: 6px;
    }
  }
  /*!* 隐藏上传按钮 *!*/
  /*#global-uploader-btn {*/
  /*  position: absolute;*/
  /*  clip: rect(0, 0, 0, 0);*/
  /*}*/
</style>
