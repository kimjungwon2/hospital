<template>
  <div>
    <form @submit.prevent="submitForm">
        <div>
            <label for="introduction">소개문:</label>
            <input id="introduction" type="text" v-model="introduction">
        </div>
        <div>
            <label for="consultationHour">영업 시간:</label>
            <input id="consultationHour" type="text" v-model="consultationHour">
        </div>
        <div>
            <label for="abnormality">특이 사항:</label>
            <input id="abnormality" type="text" v-model="abnormality">
        </div>
        <h2>의사</h2>
        <button type="submit">변경하기</button>
    </form>
  </div>
</template>

<script>
import {adminViewStaffHospitalInfo,adminModifyStaffHosInfo} from '@/api/admin';
export default {
    data() {
        return {
            introduction:'',
            consultationHour:'',
            abnormality:'',
            staffHosInfoId:'',
            doctors:[],
        };
    },
    methods:{
        submitForm(){
            const staffHosData = {
                introduction:this.introduction,
                consultationHour:this.consultationHour,
                abnormality:this.abnormality,
                doctors:this.doctors,
            }
            adminModifyStaffHosInfo(this.staffHosInfoId, staffHosData);
            this.$alert('추가 정보 수정이 정상적으로 이뤄졌습니다.');
            this.$router.push('/admin/hospitals');
        },
    },
    async created(){
        this.staffHosInfoId = this.$route.params.staffHosId;
        const {data} = await adminViewStaffHospitalInfo(this.staffHosInfoId);

        this.introduction = data.introduction;
        this.consultationHour = data.consultationHour;
        this.abnormality = data.abnormality;
        this.doctors = data.doctors;
    }
}
</script>

<style>

</style>