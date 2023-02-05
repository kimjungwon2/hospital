<template>
  <section id="bookmarkLists">
    <div class="bookmarkLists__title">
        <h1>즐겨찾기 목록</h1>
    </div>
      <div class = "bookmarkLists__hospital" @click="routeViewHospital (contentItem)" v-for="contentItem in contentItems" :key="contentItem.bookmarkId">
        <div class = "hospital__item" >
            <h3>{{ contentItem.hospitalName }} ({{contentItem.businessCondition}})</h3>
        </div>
        <div class = "hospital__item">
            <b>진료 과목</b><br>{{contentItem.medicalSubjectInformation}}
        </div>
        <div class = "hospital__item">
            <b>등록일</b> {{contentItem.createTime|formatDate}}
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
#bookmarkLists{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
}

.bookmarkLists__title{
    text-align:center;
}


.bookmarkLists__hospital{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
  margin-top:20px;
  border: 2px solid #0067a3;
  border-radius:10px;
}

.bookmarkLists__hospital .hospital__item{
  margin-top:10px;
}

</style>