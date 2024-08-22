import DefaultTheme from 'vitepress/theme'
import './custom.css'
import 'element-plus/dist/index.css'
import ElementPlus from "element-plus";
import 'element-plus/theme-chalk/dark/css-vars.css'
import '../index.css'

export default {
    ...DefaultTheme,
    enhanceApp({ app }) {
        app.use(ElementPlus)
    }
}