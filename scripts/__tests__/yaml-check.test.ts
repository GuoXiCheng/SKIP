import { yamlCheck } from "../yaml-check";

describe("YAML Check", () => {
  it("正确格式的 YAML", () => {
    const detail = yamlCheck("scripts/__tests__/examples/correct-yaml.yaml");
    expect(detail).toEqual([
      {
        packageName: "com.test.skip",
        skipTexts: [
          {
            text: "跳过",
            length: 2,
          },
        ],
        skipIds: [
          {
            id: "com.test.skip:id/skip",
          },
        ],
        skipBounds: [
          {
            bound: "123,456,789,1011",
            resolution: "1080,1920",
          },
        ],
      },
    ]);
  });
  it("缺少 packageName", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/missing-packageName.yaml")).toThrow(
      "packageName must be a string"
    );
  });
  it("packageName 不是字符串", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/packageName-not-string.yaml")).toThrow(
      "packageName must be a string"
    );
  });
  it("packageName 为空", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/packageName-empty.yaml")).toThrow(
      "packageName must not have leading or trailing whitespace"
    );
  });
  it("packageName 有前导空格", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/packageName-leading-space.yaml")).toThrow(
      "packageName must not have leading or trailing whitespace"
    );
  });
  it("只有 packageName", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/only-packageName.yaml")).toThrow(
      "skipIds, skipTexts, or skipBounds is required"
    );
  });
  it("出现未知的键", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/unknown-keys.yaml")).toThrow(
      "Unrecognized key(s) in object: 'unknownKey'"
    );
  });
  it("skipIds 不是数组", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipIds-not-array.yaml")).toThrow("skipIds must be an array");
  });
  it("skipIds 缺少 id", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipIds-missing-id.yaml")).toThrow("id must be a string");
  });
  it("skipIds.id 不是字符串", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipIds-id-not-string.yaml")).toThrow("id must be a string");
  });
  it("skipIds.id 为空", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipIds-id-empty.yaml")).toThrow(
      "id must not have leading or trailing whitespace"
    );
  });
  it("skipIds.id 有前导空格", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipIds-id-leading-space.yaml")).toThrow(
      "id must not have leading or trailing whitespace"
    );
  });
  it("skipTexts 不是数组", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipTexts-not-array.yaml")).toThrow(
      "skipTexts must be an array"
    );
  });
  it("skipTexts.text 不是字符串", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipTexts-text-not-string.yaml")).toThrow(
      "text must be a string"
    );
  });
  it("skipTexts.text 为空", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipTexts-text-empty.yaml")).toThrow(
      "text must not have leading or trailing whitespace"
    );
  });
  it("skipTexts.text 有前导空格", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipTexts-text-leading-space.yaml")).toThrow(
      "text must not have leading or trailing whitespace"
    );
  });
  it("skipTexts.length 不是数字", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipTexts-length-not-number.yaml")).toThrow(
      "length must be a number"
    );
  });
  it("skipTexts.length 是负数", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipTexts-length-negative.yaml")).toThrow(
      "length must be non-negative"
    );
  });
  it("skipTexts 出现未知的键", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipTexts-unknown-keys.yaml")).toThrow(
      "Unrecognized key(s) in object: 'unknownKey'"
    );
  });
  it("skipBounds 不是数组", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipBounds-not-array.yaml")).toThrow(
      "skipBounds must be an array"
    );
  });
  it("skipBounds 缺少 bound", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipBounds-missing-bound.yaml")).toThrow(
      "bound must be a string"
    );
  });
  it("skipBounds 缺少 resolution", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipBounds-missing-resolution.yaml")).toThrow(
      "resolution must be a string"
    );
  });
  it("skipBounds 的 bound 不是字符串", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipBounds-bound-not-string.yaml")).toThrow(
      "bound must be a string"
    );
  });
  it("skipBounds 的 bound 为空", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipBounds-bound-empty.yaml")).toThrow(
      "bound must have four numeric values"
    );
  });
  it("skipBounds 用 , 分隔后长度不为 4", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipBounds-bound-not-4-parts.yaml")).toThrow(
      "bound must have four numeric values"
    );
  });
  it("skipBounds 的 bound 分隔后不是数字", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipBounds-bound-not-number.yaml")).toThrow(
      "bound must have four numeric values"
    );
  });
  it("skipBounds 的 resolution 不是字符串", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipBounds-resolution-not-string.yaml")).toThrow(
      "resolution must be a string"
    );
  });
  it("skipBounds 的 resolution 为空", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipBounds-resolution-empty.yaml")).toThrow(
      "resolution must have two numeric values"
    );
  });
  it("skipBounds 的 resolution 用 , 分隔后长度不为 2", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipBounds-resolution-not-2-parts.yaml")).toThrow(
      "resolution must have two numeric values"
    );
  });
  it("skipBounds 的 resolution 分隔后不是数字", () => {
    expect(() => yamlCheck("scripts/__tests__/examples/skipBounds-resolution-not-number.yaml")).toThrow(
      "resolution must have two numeric values"
    );
  });
});
