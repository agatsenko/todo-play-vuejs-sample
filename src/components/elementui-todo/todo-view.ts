import { Vue, Component } from "vue-property-decorator";
import ElementUITodoList from "@/components/elementui-todo/todo-list";
import { TodoList, defaultTodoItemFactory, TodoItem } from "@/model/todo";

@Component({
  components: {
    ElementUITodoList,
  },
})
export default class ElementUITodoView extends Vue {
  newListName: string | null = null;

  lists: TodoList[] = [
    new TodoList(
      "one",
      defaultTodoItemFactory,
      [
        new TodoItem("1", "First task"),
        new TodoItem("2", "Second task", true),
        new TodoItem("3", "Third task"),
      ],
    ),
  ];

  get canAddList(): boolean {
    return this.newListName !== null && this.newListName !== "";
  }

  addList(): void {
    if (this.canAddList) {
      this.lists.push(new TodoList(this.newListName!));
      this.newListName = "";
    }
  }
}
