import * as check from "@/utils/check";
import { InvalidStateError } from "@/utils/errors";
import { Equatable } from "./equals";

export default abstract class Option<V> extends Equatable {
  static of<T>(value: T | null | undefined): Option<T> {
    return value === undefined || value === null
      ? Option.none()
      : Option.some(value);
  }

  static some<T>(value: T): Option<T> {
    return new Some(value);
  }

  static none<T>(): Option<T> {
    return none as Option<T>;
  }

  abstract get isEmpty(): boolean;

  get nonEmpty(): boolean {
    return !this.isEmpty;
  }

  abstract get(): V;

  abstract getOrElse<T extends V>(f: () => T): V | T;

  abstract orElse<T extends V>(f: () => Option<T>): Option<V> | Option<T>;

  abstract map<T>(f: (v: V) => T): Option<T>;

  abstract flatMap<T>(f: (v: V) => Option<T>): Option<T>;

  abstract filter(f: (v: V) => boolean): Option<V>;

  abstract forAll(f: (v: V) => boolean): boolean;

  abstract forEach<R>(f: (v: V) => R): void;

  abstract equals(v: any): boolean;

  abstract toString(): string;
}

export class Some<V> extends Option<V> {
  private value: V;

  constructor(value: V) {
    super();

    check.argDefined(value, "value");
    this.value = value;
  }

  get __identity__(): "@utils/option/Some" {
    return "@utils/option/Some";
  }

  get isEmpty(): boolean {
    return false;
  }

  get(): V {
    return this.value;
  }

  getOrElse<T extends V>(f: () => T): V | T {
    return this.get();
  }

  orElse<T extends V>(f: () => Option<T>): Option<V> | Option<T> {
    return this;
  }

  map<T>(f: (v: V) => T): Option<T> {
    return Option.of(f(this.value));
  }

  flatMap<T>(f: (o: V) => Option<T>): Option<T> {
    return f(this.get());
  }

  filter(f: (v: V) => boolean): Option<V> {
    return f(this.get()) ? this : Option.none();
  }

  forAll(f: (v: V) => boolean): boolean {
    return f(this.get());
  }

  forEach<R>(f: (v: V) => R): void {
    f(this.get());
  }

  equals(v: any): boolean {
    if (!this.canEquals(v)) {
      return false;
    }
    const some = v as Some<any>;
    return this.get() === some.get();
  }

  toString(): string {
    return `Some('${this.get()}')`;
  }
}

export class None extends Option<any> {
  get __identity__(): "@/utils/option/None" {
    return "@/utils/option/None";
  }

  get isEmpty(): boolean {
    return true;
  }

  get(): any {
    throw new InvalidStateError("no such element");
  }

  getOrElse<T>(f: () => T): T {
    return f();
  }

  orElse<T>(f: () => Option<T>): Option<T> {
    return f();
  }

  map<T>(f: (v: any) => T): Option<T> {
    return Option.none();
  }

  flatMap<T>(f: (v: any) => Option<T>): Option<T> {
    return Option.none();
  }

  filter(f: (v: any) => boolean): Option<any> {
    return this;
  }

  forAll(f: (v: any) => boolean): boolean {
    return false;
  }

  forEach<R>(f: (v: any) => R): void {
    // do nothing
  }

  equals(v: any): boolean {
    return this.canEquals(v);
  }

  toString(): string {
    return "None";
  }
}

export const none = new None();
