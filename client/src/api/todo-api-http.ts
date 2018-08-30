import { ApiError, TodoApi } from "@/api/todo-api";
import { ITodoList } from "@/model/todo2";
import Axios, { AxiosError, AxiosInstance } from "axios";

function resolveErrorMessage(err: AxiosError): string {
  let message;
  if (err.response) {
    message = err.response.data && err.response.data.message ? err.response.data.message : err.response.statusText;
  }
  else if (err.message) {
    message = err.message;
  }
  else {
    message = "Unknown error.";
  }
  return message;
}

function toApiError(err: AxiosError): ApiError {
  return err instanceof ApiError ? err : new ApiError(resolveErrorMessage(err), err);
}

export class HttpTodoApi implements TodoApi {
  private readonly axios: AxiosInstance;

  constructor(baseUrl: string) {
    this.axios = Axios.create({
      baseURL: baseUrl,
    });
  }

  getLists(): Promise<ITodoList[]> {
    return this.axios.
      get("/lists").
      then(resp => resp.data as ITodoList[]).
      catch(err => {
        throw toApiError(err);
      });
  }

  getList(listId: string): Promise<ITodoList> {
    return this.axios.
      get(`/lists/${listId}`).
      then(resp => {
        const list: ITodoList = resp.data as ITodoList;
        return list;
      }).
      catch(err => {
        throw toApiError(err);
      });
  }

  addList(listName: string): Promise<ITodoList> {
    return this.axios.
      post("/lists", { name: listName, tasks: [] } as ITodoList).
      then(resp => resp.data as ITodoList).
      catch(err => {
        throw toApiError(err);
      });
  }

  updateList(list: ITodoList): Promise<ITodoList> {
    return this.axios.
      put("/lists", list).
      then(resp => resp.data as ITodoList).
      catch(err => {
        throw toApiError(err);
      });
  }

  deleteList(listId: string): Promise<void> {
    return this.axios.
      delete(`/lists/${listId}`).
      then(resp => undefined).
      catch(err => {
        throw toApiError(err);
      });
  }
}
