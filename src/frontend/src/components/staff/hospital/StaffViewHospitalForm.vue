<template>
  <section id="staffModifyHospital">
    <section class="staffModifyHospital__categories">
        <button class="hospital__category__btn"> 병원 정보 </button>
        <button class="hospital__category__btn" @click.prevent="viewStaffHospital"> 추가 병원 정보 </button>
        <button class="hospital__category__btn" @click.prevent="viewThumbnail"> 병원 섬네일 </button>
        <button class="hospital__category__btn" @click.prevent="viewHospitalImage"> 병원 이미지</button>
    </section>
      <StaffModifyHospitalModal :hospitalId="hospital.hospitalId" :hospital="hospital" v-if ="isModifyHospital===true" 
      @hospitalCancel="cancelModifyHospital" @hospitalLoad="staffLoadHospital"/>

      <StaffCreateDetailedHosModal :hospitalId="hospital.hospitalId" v-if="isCreateDetailed==true"
      @createDetailedCancel="cancelCreateDetailed" @detailedHospitalLoad="staffCreateDetailedLoadHospital"/>

      <StaffCreateTagModal :hospitalId="hospital.hospitalId" :hospitalTags="hospitalTags" v-if ="isCreateTags===true" 
      @tagCancel="cancelTagModal" @tagLoad="staffLoadHospital"/>

    <section class="staffModifyHospital__modifyButton">
        <button class="functionBox__modifyHospital" @click.prevent="modifyHospital">
          <font-awesome-icon icon="pen"/> 병원 수정
        </button>
    </section>


    <section class="staffModifyHospital__hospitalInfo">
        <h1>병원 정보</h1>
        <div class="hospitalInfo__staff">
            <div class="staff__class">
                병원 번호
            </div>
            <div class="staff__content">
                {{hospital.hospitalId}}
            </div>
        </div>
        <div class="hospitalInfo__staff">
            <div class="staff__class">
                병원 이름
            </div>
            <div class="staff__content">
                {{hospital.hospitalName}}
            </div>
        </div>
        <div class="hospitalInfo__staff">
            <div class="staff__class">
                개업일
            </div>
            <div class="staff__content">
                {{hospital.licensingDate| formatYear}}
            </div>
        </div>
        <div class="hospitalInfo__staff">
            <div class="staff__class">
                전화번호
            </div>
            <div class="staff__content">
                {{hospital.phoneNumber}}
            </div>
        </div>
        <div class="hospitalInfo__staff">
            <div class="staff__class">
                병원 종류
            </div>
            <div class="staff__content">
                {{hospital.distinguishedName}}
            </div>
        </div>
        <div class="hospitalInfo__staff">
            <div class="staff__class">
                과목
            </div>
            <div class="staff__content">
                {{hospital.medicalSubjectInformation}}
            </div>
        </div>
        <div class="hospitalInfo__staff">
            <div class="staff__class">
                영업 상태
            </div>
            <div class="staff__content">
                {{hospital.businessCondition}}
            </div>
        </div>
        <div class="hospitalInfo__staff">
            <div class="staff__class">
                도시 이름
            </div>
            <div class="staff__content">
                {{hospital.cityName}}
            </div>
        </div>
      </section>

      <section v-if="hospital.detailedHosInfoId!=null" class="staffModifyHospital__detailedInfo">
        <h1>상세 정보</h1>
        <div class="detailedInfo__staff">
            <div class="staff__class">
                상세 정보 번호
            </div>
            <div class="staff__content">
                {{hospital.detailedHosInfoId}}
            </div>
        </div>
        <div class="detailedInfo__staff">
            <div class="staff__class">
                종업원 수
            </div>
            <div class="staff__content">
                {{ hospital.numberHealthcareProvider}}
            </div>
        </div>
        <div class="detailedInfo__staff">
            <div class="staff__class">
                병실 수
            </div>
            <div class="staff__content">
                {{ hospital.numberWard}}
            </div>
        </div>
        <div class="detailedInfo__staff">
            <div class="staff__class">
                환자실
            </div>
            <div class="staff__content">
                {{ hospital.numberPatientRoom}}
            </div>
        </div>
        <h5>(주소)</h5>
        <div class="detailedInfo__staff">
            <div class="staff__class">
                주소(번지)
            </div>
            <div class="staff__content">
                {{ hospital.landLotBasedSystem}}
            </div>
        </div>
        <div class="detailedInfo__staff">
            <div class="staff__class">
                도로명 주소
            </div>
            <div class="staff__content">
                {{ hospital.roadBaseAddress}}
            </div>
        </div>
        <div class="detailedInfo__staff">
            <div class="staff__class">
                우편 번호
            </div>
            <div class="staff__content">
                {{ hospital.zipCode}}
            </div>
        </div>
        <h5>(위치 좌표)</h5>
        <div class="detailedInfo__staff">
            <div class="staff__class">
                x 좌표
            </div>
            <div class="staff__content">
                {{ hospital.x_coordination}}
            </div>
        </div>
        <div class="detailedInfo__staff">
            <div class="staff__class">
                y 좌표
            </div>
            <div class="staff__content">
                {{ hospital.y_coordination}}
            </div>
        </div>
        <div class="detailedInfo__delete">
            <button class="delete__detailedInfo" @click.prevent="deleteDetailedHosInfo(hospital.detailedHosInfoId)">
                <font-awesome-icon icon="trash-alt" /> 상세 정보 삭제
            </button>
        </div>
      </section>

      <section v-else class="staffModifyHospital__detailedInfo">
        <h1>상세 정보</h1>
        상세 정보가 없습니다. 
        <button @click.prevent="createDetailedHospital">(상세정보 추가하기)</button>
        
      </section>

      <section class="staffModifyHospital__tag">
         <h1>병원 태그</h1>
         <div class="tag__tagBox" v-if="hospitalTags.length===0 || createTags===false">병원 태그가 없습니다.<br><br> 
         태그 추가<font-awesome-icon icon="plus-square" @click.prevent="createTags"/></div>
         <div class="tag__tagBox" v-else> 
             <span class="tagBox__tagName" v-for="tag in hospitalTags" :key="tag.postTagId"> 
                #{{tag.tagName}} 
                <font-awesome-icon icon="trash-alt" @click.prevent="deleteTag(tag.postTagId)"/>   
             </span>
             <br><br>태그 추가<font-awesome-icon icon="plus-square" @click.prevent="createTags"/>
        </div>
     </section>

      <section v-if="estimations.length===0" class="staffModifyHospital__estimation">
        <h1>병원 평가</h1>
        등록된 병원 평가가 없습니다.<br>
      </section>

      <section v-else class="staffModifyHospital__estimation">
        <h1>병원 평가</h1>
        <p class="estimation__explanation">
            <font-awesome-icon icon="exclamation-circle" /> 건강보험심사평가원에서 제공하는 경기도 내 병원평가 정보입니다. 
        </p>
        <div class="estimation__grade" v-for="estimation in estimations" :key="estimation.estimationId"> 
              <div class="grade__item" v-if="estimation.estimationList=='주사제'">
                  <div class="item__sort">
                    <font-awesome-icon icon="syringe"/>  {{estimation.estimationList}} 
                    <span class="sort__explanation"><font-awesome-icon icon="question-circle" /></span>
                    <div class="sort__arrox-box">등급 숫자가 작을수록 주사제 처방을 적게하는 의료기관입니다.</div>
                  </div>
                  <div class="item__grade">
                      {{estimation.distinctionGrade}}
                    <span class="sort__explanation"><font-awesome-icon icon="question-circle" /></span>
                        <div class="sort__arrox-box">
                            1등급: 백분위 순위 20이하<br>
                            2등급: 백분위 순위 20초과～40이하<br>
                            3등급: 백분위 순위 40초과～60이하<br>
                            4등급: 백분위 순위 60초과～80이하<br>
                            5등급: 백분위 순위 80초과～100<br>
                            등급제외: 기간내 폐업, 진료월이 6개월미만 등
                        </div>
                  </div>
              </div>
              <div class="grade__item" v-if="estimation.estimationList=='항생제'">
                  <div class="item__sort">
                    <font-awesome-icon icon="tablets"/>   {{estimation.estimationList}}
                    <span class="sort__explanation"><font-awesome-icon icon="question-circle" /></span>
                    <p class="sort__arrox-box">등급 숫자가 작을수록 항생제 처방을 적게 하는 의료기관입니다.</p>
                  </div>
                  <div class="item__grade">
                      {{estimation.distinctionGrade}}
                    <span class="sort__explanation"><font-awesome-icon icon="question-circle" /></span>
                        <div class="sort__arrox-box">
                            1등급: 백분위 순위 20이하<br>
                            2등급: 백분위 순위 20초과～40이하<br>
                            3등급: 백분위 순위 40초과～60이하<br>
                            4등급: 백분위 순위 60초과～80이하<br>
                            5등급: 백분위 순위 80초과～100<br>
                            등급제외: 기간내 폐업, 진료월이 6개월미만 등
                        </div>
                  </div>
              </div>
              <div class="grade__item" v-if="estimation.estimationList=='처방약품비'">
                  <div class="item__sort">
                    <font-awesome-icon icon="hand-holding-medical"/>   {{estimation.estimationList}}
                    <span class="sort__explanation"><font-awesome-icon icon="question-circle" /></span>
                    <p class="sort__arrox-box">등급 숫자가 작을수록 동일평가군 대비 투약일당(환자당)약품비가 낮은 의료기관입니다.</p>
                  </div>
                  <div class="item__grade">
                      {{estimation.distinctionGrade}}
                      <span class="sort__explanation"><font-awesome-icon icon="question-circle" /></span>
                        <div class="sort__arrox-box">
                            1등급: 백분위 순위 20이하<br>
                            2등급: 백분위 순위 20초과～40이하<br>
                            3등급: 백분위 순위 40초과～60이하<br>
                            4등급: 백분위 순위 60초과～80이하<br>
                            5등급: 백분위 순위 80초과～100<br>
                            등급제외: 기간내 폐업, 진료월이 6개월미만 등
                        </div>
                  </div>
              </div>
              <div class="grade__item" v-if="estimation.estimationList=='약품목수'">
                  <div class="item__sort">
                    <font-awesome-icon icon="pills"/>   {{estimation.estimationList}}
                    <span class="sort__explanation"><font-awesome-icon icon="question-circle" /></span>
                    <p class="sort__arrox-box">등급 숫자가 작을수록 처방전당 약품목수가 적은 의료기관입니다.</p>
                  </div>
              <div class="item__grade">
                      {{estimation.distinctionGrade}}
                      <span class="sort__explanation"><font-awesome-icon icon="question-circle" /></span>
                        <div class="sort__arrox-box">
                            1등급: 백분위 순위 20이하<br>
                            2등급: 백분위 순위 20초과～40이하<br>
                            3등급: 백분위 순위 40초과～60이하<br>
                            4등급: 백분위 순위 60초과～80이하<br>
                            5등급: 백분위 순위 80초과～100<br>
                            등급제외: 기간내 폐업, 진료월이 6개월미만 등
                        </div>
                  </div>
              </div>
        </div>
      </section>

  </section>
  
