<template>
    <div class="tag__modal">
      <div class="white__background">

            <div v-for="estimation in estimations" :key="estimation.estimationId"> 
            <b>평가 내역:</b>{{estimation.estimationList}} 
                <form @submit.prevent="submitForm(estimation.estimationId,estimation.estimationList,
                estimation.distinctionGrade)">   
                <div>    
                    <label for="distinctionGrade">평가등급</label>
                    <select name="distinctionGrade" required v-model="estimation.distinctionGrade">
                          <option value="">평가등급</option>
                          <option value="1등급">1등급</option>
                          <option value="2등급">2등급</option>
                          <option value="3등급">3등급</option>
                          <option value="4등급">4등급</option>
                          <option value="5등급">5등급</option>
                          <option value="등급제외">등급제외</option>
                    </select>
                </div>
                    <button type="submit">평가 수정</button>
                </form>
            </div> 

          <button @click.prevent="cancel">닫기</button>
      </div>
    </div>
</template>

<script>
import {adminModifyHospitalEstimation} from '@/api/admin';
export default {
    props: {
        estimations: {
            type:Array,
            required: true,
        },
    },
    methods:{
        cancel(){
            this.$emit('modifyEstimationCancel');
        },
        async submitForm(estimationId, estimationList, distinctionGrade){
            if(confirm(estimationList+' 평가를 수정하시겠습니까?')){
                const estimationData = {
                    estimationList:estimationList,
                    distinctionGrade:distinctionGrade
                };
                await adminModifyHospitalEstimation(estimationId,estimationData);
                this.$emit('estimationLoad');
                this.$alert('수정 완료');
            }
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