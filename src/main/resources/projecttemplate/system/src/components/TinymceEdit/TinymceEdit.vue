<template>
  <div class="edit-wrapper">
    <editor :key="tinymceFlag" v-model="content" :init="tinymceInit" class="tinymce-edit"></editor>
  </div>
</template>
<script>
import { getToken } from '@/utils/auth'
import 'tinymce/tinymce'
import 'tinymce/themes/silver/theme'
import Editor from '@tinymce/tinymce-vue'

import 'tinymce/plugins/fullscreen'
import 'tinymce/plugins/preview'
import 'tinymce/plugins/paste'
import 'tinymce/plugins/advlist'
import 'tinymce/plugins/image'
import 'tinymce/plugins/link'
import 'tinymce/plugins/code'
import 'tinymce/plugins/table'
import 'tinymce/plugins/lists'
import 'tinymce/plugins/contextmenu'
import 'tinymce/plugins/wordcount'
import 'tinymce/plugins/colorpicker'
import 'tinymce/plugins/textcolor'
export default {
  name: 'TinymceEditor',
  components: {
    'editor': Editor
  },
  props: {
    imageText: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      baseUrl: process.env.OSS_BUILD === 'production' ? 'https://sapi-sys.oss-cn-qingdao.aliyuncs.com/dist' : process.env.VUE_APP_BASE_API,
      tinymceFlag: 1,
      tinymceInit: {
        content_style: `
            *                         { padding:0; margin:0; }
            body                      { padding: 10px; font-size: 14px; line-height: 20px; font-family: 微软雅黑; }
            img                       { max-width:100%; display:block; height:auto; }
            a                         { text-decoration: none; }
            iframe                    { width: 100%; }
            p                         { line-height:1.6; margin: 0px; }
            table                     { word-wrap:break-word; word-break:break-all; max-width:100%; border:none; border-color:#999; }
            .mce-object-iframe        { width:100%; box-sizing:border-box; margin:0; padding:0; }
            ul,ol                     { list-style-position:inside; }
          `,
        style_formats: [
          {
            title: '首行缩进',
            block: 'p',
            styles: { 'text-indent': '2em' }
          },
          {
            title: '行高',
            items: [
              { title: '1', styles: { 'line-height': '1' }, inline: 'span' },
              { title: '1.5', styles: { 'line-height': '1.5' }, inline: 'span' },
              { title: '2', styles: { 'line-height': '2' }, inline: 'span' },
              { title: '2.5', styles: { 'line-height': '2.5' }, inline: 'span' },
              { title: '3', styles: { 'line-height': '3' }, inline: 'span' }
            ]
          }
        ],
        fontsize_formats: '10px 11px 12px 14px 16px 18px 20px 24px',
        font_formats: `
            微软雅黑=微软雅黑;
            宋体=宋体;
            黑体=黑体;
            仿宋=仿宋;
            楷体=楷体;
            隶书=隶书;
            幼圆=幼圆;
            Andale Mono=andale mono,times;
            Arial=arial, helvetica,
            sans-serif;
            Arial Black=arial black, avant garde;
            Book Antiqua=book antiqua,palatino;
            Comic Sans MS=comic sans ms,sans-serif;
            Courier New=courier new,courier;
            Georgia=georgia,palatino;
            Helvetica=helvetica;
            Impact=impact,chicago;
            Symbol=symbol;
            Tahoma=tahoma,arial,helvetica,sans-serif;
            Terminal=terminal,monaco;
            Times New Roman=times new roman,times;
            Trebuchet MS=trebuchet ms,geneva;
            Verdana=verdana,geneva;
            Webdings=webdings;
            Wingdings=wingdings,zapf dingbats`,
        tabfocus_elements: ':prev,:next'
      },
      content: ''
    }
  },
  computed: {
    getToken() {
      return { 'Authorization': getToken('user-token') }
    }
  },
  watch: {
    imageText(newContent) {
      this.content = newContent.content
    }
  },
  created() {
    this.content = this.imageText.content
    this.tinymceInit = {
      ...this.tinymceInit,
      content_css: process.env.VUE_APP_OSS_BUILD === 'production' ? `https://sapi-sys.oss-cn-qingdao.aliyuncs.com/dist/tinymce/skins/content/default/content.css` : '/tinymce/skins/content/default/content.css',
      skin_url: process.env.VUE_APP_OSS_BUILD === 'production' ? 'https://sapi-sys.oss-cn-qingdao.aliyuncs.com/dist/tinymce/skins/ui/oxide' : '/tinymce/skins/ui/oxide',
      language_url: process.env.VUE_APP_OSS_BUILD === 'production' ? 'https://sapi-sys.oss-cn-qingdao.aliyuncs.com/dist/tinymce/langs/zh_CN.js' : '/tinymce/langs/zh_CN.js',
      language: 'zh_CN',
      // height: document.body.offsetHeight - 150,
      height: 500,
      browser_spellcheck: true, // 拼写检查
      branding: false, // 去水印
      // elementpath: false,  //禁用编辑器底部的状态栏
      statusbar: false, // 隐藏编辑器底部的状态栏
      paste_data_images: true, // 允许粘贴图像
      menubar: false, // 隐藏最上方menu
      selector: 'textarea',
      // plugins: 'advlist table lists paste preview fullscreen',
      // plugins: 'advlist, code, paste, textcolor, colorpicker, fullscreen, link, lists, wordcount, contextmenu, table, image, preview, fullscreen',
      plugins: 'advlist, code, paste, textcolor, colorpicker, fullscreen, link, lists, wordcount, contextmenu, table, image, preview, fullscreen',
      toolbar: [
        'code removeformat | styleselect | fontselect | fontsizeselect | forecolor | backcolor bold | italic underline strikethrough | alignleft aligncenter alignright alignjustify | image link h2 h3 fullscreen'
      ],
      // toolbar: 'code | undo redo | removeformat | styleselect  fontselect fontsizeselect formatselect  forecolor backcolor bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | image link h2 h3 blockquote table numlist bullist preview fullscreen',
      // 链接打开方式
      default_link_target: '_blank',
      image_advtab: true,
      image_title: true, // 是否开启图片标题设置的选择，这里设置否
      automatic_uploads: true,
      images_upload_handler: (blobInfo, success, failure) => {
        const formData = new FormData()
        formData.append('file', blobInfo.blob(), blobInfo.filename())
        this.$http.uploadFile(formData).then(res => {
          if (res.result && res.obj.indexOf('https://') !== -1) {
            success(res.obj)
          } else {
            failure('图片上传失败')
          }
        }).catch(() => {
          failure('图片上传失败')
        })
      }
    }
  },
  activated() {
    this.tinymceFlag++
  },
  beforeDestroy() {
    this.$emit('close', this.content)
  },
  methods: {
    outputContent() {
      return this.content
    },
    clearContent() {
      this.content = ''
    },
    upDataTinymceFlag() {
      this.tinymceFlag++
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
