<template>
  <div class="rent-records-page">
    <div class="page-header">
      <h2>租金记录管理</h2>
      <div class="header-actions">
        <el-input
          v-model="queryParams.keyword"
          placeholder="请输入租户姓名/房间号搜索"
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
        <el-date-picker
          v-model="queryParams.month"
          type="month"
          placeholder="选择月份"
          format="YYYY-MM"
          value-format="YYYY-MM"
          style="width: 150px; margin-right: 10px"
          @change="handleSearch"
        />
        <el-select
          v-model="queryParams.status"
          placeholder="状态"
          clearable
          style="width: 120px; margin-right: 10px"
          @change="handleSearch"
        >
          <el-option label="未支付" value="UNPAID" />
          <el-option label="已支付" value="PAID" />
          <el-option label="逾期" value="OVERDUE" />
        </el-select>
        <el-button type="primary" @click="handleAdd">添加租金记录</el-button>
      </div>
    </div>

    <div class="content">
      <el-table :data="rentRecordList" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="tenantName" label="租户姓名" width="120" />
        <el-table-column prop="roomNumber" label="房间号" width="120" />
        <el-table-column prop="month" label="月份" width="100" />
        <el-table-column prop="rentAmount" label="租金" width="100">
          <template #default="{ row }">
            {{ row.rentAmount }} 元
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="总金额" width="100">
          <template #default="{ row }">
            {{ row.totalAmount }} 元
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="paymentTime" label="支付时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row)">查看</el-button>
            <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button 
              size="small" 
              type="success" 
              v-if="row.status === 'UNPAID' || row.status === 'OVERDUE'"
              @click="handlePay(row)"
            >
              支付
            </el-button>
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

    <!-- 添加/编辑租金记录对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="租户" prop="tenantId">
              <el-select v-model="form.tenantId" placeholder="请选择租户" filterable>
                <el-option 
                  v-for="item in tenantOptions" 
                  :key="item.id" 
                  :label="item.name" 
                  :value="item.id" 
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房间" prop="roomId">
              <el-select v-model="form.roomId" placeholder="请选择房间" filterable>
                <el-option 
                  v-for="item in roomOptions" 
                  :key="item.id" 
                  :label="item.roomNumber" 
                  :value="item.id" 
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="月份" prop="month">
              <el-date-picker
                v-model="form.month"
                type="month"
                placeholder="选择月份"
                format="YYYY-MM"
                value-format="YYYY-MM"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="租金" prop="rentAmount">
              <el-input-number v-model="form.rentAmount" :min="0" :precision="2" :step="100" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="水费" prop="waterFee">
              <el-input-number v-model="form.waterFee" :min="0" :precision="2" :step="10" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电费" prop="electricityFee">
              <el-input-number v-model="form.electricityFee" :min="0" :precision="2" :step="10" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="燃气费" prop="gasFee">
              <el-input-number v-model="form.gasFee" :min="0" :precision="2" :step="10" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物业费" prop="propertyFee">
              <el-input-number v-model="form.propertyFee" :min="0" :precision="2" :step="10" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="其他费用" prop="otherFee">
              <el-input-number v-model="form.otherFee" :min="0" :precision="2" :step="10" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总金额">
              <el-input-number v-model="totalAmount" disabled style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="未支付" value="UNPAID" />
            <el-option label="已支付" value="PAID" />
            <el-option label="逾期" value="OVERDUE" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看租金记录详情对话框 -->
    <el-dialog
      title="租金记录详情"
      v-model="detailDialogVisible"
      width="700px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="ID">{{ detail.id }}</el-descriptions-item>
        <el-descriptions-item label="租户姓名">{{ detail.tenantName }}</el-descriptions-item>
        <el-descriptions-item label="房间号">{{ detail.roomNumber }}</el-descriptions-item>
        <el-descriptions-item label="月份">{{ detail.month }}</el-descriptions-item>
        <el-descriptions-item label="租金">{{ detail.rentAmount }} 元</el-descriptions-item>
        <el-descriptions-item label="水费">{{ detail.waterFee }} 元</el-descriptions-item>
        <el-descriptions-item label="电费">{{ detail.electricityFee }} 元</el-descriptions-item>
        <el-descriptions-item label="燃气费">{{ detail.gasFee }} 元</el-descriptions-item>
        <el-descriptions-item label="物业费">{{ detail.propertyFee }} 元</el-descriptions-item>
        <el-descriptions-item label="其他费用">{{ detail.otherFee }} 元</el-descriptions-item>
        <el-descriptions-item label="总金额">{{ detail.totalAmount }} 元</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(detail.status)">{{ getStatusText(detail.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="支付时间" :span="2">{{ detail.paymentTime || '未支付' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detail.updateTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 支付对话框 -->
    <el-dialog
      title="确认支付"
      v-model="payDialogVisible"
      width="400px"
    >
      <div>确认将此租金记录标记为已支付？</div>
      <template #footer>
        <el-button @click="payDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmPay">确认支付</el-button>
      </template>
    </el-dialog>

    <!-- 删除确认对话框 -->
    <el-dialog
      title="提示"
      v-model="deleteDialogVisible"
      width="400px"
    >
      <div>确定要删除该租金记录吗？</div>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from "vue";
