<template>
  <!-- <div class="ds-table"> -->
  <el-table
    class="my-table"
    ref="multipleTable"
    v-loading="($attrs as any).loading"
    element-loading-text="正在加载..."
    :element-loading-spinner="svg"
    :header-cell-class-name="cellClass"
    :header-cell-style="{background:'#F3F3F3',color: '#000',fontSize: '14px',textAlign: 'center',fontWeight: '400'}"
    tooltip-effect="light"
    @current-change="chooseMcaterialChange"
    @selection-change="selectionChange"
    @select-all="selectAll"
    v-bind="$attrs"
  >
    <el-table-column
      v-if="
          ($attrs['onSelectionChange'] || $attrs['onRadioChange']) &&
          !columnsList.some((el:any) => el.type === 'selection')
        "
      type="selection"
      width="50"
      v-bind="columnsList?.find((el:any) => el.type === 'selection')"
    />

    <template v-for="(item, i) in (columnsList as Array<any>)" :key="`${item.prop}_${i}_${key}`">
      <el-table-column
        align="center"
        header-align="center"
        :show-overflow-tooltip="Object.keys(item).includes('tooltip') ? item.tooltip : true"
        v-bind="item"
      >
        <template #header="scope">
          <!-- 自定义表头 -->
          <slot :name="`${item.prop}_header`" v-bind="scope">{{ scope.column.label }}</slot>
        </template>
        <template v-if="!!$slots[item.prop]" #default="scope">
          <!-- 自定义内容 -->
          <!-- <slot :name="item.prop" :row="row" :$index="$index" /> -->
          <slot :name="item.prop" v-bind="scope" />
        </template>
      </el-table-column>
    </template>
    <el-table-column v-if="$slots._handler" align="center" header-align="center" label="操作" prop="_handler">
      <template #default="scope">
        <slot name="_handler" v-bind="scope" />
      </template>
    </el-table-column>
  </el-table>
  <!-- </div> -->
</template>

<script lang="ts">
export default {
  name: 'WisTable'
}
</script>
<script lang="ts" setup>
// import { withDefaults, ref, toRefs, watch, useAttrs } from 'vue'
import { ref, toRefs, watch, useAttrs } from 'vue'

const svg = `<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" viewBox="0 0 100 100" enable-background="new 0 0 100 100" xml:space="preserve">
<rect fill="var(--el-color-primary)" width="3" height="100" transform="translate(0) rotate(180 3 50)">
  <animate attributeName="height" attributeType="XML" dur="1s" values="30; 100; 30" repeatCount="indefinite"></animate>
</rect>
<rect x="17" fill="var(--el-color-primary)" width="3" height="100" transform="translate(0) rotate(180 20 50)">
  <animate attributeName="height" attributeType="XML" dur="1s" values="30; 100; 30" repeatCount="indefinite" begin="0.1s"></animate>
</rect>
<rect x="40" fill="var(--el-color-primary)" width="3" height="100" transform="translate(0) rotate(180 40 50)">
  <animate attributeName="height" attributeType="XML" dur="1s" values="30; 100; 30" repeatCount="indefinite" begin="0.3s"></animate>
</rect>
<rect x="60" fill="var(--el-color-primary)" width="3" height="100" transform="translate(0) rotate(180 58 50)">
  <animate attributeName="height" attributeType="XML" dur="1s" values="30; 100; 30" repeatCount="indefinite" begin="0.5s"></animate>
</rect>
<rect x="80" fill="var(--el-color-primary)" width="3" height="100" transform="translate(0) rotate(180 76 50)">
  <animate attributeName="height" attributeType="XML" dur="1s" values="30; 100; 30" repeatCount="indefinite" begin="0.1s"></animate>
</rect>
</svg>`

interface Props {
  selection?: boolean
  columns: Array<any>
  total?: number | string
  clickRowSelect?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  selection: false,
  columns: () => [],
  clickRowSelect: true
})

const { onRadioChange }: any = useAttrs()
const emit = defineEmits(['onSelectionChange'])

const { columns: columnsList, clickRowSelect } = toRefs(props)

const multipleTable = ref()

const multipleList = ref([])

const key = ref(0)

watch(columnsList, (val: any, oldVal: any) => {
  columnsList.value = val
  if (val.length !== oldVal.length) key.value++
})

const cellClass = (row: any): any => {
  if (row.columnIndex === 0 && onRadioChange) {
    return 'DisableSelection'
  }
}

const toggleRowSelection = (row: any) => {
  multipleTable.value.toggleRowSelection(row)
}

const chooseMcaterialChange = (val: any) => {
  if (val) {
    if (!clickRowSelect.value) return
    multipleTable.value.toggleRowSelection(val)
  }
}

const clearSelection = () => {
  multipleTable.value.clearSelection()
}

const getSelections = () => {
  return multipleList.value
}

// 单选多选
const selectionChange = (val: any) => {
  if (onRadioChange) {
    // 单选
    if (val.length > 1) {
      multipleTable.value.clearSelection()
      multipleTable.value.toggleRowSelection(val.pop())
    } else {
      // emit('onRadioChange', val)
      onRadioChange(val)
    }
  } else {
    // 多选
    emit('onSelectionChange', val)
  }
  multipleList.value = val
}
const selectAll = () => {
  if (onRadioChange) {
    multipleTable.value.clearSelection()
  }
}

defineExpose({
  multipleTable,
  clearSelection,
  toggleRowSelection,
    getSelections
})
</script>

<style>
.el-table .DisableSelection .cell .el-checkbox__inner {
  display: none;
  position: relative;
}
.el-table .DisableSelection .cell:before {
  content: '';
  position: absolute;
}
.el-table__fixed {
  width: auto !important;
}
.el-table th.is-hidden > *,
.el-table td.is-hidden > * {
  visibility: visible !important;
}
/* .el-table thead {
  color: #666; // 通过.el-table 的 --el-table-header-text-color 去控制
} */
.my-table .el-loading-spinner .circular {
  animation: none;
}
</style>
