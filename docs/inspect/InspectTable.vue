<template>
  <el-table
    ref="tableRef"
    :border="true"
    :data="props.tableData"
    style="width: 100%"
    row-class-name="cursor-pointer"
    @row-click="handleRowClick"
    @sort-change="onSortChange"
    @filter-change="onFilterChange"
    @select="onSelect"
    @select-all="onSelectAll"
  >
    <el-table-column type="selection" width="55" />
    <el-table-column prop="createTime" label="创建时间" sortable="custom" width="200" :formatter="formatter" />
    <el-table-column prop="appName" label="应用名称" :filters="appFilters" width="150" />
    <el-table-column prop="packageName" label="应用包名" />
    <el-table-column prop="deviceName" label="设备名称" />
  </el-table>
</template>

<script lang="ts" setup>
import { TableColumnCtx } from "element-plus";
import { FileTableData } from "./types";
import { ref, nextTick, watch } from "vue";
import { ElTable } from "element-plus";

const tableRef = ref<InstanceType<typeof ElTable>>();

const props = defineProps<{
  tableData: FileTableData[];
  appFilters: { text: string; value: string }[];
  selection: { fileId: string; select: boolean }[];
}>();

const emits = defineEmits(["onSelect", "onSortChange", "onFilterChange", "onSelectAll"]);

const handleRowClick = (row: FileTableData) => {
  window.open(`/inspect?fileId=${row.fileId}`);
};

const formatter = (row: FileTableData, column: TableColumnCtx<FileTableData>) => {
  return new Date(row.createTime)
    .toLocaleString("zh-CN", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
    })
    .replace(/\//g, "-");
};

const onSelect = (selection: FileTableData[], row: FileTableData) =>
  emits("onSelect", { fileId: row.fileId, select: selection.includes(row) });

const onSelectAll = (selection: FileTableData[]) => {
  const selected = props.tableData.length === selection.length;
  const items = props.tableData.map((item) => ({ fileId: item.fileId, select: selected }));
  emits("onSelectAll", items);
};

const onSortChange = ({ prop, order }: { prop: string; order: string }) => emits("onSortChange", order);

const onFilterChange = (filters: { [key: string]: string[] }) => emits("onFilterChange", Object.values(filters)[0]);

watch(
  () => props.tableData,
  () => {
    nextTick(() => {
      props.tableData.forEach((item) => {
        const foundItem = props.selection.find((it) => it.fileId === item.fileId);
        tableRef.value!.toggleRowSelection(item, foundItem?.select === true);
      });
    });
  }
);
</script>
