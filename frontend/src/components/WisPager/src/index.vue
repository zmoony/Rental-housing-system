<template>
  <div class="wis-pager">
    <el-pagination
      v-model:current-page="pager[options.pageNum]"
      v-model:page-size="pager[options.pageSize]"
      layout="total, sizes, prev, pager, next, jumper"
      background
      v-bind="$attrs"
    ></el-pagination>
  </div>
</template>
<script lang="ts">
export default {
  name: 'WisPager'
}
</script>
<script lang="ts" setup>
import { ref, onMounted, defineProps, toRefs } from 'vue'
const prop = defineProps({
  pager: {
    type: Object,
    default: () => {
      return {
        page: 1,
        limit: 10
      }
    }
  },
  options: {
    type: Object,
    default: () => {
      return {
        pageNum: 'page',
        pageSize: 'limit'
      }
    }
  }
})
const { pager, options } = toRefs<any>(prop)
const emit = defineEmits(['update:pager', 'pager-change'])
</script>
<style lang="scss">
.wis-pager {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}
</style>
