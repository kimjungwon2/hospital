<template>
<div>
    <h1> 병원 검색 결과 페이지</h1>
     <ul>
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
            const searchName = this.$route.params.searchName;
            const { data } = await searchHospital(searchName);
            this.contentItems = data.content;
        },
    },
    created(){
        this.searchHospital();
    },
};
</script>

<style>

</style>

