'use strict'
const path = require('path')
const defaultSettings = require('./src/settings.js')
<#if project.ossBuild == true >
const Oss = require('./oss.js')
const WebpackAliyunOss = require('webpack-aliyun-oss')
</#if>

function resolve(dir) {
  return path.join(__dirname, dir)
}

const name = defaultSettings.title || 'vue Admin Template' // page title
// If your port is set to 80,
// use administrator privileges to execute the command line.
// For example, Mac: sudo npm run
const port = 12525 // dev port

// All configuration item explanations can be find in https://cli.vuejs.org/config/
module.exports = {
  /**
   * You will need to set publicPath if you plan to deploy your site under a sub path,
   * for example GitHub Pages. If you plan to deploy your site to https://foo.github.io/bar/,
   * then publicPath should be set to "/bar/".
   * In most cases please use '/' !!!
   * Detail: https://cli.vuejs.org/config/#publicpath
   */
  publicPath: ${project.ossBuild?string("process.env.OSS_BUILD === 'production' ? 'https://sapi-sys.oss-accelerate.aliyuncs.com/dist/' : ","")}'/',
  outputDir: 'dist',
  assetsDir: 'static',
  lintOnSave: process.env.NODE_ENV === 'development',
  productionSourceMap: false,
  devServer: {
    port: port,
    open: true,
    overlay: {
      warnings: false,
      errors: true
    },
    proxy: {
      // detail: https://cli.vuejs.org/config/#devserver-proxy
      [process.env.VUE_APP_BASE_API]: {
        target: `${project.portHost}:${project.port}`,
        changeOrigin: true,
        pathRewrite: {
          ['^' + process.env.VUE_APP_BASE_API]: ''
        }
      }
    }
  },
<#if project.ossBuild == true >
  configureWebpack: (config) => {
    if (process.env.OSS_BUILD === 'production') {
      const webpackAliyunOss = [
        new WebpackAliyunOss({
          from: ['./dist/**'], // 上传那个文件或文件夹  可以是字符串或数组
          dist: '/dist', // 需要上传到oss上的给定文件目录
          region: Oss.region,
          accessKeyId: Oss.accessKeyId,
          accessKeySecret: Oss.accessKeySecret,
          bucket: Oss.bucket,

          // test: true,
          // 上面一行，可以在进行测试看上传路径是否正确, 打开后只会显示上传路径并不会真正上传;

          // 因为文件标识符 "\"  和 "/" 的区别 不进行 setOssPath配置,上传的文件夹就会拼到文件名上, 丢失了文件目录,所以需要对setOssPath 配置。
          setOssPath: filePath => {
            // some operations to filePath
            const index = filePath.lastIndexOf('dist')
            const Path = filePath.substring(index + 4, filePath.length)
            return Path.replace(/\\/g, '/')
          },
          setHeaders: filePath => {
            // some operations to filePath
            // return false to use default header
            return {
              'Cache-Control': 'max-age=31536000'
            }
          }
        })
      ]
      config.plugins = [...config.plugins, ...webpackAliyunOss]
    }
  },
</#if>
  chainWebpack(config) {
    config.plugins.delete('preload') // TODO: need test
    config.plugins.delete('prefetch') // TODO: need test

    // set svg-sprite-loader
    config.module
      .rule('svg')
      .exclude.add(resolve('src/icons'))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()

    // set preserveWhitespace
    config.module
      .rule('vue')
      .use('vue-loader')
      .loader('vue-loader')
      .tap(options => {
        options.compilerOptions.preserveWhitespace = true
        return options
      })
      .end()

    config
    // https://webpack.js.org/configuration/devtool/#development
      .when(process.env.NODE_ENV === 'development',
        config => config.devtool('cheap-source-map')
      )

    config
      .when(process.env.NODE_ENV !== 'development',
        config => {
          config
            .plugin('ScriptExtHtmlWebpackPlugin')
            .after('html')
            .use('script-ext-html-webpack-plugin', [{
            // `runtime` must same as runtimeChunk name. default is `runtime`
              inline: /runtime\..*\.js$/
            }])
            .end()
          config
            .optimization.splitChunks({
              chunks: 'all',
              cacheGroups: {
                libs: {
                  name: 'chunk-libs',
                  test: /[\\/]node_modules[\\/]/,
                  priority: 10,
                  chunks: 'initial' // only package third parties that are initially dependent
                },
                elementUI: {
                  name: 'chunk-elementUI', // split elementUI into a single package
                  priority: 20, // the weight needs to be larger than libs and app or it will be packaged into libs or app
                  test: /[\\/]node_modules[\\/]_?heyi-ui(.*)/ // in order to adapt to cnpm
                },
                commons: {
                  name: 'chunk-commons',
                  test: resolve('src/components'), // can customize your rules
                  minChunks: 3, //  minimum common number
                  priority: 5,
                  reuseExistingChunk: true
                }
              }
            })
          config.optimization.runtimeChunk('single')
        }
      )
  }
}