import { ElMessage, ElMessageBox, FormInstance } from "element-plus";
import { Search } from "@element-plus/icons-vue";
import { getRentRecordList, getRentRecordDetail, addRentRecord, updateRentRecord, deleteRentRecord, updateRentRecordStatus, RentRecord, RentRecordQuery } from "@/api/rentRecord";
import { getTenantList, Tenant } from "@/api/tenant";
import { getRoomList, Room } from "@/api/room";

// 查询参数
const queryParams = reactive<RentRecordQuery & { keyword?: string }>({
  current: 1,
  size: 10,
  keyword: '',
  month: '',
  status: ''
});

// 租金记录列表数据
const rentRecordList = ref<RentRecord[]>([]);
const loading = ref(false);
const total = ref(0);

// 租户和房间选项
const tenantOptions = ref<Tenant[]>([]);
const roomOptions = ref<Room[]>([]);

// 表单相关
const dialogVisible = ref(false);
const dialogTitle = ref('添加租金记录');
const formRef = ref<FormInstance>();
const form = reactive<Partial<RentRecord>>({
  tenantId: undefined,
  roomId: undefined,
  month: '',
  rentAmount: 0,
  waterFee: 0,
  electricityFee: 0,
  gasFee: 0,
  propertyFee: 0,
  otherFee: 0,
  status: 'UNPAID'
});

// 计算总金额
const totalAmount = computed(() => {
  return (
    (form.rentAmount || 0) +
    (form.waterFee || 0) +
    (form.electricityFee || 0) +
    (form.gasFee || 0) +
    (form.propertyFee || 0) +
    (form.otherFee || 0)
  );
});

// 表单验证规则
const rules = {
  tenantId: [{ required: true, message: '请选择租户', trigger: 'change' }],
  roomId: [{ required: true, message: '请选择房间', trigger: 'change' }],
  month: [{ required: true, message: '请选择月份', trigger: 'change' }],
  rentAmount: [{ required: true, message: '请输入租金', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
};

// 详情对话框
const detailDialogVisible = ref(false);
const detail = ref<RentRecord>({} as RentRecord);

// 支付对话框
const payDialogVisible = ref(false);
const payId = ref<number>(0);

// 删除对话框
const deleteDialogVisible = ref(false);
const deleteId = ref<number>(0);

// 状态映射
const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    UNPAID: "warning",
    PAID: "success",
    OVERDUE: "danger"
  };
  return statusMap[status] || "info";
};

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    UNPAID: "未支付",
    PAID: "已支付",
    OVERDUE: "逾期"
  };
  return statusMap[status] || status;
};

