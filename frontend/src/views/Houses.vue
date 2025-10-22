<template>
  <div class="houses-page">
    <div class="page-header">
      <h2>房屋管理</h2>
      <el-button type="primary">添加房屋</el-button>
    </div>

    <div class="content">
      <el-table :data="houses" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="houseName" label="房屋名称" />
        <el-table-column prop="address" label="地址" />
        <el-table-column prop="totalRooms" label="房间数" width="100" />
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

const houses = ref([
  {
    id: 1,
    houseName: "阳光公寓A栋",
    address: "北京市朝阳区阳光路123号",
    totalRooms: 20,
    status: "AVAILABLE"
  }
]);

const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    AVAILABLE: "success",
    OCCUPIED: "warning",
    MAINTENANCE: "danger"
  };
  return statusMap[status] || "info";
};

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    AVAILABLE: "可用",
    OCCUPIED: "已占用",
    MAINTENANCE: "维护中"
  };
  return statusMap[status] || status;
};
</script>

<style scoped>
.houses-page {
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
