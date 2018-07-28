import { Vue, Component, Prop } from "vue-property-decorator";
import { TodoList, TodoItem } from "@/model/todo";
import { ElInput } from "element-ui/types/input";

@Component
export default class ElementUITodoList extends Vue {
  @Prop({ required: true }) list!: TodoList;

  newItemDescription: string | null = null;

  showEditDialog = false;
  itemToEdit: TodoItem | null = null;

  get canAddItem(): boolean {
    return this.newItemDescription !== null && this.newItemDescription.length > 0;
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

  addItem(): void {
    if (this.canAddItem) {
      this.list.put({ description: this.newItemDescription! });
      this.newItemDescription = "";
    }
  }

  editItem(item: TodoItem): void {
    this.itemToEdit = item;
    this.showEditDialog = true;
  }

  deleteItem(item: TodoItem): void {
    this.list.remove(item.id);
  }

  rowClassName(rowInfo: any): string {
    const item = rowInfo.row as TodoItem;
    return item.completed ? "eui-completed-task-row" : "";
  }
}
