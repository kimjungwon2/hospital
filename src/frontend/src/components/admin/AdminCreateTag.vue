<template>
      <form @submit.prevent="submitForm">
        <div>
            <label for="tagName">태그 이름:</label>
            <input id="tagName" type="text" v-model="tagName">
        </div>
        <button type="submit">태그 추가하기</button>
        <p>{{ logMessage }}</p>
    </form>
</template>

<script>
import {adminCreateTag} from '@/api/admin'

export default {
    data() {
        return {
            tagName:'',
            //log
            logMessage:'',
        };
    },
    methods:{
        async submitForm(){
            const userData = {
                tagName: this.tagName,
            }
            await adminCreateTag(userData);
            this.$alert("태그 추가 완료.")
            this.initForm();
            this.$router.push('/admin/tags');
        },
        initForm(){
            this.tagName = '';
        },
    }

}
</script>

<style>

</style>