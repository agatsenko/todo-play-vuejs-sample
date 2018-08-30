import { ITodoList } from "@/model/todo2";
import { ElInput } from "element-ui/types/input";
import { Component, Vue } from "vue-property-decorator";
import { todoApi } from "@/components/elementui-todo/todo-model";

@Component
export default class EuiTodoListEditDialog extends Vue {
  private todoList: ITodoList | null = null;
  private dialogVisible: boolean = false;
  private listName: string | null = null;

  private get canApply(): boolean {
    return this.todoList != null && this.listName != null && this.listName.length > 0;
  }

  showDialog(list: ITodoList): void {
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
      todoApi.updateList(this.todoList!).
        catch(err => alert(err.message));
      }
    this.closeDialog();
  }
}
