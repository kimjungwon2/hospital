<template>
    <form @submit.prevent="submitForm">
        <div>
            <label for="introduction">소개문:</label>
            <input id="introduction" type="text" required v-model="introduction">
        </div>
        <div>
            <label for="consultationHour">영업 시간:</label>
            <input id="consultationHour" type="text" required v-model="consultationHour">
        </div>
        <div>
            <label for="abnormality">특이 사항:</label>
            <input id="abnormality" type="text" required v-model="abnormality">
        </div>
        <h2>의사 {{doctorNumber}}</h2>
        <div v-if="doctorNumber!==0">
            <div v-for="n in doctorNumber" :key="n">
                <p>의사 {{n}}</p>
                <div>
                    <label for="name">이름:</label>
                    <input id="name" type="text" required v-model="name[n-1]">
                </div>
                <div>
                    <label for="history">이력:</label>
                    <input id="history" type="text" required v-model="history[n-1]">
                </div>
            </div>
        </div>
        <button @click.prevent="addDoctor">의사 추가하기</button><br>
        <button v-if="doctorNumber>0" @click.prevent="minusDoctor">의사 제거</button><br>
        <button type="submit">정보 등록하기</button>
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
            doctorNumber:0,
            name:[],
            history:[],
            doctors:[],
            isCreate:false,
        }
    },
    methods:{
        async submitForm(){
            for(let i=0; i<this.doctorNumber; i++){
                this.doctors.push({name:this.name[i],history:this.history[i]});
            }

            const hospitalData = {
                hospitalId:this.$route.query.hospitalId,
                introduction: this.introduction,
                consultationHour: this.consultationHour,
                abnormality: this.abnormality,
                doctors:this.doctors,
            }
            await adminRegisterStaffHospitalInfo(hospitalData);
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
        addDoctor(){
            this.doctorNumber +=1;
        },
        minusDoctor(){
            if(this.doctorNumber>0){
                this.doctorNumber -=1;
                this.history.pop();
                this.name.pop();
            }
        },
    },

}
</script>

<style>

</style>