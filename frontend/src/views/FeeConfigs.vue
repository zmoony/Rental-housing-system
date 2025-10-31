<template>
  <div class="fee-configs-page">
    <div class="page-header">
      <h2>费用配置管理</h2>
      <div class="header-actions">
        <el-input
          v-model="queryParams.keyword"
          placeholder="请输入费用名称搜索"
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
          v-model="queryParams.feeType"
          placeholder="费用类型"
          clearable
          style="width: 120px; margin-right: 10px"
          @change="handleSearch"
        >
          <el-option label="水费" value="WATER" />
          <el-option label="电费" value="ELECTRICITY" />
          <el-option label="燃气费" value="GAS" />
          <el-option label="物业费" value="PROPERTY" />
          <el-option label="其他" value="OTHER" />
        </el-select>
        <el-select
          v-model="queryParams.houseId"
          placeholder="所属房屋"
          clearable
          style="width: 150px; margin-right: 10px"
          @change="handleSearch"
        >
          <el-option 
            v-for="item in houseOptions" 
            :key="item.id" 
            :label="item.name" 
            :value="item.id" 
          />
        </el-select>
        <el-button type="primary" @click="handleAdd">添加费用配置</el-button>
      </div>
    </div>

    <div class="content">
      <el-table :data="feeConfigList" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="feeName" label="费用名称" width="150" />
        <el-table-column prop="feeType" label="费用类型" width="120">
          <template #default="{ row }">
            {{ getFeeTypeText(row.feeType) }}
          </template>
        </el-table-column>
        <el-table-column prop="feeStandard" label="收费标准" width="120">
          <template #default="{ row }">
            {{ row.feeStandard }} 元
          </template>
        </el-table-column>
        <el-table-column prop="houseName" label="所属房屋" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row)">查看</el-button>
            <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
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

    <!-- 添加/编辑费用配置对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="费用名称" prop="feeName">
          <el-input v-model="form.feeName" placeholder="请输入费用名称" />
        </el-form-item>
        <el-form-item label="费用类型" prop="feeType">
          <el-select v-model="form.feeType" placeholder="请选择费用类型" style="width: 100%">
            <el-option label="水费" value="WATER" />
            <el-option label="电费" value="ELECTRICITY" />
            <el-option label="燃气费" value="GAS" />
            <el-option label="物业费" value="PROPERTY" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="收费标准" prop="feeStandard">
          <el-input-number v-model="form.feeStandard" :min="0" :precision="2" :step="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="所属房屋" prop="houseId">
          <el-select v-model="form.houseId" placeholder="请选择所属房屋" filterable style="width: 100%">
            <el-option 
              v-for="item in houseOptions" 
              :key="item.id" 
              :label="item.name" 
              :value="item.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="启用" value="ENABLED" />
            <el-option label="禁用" value="DISABLED" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看费用配置详情对话框 -->
    <el-dialog
      title="费用配置详情"
      v-model="detailDialogVisible"
      width="600px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="ID">{{ detail.id }}</el-descriptions-item>
        <el-descriptions-item label="费用名称">{{ detail.feeName }}</el-descriptions-item>
        <el-descriptions-item label="费用类型">{{ getFeeTypeText(detail.feeType) }}</el-descriptions-item>
        <el-descriptions-item label="收费标准">{{ detail.feeStandard }} 元</el-descriptions-item>
        <el-descriptions-item label="所属房屋">{{ detail.houseName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(detail.status)">{{ getStatusText(detail.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detail.updateTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 删除确认对话框 -->
    <el-dialog
      title="提示"
      v-model="deleteDialogVisible"
      width="400px"
    >
      <div>确定要删除该费用配置吗？</div>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessage, FormInstance } from "element-plus";
import { Search } from "@element-plus/icons-vue";
import { getFeeConfigList, getFeeConfigDetail, addFeeConfig, updateFeeConfig, deleteFeeConfig, FeeConfig, FeeConfigQuery } from "@/api/feeConfig";
import { getHouseList } from "@/api/house";

// 查询参数
const queryParams = reactive<FeeConfigQuery & { keyword?: string }>({
  current: 1,
  size: 10,
  keyword: '',
  feeType: '',
  houseId: undefined
});

// 费用配置列表数据
const feeConfigList = ref<FeeConfig[]>([]);
const loading = ref(false);
const total = ref(0);

// 房屋选项
const houseOptions = ref<any[]>([]);

// 表单相关
const dialogVisible = ref(false);
const dialogTitle = ref('添加费用配置');
const formRef = ref<FormInstance>();
const form = reactive<Partial<FeeConfig>>({
  feeName: '',
  feeType: '',
  feeStandard: 0,
  houseId: undefined,
  status: 'ENABLED'
});

// 表单验证规则
const rules = {
  feeName: [{ required: true, message: '请输入费用名称', trigger: 'blur' }],
  feeType: [{ required: true, message: '请选择费用类型', trigger: 'change' }],
  feeStandard: [{ required: true, message: '请输入收费标准', trigger: 'blur' }],
  houseId: [{ required: true, message: '请选择所属房屋', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
};

// 详情对话框
const detailDialogVisible = ref(false);
const detail = ref<FeeConfig>({} as FeeConfig);

// 删除对话框
const deleteDialogVisible = ref(false);
const deleteId = ref<number>(0);

// 费用类型映射
const getFeeTypeText = (feeType: string) => {
  const feeTypeMap: Record<string, string> = {
    WATER: "水费",
    ELECTRICITY: "电费",
    GAS: "燃气费",
    PROPERTY: "物业费",
    OTHER: "其他"
  };
  return feeTypeMap[feeType] || feeType;
};

// 状态映射
const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    ENABLED: "success",
    DISABLED: "info"
  };
  return statusMap[status] || "info";
};

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    ENABLED: "启用",
    DISABLED: "禁用"
  };
  return statusMap[status] || status;
};

