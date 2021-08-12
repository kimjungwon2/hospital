<template>

  <div>
  <router-link to="/admin/create/tag">+</router-link>
  <form @submit.prevent="submitForm">
            <select name="searchCondition" v-model="searchCondition">
                  <option value="tagName">태그 이름</option>
            </select>
        <input id="tagName" type="text" v-model="tagName"/><button type="submit">검색하기</button>
	</form>

    <ul v-for="tag in tags.content" :key="tag.tagId" >
      <div>
        <li>태그 번호: {{ tag.tagId }} </li>
        <li>태그 이름: {{ tag.name}}</li>
        <li>등록 날짜: {{ tag.createdDate|formatDate }}</li>
        <div><font-awesome-icon icon="trash-alt" @click.prevent="deleteTag(tag.tagId)"/></div>
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
import { adminViewTagsList, adminSearchTags, adminDeleteTag} from '@/api/admin';

library.add(faTrashAlt)

export default {
   data() {
     return {
       //페이징 관련 데이터 
       pageNum: 0,
       totalPageNum:'',

       //태그 데이터
       tags: [],
       //검색 조건 default = memberIdName로 한다. 
       searchCondition:'tagName',
       //검색명
       tagName:'',
     }
   },
   methods:{
    //Q&A 삭제
    async deleteTag(tagId){
      if(confirm('정말로 태그를 삭제하시겠습니까?')){
            await adminDeleteTag(tagId);
            this.adminLoadTags();
      }
    },
    async adminLoadTags(){
        const {data} = await adminViewTagsList(this.pageNum);
        this.tags = data;
    },
    
    //검색
    async submitForm(){
        this.pageNum = 0;
        const {data} = await adminSearchTags(this.tagName);
        this.tags = data;
    },
    //이전 페이지
    async prevPage(){
        this.pageNum-=1;

        const {data} = await adminViewTagsList(this.pageNum);
        this.tags = data;
    },
    //다음 페이지
    async nextPage(){
        this.pageNum+=1;

        const {data} = await adminViewTagsList(this.pageNum);
        this.tags = data;
    }
   },

   async created(){

        const {data} = await adminViewTagsList(0);
        this.tags = data;
        this.totalPageNum = this.tags.totalPages;
    }
}
</script>

<style>

</style>