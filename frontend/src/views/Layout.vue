<template>
  <div class="layout">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="250px" class="sidebar">
        <div class="logo">
          <h2>租房管理系统</h2>
        </div>

        <el-menu
          :default-active="$route.path"
          class="sidebar-menu"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/">
            <el-icon><House /></el-icon>
            <span>仪表盘</span>
          </el-menu-item>

          <el-menu-item index="/houses">
            <el-icon><OfficeBuilding /></el-icon>
            <span>房屋管理</span>
          </el-menu-item>

          <el-menu-item index="/rooms">
            <el-icon><HomeFilled /></el-icon>
            <span>房间管理</span>
          </el-menu-item>

          <el-menu-item index="/tenants">
            <el-icon><User /></el-icon>
            <span>租户管理</span>
          </el-menu-item>

          <el-menu-item index="/contracts">
            <el-icon><Document /></el-icon>
            <span>合同管理</span>
          </el-menu-item>

          <el-menu-item index="/utilities">
            <el-icon><Lightning /></el-icon>
            <span>水电费管理</span>
          </el-menu-item>

          <el-menu-item index="/finance">
            <el-icon><Money /></el-icon>
            <span>财务管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-container>
        <!-- 顶部导航 -->
        <el-header class="header">
          <div class="header-left">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item v-if="$route.meta.title">
                {{ $route.meta.title }}
              </el-breadcrumb-item>
            </el-breadcrumb>
          </div>

          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                <el-avatar :size="32" :src="userStore.user?.avatarUrl">
                  {{ userStore.user?.realName?.charAt(0) }}
                </el-avatar>
                <span class="username">{{ userStore.user?.realName }}</span>
                <el-icon><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <!-- 内容区域 -->
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { ElMessageBox } from "element-plus";
import { useUserStore } from "@/store/user";
import {
  House,
  OfficeBuilding,
  HomeFilled,
  User,
  Document,
  Lightning,
  Money,
  ArrowDown
} from "@element-plus/icons-vue";

const router = useRouter();
const userStore = useUserStore();

const handleCommand = async (command: string) => {
  if (command === "profile") {
    router.push("/profile");
  } else if (command === "logout") {
    try {
      await ElMessageBox.confirm("确定要退出登录吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      });

      await userStore.logoutUser();
      router.push("/login");
    } catch (error) {
      // 用户取消
    }
  }
};
</script>

<style scoped>
.layout {
  height: 100vh;
}

.sidebar {
  background-color: #304156;
  overflow: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #2b3a4b;
  color: white;
}

.logo h2 {
  margin: 0;
  font-size: 18px;
}

.sidebar-menu {
  border: none;
  height: calc(100vh - 60px);
}

.header {
  background-color: white;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header-left {
  flex: 1;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 0 10px;
}

.username {
  margin: 0 8px;
  color: #333;
}

.main-content {
  background-color: #f5f5f5;
  padding: 20px;
}
</style>
