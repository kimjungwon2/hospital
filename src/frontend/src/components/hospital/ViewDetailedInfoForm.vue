<template>
  <div><h1>상세 정보</h1>
  
  소개문: {{staffInfo.introduction}}
  영업 시간: {{staffInfo.consoluationHour}}
  특이사항: {{staffInfo.abnormality}}
  
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