import fs from "node:fs";
import yaml from "js-yaml";

interface SkipConfig {
  packageName: string;
  skipIds?: SkipIds[];
  skipTexts?: SkipTexts[];
  skipBounds?: SkipBounds[];
}

interface SkipIds {
  id: string;
}

interface SkipTexts {
  text?: string;
  length?: number;
}

interface SkipBounds {
  bound: string;
  resolution: string;
}

export function yamlCheck(yamlPath: string) {
  try {
    const doc = yaml.load(fs.readFileSync(yamlPath, "utf8")) as SkipConfig[];
    doc.forEach((config) => checkConfig(config));
    return doc;
  } catch (e) {
    throw e;
  }
}

function checkConfig(skipConfig: SkipConfig) {
  if (!skipConfig.packageName) throw new Error("packageName is required");

  const packageName = skipConfig.packageName;
  if (typeof packageName !== "string") throw new Error("packageName must be a string");
  if (packageName.trim().length === 0) throw new Error("packageName must not be empty");
  if (packageName.trim() !== packageName) throw new Error("packageName must not have leading or trailing whitespace");

  if (skipConfig.skipIds) checkSkipIds(skipConfig.skipIds);

  if (skipConfig.skipTexts) checkSkipTexts(skipConfig.skipTexts);

  if (skipConfig.skipBounds) checkSkipBounds(skipConfig.skipBounds);

  if (!(skipConfig.skipIds || skipConfig.skipTexts || skipConfig.skipBounds)) {
    throw new Error("skipIds, skipTexts, or skipBounds is required");
  }

  const keys = ["skipIds", "skipTexts", "skipBounds", "packageName"];
  const extraKeys = Object.keys(skipConfig).filter((key) => !keys.includes(key));
  if (extraKeys.length > 0) throw new Error(`Unknown keys: ${extraKeys.join(", ")}`);
}

function checkSkipIds(skipIds: SkipIds[]) {
  if (!Array.isArray(skipIds)) throw new Error("skipIds must be an array");

  skipIds.forEach((skipId) => {
    if (!skipId.id) throw new Error("id is required");

    const id = skipId.id;
    if (typeof id !== "string") throw new Error("id must be a string");
    if (id.trim().length === 0) throw new Error("id must not be empty");
    if (id.trim() !== id) throw new Error("id must not have leading or trailing whitespace");
  });
}

function checkSkipTexts(skipTexts: SkipTexts[]) {
  if (!Array.isArray(skipTexts)) throw new Error("skipTexts must be an array");

  skipTexts.forEach((skipText) => {
    if (skipText.text) {
      const text = skipText.text;
      if (typeof text !== "string") throw new Error("text must be a string");
      if (text.trim().length === 0) throw new Error("text must not be empty");
      if (text.trim() !== text) throw new Error("text must not have leading or trailing whitespace");
    }

    if (skipText.length) {
      const length = skipText.length;
      if (typeof length !== "number") throw new Error("length must be a number");
      if (length < 0) throw new Error("length must be non-negative");
    }

    const keys = ["text", "length"];
    const extraKeys = Object.keys(skipText).filter((key) => !keys.includes(key));
    if (extraKeys.length > 0) throw new Error(`Unknown keys: ${extraKeys.join(", ")}`);
  });
}

function checkSkipBounds(skipBounds: SkipBounds[]) {
  if (!Array.isArray(skipBounds)) throw new Error("skipBounds must be an array");

  skipBounds.forEach((skipBound) => {
    if (!skipBound.bound) throw new Error("bound is required");
    if (!skipBound.resolution) throw new Error("resolution is required");

    const bound = skipBound.bound;
    if (typeof bound !== "string") throw new Error("bound must be a string");
    if (bound.trim().length === 0) throw new Error("bound must not be empty");
    if (bound.trim() !== bound) throw new Error("bound must not have leading or trailing whitespace");

    const rect = bound.split(",");
    if (rect.length !== 4) throw new Error("bound must have four comma-separated values");
    rect.forEach((value) => {
      const number = parseFloat(value);
      if (isNaN(number)) throw new Error("bound must have four numeric values");
    });

    const resolution = skipBound.resolution;
    if (typeof resolution !== "string") throw new Error("resolution must be a string");
    if (resolution.trim().length === 0) throw new Error("resolution must not be empty");
    if (resolution.trim() !== resolution) throw new Error("resolution must not have leading or trailing whitespace");

    const size = resolution.split(",");
    if (size.length !== 2) throw new Error("resolution must have two comma-separated values");
    size.forEach((value) => {
      const number = parseFloat(value);
      if (isNaN(number)) throw new Error("resolution must have two numeric values");
    });
  });
}
