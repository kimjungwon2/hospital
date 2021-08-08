<template>
  <div>
      <ul v-for="contentItem in contentItems" :key="contentItem.bookmarkId">
        <li @click="routeViewHospital(contentItem)">
            {{ contentItem.hospitalName }} | 상태: {{contentItem.businessCondition}}
        </li>
        <li>
            진료 과목:{{contentItem.medicalSubjectInformation}}
        </li>
        <li>
            등록일: {{contentItem.createTime|formatDate}}
        </li>
       </ul>
  </div>
</template>

<script>
import {viewUserBookmarks} from '@/api/user';

export default {
    
    data() {
        return {
            contentItems:[],
            hospitalId:'',
        };
    },
    methods:{
        async userBookmarks(){
            const id = this.$route.params.id;
            const { data } = await viewUserBookmarks(id);
            this.contentItems = data;
        },
        routeViewHospital(contentItem){
            const id = contentItem.hospitalId;
            this.$router.push(`/hospital/view/${id}`);
        },
    },
    created(){
        this.userBookmarks();
    },
};
</script>

<style>

</style>