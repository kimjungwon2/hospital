<template>
  <div class="tag__modal">
      <div class="white__background">
        <p>병원에 등록된 태그<br>
        <span v-for="(hospitalTag,i) in hospitalTags" :key="hospitalTag.postTagId"> #<b>{{hospitalTag.tagName}}</b>
        <br v-if="i!==0 && i%5===0"/>
        </span> </p>
        <h4> 태그 생성 모달창</h4>
            <form @submit.prevent="submitForm">
                <select name="searchCondition" v-model="searchCondition">
                     <option value="tagName">태그 이름</option>
                </select>
                <input id="tagName" required type="text" v-model="tagName"/><button type="submit">검색하기</button>
	        </form>

            <ul v-for="tag in tags.content" :key="tag.tagId" >
              <div>
                <li>태그 번호: {{ tag.tagId }} </li>
                <li>태그 이름: {{ tag.name}}</li>
                <li>태그 등록 <font-awesome-icon icon="plus-square" @click.prevent="registerTag(tag.tagId)"/></li>
              </div>
            </ul>
            <div v-if="tags.totalElements===0 && isSearch===true">
                검색한 태그가 없습니다.<br> '{{tagName}}' 태그를 추가하시겠습니까? <font-awesome-icon icon="plus-square" @click.prevent="addTag(tagName)"/>
            </div>
          <button @click.prevent="cancel">닫기</button>
      </div>
      
  </div>
</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core';
import { faPlusSquare } from '@fortawesome/free-solid-svg-icons';
import { staffSearchTags, staffLinkHospitalTag, staffCreateTag} from '@/api/staff';

library.add(faPlusSquare);

export default {
    props: {
        hospitalTags: {
            type:Array,
            required: true,
        },
        hospitalId: {
            type: Number,
            required: true,
        },
    },
    data() {
        return {
            //태그 데이터
            tags: [],
             //검색 조건 default = memberIdName로 한다. 
            searchCondition:'tagName',
             //검색명
            tagName:'',
            isSearch:false,
        }
    },
    methods:{
        cancel(){
            this.$emit('tagCancel');
        },

        async submitForm(){
            this.isSearch = true;
            const {data} = await staffSearchTags(this.tagName);
            this.tags = data;
        },
        async registerTag(tagId){
            if(confirm(this.tagName+' 태그를 병원에 등록하시겠습니까?')){
                const data={
                    memberId:this.$store.getters.getMemberId,
                    tagId:tagId,
                    hospitalId:this.hospitalId
                };

                await staffLinkHospitalTag(data);
                this.$emit('tagLoad');
            }
        },
        async addTag(tagName){
            if(confirm(tagName+' 태그를 새로 추가하시겠습니까?')){
                const tagData={
                    tagName:tagName
                };
                await staffCreateTag(tagData);
                this.$alert(tagName+ '태그가 추가되었습니다.');
                this.submitForm();
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