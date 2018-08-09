import { Component, Vue } from "vue-property-decorator";
import { MenuItem } from "@/components/menu";

@Component
export default class App extends Vue {
  mainMenu: MenuItem[] = [
    new MenuItem("1", "Simple TODO List", "/simple-todo.html"),
    new MenuItem("2", "ElementUI TODO", "/element-ui-todo.html"),
    new MenuItem("3", "Samples", "/samples.html"),
    new MenuItem("4", "Invalid Link", "/invlaid-link"),
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