</template>

<script>
import {staffViewHospital,staffDeleteHospitalTag,staffDeleteDetailedHosInfo} from '@/api/staff';
import StaffCreateTagModal from '@/components/staff/hospital/StaffCreateTagModal.vue';
import StaffModifyHospitalModal from '@/components/staff/hospital/StaffModifyHospitalModal.vue';
import StaffCreateDetailedHosModal from '@/components/staff/hospital/StaffCreateDetailedHosModal.vue';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faTrashAlt,faPen,faExclamationCircle,faQuestionCircle,faSyringe,faTablets,faHandHoldingMedical,faPills } from '@fortawesome/free-solid-svg-icons';
import { faPlusSquare } from '@fortawesome/free-solid-svg-icons';

library.add(faPen,faExclamationCircle,faTrashAlt,faPlusSquare,faQuestionCircle,faSyringe,faTablets,faHandHoldingMedical,faPills);
export default {
    components:{
        StaffCreateTagModal,
        StaffModifyHospitalModal,
        StaffCreateDetailedHosModal,
    },
    data() {
        return {
            hospital: [],

            hospitalTags:[],
            estimations:[],

            userId:'',
            memberId:'',
            isCreateDetailed:false,
            isCreateTags:false,
            isCreateEstimatons:false,
            isModifyEstimations:false,
            isModifyHospital:false,
        }
    },
    filters:{
        formatYear(value){
            let Year = value.substring(0,4);
            let Month = value.substring(4,6);
            let Date = value.substring(6,8);
            
            return `${Year}-${Month}-${Date}`; 
        },
    },
    methods:{
        //병원 추가 정보 보기
        viewStaffHospital(){
            this.$router.push({name:'StaffViewHospital',
                query: {hospitalId:this.hospital.hospitalId,
                    staffHosInfoId:this.hospital.staffHosInfoId,
                    thumbnailId:this.hospital.thumbnailId}
            }); 
        },
        //병원 섬네일 보기
        viewThumbnail(){
            this.$router.push({name:'staffThumbnail',
                query: {hospitalId:this.hospital.hospitalId,
                    staffHosInfoId:this.hospital.staffHosInfoId,
                    thumbnailId:this.hospital.thumbnailId}
            }); 
        },
        viewHospitalImage(){
            this.$router.push({name:'staffHospitalImage',
                query: {hospitalId:this.hospital.hospitalId,
                    staffHosInfoId:this.hospital.staffHosInfoId,
                    thumbnailId:this.hospital.thumbnailId}
            }); 
        },
        //상세 정보 삭제
        async deleteDetailedHosInfo(detailedHosInfoId){
            if(confirm('정말로 상세 정보를 삭제하시겠습니까?')){
               await staffDeleteDetailedHosInfo(this.memberId, detailedHosInfoId);
               this.staffDeleteDetailedLoadHospital();
            }
        },
        //태그 삭제
        async deleteTag(postTagId){
            if(confirm('정말로 해당 태그를 삭제하시겠습니까?')){
               await staffDeleteHospitalTag(this.memberId,postTagId);
               this.staffLoadHospital();
            }
        },
        //병원 정보 불러오기(삭제한 상세 정보)
        async staffDeleteDetailedLoadHospital(){
            const {data} = await staffViewHospital(this.userId);
            this.hospital = data;
            this.hospitalTags=data.hospitalTags;
            this.estimations=data.estimations;
        },
        //병원 정보 불러오기(생성한 상세 정보)
        async staffCreateDetailedLoadHospital(detailedHosId){
            const {data} = await staffViewHospital();
            this.hospital = data;
            this.hospitalTags=data.hospitalTags;
            this.estimations=data.estimations;
        },
        //병원 정보 불러오기
        async staffLoadHospital(){
            const {data} = await staffViewHospital();
            this.hospitalTags=data.hospitalTags;
            this.estimations=data.estimations;  
        },
        //모달창.
        modifyHospital(){
            this.isModifyHospital = true;
        },
        cancelModifyHospital(){
            this.isModifyHospital = false;
        },
        //Detailed 정보 생성
        createDetailedHospital(){
            this.isCreateDetailed = true;
        },
        cancelCreateDetailed(){
            this.isCreateDetailed = false;
        },

        //태그 생성
        createTags(){
            this.isCreateTags = true;
        },
        //태그 모달창 닫기
        cancelTagModal(){
            this.isCreateTags= false;
        },
    },

    async created(){
        const {data} = await staffViewHospital();
        this.hospital = data;

        this.memberId = this.$store.getters.getMemberId;
        this.hospitalTags=data.hospitalTags;
        this.estimations=data.estimations;
    },
}
</script>

