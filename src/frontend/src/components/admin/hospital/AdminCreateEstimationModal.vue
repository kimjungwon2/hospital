<template>
    <div class="tag__modal">
      <div class="white__background">
            <p>병원에 등록된 평가<br>
                <span v-for="estimation in estimations" :key="estimation.estimationId"> 
                <b>평가 목록:</b>{{estimation.estimationList}} 평가 등급:{{estimation.distinctionGrade}}<br>
                </span> 
            </p>

      <form @submit.prevent="submitForm">    
            <div>
                <label for="estimationList">평가내역</label>
                <select name="estimationList" required v-model="estimationList">
                      <option value="">평가내역</option>
                      <option value="처방약품비">처방약품비</option>
                      <option value="약품목수">약품목수</option>
                      <option value="항생제">항생제</option>
                      <option value="주사제">주사제</option>
                </select>
            </div>
            <div>
                <label for="distinctionGrade">평가등급</label>
                <select name="distinctionGrade" required v-model="distinctionGrade">
                      <option value="">평가등급</option>
                      <option value="1등급">1등급</option>
                      <option value="2등급">2등급</option>
                      <option value="3등급">3등급</option>
                      <option value="4등급">4등급</option>
                      <option value="5등급">5등급</option>
                      <option value="등급제외">등급제외</option>
                </select>
            </div>
            <button type="submit">평가 등록</button>
        </form>

          <button @click.prevent="cancel">닫기</button>
      </div>
    </div>
</template>

<script>
import {adminCreateHospitalEstimation} from '@/api/admin';
export default {
    props: {
        estimations: {
            type:Array,
            required: true,
        },
        hospitalEstimationInfo: {
            type:Object,
            required: true,
        },
    },
    data() {
        return {
            estimationList: '',
            distinctionGrade:'',
            hospitalId:'',
        };
    },
    methods:{
        cancel(){
            this.$emit('estimationCancel');
        },
        async submitForm(){
            if(confirm(this.estimationList+' 평가를 등록하시겠습니까?')){
                const estimationData = {
                    hospitalId:this.hospitalId,
                    cityName:this.hospitalEstimationInfo.cityName,
                    hospitalName: this.hospitalEstimationInfo.hospitalName,
                    estimationList:this.estimationList,
                    distinctionGrade:this.distinctionGrade
                };

                await adminCreateHospitalEstimation(estimationData);
                this.initForm();
                this.$emit('estimationLoad');
            }
        },
        initForm(){
            this.estimationList='';
            this.distinctionGrade='';
        },
    },
    created(){
        this.hospitalId = this.$route.query.hospitalId;
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