<template>
  <div class="utilities-page">
    <div class="page-header">
      <h2>水电费管理</h2>
      <div class="header-actions">
        <el-button type="primary" @click="showRecordDialog = true">
          <el-icon><Plus /></el-icon>
          记录读数
        </el-button>
        <el-button @click="handleGenerateMonthlyBills">
          <el-icon><Refresh /></el-icon>
          生成月度账单
        </el-button>
      </div>
    </div>

    <!-- 搜索区域 -->
    <div class="search-section">
      <el-form :model="searchForm" inline>
        <el-form-item label="房间号">
          <el-input v-model="searchForm.roomNumber" placeholder="请输入房间号" />
        </el-form-item>
        <el-form-item label="记录日期">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-section">
      <el-table :data="utilityRecords" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="roomNumber" label="房间号" width="120" />
        <el-table-column prop="recordDate" label="记录日期" width="120" />
        <el-table-column prop="waterReading" label="水表读数" width="100" />
        <el-table-column prop="electricityReading" label="电表读数" width="100" />
        <el-table-column prop="gasReading" label="燃气表读数" width="120" />
        <el-table-column prop="waterUsage" label="用水量" width="100" />
        <el-table-column prop="electricityUsage" label="用电量" width="100" />
        <el-table-column prop="gasUsage" label="用气量" width="100" />
        <el-table-column prop="waterFee" label="水费" width="100" />
        <el-table-column prop="electricityFee" label="电费" width="100" />
        <el-table-column prop="gasFee" label="燃气费" width="100" />
        <el-table-column prop="totalFee" label="总费用" width="100">
          <template #default="{ row }">
            <span class="total-fee">¥{{ row.totalFee?.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="viewDetails(row)">查看</el-button>
            <el-button size="small" type="primary" @click="editRecord(row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 记录读数对话框 -->
    <el-dialog
      v-model="showRecordDialog"
      title="记录水电费读数"
      width="600px"
      @close="resetRecordForm"
    >
      <el-form ref="recordFormRef" :model="recordForm" :rules="recordRules" label-width="120px">
        <el-form-item label="合同" prop="contractId">
          <el-select v-model="recordForm.contractId" placeholder="请选择合同" style="width: 100%">
            <el-option
              v-for="contract in contracts"
              :key="contract.id"
              :label="`${contract.contractNumber} - ${contract.roomNumber}`"
              :value="contract.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="房间" prop="roomId">
          <el-select v-model="recordForm.roomId" placeholder="请选择房间" style="width: 100%">
            <el-option
              v-for="room in rooms"
              :key="room.id"
              :label="room.roomNumber"
              :value="room.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="记录日期" prop="recordDate">
          <el-date-picker
            v-model="recordForm.recordDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="水表读数" prop="waterReading">
          <el-input-number
            v-model="recordForm.waterReading"
            :precision="2"
            :min="0"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="电表读数" prop="electricityReading">
          <el-input-number
            v-model="recordForm.electricityReading"
            :precision="2"
            :min="0"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="燃气表读数" prop="gasReading">
          <el-input-number
            v-model="recordForm.gasReading"
            :precision="2"
            :min="0"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showRecordDialog = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="submitRecord"> 确定 </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessage, type FormInstance, type FormRules } from "element-plus";
import { Plus, Refresh } from "@element-plus/icons-vue";
import { recordUtilityReading, getRoomUtilityRecords } from "@/api";

// 响应式数据
const loading = ref(false);
const submitting = ref(false);
const showRecordDialog = ref(false);
const utilityRecords = ref([]);
const contracts = ref([]);
const rooms = ref([]);

const searchForm = reactive({
  roomNumber: "",
  dateRange: null
});

const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
});

const recordForm = reactive({
  contractId: null,
  roomId: null,
  recordDate: null,
  waterReading: null,
  electricityReading: null,
  gasReading: null
});

const recordFormRef = ref<FormInstance>();

const recordRules: FormRules = {
  contractId: [{ required: true, message: "请选择合同", trigger: "change" }],
  roomId: [{ required: true, message: "请选择房间", trigger: "change" }],
  recordDate: [{ required: true, message: "请选择记录日期", trigger: "change" }],
  waterReading: [{ required: true, message: "请输入水表读数", trigger: "blur" }],
  electricityReading: [{ required: true, message: "请输入电表读数", trigger: "blur" }],
  gasReading: [{ required: true, message: "请输入燃气表读数", trigger: "blur" }]
};

// 方法
const loadUtilityRecords = async () => {
  loading.value = true;
  try {
    // 这里应该调用实际的API
    // const response = await getRoomUtilityRecords(searchForm.roomId)
    // utilityRecords.value = response.data
    // pagination.total = response.total

    // 模拟数据
    utilityRecords.value = [
      {
        id: 1,
        roomNumber: "A101",
        recordDate: "2024-01-15",
        waterReading: 125.5,
        electricityReading: 856.2,
        gasReading: 45.8,
        waterUsage: 12.5,
        electricityUsage: 85.6,
        gasUsage: 4.5,
        waterFee: 43.75,
        electricityFee: 51.36,
        gasFee: 11.25,
        totalFee: 106.36,
        status: "PENDING"
      }
    ];
    pagination.total = 1;
  } catch (error) {
    ElMessage.error("加载数据失败");
  } finally {
    loading.value = false;
  }
};

const handleSearch = () => {
  pagination.page = 1;
  loadUtilityRecords();
};

const handleReset = () => {
  searchForm.roomNumber = "";
  searchForm.dateRange = null;
  handleSearch();
};

const handleSizeChange = (size: number) => {
  pagination.size = size;
  loadUtilityRecords();
};

const handleCurrentChange = (page: number) => {
  pagination.page = page;
  loadUtilityRecords();
};

const submitRecord = async () => {
  if (!recordFormRef.value) return;

  try {
    await recordFormRef.value.validate();
    submitting.value = true;

    await recordUtilityReading(recordForm);

    ElMessage.success("记录成功");
    showRecordDialog.value = false;
    loadUtilityRecords();
  } catch (error: any) {
    ElMessage.error(error.message || "记录失败");
  } finally {
    submitting.value = false;
  }
};

const resetRecordForm = () => {
  recordFormRef.value?.resetFields();
};

const handleGenerateMonthlyBills = async () => {
  try {
    await generateMonthlyBills();
    ElMessage.success("月度账单生成成功");
    loadUtilityRecords();
  } catch (error) {
    ElMessage.error("生成月度账单失败");
  }
};

const viewDetails = (row: any) => {
  // 查看详情逻辑
  console.log("查看详情:", row);
};

const editRecord = (row: any) => {
  // 编辑记录逻辑
  console.log("编辑记录:", row);
};

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

// 生命周期
onMounted(() => {
  loadUtilityRecords();
});
</script>

<style scoped>
.utilities-page {
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

.search-section {
  margin-bottom: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 6px;
}

.table-section {
  margin-bottom: 20px;
}

.total-fee {
  font-weight: bold;
  color: #e6a23c;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
