<template>
  <div class="contracts-page">
    <div class="page-header">
      <h2>合同管理</h2>
      <el-button type="primary">创建合同</el-button>
    </div>

    <div class="content">
      <el-table :data="contracts" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="contractNumber" label="合同编号" width="150" />
        <el-table-column prop="tenantName" label="租户" width="120" />
        <el-table-column prop="roomNumber" label="房间" width="100" />
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
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
            <el-button size="small" type="primary">续签</el-button>
            <el-button size="small" type="danger">终止</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

const contracts = ref([
  {
    id: 1,
    contractNumber: "HT20240115001",
    tenantName: "张三",
    roomNumber: "A101",
    startDate: "2024-01-01",
    endDate: "2024-12-31",
    monthlyRent: 1500,
    status: "ACTIVE"
  }
]);

const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    ACTIVE: "success",
    EXPIRED: "warning",
    TERMINATED: "danger"
  };
  return statusMap[status] || "info";
};

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    ACTIVE: "生效中",
    EXPIRED: "已过期",
    TERMINATED: "已终止"
  };
  return statusMap[status] || status;
};
</script>

<style scoped>
.contracts-page {
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
