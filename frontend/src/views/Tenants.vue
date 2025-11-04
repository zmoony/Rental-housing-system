<template>
  <div class="page-header">
    <div class="header-title">
      <el-icon color="#fff" :size="24"><User/></el-icon>
      <h2>租户管理</h2>
    </div>
    <div class="header-actions">
      <el-input
          v-model="queryParams.keyword"
          placeholder="请输入姓名/电话搜索"
          class="search-input"
          clearable
          @keyup.enter="handleSearch"
      >
        <template #prefix>
          <i class="fas fa-search"></i>
        </template>
      </el-input>
      <el-select
          v-model="queryParams.status"
          placeholder="状态"
          clearable
          class="status-select"
          @change="handleSearch"
      >
        <el-option label="活跃" value="ACTIVE" />
        <el-option label="非活跃" value="INACTIVE" />
        <el-option label="已搬出" value="MOVED_OUT" />
      </el-select>
      <el-button type="primary" class="add-button" @click="handleAdd">
        添加租户
      </el-button>
    </div>
  </div>
  <div class="common-page">


    <div class="content">
      <WisTable :data="tenantList" :columns="commonColumsAdd" ref="tableRefAdd"
                :loading="loading" max-height="72vh"
                border>
        <template #gender="{row}">
           <span class="gender-icon">
              <i :class="row.gender === 'MALE' ? 'fas fa-mars' : 'fas fa-venus'"></i>
              {{ row.gender === 'MALE' ? '男' : '女' }}
            </span>
        </template>

        <template #status="{row}">
          <el-tag
              :type="getStatusType(row.status)"
              class="status-tag"
              effect="light"
          >
            <i :class="getStatusIcon(row.status)"></i>
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>

        <template #opt="{row}">
          <el-button text bg size="small" type="primary" @click="handleView(row)">查看</el-button>
          <el-button text bg size="small" type="warning" @click="handleEdit(row)">编辑</el-button>
          <el-button text bg size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </WisTable>
      <div class="pagination-container">
        <WisPager v-model:pager="pager" :total="pager.total" @change="fetchTenantList"></WisPager>
      </div>

    </div>

    <!-- 添加/编辑租户对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="600px"
      :close-on-click-modal="false"
      class="custom-dialog"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" class="custom-form">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="身份证" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号码" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio label="MALE">男</el-radio>
            <el-radio label="FEMALE">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="form.age" :min="1" :max="120" />
        </el-form-item>
        <el-form-item label="职业" prop="occupation">
          <el-input v-model="form.occupation" placeholder="请输入职业" />
        </el-form-item>
        <el-form-item label="紧急联系人" prop="emergencyContact">
          <el-input v-model="form.emergencyContact" placeholder="请输入紧急联系人" />
        </el-form-item>
        <el-form-item label="紧急电话" prop="emergencyPhone">
          <el-input v-model="form.emergencyPhone" placeholder="请输入紧急联系电话" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="活跃" value="ACTIVE" />
            <el-option label="非活跃" value="INACTIVE" />
            <el-option label="已搬出" value="MOVED_OUT" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">
            <i class="fas fa-times"></i> 取消
          </el-button>
          <el-button type="primary" @click="handleSubmit">
            <i class="fas fa-check"></i> 确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看租户详情对话框 -->
    <el-dialog
      title="租户详情"
      v-model="detailDialogVisible"
      width="600px"
      class="custom-dialog"
    >
      <el-descriptions :column="2" border class="custom-descriptions">
        <el-descriptions-item label="ID">{{ detail.id }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ detail.name }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ detail.phone }}</el-descriptions-item>
        <el-descriptions-item label="身份证">{{ detail.idCard }}</el-descriptions-item>
        <el-descriptions-item label="性别">
          <span class="gender-icon">
            <i :class="detail.gender === 'MALE' ? 'fas fa-mars' : 'fas fa-venus'"></i>
            {{ detail.gender === 'MALE' ? '男' : '女' }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="年龄">{{ detail.age }}</el-descriptions-item>
        <el-descriptions-item label="职业">{{ detail.occupation }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(detail.status)" class="status-tag" effect="light">
            <i :class="getStatusIcon(detail.status)"></i>
            {{ getStatusText(detail.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="紧急联系人">{{ detail.emergencyContact }}</el-descriptions-item>
        <el-descriptions-item label="紧急电话">{{ detail.emergencyPhone }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detail.updateTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 删除确认对话框 -->
    <el-dialog
      title="提示"
      v-model="deleteDialogVisible"
      width="400px"
      class="custom-dialog"
    >
      <div class="delete-confirm">
        <i class="fas fa-exclamation-triangle warning-icon"></i>
        <p>确定要删除该租户吗？</p>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="deleteDialogVisible = false">
            <i class="fas fa-times"></i> 取消
          </el-button>
          <el-button type="danger" @click="confirmDelete">
            <i class="fas fa-trash-alt"></i> 确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox, FormInstance } from "element-plus";
import { Search } from "@element-plus/icons-vue";
import { getTenantList, getTenantDetail, addTenant, updateTenant, deleteTenant, Tenant, TenantQuery } from "@/api/tenant";
import WisPager from "@/components/WisPager";
import WisTable from "@/components/WisTable";

// 查询参数
const queryParams = reactive<TenantQuery>({
  current: 1,
  size: 10,
  keyword: '',
  status: ''
});

const pager = reactive({
  page: 1,
  limit: 15,
  total: 0
});

// 租户列表数据
const tenantList = ref<Tenant[]>([]);
const loading = ref(false);
const total = ref(0);
const commonColumsAdd = [
  {prop: 'name', label: '姓名', width: '120', 'show-overflow-tooltip': true},
  {prop: 'phone', label: '电话', width: '150', 'show-overflow-tooltip': true},
  {prop: 'idCard', label: '身份证', width: '200', 'show-overflow-tooltip': true},
  {prop: 'gender', label: '性别', width: '80', 'show-overflow-tooltip': true},
  {prop: 'age', label: '年龄', width: '80',sortable: true, 'show-overflow-tooltip': true},
  {prop: 'occupation', label: '职业', 'show-overflow-tooltip': true},
  {prop: 'status', label: '状态', width: '150',sortable: true, 'show-overflow-tooltip': true},
  {prop: 'opt', label: '操作', width: '220', 'show-overflow-tooltip': true},
]

// 表单相关
const dialogVisible = ref(false);
const dialogTitle = ref('添加租户');
const formRef = ref<FormInstance>();
const form = reactive<Partial<Tenant>>({
  name: '',
  phone: '',
  idCard: '',
  gender: 'MALE',
  age: 18,
  occupation: '',
  emergencyContact: '',
  emergencyPhone: '',
  status: 'ACTIVE'
});

// 表单验证规则
const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  idCard: [
    { required: true, message: '请输入身份证号码', trigger: 'blur' },
    { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号码', trigger: 'blur' }
  ],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
};

// 详情对话框
const detailDialogVisible = ref(false);
const detail = ref<Tenant>({} as Tenant);

// 删除对话框
const deleteDialogVisible = ref(false);
const deleteId = ref<number>(0);

// 状态映射
const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    ACTIVE: "success",
    INACTIVE: "warning",
    MOVED_OUT: "info"
  };
  return statusMap[status] || "info";
};

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    ACTIVE: "活跃",
    INACTIVE: "非活跃",
    MOVED_OUT: "已搬出"
  };
  return statusMap[status] || status;
};

// 获取状态图标
const getStatusIcon = (status: string) => {
  const iconMap: Record<string, string> = {
    ACTIVE: "fas fa-check-circle",
    INACTIVE: "fas fa-pause-circle",
    MOVED_OUT: "fas fa-sign-out-alt"
  };
  return iconMap[status] || "fas fa-question-circle";
};

// 初始化数据
onMounted(() => {
  fetchTenantList();
});

// 获取租户列表
const fetchTenantList = async () => {
  loading.value = true;
  try {
    const res = await getTenantList(queryParams);
    tenantList.value = res.data.records;
    total.value = res.data.total;
  } catch (error) {
    console.error('获取租户列表失败', error);
    ElMessage.error('获取租户列表失败');
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  queryParams.current = 1;
  fetchTenantList();
};

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields();
  }
  form.id = undefined;
  form.name = '';
  form.phone = '';
  form.idCard = '';
  form.gender = 'MALE';
  form.age = 18;
  form.occupation = '';
  form.emergencyContact = '';
  form.emergencyPhone = '';
  form.status = 'ACTIVE';
};

