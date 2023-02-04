<template>
<section id="staffModifyHospitalImages">
   <section class="staffModifyHospital__categories">
    <button class="hospital__category__btn" @click.prevent=viewHospital> 병원 정보 </button>
    <button class="hospital__category__btn" @click.prevent="viewStaffHospital"> 추가 병원 정보 </button>
    <button class="hospital__category__btn" @click.prevent="viewThumbnail"> 병원 섬네일 </button>
    <button class="hospital__category__btn" > 병원 이미지</button>
  </section>


  <section id="imageUpload">
      <div v-if="uploadedImages.length!==0" class="imageUpload__images" >
          <hooper class="images_array">
              <slide class ="slide" v-for="image in uploadedImages" :key="image.hospitalImageId">
                <span class="slide__delete-button" @click="deleteImageHospital(image.hospitalImageId)">
                    <img src='@/assets/delete.png' alt="delete" class="delete__button">
                </span>
                <img alt="hospitalImage" class="image__hospital" 
                            :src='`http://d123wf46onsgyf.cloudfront.net/w600/${image.imageKey}`'/>
              </slide>
              <hooper-navigation slot="hooper-addons"></hooper-navigation>
          </hooper>
      </div>

     
     <div v-else class="imageUpload__image-notice">
        <ul class="imageUpload__image-wrapper">
                <li>실내 병원 이미지를 여러 개 등록할 수 있습니다.</li>
                <li>너무 사이즈가 큰 사진은 지양해주세요.</li>
                <li>이미지 파일만 등록이 가능합니다.</li>
        </ul>
    </div>

     <form @submit.prevent="submitForm" enctype ="multipart/form-data">
         <!-- 이미지 업로드 -->
         <div v-if="!hospitalImages.length" class="imageUpload__image-dropper">
            이미지 파일을 올려주세요
            <input multiple @change ='onInputImage' ref="imageInput" type="file" />
         </div>

        <!-- 이미지 업로드가 된 후, 미리보기 -->
        <div v-else class = "imageUpload__file-upload-wrapper">

                <div class="imageUpload__image-preview-container">
                    <div v-for="(file, index) in hospitalImages" :key="index" class="image-preview-wrapper">
                        <!-- 이미지 닫기-->
                        <div class="imageUpload__image-delete-button" @click="imageDeleteButton" :name="file.number">
                                       x
                        </div>
                        <!--이미지 미리보기-->
                        <img :src="file.preview" />
                    </div>

                    <div class="imageUpload__image-preview-wrapper-upload">
                        <input @change ='imageAddUpload' ref="imageInput" type="file" />
                    </div>
                </div>
        </div>
        <button class ="imageUpload__image-button" type="submit">등록</button>
    </form>
  </section>
</section>
</template>

