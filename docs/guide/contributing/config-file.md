# 配置文件

配置文件是一个 YAML 文件，用于根据不同的 App 应用不同的配置。

配置文件位置：[配置文件](https://github.com/GuoXiCheng/SKIP/blob/main/app/src/main/assets/skip_config_v2.yaml)

## 配置文件选项

### packageName（必填）

应用包名称。

在 SKIP 的应用白名单中，可以查看应用的包名。

### skipTexts （选填）

根据文本匹配，当节点的文本**包含**指定字符串时，执行点击动作。可以配置多组。

在手机屏幕上看到的文本是什么就可以填什么，但是实际节点 text = null 时，会失效。

```yaml
- packageName: com.xxx.xxx
  skipTexts:
    - text: 跳过广告
      length: 4
```

### skipIds （选填）

根据 id 匹配，当节点的 id **等于**指定字符串时，执行点击动作。可以配置多组。

id 需要使用 Android 布局分析工具查询，但实际节点 id = null 时，不可用。

```yaml
- packageName: com.xxx.xxx
  skipIds:
    - id: com.xxx.xxx:id/view_count_down
```

### skipBounds（选填）

根据 bounds 匹配，当所设定的 bounds **包含**节点的 bounds 时，执行点击动作。可以配置多组。

一般可交互的节点都会具有 bounds，需要使用 Android 布局分析工具查询。
