import Option from "@/utils/option";

export interface ITodoTask {
  readonly id?: string;

  description: string;

  completed: boolean;
}

export interface ITodoList {
  readonly id?: string;

  name: string;

  readonly tasks: ITodoTask[];
}
