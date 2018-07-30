import { Vue, Component, Prop } from "vue-property-decorator";
import { TodoList, TodoItem } from "@/model/todo";
import { ElInput } from "element-ui/types/input";
import EuiTodoListEditDialog from "@/components/elementui-todo/todo-list-editor";
import EuiTodoItemEditDialog from "@/components/elementui-todo/todo-item-editor";

@Component({
  components: {
    EuiTodoListEditDialog,
    EuiTodoItemEditDialog,
  },
})
export default class EuiTodoList extends Vue {
  @Prop({ required: true }) list!: TodoList;

  newItemDescription: string | null = null;

  editItemDialogVisible = false;
  itemToEdit: TodoItem | null = null;

  get hasCompletedItems(): boolean {
    return this.list.items.find(item => item.completed) !== undefined;
  }

  get canAddItem(): boolean {
    return this.newItemDescription !== null && this.newItemDescription.length > 0;
  }

  get activeItemsCount(): number {
    let activeCount = 0;
    this.list.items.forEach(item => {
      if (!item.completed) {
        ++activeCount;
      }
    });
    return activeCount;
  }

  get items(): ReadonlyArray<TodoItem> {
    return this.list.items;
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

  filterItems(completed: boolean, item: TodoItem): boolean {
    return item.completed === completed;
  }

  addItem(): void {
    if (this.canAddItem) {
      this.list.put({ description: this.newItemDescription! });
      this.newItemDescription = "";
    }
  }

  editItem(item: TodoItem): void {
    const dialog = this.$refs.itemEditDialog as EuiTodoItemEditDialog;
    dialog.showDialog(item);
  }

  deleteItem(item: TodoItem): void {
    this.list.remove(item.id);
  }

  clearCompletedItems(): void {
    this.
      $confirm("Are you sure to delete the all completed tasks?", "Clear Completed Tsks").
      then(() => {
        for (let i = 0; i < this.list.items.length; ++i) {
          const item = this.list.items[i];
          if (item.completed) {
            this.list.remove(item);
            --i;
          }
        }
      }).
      catch(() => {
        // do nothing
      });
  }

  rowClassName(rowInfo: any): string {
    const item = rowInfo.row as TodoItem;
    return item.completed ? "eui-completed-task-row" : "";
  }
}
