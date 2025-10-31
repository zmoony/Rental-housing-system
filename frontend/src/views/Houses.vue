<template>
  <div class="houses-page">
    <div class="page-header">
      <h2>房屋管理</h2>
      <div class="header-actions">
        <el-input
          v-model="queryParams.keyword"
          placeholder="搜索房屋名称或地址"
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
        <el-button type="primary" @click="handleAdd">添加房屋</el-button>
      </div>
    </div>

    <div class="content">
      <el-table :data="houseList" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="houseName" label="房屋名称" />
        <el-table-column prop="address" label="地址" />
        <el-table-column prop="totalRooms" label="房间数" width="100" />
        <el-table-column prop="totalArea" label="总面积(㎡)" width="120" />
        <el-table-column prop="houseType" label="房屋类型" width="120" />
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
      width="650px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="房屋名称">{{ detail.houseName }}</el-descriptions-item>
        <el-descriptions-item label="房屋状态">
          <el-tag :type="getStatusType(detail.status)">
            {{ getStatusText(detail.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ detail.address }}</el-descriptions-item>
        <el-descriptions-item label="房间数">{{ detail.totalRooms }}</el-descriptions-item>
        <el-descriptions-item label="总面积">{{ detail.totalArea }} ㎡</el-descriptions-item>
        <el-descriptions-item label="房屋类型">{{ detail.houseType }}</el-descriptions-item>
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
import { getHouseList, getHouseDetail, addHouse, updateHouse, deleteHouse, type House } from "../api/house";

// 查询参数
const queryParams = reactive({
  current: 1,
  size: 10,
  keyword: ""
});

// 数据列表
const houseList = ref<House[]>([]);
const loading = ref(false);
const total = ref(0);

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
  getHouseList(queryParams)
    .then(res => {
      houseList.value = res.data.records;
      total.value = res.data.total;
    })
    .catch(() => {
      ElMessage.error("获取房屋列表失败");
    })
    .finally(() => {
      loading.value = false;
    });
};

// 搜索
const handleSearch = () => {
  queryParams.current = 1;
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
      detail.value = res.data;
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

// 分页大小变化
const handleSizeChange = (size: number) => {
  queryParams.size = size;
  getList();
};

// 页码变化
const handleCurrentChange = (current: number) => {
  queryParams.current = current;
  getList();
};
</script>

<style scoped>
.houses-page {
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
