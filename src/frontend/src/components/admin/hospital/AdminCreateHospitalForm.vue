<template>
      <form @submit.prevent="submitForm">
        <div>
            <label for="hospitalName">병원 이름</label>
            <input id="hospitalName" type="text" v-model="hospitalName">
        </div>
        <div>
            <label for="licensingDate">인허가 날짜</label>
            <input id="licensingDate" type="text" v-model="licensingDate">
        </div>
        <div>
            <label for="phoneNumber">전화번호</label>
            <input id="phoneNumber" type="text" v-model="phoneNumber">
        </div>
        <div>
            <label for="distinguishedName">의료 기관종 별명</label>
            <input id="distinguishedName" type="text" v-model="distinguishedName">
        </div>
        <div>
            <label for="medicalSubjectInformation">진료 과목 내용</label>
            <input id="medicalSubjectInformation" type="text" v-model="medicalSubjectInformation">
        </div>
        <div>
            <label for="cityName">도시명</label>
            <input id="cityName" type="text" v-model="cityName">
        </div>
        <div>
            <label for="businessCondition">영업 상태명</label>
            <select name="businessCondition" v-model="businessCondition">
                  <option value="">영업 상태명</option>
                  <option value="영업중">영업중</option>
                  <option value="휴업">휴업</option>
                  <option value="폐업">폐업</option>
                  <option value="직권폐업">직권폐업</option>
            </select>
        </div>
        <div>
          <label for="detailedInfoCheck">상세 정보 추가</label>
          <input type="checkbox" name="detailedInfoCheck" value="true" v-model="detailedInfoCheck">
        </div>
        <div v-if="detailedInfoCheck===true">
          <div>
            <label for="numberHealthcareProvider">의료인 수:</label>
            <input id="numberHealthcareProvider" type="text" v-model="numberHealthcareProvider">
          </div>
          <div>
            <label for="numberWard">병상 수:</label>
            <input id="numberWard" type="text" v-model="numberWard">
          </div>
          <div>
            <label for="numberPatientRoom">입원실 수:</label>
            <input id="numberPatientRoom" type="text" v-model="numberPatientRoom">
          </div>
          <div>
            <label for="x_coordination">x 좌표:</label>
            <input id="x_coordination" type="text" v-model="x_coordination">
          </div>
          <div>
            <label for="y_coordination">y 좌표:</label>
            <input id="y_coordination" type="text" v-model="y_coordination">
          </div>
          <div>
            <label for="latitude">위도:</label>
            <input id="latitude" type="text" v-model="latitude">
          </div>
          <div>
            <label for="longitude">경도:</label>
            <input id="longitude" type="text" v-model="longitude">
          </div>
          <div>
            <label for="landLotBasedSystem">지번 주소:</label>
            <input id="landLotBasedSystem" type="text" v-model="landLotBasedSystem">
          </div>
          <div>
            <label for="roadBaseAddress">도로명 주소:</label>
            <input id="roadBaseAddress" type="text" v-model="roadBaseAddress">
          </div>
          <div>
            <label for="zipCode">우편 번호:</label>
            <input id="zipCode" type="text" v-model="zipCode">
          </div>
        </div>
        <button type="submit">병원 등록</button>
        <p>{{ logMessage }}</p>
    </form>
</template>

<script>
import {adminCreateHospital} from '@/api/admin'

export default {
    data() {
        return {
            hospitalName:'',
            licensingDate:'',
            phoneNumber:'',
            distinguishedName:'',
            medicalSubjectInformation:'',
            businessCondition:'',
            cityName:'',

            //추가 정보
            detailedInfoCheck:false,

            numberHealthcareProvider:'',
            numberWard:'',
            numberPatientRoom:'',

            x_coordination:'',
            y_coordination:'',
            latitude:'',
            longitude:'',

            landLotBasedSystem:'',
            roadBaseAddress:'',
            zipCode:'',
            
            //log
            logMessage:'',
        };
    },
    methods:{
        async submitForm(){

          //상세 정보와 병원 정보 등록.
          if(this.detailedInfoCheck===true){
            
            const hospitalData = {
                hospitalName: this.hospitalName,
                licensingDate: this.licensingDate,
                phoneNumber: this.phoneNumber,
                distinguishedName: this.distinguishedName,
                medicalSubjectInformation: this.medicalSubjectInformation,
                businessCondition: this.businessCondition,
                cityName:this.cityName,

                detailedInfoCheck:this.detailedInfoCheck,

                numberHealthcareProvider:this.numberHealthcareProvider,
                numberWard:this.numberWard,
                numberPatientRoom:this.numberPatientRoom,
                hospitalAddress:{
                    landLotBasedSystem:this.landLotBasedSystem,
                    roadBaseAddress:this.roadBaseAddress,
                    zipCode:this.zipCode,
                },
                hospitalLocation:{
                     x_coordination:this.x_coordination,
                     y_coordination:this.y_coordination,
                     latitude:this.latitude,
                     longitude:this.longitude,
                },
            }

            if(this.numberHealthcareProvider==='' ||this.numberWard==='' ||
             this.x_coordination===''|| this.y_coordination===''|| this.latitude===''||
             this.longitude===''|| this.landLotBasedSystem===''|| this.roadBaseAddress===''||
             this.zipCode ===''){
               this.$alert('상세 정보를 모두 기입해주세요.');
             }
            else{
                await adminCreateHospital(hospitalData);
                this.$alert("병원 등록이 완료되었습니다.")
                this.initForm();
                this.$router.push('/admin/hospitals').catch(error=>error);
            }
          } 
          //상세 정보 없이 등록
          else{
              const hospitalData = {
                hospitalName: this.hospitalName,
                licensingDate: this.licensingDate,
                phoneNumber: this.phoneNumber,
                distinguishedName: this.distinguishedName,
                medicalSubjectInformation: this.medicalSubjectInformation,
                businessCondition: this.businessCondition,
                cityName:this.cityName,
                detailedInfoCheck:this.detailedInfoCheck,
            }
            await adminCreateHospital(hospitalData);
            this.$alert("병원 등록이 완료되었습니다.")
            this.initForm();
            this.$router.push('/admin/hospitals').catch(error=>error);
          }
        },
        initForm(){
            this.hospitalName = '';
            this.licensingDate = '';
            this.userName = '';
            this.phoneNumber = '';
            this.distinguishedName = '';
            this.medicalSubjectInformation = '';
            this.businessCondition = '';
            this.cityName = '';
            this.detailedInfoCheck=false;
            this.numberHealthcareProvider='';
            this.numberWard='';
            this.numberPatientRoom='';

            this.x_coordination='';
            this.y_coordination='';
            this.latitude='';
            this.longitude='';

            this.landLotBasedSystem='';
            this.roadBaseAddress='';
            this.zipCode='';
        },
    }

}
</script>

<style>

</style>