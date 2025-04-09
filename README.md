![GitHub](https://img.shields.io/github/license/GuoXiCheng/SKIP) ![GitHub all releases](https://img.shields.io/github/downloads/GuoXiCheng/SKIP/total) ![GitHub Repo stars](https://img.shields.io/github/stars/GuoXiCheng/SKIP)

## SKIP 介绍

SKIP 是一款免费开源的安卓应用，旨在利用安卓无障碍服务帮助用户快速点击 APP 开屏广告的跳过按钮，让你的使用体验更加流畅。

## 主界面预览

<img src="https://skip.guoxicheng.top//images/main-interface-light.png" alt="https://skip.guoxicheng.top//images/main-interface-light.png" style="width: 30%;" />

## 文档

[SKIP 文档](https://skip.guoxicheng.top/)

## 许可证

[GPL-3.0 license](https://github.com/GuoXiCheng/SKIP/blob/main/LICENSE)

## 补充说明

[免责声明](https://github.com/GuoXiCheng/SKIP/blob/main/DISCLAIMER.md)

## Star History

[![Star History Chart](https://api.star-history.com/svg?repos=GuoXiCheng/SKIP&type=Date)](https://star-history.com/#GuoXiCheng/SKIP&Date)

## 自定义规则订阅

<table>
  <thead>
    <tr>
      <th>规则描述</th>
      <th>订阅链接</th>
      <th>作者</th>
      <th>源码地址</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>默认订阅</td>
      <td><code>https://skip.guoxicheng.top/skip_config_v3.yaml</code></td>
      <td><a href="https://github.com/GuoXiCheng">GuoXiCheng</a></td>
      <td>
        <a href="https://github.com/GuoXiCheng/SKIP/blob/main/app/src/main/assets/skip_config_v3.yaml"
          >skip_config_v3</a
        >
      </td>
    </tr>
    <!-- 添加你的订阅 -->
  </tbody>
</table>

> 添加自定义订阅规则订阅请修改[README.md](https://github.com/GuoXiCheng/SKIP/edit/main/README.md)提交 PR。
{id:666,name:'AIsouler的GKD订阅-禁止传播',version:197,author:'AIsouler',checkUpdateUrl:'./AIsouler_gkd.version.json5',supportUri:'https://github.com/AIsouler/GKD_subscription/issues/new/choose',categories:[{key:0,name:'开屏广告'},{key:1,name:'青少年模式'},{key:2,name:'更新提示'},{key:3,name:'评价提示'},{key:4,name:'通知提示'},{key:5,name:'权限提示'},{key:6,name:'局部广告'},{key:7,name:'全屏广告'},{key:8,name:'分段广告'},{key:9,name:'功能类'},{key:10,name:'其他'}],globalGroups:[{key:0,name:'开屏广告-全局',desc:'关闭打开应用时的开屏广告',order:-10,fastQuery:true,matchTime:10000,actionMaximum:2,resetMatch:'app',actionCdKey:0,actionMaximumKey:0,priorityTime:10000,disableIfAppGroupMatch:'开屏广告',rules:[{key:0,action:'clickCenter',excludeMatches:'[text*="搜索" || text^="猜你" || text="历史记录" || text$="在搜" || text*="退款详情"][text.length>3 && text.length<6][visibleToUser=true]',anyMatches:['[text*="跳过"][text.length<10][visibleToUser=true]','[childCount=0][visibleToUser=true][(text.length<10 && (text*="跳过" || text*="跳過" || text~="(?is).*skip.*")) || (vid~="(?is).*skip.
