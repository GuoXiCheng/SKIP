const fs = require('fs');
const LATEST_VERSION = "2.1.1";

(() => {
    const targetPath = 'docs/.vitepress/dist';
    if (!fs.existsSync(targetPath)) {
        console.error('Please run `npm run docs:build` first.');
        process.exit(1);
    }

    fs.writeFileSync(`${targetPath}/latest_version.txt`, LATEST_VERSION);
    fs.copyFileSync(`apk/SKIP-v${LATEST_VERSION}.apk`, `${targetPath}/SKIP-v${LATEST_VERSION}.apk`);
    fs.copyFileSync('app/src/main/assets/skip_config.yaml', `${targetPath}/skip_config.yaml`);
    fs.copyFileSync('app/src/main/assets/skip_config_v2.yaml', `${targetPath}/skip_config_v2.yaml`);

    fs.copyFileSync('apk/OneClick-v1.0.apk', `${targetPath}/OneClick-v1.0.apk`);
})();