<template>
  <section class="register-review">
    <form @submit.prevent="submitForm">
        <div class="register-review__write">
            <div class="review__write">
                <div class="write__item">
                    <label for="disease">병명</label>
                </div>
                <div class="write__content__top">
                    <input id="disease" type="text" required v-model="disease">
                </div>
            </div>
            
            <div class="review__write">
                <div class="write__item">
                    <label for="recommendationStatus">추천 유무</label>
                </div>
                <div class="write__content">
                    <select name="recommendationStatus" required v-model="recommendationStatus">
                          <option value="RECOMMENDATION">추천</option>
                          <option value="DECOMMENDATION">비추천</option>
                    </select>
                </div>
            </div>

            <div class="review__write">
                <div class="write__item">
                    <label for="sumPrice">가격</label>
                </div>
                <div class="write__content">
                    <select name="sumPrice" required v-model="sumPrice">
                          <option value=10>10점</option>
                          <option value=9>9점</option>
                          <option value=8>8점</option>
                          <option value=7>7점</option>
                          <option value=6>6점</option>
                          <option value=5>5점</option>
                          <option value=4>4점</option>
                          <option value=3>3점</option>
                          <option value=2>2점</option>
                          <option value=1>1점</option>
                    </select>
                </div>
            </div>
            <div class="review__write">
                <div class="write__item">
                    <label for="kindness">친절함</label>
                </div>
                <div class="write__content">
                    <select name="kindness" required v-model="kindness">
                          <option value=10>10점</option>
                          <option value=9>9점</option>
                          <option value=8>8점</option>
                          <option value=7>7점</option>
                          <option value=6>6점</option>
                          <option value=5>5점</option>
                          <option value=4>4점</option>
                          <option value=3>3점</option>
                          <option value=2>2점</option>
                          <option value=1>1점</option>
                    </select>
                </div>
            </div>
            <div class="review__write">
                <div class="write__item">
                    <label for="symptomRelief ">증상 완화</label>
                </div>
                <div class="write__content">
                    <select name="symptomRelief" v-model="symptomRelief">
                          <option value=10>10점</option>
                          <option value=9>9점</option>
                          <option value=8>8점</option>
                          <option value=7>7점</option>
                          <option value=6>6점</option>
                          <option value=5>5점</option>
                          <option value=4>4점</option>
                          <option value=3>3점</option>
                          <option value=2>2점</option>
                          <option value=1>1점</option>
                    </select>
                </div>
            </div>
            <div class="review__write">
                <div class="write__item">
                    <label for="cleanliness">청결</label>
                </div>
                <div class="write__content">
                    <select name="cleanliness" required v-model="cleanliness">
                          <option value=10>10점</option>
                          <option value=9>9점</option>
                          <option value=8>8점</option>
                          <option value=7>7점</option>
                          <option value=6>6점</option>
                          <option value=5>5점</option>
                          <option value=4>4점</option>
                          <option value=3>3점</option>
                          <option value=2>2점</option>
                          <option value=1>1점</option>
                    </select>
                </div>
            </div>

            <div class="review__write">
                <div class="write__item">
                    <label for="waitTime">대기 시간</label>
                </div>
            
                <div class="write__content">
                    <select name="waitTime" required v-model="waitTime">
                          <option value=10>10점</option>
                          <option value=9>9점</option>
                          <option value=8>8점</option>
                          <option value=7>7점</option>
                          <option value=6>6점</option>
                          <option value=5>5점</option>
                          <option value=4>4점</option>
                          <option value=3>3점</option>
                          <option value=2>2점</option>
                          <option value=1>1점</option>
                    </select>
                </div>
            </div>

            <div class="review__content">    
                <div class="content__title">
                    <label for="content">리뷰 내용</label>
                </div>
                <div class="write__content__content">
                    <textarea id="content" name ="content" required v-model="content"></textarea>
                </div>
            </div>

        </div>

      
            <!-- 이미지 업로드 -->
             <div v-if="reviewReceipt==''" class="image-dropper">
                병원 영수증 파일을 올려주세요
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

            <div class="write__button">
                <button id="review__button" type="submit">리뷰 등록</button>
            </div>
    </form>
  </section>
</template>

<script>
import {registerUserReview} from '@/api/user';

