import * as check from "@/utils/check";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// TodoItem components

export class TodoItem {
  private _id: string;
  private _description: string;
  private _completed: boolean;

  constructor(id: string, description: string, completed: boolean = false) {
    check.argNotEmpty(id, "id");
    check.argNotEmpty(description, "description");

    this._id = id;
    this._description = description;
    this._completed = completed;
  }

  get id(): string {
    return this._id;
  }

  get description(): string {
    return this._description;
  }

  set description(description: string) {
    check.argNotEmpty(description, "description");
    this._description = description;
  }

  get completed(): boolean {
    return this._completed;
  }

  set completed(completed: boolean) {
    this._completed = completed;
  }

  toString() {
    return `${TodoItem.constructor.name} ` +
      `{id: ${this._id}, description: ${this._description}, completed: ${this._completed}}`;
  }
}

export interface ITodoItemSpec {
  description: string;
  completed?: boolean;
}

export type TodoItemFactory = (spec: ITodoItemSpec) => TodoItem;

let todoItemIdSeq = 0;
export const defaultTodoItemFactory = (spec: ITodoItemSpec): TodoItem => {
  return new TodoItem(
    `item${++todoItemIdSeq}`,
    spec.description,
    spec.completed === undefined ? false : spec.completed,
  );
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// TodoList components

export class TodoList {
  private readonly _itemFactory: TodoItemFactory;
  private readonly _items: TodoItem[];

  constructor(itemFactory: TodoItemFactory = defaultTodoItemFactory, items?: TodoItem[]) {
    this._itemFactory = itemFactory;
    this._items = [];
    if (items !== undefined) {
      items.forEach(item => this.putItem(item));
    }
  }

  get size(): number {
    return this._items.length;
  }

  get items(): ReadonlyArray<TodoItem> {
    return this._items;
  }

  contains(idOrItem: string | TodoItem): boolean {
    return typeof idOrItem === "string" ? this.containsItemById(idOrItem) : this.containsItem(idOrItem);
  }

  put(spec: ITodoItemSpec): TodoItem;
  put(item: TodoItem): void;
  put(item: any): any {
    let newItem: TodoItem;
    let result: any;
    if (item instanceof TodoItem) {
      newItem = item as TodoItem;
      result = undefined;
    }
    else {
      newItem = this._itemFactory(item as ITodoItemSpec);
      result = newItem;
    }
    this.putItem(newItem);
    return result;
  }

  remove(id: string): TodoItem | undefined;
  remove(item: TodoItem): boolean;
  remove(item: any): any {
    return typeof item === "string" ? this.removeItemById(item as string) : this.removeItem(item as TodoItem);
  }

  clear(): void {
    if (this._items.length > 0) {
      this._items.splice(0, this._items.length);
    }
  }

  toString(): string {
    return `${TodoList.constructor.name} { items: ${this._items} }`;
  }

  private containsItem(item: TodoItem): boolean {
    return this._items.indexOf(item) > -1;
  }

  private containsItemById(id: string): boolean {
    let contains = false;
    for (const item of this._items) {
      if (item.id === id) {
        contains = true;
        break;
      }
    }
    return contains;
  }

  private findItemIndex(id: string): number | undefined {
    let foundIndex: number | undefined;
    for (let index = 0; index < this._items.length; ++index) {
      if (this._items[index].id === id) {
        foundIndex = index;
        break;
      }
    }
    return foundIndex;
  }

  private putItem(item: TodoItem): void {
    const existingIndex = this.findItemIndex(item.id);
    if (existingIndex === undefined) {
      this._items.push(item);
    }
    else if (this._items[existingIndex] !== item) {
      this._items.splice(existingIndex, 1);
      this._items.push(item);
    }
  }

  private removeItem(item: TodoItem): boolean {
    const index = this._items.indexOf(item);
    if (index > -1) {
      this._items.splice(index, 1);
    }
    return index > -1;
  }

  private removeItemById(id: string): TodoItem | undefined {
    let item: TodoItem | undefined;
    const index = this.findItemIndex(id);
    if (index !== undefined) {
      item = this._items[index];
      this._items.splice(index, 1);
    }
    return item;
  }
}
