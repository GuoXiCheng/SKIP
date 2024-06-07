# 布局查看

使用布局查看器可以探查屏幕节点的布局结构，例如使用[web-editor](https://github.com/alibaba/web-editor)或 Android Studio 的布局查看器。

## 应用布局参考图

当选中想要的目标节点时，会显示该节点的布局结构，参考如下：

![应用布局参考图](/layout-reference.png)

### 配置 skipTexts 和 skipIds

text 呈现的值，可以对应到配置文件中的 text

resourceId 呈现的值，可以对应到配置文件中的 id

### 配置 skipBounds

skipBounds 的值是需要计算得到的，可能长得像这样

```yaml
- packageName: com.xxx.xxx
  skipBounds:
    - bound: 1223,196,1384,308
      resolution: 1440,3024
```

其中的 1440,3024 是你当前手机屏幕的最大宽高。

其中的 1223,196,1384,308 分别表示一个节点在屏幕中的: left,top,right,bottom

参考上图中 rect 的信息: left=x,top=y,right=x+width,bottom=y+height

![Android屏幕节点布局](/android-rect.png)
