<template>
  <div class="finance-page">
    <div class="page-header">
      <h2>财务管理</h2>
      <div class="header-actions">
        <el-button type="primary">生成账单</el-button>
        <el-button>导出报表</el-button>
      </div>
    </div>

    <div class="stats-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card>
            <div class="stat-item">
              <div class="stat-value">¥25,680</div>
              <div class="stat-label">本月收入</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <div class="stat-item">
              <div class="stat-value">¥1,250</div>
              <div class="stat-label">水电费收入</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <div class="stat-item">
              <div class="stat-value">3</div>
              <div class="stat-label">逾期账单</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <div class="stat-item">
              <div class="stat-value">95%</div>
              <div class="stat-label">缴费率</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div class="table-section">
      <el-table :data="rentRecords" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="tenantName" label="租户" width="120" />
        <el-table-column prop="roomNumber" label="房间" width="100" />
        <el-table-column prop="rentMonth" label="月份" width="120" />
        <el-table-column prop="rentAmount" label="租金" width="120">
          <template #default="{ row }"> ¥{{ row.rentAmount }} </template>
        </el-table-column>
        <el-table-column prop="utilityFee" label="水电费" width="120">
          <template #default="{ row }"> ¥{{ row.utilityFee }} </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="总金额" width="120">
          <template #default="{ row }"> ¥{{ row.totalAmount }} </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small">查看</el-button>
            <el-button size="small" type="primary">缴费</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

const rentRecords = ref([
  {
    id: 1,
    tenantName: "张三",
    roomNumber: "A101",
    rentMonth: "2024-01",
    rentAmount: 1500,
    utilityFee: 120,
    totalAmount: 1620,
    status: "PAID"
  }
]);

const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    PENDING: "warning",
    PAID: "success",
    OVERDUE: "danger"
  };
  return statusMap[status] || "info";
};

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    PENDING: "待缴费",
    PAID: "已缴费",
    OVERDUE: "逾期"
  };
  return statusMap[status] || status;
};
</script>

<style scoped>
.finance-page {
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

.header-actions {
  display: flex;
  gap: 10px;
}

.stats-section {
  margin-bottom: 30px;
}

.stat-item {
  text-align: center;
  padding: 20px;
}

.stat-value {
  font-size: 2rem;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 1rem;
  color: #666;
}
</style>