// 添加租户
const handleAdd = () => {
  resetForm();
  dialogTitle.value = '添加租户';
  dialogVisible.value = true;
};

// 编辑租户
const handleEdit = (row: Tenant) => {
  resetForm();
  dialogTitle.value = '编辑租户';
  Object.assign(form, row);
  dialogVisible.value = true;
};

// 查看租户详情
const handleView = async (row: Tenant) => {
  try {
    const res = await getTenantDetail(row.id);
    detail.value = res.data;
    detailDialogVisible.value = true;
  } catch (error) {
    console.error('获取租户详情失败', error);
    ElMessage.error('获取租户详情失败');
  }
};

// 删除租户
const handleDelete = (row: Tenant) => {
  deleteId.value = row.id;
  deleteDialogVisible.value = true;
};

// 确认删除
const confirmDelete = async () => {
  try {
    await deleteTenant(deleteId.value);
    ElMessage.success('删除成功');
    deleteDialogVisible.value = false;
    fetchTenantList();
  } catch (error) {
    console.error('删除租户失败', error);
    ElMessage.error('删除租户失败');
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
          await updateTenant(form as Tenant);
          ElMessage.success('更新成功');
        } else {
          // 添加
          await addTenant(form as Tenant);
          ElMessage.success('添加成功');
        }
        dialogVisible.value = false;
        fetchTenantList();
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
  fetchTenantList();
};

