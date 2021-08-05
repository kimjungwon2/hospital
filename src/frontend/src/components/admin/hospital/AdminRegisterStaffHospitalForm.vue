<template>
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
        <button type="submit">추가하기</button>
    </form>
</template>

<script>
import {adminRegisterStaffHospitalInfo} from '@/api/admin'
export default {
    data() {
        return {
            introduction:'',
            consultationHour:'',
            abnormality:'',
            doctors:[],
            isCreate:false,
        }
    },
    methods:{
        async submitForm(){
            const hospitalData = {
                hospitalId:this.$route.query.hospitalId,
                introduction: this.introduction,
                consultationHour: this.consultationHour,
                abnormality: this.abnormality,
            }
            const staffHosId = await adminRegisterStaffHospitalInfo(hospitalData);
            this.initForm();
            this.$alert("추가 정보 등록이 정상적으로 이뤄졌습니다.");
            this.$router.push('/admin/hospitals');
        },
        initForm(){
            this.introduction='';
            this.consultationHour='';
            this.abnormality='';
            this.doctors=[];
        },
    },

}
</script>

<style>

</style>