<template>
  <div>
     <h1>이미지 섬네일 업로드</h1>

    <!-- 섬네일이 없는 경우 -->
    <div v-if="thumbnailId===null">
        <div class="image-notice">
                <ul class="image-wrapper">
                    <li>
                        너무 사이즈가 큰 사진은 지양해주세요. (가로 사이즈 최소 800px)
                    </li>
                    <li>사진 용량은 사진 한 장당 10MB 까지 등록이 가능합니다.</li>
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
    </div>

    <!--등록된 섬네일 보기 -->
    <div v-else>
        <h3>등록된 섬네일</h3>
        <img :src="imageUrl"/>
        <button @click.prevent="deleteThumbnail(thumbnailId)">이미지 삭제</button>
    </div>
  </div>
</template>

<script>
import {adminViewThumbnail, adminCreateThumbnail, adminDeleteThumbnail} from '@/api/admin';
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
            
            const URL = await adminCreateThumbnail(data);

            //페이지 이동
            this.$router.push('/admin/hospitals');
        },

        //섬네일 삭제 버튼
        async deleteThumbnail(thumbnailId){
            if(confirm('정말로 섬네일을 삭제하시겠습니까?')){
                await adminDeleteThumbnail(thumbnailId);
                //페이지 이동
                this.$router.push('/admin/hospitals');
            }
        }

    },
    //초기 섬네일 이미지 불러오기
    async created(){
        this.thumbnailId = this.$route.query.thumbnailId;

        if(this.thumbnailId!==null){
            const {data} = await adminViewThumbnail(this.thumbnailId);
            this.imageKey = data.imageKey;
            this.imageUrl= this.imageUrl+this.imageKey;
            this.thumbnailId = data.thumbnailId;
        }
    }
}
</script>

<style>
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

.image-button{
    width:100%;
    height:40px;
    cursor: pointer;
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