<template>
  <div id="map" style="width:600px;height:500px;"></div>
</template>

<script>
export default {
  props:{
      detailed:{
          type: Object,
          default: function() {
            return {}
          }
      },
  },
   mounted() {
       if (window.kakao && window.kakao.maps) {
        this.initMap()
      } else {
          const script = document.createElement('script')
          script.onload = () => kakao.maps.load(this.initMap);
          script.src = 'http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=bcdb595a4b1c6bc005200d3b0d9271fb'
          document.head.appendChild(script)
    }
  },
  methods: {
    initMap () {
      const container = document.querySelector('#map')
      const options = {
        center: new kakao.maps.LatLng(this.detailed.latitude, this.detailed.longitude),
        level: 3
      }
      const map = new kakao.maps.Map(container, options)
      const markerPosition = new kakao.maps.LatLng(this.detailed.latitude, this.detailed.longitude);
      console.log(this.detailed.longitude);
      console.log(this.detailed.latitude);
      const marker = new kakao.maps.Marker({
        position: markerPosition
      });
      marker.setMap(map)
    },
  },
}
</script>

<style>

</style>