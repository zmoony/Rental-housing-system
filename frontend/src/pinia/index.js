//引入createPinia，并使用piniaPluginPersistedstate插件
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const mypinia = createPinia()
mypinia.use(piniaPluginPersistedstate)
export default mypinia
