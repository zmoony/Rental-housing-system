# 参数修复清单

## 已修复的问题

### 1. UtilityCalculationService.java - 已修复 ✅
- **问题**: `getRecordByContractAndMonth` 方法接收2个参数（year, month），但实际只需要1个参数（month字符串）
- **修复**: 改为传递格式化的月份字符串 `"YYYY-MM"`
```java
String monthStr = String.format("%04d-%02d", lastMonth.getYear(), lastMonth.getMonthValue());
UtilityRecord existingRecord = utilityRecordMapper.getRecordByContractAndMonth(contract.getId(), monthStr);
```

### 2. UtilityRecordMapper.java - 已修复 ✅
- **问题**: 使用 MySQL 的 `DATE_FORMAT` 函数，不兼容 PostgreSQL
- **修复**: 改用 PostgreSQL 的 `TO_CHAR` 函数
```java
@Select("SELECT * FROM utility_records WHERE contract_id = #{contractId} AND TO_CHAR(record_date, 'YYYY-MM') = #{month}")
```

### 3. FeeConfigMapper.java - 已修复 ✅
- **问题**: 缺少 `List` 导入
- **修复**: 添加 `import java.util.List;`

## 需要手动修复的问题

### 1. frontend/src/views/Utilities.vue - 需要修复 ⚠️
- **问题**: `generateMonthlyBills` 方法内部调用自身，造成无限递归
- **当前代码**:
```javascript
const generateMonthlyBills = async () => {
  try {
    await generateMonthlyBills();  // ❌ 这里调用了自己
    ElMessage.success("月度账单生成成功");
    loadUtilityRecords();
  } catch (error) {
    ElMessage.error("生成月度账单失败");
  }
};
```

- **应该修改为**:
```javascript
const handleGenerateMonthlyBills = async () => {
  try {
    await generateMonthlyBills();  // ✅ 调用API方法
    ElMessage.success("月度账单生成成功");
    loadUtilityRecords();
  } catch (error) {
    ElMessage.error("生成月度账单失败");
  }
};
```

- **同时修改模板中的引用**:
```vue
<el-button @click="handleGenerateMonthlyBills">
  <el-icon><Refresh /></el-icon>
  生成月度账单
</el-button>
```

### 2. frontend/src/api/index.ts - 需要检查
- **需要确认**: `generateMonthlyBills` API 方法是否正确导出

## 完整修复步骤

1. 打开 `frontend/src/views/Utilities.vue`
2. 找到第 288-296 行的 `generateMonthlyBills` 方法
3. 将方法名改为 `handleGenerateMonthlyBills`
4. 找到模板中的按钮（约第 13 行）
5. 将 `@click="generateMonthlyBills"` 改为 `@click="handleGenerateMonthlyBills"`

## 其他注意事项

### API导入检查
确保在 `frontend/src/api/index.ts` 中正确导出了 `generateMonthlyBills` 方法：
```typescript
export const generateMonthlyBills = () => {
  return api.post("/utilities/generate-monthly-bills");
};
```

### 后端验证
确保后端 `UtilityController.java` 中有对应的端点：
```java
@PostMapping("/generate-monthly-bills")
public ResponseEntity<Void> generateMonthlyBills() {
    utilityCalculationService.generateMonthlyBills();
    return ResponseEntity.ok().build();
}
```