export default {
    data() {
        return {
            hospitalId: '',
            memberId:'',
            disease:'',
            content:'',
            recommendationStatus:'RECOMMENDATION',
            sumPrice:1,
            kindness:1,
            symptomRelief:1,
            cleanliness:1,
            waitTime:1,

            reviewReceipt:'',
            uploadImage:'',
        }
    },
    methods:{
        //이미지 등록 버튼
        onInputImage(event){
            this.reviewReceipt = this.$refs.imageInput.files[0];

            //이미지 미리보기
            let input = event.target;
            let reader = new FileReader(); 
            reader.onload = (e) => { this.uploadImage = e.target.result; } 
            reader.readAsDataURL(input.files[0]);
        },

        //이미지 삭제 버튼
        imageDeleteButton(e) {
            this.reviewReceipt = '';
        },

        async submitForm(){
            const reviewData = new FormData();
            reviewData.append("hospitalId",this.hospitalId);
            reviewData.append("memberId",this.memberId);
            reviewData.append("disease",this.disease);
            reviewData.append("content",this.content);
            reviewData.append("recommendationStatus",this.recommendationStatus);
            reviewData.append("kindness",this.kindness);
            reviewData.append("symptomRelief",this.symptomRelief);
            reviewData.append("sumPrice",this.sumPrice);
            reviewData.append("cleanliness",this.cleanliness);
            reviewData.append("waitTime",this.waitTime);
            reviewData.append("imageFile",this.reviewReceipt);

            try{
                await registerUserReview(reviewData);
                this.$router.push('/hospital/view/'+this.hospitalId);
                this.$alert('리뷰 등록이 완료되었습니다.');
            }
            catch(error){
                alert(error.response.data.message);
            }finally{
                this.initForm();
            }
        },
        initForm(){
            this.hospitalId= '';
            this.memberId='';
            this.disease='';
            this.content='';
            this.recommendationStatus='RECOMMENDATION';
            this.sumPrice=1;
            this.kindness=1;
            this.symptomRelief=1;
            this.cleanliness=1;
            this.waitTime=1;
            this.reviewReceipt='';
            this.uploadImage='';
        }
    },
    created(){
        this.hospitalId = this.$route.params.hospitalId;
        this.memberId = this.$store.getters.getMemberId;
    },
}
</script>

<style>
.register-review{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
  margin-top:20px;
}

.review__write{
    display:flex;
    width:100%;
}

.register-review__write{
    margin-bottom:20px;
}

.register-review__write .write__item{
    width:20%;
    padding:10px;
    background:#005b96;
    color:white;
    text-align:center;
    border-bottom: 1px solid #e2e2e2;
    border-right: 1px solid #e2e2e2;
}

.register-review__write .write__item__content{
    width:20%;
    text-align:center;
    border-bottom: 1px solid #e2e2e2;
    border-right: 1px solid #e2e2e2;
    vertical-align: middle;
}

.write__content__top{
    margin-left:10px;
    width:80%;
    border-bottom: 1px solid #e2e2e2;
    border-left: 1px solid #e2e2e2;
    padding:10px;
    margin-top:1px solid #e2e2e2;
}

.register-review__write .write__content{
    margin-left:10px;
    width:80%;
    border-bottom: 1px solid #e2e2e2;
    border-left: 1px solid #e2e2e2;
    padding:10px;
}

.register-review__write .write__content__content #content{
    width:100%;
    height:200px;
    border-radius: 10px;
}

.register-review__write .review__content{
    margin-top:20px;
}

.review__content .content__title{
    position:relative;
    left:8px;
    margin-bottom:5px;
    font-weight:bold;
}

.register-review .image-dropper{
    border: 1px dashed #004978;
    height: 150px;
    background-color:#0072bc;
    color:white;
    border-radius: 10px;
    margin-bottom: 20px;
    display:flex;
    position: relative;
    justify-content: center;
    align-items: center;
}

.register-review .image-dropper input{
    opacity:0;
    width:100%;
    height:100%;
    position:absolute;
    cursor:pointer;
}

.register-review .image-dropper:hover{
    background-color:#004978;
    color:white;
    transition:0.5s;
}

.register-review .image-button{
    width:100%;
    height:40px;
    cursor: pointer;
}

.register-review .image-notice {
    margin: 20px;
    padding: 20px 40px;
    border: 1px solid #dddddd;
}

.register-review .file-upload-wrapper {
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

.register-review .image-preview-container{
    display: flex;
}


.register-review .image-preview-wrapper {
    padding: 10px;
    position: relative;
}

.register-review .image-preview-wrapper>img {
    position: relative;
    width: 300px;
    height: 300px;
    z-index: 10;
}

.register-review .image-preview-wrapper-upload {
    margin: 10px;
    padding-top: 20px;
    background-color: #888888;
    width: 300px;
    height: 300px;
    display:flex;
    position: relative;
    justify-content: center;
    align-items: center;
}

.register-review .image-preview-wrapper-upload input {
    opacity:0;
    width:100%;
    height:100%;
    position:relative;
    cursor:pointer;
}

.register-review .image-delete-button {
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

.write__button{
    text-align:right;
}

.write__button #review__button{
    background-color:#004978; 
    color: white; 
    border-radius:10px;
    height:30px;
}

</style>