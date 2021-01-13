<template>
  <div class="map-box" :style="{width: width, height: height}">
    <div id="gaud-map" class="gaud-map">
    </div>
  </div>
</template>

<script>

export default {
  name: 'GaudMap',
  components: {
  },
  props: {
    width: {
      type: String,
      defalut: '300px'
    },
    height: {
      type: String,
      defalut: '300px'
    },
    lng: {
      type: String,
      defalut: ''
    },
    address: {
      type: String,
      defalut: ''
    },
    lat: {
      type: String,
      defalut: ''
    },
    disabled: {
      type: Boolean,
      defalut: false
    }
  },
  data() {
    return {
      mapObj: null,
      geocoder: null,
      inputObj: null,
      selfMarker: null,
      markerLng: this.lng,
      makerLat: this.lat,
      makerAddress: this.address
    }
  },
  computed: {
  },
  mounted() {
    if (this.lng && this.lat) {
      this.initMap(this.lng, this.lat)
    } else {
      this.getLocalPosation().then(res => {
        if (res.status === 1) {
          this.markerLng = res.position.lng
          this.makerLat = res.position.lat
          this.initMap(res.position.lng, res.position.lat)
        } else {
          this.initMap()
        }
      }).catch(() => {
        this.initMap()
      })
    }
  },
  methods: {
    // 当前经纬度获取方法
    getSelectLnglat() {
      return {
        makerAddress: this.makerAddress,
        lng: this.markerLng,
        lat: this.makerLat
      }
    },
    // 初始化地图
    initMap(lng = this.markerLng, lat = this.makerLat) {
      // eslint-disable-next-line no-undef
      this.mapObj = new AMap.Map('gaud-map', {
        resizeEnable: true, // 是否监控地图容器尺寸变化
        zoom: 14, // 初始化地图层级
        center: [lng, lat] // 初始化地图中心点
      })
      this.geocoder = new AMap.Geocoder({
        radius: 1000
      })
      this.selfMarker = new AMap.Marker({
        position: new AMap.LngLat(lng, lat), // 经纬度对象，也可以是经纬度构成的一维数组[116.39, 39.9]
        title: '钓点'
      })
      this.mapObj.add(this.selfMarker)
      // 添加地图点击事件
      this.mapObj.on('click', this.mapClick)
    },
    // 地图点击事件方法
    mapClick(e) {
      if (!this.disabled) {
        this.markerLng = e.lnglat.lng
        this.makerLat = e.lnglat.lat
        this.selfMarker.setPosition([e.lnglat.lng, e.lnglat.lat])
        this.getAddress(e.lnglat.lng, e.lnglat.lat).then(res => {
          this.makerAddress = res
          this.$emit('onChange', {
            address: res,
            lng: e.lnglat.lng,
            lat: e.lnglat.lat
          })
        })
      }
    },
    // 获取当前定位
    getLocalPosation() {
      return new Promise((resolve, reject) => {
        AMap.plugin('AMap.Geolocation', function() {
          var geolocation = new AMap.Geolocation({
            // 是否使用高精度定位，默认：true
            enableHighAccuracy: true,
            // 设置定位超时时间，默认：无穷大
            timeout: 10000,
            // 定位按钮的停靠位置的偏移量，默认：Pixel(10, 20)
            buttonOffset: new AMap.Pixel(0, 0),
            //  定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
            zoomToAccuracy: true,
            //  定位按钮的排放位置,  RB表示右下
            buttonPosition: 'RB'
          })

          geolocation.getCurrentPosition()
          AMap.event.addListener(geolocation, 'complete', onComplete)
          AMap.event.addListener(geolocation, 'error', onError)

          function onComplete(data) {
            // data是具体的定位信息
            resolve(data)
          }

          function onError(data) {
            // 定位出错
            reject(data)
          }
        })
      })
    },
    // 获取当前地理位置
    getAddress(lng, lat) {
      return new Promise((resolve, reject) => {
        this.geocoder.getAddress([lng, lat], function(status, result) {
          if (status === 'complete' && result.regeocode) {
            const address = result.regeocode.formattedAddress
            resolve(address)
          } else {
            resolve('')
          }
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .map-box {
    position: relative;
    .gaud-map {
      width: 100%;
      height: 100%;
    }
  }
</style>
