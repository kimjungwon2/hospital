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
            meta:{title:"메인페이지"},
       },
       {
           path: '/signup',
           component: () => import('@/views/SignupPage.vue'),
       },
       {
          path:'/user/:userId/info',
          component:() => import('@/views/user/UserInformationPage.vue'),
          meta: { login: true, title:"내 정보 수정하기"},
       },
       {
           path:'/user/:id/bookmarks',
           component: ()=> import('@/views/user/UserBookmarksPage.vue'),
           meta: { login: true, title:"나의 즐겨찾기 목록"},
       },
       {
           path:'/user/:id/reviews',
           component: ()=> import('@/views/user/UserReviewsPage.vue'),
           meta: { login: true, title:"나의 리뷰 목록"},
       },
       {
           path:'/user/:id/questions',
           component: ()=> import('@/views/user/UserQuestionsPage.vue'),
           meta: { login: true, title:"나의 질문 목록"},
       },
       {
           path:'/user/hospital/:hospitalId/register/review',
           component: ()=> import('@/views/user/UserRegisterReviewPage.vue'),
           meta: { login: true,title:"리뷰 작성"},
       },
       {
           path:'/user/hospital/:hospitalId/register/question',
           component: ()=> import('@/views/user/UserRegisterQuestionPage.vue'),
           meta: { login: true,title:"질문 작성"},
       },
       {
          path:'/staff/reviews',
          component: ()=> import('@/views/staff/StaffViewReviewsPage.vue'),
          meta: { staff: true,title:"병원에 등록된 리뷰 검색"},
       },
       {
            path:'/staff/review/view/:reviewId',
            component:() => import('@/views/staff/StaffViewReviewPage.vue'),
            meta: {staff: true,title:"병원 관계자 리뷰 상세 보기"},
       },
       {
          path:'/staff/Q&A',
          component: ()=> import('@/views/staff/StaffManageQuestionsPage.vue'),
          meta: { staff: true,title:"병원에 등록된 Q&A 검색"},
       },
       {
          path:'/staff/questions',
          component: ()=> import('@/views/staff/StaffManageNoAnswerQuestionsPage.vue'),
          meta: { staff: true,title:"질문에 답변하기"},
       },
       {
          path:'/staff/bookmark/users',
          component: ()=> import('@/views/staff/StaffViewBookmarkUsersPage.vue'),
          meta: { staff: true,title:"북마크 유저 검색"},
       },
       {
          path:'/staff/view/hospital',
          component: ()=> import('@/views/staff/hospital/StaffViewHospitalPage.vue'),
          meta: { staff: true,title:"자신의 병원 관리"},
       },
       {
            path:'/staff/thumbnail',
            component:() => import('@/views/staff/hospital/StaffViewThumbnailPage.vue'),
            name:'staffThumbnail',
            meta: {staff: true, title:"병원 섬네일 등록하기"},
       },
       {
            path:'/staff/hospitalImage',
            component:() => import('@/views/staff/hospital/StaffViewHospitalImagePage.vue'),
            name:'staffHospitalImage',
            meta: {staff: true, title:"병원 사진들 등록하기"},
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
          meta: { staff: true, title:"병원 추가 정보 등록하기"},
       },
       {
          path:'/staff/staffHospital/edit',
          component: ()=> import('@/views/staff/hospital/StaffModifyStaffHospitalInfoPage.vue'),
          name:'staffEditStaffHospital',
          meta: { staff: true, title:"병원 추가 정보 수정하기"},
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
            path:'/admin/reviews/unapproved',
            component: () => import('@/views/admin/review/ViewUnapprovedReviewPage.vue'),
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
            meta:{title:"병원 검색"},
       },
      {
            path:'/search/review/:searchName',
            component: () => import('@/views/SearchReviewPage.vue'),
            meta:{title:"리뷰 검색"},
       },
       {
            path:'/hospital/view/:id',
            component: () => import('@/views/ViewHospitalPage.vue'),
            meta:{title:"병원 정보"},
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

const makeTitle = (title) =>
  title ? `k-hospital - ${title}` : "k-hospital";

router.afterEach((to, from) => {
  Vue.nextTick(() => {
    document.title = makeTitle(to.meta.title);
  });
});

export default router;