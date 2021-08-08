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
        }
    },
    methods:{
        async submitForm(){
            const reviewData ={
                    hospitalId: this.hospitalId,
                    memberId:this.memberId,
                    disease:this.disease,
                    content:this.content,
                    recommendationStatus:this.recommendationStatus,
                    sumPrice:this.sumPrice,
                    kindness:this.kindness,
                    symptomRelief:this.symptomRelief,
                    cleanliness:this.cleanliness,
                    waitTime:this.waitTime,
            }

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

</style>