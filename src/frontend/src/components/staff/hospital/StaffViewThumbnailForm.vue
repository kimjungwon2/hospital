<template>
  <section id="staffModifyThumbnail">
    <section class="staffModifyHospital__categories">
        <button class="hospital__category__btn" @click.prevent=viewHospital> 병원 정보 </button>
        <button class="hospital__category__btn" @click.prevent="viewStaffHospital"> 추가 병원 정보 </button>
        <button class="hospital__category__btn"> 병원 섬네일 </button>
        <button class="hospital__category__btn" @click.prevent="viewHospitalImage"> 병원 이미지</button>
    </section>

    <!-- 섬네일이 없는 경우 -->
    <section v-if="thumbnailId===null">
        <div class="image-notice">
                <ul class="image-wrapper">
                    <li>섬네일 이미지라 하나만 등록이 가능합니다.</li>
                    <li>너무 사이즈가 큰 사진은 지양해주세요.</li>
                    <li>이미지 파일만 등록이 가능합니다.</li>
                </ul>
            </div>

             <form @submit.prevent="submitForm">
                 <!-- 이미지 업로드 -->
                 <div v-if="hospitalImage==''" class="image-dropper">
                    이미지 파일을 올려주세요
                    <input @change ='onInputImage' ref="imageInput" type="file" />
                 </div>

                <!-- 이미지 업로드가 된 후, 미리보기 -->
                <div v-else class = "file-upload-wrapper">
                
                        <div class="image-preview-container">
                            <div class="image-preview-wrapper">
                                <!-- 이미지 닫기-->
                                <div class="image-delete-button" @click="imageDeleteButton">
                                               x
                                </div>
                                <!--이미지 미리보기-->
                                <img :src="uploadImage" />
                            </div>
                        </div>
                </div>

                <button class ="image-button" type="submit">등록</button>
            </form>
    </section>

    <!--등록된 섬네일 보기 -->
    <section v-else>
        <h3>등록된 섬네일</h3>
        <img :src="imageUrl"/><br>
        <button @click.prevent="deleteThumbnail(thumbnailId)">이미지 삭제</button>
    </section>

  </section>
</template>

<script>
import {staffCreateThumbnail} from '@/api/staff';
import {staffViewThumbnail, staffDeleteThumbnail} from '@/api/staff';
export default {
    data() {
        return {
            hospitalImage: '',
            thumnailId: '',
            uploadImage:'',
            imageKey:'',
            imageUrl:'http://d123wf46onsgyf.cloudfront.net/w140/',
            thumbnailId:'',
        }
    },
    methods:{
        //병원 정보 보기
        viewHospital(){
            this.$router.push(`/staff/view/hospital`); 
        },
        //병원 추가 정보 보기
        viewStaffHospital(){
            this.$router.push({name:'StaffViewHospital',
                query: {hospitalId:this.$route.query.hospitalId,
                    staffHosInfoId:this.$route.query.staffHosInfoId,
                    thumbnailId:this.$route.query.thumbnailId}
            }); 
        },
        //병원 이미지 보기
        viewHospitalImage(){
            this.$router.push({name:'staffHospitalImage',
                query: {hospitalId:this.$route.query.hospitalId,
                    staffHosInfoId:this.$route.query.staffHosInfoId,
                    thumbnailId:this.$route.query.thumbnailId}
            }); 
        },
        //이미지 등록 버튼
        onInputImage(event){
            this.hospitalImage = this.$refs.imageInput.files[0];

            //이미지 미리보기
            let input = event.target;
            let reader = new FileReader(); 
            reader.onload = (e) => { this.uploadImage = e.target.result; } 
            reader.readAsDataURL(input.files[0]);
        },

        //이미지 삭제 버튼
        imageDeleteButton(e) {
            this.hospitalImage = '';
        },

        //이미지 전송 버튼
        async submitForm(){
            const data = new FormData();
            data.append("imageFile",this.hospitalImage);
            data.append("hospitalId",this.$route.query.hospitalId);
            

            //Presigned 획득
            const URL = await staffCreateThumbnail(data);

            //페이지 이동
            this.$router.push(`/staff/view/hospital`);
            this.$router.go();
        },

        //섬네일 삭제 버튼
        async deleteThumbnail(thumbnailId){
            if(confirm('정말로 섬네일을 삭제하시겠습니까?')){
                await staffDeleteThumbnail(thumbnailId);
                //페이지 이동
                this.$router.push(`/staff/view/hospital`);
            }
        }

    },
    //초기 섬네일 이미지 불러오기
    async created(){
        this.thumbnailId = this.$route.query.thumbnailId;

        if(this.thumbnailId!==null){
            const {data} = await staffViewThumbnail(this.thumbnailId);
            this.imageKey = data.imageKey;
            this.imageUrl= this.imageUrl+this.imageKey;
            this.thumbnailId = data.thumbnailId;
        }
    }
}
</script>

<style>
#staffModifyThumbnail{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
}

#staffModifyThumbnail .staffModifyHospital__categories .hospital__category__btn.active,
#staffModifyThumbnail .staffModifyHospital__categories .hospital__category__btn:hover{
  background-color:#006ab0;
  color:white;
}

#staffModifyThumbnail .staffModifyHospital__categories .hospital__category__btn{
  border-top: 1px solid #dee2e6!important;
  border-bottom: 1px solid #dee2e6!important;
  margin:5px;
  width: 20%;
  font-size:14px;
  border-radius: 4px;
  padding: 8px 48px;
  size:35px;
  height:50px;
}

#staffModifyThumbnail .staffModifyHospital__categories{
  margin-left:12%;
  margin-top:1%;
  margin-bottom:1%;
  position: relative;
  text-align: left;
}

.image-dropper{
    border: 1px dashed green;
    height: 200px;
    background-color:darkseagreen;
    border-radius: 10px;
    margin-bottom: 20px;
    display:flex;
    position: relative;
    justify-content: center;
    align-items: center;
}

.image-dropper input{
    opacity:0;
    width:100%;
    height:100%;
    position:absolute;
    cursor:pointer;
}

.image-dropper:hover{
    background-color:seagreen;
    color:white;
    transition:0.5s;
}

#staffModifyThumbnail .image-button{
    width:100%;
    height:40px;
    cursor: pointer;
    background-color:#006ab0;
    color:white;
    border-radius: 20px;
}

.image-notice {
    margin: 20px;
    padding: 20px 40px;
    border: 1px solid #dddddd;
}

.file-upload-wrapper {
    margin: 20px;
    border: 1px solid #dddddd;
    background-color: #f4f4f4;
    min-height: 350px;
    font-size: 15px;
    color: #888888;
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    flex-direction: row;
}

.image-preview-container{
    display: flex;
}


.image-preview-wrapper {
    padding: 10px;
    position: relative;
}

.image-preview-wrapper>img {
    position: relative;
    width: 190px;
    height: 130px;
    z-index: 10;
}

.image-preview-wrapper-upload {
    margin: 10px;
    padding-top: 20px;
    background-color: #888888;
    width: 190px;
    height: 130px;
    display:flex;
    position: relative;
    justify-content: center;
    align-items: center;
}

.image-preview-wrapper-upload input {
    opacity:0;
    width:100%;
    height:100%;
    position:relative;
    cursor:pointer;
}

.image-delete-button {
    position: absolute;
    /* align-items: center; */
    line-height: 18px;
    z-index: 99;
    font-size: 18px;
    right: 5px;
    top: 10px;
    color: #fff;
    font-weight: bold;
    background-color: #666666;
    width: 20px;
    height: 20px;
    text-align: center;
    cursor: pointer;
}
</style>