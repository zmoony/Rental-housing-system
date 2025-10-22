<template>
  <div class="profile-page">
    <div class="page-header">
      <h2>个人中心</h2>
    </div>

    <div class="profile-content">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card>
            <template #header>
              <span>个人信息</span>
            </template>
            <div class="profile-info">
              <el-avatar :size="80" :src="userInfo.avatarUrl">
                {{ userInfo.realName?.charAt(0) }}
              </el-avatar>
              <div class="user-details">
                <h3>{{ userInfo.realName }}</h3>
                <p>{{ userInfo.email }}</p>
                <p>{{ userInfo.phone }}</p>
                <el-tag :type="getRoleType(userInfo.role)">
                  {{ getRoleText(userInfo.role) }}
                </el-tag>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="16">
          <el-card>
            <template #header>
              <span>账户设置</span>
            </template>
            <el-form :model="userInfo" label-width="100px">
              <el-form-item label="用户名">
                <el-input v-model="userInfo.username" disabled />
              </el-form-item>
              <el-form-item label="真实姓名">
                <el-input v-model="userInfo.realName" />
              </el-form-item>
              <el-form-item label="邮箱">
                <el-input v-model="userInfo.email" />
              </el-form-item>
              <el-form-item label="电话">
                <el-input v-model="userInfo.phone" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary">保存修改</el-button>
                <el-button>重置</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

const userInfo = ref({
  id: 1,
  username: "admin",
  realName: "系统管理员",
  email: "admin@zufang.com",
  phone: "13800138000",
  role: "ADMIN",
  avatarUrl: ""
});

const getRoleType = (role: string) => {
  const roleMap: Record<string, string> = {
    ADMIN: "danger",
    LANDLORD: "warning",
    TENANT: "success"
  };
  return roleMap[role] || "info";
};

const getRoleText = (role: string) => {
  const roleMap: Record<string, string> = {
    ADMIN: "管理员",
    LANDLORD: "房东",
    TENANT: "租户"
  };
  return roleMap[role] || role;
};
</script>

<style scoped>
.profile-page {
  background: white;
  border-radius: 8px;
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e6e6e6;
}

.page-header h2 {
  margin: 0;
  color: #333;
}

.profile-info {
  text-align: center;
}

.user-details {
  margin-top: 20px;
}

.user-details h3 {
  margin: 10px 0;
  color: #333;
}

.user-details p {
  margin: 5px 0;
  color: #666;
}
</style>
