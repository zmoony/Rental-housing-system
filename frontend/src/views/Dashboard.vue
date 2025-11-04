<template>
  <div class="dashboard">
    <div class="page-header">
      <h2>仪表盘</h2>
      <div class="header-actions">
        <el-button type="primary">
          <i class="fas fa-sync-alt"></i> 刷新数据
        </el-button>
      </div>
    </div>
    
    <div class="stats-grid">
      <div class="stat-card fade-in">
        <div class="stat-icon">
          <i class="fas fa-home"></i>
        </div>
        <div class="stat-content">
          <div class="stat-card-title">总房屋数</div>
          <div class="stat-card-value">12</div>
          <div class="stat-card-change positive">
            <i class="fas fa-arrow-up"></i> 3.48% 较上月
          </div>
        </div>
      </div>

      <div class="stat-card fade-in" style="animation-delay: 0.1s">
        <div class="stat-icon">
          <i class="fas fa-door-open"></i>
        </div>
        <div class="stat-content">
          <div class="stat-card-title">已租房间</div>
          <div class="stat-card-value">8</div>
          <div class="stat-card-change positive">
            <i class="fas fa-arrow-up"></i> 12.18% 较上月
          </div>
        </div>
      </div>

      <div class="stat-card fade-in" style="animation-delay: 0.2s">
        <div class="stat-icon">
          <i class="fas fa-users"></i>
        </div>
        <div class="stat-content">
          <div class="stat-card-title">活跃租户</div>
          <div class="stat-card-value">15</div>
          <div class="stat-card-change positive">
            <i class="fas fa-arrow-up"></i> 5.72% 较上月
          </div>
        </div>
      </div>

      <div class="stat-card fade-in" style="animation-delay: 0.3s">
        <div class="stat-icon">
          <i class="fas fa-yuan-sign"></i>
        </div>
        <div class="stat-content">
          <div class="stat-card-title">本月收入</div>
          <div class="stat-card-value">¥25,680</div>
          <div class="stat-card-change positive">
            <i class="fas fa-arrow-up"></i> 54.9% 较上月
          </div>
        </div>
      </div>
    </div>

    <div class="charts-section">
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="content slide-in-up">
            <div class="chart-header">
              <h3>收入趋势</h3>
              <div class="chart-actions">
                <el-radio-group v-model="incomeTimeRange" size="small">
                  <el-radio-button label="month">月</el-radio-button>
                  <el-radio-button label="week">周</el-radio-button>
                </el-radio-group>
              </div>
            </div>
            <div class="chart-placeholder">
              <img src="https://images.unsplash.com/photo-1551288049-bebda4e38f71?ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80" alt="收入趋势图表" style="width: 100%; height: 100%; object-fit: cover;">
            </div>
          </div>
        </el-col>

        <el-col :span="12">
          <div class="content slide-in-up" style="animation-delay: 0.2s">
            <div class="chart-header">
              <h3>房间状态</h3>
              <div class="chart-actions">
                <el-select v-model="selectedHouse" size="small" placeholder="选择房屋">
                  <el-option label="所有房屋" value="all"></el-option>
                  <el-option label="A栋" value="A"></el-option>
                  <el-option label="B栋" value="B"></el-option>
                </el-select>
              </div>
            </div>
            <div class="chart-placeholder">
              <img src="https://images.unsplash.com/photo-1523961131990-5ea7c61b2107?ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80" alt="房间状态图表" style="width: 100%; height: 100%; object-fit: cover;">
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

// 仪表盘逻辑
const incomeTimeRange = ref('month');
const selectedHouse = ref('all');
</script>

<style scoped>
.dashboard {
  padding: 20px;
  background-color: var(--secondary-color);
  min-height: calc(100vh - 60px);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background-color: #fff;
  border-radius: var(--border-radius);
  box-shadow: var(--box-shadow);
  padding: 1.5rem;
  display: flex;
  align-items: center;
  transition: var(--transition-base);
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--box-shadow-lg);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background-color: rgba(17, 205, 239, 0.1);
  color: var(--primary-color);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  margin-right: 1rem;
}

.stat-content {
  flex: 1;
}

.stat-card-title {
  font-size: var(--font-size-sm);
  color: var(--gray-600);
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin-bottom: 0.25rem;
}

.stat-card-value {
  font-size: 1.75rem;
  font-weight: 700;
  color: var(--gray-800);
  margin-bottom: 0.5rem;
}

.stat-card-change {
  font-size: var(--font-size-sm);
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.stat-card-change.positive {
  color: var(--success-color);
}

.stat-card-change.negative {
  color: var(--danger-color);
}

.charts-section {
  margin-top: 30px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.chart-header h3 {
  margin: 0;
  color: var(--gray-800);
  font-weight: 600;
}

.chart-placeholder {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fff;
  border-radius: var(--border-radius);
  overflow: hidden;
}

.content {
  background-color: #fff;
  border-radius: var(--border-radius);
  box-shadow: var(--box-shadow);
  padding: 1.5rem;
  height: 100%;
  transition: var(--transition-base);
}

.content:hover {
  box-shadow: var(--box-shadow-lg);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.page-header h2 {
  margin: 0;
  color: var(--gray-800);
  font-weight: 600;
}

/* 动画效果 */
.fade-in {
  animation: fadeIn 0.6s ease-in-out forwards;
}

.slide-in-up {
  animation: slideInUp 0.6s ease-in-out forwards;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: repeat(auto-fit, minmax(100%, 1fr));
  }
  
  .charts-section .el-col {
    margin-bottom: 20px;
  }
}
</style>
