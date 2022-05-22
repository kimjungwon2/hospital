<template>
  <div>
    <form @submit.prevent="submitForm">
        <div>
            <label for="disease">병명</label>
            <input id="disease" type="text" required v-model="disease">
        </div>
        <div>
            <label for="content">리뷰 내용</label>
            <input id="content" type="text" required v-model="content">
        </div>
        <div>
            <label for="recommendationStatus">추천 유무</label>
            <select name="recommendationStatus" required v-model="recommendationStatus">
                  <option value="RECOMMENDATION">추천</option>
                  <option value="DECOMMENDATION">비추천</option>
            </select>
        </div>
        <div>
            <label for="sumPrice">가격</label>
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
        <div>
            <label for="kindness">친절함</label>
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
        <div>
            <label for="symptomRelief ">증상 완화</label>
            <select name="symptomRelief" required v-model="symptomRelief ">
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
        <div>
            <label for="cleanliness">청결</label>
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
        <div>
            <label for="waitTime">대기 시간</label>
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

        <div class="button">
                   <button type="submit">리뷰 등록</button>
        </div>
    </form>
  </div>
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
            reviewData.append("sumPrice",this.sumPrice);
            reviewData.append("kindness",this.kindness);
            reviewData.append("symptomRelief",this.symptomRelief);
            reviewData.append("cleanliness",this.cleanliness);
            reviewData.append("waitTime",this.waitTime);
            reviewData.append("imageFile",this.reviewReceipt);

            await registerUserReview(reviewData);
            this.$alert('리뷰 등록이 완료되었습니다.');
            this.$router.push('/hospital/view/'+this.hospitalId);
        },
    },
    created(){
        this.hospitalId = this.$route.params.hospitalId;
        this.memberId = this.$store.getters.getMemberId;
    },
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
    width: 300px;
    height: 300px;
    z-index: 10;
}

.image-preview-wrapper-upload {
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