<style>
#staffModifyHospital{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
}

#staffModifyHospital .functionBox__modifyHospital{
  background-color:#0067a3; 
  color: white; 
  font-size: 15px; 
  height: 32px; 
  line-height: 32px;
  margin-right:10px;
  border-radius:10px;
}

.delete__detailedInfo{
  background-color:#0067a3; 
  color: white; 
  font-size: 15px; 
  height: 32px; 
  line-height: 32px;
  margin-right:10px;
  border-radius:10px;
}

.staffModifyHospital__modifyButton{
    text-align:center;
}

#staffModifyHospital .staffModifyHospital__categories{
  margin-left:12%;
  margin-top:1%;
  margin-bottom:1%;
  position: relative;
  text-align: left;
}

#staffModifyHospital .staffModifyHospital__categories .hospital__category__btn{
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

.detailedInfo__delete{
  float:right;
  height:100%;
  position:relative;
  top:10px;
}

#staffModifyHospital .staffModifyHospital__categories .hospital__category__btn.active,
#staffModifyHospital .staffModifyHospital__categories .hospital__category__btn:hover{
  background-color:#006ab0;
  color:white;
}

.hospitalInfo__staff{
    margin-left:20px;
    margin-right:20px;
    display:flex;
    border-bottom:1px solid #d3d3d3;
}

