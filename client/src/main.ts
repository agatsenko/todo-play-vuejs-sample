import App from "@/app";
import router from "@/router";
import "@/styles/main.scss";
import { library as faLibrary } from "@fortawesome/fontawesome-svg-core";
import { faCcVisa as fabCcVisa } from "@fortawesome/free-brands-svg-icons";
import { faCheckSquare as farCheckSquare } from "@fortawesome/free-regular-svg-icons";
import {
  faCheckSquare as fasCheckSquare,
  faCoffee as fasCoffee,
  faList as fasList,
  faSpinner as fasSpinner
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import ElementUI from "element-ui";
import locale from "element-ui/lib/locale/lang/en";
import "element-ui/lib/theme-chalk/index.css";
import Vue from "vue";



faLibrary.add(
  // FIXME: need to remove
  fasCheckSquare,
  farCheckSquare,
  fabCcVisa,
  fasSpinner,
  fasCoffee,
  fasList
);
Vue.component("fa-icon", FontAwesomeIcon);

Vue.use(/*Vuex,*/ ElementUI, { locale });

Vue.config.productionTip = false;

new Vue({
  router,
  render: h => h(App)
}).$mount("#app");