<script>
import {staffCreateHospitalImage,staffViewHospitalImages,staffDeleteHospitalImage} from '@/api/staff';
import {Hooper,Slide,Navigation as HooperNavigation} from 'hooper';
import 'hooper/dist/hooper.css';
export default {
    components:{
        Hooper,
        Slide,
        HooperNavigation,
    },
    data() {
        return {
            //병원 데이터 받아오기
            uploadedImages:[],

            hospitalImages: [],
            imageIndex: 0,
            hospitalId:'',
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

        //병원 섬네일 보기
        viewThumbnail(){
            this.$router.push({name:'staffThumbnail',
                query: {hospitalId:this.$route.query.hospitalId,
                  staffHosInfoId:this.$route.query.staffHosInfoId,
                  thumbnailId:this.$route.query.thumbnailId}
            }); 
        },

        //이미지 등록 버튼
        onInputImage(){
            this.images = this.$refs.imageInput.files;

            let num = -1;
            for(let i = 0; i<this.$refs.imageInput.files.length; i++){
                this.hospitalImages = [
                    ...this.hospitalImages,
                    {
                        file: this.$refs.imageInput.files[i],
                        //이미지 미리보기
                        preview: URL.createObjectURL(this.$refs.imageInput.files[i]),
                        //관리 인덱스 값
                        number: i
                    }
                ];
                num = i;
            }
            this.imageIndex = num +1;// 이미지 index 마지막 값 +1 저장.
        },

        //이미지 추가 버튼
        imageAddUpload(){
            let num = -1;
            for (let i = 0; i < this.$refs.imageInput.files.length; i++) {
                this.hospitalImages = [
                    ...this.hospitalImages,
                    //이미지 업로드
                    {
                        //실제 파일
                        file: this.$refs.imageInput.files[i],
                        //이미지 프리뷰
                        preview: URL.createObjectURL(this.$refs.imageInput.files[i]),
                        //삭제및 관리를 위한 number
                        number: i + this.imageIndex
                    }
                ];
                num = i;
            }
            this.imageIndex = this.imageIndex + num + 1;
        },

        //이미지 삭제 버튼
        imageDeleteButton(e) {
            const name = e.target.getAttribute('name');
            this.hospitalImages = this.hospitalImages.filter(data => data.number !== Number(name));
        },

        //이미지 등록 버튼
        async submitForm(){
            const data = new FormData();

            for(let i=0; i<this.hospitalImages.length;i++){
                data.append("imageFiles",this.hospitalImages[i].file);
            }
            data.append("hospitalId",this.$route.query.hospitalId);
            await staffCreateHospitalImage(data);
            this.$router.push(`/staff/view/hospital`);
            this.$router.go();
        },

        //병원 이미지 삭제 버튼
        async deleteImageHospital(hospitalImageId){
            if(confirm('정말로 병원 이미지를 삭제하시겠습니까?')){
                await staffDeleteHospitalImage(hospitalImageId);
                this.loadInofo();
            }
        },
        //데이터 로딩
        async loadInofo(){
            this.hospitalImages=[];
            this.imageIndex=0;

            this.hospitalId = this.$route.query.hospitalId;
            if(this.hospitalId!==null){
                const {data} = await staffViewHospitalImages(this.hospitalId);
                this.uploadedImages = data;
            }
        }

    },
    //초기 병원 이미지들 불러오기
    async created(){
        this.hospitalId = this.$route.query.hospitalId;

        if(this.hospitalId!==null){
            const {data} = await staffViewHospitalImages(this.hospitalId);
            this.uploadedImages = data;
        }
    }
}
</script>

<style>
#staffModifyHospitalImages{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
}

#staffModifyHospitalImages .staffModifyHospital__categories .hospital__category__btn.active,
#staffModifyHospitalImages .staffModifyHospital__categories .hospital__category__btn:hover{
  background-color:#006ab0;
  color:white;
}

#staffModifyHospitalImages .staffModifyHospital__categories .hospital__category__btn{
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

#staffModifyHospitalImages .staffModifyHospital__categories{
  margin-left:12%;
  margin-top:1%;
  margin-bottom:1%;
  position: relative;
  text-align: left;
}

#staffModifyHospitalImages .imageUpload__image-button{
    width:100%;
    height:40px;
    cursor: pointer;
    background-color:#006ab0;
    color:white;
    border-radius: 20px;
}

.image__hospital{
    width:600px;
    height:200px;
}

.imageUpload__images{
    margin-bottom:20px;
}

.images_array{
    text-align:center;
}

.slide__delete-button{
    position:relative;
    bottom:188px;
    left:598px;
    cursor: pointer;
}

.imageUpload__image-dropper{
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

.imageUpload__image-dropper input{
    opacity:0;
    width:100%;
    height:100%;
    position:absolute;
    cursor:pointer;
}

.imageUpload__image-dropper:hover{
    background-color:seagreen;
    color:white;
    transition:0.5s;
}

.imageUpload__image-button{
    width:100%;
    height:40px;
    cursor: pointer;
}

.imageUpload__image-notice {
    margin: 20px;
    padding: 20px 40px;
    border: 1px solid #dddddd;
}

.imageUpload__file-upload-wrapper {
    margin: 20px;
    border: 1px solid #dddddd;
    background-color: #f4f4f4;
    min-height: 350px;
    font-size: 15px;
    color: #888888;
    display: flex;
    align-items: left;
    justify-content: left;
    height: 100%;
}

.imageUpload__image-preview-content-container{
    height: 100%;
}

.imageUpload__image-preview-container{
    height: 100%;
    display: flex;
    flex-wrap: wrap;
}

.imageUpload__image-preview-wrapper {
    padding: 10px;
    position: relative;
}

.imageUpload__image-preview-wrapper>img {
    position: relative;
    width: 190px;
    height: 130px;
    z-index: 10;
}

.imageUpload__image-preview-wrapper-upload {
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

.imageUpload__image-preview-wrapper-upload input {
    opacity:0;
    width:100%;
    height:100%;
    position:relative;
    cursor:pointer;
}

.imageUpload__image-delete-button {
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