.hospitalInfo__staff .staff__class{
    width:15%;
    text-align:center;
    padding:5px;
}

.hospitalInfo__staff .staff__content{
    margin-left:10px;
    padding:5px;
    width:80%;
}

.detailedInfo__staff{
    margin-left:20px;
    margin-right:20px;
    display:flex;
    border-bottom:1px solid #d3d3d3;
}

.detailedInfo__staff .staff__class{
    width:15%;
    text-align:center;
    padding:5px;
}

.detailedInfo__staff .staff__content{
    margin-left:10px;
    padding:5px;
    width:80%;
}

.staffModifyHospital__estimation .estimation__explanation{
    text-align:left;
    font-size: 8px;
    color: #797979;
    margin-bottom:10px;
}


.staffModifyHospital__hospitalInfo{
    margin-top:10px;
    border-top: 1px solid #dee2e6!important;
    margin-bottom:30px;    
}

.tag__tagBox{
  margin-top:10px;
  border: 2px solid #0067A3;
}

.tag__tagBox .tagBox__tagName{
  font-size: 14px; 
  letter-spacing: -0.4px;
  background-color:rgba(233, 233, 233, 0.55); 
  display: inline-block;
  margin-right:5px;
}

.staffModifyHospital__detailedInfo{
    margin-top:10px;
    border-top: 1px solid #dee2e6!important;
    margin-bottom:50px;    
}

.staffModifyHospital__tag{
    margin-top:10px;
    border-top: 1px solid #dee2e6!important;
    margin-bottom:30px;    
}

.staffModifyHospital__estimation{
    margin-top:10px;
    border-top: 1px solid #dee2e6!important;
    margin-bottom:30px;        
}




.staffModifyHospital__estimation .grade__item{
    display:flex;
}

.staffModifyHospital__estimation .item__sort{
    width:130px;
}

.staffModifyHospital__estimation .sort__explanation{
    position:relative;
    bottom:12px;
    right:7px;
    font-size:8px;
    cursor: pointer;
}

.staffModifyHospital__estimation .sort__explanation:hover + .sort__arrox-box{
    display:block;
}

.staffModifyHospital__estimation .sort__arrox-box{
    display:none;
    position:relative;
    left:60px;
    bottom:60px;
   -webkit-border-radius: 8px;
   -moz-border-radius: 8px;
   border-radius: 8px;
   background: #005b96;
   color: white;
   font-size: 10px;
   text-align:center;
   width:300px;
}

</style>