import { ITodoList, ITodoTask } from "@/model/todo2";

export let idSeq = 0;

export function createTodoList(listName: string, ...listTasks: ITodoTask[]): ITodoList {
  return {
    id: `${++idSeq}`,
    name: listName,
    tasks: listTasks,
  };
}

export function createTodoTask(taskDescription: string, taskCompleted: boolean = false): ITodoTask {
  return {
    id: `${++idSeq}`,
    description: taskDescription,
    completed: taskCompleted,
  };
}

export const todoLists: ITodoList[] = [
  createTodoList(
    "first",
    createTodoTask("task 1", true),
    createTodoTask("task 2", true),
    createTodoTask("task 3", false),
    createTodoTask("task 4"),
  ),
];
