<template>
  <div v-if="noMap" class="map" > <img src='@/assets/noMap.png' alt="no map img" class="map__image"></div>
  <div v-else class="map" id="map" style="width:700px;height:500px;"></div>
</template>

<script>
export default {
  data() {
    return {
      noMap:false,
    }
  },
  props:{
      detailed:{
          type: Object,
          default: function() {
            return {}
          }
      },
  },
  //detailed 값을 받아오면, watch로 변경감지해서 지도 재출력.
  watch:{
    detailed: function(){
      if (window.kakao && window.kakao.maps) {
        this.initMap()
      } else {
          const script = document.createElement('script')
          script.onload = () => kakao.maps.load(this.initMap);
          script.src = 'http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=bcdb595a4b1c6bc005200d3b0d9271fb'
          document.head.appendChild(script)
      }
    }
  },
  methods: {
    initMap () {
      if((this.detailed.latitude ===0 && this.detailed.longitude ===0)||(this.detailed.latitude ==null)){
        this.noMap=true;
      }
      else{
          const container = document.getElementById('map');
          const options = {
              center: new kakao.maps.LatLng(this.detailed.latitude, this.detailed.longitude),
              level: 3
          };

          const map = new kakao.maps.Map(container, options)
          const markerPosition = new kakao.maps.LatLng(this.detailed.latitude, this.detailed.longitude);
          const marker = new kakao.maps.Marker({
          position: markerPosition
          });
           marker.setMap(map);
      }
    },
  },
}
</script>

<style>
.map__image img{
  width:700px;
  height:500px;
}
.map{
  margin:auto;
}

</style>