import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);

export default new VueRouter({
    mode: 'history',
    routes: [
       {
           path:'/',
           redirect:'/main',
       }, 
       {
           path: '/login',
           component: () => import('@/views/LoginPage.vue'),
       },
       {
           path: '/signup',
           component: () => import('@/views/SignupPage.vue'),
       },
       {
            path:'/main',
            component: () => import('@/views/MainPage.vue'),
       },
       {
           path: '*',
           component: () => import('@/views/NotFoundPage.vue'),
       }
   ],
});