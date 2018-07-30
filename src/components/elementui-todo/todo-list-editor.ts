import { Vue, Component, Prop } from "vue-property-decorator";
import { TodoList } from "@/model/todo";
import { ElInput } from "element-ui/types/input";

@Component
export default class EuiTodoListEditDialog extends Vue {
  private todoList: TodoList | null = null;
  private dialogVisible: boolean = false;
  private listName: string | null = null;

  private get canApply(): boolean {
    return this.todoList != null && this.listName != null && this.listName.length > 0;
  }

  showDialog(list: TodoList): void {
    this.todoList = list;
    this.listName = list.name;
    this.dialogVisible = true;

    this.$nextTick(() => {
      const input = this.$refs.listNameInput as ElInput;
      input.select();
      input.focus();
    });
  }

  private clearData(): void {
    this.todoList = null;
    this.listName = null;
  }

  private closeDialog(): void {
    this.dialogVisible = false;
  }

  private okHandler(): void {
    if (this.canApply) {
      this.todoList!.name = this.listName!;
    }
    this.closeDialog();
  }
}
