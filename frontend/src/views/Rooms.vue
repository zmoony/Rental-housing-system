<template>
  <div class="page-header">
    <div class="header-title">
      <el-icon color="#fff" :size="24"><HomeFilled/></el-icon>
      <h2>房间管理</h2>
    </div>
    <div class="header-actions">
      <el-select
          v-model="queryParams.houseId"
          placeholder="选择房屋"
          clearable
          style="width: 200px; margin-right: 15px"
          @change="handleHouseChange"
      >
        <el-option
            v-for="house in houseList"
            :key="house.id"
            :label="house.houseName"
            :value="house.id"
        />
      </el-select>
      <el-input
          v-model="queryParams.keyword"
          placeholder="搜索房间号或类型"
          style="width: 250px; margin-right: 15px"
          clearable
          @keyup.enter="handleSearch"
      >
        <template #append>
          <el-button @click="handleSearch">
            <el-icon><Search /></el-icon>
          </el-button>
        </template>
      </el-input>
      <el-button type="primary" @click="handleAdd">添加房间</el-button>
    </div>
  </div>
  <div class="rooms-page">

    <div class="content">
      <WisTable :data="roomList" :columns="commonColumsAdd" ref="tableRefAdd"
                :loading="loading" max-height="72vh"
                border>
        <template #monthlyRent="{row}">
          ¥{{ row.monthlyRent }}
        </template>
        <template #deposit="{row}">
          ¥{{ row.deposit }}
        </template>
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
        <WisPager v-model:pager="pager" :total="pager.total" @change="fetchRoomList"></WisPager>
      </div>
    </div>
    
    <!-- 房间表单对话框 -->
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
        <el-form-item label="所属房屋" prop="houseId">
          <el-select v-model="form.houseId" placeholder="请选择所属房屋">
            <el-option 
              v-for="house in houseList" 
              :key="house.id" 
              :label="house.houseName" 
              :value="house.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="房间号" prop="roomNumber">
          <el-input v-model="form.roomNumber" placeholder="请输入房间号" />
        </el-form-item>
        <el-form-item label="房间类型" prop="roomType">
          <el-select v-model="form.roomType" placeholder="请选择房间类型">
            <el-option label="单间" value="单间" />
            <el-option label="主卧" value="主卧" />
            <el-option label="次卧" value="次卧" />
            <el-option label="标准间" value="标准间" />
            <el-option label="套间" value="套间" />
          </el-select>
        </el-form-item>
        <el-form-item label="面积" prop="area">
          <el-input-number v-model="form.area" :min="1" :precision="2" />
        </el-form-item>
        <el-form-item label="月租金" prop="monthlyRent">
          <el-input-number v-model="form.monthlyRent" :min="1" :precision="2" />
        </el-form-item>
        <el-form-item label="押金" prop="deposit">
          <el-input-number v-model="form.deposit" :min="1" :precision="2" />
        </el-form-item>
        <el-form-item label="房间状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择房间状态">
            <el-option label="可用" value="AVAILABLE" />
            <el-option label="已占用" value="OCCUPIED" />
            <el-option label="维护中" value="MAINTENANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="设施" prop="facilities">
          <el-input
            v-model="form.facilities"
            type="textarea"
            placeholder="请输入房间设施，如：空调、热水器、床、衣柜等"
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
    
    <!-- 房间详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="房间详情"
      :width="1200"
      class="property-detail-dialog"
    >
      <div class="card-header">
        <el-icon><InfoFilled /></el-icon>
        <span>基本信息</span>
      </div>
      <!-- 基本信息卡片 -->
      <!-- 基本信息卡片 -->
      <el-card class="info-card" shadow="always">
        <el-row :gutter="24">
          <el-col :span="24">
            <div class="info-item">
              <label>所属房屋</label>
              <div class="info-value">{{ detail.houseName }}</div>
            </div>
          </el-col>

        </el-row>

        <el-row :gutter="24">
          <el-col :span="8">
            <div class="info-item">
              <label>房间号</label>
              <div class="info-value">
                <el-icon><Location /></el-icon>
                {{ detail.roomNumber }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>房间状态</label>
              <div class="info-value">
                <el-text
                    :type="getStatusType(detail.status)"
                >
                  {{ getStatusText(detail.status) }}
                </el-text>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>房间类型</label>
              <div class="info-value">{{ detail.roomType }} 间</div>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="8">
            <div class="info-item">
              <label>面积</label>
              <div class="info-value">{{ detail.area }} m²</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>月租金</label>
              <div class="info-value">¥{{ detail.monthlyRent }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>押金</label>
              <div class="info-value">¥{{ detail.deposit }} </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <div class="card-header">
        <el-icon><Grid /></el-icon>
        <span>房间设施</span>
      </div>
      <!-- 设施信息卡片 -->
      <el-card class="facilities-card" shadow="always">

        <div class="facilities-grid">
          {{ detail.facilities }}
        </div>
      </el-card>

    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {OfficeBuilding, Search} from "@element-plus/icons-vue";
import { getRoomList, getRoomDetail, addRoom, updateRoom, deleteRoom, type Room } from "../api/room";
import { getHouseList, type House } from "../api/house";
import type { FormInstance, FormRules } from "element-plus";
import WisPager from "@/components/WisPager";
import WisTable from "@/components/WisTable";

// 房间列表数据
const loading = ref(false);
const roomList = ref<Room[]>([]);
const houseList = ref<House[]>([]);
const total = ref(0);

const commonColumsAdd = [
  {prop: 'houseName', label: '所属房屋', 'show-overflow-tooltip': true},
  {prop: 'roomNumber', label: '房间号', width: '150', 'show-overflow-tooltip': true},
  {prop: 'roomType', label: '房间类型', width: '150', 'show-overflow-tooltip': true},
  {prop: 'area', label: '面积(㎡)', width: '150', 'show-overflow-tooltip': true},
  {prop: 'monthlyRent', label: '月租金', width: '200', 'show-overflow-tooltip': true},
  {prop: 'deposit', label: '押金', width: '200', 'show-overflow-tooltip': true},
  {prop: 'status', label: '状态', width: '150', 'show-overflow-tooltip': true},
  {prop: 'opt', label: '操作', width: '220', 'show-overflow-tooltip': true},
]

const pager = reactive({
  page: 1,
  limit: 15,
  total: 0
});


// 查询参数
const queryParams = reactive({
  keyword: "",
  houseId: undefined as number | undefined
});

// 表单相关
const dialogVisible = ref(false);
const detailVisible = ref(false);
const formRef = ref<FormInstance>();
const formTitle = ref("");
const isEdit = ref(false);

// 表单数据
const form = reactive<Partial<Room>>({
  id: undefined,
  houseId: undefined,
  roomNumber: "",
  roomType: "",
  area: 0,
  monthlyRent: 0,
  status: "AVAILABLE",
  facilities: "",
  deposit: 0,
  description: ""
});

// 详情数据
const detail = reactive<Partial<Room & { houseName: string }>>({});

// 表单验证规则
const rules = reactive<FormRules>({
  houseId: [{ required: true, message: "请选择所属房屋", trigger: "change" }],
  roomNumber: [{ required: true, message: "请输入房间号", trigger: "blur" }],
  roomType: [{ required: true, message: "请选择房间类型", trigger: "change" }],
  area: [{ required: true, message: "请输入面积", trigger: "blur" }],
  monthlyRent: [{ required: true, message: "请输入月租金", trigger: "blur" }],
  status: [{ required: true, message: "请选择房间状态", trigger: "change" }]
});

// 获取状态类型
const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    AVAILABLE: "success",
    OCCUPIED: "warning",
    MAINTENANCE: "danger"
  };
  return statusMap[status] || "info";
};

