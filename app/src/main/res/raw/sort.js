const fs = require("fs").promises;
const filename = "skip_config_v1.json";

async function main() {
  try {
    // 读取本地文件
    const rawData = await fs.readFile(filename, "utf8");

    // 解析JSON数据为JavaScript对象
    const data = JSON.parse(rawData);

    // 使用sort方法进行排序
    data.sort((a, b) => a.package_name.localeCompare(b.package_name, undefined, { sensitivity: 'base' }));

    // 对象属性按字母顺序排序并输出
    const sortedData = data.map(item => {
      return Object.fromEntries(Object.entries(item).sort());
    });

    // 输出排序后的数据
    await fs.writeFile(`./${filename}`, JSON.stringify(sortedData, null, 2));
    
    console.log("数据已排序并写入文件成功！");
  } catch (error) {
    console.error("发生错误:", error);
  }
}

main();
