<template>
  <div class="p-2">
    <el-segmented v-model="segmentedValue" :options="options" block />
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="key" label="属性" width="auto" />
      <el-table-column prop="value" label="值" width="auto" />
    </el-table>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch } from "vue";
import { AccessibilityNode, AccessibilityWindow } from "./types";

const options = ["Window", "Node"];
const segmentedValue = ref("Node");
const tableData = ref<{ key: string; value: any }[]>([]);

const props = defineProps<{
  rawData: AccessibilityWindow | null;
  nodeData: AccessibilityNode | null;
}>();

function buildWindowTableData(data: AccessibilityWindow | null) {
  if (!data) return [];
  return [
    {
      key: "设备名称",
      value: data.deviceName,
    },
    {
      key: "应用名称",
      value: data.appName,
    },
    {
      key: "包名",
      value: data.packageName,
    },
    {
      key: "Activity 名称",
      value: data.activityName,
    },
    {
      key: "屏幕分辨率",
      value: `${data.screenWidth}x${data.screenHeight}`,
    },
    {
      key: "创建时间",
      value: new Date(data.createTime).toLocaleString(),
    },
  ];
}

function buildNodeTableData(data: AccessibilityNode | null) {
  if (!data) return [];
  return [
    { key: "viewIdResourceName", value: data.viewIdResourceName },
    { key: "className", value: data.className },
    { key: "text", value: data.text },
    { key: "textLength", value: data.text?.length },
    { key: "bound(left,top,right,bottom)", value: `${data.left},${data.top},${data.right},${data.bottom}` },
    { key: "childCount", value: data.childCount },
    { key: "isClickable", value: data.isClickable },
    { key: "nodeId", value: data.nodeId },
  ];
}

watch(
  () => segmentedValue.value,
  (newVal) => {
    if (newVal === "Window") {
      tableData.value = buildWindowTableData(props.rawData);
    } else if (newVal === "Node") {
      tableData.value = buildNodeTableData(props.nodeData);
    }
  },
  { immediate: true }
);

watch(
  () => props.nodeData,
  (newVal) => {
    if (segmentedValue.value === "Node") {
      tableData.value = buildNodeTableData(newVal!);
    }
  }
);
</script>
