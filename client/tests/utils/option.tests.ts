import { InvalidArgError, InvalidStateError } from "@/utils/errors";
import Option, { None, none, Some } from "@/utils/option";

//#region tests of Option

test.each`
  actual                  | expected            | isSame
  ${Option.of(undefined)} | ${none}             | ${true}
  ${Option.of(null)}      | ${none}             | ${true}
  ${Option.of("test")}    | ${new Some("test")} | ${false}
  ${Option.of(10)}        | ${new Some(10)}     | ${false}
  ${Option.some(10)}      | ${new Some(10)}     | ${false}
  ${Option.some("test")}  | ${new Some("test")} | ${false}
  ${Option.some(null)}    | ${new Some(null)}   | ${false}
  ${Option.none()}        | ${none}             | ${true}
`("create tests", ({ actual, expected, isSame }) => {
  expect(actual).toBeDefined();
  expect(actual).not.toBeNull();
  expect(actual).toStrictEqual(expected);
  if (isSame === true) {
    expect(actual === expected).toBeTruthy();
  }
});

//#endregion

//#region tests of Some

test("Option.some(undefined) should throw InvalidArgError", () => {
  expect(() => Option.some(undefined)).toThrow(InvalidArgError);
});

test("Option.some(value).isEmpty should be false", () => {
  expect(Option.some("test").isEmpty).toBeFalsy();
});

test("Option.some(value).nonEmpty should be true", () => {
  expect(Option.some("test").nonEmpty).toBeTruthy();
});

test("Option.some(value).get() should return value", () => {
  const value = "test";
  expect(Option.some(value).get()).toEqual(value);
});

test("Option.some(value).getOrElse(f) should return value", () => {
  const value = "test";
  const otherValue = "other";
  expect(value).not.toStrictEqual(otherValue);
  expect(Option.some(value).getOrElse(() => otherValue)).toEqual(value);
});

test("Option.some(value).orElse(f) should return same option", () => {
  const opt = Option.some("test");
  expect(opt.orElse(() => Option.some("other")) === opt).toBeTruthy();
});

test("Option.some('test').map(value => value + 'new') should return Some('testnew')", () => {
  const srcValue = "test";
  const newValue = "new";
  const expectedValue = srcValue + newValue;
  expect(Option.some(srcValue).map(v => v + newValue)).toStrictEqual(Option.some(expectedValue));
});

test("Option.some('test').flatMap(v => Option.some(v + 'new')) should return Option.some('testnew')", () => {
  const srcValue = "test";
  const newValue = "new";
  const expectedValue = srcValue + newValue;
  expect(Option.some(srcValue).flatMap(v => Option.some(v + newValue))).toStrictEqual(Option.some(expectedValue));
});

test.each`
  value     | arg                                             | result
  ${"test"} | ${Option.some("test")}                          | ${true}
  ${"test"} | ${Option.some(10)}                              | ${true}
  ${"test"} | ${Option.none()}                                | ${false}
  ${"test"} | ${"test"}                                       | ${false}
  ${"test"} | ${10}                                           | ${false}
  ${"test"} | ${null}                                         | ${false}
  ${"test"} | ${undefined}                                    | ${false}
  ${"test"} | ${{ __discriminator__: "test", value: "test" }} | ${false}
`("Option.some(value).canEquals(arg) tests", ({ value, arg, result }) => {
  expect(Option.some(value).canEquals(arg)).toStrictEqual(result);
});

test.each`
  value     | arg                                             | result
  ${"test"} | ${Option.some("test")}                          | ${true}
  ${"test"} | ${Option.some(10)}                              | ${false}
  ${"test"} | ${Option.none()}                                | ${false}
  ${"test"} | ${"test"}                                       | ${false}
  ${"test"} | ${10}                                           | ${false}
  ${"test"} | ${null}                                         | ${false}
  ${"test"} | ${undefined}                                    | ${false}
  ${"test"} | ${{ __discriminator__: "test", value: "test" }} | ${false}
`("Option.some(value).equals(arg) tests", ({ value, arg, result }) => {
  expect(Option.some(value).equals(arg)).toStrictEqual(result);
});

test(`Option.some('test').toString() should return "Some('test')" string`, () => {
  expect(Option.some("test").toString()).toStrictEqual("Some('test')");
});

//#endregion

//#region tests of None

test("Option.none().isEmpty should return true", () => {
  expect(Option.none().isEmpty).toBeTruthy();
});

test("Option.none().nonEmpty should return false", () => {
  expect(Option.none().nonEmpty).toBeFalsy();
});

test("Option.none().get() should throw InvalidStateError", () => {
  expect(() => Option.none().get()).toThrowError(InvalidStateError);
});

test("Option.none().getOrElse(() => 'other') should return 'other'", () => {
  const other = "other";
  expect(Option.none().getOrElse(() => other)).toStrictEqual(other);
});

test("Option.none().orElse(() => Option.some('other')) should return Some('other')", () => {
  const opt = Option.some("other");
  expect(Option.none().orElse(() => opt)).toStrictEqual(opt);
});

test("Option.none().map(f) should return None", () => {
  expect(Option.none().map(v => "other")).toStrictEqual(none);
});

test("Option.none().flatMap(f) should return None", () => {
  expect(Option.none().flatMap(v => Option.some("other"))).toStrictEqual(none);
});

test.each`
  arg                              | result
  ${Option.some("test")}           | ${false}
  ${Option.some(10)}               | ${false}
  ${Option.none()}                 | ${true}
  ${new None()}                    | ${true}
  ${"test"}                        | ${false}
  ${10}                            | ${false}
  ${null}                          | ${false}
  ${undefined}                     | ${false}
  ${{ __discriminator__: "test" }} | ${false}
`("Option.none().canEquals(arg) tests", ({ arg, result }) => {
  expect(Option.none().canEquals(arg)).toStrictEqual(result);
});

test.each`
  arg                              | result
  ${Option.some("test")}           | ${false}
  ${Option.some(10)}               | ${false}
  ${Option.none()}                 | ${true}
  ${new None()}                    | ${true}
  ${"test"}                        | ${false}
  ${10}                            | ${false}
  ${null}                          | ${false}
  ${undefined}                     | ${false}
  ${{ __discriminator__: "test" }} | ${false}
`("Option.none.equals(arg) tests", ({ arg, result }) => {
  expect(Option.none().equals(arg)).toStrictEqual(result);
});

test("Option.none().toString() should return 'None'", () => {
  expect(Option.none().toString()).toStrictEqual("None");
});

//#endregion
