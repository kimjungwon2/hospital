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
            path:'/main',
            component: () => import('@/views/MainPage.vue'),
       },
       {
           path: '/signup',
           component: () => import('@/views/SignupPage.vue'),
       },
       {
           path: '/user/activity',
           component:() => import('@/views/user/UserActivityPage.vue'),
           meta: { login: true},
       },
       {
          path:'/user/:userId/info',
          component:() => import('@/views/user/UserInformationPage.vue'),
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
           path:'/user/hospital/:hospitalId/register/review',
           component: ()=> import('@/views/user/UserRegisterReviewPage.vue'),
           meta: { login: true},
       },
       {
           path:'/user/hospital/:hospitalId/register/question',
           component: ()=> import('@/views/user/UserRegisterQuestionPage.vue'),
           meta: { login: true},
       },
       {
          path:'/staff',
          component: ()=> import('@/views/staff/StaffPage.vue'),
          meta: { staff: true},
       },
       {
          path:'/staff/reviews',
          component: ()=> import('@/views/staff/StaffViewReviewsPage.vue'),
          meta: { staff: true},
       },
       {
          path:'/staff/Q&A',
          component: ()=> import('@/views/staff/StaffManageQuestionPage.vue'),
          meta: { staff: true},
       },
       {
          path:'/staff/bookmark/users',
          component: ()=> import('@/views/staff/StaffViewReviewsPage.vue'),
          meta: { staff: true},
       },
       {
          path:'/staff/view/hospital',
          component: ()=> import('@/views/staff/hospital/StaffViewHospitalPage.vue'),
          meta: { staff: true},
       },
       {
          path:'/staff/register/:hospitalId/staffHosInfo',
          component: ()=> import('@/views/staff/hospital/StaffRegisterStaffHosInfoPage.vue'),
          meta: { staff: true},
       },
       {
          path:'/staff/staffHosInfo',
          component: ()=> import('@/views/staff/hospital/StaffViewStaffHosInfoPage.vue'),
          name:'StaffViewHospital',
          meta: { staff: true},
       },
       {
          path:'/staff/staffHospital/edit',
          component: ()=> import('@/views/staff/hospital/StaffModifyStaffHospitalInfoPage.vue'),
          name:'staffEditStaffHospital',
          meta: { staff: true},
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
            component: () => import('@/views/admin/hospital/ManageHospitalsPage.vue'),
            meta: { admin: true},
       },
       {
            path:'/admin/hospital/view',
            component:() => import('@/views/admin/hospital/ManageViewHospitalPage.vue'),
            name:'adminHospitalView',
            meta: {admin: true},
       },
       {
            path:'/admin/staffHospital/edit/:staffHosId',
            component:() => import('@/views/admin/hospital/ManageEditStaffHospitalInfoPage.vue'),
            meta: {admin: true},
       },
       {
            path:'/admin/hospital/create',
            component:() => import('@/views/admin/hospital/ManageCreateHospitalPage.vue'),
            meta: {admin: true},
       },
       {
            path:'/admin/QandAs',
            component: () => import('@/views/admin/ManageQandAsPage.vue'),
            meta: { admin: true},
       },
       {
            path:'/admin/reviews',
            component: () => import('@/views/admin/review/ManageReviewsPage.vue'),
            meta: { admin: true},
       },
       {
            path:'/admin/review/view/:reviewId',
            component:() => import('@/views/admin/review/ManageViewReviewPage.vue'),
            meta: {admin: true},
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
            meta: { admin: true},
       },
       {
            path:'/admin/create/tag',
            component:() => import('@/views/admin/ManageCreateTagPage.vue'),
            meta: {admin: true},
       },
       {
            path:'/search/view/:searchName',
            component: () => import('@/views/SearchHospitalPage.vue'),
       },
      {
            path:'/search/review/:searchName',
            component: () => import('@/views/SearchReviewPage.vue'),
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

router.beforeEach((to, from, next ) => {
     if(to.meta.login && !store.getters.isLogin && !store.getters.isStaff && !store.getters.isAdmin){
          alert('로그인이 필요합니다.');
          next('/login');
          return;
     }
     if(to.meta.staff && store.getters.isAdmin){
          alert('관리자는 관리자 페이지를 이용해주세요.');
          next('/main');
          return;
     }
     else if(to.meta.staff && !store.getters.isStaff && !store.getters.isAdmin){
          alert('병원 관계자가 아닙니다.');
          next('/main');
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