// 获取状态文本
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
  fetchHouseList();
  fetchRoomList();
});

// 获取房屋列表
const fetchHouseList = async () => {
  try {
    const res = await getHouseList({ current: 1, size: 100 });
    houseList.value = res.records || [];
  } catch (error) {
    console.error("获取房屋列表失败", error);
  }
};

// 获取房间列表
const fetchRoomList = async () => {
  loading.value = true;
  try {
    const res = await getRoomList({
      current: pager.page,
      size: pager.limit,
      houseId: queryParams.houseId,
      keyword: queryParams.keyword
    });
    roomList.value = res.records || [];
    pager.total = res.total || 0;
  } catch (error) {
    console.error("获取房间列表失败", error);
  } finally {
    loading.value = false;
  }
};

// 处理房屋变更
const handleHouseChange = () => {
  fetchRoomList();
};

// 处理搜索
const handleSearch = () => {
  queryParams.current = 1;
  fetchRoomList();
};

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields();
  }
  form.id = undefined;
  form.houseId = undefined;
  form.roomNumber = "";
  form.roomType = "";
  form.area = 0;
  form.monthlyRent = 0;
  form.status = "AVAILABLE";
  form.facilities = "";
  form.description = "";
  form.deposit = 0;
};

// 处理添加
const handleAdd = () => {
  resetForm();
  isEdit.value = false;
  formTitle.value = "添加房间";
  dialogVisible.value = true;
};

// 处理编辑
const handleEdit = (row: Room) => {
  resetForm();
  isEdit.value = true;
  formTitle.value = "编辑房间";
  
  // 填充表单数据
  Object.assign(form, row);
  
  dialogVisible.value = true;
};

// 处理查看
const handleView = async (row: Room) => {
  try {
    const res = await getRoomDetail(row.id);
    Object.assign(detail, res);
    
    // 获取房屋名称
    const house = houseList.value.find(h => h.id === res.houseId);
    if (house) {
      detail.houseName = house.houseName;
    }
    
    detailVisible.value = true;
  } catch (error) {
    console.error("获取房间详情失败", error);
  }
};

// 处理删除
const handleDelete = (row: Room) => {
  ElMessageBox.confirm(`确认删除房间 ${row.roomNumber} 吗？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(async () => {
    try {
      await deleteRoom(row.id);
      ElMessage.success("删除成功");
      fetchRoomList();
    } catch (error) {
      console.error("删除房间失败", error);
    }
  }).catch(() => {});
};

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return;
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateRoom(form as Room);
          ElMessage.success("更新成功");
        } else {
          await addRoom(form as Room);
          ElMessage.success("添加成功");
        }
        dialogVisible.value = false;
        fetchRoomList();
      } catch (error) {
        console.error("保存房间失败", error);
      }
    }
  });
};

// 处理分页大小变更
const handleSizeChange = (size: number) => {
  queryParams.size = size;
  fetchRoomList();
};

// 处理页码变更
const handleCurrentChange = (current: number) => {
  queryParams.current = current;
  fetchRoomList();
};
</script>

<style scoped>
.rooms-page {
  background: white;
  border-radius: 8px;
  padding: 20px;
  height: calc(100% - 40px - 18px);
}

.property-detail-dialog {
  --el-dialog-padding-primary: 0;
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


</style>
