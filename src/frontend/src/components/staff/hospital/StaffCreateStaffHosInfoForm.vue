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
import {staffRegisterStaffHospitalInfo} from '@/api/staff';
export default {
    data() {
        return {
            hospitalId:'',
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
            if(this.hospitalId===null){
                this.$alert("병원 번호가 없어서 등록할 수 없습니다.");
                this.$router.push('/');
            }
            else {
                //의사 데이터 list에 넣기.
                for(let i=0; i<this.doctorNumber; i++){
                    this.doctors.push({name:this.name[i],history:this.history[i]});
                }

                const hospitalData = {
                    hospitalId:this.hospitalId,
                    memberId:this.$store.getters.getMemberId,
                    introduction: this.introduction,
                    consultationHour: this.consultationHour,
                    abnormality: this.abnormality,
                    doctors:this.doctors,
                }
                const staffHosInfoId = await staffRegisterStaffHospitalInfo(hospitalData);
                this.initForm();
                this.$alert("추가 정보 등록이 정상적으로 이뤄졌습니다.");
                this.$router.push({name:'staffViewStaffViewHospital',
                    query: {staffHosInfoId:staffHosInfoId.data.id}
                }); 
            }
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
    created(){
        this.hospitalId =  this.$route.params.hospitalId;
        console.log(this.hospitalId);
    },
}
</script>

<style>

</style>