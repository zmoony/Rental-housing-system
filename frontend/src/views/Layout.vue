<template>
  <div class="mobile-layout">
    <el-container class="mobile-layout-main">
      <!-- 侧边导航 -->
      <el-aside
          :width="drawerVisible ? '250px' : '60px'"
          class="mobile-drawer"
          :class="{ 'drawer-collapsed': !drawerVisible }"
      >
        <div class="drawer-header">
          <el-avatar :size="50" :src="header" />
          <div v-show="drawerVisible" class="user-info">
            <div class="username">管理员</div>
            <div class="user-role">超级管理员</div>
          </div>
        </div>

        <div class="drawer-content">
          <!-- 主要功能 -->
          <div class="menu-section">
            <div v-show="drawerVisible" class="section-label">主要功能</div>
            <div class="menu-items">
              <div
                  v-for="item in mainMenuItems"
                  :key="item.path"
                  class="menu-item"
                  :class="{ active: activeMenu === item.path }"
                  @click="navigateTo(item.path, item.title)"
              >
                <div class="menu-icon" :style="{ background: item.color }">
                  <el-icon><component :is="item.icon" /></el-icon>
                  <div v-if="!drawerVisible && item.badge" class="icon-badge">{{ item.badge }}</div>
                </div>
                <span v-if="drawerVisible" class="menu-text">{{ item.title }}</span>
                <el-badge v-if="drawerVisible && item.badge" :value="item.badge" class="menu-badge" />
              </div>
            </div>
          </div>

          <!-- 工具功能 -->
          <div class="menu-section">
            <div v-show="drawerVisible" class="section-label">工具功能</div>
            <div class="menu-items">
              <div
                  v-for="item in toolMenuItems"
                  :key="item.path"
                  class="menu-item"
                  @click="navigateTo(item.path, item.title)"
              >
                <div class="menu-icon" :style="{ background: item.color }">
                  <el-icon><component :is="item.icon" /></el-icon>
                </div>
                <span v-if="drawerVisible" class="menu-text">{{ item.title }}</span>
              </div>
            </div>
          </div>

          <!-- 底部操作 -->
          <div class="drawer-footer">
            <el-button type="primary" :icon="drawerVisible ? Back : Right" circle @click="toggleDrawer" />
          </div>
        </div>
      </el-aside>

      <el-container>
        <el-header class="mobile-header">
          <div class="mobile-header-content">
            <el-button @click="toggleDrawer" :icon="Menu" text />
            <header class="modern-header">
              <div class="title-group">
                <el-image :src="logo" class="logo-icon"/>
                <h1>租房管理系统</h1>
              </div>
            </header>

            <el-dropdown>
              <el-button :icon="MoreFilled" text />
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="changePWD">修改密码</el-dropdown-item>
                  <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        <!-- 主内容区 -->
        <el-main class="mobile-main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>

    <ChangePWD v-if="changePWDFlag" :visiable="changePWDFlag" @close="closeChangePWD"/>


  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  Menu,
  MoreFilled,
  House,
  HomeFilled,
  User,
  Document,
  DataLine,
  Money,
  Bell,
  ChatDotRound,
  Plus,
  Edit,
  Upload,
  Download,
  Lightning,
  OfficeBuilding,
  Wallet, 
  Back,
  Right
} from '@element-plus/icons-vue'
import header from '@/assets/images/header.png'
import logo from '@/assets/images/logo.svg'
import {useUserStore} from "@/store/user";
import {ElMessageBox} from "element-plus";

const route = useRoute()
const router = useRouter()

const drawerVisible = ref(false)
const showQuickActions = ref(false)
const currentPageTitle = ref('首页')

const mainMenuItems = ref([
  {
    path: '/',
    title: '仪表盘',
    icon: House,
    color: 'linear-gradient(45deg, #667eea, #764ba2)'
  },
  {
    path: '/houses',
    title: '房屋管理',
    icon: OfficeBuilding,
    color: 'linear-gradient(45deg, #f093fb, #f5576c)',
    badge: 5
  },
  {
    path: '/tenants',
    title: '房间管理',
    icon: HomeFilled,
    color: 'linear-gradient(45deg, #4facfe, #00f2fe)'
  },
  {
    path: '/tenants',
    title: '租户管理',
    icon: User,
    color: 'linear-gradient(45deg, #43e97b, #38f9d7)'
  },
  {
    path: '/utilities',
    title: '水电费管理',
    icon: Lightning,
    color: 'linear-gradient(45deg, #fa709a, #fee140)'
  }
])

