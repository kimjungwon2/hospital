<template>
  <section id="searchReview">
    <SearchReviewMenuItem></SearchReviewMenuItem>
        <SearchReviewItem
           v-for="contentItem in contentItems" 
           :key="contentItem.hospitalId"
           :contentItem="contentItem"
        ></SearchReviewItem>
        <infinite-loading @infinite="infiniteHandler" spinner="spiral">
         <div slot="no-more" style="color: rgb(102, 102, 102); font-size: 14px; padding: 10px 0px;">목록의 끝입니다 :)</div>
        </infinite-loading>
  </section>
</template>

<script>
import SearchReviewItem from '@/components/hospitals/SearchReviewItem.vue';
import SearchReviewMenuItem from '@/components/hospitals/SearchReviewMenuItem.vue';
import InfiniteLoading from 'vue-infinite-loading';
import {searchReview} from '@/api/index';

export default {
    components:{
        SearchReviewMenuItem,
        SearchReviewItem,
        InfiniteLoading,
    },
    data() {
        return {
            contentItems: [],
            page:0,
        };
    },
    methods:{
        infiniteHandler($state){
            searchReview(this.$route.params.searchName, this.page)
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

}
</script>

<style>

</style>