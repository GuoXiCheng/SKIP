// .vitepress/config.js
export default {
    // 站点级选项
    title: 'SKIP',
    description: 'Just playing around.',
    base: "/",
    themeConfig: {
        nav: [
            { text: "首页", link: "/" },
            { text: "指南", link: "/guide/intro/what-is-skip" },
            { text: "下载", link: "/download/app-download/index" }
        ],
        sidebar: {
            "/guide/": [
                {
                    text: "简介",
                    items: [{
                        text: "什么是 SKIP",
                        link: "/guide/intro/what-is-skip"
                    }]
                }, {
                    text: "使用方法",
                    items: [{
                        text: "启用无障碍服务",
                        link: "/guide/usage/enable-accessibility-service"
                    }, {
                        text: "后台进程保活",
                        link: "/guide/usage/background-process-keep-alive"
                    }]
                }
            ],
            "/download/": [
                {
                    text: "下载 APP",
                    items: [{
                        text: "下载方式",
                        link: "/download/app-download/index"
                    }]
                }
            ]
        }
    }
}