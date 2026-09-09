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

<a href="https://www.star-history.com/?repos=GuoXiCheng%2FSKIP&type=date&legend=top-left">
 <picture>
   <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/chart?repos=GuoXiCheng/SKIP&type=date&theme=dark&legend=top-left&sealed_token=A2-bonTSUbt1ikMAft77ZB2wFf5oAbd_v8rSpnTATyoZZu3dSXnBgYJvA4ryBg3m5I38hpeT1Afcw1jLrmpvOUCBLNlIPxcactLjPbIKL_C3JLEs-Je57yQ7yh2QiGPzdhKve1wc_zVKuOZ43YbfHOzedGtx33fbhL5yQFQ2kDxNAC7bXe-wHpECR8qC" />
   <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/chart?repos=GuoXiCheng/SKIP&type=date&legend=top-left&sealed_token=A2-bonTSUbt1ikMAft77ZB2wFf5oAbd_v8rSpnTATyoZZu3dSXnBgYJvA4ryBg3m5I38hpeT1Afcw1jLrmpvOUCBLNlIPxcactLjPbIKL_C3JLEs-Je57yQ7yh2QiGPzdhKve1wc_zVKuOZ43YbfHOzedGtx33fbhL5yQFQ2kDxNAC7bXe-wHpECR8qC" />
   <img alt="Star History Chart" src="https://api.star-history.com/chart?repos=GuoXiCheng/SKIP&type=date&legend=top-left&sealed_token=A2-bonTSUbt1ikMAft77ZB2wFf5oAbd_v8rSpnTATyoZZu3dSXnBgYJvA4ryBg3m5I38hpeT1Afcw1jLrmpvOUCBLNlIPxcactLjPbIKL_C3JLEs-Je57yQ7yh2QiGPzdhKve1wc_zVKuOZ43YbfHOzedGtx33fbhL5yQFQ2kDxNAC7bXe-wHpECR8qC" />
 </picture>
</a>

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

> > - 发布你自己的规则订阅源：在本表格中添加一行（订阅链接 / 作者 / 源码地址），然后提交 PR。
> >
> > - 为某个 App 补充跳过规则：在 [app/src/main/assets/skip_config_v3.yaml](https://github.com/GuoXiCheng/SKIP/blob/main/app/src/main/assets/skip_config_v3.yaml)
> >   末尾追加一条规则，然后提交 PR。若同时更新同目录的 skip_config_v3.json（文档用的 JSON 镜像）更佳；
> >   建议把「布局检查」导出的抓取包一并放入 capture/ 目录，供他人复核。
> >
> > > 注意：skipBounds 使用绝对像素坐标，仅对抓取时的设备分辨率有效，添加规则时请在 desc 中注明分辨率。
