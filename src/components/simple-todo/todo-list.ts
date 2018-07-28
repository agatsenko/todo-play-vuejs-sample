import { Component, Vue } from "vue-property-decorator";
import SimpleTodoItemComponent from "@/components/simple-todo/todo-list-item";

import { TodoItem, TodoList, defaultTodoItemFactory } from "@/model/todo";
import { Filter, FilterValue } from "@/components/simple-todo/filter";


@Component({
  components: {
    SimpleTodoItemComponent,
  },
})
export default class SimpleTodoListComponent extends Vue {
  rowJustify = "center";
  rowGutter = 10;
  lgOuterColSpan = 8;
  lgMiddleColSpan = 8;
  mdOuterColSpan = 7;
  mdMiddleColSpan = 10;
  smOuterColSpan = 4;
  smMiddleColSpan = 16;

  newTaskDescription: string | null = null;

  filter = new Filter();

  private todoList: TodoList = new TodoList(
    "Simple TODO List",
    defaultTodoItemFactory,
    [
      new TodoItem("1", "First task"),
      new TodoItem("2", "Second task"),
      new TodoItem("3", "Thrid task"),
      new TodoItem("4", "Fourth task"),
    ],
  );

  get todoItems(): ReadonlyArray<TodoItem> {
    let filteredItems: ReadonlyArray<TodoItem>;
    switch (this.filter.selected) {
      case FilterValue.Active:
        filteredItems = this.filterActive();
        break;
      case FilterValue.Completed:
        filteredItems = this.filterCompleted();
        break;
      default:
        filteredItems = this.filterAll();
        break;
    }
    return filteredItems;
  }

  get itemsLeft(): number {
    return this.todoList.items.filter(item => item.completed).length;
  }

  get canAddTask(): boolean {
    return this.newTaskDescription !== null && this.newTaskDescription.length > 0;
  }

  filterItems(filterValue: FilterValue): void {
    if (filterValue !== this.filter.selected) {
      this.filter.selected = filterValue;
    }
  }

  addNewItem(): void {
    if (this.canAddTask) {
      this.todoList.put({ description: this.newTaskDescription! });
      this.newTaskDescription = "";
    }
  }

  removeItem(id: string): void {
    this.todoList.remove(id);
  }

  clearCompleted(): void {
    for (let i = 0; i < this.todoList.items.length; ++i) {
      if (this.todoList.items[i].completed) {
        this.todoList.remove(this.todoList.items[i]);
        --i;
      }
    }
  }

  private filterAll(): ReadonlyArray<TodoItem> {
    return this.todoList.items;
  }

  private filterActive(): ReadonlyArray<TodoItem> {
    return this.todoList.items.filter(item => !item.completed);
  }

  private filterCompleted(): ReadonlyArray<TodoItem> {
    return this.todoList.items.filter(item => item.completed);
  }
}