// 初始化数据
onMounted(() => {
  fetchRentRecordList();
  fetchTenantOptions();
  fetchRoomOptions();
});

// 获取租金记录列表
const fetchRentRecordList = async () => {
  loading.value = true;
  try {
    const res = await getRentRecordList(queryParams);
    rentRecordList.value = res.data.records;
    total.value = res.data.total;
  } catch (error) {
    console.error('获取租金记录列表失败', error);
    ElMessage.error('获取租金记录列表失败');
  } finally {
    loading.value = false;
  }
};

// 获取租户选项
const fetchTenantOptions = async () => {
  try {
    const res = await getTenantList({ current: 1, size: 1000 });
    tenantOptions.value = res.data.records;
  } catch (error) {
    console.error('获取租户列表失败', error);
  }
};

// 获取房间选项
const fetchRoomOptions = async () => {
  try {
    const res = await getRoomList({ current: 1, size: 1000 });
    roomOptions.value = res.data.records;
  } catch (error) {
    console.error('获取房间列表失败', error);
  }
};

// 搜索
const handleSearch = () => {
  queryParams.current = 1;
  fetchRentRecordList();
};

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields();
  }
  form.id = undefined;
  form.tenantId = undefined;
  form.roomId = undefined;
  form.month = '';
  form.rentAmount = 0;
  form.waterFee = 0;
  form.electricityFee = 0;
  form.gasFee = 0;
  form.propertyFee = 0;
  form.otherFee = 0;
  form.status = 'UNPAID';
};

// 添加租金记录
const handleAdd = () => {
  resetForm();
  dialogTitle.value = '添加租金记录';
  dialogVisible.value = true;
};

// 编辑租金记录
const handleEdit = (row: RentRecord) => {
  resetForm();
  dialogTitle.value = '编辑租金记录';
  Object.assign(form, row);
  dialogVisible.value = true;
};

// 查看租金记录详情
const handleView = async (row: RentRecord) => {
  try {
    const res = await getRentRecordDetail(row.id);
    detail.value = res.data;
    detailDialogVisible.value = true;
  } catch (error) {
    console.error('获取租金记录详情失败', error);
    ElMessage.error('获取租金记录详情失败');
  }
};

// 支付租金
const handlePay = (row: RentRecord) => {
  payId.value = row.id;
  payDialogVisible.value = true;
};

// 确认支付
const confirmPay = async () => {
  try {
    await updateRentRecordStatus(payId.value, 'PAID');
    ElMessage.success('支付成功');
    payDialogVisible.value = false;
    fetchRentRecordList();
  } catch (error) {
    console.error('支付失败', error);
    ElMessage.error('支付失败');
  }
};

// 删除租金记录
const handleDelete = (row: RentRecord) => {
  deleteId.value = row.id;
  deleteDialogVisible.value = true;
};

// 确认删除
const confirmDelete = async () => {
  try {
    await deleteRentRecord(deleteId.value);
    ElMessage.success('删除成功');
    deleteDialogVisible.value = false;
    fetchRentRecordList();
  } catch (error) {
    console.error('删除租金记录失败', error);
    ElMessage.error('删除租金记录失败');
  }
};

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return;
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 设置总金额
        form.totalAmount = totalAmount.value;
        
        if (form.id) {
          // 更新
          await updateRentRecord(form as RentRecord);
          ElMessage.success('更新成功');
        } else {
          // 添加
          await addRentRecord(form as RentRecord);
          ElMessage.success('添加成功');
        }
        dialogVisible.value = false;
        fetchRentRecordList();
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
  fetchRentRecordList();
};

// 页码变化
const handleCurrentChange = (current: number) => {
  queryParams.current = current;
  fetchRentRecordList();
};
</script>

<style scoped>
.rent-records-page {
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