<template>
  <div>
    <router-link to="/admin/hospital/create">+</router-link>
    <form @submit.prevent="submitForm">
            <select name="searchCondition" v-model="searchCondition">
                  <option value="hospitalId">병원 번호</option>
                  <option value="hospitalName">병원 이름</option>
                  <option value="businessCondition" >영업 상태</option>
                  <option value="cityName">도시 이름</option>
            </select>
        <input id="keyword" type="text" v-model="keyword"/><button type="submit">검색하기</button>
	</form>

    <ul v-for="hospital in hospitals.content" :key="hospital.hospitalId" >
      <div>
        <li @click="routeAdminViewHospital(hospital.hospitalId,hospital.detailedHosId,hospital.staffHosInformationId,hospital.thumbnailId)">병원 번호: {{ hospital.hospitalId }} </li>
        <li v-if="hospital.detailedHosId!==null">상세 정보: {{ hospital.detailedHosId}}</li>
        <li v-else>상세 정보가 등록되지 않았습니다.</li>
        <li v-if="hospital.staffHosInformationId!==null">추가 정보: {{ hospital.staffHosInformationId }}</li>
        <li v-else>추가 정보가 등록되지 않았습니다.</li>
        <li>병원 이름: {{ hospital.hospitalName}}</li>
        <li>영업 상태: {{ hospital.businessCondition}}</li>
        <li>도시명: {{ hospital.cityName}}</li>
        <li>전화번호: {{ hospital.phoneNumber}}</li>
        <div><font-awesome-icon icon="trash-alt" @click.prevent="deleteHospital(hospital.hospitalId,hospital.staffHosInformationId)"/></div>
      </div>
    </ul>

    <div>
      <button :disabled="pageNum === 0" @click.prevent="prevPage">
        이전
      </button>
      <span>{{ pageNum + 1 }} / {{ totalPageNum }} 페이지</span>
      <button :disabled="pageNum >= totalPageNum - 1" @click.prevent="nextPage">
        다음
      </button>
    </div>
  </div>
</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { adminSearchHospitalLists, adminDeleteHospital} from '@/api/admin';
library.add(faTrashAlt)

export default {
   data() {
     return {
       //데이터  
       pageNum: 0,
       totalPageNum:'',

       //병원 데이터
       hospitals: [],
       //검색 조건 default = hospitalId로 한다. 
       searchCondition:'hospitalId',
       //검색명
       keyword:'',
     }
   },
   methods:{
    async adminLoadHospitals(){
        const condition ={};
        let key = this.searchCondition;
        let page = "page";
        condition[key] = this.keyword;
        condition[page] = this.pageNum;

        const {data} = await adminSearchHospitalLists(condition);
        this.hospitals = data;
    },
    routeAdminViewHospital(hospitalId, detailedHosInfoId, staffHosInfoId,thumbnailId){
      this.$router.push({name:'adminHospitalView',
      query: {hospitalId:hospitalId, detailedHosInfoId:detailedHosInfoId, staffHosInfoId:staffHosInfoId,thumbnailId:thumbnailId}
      })
    },
    //병원 삭제
    async deleteHospital(hospitalId, staffHosInformationId){
      if(confirm('정말로 병원을 삭제하시겠습니까?')){
            await adminDeleteHospital(hospitalId, staffHosInformationId);
            this.adminLoadHospitals();
      }
    },
    
    //검색
    async submitForm(){
        this.pageNum = 0;
        const obj ={};
        let key = this.searchCondition;
        obj[key] = this.keyword;

        const {data} = await adminSearchHospitalLists(obj);
        this.hospitals = data;
        this.totalPageNum = this.hospitals.totalPages;
    },
    //이전 페이지
    async prevPage(){
        this.pageNum-=1;
        
        const obj ={};
        let key = this.searchCondition;
        let page = "page";
        //검색
        obj[key] = this.keyword;
        //페이지 번호
        obj[page] = this.pageNum;

        const {data} = await adminSearchHospitalLists(obj);
        this.hospitals = data;
    },
    //다음 페이지
    async nextPage(){
        this.pageNum+=1;

        const obj ={};
        let key = this.searchCondition;
        let page = "page";
        obj[key] = this.keyword;
        obj[page] = this.pageNum;

        const {data} = await adminSearchHospitalLists(obj);
        this.hospitals = data;
    }
   },

   async created(){
       const condition ={
            page: 0,
       }
        const {data} = await adminSearchHospitalLists(condition);
        this.hospitals = data;
        this.totalPageNum = this.hospitals.totalPages;
    }
}
</script>

<style>

</style>