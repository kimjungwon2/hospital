<template>
  <div>
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
        <button type="submit">변경하기</button>
    </form>

    <h2>의사</h2>
        <div v-if="doctors.length===0">
            등록된 의사가 없습니다.<br> <button @click.prevent="addDoctor">의사 추가하기</button>
        </div>
        <div v-else v-for="(doctor,n) in doctors" :key="doctor.doctorId"> 
            <form @submit.prevent="modifyDoctor(doctor.doctorId,doctor.name,doctor.history)">
                <p>의사 {{n+1}}</p>  
                <div>
                    <label for="name">이름:</label>
                    <input id="name" type="text" required v-model="doctor.name">
                </div>
                <div>
                    <label for="history">경력:</label>
                    <input id="history" type="text" required v-model="doctor.history">
                </div>
                <button type="submit">의사 수정</button>
            </form>
            <form @submit.prevent="deleteDoctor(doctor.doctorId)">
                <button type="submit">의사 삭제</button>
            </form>
        </div> 

        <br>
        <button v-if="doctors.length!==0" @click.prevent="addDoctor">의사 추가</button>
        <div v-if="isDoctorCrate===true">
            <form @submit.prevent="createDoctor">
                <div>
                    <label for="name">이름:</label>
                    <input id="name" type="text" required v-model="name">
                </div>
                <div>
                    <label for="history">이력:</label>
                    <input id="history" type="text" required v-model="history">
                </div>
                <button type="submit">의사 등록</button>
            </form>
        </div>

  </div>
</template>

<script>
import {adminViewStaffHospitalInfo,adminDeleteDoctor,
        adminModifyStaffHosInfo,adminModifyDoctor,adminCreateDoctor} from '@/api/admin';
export default {
    data() {
        return {
            introduction:'',
            consultationHour:'',
            abnormality:'',
            staffHosInfoId:'',
            doctors:[],
            isDoctorCrate:false,
            name:'',
            history:''
        };
    },
    methods:{
        async submitForm(){
            const staffHosData = {
                introduction:this.introduction,
                consultationHour:this.consultationHour,
                abnormality:this.abnormality,
                doctors:this.doctors,
            }
            await adminModifyStaffHosInfo(this.staffHosInfoId, staffHosData);
            this.$alert('추가 정보 수정이 정상적으로 이뤄졌습니다.');
            this.$router.push('/admin/hospitals');
        },
        async deleteDoctor(doctorId){
            if(confirm('해당 의사를 삭제하시겠습니까?')){
                await adminDeleteDoctor(doctorId);
                this.loadStaffHosInfo();
                this.$alert('정상적으로 삭제됐습니다.');
            }
        },
        async modifyDoctor(doctorId,name,history){
            if(confirm('의사 정보를 수정하시겠습니까?')){
                const doctorData={
                    name:name,
                    history:history
                };
                await adminModifyDoctor(doctorId,doctorData);
                this.loadStaffHosInfo();
                this.$alert('정상적으로 수정됐습니다.');
            }
        },
        addDoctor(){
            this.isDoctorCrate = true;
        },
        async createDoctor(){
            if(confirm('의사를 등록하시겠습니까?')){
                const doctorData ={
                    staffHosId:this.staffHosInfoId,
                    name:this.name,
                    history:this.history
                };

                await adminCreateDoctor(doctorData);
                this.initForm();
                this.loadStaffHosInfo();
                this.$alert('정상적으로 등록됐습니다.');
            }
        },
        initForm(){
            this.isDoctorCrate = false;
            this.history = '';
            this.name = '';
        },
        async loadStaffHosInfo(){
            const {data} = await adminViewStaffHospitalInfo(this.staffHosInfoId);

            this.introduction = data.introduction;
            this.consultationHour = data.consultationHour;
            this.abnormality = data.abnormality;
            this.doctors = data.doctors;
        }
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