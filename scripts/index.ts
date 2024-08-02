(() => {
  const args = process.argv.slice(2);
  const params: Record<string, string> = {};
  args.forEach((arg) => {
    const [key, value] = arg.split("=");
    params[key.substring(2)] = value;
  });

  if (params.func === "release-file") {
    const { releaseFile } = require("./release-file");
    releaseFile(params);
  }
})();
