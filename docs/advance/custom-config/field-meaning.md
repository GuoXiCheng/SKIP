# 可配置字段

## packageName `必填`

### skipTexts `可选`

#### text `必填`

根据节点的`text`属性跳过指定的文本。

#### length `可选`

判断节点的`text`属性的长度是否小于等于`length`的值。

#### activityName `可选`

判断当前页面的`activityName`是否等于`activityName`的值。相等时才会执行。

#### click `可选`

点击的坐标，格式为`x,y`。

默认情况下，会点击找到节点的中心点。可以通过`click`属性可额外指定点击的坐标，而不点击所找到节点的中心点。

#### 参考

```yaml
- packageName: com.android.skip
  skipTexts:
    - text: 布局检查
      length: 4
      activityName: com.android.skip.ui.inspect.InspectActivity
      click: 123,456
```

### skipIds `可选`

#### id `必填`

根据节点的`viewIdResourceName`属性搜索指定的节点。

#### activityName `可选`

#### click `可选`

#### 参考

```yaml
- packageName: com.android.skip
  skipIds:
    - id: android:id/navigationBarBackground
      activityName: com.android.skip.ui.inspect.InspectActivity
      click: 123,456
```

### skipBounds `可选`

#### bound `必填`

根据节点的`boundsInScreen`属性搜索指定的节点，即：`bound(left,top,right,bottom)`。

#### activityName `可选`

#### click `可选`

#### 参考

```yaml
- packageName: com.android.skip
  skipBounds:
    - bound: 53,639,1387,849
      activityName: com.android.skip.ui.inspect.InspectActivity
      click: 123,456
```
