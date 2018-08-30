import EuiTodoItemEditDialog from "@/components/elementui-todo/todo-item-editor";
import EuiTodoListEditDialog from "@/components/elementui-todo/todo-list-editor";
import { createTodoTask } from "@/components/elementui-todo/todo-model";
import { ITodoList, ITodoTask } from "@/model/todo2";
import { ElInput } from "element-ui/types/input";
import { Component, Prop, Vue } from "vue-property-decorator";

@Component({
  components: {
    EuiTodoListEditDialog,
    EuiTodoItemEditDialog,
  },
})
export default class EuiTodoList extends Vue {
  @Prop({ required: true }) list!: ITodoList;

  newItemDescription: string | null = null;

  editItemDialogVisible = false;
  itemToEdit: ITodoTask | null = null;

  get hasCompletedItems(): boolean {
    return this.list.tasks.find(task => task.completed) !== undefined;
  }

  get canAddItem(): boolean {
    return this.newItemDescription !== null && this.newItemDescription.length > 0;
  }

  get activeItemsCount(): number {
    let activeCount = 0;
    this.list.tasks.forEach(task => {
      if (!task.completed) {
        ++activeCount;
      }
    });
    return activeCount;
  }

  get items(): ReadonlyArray<ITodoTask> {
    return this.list.tasks;
  }

  mounted(): void {
    this.$nextTick(() => {
      const input = this.$refs.newItemInput as ElInput;
      input.focus();
    });
  }

  renameList(): void {
    const dialog = this.$refs.renameListDialog as EuiTodoListEditDialog;
    dialog.showDialog(this.list);
  }

  deleteList(): void {
    this.
      $confirm(`Are you sure to delete '${this.list.name}' list?`, "Delete List").
      then(() => {
        this.$emit("deleteList", this.list);
      }).
      catch(() => {
        // do nothing
      });
  }

  filterItems(completed: boolean, task: ITodoTask): boolean {
    return task.completed === completed;
  }

  addItem(): void {
    if (this.canAddItem) {
      this.list.tasks.push(createTodoTask(this.newItemDescription!));
      this.newItemDescription = "";
    }
  }

  editItem(task: ITodoTask): void {
    const dialog = this.$refs.itemEditDialog as EuiTodoItemEditDialog;
    dialog.showDialog(task);
  }

  deleteItem(task: ITodoTask): void {
    const foundIndex = this.list.tasks.findIndex(t => t.id === task.id);
    if (foundIndex > -1) {
      this.list.tasks.splice(foundIndex, 1);
    }
  }

  clearCompletedItems(): void {
    this.
      $confirm("Are you sure to delete the all completed tasks?", "Clear Completed Tsks").
      then(() => {
        for (let i = 0; i < this.list.tasks.length; ++i) {
          const task = this.list.tasks[i];
          if (task.completed) {
            this.deleteItem(task);
            --i;
          }
        }
      }).
      catch(() => {
        // do nothing
      });
  }

  rowClassName(rowInfo: any): string {
    const task = rowInfo.row as ITodoTask;
    return task.completed ? "eui-completed-task-row" : "";
  }
}
