export default {
  testTimeout: 30000, // 设置测试超时时间
  preset: "ts-jest",
  testEnvironment: "node",
  collectCoverage: true, // 开启测试覆盖率
  coverageDirectory: "coverage", // 指定覆盖率报告输出目录
  collectCoverageFrom: [
    // 指定需要收集覆盖率的文件
    "scripts/**/*.ts", // 包括 src 目录下所有的 TypeScript 文件
    "!scripts/**/*.d.ts", // 排除 TypeScript 声明文件
    "!scripts/__tests__/**/*.ts",
  ],
  testMatch: ["<rootDir>/scripts/__tests__/*.test.ts"], // 指定测试文件的匹配规则
  testPathIgnorePatterns: [], // 指定需要忽略的测试文件
};
