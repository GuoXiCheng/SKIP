import { withMermaid } from "vitepress-plugin-mermaid";
import { defineConfig } from "vitepress";

export default defineConfig(
  withMermaid({
    // 站点级选项
    title: "SKIP",
    description: "SKIP APP Docs",
    base: "/",
    lastUpdated: true,
    head: [
      ["link", { rel: "icon", type: "image/x-icon", href: "/images/favicon.ico" }],
      ["script", { src: "/js/baidu-analytics.js", async: true }],
    ],
    themeConfig: {
      logo: "/images/favicon.ico",
      outline: {
        level: "deep",
      },
      nav: [
        { text: "首页", link: "/" },
        { text: "指南", link: "/guide/intro/what-is-skip" },
        { text: "进阶", link: "/advance/layout-inspect/intro" },
        {
          text: "辅助功能",
          items: [
            { text: "布局检查", link: "/inspect/index", target: "_blank" },
            // { text: "可视化配置", link: "/visual/index", target: "_blank" },
          ],
        },
      ],
      sidebar: {
        "/guide/": [
          {
            text: "简介",
            items: [
              {
                text: "什么是 SKIP",
                link: "/guide/intro/what-is-skip",
              },
              {
                text: "下载 APP",
                link: "/guide/intro/download-app",
              },
              {
                text: "开始使用",
                link: "/guide/intro/getting-started",
              },
            ],
          },
          {
            text: "主要功能",
            items: [
              {
                text: "应用保活",
                link: "/guide/keep-alive/intro",
              },
              {
                text: "应用白名单",
                link: "/guide/white-list/intro",
              },
              {
                text: "设置",
                link: "/guide/settings/intro",
              },
              {
                text: "关于",
                link: "/guide/about/intro",
              },
            ],
          },
          // {
          //   text: "使用方法",
          //   items: [
          //     {
          //       text: "启用无障碍服务",
          //       link: "/guide/usage/enable-accessibility-service",
          //     },
          //     {
          //       text: "后台进程保活",
          //       link: "/guide/usage/background-process-keep-alive",
          //     },
          //   ],
          // },
          // {
          //   text: "贡献指南",
          //   items: [
          //     {
          //       text: "核心逻辑",
          //       link: "/guide/contributing/core-logic",
          //     },
          //     {
          //       text: "配置文件",
          //       link: "/guide/contributing/config-file",
          //     },
          //     {
          //       text: "布局查看",
          //       link: "/guide/contributing/layout-view",
          //     },
          //   ],
          // },
        ],
        "/advance/": [
          {
            text: "布局检查",
            items: [
              {
                text: "布局检查功能",
                link: "/advance/layout-inspect/intro",
              },
              {
                text: "使用方法",
                link: "/advance/layout-inspect/usage",
              },
            ],
          },
          {
            text: "自定义配置",
            items: [
              {
                text: "自定义配置功能",
                link: "/advance/custom-config/intro",
              },
              {
                text: "可配置字段数据结构",
                link: "/advance/custom-config/data-structure",
              },
              {
                text: "可配置字段含义",
                link: "/advance/custom-config/field-meaning",
              },
              {
                text: "完整的配置参考",
                link: "/advance/custom-config/full-reference",
              },
            ],
          },
        ],
      },

      socialLinks: [{ icon: "github", link: "https://github.com/GuoXiCheng/SKIP" }],
    },
  })
);
