<template>
  <el-table
    :border="true"
    :data="props.tableData"
    style="width: 100%"
    row-class-name="cursor-pointer"
    @row-click="handleRowClick"
    :default-sort="{ prop: 'createTime', order: 'descending' }"
  >
    <el-table-column prop="createTime" label="创建时间" sortable width="200" :formatter="formatter" />
    <el-table-column prop="appName" label="应用名称" :filters="appFilters" :filter-method="filterHandler" width="150" />
    <el-table-column prop="packageName" label="应用包名" />
    <el-table-column prop="activityName" label="Activity 名称" />
  </el-table>
</template>

<script lang="ts" setup>
import { TableColumnCtx } from "element-plus";
import { FileTableData } from "./types";
import { computed } from "vue";

const props = defineProps<{
  tableData: FileTableData[];
}>();

const handleRowClick = (row: FileTableData) => {
  window.open(`/inspect?fileId=${row.fileId}`);
};

const appFilters = computed(() => {
  const map = new Map<string, string>();
  props.tableData.forEach((item) => {
    map.set(item.packageName, item.appName);
  });
  return Array.from(map).map((item) => ({ text: item[1], value: item[0] }));
});

const filterHandler = (value: string, row: FileTableData, column: TableColumnCtx<FileTableData>) => {
  return row.packageName === value;
};

const formatter = (row: FileTableData, column: TableColumnCtx<FileTableData>) => {
  return new Date(row.createTime).toLocaleString();
};
</script>
