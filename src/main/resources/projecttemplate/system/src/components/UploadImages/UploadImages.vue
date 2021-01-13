<template>
  <div class="upload-box">
    <!-- 多图片上传 -->
    <el-upload v-if="multiple" action="string" :accept="acceptList" list-type="picture-card" :on-preview="handlePreview" :auto-upload="false" :on-remove="handleRemove" :http-request="upload" :on-change="consoleFL" :file-list="uploadList">
      <i class="el-icon-plus"></i>
    </el-upload>
    <!-- 单图片上传 -->
    <template v-else>
      <el-upload class="avatar-uploader" :accept="acceptList" action="'string'" :auto-upload="false" :show-file-list="false" list-type="picture" :on-change="handleCrop" :http-request="upload" :disabled="!needUpload">
        <el-image v-if="imgUrl" ref="singleImg" :src="imgUrl" class="avatar" :style="{width:width+'px',height:height+'px'}" alt="" @mouseenter="mouseEnter" @mouseleave="mouseLeave" />
        <i v-else class="el-icon-plus avatar-uploader-icon" :style="{width:width+'px',height:height+'px','line-height':height+'px','font-size':height/6+'px'}"></i>
        <!-- 单图片上传状态显示 -->
        <div v-if="imgUrl" id="uploadIcon" ref="reupload" :style="{width:'100%'}" @mouseenter="mouseEnter" @mouseleave="mouseLeave">
          <div class="content">
            <i class="el-icon-zoom-in icon" @click.stop="handlePreviewSingle"></i>
            <i v-if="needUpload" class="el-icon-upload icon"></i>
          </div>
        </div>
        <div ref="uploading" class="reupload" :style="{width:reuploadWidth+'px',height:reuploadWidth+'px','line-height':reuploadWidth+'px','font-size':reuploadWidth/5+'px'}">上传中..</div>
        <div ref="failUpload" class="reupload" :style="{width:reuploadWidth+'px',height:reuploadWidth+'px','line-height':reuploadWidth+'px','font-size':reuploadWidth/5+'px'}">上传失败</div>
      </el-upload>
    </template>
    <!-- 多图片预览弹窗 -->
    <el-dialog :visible.sync="dialogVisible" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
    <!-- 剪裁组件弹窗 -->
    <el-dialog :visible.sync="cropperModel" append-to-body width="800px" :before-close="beforeClose">
      <Cropper ref="vueCropper" :img-file="file" :cropperConfig="cropperConfig" @upload="upload">
      </Cropper>
    </el-dialog>
  </div>
</template>
<script>
import Cropper from './cropper'

