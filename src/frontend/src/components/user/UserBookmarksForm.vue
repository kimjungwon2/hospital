<template>
  <section id="bookmarkLists">
      <div class = "bookmarkLists__hospital" @click="routeViewHospital (contentItem)" v-for="contentItem in contentItems" :key="contentItem.bookmarkId">
        <div class = "hospital__item" >
            {{ contentItem.hospitalName }}({{contentItem.businessCondition}})
        </div>
        <div class = "hospital__item">
            진료 과목:{{contentItem.medicalSubjectInformation}}
        </div>
        <div class = "hospital__item">
            등록일: {{contentItem.createTime|formatDate}}
        </div>
      </div>
  </section>
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
.bookmarkLists__hospital{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
  margin-top:10px;
  border-top: 1px solid #dee2e6!important;
  border-bottom: 1px solid #dee2e6!important;
}

.bookmarkLists__hospital .hospital__item{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
  margin-top:10px;
}

</style>