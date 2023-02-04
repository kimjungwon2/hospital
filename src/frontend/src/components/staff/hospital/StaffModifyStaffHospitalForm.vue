<template>
<section id="staffModifyStaffHosInfo">
    <form @submit.prevent="submitForm">
        <h2>추가정보 수정 </h2>
        <section class="staffModifyStaffHosInfo__introduction">
            <div class="introduction__title">
                <label for="introduction"><b>소개문</b></label>
            </div>
            <div class="introduction__content">
                <textarea id="introduction" type="text" required v-model="introduction"></textarea>
            </div>
        </section>

        <section class="staffModifyStaffHosInfo__businessHours">
            <div class="businessHours__title">
                <label for="businessHours"><b>영업 시간</b></label>
            </div>
            <div class="businessHours__content">
                <textarea id="businessHours" type="text" required v-model="consultationHour"></textarea>
            </div>
        </section>

        <section class="staffModifyStaffHosInfo__remarks">
            <div class="remarks__title">
                <label for="remarks"><b>특이 사항</b></label>
            </div>
            <div class="remarks__content">
                <textarea id="remarks" type="text" required v-model="abnormality"></textarea>
            </div>
        </section>

        <section class="staffModifyStaffHosInfo__registerButton">
            <button id="staffHosInfo" type="submit"><font-awesome-icon icon="edit"/>정보 수정하기</button>
        </section>
    </form>

    <section class="staffModifyStaffHosInfo__doctors">
        <h2>의사 정보 수정 </h2>

        <div class="doctors__null" v-if="doctors.length===0">
            <h1>등록된 의사가 없습니다.</h1>
            <button id="add_button" @click.prevent="addDoctor"><font-awesome-icon icon="pen"/>의사 추가</button>
        </div>

        <div class="doctors__modify" v-else v-for="(doctor,n) in doctors" :key="doctor.doctorId"> 
            <form @submit.prevent="modifyDoctor(doctor.doctorId,doctor.name,doctor.history)">
                <div class="modify__name">
                    의사 {{n+1}}<br>
                    <label for="name">이름: </label>
                    <input id="name" type="text" required v-model="doctor.name">
                </div>
                <div class="modify__history">
                    <div class="history__title">
                        <label for="history">이력</label>
                    </div>
                    <div class="history__content">
                        <textarea id="history" type="text" required v-model="doctor.history"/>
                    </div>
                </div>
                <div class="modify__doctor">
                    <button id="modify__button" type="submit"><font-awesome-icon icon="edit"/>의사 수정</button>
                </div>
            </form>

            <form @submit.prevent="deleteDoctor(doctor.doctorId)">
                <div class="delete__doctor">
                    <button id="delete__button" type="submit"><font-awesome-icon icon="trash-alt"/>의사 삭제</button>
                </div>
            </form>
        </div> 

        <div class="doctors__create" v-if="isDoctorCrate===true">
            <form @submit.prevent="createDoctor">
                <div class="create__name">
                    <label for="name">이름: </label>
                    <input id="name" type="text" required v-model="name">
                </div>
                <div class="create__history">
                    <div class="history__title">
                        <label for="history">이력</label>                        
                    </div>
                    <div class="history__content">
                        <textarea id="history" type="text" required v-model="history"/>                        
                    </div>
                </div>
                <div class="register__doctor">
                    <button id="register__doctor__button" type="submit"><font-awesome-icon icon="pen"/>의사 등록</button>
                </div>
            </form>
        </div>

        <div class="add__doctor">
            <button id="add_button" v-if="doctors.length!==0" @click.prevent="addDoctor"><font-awesome-icon icon="pen"/>의사 추가</button>
        </div>
    </section>

</section>
</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core';
import { faEdit,faPen } from '@fortawesome/free-solid-svg-icons';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';

import {staffViewStaffHospitalInfo,staffDeleteDoctor,
        staffModifyStaffHosInfo,staffModifyDoctor,staffCreateDoctor} from '@/api/staff';
library.add(faEdit,faPen)
library.add(faTrashAlt)

