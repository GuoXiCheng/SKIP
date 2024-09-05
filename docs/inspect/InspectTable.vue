<template>
  <el-table
    :border="true"
    :data="props.tableData"
    style="width: 100%"
    row-class-name="cursor-pointer"
    @row-click="handleRowClick"
  >
    <el-table-column prop="createTime" label="创建时间" />
    <el-table-column prop="appName" label="应用名称" />
    <el-table-column prop="packageName" label="应用包名" />
    <el-table-column prop="activityName" label="Activity 名称" />
  </el-table>
</template>

<script lang="ts" setup>
import { FileTableData } from "./types";

const props = defineProps<{
  tableData: FileTableData[];
}>();

const handleRowClick = (row: FileTableData) => {
  window.open(`/inspect?fileId=${row.fileId}`);
};
</script>
