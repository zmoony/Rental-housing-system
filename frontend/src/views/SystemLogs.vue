<template>
  <div class="system-logs-page">
    <div class="page-header">
      <h2>系统日志管理</h2>
      <div class="header-actions">
        <el-input
          v-model="queryParams.keyword"
          placeholder="请输入操作内容搜索"
          style="width: 250px; margin-right: 10px"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
        <el-select
          v-model="queryParams.operationType"
          placeholder="操作类型"
          clearable
          style="width: 120px; margin-right: 10px"
          @change="handleSearch"
        >
          <el-option label="登录" value="LOGIN" />
          <el-option label="新增" value="INSERT" />
          <el-option label="修改" value="UPDATE" />
          <el-option label="删除" value="DELETE" />
          <el-option label="查询" value="SELECT" />
          <el-option label="其他" value="OTHER" />
        </el-select>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
          style="width: 300px; margin-right: 10px"
          @change="handleDateRangeChange"
        />
        <el-button type="danger" @click="handleClear">清空日志</el-button>
      </div>
    </div>

    <div class="content">
      <el-table 
        :data="systemLogList" 
        stripe 
        v-loading="loading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="operationType" label="操作类型" width="100">
          <template #default="{ row }">
            {{ getOperationTypeText(row.operationType) }}
          </template>
        </el-table-column>
        <el-table-column prop="operationContent" label="操作内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="operator" label="操作人" width="120" />
        <el-table-column prop="operationTime" label="操作时间" width="180" />
        <el-table-column prop="operationResult" label="操作结果" width="100">
          <template #default="{ row }">
            <el-tag :type="row.operationResult === 'SUCCESS' ? 'success' : 'danger'">
              {{ row.operationResult === 'SUCCESS' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationIp" label="IP地址" width="150" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row)">查看</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="batch-actions" v-if="selectedLogs.length > 0">
        <el-button type="danger" @click="handleBatchDelete">批量删除</el-button>
        <span>已选择 {{ selectedLogs.length }} 项</span>
      </div>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.current"
          v-model:page-size="queryParams.size"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 查看系统日志详情对话框 -->
    <el-dialog
      title="系统日志详情"
      v-model="detailDialogVisible"
      width="600px"
    >
      <el-descriptions :column="1" border>
        <el-descriptions-item label="ID">{{ detail.id }}</el-descriptions-item>
        <el-descriptions-item label="操作类型">{{ getOperationTypeText(detail.operationType) }}</el-descriptions-item>
        <el-descriptions-item label="操作内容">{{ detail.operationContent }}</el-descriptions-item>
        <el-descriptions-item label="操作人">{{ detail.operator }}</el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ detail.operationTime }}</el-descriptions-item>
        <el-descriptions-item label="操作结果">
          <el-tag :type="detail.operationResult === 'SUCCESS' ? 'success' : 'danger'">
            {{ detail.operationResult === 'SUCCESS' ? '成功' : '失败' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ detail.operationIp }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 删除确认对话框 -->
    <el-dialog
      title="提示"
      v-model="deleteDialogVisible"
      width="400px"
    >
      <div>确定要删除该系统日志吗？</div>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确定</el-button>
      </template>
    </el-dialog>

    <!-- 批量删除确认对话框 -->
    <el-dialog
      title="提示"
      v-model="batchDeleteDialogVisible"
      width="400px"
    >
      <div>确定要删除选中的 {{ selectedLogs.length }} 条系统日志吗？</div>
      <template #footer>
        <el-button @click="batchDeleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmBatchDelete">确定</el-button>
      </template>
    </el-dialog>

    <!-- 清空确认对话框 -->
    <el-dialog
      title="提示"
      v-model="clearDialogVisible"
      width="400px"
    >
      <div>确定要清空所有系统日志吗？此操作不可恢复！</div>
      <template #footer>
        <el-button @click="clearDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmClear">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Search } from "@element-plus/icons-vue";
import { getSystemLogList, getSystemLogDetail, deleteSystemLog, batchDeleteSystemLog, clearSystemLog, SystemLog, SystemLogQuery } from "@/api/systemLog";

// 查询参数
const queryParams = reactive<SystemLogQuery & { keyword?: string }>({
  current: 1,
  size: 10,
  keyword: '',
  operationType: '',
  startTime: '',
  endTime: ''
});

