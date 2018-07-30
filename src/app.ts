import { Component, Vue } from "vue-property-decorator";
import { MenuItem } from "@/components/menu";

@Component({
  components: {
    "simple-todo-list-component": () => import("@/components/simple-todo/todo-list"),
    "eui-todo-view": () => import("@/components/elementui-todo/todo-view"),
    "samples-component": () => import("@/components/samples.vue"),
  },
})
export default class App extends Vue {
  mainMenu: MenuItem[] = [
    new MenuItem("1", "Simple TODO List", "simple-todo-list-component"),
    new MenuItem("2", "ElementUI TODO", "eui-todo-view"),
    new MenuItem("3", "Samples", "samples-component"),
  ];

  selectedMenuItem: MenuItem | null = this.defaultSelectedMenuItem;

  get defaultSelectedMenuItem(): MenuItem {
    return this.mainMenu[0];
  }

  selectMenu(index: string): void {
    const foundItem = this.mainMenu.find(item => item.id === index);
    this.selectedMenuItem = foundItem === undefined ? null : foundItem;
  }
}
