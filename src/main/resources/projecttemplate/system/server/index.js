const Koa = require('koa')
const path = require('path')
const app = new Koa()
// const route = require('koa-route')
const proxy = require('http-proxy-middleware')
const serve = require('koa-static')
const url = '${project.portHost}:${project.port}' // 后端服务器地址
// 1.主页静态网页 把静态页统一放到public中管理
const home = serve(path.join(__dirname, '../dist/'))
// 2.hello接口
app.use(async(ctx, next) => {
  if (ctx.url.startsWith('/sapi-api')) { // 以api开头的异步请求接口都会被转发
    ctx.respond = false
    return proxy({
      target: url, // 服务器地址
      changeOrigin: true,
      secure: false,
      pathRewrite: {
        '^/sapi-api': ''
      }
      /* ^^^
      上面这个pathRewrite字段不是必须的，
      你的开发环境和生产环境接口路径不一致的话，才需要加这个。
      */
    })(ctx.req, ctx.res, next)
  }
  return next()
})
app.use(home)
app.listen(${project.systemPort}, () => {
  console.log('服务已启动，在 http://localhost:${project.systemPort}')
})