export default {
  name: 'Uploader',
  components: {
    Cropper
  },
  model: {
    prop: 'imgInfo',
    event: 'input'
  },
  props: {
    imgInfo: {
      type: [Array, String]
    }, // 上传图片列表
    customize: {
      type: Boolean,
      defalut: false
    },
    // 多图路径
    imgList: {
      type: Array,
      default: () => []
    },
    // 单图路径
    imgUrl: {
      type: String,
      default: ''
    },
    // 上传时数据库存储的名称
    listPropName: String,
    // 是否添加水印
    addWatermark: {
      type: Boolean,
      default: false
    },
    // 是否需要上传
    needUpload: {
      type: Boolean,
      default: true
    },
    // 上传文件的类型，1为头像，2为专辑封面
    uploadType: {
      type: Number,
      default: 1
    },
    /**
       * 以下是新模板的参数
       */
    targetUrl: {
      // 上传地址
      type: String,
      default: '/storage/upload'
    },
    multiple: {
      // 多图开关
      type: Boolean,
      default: false
    },
    initUrl: {
      // 初始图片链接
      type: String,
      default: ''
    },
    // 是否开启图片裁剪功能
    openCropper: {
      type: Boolean,
      default: false
    },
    cropperConfig: {
      type: Object,
      default: () => {}
    },
    // eslint-disable-next-line vue/require-prop-types
    width: {
      // 单图剪裁框宽度
      type: Number,
      default: 146
    },
    height: {
      // 单图剪裁框高度
      type: Number,
      default: 146
    },
    openSizeLimit: {
      type: Boolean,
      default: true // 默认开启上传大小限制
    },
    // 是否限制上传图片大小
    needLimitImage: {
      type: Boolean,
      default: false
    },
    // 限制图片宽度
    imgWidth: {
      type: Number
    },
    // 限制图片高度
    imgHeight: {
      type: Number
    }
  },
  data() {
    return {
      uploadList: [],
      acceptList: '.jpg,.jpeg,.png,.JPG,.JPEG,bmp,BMP,PNG',
      file: '', // 当前被选择的图片文件
      imageUrl: '', // 单图情况框内图片链接
      dialogImageUrl: '', // 多图情况弹窗内图片链接
      reupload: true, // 控制"重新上传"开关
      dialogVisible: false, // 展示弹窗开关
      cropperModel: false, // 剪裁组件弹窗开关
      reuploadWidth: this.height * 0.7 // 动态改变”重新上传“大小
    }
  },
  watch: {
    imgList: function(val) {
      // 监听传入初始化图片
      this.uploadList = this.formatImgArr(val)
    }
  },
  updated() {
    if (this.$refs.vueCropper) {
      this.$refs.vueCropper.Update()
    }
  },
  mounted() {
    if (this.multiple) {
      this.uploadList = this.formatImgArr(this.imgInfo)
    }
  },
  methods: {
    /** **************************** multiple多图情况 **************************************/
    handlePreview(file) {
      // 点击进行图片展示
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    handleRemove(file, fileList) {
      // 删除图片后更新图片文件列表并通知父级变化
      this.uploadList = fileList
      this.$emit('input', this.formatImgArr(this.uploadList))
    },
    consoleFL(file, fileList) {
      // 弹出剪裁框，将当前文件设置为文件
      if (this.openCropper) {
        this.cropperModel = true
      } else {
        this.upload(file.raw)
      }
      this.file = file
      this.uploadList = fileList
    },
    /** **********************************************************************************/

    /** **************************** single单图情况 **************************************/
    handlePreviewSingle(file) { // 点击进行图片展示
      this.dialogImageUrl = this.imgUrl
      this.dialogVisible = true
    },
    mouseEnter() { // 鼠标划入显示“重新上传”
      this.$refs.reupload.style.display = 'block'
      if (this.$refs.failUpload.style.display === 'block') {
        this.$refs.failUpload.style.display = 'none'
      }
      this.$refs.singleImg.imageStyle.opacity = '0.6'
    },
    mouseLeave() {
      // 鼠标划出隐藏“重新上传”
      this.$refs.reupload.style.display = 'none'
      this.$refs.singleImg.imageStyle.opacity = '1'
    },
    handleCrop(file, files) {
      // 点击弹出剪裁框
      if (this.openCropper) {
        this.cropperModel = true
      } else {
        this.upload(file.raw)
      }
      this.file = file
      // this.imageUrl = file.url
    },
    /** **********************************************************************************/
    upload(data) {
      // 自定义upload事件
      if (!this.multiple) {
        // 如果单图，则显示正在上传
        this.$refs.uploading.style.display = 'block'
      }
      const formData = new FormData()
      formData.append('file', data, 'cutImage.jpeg')
      this.$http.uploadFile(formData).then(res => {
        if (!this.multiple) {
          // 上传完成后隐藏正在上传
          this.$refs.uploading.style.display = 'none'
        }
        if (res.result) {
          // 上传成功将照片传回父组件
          this.$message({ message: res.description, type: 'success', customClass: 'messageIndex' })
          const currentPic = res.obj
          if (this.multiple) {
            this.uploadList.pop()
            this.uploadList.push({
              url: currentPic,
              uid: this.uploadList.length
            })
            this.$emit('input', this.formatImgArr(this.uploadList))
            // this.$emit('imgupload', this.formatImgArr(this.uploadList))
          } else {
            this.$emit('input', currentPic)
          }
        } else {
          // 上传失败则显示上传失败，如多图则从图片列表删除图片
          if (!this.multiple) {
            this.$refs.failUpload.style.display = 'block'
          } else {
            this.uploadList.pop()
          }
        }
      })
      this.cropperModel = false
    },
    formatImgArr(arr) {
      return arr && arr.length ? arr.map((item, index) => {
        const arrayItem = {
          url: item.url ? item.url : item[this.listPropName],
          uid: `index${r'${'}index}`
        }
        if (this.listPropName) {
          this.$set(arrayItem, this.listPropName, item.url ? item.url : item[this.listPropName])
        }
        return arrayItem
      }) : []
    },
    beforeClose(done) {
      this.uploadList.pop()
      this.cropperModel = false
    },
    // 上传图片大小限制
    beforeAvatarUpload(file) {}
  }
}
</script>
<style lang="scss">
  .upload-box {
    .avatar-uploader {
      .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
      }
      .el-upload:hover {
        border-color: #409eff;
      }
    }
    .avatar-uploader-icon {
      color: #8c939d;
      text-align: center;
    }
    .avatar {
      display: block;
    }
    .reupload {
      border-radius: 50%;
      position: absolute;
      color: #fff;
      background-color: #000000;
      opacity: 0.6;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      display: none;
    }
    #uploadIcon{
      display: none;
      position: absolute;
      width: 100%;
      height: 100%;
      left: 0;
      top: 0;
      cursor: default;
      text-align: center;
      color: #fff;
      background-color: rgba(0, 0, 0, 0.5);
      -webkit-transition: opacity .3s;
      transition: opacity .3s;
      z-index: 10;
      .content {
        position: absolute;
        width: 90%;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        line-height: 20px;
        text-align: center;
      }
      .icon {
        margin-right: 10px !important;
        display: inline-block;
        vertical-align: top;
        font-size: 20px;
        cursor: pointer;
        &:last-of-type {
          margin-right: 0 !important;
        }
      }
    }
  }
</style>
