import { InvalidArgError, InvalidStateError } from "@/utils/errors";

export function arg(condition: boolean, errMsg: () => string): void {
  check(condition, (msg) => new InvalidArgError(msg), errMsg);
}

export function argDefined<T>(argVal: T | undefined, argName: string): void {
  arg(argVal !== undefined, () => `${argName} is undefined`);
}

export function argNotNull<T>(argVal: T | null, argName: string): void {
  arg(argVal !== null, () => `${argName} is null`);
}

export function argDefinedAndNotNull<T>(argVal: T | undefined | null, argName: string) {
  argDefined(argVal, argName);
  argNotNull(argVal, argName);
}

export function argNotEmpty(argVal: string | undefined | null, argName: string): void;
export function argNotEmpty<T>(
  argVal: T[] | ReadonlyArray<T> | Set<T> | ReadonlySet<T> | undefined | null,
  argName: string,
): void;
export function argNotEmpty<K, V>(argVal: Map<K, V> | ReadonlyMap<K, V> | undefined | null, argName: string): void;
export function argNotEmpty(argVal: any | undefined | null, argName: string): void {
  argDefinedAndNotNull(argVal, argName);

  let length: number;
  if (typeof argVal === "string") {
    length = (argVal as string).length;
  }
  else if (Array.isArray(argVal)) {
    length = (argVal as any[]).length;
  }
  else if (argVal instanceof Set) {
    length = (argVal as Set<any>).size;
  }
  else if (argVal instanceof Map) {
    length = (argVal as Map<any, any>).size;
  }
  else {
    throw new InvalidStateError(`${argVal} argument is unsupported`);
  }
  arg(length > 0, () => `${argName} is empty`);
}

export function state(condition: boolean, errMsg: () => string) {
  check(condition, (msg: string) => new InvalidStateError(msg), errMsg);
}

function check<E extends Error>(condition: boolean, err: (msg: string) => E, msg: () => string): void {
  if (!condition) {
    throw err(msg());
  }
}
