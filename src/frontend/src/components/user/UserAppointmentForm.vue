<template>
<div>
    <ul v-for="contentItem in contentItems" :key="contentItem.appointmentId">
        <li @click="routeViewHospital(contentItem)">
            {{ contentItem.hospitalName}}
        </li>
    </ul>
</div>
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
        routeViewHospital(contentItem){
            const id = contentItem.hospitalId;
            this.$router.push(`/hospital/view/${id}`);
        },
    },
    created(){
        this.userAppointments();
    },
};
</script>

<style>

</style>