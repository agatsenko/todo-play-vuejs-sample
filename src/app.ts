import { Component, Vue } from "vue-property-decorator";
import SimpleTodoListComponent from "@/components/simple-todo/todo-list";
import SamplesComponent from "@/components/samples.vue";
import EuiTodoView from "@/components/elementui-todo/todo-view";

import { MenuItem } from "@/components/menu";

@Component({
  components: {
    SimpleTodoListComponent,
    EuiTodoView,
    SamplesComponent,
  },
})
export default class App extends Vue {
  mainMenu: MenuItem[] = [
    new MenuItem("1", "Simple TODO List", "SimpleTodoListComponent"),
    new MenuItem("2", "ElementUI TODO", "EuiTodoView"),
    new MenuItem("3", "Samples", "SamplesComponent"),
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
