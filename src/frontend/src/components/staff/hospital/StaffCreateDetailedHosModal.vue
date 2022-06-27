<template>
    <div class="tag__modal">
        <div class="white__background">
            <form @submit.prevent="submitForm">
                <div>
                    <label for="numberHealthcareProvider">의료인 수:</label>
                    <input id="numberHealthcareProvider" type="text" required  v-model="numberHealthcareProvider">
                  </div>
                  <div>
                    <label for="numberWard">병상 수:</label>
                    <input id="numberWard" type="text" required  v-model="numberWard">
                  </div>
                  <div>
                    <label for="numberPatientRoom">입원실 수:</label>
                    <input id="numberPatientRoom" required type="text" v-model="numberPatientRoom">
                  </div>
                  <div>
                    <label for="x_coordination">x 좌표:</label>
                    <input id="x_coordination" type="text" required v-model="x_coordination">
                  </div>
                  <div>
                    <label for="y_coordination">y 좌표:</label>
                    <input id="y_coordination" type="text" required v-model="y_coordination">
                  </div>
                  <div>
                    <label for="latitude">위도:</label>
                    <input id="latitude" type="text" required v-model="latitude">
                  </div>
                  <div>
                    <label for="longitude">경도:</label>
                    <input id="longitude" type="text" required v-model="longitude">
                  </div>
                  <div>
                    <label for="landLotBasedSystem">지번 주소:</label>
                    <input id="landLotBasedSystem" type="text" required v-model="landLotBasedSystem">
                  </div>
                  <div>
                    <label for="roadBaseAddress">도로명 주소:</label>
                    <input id="roadBaseAddress" type="text" required v-model="roadBaseAddress">
                  </div>
                  <div>
                    <label for="zipCode">우편 번호:</label>
                    <input id="zipCode" type="text" required v-model="zipCode">
                  </div>
                <button type="submit">상세 정보 등록</button>
                <p>{{ logMessage }}</p>
            </form>
            <button @click.prevent="cancel">닫기</button>
        </div>
    </div>

</template>

<script>
import {staffRegisterDetailedHosInfo} from '@/api/staff';

export default {
    props: {
        hospitalId: {
            type: Number,
            required: true,
        },
    },
    data() {
        return {
            detailedHosId:'',

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
        }
    },
    methods:{
        cancel(){
            this.$emit('createDetailedCancel');
        },
        async submitForm(){
            if(confirm('상세 정보를 추가하시겠습니까?')){
                const detailedData = {
                    hospitalId:this.hospitalId,
                    memberId:this.$store.getters.getMemberId,
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

                const detailedHos = await staffRegisterDetailedHosInfo(detailedData);
                this.detailedHosId = detailedHos.data.id;
                this.$emit('detailedHospitalLoad',this.detailedHosId);
                this.$alert('상세 정보 등록이 완료되었습니다.');
                this.cancel();
            }
        },
        initForm(){
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
    },
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