const toolMenuItems = ref([
  {
    path: '/contracts',
    title: '合同管理',
    icon: Document,
    color: 'linear-gradient(45deg, #a8edea, #fed6e3)'
  },
  {
    path: '/finance',
    title: '财务管理',
    icon: Money,
    color: 'linear-gradient(45deg, #ffecd2, #fcb69f)'
  }

])


const activeMenu = computed(() => route.path)

const toggleDrawer = () => {
  drawerVisible.value = !drawerVisible.value
}

const navigateTo = (path, title) => {
  currentPageTitle.value = title
  router.push(path)
  drawerVisible.value = false
}

const changePWDFlag = ref(false)
const changePWD = ()=>{
  changePWDFlag.value = true
}
const closeChangePWD = ()=>{
  changePWDFlag.value = false
}

const logout = ()=>{
  ElMessageBox.confirm('确定是否退出平台', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 确保用户存储被清除
    const userStore = useUserStore();
    userStore.logoutUser();

    // 使用 nextTick 确保在下次 DOM 更新后执行路由跳转
    router.push('/login');
  }).catch(() => {
    // 用户取消操作
    console.log('用户取消退出');
  });
}

</script>

<style scoped>
.mobile-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.mobile-header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
}

.mobile-header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 16px;
}
.mobile-layout-main{
  height: 100%;
}

.mobile-drawer{
  display: flex;
  flex-direction: column;
  background: #fff;
  max-width: 280px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  transition: width 0.3s ease;
  overflow: hidden;
  flex-shrink: 0;
}

.mobile-title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
}
.modern-header {
  background: #ffffff;
  border-bottom: 1px solid #e8eaed;
  padding: 16px 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo-section {
  display: flex;
  align-items: center;
}

.logo-icon {
  width: 24px;
  height: 24px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
}

.title-group{
  display: flex;
  gap: 8px;
  align-items: center;
  justify-content: flex-start;
}

.title-group h1 {
  font-size: 22px;
  font-weight: 600;
  color: rgba(32, 111, 225, 0.93);
  margin: 0;
}






.drawer-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px 0;
}

.user-info {
  flex: 1;
  transition: opacity 0.3s ease;
}

.username {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
}

.user-role {
  font-size: 12px;
  color: #7f8c8d;
  margin-top: 4px;
}

.drawer-content {
  display: flex;
  flex-direction: column;
  flex: 1;
  position: relative;
}

.menu-section {
  margin-bottom: 24px;
}

.section-label {
  font-size: 12px;
  color: #95a5a6;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 12px;
  padding: 0 4px;
  transition: opacity 0.3s ease;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 4px;
}

.menu-item:hover {
  background: #f8f9fa;
}

.menu-item.active {
  background: #e3f2fd;
  color: #1976d2;
}

.menu-icon {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 16px;
  position: relative;
}

.menu-text {
  flex: 1;
  font-size: 14px;
  transition: opacity 0.3s ease;
}

.menu-badge {
  margin-left: auto;
}

.icon-badge {
  position: absolute;
  top: -6px;
  right: -6px;
  background-color: #ff4d4f;
  color: white;
  border-radius: 10px;
  min-width: 16px;
  height: 16px;
  font-size: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 4px;
}

.drawer-footer {
  position: absolute;
  bottom: 24px;
  left:50%;
  transform: translateX(-50%);
}

.mobile-main {
  flex: 1;
  background: #f5f5f5;
  position: relative;
  padding: 16px;
}

.fab-container {
  position: fixed;
  right: 20px;
  bottom: 80px;
  z-index: 999;
}

.fab-button {
  width: 56px;
  height: 56px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.fab-menu {
  position: absolute;
  right: 0;
  bottom: 70px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.fab-action {
  width: 44px;
  height: 44px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.fab-menu-enter-active,
.fab-menu-leave-active {
  transition: all 0.3s ease;
}

.fab-menu-enter-from,
.fab-menu-leave-to {
  opacity: 0;
  transform: translateY(20px);
}

.mobile-footer {
  background: #fff;
  border-top: 1px solid #e8e8e8;
  padding: 0;
}

.tab-bar {
  display: flex;
  height: 100%;
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: color 0.3s;
  gap: 4px;
}

.tab-item.active {
  color: #1976d2;
}

.tab-text {
  font-size: 10px;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .mobile-header-content {
    padding: 0 12px;
  }

  .mobile-main {
    padding: 12px;
  }

  .fab-container {
    right: 16px;
    bottom: 76px;
  }
}
</style>