<template>
  <div class="tenants-page">
    <div class="page-header">
      <h2>租户管理</h2>
      <el-button type="primary">添加租户</el-button>
    </div>

    <div class="content">
      <el-table :data="tenants" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="realName" label="姓名" width="120" />
        <el-table-column prop="phone" label="电话" width="150" />
        <el-table-column prop="occupation" label="职业" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small">查看</el-button>
            <el-button size="small" type="primary">编辑</el-button>
            <el-button size="small" type="danger">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

const tenants = ref([
  {
    id: 1,
    realName: "张三",
    phone: "13800138000",
    occupation: "程序员",
    status: "ACTIVE"
  }
]);

const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    ACTIVE: "success",
    INACTIVE: "warning",
    MOVED_OUT: "info"
  };
  return statusMap[status] || "info";
};

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    ACTIVE: "活跃",
    INACTIVE: "非活跃",
    MOVED_OUT: "已搬出"
  };
  return statusMap[status] || status;
};
</script>

<style scoped>
.tenants-page {
  background: white;
  border-radius: 8px;
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e6e6e6;
}

.page-header h2 {
  margin: 0;
  color: #333;
}
</style>
