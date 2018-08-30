import { ITodoList } from "@/model/todo2";
import { BaseError } from "./../utils/errors";

export class ApiError extends BaseError {
}

export interface TodoApi {
  getLists(): Promise<ITodoList[]>;

  getList(listId: string): Promise<ITodoList>;

  addList(listName: string): Promise<ITodoList>;

  updateList(list: ITodoList): Promise<ITodoList>;

  deleteList(listId: string): Promise<void>;
}
