<template>
    <div class="page-header">
      <div class="header-title">
        <el-icon color="#fff" :size="24"><OfficeBuilding/></el-icon>
        <h2>房屋管理</h2>
      </div>
      <div class="header-actions">
        <el-input
          v-model="queryParams.keyword"
          placeholder="搜索房屋名称或地址"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
        <el-button type="primary" @click="handleAdd">添加房屋</el-button>
      </div>
    </div>
  <div class="houses-page">

    <div class="content">
      <WisTable :data="houseList" :columns="commonColumsAdd" ref="tableRefAdd"
                :loading="loading" max-height="72vh"
                border>
        <template #status="{row}">
          <el-tag :type="getStatusType(row.status)">
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
        <WisPager v-model:pager="pager" :total="pager.total" @change="getList"></WisPager>
      </div>
    </div>
    
    <!-- 房屋表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="formTitle"
      width="650px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="房屋名称" prop="houseName">
          <el-input v-model="form.houseName" placeholder="请输入房屋名称" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="房间数" prop="totalRooms">
          <el-input-number v-model="form.totalRooms" :min="1" />
        </el-form-item>
        <el-form-item label="总面积" prop="totalArea">
          <el-input-number v-model="form.totalArea" :min="1" :precision="2" />
        </el-form-item>
        <el-form-item label="房屋类型" prop="houseType">
          <el-select v-model="form.houseType" placeholder="请选择房屋类型">
            <el-option label="公寓" value="公寓" />
            <el-option label="别墅" value="别墅" />
            <el-option label="普通住宅" value="普通住宅" />
            <el-option label="商铺" value="商铺" />
            <el-option label="写字楼" value="写字楼" />
          </el-select>
        </el-form-item>
        <el-form-item label="房屋状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择房屋状态">
            <el-option label="可用" value="AVAILABLE" />
            <el-option label="已占用" value="OCCUPIED" />
            <el-option label="维护中" value="MAINTENANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="设施" prop="facilities">
          <el-input
            v-model="form.facilities"
            type="textarea"
            placeholder="请输入房屋设施，如：空调、热水器、冰箱等"
          />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入房屋描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>
    
    <!-- 房屋详情对话框 -->
    <el-dialog
        v-model="detailVisible"
        title="房屋详情"
        :width="1200"
        class="property-detail-dialog"
    >
      <div class="card-header">
        <el-icon><InfoFilled /></el-icon>
        <span>基本信息</span>
      </div>
      <!-- 基本信息卡片 -->
      <el-card class="info-card" shadow="always">
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="info-item">
              <label>房屋名称</label>
              <div class="info-value">{{ detail.houseName }}</div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>房屋状态</label>
              <div class="info-value">
                <el-text
                    :type="getStatusType(detail.status)"
                >
                  {{ getStatusText(detail.status) }}
                </el-text>
              </div>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="24">
            <div class="info-item">
              <label>地址</label>
              <div class="info-value">
                <el-icon><Location /></el-icon>
                {{ detail.address }}
              </div>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="8">
            <div class="info-item">
              <label>房间数</label>
              <div class="info-value">{{ detail.totalRooms }} 间</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>总面积</label>
              <div class="info-value">{{ detail.totalArea }} m²</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>房屋类型</label>
              <div class="info-value">{{ detail.houseType }}</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <div class="card-header">
        <el-icon><Grid /></el-icon>
        <span>房屋设施</span>
      </div>
      <!-- 设施信息卡片 -->
      <el-card class="facilities-card" shadow="always">

        <div class="facilities-grid">
          {{ detail.facilities }}
        </div>
      </el-card>

      <div class="card-header">
        <el-icon><Document /></el-icon>
        <span>房屋描述</span>
      </div>
      <!-- 描述信息 -->
      <el-card class="description-card" shadow="always">
        <div class="description-content">
          {{ detail.description || '暂无描述信息' }}
        </div>
      </el-card>

    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {OfficeBuilding, Search} from "@element-plus/icons-vue";
import { getHouseList, getHouseDetail, addHouse, updateHouse, deleteHouse, type House } from "../api/house";
import WisPager from "@/components/WisPager";
import WisTable from "@/components/WisTable";

// 查询参数
const queryParams = reactive({
  keyword: ""
});

const pager = reactive({
  page: 1,
  limit: 15,
  total: 0
});

const commonColumsAdd = [
  {prop: 'houseName', label: '房屋名称', 'show-overflow-tooltip': true},
  {prop: 'address', label: '地址', 'show-overflow-tooltip': true},
  {prop: 'totalRooms', label: '房间数', width: '100', 'show-overflow-tooltip': true},
  {prop: 'totalArea', label: '总面积(㎡)', width: '120', 'show-overflow-tooltip': true},
  {prop: 'houseType', label: '房屋类型', width: '120', 'show-overflow-tooltip': true},
  {prop: 'status', label: '状态', width: '100', 'show-overflow-tooltip': true},
  {prop: 'opt', label: '操作', width: '220', 'show-overflow-tooltip': true},
]

// 数据列表
const houseList = ref<House[]>([]);
const loading = ref(false);

// 表单相关
const dialogVisible = ref(false);
const formTitle = ref("");
const formRef = ref();
const form = reactive<House>({
  houseName: "",
  address: "",
  totalRooms: 1,
  totalArea: 0,
  houseType: "",
  description: "",
  facilities: "",
  images: "",
  landlordId: 1, // 默认值，实际应从登录用户获取
  status: "AVAILABLE"
});

