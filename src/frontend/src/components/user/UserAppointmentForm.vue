<template>
  
</template>

<script>
import {viewUserAppointments} from '@/api/user';

export default {
    
    data() {
        return {
            contentItems:[],
        };
    },
    methods:{
        async userAppointments(){
            const id = this.$route.params.id;
            const { data } = await viewUserAppointments(id);
            this.contentItems = data;
            console.log(this.contentItems);
        },
    },
    created(){
        this.userAppointments();
    },
};
</script>

<style>

</style>