export default {
    data() {
        return {
            introduction:'',
            consultationHour:'',
            abnormality:'',
            staffHosInfoId:'',
            hospitalId:'',
            doctors:[],
            isDoctorCrate:false,
            name:'',
            memberId:'',
            history:''
        };
    },
    methods:{
        async submitForm(){
            const staffHosData = {
                introduction:this.introduction,
                consultationHour:this.consultationHour,
                abnormality:this.abnormality,
                memberId:this.memberId,
            }
            await staffModifyStaffHosInfo(this.staffHosInfoId, staffHosData);
            this.$alert('추가 정보 수정이 정상적으로 이뤄졌습니다.');
            
            this.$router.push({name:'StaffViewHospital',
                query: {hospitalId:this.hospitalId, staffHosInfoId:this.staffHosInfoId}
            }); 
        },
        async deleteDoctor(doctorId){
            if(confirm('해당 의사를 삭제하시겠습니까?')){
                await staffDeleteDoctor(this.memberId, doctorId);
                this.loadStaffHosInfo();
                this.$alert('정상적으로 삭제됐습니다.');
            }
        },
        async modifyDoctor(doctorId,name,history){
            if(confirm('의사 정보를 수정하시겠습니까?')){
                const doctorData={
                    memberId:this.memberId,
                    name:name,
                    history:history
                };
                await staffModifyDoctor(doctorId,doctorData);
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
                    history:this.history,
                    memberId:this.memberId
                };

                await staffCreateDoctor(doctorData);
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
            const {data} = await staffViewStaffHospitalInfo(this.staffHosInfoId);

            this.introduction = data.introduction;
            this.consultationHour = data.consultationHour;
            this.abnormality = data.abnormality;
            this.doctors = data.doctors;
        }
    },
    async created(){
        this.staffHosInfoId = this.$route.query.staffHosInfoId;
        this.hospitalId = this.$route.query.hospitalId;
        this.memberId = this.$store.getters.getMemberId;
        const {data} = await staffViewStaffHospitalInfo(this.staffHosInfoId);

        this.introduction = data.introduction;
        this.consultationHour = data.consultationHour;
        this.abnormality = data.abnormality;
        this.doctors = data.doctors;
    }
}
</script>

<style>
#staffModifyStaffHosInfo{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
  bottom:-50px;
}

.introduction__content #introduction{
    width:100%;
    height:150px;
    border-radius: 10px;
}

.businessHours__content #businessHours{
    width:100%;
    height:50px;
    border-radius: 10px;
}

.remarks__content #remarks{
    width:100%;
    height:150px;
    border-radius: 10px;
}

.staffModifyStaffHosInfo__registerButton{
    position:relative;
    bottom:-25px;
    text-align:right;
}

.staffModifyStaffHosInfo__doctors{
    border-top: 1px solid #dee2e6!important;
    position:relative;
    bottom:-50px;
}

.doctrs__button{
    text-align: center;
}



.staffModifyStaffHosInfo__doctors .modify__doctor{
    text-align: right;
}

.staffModifyStaffHosInfo__doctors .delete__doctor{
    position: relative;
    text-align: right;
    bottom:-10px;
}

.staffModifyStaffHosInfo__doctors .register__doctor{
    position: relative;
    text-align: right;
}

.staffModifyStaffHosInfo__doctors .add__doctor{
    position: relative;
    text-align: center;
}

.doctors__null{
    text-align: center;
}

.staffModifyStaffHosInfo__registerButton #staffHosInfo{
    background-color:#b0b8fb; 
    color: white; 
    border-radius:10px;
    height:30px;
}

.staffModifyStaffHosInfo__doctors .modify__doctor #modify__button{
    background-color:#0067a3; 
    color: white; 
    border-radius:10px;
    height:30px;
}

.staffModifyStaffHosInfo__doctors .delete__doctor #delete__button{
    background-color:#0067a3; 
    color: white; 
    border-radius:10px;
    height:30px;
}

.staffModifyStaffHosInfo__doctors .register__doctor #register__doctor__button{
    background-color:#0067a3; 
    color: white; 
    border-radius:10px;
    height:30px;
}

.staffModifyStaffHosInfo__doctors .doctors__null #add_button{
    background-color:#0067a3; 
    color: white; 
    border-radius:10px;
    height:30px;
}

.staffModifyStaffHosInfo__doctors .add__doctor #add_button{
    background-color:#0067a3; 
    color: white; 
    border-radius:10px;
    height:30px;
}

.staffModifyStaffHosInfo__doctors .doctors__create .history__content{
    width:100%;
    height:70px;
    border-radius: 10px;
}

#staffModifyStaffHosInfo .history__content #history{
    width:100%;
    height:70px;
    border-radius: 10px;
}



</style>