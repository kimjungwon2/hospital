<template>
 <div>
    <h1> 병원 검색 결과 페이지</h1>
     <div v-if="this.contentItems===[]">요청하신 검색 결과가 없습니다. 리뷰 검색을 이용해 주세요.</div> 
     <ul v-else>
         <SearchHospitalItem
            v-for="contentItem in contentItems" 
            :key="contentItem.hospitalId"
            :contentItem="contentItem"
         ></SearchHospitalItem>
     </ul>
 </div>
</template>

<script>
import { searchHospital } from '@/api/index';
import SearchHospitalItem from '@/components/hospitals/SearchHospitalItem.vue';

export default {
    components:{
        SearchHospitalItem,
    },
    data() {
        return {
            contentItems: [],
        };
    },
    methods: {
        async searchHospital(){
            const { data } = await searchHospital(this.$store.getters.getSearchName);
            this.contentItems = data.content;
            console.log(data.content);
        },
    },
    created(){
        this.searchHospital();
    },
};
</script>

<style>

</style>

