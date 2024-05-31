// .vitepress/config.js
export default {
    // 站点级选项
    title: 'SKIP Docs',
    description: 'Just playing around.',
    base: "/",
    themeConfig: {
        nav: [
            { text: "首页", link: "/" },
            { text: "指南", link: "/guide/intro/what-is-skip" }
        ],
        sidebar: {
            "/guide/": [
                {
                    text: "简介",
                    items: [{
                        text: "什么是 SKIP",
                        link: "/intro/what-is-skip"
                    }]
                }
            ]
        }
    }
}