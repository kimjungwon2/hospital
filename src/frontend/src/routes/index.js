import Vue from 'vue';
import VueRouter from 'vue-router';
import store from '@/store/index';

Vue.use(VueRouter);

const router =  new VueRouter({
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
            meta: { login: true},
       },
       {
           path: '/user/activity',
           component:() => import('@/views/user/UserActivityPage.vue'),
           meta: { login: true},
       },
       {
           path:'/user/:id/bookmarks',
           component: ()=> import('@/views/user/UserBookmarksPage.vue'),
           meta: { login: true},
       },
       {
           path:'/user/:id/reviews',
           component: ()=> import('@/views/user/UserReviewsPage.vue'),
           meta: { login: true},
       },
       {
           path:'/user/:id/questions',
           component: ()=> import('@/views/user/UserQuestionsPage.vue'),
           meta: { login: true},
       },
       {
           path:'/user/:id/appointments',
           component: ()=> import('@/views/user/UserAppointmentsPage.vue'),
           meta: { login: true},
       },
       {
            path:'/main',
            component: () => import('@/views/MainPage.vue'),
       },
       {
            path:'/admin',
            component: () => import('@/views/admin/AdminPage.vue'),
            meta: { admin: true},
       },
       {
            path:'/admin/appointments',
            component: () => import('@/views/admin/ManageAppointmentsPage.vue'),
            meta: { admin: true},
       },
       {
            path:'/admin/hospitals',
            component: () => import('@/views/admin/ManageHospitalsPage.vue'),
            meta: { admin: true},
       },
       {
            path:'/admin/QandAs',
            component: () => import('@/views/admin/ManageQandAsPage.vue'),
            meta: { admin: true},
       },
       {
            path:'/admin/reviews',
            component: () => import('@/views/admin/ManageReviewsPage.vue'),
            meta: { admin: true},
       },
       {
            path:'/admin/users',
            component: () => import('@/views/admin/user/ManageUsersPage.vue'),
            meta: { admin: true},
       },
       {
            path:'/admin/user/view/:userId',
            component:() => import('@/views/admin/user/ManageViewUserPage.vue'),
            meta: {admin: true},
       },
       {
            path:'/admin/signup',
            component:() => import('@/views/admin/user/ManageAddUserPage.vue'),
            meta: {admin: true},
       },
       {
            path:'/admin/tags',
            component: () => import('@/views/admin/ManageTagsPage.vue'),
            meta: { ladminogin: true},
       },
       {
            path:'/search/view/:searchName',
            component: () => import('@/views/SearchHospitalPage.vue'),
            meta: { admin: true},
       },
       {
            path:'/hospital/view/:id',
            component: () => import('@/views/ViewHospitalPage.vue'),
            meta: { admin: true},
       },
       {
           path: '*',
           component: () => import('@/views/NotFoundPage.vue'),
       }
   ],
});

router.beforeEach((to, from, next ) => {
     if(to.meta.login && !store.getters.isLogin){
          alert('로그인이 필요합니다.');
          next('/login');
          return;
     }
     if(to.meta.admin && !store.getters.isAdmin){
          alert('관리자 계정이 아닙니다.');
          next('/main');
          return;
     }
     next();
}); 

export default router;