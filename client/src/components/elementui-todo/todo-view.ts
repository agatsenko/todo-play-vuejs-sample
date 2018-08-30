import EuiTodoList from "@/components/elementui-todo/todo-list";
import { createTodoList, todoLists } from "@/components/elementui-todo/todo-model";
import { ITodoList } from "@/model/todo2";
import { ElInput } from "element-ui/types/input";
import { Component, Vue } from "vue-property-decorator";

@Component({
  components: {
    EuiTodoList,
  },
})
export default class EuiTodoView extends Vue {
  newListName: string | null = null;

  lists: ITodoList[] = todoLists;

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
      this.lists.push(createTodoList(this.newListName!));
      this.newListName = "";
    }
  }

  deleteList(list: ITodoList): void {
    const foundIndex = this.lists.findIndex(iterList => iterList === list);
    if (foundIndex > -1)  {
      this.lists.splice(foundIndex, 1);
    }
  }
}
