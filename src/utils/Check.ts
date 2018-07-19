import { InvalidArgError, InvalidStateError } from '@/utils/Errors';

export class Check {
  static arg(condition: boolean, msg: () => string): void {
    Check.check(condition, (errMsg) => new InvalidArgError(errMsg), msg);
  }

  static argDefined<T>(arg: T | undefined, argName: string): void {
    Check.arg(arg !== undefined, () => `${argName} is undefined`);
  }

  static argNotNull<T>(arg: T | null, argName: string): void {
    Check.arg(arg !== null, () => `${argName} is null`);
  }

  static foo(): void {
    const v: string | null | undefined = null;
    Check.argDefined(v, 'v');
    Check.argNotNull(v, 'v');
    Check.argNotEmpty(v, 'v');
  }

  static argNotEmpty(arg: string | undefined | null, argName: string): void;
  static argNotEmpty<T>(
    arg: T[] | ReadonlyArray<T> | Set<T> | ReadonlySet<T> | undefined | null,
    argName: string,
  ): void;
  static argNotEmpty<K, V>(arg: Map<K, V> | ReadonlyMap<K, V> | undefined | null, argName: string): void;
  static argNotEmpty(arg: any | undefined | null, argName: string): void {
    Check.argDefined(arg, argName);
    Check.argNotNull(arg, argName);

    let length: number;
    if (typeof arg === 'string') {
      length = (arg as string).length;
    }
    else if (Array.isArray(arg)) {
      length = (arg as any[]).length;
    }
    else if (arg instanceof Set) {
      length = (arg as Set<any>).size;
    }
    else if (arg instanceof Map) {
      length = (arg as Map<any, any>).size;
    }
    else {
      throw new InvalidStateError(`${arg} argument is unsupported`);
    }
    Check.arg(length > 0, () => `${argName} is empty`);
  }

  private static check<E extends Error>(condition: boolean, err: (msg: string) => E, msg: () => string): void {
    if (!condition) {
      throw err(msg());
    }
  }
}
