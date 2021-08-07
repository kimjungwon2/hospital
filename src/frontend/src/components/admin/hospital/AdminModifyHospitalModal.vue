<template>
  <div class="tag__modal">
    <div class="white__background">
        <form @submit.prevent="submitForm">
          <div>
              <label for="hospitalName">병원 이름</label>
              <input id="hospitalName" type="text" required v-model="hospital.hospitalName">
          </div>
          <div>
              <label for="licensingDate">인허가 날짜</label>
              <input id="licensingDate" type="text" required v-model="hospital.licensingDate">
          </div>
          <div>
              <label for="phoneNumber">전화번호</label>
              <input id="phoneNumber" type="text" required v-model="hospital.phoneNumber">
          </div>
          <div>
              <label for="distinguishedName">의료 기관종 별명</label>
              <input id="distinguishedName" type="text" required v-model="hospital.distinguishedName">
          </div>
          <div>
              <label for="medicalSubjectInformation">진료 과목 내용</label>
              <input id="medicalSubjectInformation" type="text" required v-model="hospital.medicalSubjectInformation">
          </div>
          <div>
              <label for="cityName">도시명</label>
              <input id="cityName" type="text" required v-model="hospital.cityName">
          </div>
          <div>
              <label for="businessCondition">영업 상태명</label>
              <select name="businessCondition" required v-model="hospital.businessCondition">
                    <option value="">영업 상태명</option>
                    <option value="영업중">영업중</option>
                    <option value="휴업">휴업</option>
                    <option value="폐업">폐업</option>
                    <option value="직권폐업">직권폐업</option>
              </select>
          </div>
          <div v-if="hospital.detailedHosInfoId!==null">

              <div>
                <label for="detailedModifyCheck">상세 정보 수정 유무</label>
                <input type="checkbox" name="detailedModifyCheck" value="true" v-model="detailedModifyCheck">
              </div>

              <div v-if="detailedModifyCheck===true">
                    <div>
                      <label for="numberHealthcareProvider">의료인 수:</label>
                      <input id="numberHealthcareProvider" type="text" required v-model="hospital.numberHealthcareProvider">
                    </div>
                    <div>
                      <label for="numberWard">병상 수:</label>
                      <input id="numberWard" type="text" required v-model="hospital.numberWard">
                    </div>
                    <div>
                      <label for="numberPatientRoom">입원실 수:</label>
                      <input id="numberPatientRoom" type="text" required v-model="hospital.numberPatientRoom">
                    </div>
                    <div>
                      <label for="x_coordination">x 좌표:</label>
                      <input id="x_coordination" type="text" required v-model="hospital.x_coordination">
                    </div>
                    <div>
                      <label for="y_coordination">y 좌표:</label>
                      <input id="y_coordination" type="text" required v-model="hospital.y_coordination">
                    </div>
                    <div>
                      <label for="latitude">위도:</label>
                      <input id="latitude" type="text" required v-model="hospital.latitude">
                    </div>
                    <div>
                      <label for="longitude">경도:</label>
                      <input id="longitude" type="text" required v-model="hospital.longitude">
                    </div>
                    <div>
                      <label for="landLotBasedSystem">지번 주소:</label>
                      <input id="landLotBasedSystem" type="text" required v-model="hospital.landLotBasedSystem">
                    </div>
                    <div>
                      <label for="roadBaseAddress">도로명 주소:</label>
                      <input id="roadBaseAddress" type="text" required v-model="hospital.roadBaseAddress">
                    </div>
                    <div>
                      <label for="zipCode">우편 번호:</label>
                      <input id="zipCode" type="text" required v-model="hospital.zipCode">
                    </div>
              </div>
          </div>

          <button type="submit">병원 정보 수정</button>
          <p>{{ logMessage }}</p>

          </form>
          <button @click.prevent="cancel">닫기</button>
      </div>
    </div>
</template>

<script>
import {adminModifyHospital} from '@/api/admin'

export default {
    props: {
        hospital: {
            type:Object,
            required: true,
        },
    },
    data() {
        return {
            //추가 정보 수정 유무
            detailedModifyCheck:false,
            hospitalId:'',   
            //log
            logMessage:'',
        };
    },
    methods:{
        cancel(){
            this.$emit('hospitalCancel');
        },
        async submitForm(){

          //상세 정보와 병원 정보 등록.
          if(this.detailedModifyCheck===true){
            
            const hospitalData = {
                hospitalName: this.hospital.hospitalName,
                licensingDate: this.hospital.licensingDate,
                phoneNumber: this.hospital.phoneNumber,
                distinguishedName: this.hospital.distinguishedName,
                medicalSubjectInformation: this.hospital.medicalSubjectInformation,
                businessCondition: this.hospital.businessCondition,
                cityName:this.hospital.cityName,
                detailedHosInfoId:this.hospital.detailedHosInfoId,

                detailedModifyCheck:this.detailedModifyCheck,

                numberHealthcareProvider:this.hospital.numberHealthcareProvider,
                numberWard:this.hospital.numberWard,
                numberPatientRoom:this.hospital.numberPatientRoom,
                hospitalAddress:{
                    landLotBasedSystem:this.hospital.landLotBasedSystem,
                    roadBaseAddress:this.hospital.roadBaseAddress,
                    zipCode:this.hospital.zipCode,
                },
                hospitalLocation:{
                     x_coordination:this.hospital.x_coordination,
                     y_coordination:this.hospital.y_coordination,
                     latitude:this.hospital.latitude,
                     longitude:this.hospital.longitude,
                },
            }

            if(this.numberHealthcareProvider==='' ||this.numberWard==='' ||
             this.x_coordination===''|| this.y_coordination===''|| this.latitude===''||
             this.longitude===''|| this.landLotBasedSystem===''|| this.roadBaseAddress===''||
             this.zipCode ===''){
               this.$alert('상세 정보를 모두 기입해주세요.');
            }
            else{
              if(confirm('병원 정보와 상세 정보를 수정하시겠습니까?')){
                await adminModifyHospital(this.hospitalId, hospitalData);
                this.$alert("병원 수정이 완료되었습니다.");
                this.$emit('hospitalLoad');
                this.initForm();
                this.cancel();
              }
            }
          } 
          //상세 정보 없이 등록
          else{
              const hospitalData = {
                hospitalName: this.hospital.hospitalName,
                licensingDate: this.hospital.licensingDate,
                phoneNumber: this.hospital.phoneNumber,
                distinguishedName: this.hospital.distinguishedName,
                medicalSubjectInformation: this.hospital.medicalSubjectInformation,
                businessCondition: this.hospital.businessCondition,
                cityName:this.hospital.cityName,
                detailedModifyCheck:this.detailedModifyCheck,
            }
            if(confirm('병원 정보를 수정하시겠습니까?')){
                await adminModifyHospital(this.hospitalId,hospitalData);
                this.$alert("병원 수정이 완료되었습니다.");
                this.$emit('hospitalLoad');
                this.initForm();
                this.cancel();
            }
          }
        },
        initForm(){
            this.detailedModifyCheck=false;
        },
    },
    created(){
        this.hospitalId = this.$route.query.hospitalId;
    }

}
</script>

<style>
.tag__modal{
    width:100%;
    height:100%;
    background:rgba(0,0,0,0.5);
    position:fixed; padding:20px;
}

.white__background{
    width:60%; 
    background: white;
    border-radius: 8px;
    padding:10%;
}

</style>