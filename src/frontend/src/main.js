import Vue from 'vue'
import App from './App.vue'
import router from '@/routes/index'
import store from '@/store/index';
import { formatDate } from '@/utils/filters';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'


Vue.component('font-awesome-icon', FontAwesomeIcon)
Vue.filter('formatDate', formatDate);
Vue.config.productionTip = false

new Vue({
  render: h => h(App),
  store,
  router,
}).$mount('#app')
