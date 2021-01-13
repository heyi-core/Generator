import pcaCode from '@/assets/js/pca-code'

export default function codeToArea(code, type) {
  const symbol = type || ''
  let result = ''
  const p = pcaCode.filter(item => item.code === (parseInt(code / 10000) * 10000).toString())
  const c = p.length ? p[0].children.filter(item => item.code === (parseInt(code / 100) * 100).toString()) : []
  const a = c.length ? c[0].children.filter(item => item.code === parseInt(code / 1).toString()) : []
  if (p.length) {
    result += p[0].name + symbol
  }
  if (c.length) {
    result += c[0].name + symbol
  }
  if (a.length) {
    result += a[0].name
  }
  return result.trim()
}
