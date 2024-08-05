import { releaseFile } from "../release-file";
import fs from "node:fs";

describe("Release File", () => {
  it("目录不存在", () => {
    expect(() =>
      releaseFile({ apkPath: "scripts/__tests__/examples/not-exist", releasePath: "scripts/__tests__/examples/dist" })
    ).toThrow("apk path and release path should exist");
    expect(() =>
      releaseFile({
        apkPath: "scripts/__tests__/examples/apk-multiple",
        releasePath: "scripts/__tests__/examples/not-exist",
      })
    ).toThrow("apk path and release path should exist");
  });
  it("apk 目录存在多个文件", () => {
    expect(() =>
      releaseFile({
        apkPath: "scripts/__tests__/examples/apk-multiple",
        releasePath: "scripts/__tests__/examples/dist",
      })
    ).toThrow("apk path should contain only one file");
  });
  it("apk 的文件不是以 .apk 结尾", () => {
    expect(() =>
      releaseFile({ apkPath: "scripts/__tests__/examples/apk-not-apk", releasePath: "scripts/__tests__/examples/dist" })
    ).toThrow("apk file should end with .apk");
  });
  it("apk 的文件名不包含版本号", () => {
    expect(() =>
      releaseFile({
        apkPath: "scripts/__tests__/examples/apk-no-version",
        releasePath: "scripts/__tests__/examples/dist",
      })
    ).toThrow("apk file should contain version number");
  });
  it("apk 的文件名包含版本号", () => {
    expect(() =>
      releaseFile({
        apkPath: "scripts/__tests__/examples/apk-has-version",
        releasePath: "scripts/__tests__/examples/dist",
      })
    ).not.toThrow();
    const apkVersion = fs.readFileSync("scripts/__tests__/examples/dist/latest_version.txt", "utf-8");
    expect(apkVersion).toBe("2.1.1");
    expect(() => fs.accessSync("scripts/__tests__/examples/dist/SKIP-v2.1.1.apk")).not.toThrow();
    expect(() => fs.accessSync("scripts/__tests__/examples/dist/skip_config.yaml")).not.toThrow();
    expect(() => fs.accessSync("scripts/__tests__/examples/dist/skip_config_v2.yaml")).not.toThrow();
  });
});
