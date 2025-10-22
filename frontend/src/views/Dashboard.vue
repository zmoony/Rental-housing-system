<template>
  <div class="dashboard">
    <h2>仪表盘</h2>
    <div class="stats-grid">
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-number">12</div>
          <div class="stat-label">总房屋数</div>
        </div>
      </el-card>

      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-number">8</div>
          <div class="stat-label">已租房间</div>
        </div>
      </el-card>

      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-number">15</div>
          <div class="stat-label">活跃租户</div>
        </div>
      </el-card>

      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-number">¥25,680</div>
          <div class="stat-label">本月收入</div>
        </div>
      </el-card>
    </div>

    <div class="charts-section">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>收入趋势</span>
            </template>
            <div class="chart-placeholder">图表区域</div>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card>
            <template #header>
              <span>房间状态</span>
            </template>
            <div class="chart-placeholder">图表区域</div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
// 仪表盘逻辑
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  text-align: center;
}

.stat-content {
  padding: 20px;
}

.stat-number {
  font-size: 2.5rem;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 1rem;
  color: #666;
}

.charts-section {
  margin-top: 30px;
}

.chart-placeholder {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  border-radius: 4px;
  color: #999;
}
</style>
