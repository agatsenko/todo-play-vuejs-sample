import { ITodoList, ITodoTask } from "@/model/todo2";
import { HttpTodoApi } from "@/api/todo-api-http";

export function createTodoList(listName: string, ...listTasks: ITodoTask[]): ITodoList {
  return {
    name: listName,
    tasks: listTasks,
  };
}

export function createTodoTask(taskDescription: string, taskCompleted: boolean = false): ITodoTask {
  return {
    description: taskDescription,
    completed: taskCompleted,
  };
}

// export const todoApi = new HttpTodoApi("http://localhost:9000/api/todo/v2/");
export const todoApi = new HttpTodoApi("/api/todo/v2/");
