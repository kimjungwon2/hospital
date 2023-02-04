<template>
<section id = "staffModifyStaffInfo">
  <section class="staffModifyHospital__categories">
    <button class="hospital__category__btn" @click.prevent=viewHospital> 병원 정보 </button>
    <button class="hospital__category__btn"> 추가 병원 정보 </button>
    <button class="hospital__category__btn" @click.prevent="viewThumbnail"> 병원 섬네일 </button>
    <button class="hospital__category__btn" @click.prevent="viewHospitalImage"> 병원 이미지</button>
  </section>

  <section class="staffModifyStaffInfo__register" v-if="staffHosInfoId===null">
        <h1>등록된 추가 정보가 없습니다. </h1>
        <br>
        <button class="register__staffHosInfo"  @click.prevent="createHospital">
          <font-awesome-icon icon="edit"/>추가 정보 등록
        </button>
  </section>

  <section class="staffModifyStaffInfo__staffInfo" v-else>
      <br>
      <h1 id="staffHosInfoId">추가 정보 ID: {{staffHosInfoId}}<br><br></h1> 
      <h1>병원 소개</h1>
      <div class="staffInfo__introduction">
        {{staffHosInfo.introduction}}<br><br>
      </div>
      <h1>영업 시간</h1>
      <div class="staffInfo__consultationHour">
        {{staffHosInfo.consultationHour}}<br><br>
      </div>
      <h1>특이 사항</h1>
      <div class="staffInfo__abnormality">
        {{staffHosInfo.abnormality}}<br><br>
      </div>

      <div class="staffInfo__doctors" v-if="doctors.length!==0" >
        <h1>의사</h1>
        <div class="doctors__doctor" v-for="(doctor, i) in doctors" :key="doctor.doctorId">
            의사 ({{i+1}})<br><br>
            의사 번호: {{doctor.doctorId}}<br>
            이름: {{doctor.name}}<br>
            경력: {{doctor.history}}<br><br>
        </div>
      </div>
      <div class="staffInfo__noDoctor" v-else>
        <h1>의사</h1>
        <div class="noDoctor__noContent">
          <h1>의사가 등록되지 않았습니다.</h1>
        </div>
      </div>
      <br>
  </section>

  <section class="staffModifyStaffInfo__button" v-if="staffHosInfoId!=null">
    <button id="button__modify" @click.prevent="editStaffHospitalInfo">
      <font-awesome-icon icon="edit"/>수정하기      
    </button>
    <button id="button__delete" @click.prevent="deleteStaffHospitalInfo">
      <font-awesome-icon icon="trash-alt"/>삭제하기      
    </button>
  </section>    
    
</section>
</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core';
import { faEdit } from '@fortawesome/free-solid-svg-icons';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';

import {staffViewStaffHospitalInfo,staffDeleteStaffHosInfo} from '@/api/staff';

library.add(faEdit)
library.add(faTrashAlt)

export default {
   data() {
      return {
        hospitalId:'',
        staffHosInfoId:'',
        staffHosInfo:[],
        doctors:[],
      }
  },
  methods:{
    //병원 정보 보기
    viewHospital(){
            this.$router.push(`/staff/view/hospital`); 
    },
    //병원 섬네일 보기
    viewThumbnail(){
        this.$router.push({name:'staffThumbnail',
            query: {hospitalId:this.$route.query.hospitalId,
                staffHosInfoId:this.$route.query.staffHosInfoId,
                thumbnailId:this.$route.query.thumbnailId}
        }); 
    },
    viewHospitalImage(){
            this.$router.push({name:'staffHospitalImage',
                query: {hospitalId:this.$route.query.hospitalId,
                    staffHosInfoId:this.$route.query.staffHosInfoId,
                    thumbnailId:this.$route.query.thumbnailId}
            }); 
    },
    createHospital(){
      this.$router.push('/staff/register/'+this.hospitalId+'/staffHosInfo');
    },
    async deleteStaffHospitalInfo(){
      if(confirm('정말로 추가 정보를 삭제하시겠습니까?')){
          await staffDeleteStaffHosInfo(this.$store.getters.getMemberId,this.staffHosInfoId);
          this.loadDeleteStaffHosInfo();
        }
    },
    editStaffHospitalInfo(){
      this.$router.push({name:'staffEditStaffHospital',
                query: {hospitalId:this.hospitalId, staffHosInfoId:this.staffHosInfoId}
        }); 
    },
    loadDeleteStaffHosInfo(){
         this.staffHosInfoId = null;
         this.staffHosInfo = null;
         this.doctors = null;
         this.$router.push({name:'StaffViewHospital',
                query: {hospitaId:this.hospitalId, staffHosInfoId:this.staffHosInfoId}
        }); 
    }
  },
  async created(){
    this.staffHosInfoId = this.$route.query.staffHosInfoId;
    this.hospitalId = this.$route.query.hospitalId;

    if(this.staffHosInfoId!==null){
         const {data} = await staffViewStaffHospitalInfo(this.staffHosInfoId);
         this.staffHosInfo = data;
         this.doctors = data.doctors;
    }

  }

}
</script>

<style>
#staffModifyStaffInfo{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
}

#staffModifyStaffInfo .staffModifyHospital__categories .hospital__category__btn.active,
#staffModifyStaffInfo .staffModifyHospital__categories .hospital__category__btn:hover{
  background-color:#006ab0;
  color:white;
}

#staffModifyStaffInfo .staffModifyHospital__categories .hospital__category__btn{
  border-top: 1px solid #dee2e6!important;
  border-bottom: 1px solid #dee2e6!important;
  margin:5px;
  width: 20%;
  font-size:14px;
  border-radius: 4px;
  padding: 8px 48px;
  size:35px;
  height:50px;
}

#staffModifyStaffInfo .staffModifyHospital__categories{
  margin-left:12%;
  margin-top:1%;
  margin-bottom:1%;
  position: relative;
  text-align: left;
}

.staffModifyStaffInfo__register{
  text-align:center;
}

.staffModifyStaffInfo__register .register__staffHosInfo{
  background-color:#0067a3; 
  color: white; 
  font-size: 15px; 
  height: 32px; 
  line-height: 32px;
  margin-right:10px;
  border-radius:10px;
}

.staffModifyStaffInfo__staffInfo #staffHosInfoId{
  border-bottom: 1px dotted #e2e2e2!important;
}

.staffModifyStaffInfo__staffInfo .staffInfo__introduction{
  border-bottom: 1px dotted #e2e2e2!important;
}

.staffModifyStaffInfo__staffInfo .staffInfo__consultationHour{
  border-bottom: 1px dotted #e2e2e2!important;
}

.staffModifyStaffInfo__staffInfo .staffInfo__abnormality{
  border-bottom: 1px dotted #e2e2e2!important;
}

.staffInfo__doctors .doctors__doctor{
    margin-top:10px;
    border-bottom: 1px dotted #e2e2e2!important;    
}

.staffInfo__noDoctor .noDoctor__noContent{
  text-align:center;
}

.staffModifyStaffInfo__button{
  display:flex;
  float:right;
  height:100%;
  position:relative;
  top:10px;
}

.staffModifyStaffInfo__button #button__modify{
  background-color:#0067a3; 
  color: white; 
  font-size: 15px; 
  height: 32px; 
  line-height: 32px;
  margin-right:10px;
  border-radius:10px;
}

.staffModifyStaffInfo__button #button__delete{
  background-color:#0067a3; 
  color: white; 
  font-size: 15px; 
  height: 32px; 
  line-height: 32px;
  margin-right:10px;
  border-radius:10px;
}

</style>