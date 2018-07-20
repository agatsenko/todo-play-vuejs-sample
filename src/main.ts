import Vue from "vue";

import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { library as faLibrary } from "@fortawesome/fontawesome-svg-core";
import {
  faCheckSquare as fasCheckSquare,
  faSpinner as fasSpinner,
  faCoffee as fasCoffee,
} from "@fortawesome/free-solid-svg-icons";
import {
  faCheckSquare as farCheckSquare,
} from "@fortawesome/free-regular-svg-icons";
import {
  faCcVisa as fabCcVisa,
} from "@fortawesome/free-brands-svg-icons";

import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

import App from "./app.vue";


faLibrary.add(
  fasCheckSquare,
  farCheckSquare,
  fabCcVisa,

  // FIXME: need to remove
  fasSpinner,
  fasCoffee,
);
Vue.component("fa-icon", FontAwesomeIcon);

Vue.use(ElementUI);

Vue.config.productionTip = false;

new Vue({
  render: (h) => h(App),
}).$mount("#app");
