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

test("check.argDefined(arg, argName) should not throw any error if arg is defined", () => {
  // FIXME: not yet implemented
  throw new Error("not yet implemented");
});

test("check.argDefined(undefined, argName) should throw InvalidArgError", () => {
  // FIXME: not yet implemented
  throw new Error("not yet implemented");
});

test("check.argNotNull(arg, argName) should not throw any error if arg is not null", () => {
  // FIXME: not yet implemented
  throw new Error("not yet implemented");
});

test("check.argNotNull(null, argName) should throw IllegalArgError", () => {
  // FIXME: not yet implemented
  throw new Error("not yet implemented");
});

test("check.argDefinedAndNotNull(arg, argName) should not throw any error if arg is defined and not null", () => {
  check.argDefinedAndNotNull("string value", "arg");
});

test("check.argDefinedAndNotNull(undefined, argName) should throw IllegalArgError", () => {
  const argName = "arg";
  function test(): void {
    check.argDefinedAndNotNull(undefined, argName);
  }
  expect(test).toThrow(InvalidArgError);
  expect(test).toThrow(new RegExp(`.*undefined.*`));
  expect(test).toThrow(new RegExp(`.*${argName}.*`));
});

test("check.argDefinedAndNotNull(null, argName) should throw IllegalArgError", () => {
  const argName = "arg";
  function test(): void {
    check.argDefinedAndNotNull(null, argName);
  }
  expect(test).toThrow(InvalidArgError);
  expect(test).toThrow(new RegExp(`.*null.*`));
  expect(test).toThrow(new RegExp(`.*${argName}.*`));
});

test("check.argNotEmpty(arg, argName) should not throw any error if arg defined and not null and not empty", () => {
  // FIXME: not yet implemented
  throw new Error("not yet implemented");
});

test("check.argNotEmpty(arg, argName) should not throw IllegalArgError if arg undefined or null or empty", () => {
  // FIXME: not yet implemented
  throw new Error("not yet implemented");
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
