import { createApp } from "vue";
import { createPinia } from "pinia";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import zhCn from "element-plus/dist/locale/zh-cn.mjs";

import App from "./App.vue";
import router from "./router";

async function bootstrap() {
  // 运行时配置：优先从 /config.json 读取
  try {
    const resp = await fetch("/config.json", { cache: "no-store" });
    if (resp.ok) {
      const cfg = await resp.json();
      (window as any).__APP_CONFIG__ = Object.assign({}, (window as any).__APP_CONFIG__ || {}, cfg);
      if (cfg.APP_TITLE) {
        document.title = cfg.APP_TITLE;
      }
    }
  } catch (e) {
    // 忽略，使用内置或 .env 配置
  }

  const app = createApp(App);

  // 注册 Element Plus 图标
  for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
  }

  app.use(createPinia());
  app.use(router);
  app.use(ElementPlus, {
    locale: zhCn
  });

  app.mount("#app");
}

bootstrap();
