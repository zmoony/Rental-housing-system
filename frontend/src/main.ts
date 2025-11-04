import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import mypinia from './pinia/index'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import './assets/styles/theme.css'
import '@/assets/font/font.scss'

// 引入Font Awesome 6
const fontAwesomeCSS = document.createElement('link')
fontAwesomeCSS.rel = 'stylesheet'
fontAwesomeCSS.href = 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css'
document.head.appendChild(fontAwesomeCSS)

const app = createApp(App)

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(router)
app.use(mypinia)
app.use(ElementPlus, {
  locale: zhCn,
})

app.mount('#app')