// 页码变化
const handleCurrentChange = (current: number) => {
  queryParams.current = current;
  fetchTenantList();
};
</script>

<style scoped>


.search-input {
  width: 250px;
}

.status-select {
  width: 120px;
}

.add-button {
  transition: var(--transition-base);
}

.add-button:hover {
  transform: translateY(-2px);
  box-shadow: var(--box-shadow-sm);
}

.content-card {
  background-color: #fff;
  border-radius: var(--border-radius);
  box-shadow: var(--box-shadow);
  padding: 1.5rem;
  margin-bottom: 2rem;
}

.custom-table {
  margin-bottom: 1.5rem;
}

.gender-icon {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.gender-icon i {
  color: var(--primary-color);
}

.status-tag {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.25rem 0.75rem;
  border-radius: 50rem;
  font-weight: 500;
}

.action-buttons {
  display: flex;
  gap: 0.5rem;
}

.view-button, .edit-button, .delete-button {
  transition: var(--transition-base);
}

.view-button:hover, .edit-button:hover, .delete-button:hover {
  transform: translateY(-2px);
}

.pagination-container {
  margin-top: 1.5rem;
  display: flex;
  justify-content: flex-end;
}

/* 对话框样式 */
.custom-dialog :deep(.el-dialog__header) {
  padding: 1.5rem;
  border-bottom: 1px solid var(--gray-200);
}

.custom-dialog :deep(.el-dialog__body) {
  padding: 1.5rem;
}

.custom-dialog :deep(.el-dialog__footer) {
  padding: 1rem 1.5rem;
  border-top: 1px solid var(--gray-200);
}

.custom-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: var(--gray-700);
}

.custom-descriptions :deep(.el-descriptions__label) {
  font-weight: 500;
  color: var(--gray-700);
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
}

.delete-confirm {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1.5rem 0;
}

.warning-icon {
  font-size: 3rem;
  color: var(--warning-color);
  margin-bottom: 1rem;
}

/* 动画效果 */
.content-card {
  animation: fadeIn 0.6s ease-in-out forwards;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .header-actions {
    width: 100%;
    flex-wrap: wrap;
  }
  
  .search-input, .status-select {
    width: 100%;
  }
  
  .action-buttons {
    flex-direction: column;
  }
}
</style>
