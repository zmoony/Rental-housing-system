<template>
  <div class="rooms-page">
    <div class="page-header">
      <h2>房间管理</h2>
      <el-button type="primary">添加房间</el-button>
    </div>

    <div class="content">
      <el-table :data="rooms" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="roomNumber" label="房间号" width="120" />
        <el-table-column prop="roomType" label="房间类型" width="120" />
        <el-table-column prop="monthlyRent" label="月租金" width="120">
          <template #default="{ row }"> ¥{{ row.monthlyRent }} </template>
        </el-table-column>
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

const rooms = ref([
  {
    id: 1,
    roomNumber: "A101",
    roomType: "单间",
    monthlyRent: 1500,
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
.rooms-page {
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
