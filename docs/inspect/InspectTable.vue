<template>
  <el-table :data="tableData" border style="width: 100%" row-class-name="cursor-pointer" @row-click="handleRowClick">
    <el-table-column prop="createTime" label="创建时间" />
    <el-table-column prop="appName" label="应用名称" />
    <el-table-column prop="packageName" label="应用包名" />
    <el-table-column prop="activityName" label="Activity 名称" />
  </el-table>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";
import { NodeDB } from "./MyDB";

interface TableData {
  fileId: number;
  createTime: string;
  appName: string;
  packageName: string;
  activityName: string;
}
const tableData = ref<TableData[]>();

onMounted(async () => {
  const nodeInfoList = await NodeDB.getAllNodeInfo();
  tableData.value = nodeInfoList.map((item) => ({
    fileId: item.fileId,
    createTime: new Date(item.fileId).toLocaleString(),
    appName: item.appName,
    packageName: item.packageName,
    activityName: item.activityName,
  }));
});

const handleRowClick = (row: TableData) => {
  window.open(`/inspect?fileId=${row.fileId}`);
};
</script>