// 详情相关
const detailVisible = ref(false);
const detail = ref<House>({
  houseName: "",
  address: "",
  totalRooms: 0,
  totalArea: 0,
  houseType: "",
  description: "",
  facilities: "",
  images: "",
  landlordId: 0,
  status: ""
});

// 表单校验规则
const rules = {
  houseName: [{ required: true, message: "请输入房屋名称", trigger: "blur" }],
  address: [{ required: true, message: "请输入地址", trigger: "blur" }],
  totalRooms: [{ required: true, message: "请输入房间数", trigger: "blur" }],
  totalArea: [{ required: true, message: "请输入总面积", trigger: "blur" }],
  houseType: [{ required: true, message: "请选择房屋类型", trigger: "change" }],
  status: [{ required: true, message: "请选择房屋状态", trigger: "change" }]
};

// 状态映射
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

// 初始化数据
onMounted(() => {
  getList();
});

// 获取列表数据
const getList = () => {
  loading.value = true;
  getHouseList({
    ...queryParams,
    current: pager.page,
    size: pager.limit
  })
    .then(res => {
      houseList.value = res?.records || [];
      pager.total = res?.total || 0;
    })
    .catch((error) => {
      console.error('Error:', error);
      ElMessage.error("获取房屋列表失败");
    })
    .finally(() => {
      loading.value = false;
    });
};

// 搜索
const handleSearch = () => {
  pager.page = 1;
  getList();
};

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    houseName: "",
    address: "",
    totalRooms: 1,
    totalArea: 0,
    houseType: "",
    description: "",
    facilities: "",
    images: "",
    landlordId: 1,
    status: "AVAILABLE"
  });
};

// 添加房屋
const handleAdd = () => {
  resetForm();
  formTitle.value = "添加房屋";
  dialogVisible.value = true;
};

// 编辑房屋
const handleEdit = (row: House) => {
  resetForm();
  formTitle.value = "编辑房屋";
  Object.assign(form, JSON.parse(JSON.stringify(row)));
  dialogVisible.value = true;
};

// 查看房屋详情
const handleView = (row: House) => {
  getHouseDetail(row.id as number)
    .then(res => {
      detail.value = res;
      detailVisible.value = true;
    })
    .catch(() => {
      ElMessage.error("获取房屋详情失败");
    });
};

// 删除房屋
const handleDelete = (row: House) => {
  ElMessageBox.confirm(`确认删除房屋 ${row.houseName} 吗？`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      deleteHouse(row.id as number)
        .then(() => {
          ElMessage.success("删除成功");
          getList();
        })
        .catch(() => {
          ElMessage.error("删除失败");
        });
    })
    .catch(() => {});
};

// 提交表单
const submitForm = () => {
  formRef.value.validate((valid: boolean) => {
    if (valid) {
      const isAdd = !form.id;
      const apiCall = isAdd ? addHouse(form) : updateHouse(form);
      
      apiCall
        .then(() => {
          ElMessage.success(isAdd ? "添加成功" : "更新成功");
          dialogVisible.value = false;
          getList();
        })
        .catch(() => {
          ElMessage.error(isAdd ? "添加失败" : "更新失败");
        });
    }
  });
};

</script>

<style scoped>
.houses-page {
  background: white;
  border-radius: 8px;
  padding: 20px;
  height: calc(100% - 40px - 18px);
}

.property-detail-dialog {
  --el-dialog-padding-primary: 0;
}

.dialog-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  margin: -20px -24px 0 -24px;
}

.header-left {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
}

.header-icon {
  font-size: 20px;
  margin-right: 8px;
}

.status-section {
  text-align: center;
  margin: 20px 0;
}

.status-tag {
  font-size: 14px;
  padding: 8px 16px;
  border-radius: 20px;
}

.info-card,
.facilities-card,
.description-card {
  margin-bottom: 16px;
  border: 1px solid #e4e7ed;
}

.card-header {
  display: flex;
  align-items: center;
  font-weight: 600;
  color: #303133;
  background: linear-gradient(90deg, #3D89F4, #56B8FF);
  border-radius: 6px 6px 0px 0px;
  width: 220px;
  font-family: hzgb;
  font-weight: 400;
  font-size: 16px;
  color: #FFFFFF;
  line-height: 18px;
  font-style: italic;
  padding: 8px 16px;
}

.card-header .el-icon {
  margin-right: 8px;
  font-size: 16px;
}

.info-item {
  margin-bottom: 16px;
}

.info-item label {
  display: block;
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
  font-weight: 500;
}

.info-value {
  font-size: 14px;
  color: #303133;
  display: flex;
  align-items: center;
  padding: 8px 12px;
  background: #f8f9fa;
  border-radius: 6px;
  min-height: 20px;
}

.info-value .el-icon {
  margin-right: 6px;
  color: #67c23a;
}

.facilities-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  color: #606266;
  background: #f8f9fa;
  padding: 16px;
  border-radius: 8px;
  border-left: 4px solid #efaab4;
}

.facility-tag {
  border-radius: 16px;
  padding: 6px 12px;
  font-size: 12px;
}

.description-content {
  line-height: 1.6;
  color: #606266;
  background: #f8f9fa;
  padding: 16px;
  border-radius: 8px;
  border-left: 4px solid #409eff;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 0 0 0;
  border-top: 1px solid #e4e7ed;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .property-detail-dialog {
    width: 95vw !important;
    margin: 0 auto;
  }

  .dialog-header {
    padding: 16px;
  }

  .el-row .el-col {
    margin-bottom: 12px;
  }
}
</style>
