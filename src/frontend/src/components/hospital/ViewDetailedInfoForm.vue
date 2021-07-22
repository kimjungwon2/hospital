<template>
  <div><h1>상세 정보</h1>
  <div v-if="this.staffHosInfoId !== null">
      소개문: {{staffInfo.introduction}}
      영업 시간: {{staffInfo.consoluationHour}}
      특이사항: {{staffInfo.abnormality}}
  </div>
  <div v-else>
      등록된 병원 추가 정보가 없습니다.
  </div> 
  
  </div>

</template>

<script>
import { viewStaffHosInfo } from '@/api/hospital';

export default {
  data() {
    return {
      staffInfo:[],
    };
  },
  props: {
    staffHosInfoId: {
      type: undefined,
      required: true,
    },
  },
  async created(){
    if(this.staffHosInfoId !== null) {
      const{data} = await viewStaffHosInfo(this.staffHosInfoId);
      this.staffInfo = data;
    }
  }
}
</script>

<style>

</style>