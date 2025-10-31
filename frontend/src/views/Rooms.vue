<template>
  <div class="rooms-page">
    <div class="page-header">
      <h2>房间管理</h2>
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

    <div class="content">
      <el-table :data="roomList" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="houseName" label="所属房屋" width="150" />
        <el-table-column prop="roomNumber" label="房间号" width="120" />
        <el-table-column prop="roomType" label="房间类型" width="120" />
        <el-table-column prop="area" label="面积(㎡)" width="100" />
        <el-table-column prop="price" label="月租金" width="120">
          <template #default="{ row }"> ¥{{ row.price }} </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220">
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
        <el-form-item label="月租金" prop="price">
          <el-input-number v-model="form.price" :min="1" :precision="2" />
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
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入房间描述"
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
      width="650px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="所属房屋">{{ detail.houseName }}</el-descriptions-item>
        <el-descriptions-item label="房间号">{{ detail.roomNumber }}</el-descriptions-item>
        <el-descriptions-item label="房间类型">{{ detail.roomType }}</el-descriptions-item>
        <el-descriptions-item label="面积">{{ detail.area }} ㎡</el-descriptions-item>
        <el-descriptions-item label="月租金">¥{{ detail.price }}</el-descriptions-item>
        <el-descriptions-item label="房间状态">
          <el-tag :type="getStatusType(detail.status)">
            {{ getStatusText(detail.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="设施" :span="2">{{ detail.facilities }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ detail.description }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Search } from "@element-plus/icons-vue";
import { getRoomList, getRoomDetail, addRoom, updateRoom, deleteRoom, type Room } from "../api/room";
import { getHouseList, type House } from "../api/house";
import type { FormInstance, FormRules } from "element-plus";

// 房间列表数据
const loading = ref(false);
const roomList = ref<Room[]>([]);
const houseList = ref<House[]>([]);
const total = ref(0);

// 查询参数
const queryParams = reactive({
  current: 1,
  size: 10,
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
  price: 0,
  status: "AVAILABLE",
  facilities: "",
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
  price: [{ required: true, message: "请输入月租金", trigger: "blur" }],
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
    const res = await getRoomList(queryParams);
    roomList.value = res.records || [];
    total.value = res.total || 0;
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
  form.price = 0;
  form.status = "AVAILABLE";
  form.facilities = "";
  form.description = "";
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
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-actions {
  display: flex;
  align-items: center;
}

.content {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
