import { TodoItem, ITodoItemSpec, defaultTodoItemFactory, TodoItemFactory, TodoList } from "@/model/todo";
import { InvalidArgError } from "@/utils/errors";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// TodoItem tests

interface ITodoItemCtorSpec {
  id: string;
  description: string;
  completed: boolean;
}

interface ITodoItemFactoryTestSpec extends ITodoItemSpec {
  expectedCompleted: boolean;
}

test.each`
  id         | description           | completed
  ${"item1"} | ${"todo description"} | ${true}
  ${"item1"} | ${"todo description"} | ${false}
`("new TodoItem($id, $description, $completed) should create new TodoItem", (spec: ITodoItemCtorSpec) => {
  const item = new TodoItem(spec.id, spec.description, spec.completed);
  expect(item.id).toBe(spec.id);
  expect(item.description).toBe(spec.description);
  expect(item.completed).toBe(spec.completed);
});

test.each`
  id         | description    | completed
  ${""}      | ${"test todo"} | ${false}
  ${""}      | ${"test todo"} | ${true}
  ${"item1"} | ${""}          | ${false}
  ${"item1"} | ${""}          | ${true}
`("new TodoItem($id, $description, $completed) should throw InvalidArgError", (spec: ITodoItemCtorSpec) => {
  function test(): void {
    const item = new TodoItem(spec.id, spec.description, spec.completed);
  }
  expect(test).toThrow(InvalidArgError);
});

test("todoItem.description = '' should throw InvalidArgError", () => {
  const todoItem = new TodoItem("item1", "item description", false);
  function test(): void {
    todoItem.description = "";
  }
  expect(test).toThrow(InvalidArgError);
  expect(test).toThrow(/.*description.*/);
});

test.each`
  description           | completed    | expectedCompleted
  ${"todo description"} | ${true}      | ${true}
  ${"todo description"} | ${false}     | ${false}
  ${"todo description"} | ${undefined} | ${false}
`(
  "defautTodoItemFactory({ $description, $completed }) should create a new TodoItem",
  (spec: ITodoItemFactoryTestSpec) => {
    const item = defaultTodoItemFactory(spec);
    expect(item.id).toBeDefined();
    expect(item.id).not.toBeNull();
    expect(item.description).toBe(spec.description);
    expect(item.completed).toBe(spec.expectedCompleted);
});

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// TodoList tests

test("new TodoList(itemFactory) should create a new empty list", () => {
  const list = new TodoList(defaultTodoItemFactory);
  expect(list.items.length).toBe(0);
});

test("new TodoList(itemFactory, items) should create a new list with the specified items", () => {
  const item1 = new TodoItem("item1", "description for item1", false);
  // tslint:disable-next-line:variable-name
  const item2_1 = new TodoItem("item2", "description for item2_1", false);
  // tslint:disable-next-line:variable-name
  const item2_2 = new TodoItem("item2", "description for item2_2", true);
  const item3 = new TodoItem("item3", "description for item3", true);

  const items = [item1, item2_1, item2_2, item3];
  const expectedItems = [item1, item2_2, item3];

  const list = new TodoList(defaultTodoItemFactory, items);
  expect(list.items).toEqual(expectedItems);
});

test.each`
  description | completed
  ${"item 1"} | ${true}
  ${"item 2"} | ${false}
`("list.put(spec: ITodoItemSpec) should create a new item and add it to the list", (spec: ITodoItemSpec) => {
  const list = new TodoList(
    defaultTodoItemFactory,
    [
      defaultTodoItemFactory({description: "test item 1"}),
      defaultTodoItemFactory({description: "test item 2", completed: true}),
      defaultTodoItemFactory({description: "test item 3"}),
    ],
  );
  const item = list.put(spec);
  expect(item).toBeDefined();
  expect(item).not.toBeNull();
  expect(item.id).toBeDefined();
  expect(item.id).not.toBeNull();
  expect(item.description).toBe(spec.description);
  expect(item.completed).toBe(spec.completed);
  expect(list.items.indexOf(item)).toBeGreaterThan(-1);
});