// 日期范围
const dateRange = ref<string[]>([]);

// 系统日志列表数据
const systemLogList = ref<SystemLog[]>([]);
const loading = ref(false);
const total = ref(0);

// 选中的日志
const selectedLogs = ref<SystemLog[]>([]);

// 详情对话框
const detailDialogVisible = ref(false);
const detail = ref<SystemLog>({} as SystemLog);

// 删除对话框
const deleteDialogVisible = ref(false);
const deleteId = ref<number>(0);

// 批量删除对话框
const batchDeleteDialogVisible = ref(false);

// 清空对话框
const clearDialogVisible = ref(false);

// 操作类型映射
const getOperationTypeText = (operationType: string) => {
  const operationTypeMap: Record<string, string> = {
    LOGIN: "登录",
    INSERT: "新增",
    UPDATE: "修改",
    DELETE: "删除",
    SELECT: "查询",
    OTHER: "其他"
  };
  return operationTypeMap[operationType] || operationType;
};

// 初始化数据
onMounted(() => {
  fetchSystemLogList();
});

// 获取系统日志列表
const fetchSystemLogList = async () => {
  loading.value = true;
  try {
    const res = await getSystemLogList(queryParams);
    systemLogList.value = res.data.records;
    total.value = res.data.total;
  } catch (error) {
    console.error('获取系统日志列表失败', error);
    ElMessage.error('获取系统日志列表失败');
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  queryParams.current = 1;
  fetchSystemLogList();
};

// 日期范围变化
const handleDateRangeChange = (val: string[]) => {
  if (val) {
    queryParams.startTime = val[0];
    queryParams.endTime = val[1];
  } else {
    queryParams.startTime = '';
    queryParams.endTime = '';
  }
  handleSearch();
};

// 表格选择变化
const handleSelectionChange = (selection: SystemLog[]) => {
  selectedLogs.value = selection;
};

// 查看系统日志详情
const handleView = async (row: SystemLog) => {
  try {
    const res = await getSystemLogDetail(row.id);
    detail.value = res.data;
    detailDialogVisible.value = true;
  } catch (error) {
    console.error('获取系统日志详情失败', error);
    ElMessage.error('获取系统日志详情失败');
  }
};

// 删除系统日志
const handleDelete = (row: SystemLog) => {
  deleteId.value = row.id;
  deleteDialogVisible.value = true;
};

// 确认删除
const confirmDelete = async () => {
  try {
    await deleteSystemLog(deleteId.value);
    ElMessage.success('删除成功');
    deleteDialogVisible.value = false;
    fetchSystemLogList();
  } catch (error) {
    console.error('删除系统日志失败', error);
    ElMessage.error('删除系统日志失败');
  }
};

// 批量删除
const handleBatchDelete = () => {
  if (selectedLogs.value.length === 0) {
    ElMessage.warning('请选择要删除的日志');
    return;
  }
  batchDeleteDialogVisible.value = true;
};

// 确认批量删除
const confirmBatchDelete = async () => {
  try {
    const ids = selectedLogs.value.map(item => item.id);
    await batchDeleteSystemLog(ids);
    ElMessage.success('批量删除成功');
    batchDeleteDialogVisible.value = false;
    fetchSystemLogList();
  } catch (error) {
    console.error('批量删除系统日志失败', error);
    ElMessage.error('批量删除系统日志失败');
  }
};

// 清空日志
const handleClear = () => {
  clearDialogVisible.value = true;
};

// 确认清空
const confirmClear = async () => {
  try {
    await clearSystemLog();
    ElMessage.success('清空日志成功');
    clearDialogVisible.value = false;
    fetchSystemLogList();
  } catch (error) {
    console.error('清空系统日志失败', error);
    ElMessage.error('清空系统日志失败');
  }
};

// 分页大小变化
const handleSizeChange = (size: number) => {
  queryParams.size = size;
  fetchSystemLogList();
};

// 页码变化
const handleCurrentChange = (current: number) => {
  queryParams.current = current;
  fetchSystemLogList();
};
</script>

<style scoped>
.system-logs-page {
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
  align-items: center;
}

.content {
  margin-bottom: 20px;
}

.batch-actions {
  margin-top: 15px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>