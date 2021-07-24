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
            path: '/user/info',
            component:() => import('@/views/user/UserInfoPage.vue'),
       },
       {
           path: '/user/activity',
           component:() => import('@/views/user/UserActivityPage.vue'),
       },
       {
           path:'/user/:id/bookmarks',
           component: ()=> import('@/views/user/UserBookmarksPage.vue'),
       },
       {
           path:'/user/:id/reviews',
           component: ()=> import('@/views/user/UserReviewsPage.vue'),
       },
       {
           path:'/user/:id/questions',
           component: ()=> import('@/views/user/UserQuestionsPage.vue'),
       },
       {
           path:'/user/:id/appointments',
           component: ()=> import('@/views/user/UserAppointmentsPage.vue'),
       },
       {
            path:'/main',
            component: () => import('@/views/MainPage.vue'),
       },
       {
            path:'/admin',
            component: () => import('@/views/admin/AdminPage.vue'),
       },
       {
            path:'/admin/appointments',
            component: () => import('@/views/admin/ManageAppointmentsPage.vue'),
       },
       {
            path:'/admin/hospitals',
            component: () => import('@/views/admin/ManageHospitalsPage.vue'),
       },
       {
            path:'/admin/QandAs',
            component: () => import('@/views/admin/ManageQandAsPage.vue'),
       },
       {
            path:'/admin/reviews',
            component: () => import('@/views/admin/ManageReviewsPage.vue'),
       },
       {
            path:'/admin/users',
            component: () => import('@/views/admin/ManageUsersPage.vue'),
       },
       {
            path:'/admin/tags',
            component: () => import('@/views/admin/ManageTagsPage.vue'),
       },
       {
            path:'/search/view/:searchName',
            component: () => import('@/views/SearchHospitalPage.vue'),
       },
       {
            path:'/hospital/view/:id',
            component: () => import('@/views/ViewHospitalPage.vue'),
       },
       {
           path: '*',
           component: () => import('@/views/NotFoundPage.vue'),
       }
   ],
});