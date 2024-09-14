import fs from "node:fs";

interface ReleaseFileOptions {
  apkPath: string;
  releasePath: string;
}

export function releaseFile(options: ReleaseFileOptions) {
  const { apkPath, releasePath } = options;
  if (!fs.existsSync(apkPath) || !fs.existsSync(releasePath)) {
    throw new Error("apk path and release path should exist");
  }

  const apkFileList = fs.readdirSync(apkPath);

  if (apkFileList.length !== 1) {
    throw new Error("apk path should contain only one file");
  }

  const apkFile = apkFileList[0];
  if (!apkFile.endsWith(".apk")) {
    throw new Error("apk file should end with .apk");
  }

  const regex = /SKIP-v(\d+\.\d+\.\d+)\.apk/;
  const match = apkFile.match(regex);
  if (!match) {
    throw new Error("apk file should contain version number");
  }

  const apkVersion = match[1];

  // 写入 apkVersion 到 releasePath
  fs.writeFileSync(`${releasePath}/latest_version.txt`, apkVersion);
  // 复制 apkFile 到 releasePath
  fs.copyFileSync(`${apkPath}/${apkFile}`, `${releasePath}/${apkFile}`);
  // 复制 config
  fs.copyFileSync("app/src/main/assets/skip_config.yaml", `${releasePath}/skip_config.yaml`);
  fs.copyFileSync("app/src/main/assets/skip_config_v2.yaml", `${releasePath}/skip_config_v2.yaml`);
  fs.copyFileSync("app/src/main/assets/skip_config_v3.yaml", `${releasePath}/skip_config_v3.yaml`);
}