// 初始化数据
onMounted(() => {
  fetchFeeConfigList();
  fetchHouseOptions();
});

// 获取费用配置列表
const fetchFeeConfigList = async () => {
  loading.value = true;
  try {
    const res = await getFeeConfigList(queryParams);
    feeConfigList.value = res.data.records;
    total.value = res.data.total;
  } catch (error) {
    console.error('获取费用配置列表失败', error);
    ElMessage.error('获取费用配置列表失败');
  } finally {
    loading.value = false;
  }
};

// 获取房屋选项
const fetchHouseOptions = async () => {
  try {
    const res = await getHouseList({ current: 1, size: 1000 });
    houseOptions.value = res.data.records;
  } catch (error) {
    console.error('获取房屋列表失败', error);
  }
};

// 搜索
const handleSearch = () => {
  queryParams.current = 1;
  fetchFeeConfigList();
};

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields();
  }
  form.id = undefined;
  form.feeName = '';
  form.feeType = '';
  form.feeStandard = 0;
  form.houseId = undefined;
  form.status = 'ENABLED';
};

// 添加费用配置
const handleAdd = () => {
  resetForm();
  dialogTitle.value = '添加费用配置';
  dialogVisible.value = true;
};

// 编辑费用配置
const handleEdit = (row: FeeConfig) => {
  resetForm();
  dialogTitle.value = '编辑费用配置';
  Object.assign(form, row);
  dialogVisible.value = true;
};

// 查看费用配置详情
const handleView = async (row: FeeConfig) => {
  try {
    const res = await getFeeConfigDetail(row.id);
    detail.value = res.data;
    detailDialogVisible.value = true;
  } catch (error) {
    console.error('获取费用配置详情失败', error);
    ElMessage.error('获取费用配置详情失败');
  }
};

// 删除费用配置
const handleDelete = (row: FeeConfig) => {
  deleteId.value = row.id;
  deleteDialogVisible.value = true;
};

// 确认删除
const confirmDelete = async () => {
  try {
    await deleteFeeConfig(deleteId.value);
    ElMessage.success('删除成功');
    deleteDialogVisible.value = false;
    fetchFeeConfigList();
  } catch (error) {
    console.error('删除费用配置失败', error);
    ElMessage.error('删除费用配置失败');
  }
};

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return;
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.id) {
          // 更新
          await updateFeeConfig(form as FeeConfig);
          ElMessage.success('更新成功');
        } else {
          // 添加
          await addFeeConfig(form as FeeConfig);
          ElMessage.success('添加成功');
        }
        dialogVisible.value = false;
        fetchFeeConfigList();
      } catch (error) {
        console.error('操作失败', error);
        ElMessage.error('操作失败');
      }
    }
  });
};

// 分页大小变化
const handleSizeChange = (size: number) => {
  queryParams.size = size;
  fetchFeeConfigList();
};

// 页码变化
const handleCurrentChange = (current: number) => {
  queryParams.current = current;
  fetchFeeConfigList();
};
</script>

<style scoped>
.fee-configs-page {
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

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>