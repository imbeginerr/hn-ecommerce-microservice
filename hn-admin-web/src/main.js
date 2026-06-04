import { createApp } from 'vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import App from './App.vue';
import router from './routers';
import './assets/tailwind.css';
import './assets/styles.css';

createApp(App).use(router).use(ElementPlus).mount('#app');
