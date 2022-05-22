<template>
<section id="searchHospital">
    <SearchHospitalMenuItem></SearchHospitalMenuItem>
    <SearchHospitalItem
            v-for="contentItem in contentItems" 
            :key="contentItem.hospitalId"
            :contentItem="contentItem"
    ></SearchHospitalItem>

    <infinite-loading @infinite="infiniteHandler" spinner="spiral">
        <p slot="no-more" style="color: rgb(102, 102, 102); font-size: 14px; padding: 10px 0px;">목록의 끝입니다 :)</p>
    </infinite-loading>
 </section>
</template>

<script>
import SearchHospitalItem from '@/components/hospitals/SearchHospitalItem.vue';
import SearchHospitalMenuItem from '@/components/hospitals/SearchHospitalMenuItem.vue';
import InfiniteLoading from 'vue-infinite-loading';
import {searchHospital} from '@/api/index';

export default {
    components:{
        SearchHospitalItem,
        SearchHospitalMenuItem,
        InfiniteLoading,
    },
    data() {
        return {
            contentItems: [],
            page:0,
        };
    },
    methods: {
        infiniteHandler($state){
            searchHospital(this.$route.params.searchName, this.page)
            .then(({ data }) => {
                 if(data.content.length){
                    this.page+=1;
                    this.contentItems.push(...data.content);
                    $state.loaded();
                }
            else $state.complete();
            }).catch(error=>{
                console.error(error);
            });
        },
    }
};
</script>

<style>

</style>

