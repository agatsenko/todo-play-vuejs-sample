import * as check from "@/utils/check";
import { InvalidArgError, InvalidStateError } from "@/utils/errors";

test("check.arg(true, msg) should not throw any error", () => {
  check.arg(true, () => "error message");
});

test("check.arg(false, msg) should throw InvalidArgError", () => {
  const errMsg = "error message";
  function test(): void {
    check.arg(false, () => errMsg);
  }
  expect(test).toThrow(InvalidArgError);
  expect(test).toThrow(errMsg);
});

test.each`
  arg
  ${1}
  ${"string value"}
  ${null}
  ${{ one: 1, two: "two" }}
`("check.argDefined($arg, argName) should not throw any error", ({ arg }) => {
  check.argDefined(arg, "arg");
});

test("check.argDefined(undefined, argName) should throw InvalidArgError", () => {
  const argName = "argName";
  function test(): void {
    check.argDefined(undefined, argName);
  }
  expect(test).toThrow(InvalidArgError);
  expect(test).toThrow(/.*undefined.*/);
  expect(test).toThrow(new RegExp(`.*${argName}.*`));
});

test.each`
  arg
  ${1}
  ${"string value"}
  ${undefined}
  ${{ one: 1, two: "two" }}
`("check.argNotNull($arg, argName) should not throw any error", ({ arg }) => {
  check.argNotNull(arg, "arg");
});

test("check.argNotNull(null, argName) should throw IllegalArgError", () => {
  const argName = "arg";
  function test(): void {
    check.argNotNull(null, argName);
  }
  expect(test).toThrow(InvalidArgError);
  expect(test).toThrow(/.*null.*/);
  expect(test).toThrow(new RegExp(`.*${argName}.*`));
});

test.each`
  arg
  ${1}
  ${"string value"}
  ${{ one: 1, two: "two" }}
`("check.argDefinedAndNotNull($arg, argName) should not throw any error", ({ arg }) => {
  check.argDefinedAndNotNull(arg, "arg");
});

test.each`
  arg
  ${null}
  ${undefined}
`("check.argDefinedAndNotNull($arg, argName) should throw IllegalArgError", ({ arg }) => {
  const argName = "arg";
  function test(): void {
    check.argDefinedAndNotNull(undefined, argName);
  }
  expect(test).toThrow(InvalidArgError);
  expect(test).toThrow(/.*(undefined|null).*/);
  expect(test).toThrow(new RegExp(`.*${argName}.*`));
});

test.each`
  arg
  ${"string value"}
  ${[1, "two", 3]}
  ${new Set<number>([1, 2, 3])}
  ${new Map<number, string>([[1, "one"], [2, "two"]])}
`("check.argNotEmpty($arg, argName) should not throw any error", ({ arg }) => {
  check.argNotEmpty(arg, "arg");
});

test.each`
  arg
  ${undefined}
  ${null}
  ${""}
  ${[]}
  ${new Set<number>()}
  ${new Map<number, string>()}
`("check.argNotEmpty($arg, argName) should throw IllegalArgError", ({ arg }) => {
  const argName = "arg";
  function test(): void {
    check.argNotEmpty(arg, argName);
  }
  expect(test).toThrow(InvalidArgError);
  expect(test).toThrow(/.*(undefined|null|empty).*/);
  expect(test).toThrow(new RegExp(`.*${argName}.*`));
});

test("check.state(true, msg) should not throw any error", () => {
  check.state(true, () => "error message");
});

test("check.state(false, msg) should throw InvalidArgError", () => {
  const errMsg = "error message";
  function test(): void {
    check.state(false, () => errMsg);
  }
  expect(test).toThrow(InvalidStateError);
  expect(test).toThrow(errMsg);
});
