const fs = require('fs');
const filename = 'skip_config_v1.json'

// 读取本地文件
const rawData = fs.readFileSync(filename, 'utf8');

// 解析JSON数据为JavaScript对象
const data = JSON.parse(rawData);

// 使用sort方法进行排序
data.sort(function(a, b) {
  var packageNameA = a.package_name.toUpperCase();
  var packageNameB = b.package_name.toUpperCase();

  if (packageNameA < packageNameB) {
    return -1;
  }
  if (packageNameA > packageNameB) {
    return 1;
  }

  return 0; // 两个package_name相等
});

// 输出排序后的数据
fs.writeFileSync(`./${filename}`, JSON.stringify(data, null, 2))