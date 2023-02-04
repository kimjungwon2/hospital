<template>
<section id="staffCreateStaffHosInfo">
    <form @submit.prevent="submitForm">
        <section class="staffCreateStaffHosInfo__introduction">
            <div class="introduction__title">
                <label for="introduction"><b>소개문</b></label>
            </div>
            <div class="introduction__content">
                <textarea id="introduction" type="text" required v-model="introduction"></textarea>
            </div>
        </section>

        <section class="staffCreateStaffHosInfo__businessHours">
            <div class="businessHours__title">
                <label for="businessHours"><b>영업 시간</b></label>
            </div>
            <div class="businessHours__content">
                <textarea id="businessHours" type="text" required v-model="consultationHour"></textarea>
            </div>
        </section>

        <section class="staffCreateStaffHosInfo__remarks">
            <div class="remarks__title">
                <label for="remarks"><b>특이 사항</b></label>
            </div>
            <div class="remarks__content">
                <textarea id="remarks" type="text" required v-model="abnormality"></textarea>
            </div>
        </section>

        <section class="staffCreateStaffHosInfo__doctors">
            <h2>의사 {{doctorNumber}}</h2>
            <div class="doctors__item" v-if="doctorNumber!==0">
                <div class="item__doctor" v-for="n in doctorNumber" :key="n">
                    <b>의사 {{n}}</b>
                    <div class="doctor__name">
                        <label for="name"><b>이름: </b></label>
                        <input id="name" type="text" required v-model="name[n-1]">
                    </div>
                    <div class="doctor__history">
                        <div class="history__title">
                            <label for="history"><b>이력</b></label>
                        </div>
                        <div class="history__content">
                            <textarea id="history" type="text" required v-model="history[n-1]"/>
                        </div>
                    </div>
                </div>
            </div>

            <div class="doctrs__button">
                <button id="button__add" @click.prevent="addDoctor">의사 추가하기</button><br>
                <button id="button__delete" v-if="doctorNumber>0" @click.prevent="minusDoctor">의사 제거</button><br>
            </div>
        </section>

        <section class="staffCreateStaffHosInfo__registerButton">
            <button id="staffHosInfo" type="submit">정보 등록하기</button>
        </section>
    </form>
</section>
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
                this.$router.push({name:'StaffViewHospital',
                    query: {hospitalId:this.hospitalId, staffHosInfoId:staffHosInfoId.data.id,}
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
    },
}
</script>

<style>
#staffCreateStaffHosInfo{
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

.staffCreateStaffHosInfo__registerButton{
    position:relative;
    bottom:-25px;
    text-align:right;
}

.staffCreateStaffHosInfo__doctors{
    border-top: 1px solid #dee2e6!important;
      position:relative;
      bottom:-15px;
}

.doctrs__button{
    text-align: center;
}

.doctrs__button #button__add{
  
  background-color:#0067a3; 
  color: white; 
  font-size: 15px; 
  height: 32px; 
  line-height: 32px;
  margin-right:10px;
  border-radius:10px;
}

.doctrs__button #button__delete{
  position:relative;
  bottom:-8px;
  background-color:#0067a3; 
  color: white; 
  font-size: 15px; 
  height: 32px; 
  line-height: 32px;
  margin-right:10px;
  border-radius:10px;
}

.staffCreateStaffHosInfo__registerButton #staffHosInfo{
    background-color:#b0b8fb; 
    color: white; 
    border-radius:10px;
    height:30px;
}

.history__content #history{
    width:100%;
    height:70px;
    border-radius: 10px;
}

</style>