test(
  "todoList.put(item: TodoItem) should add the item to the list if the list dose not contain other item with same id",
  () => {
    const list = new TodoList(
      defaultTodoItemFactory,
      [
        new TodoItem("1", "desc1", true),
        new TodoItem("2", "desc2", false),
      ],
    );
    const initialListSize = list.size;
    const newItem = new TodoItem("3", "desc3", false);

    list.put(newItem);
    expect(list.size).toBe(initialListSize + 1);
    expect(list.items.indexOf(newItem)).toBeGreaterThan(-1);
});

test("todoList.put(item: TodoItem) should replace the existing item with the same id", () => {
  const containingItem = new TodoItem("test-item", "test item", false);
  const newItem = new TodoItem("test-item", "new item", true);
  const list = new TodoList(
    defaultTodoItemFactory,
    [
      new TodoItem("1", "desc1", true),
      containingItem,
      new TodoItem("2", "desc2", false),
    ],
  );
  const initialListSize = list.size;

  list.put(newItem);
  expect(list.size).toBe(initialListSize);
  expect(list.contains(containingItem)).toBeFalsy();
  expect(list.contains(newItem)).toBeTruthy();
});

test("todoList.remove(id: string) should remove the item with the specified id", () => {
  const containingItem = new TodoItem("test-item", "test item", false);
  const list = new TodoList(
    defaultTodoItemFactory,
    [
      new TodoItem("1", "desc1", true),
      containingItem,
      new TodoItem("2", "desc2", false),
    ],
  );
  const initialListSize = list.size;

  const removedItem = list.remove(containingItem.id);
  expect(list.size).toBe(initialListSize - 1);
  expect(list.contains(containingItem)).toBeFalsy();
  expect(removedItem).toBe(containingItem);
});

test(
  "todoList.remove(id: string) should not remove any item if the list does not contain the item with the specified id",
  () => {
    const id = "test-item";
    const list = new TodoList(
      defaultTodoItemFactory,
      [
        new TodoItem("1", "desc1", true),
        new TodoItem("2", "desc2", false),
      ],
    );
    const initialListSize = list.size;

    expect(list.contains(id)).toBeFalsy();

    const removedItem = list.remove(id);
    expect(list.size).toBe(initialListSize);
    expect(removedItem).toBeUndefined();
});

test("todoList.remove(item: TodoItem) should remove the specified item if it is contained in the list", () => {
  const containingItem = new TodoItem("test-item", "test item", false);
  const list = new TodoList(
    defaultTodoItemFactory,
    [
      new TodoItem("1", "desc1", true),
      containingItem,
      new TodoItem("2", "desc2", false),
    ],
  );
  const initialListSize = list.size;

  expect(list.contains(containingItem)).toBeTruthy();

  expect(list.remove(containingItem)).toBeTruthy();
  expect(list.size).toBe(initialListSize - 1);
  expect(list.contains(containingItem)).toBeFalsy();
});

test(
  "todoList.remove(item: TodoItem) should not remove any item if the specified item is not contained in the list",
  () => {
    const notContainingItem = new TodoItem("test-item", "test item", false);
    const list = new TodoList(
      defaultTodoItemFactory,
      [
        new TodoItem("1", "desc1", true),
        new TodoItem("2", "desc2", false),
      ],
    );
    const initialListSize = list.size;

    expect(list.contains(notContainingItem)).toBeFalsy();

    expect(list.remove(notContainingItem)).toBeFalsy();
    expect(list.size).toBe(initialListSize);
});

test.each`
  list
  ${new TodoList(defaultTodoItemFactory, [new TodoItem("1", "desc", false), new TodoItem("2", "desc", true)])}
  ${new TodoList(defaultTodoItemFactory)}
`("todoList.clear() should remove the all items in list", ({ list }) => {
  const todoList = list as TodoList;
  todoList.clear();
  expect(todoList.items.length).toBe(0);
});
