import { Vue, Component } from "vue-property-decorator";
import EuiTodoList from "@/components/elementui-todo/todo-list";
import { TodoList, defaultTodoItemFactory, TodoItem } from "@/model/todo";
import { ElInput } from "element-ui/types/input";

const todoLists = [
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

@Component({
  components: {
    EuiTodoList,
  },
})
export default class EuiTodoView extends Vue {
  newListName: string | null = null;

  lists: TodoList[] = todoLists;

  get canAddList(): boolean {
    return this.newListName !== null && this.newListName !== "";
  }

  mounted(): void {
    this.$nextTick(() => {
      const input = this.$refs.newListInput as ElInput;
      input.focus();
    });
  }

  addList(): void {
    if (this.canAddList) {
      this.lists.push(new TodoList(this.newListName!));
      this.newListName = "";
    }
  }

  deleteList(list: TodoList): void {
    const foundIndex = this.lists.findIndex(iterList => iterList === list);
    if (foundIndex > -1)  {
      this.lists.splice(foundIndex, 1);
    }
  }
}
