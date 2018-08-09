import { Vue, Component, Prop } from "vue-property-decorator";
import { TodoItem } from "@/model/todo";
import { ElInput } from "element-ui/types/input";

@Component
export default class EuiTodoItemEditDialog extends Vue {
  private todoItem: TodoItem | null = null;
  private dialogVisible: boolean = false;
  private description: string | null = null;

  private get canApply(): boolean {
    return this.todoItem !== null && this.description !== null && this.description.length > 0;
  }

  showDialog(todoItem: TodoItem): void {
    this.todoItem = todoItem;
    this.description = todoItem.description;
    this.dialogVisible = true;

    this.$nextTick(() => {
      const input = this.$refs.descriptionInput as ElInput;
      input.select();
      input.focus();
    });
  }

  private clearData(): void {
    this.todoItem = null;
    this.description = null;
  }

  private closeDialog(): void {
    this.dialogVisible = false;
  }

  private okHandler(): void {
    if (this.canApply) {
      this.todoItem!.description = this.description!;
    }
    this.closeDialog();
  }
}
