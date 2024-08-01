import fs from "node:fs";
import yaml from "js-yaml";
import { z } from "zod";

const skipIdsSchema = z.object({
  id: z
    .string({ message: "id must be a string" })
    .refine((val) => val.trim() === val, { message: "id must not have leading or trailing whitespace" }),
});

const skipTextsSchema = z
  .object({
    text: z
      .string({ message: "text must be a string" })
      .optional()
      .refine((val) => val?.trim() === val, { message: "text must not have leading or trailing whitespace" }),
    length: z
      .number({ message: "length must be a number" })
      .nonnegative({ message: "length must be non-negative" })
      .optional(),
  })
  .strict();

const skipBoundsSchema = z.object({
  bound: z.string({ message: "bound must be a string" }).refine((val) => {
    const rect = val.split(",");
    return rect.length === 4 && rect.every((v) => !isNaN(parseFloat(v)));
  }, "bound must have four numeric values"),
  resolution: z.string({ message: "resolution must be a string" }).refine((val) => {
    const size = val.split(",");
    return size.length === 2 && size.every((v) => !isNaN(parseFloat(v)));
  }, "resolution must have two numeric values"),
});

const skipConfigSchema = z
  .object({
    packageName: z
      .string({ message: "packageName must be a string" })
      .refine((val) => val.trim() === val, { message: "packageName must not have leading or trailing whitespace" }),

    skipIds: z.array(skipIdsSchema, { message: "skipIds must be an array" }).optional(),
    skipTexts: z.array(skipTextsSchema, { message: "skipTexts must be an array" }).optional(),
    skipBounds: z.array(skipBoundsSchema, { message: "skipBounds must be an array" }).optional(),
  })
  .strict()
  .refine((data) => data.skipIds || data.skipTexts || data.skipBounds, "skipIds, skipTexts, or skipBounds is required");

// 读取并验证 yaml 文件
export function yamlCheck(yamlPath: string) {
  try {
    const doc = yaml.load(fs.readFileSync(yamlPath, "utf8")) as any[];
    const parsedDoc = doc.map((config) => skipConfigSchema.parse(config));
    return parsedDoc;
  } catch (e) {
    throw e;
  }
}
