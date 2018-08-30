import { ITodoTask } from "@/model/todo2";
import { Vue, Component, Prop } from "vue-property-decorator";
import { TodoItem } from "@/model/todo";
import { ElInput } from "element-ui/types/input";

@Component
export default class EuiTodoItemEditDialog extends Vue {
  private task: ITodoTask | null = null;
  private dialogVisible: boolean = false;
  private description: string | null = null;

  private get canApply(): boolean {
    return this.task !== null && this.description !== null && this.description.length > 0;
  }

  showDialog(task: ITodoTask): void {
    this.task = task;
    this.description = task.description;
    this.dialogVisible = true;

    this.$nextTick(() => {
      const input = this.$refs.descriptionInput as ElInput;
      input.select();
      input.focus();
    });
  }

  private clearData(): void {
    this.task = null;
    this.description = null;
  }

  private closeDialog(): void {
    this.dialogVisible = false;
  }

  private okHandler(): void {
    if (this.canApply) {
      this.task!.description = this.description!;
      this.$emit("itemModified", this.task);
    }
    this.closeDialog();
  }
}
