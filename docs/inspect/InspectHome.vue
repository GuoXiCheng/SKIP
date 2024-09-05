<template>
  <template v-if="isShowInspectContainer">
    <InspectContainer :raw="raw" :pic="pic" />
  </template>
  <template v-else>
    <el-container>
      <el-header class="flex items-center"><InspectHeader @upload-success="handleUploadSuccess" /></el-header>
      <el-main><InspectTable :table-data="tableData" /></el-main>
    </el-container>
  </template>
</template>
<script lang="ts" setup>
import { ref, onMounted } from "vue";
import InspectContainer from "./InspectContainer.vue";
import InspectTable from "./InspectTable.vue";
import InspectHeader from "./InspectHeader.vue";
import { NodeDB } from "./MyDB";
import { AccessibilityWindow, FileTableData } from "./types";
import { useZip } from "./hook/useZip";

const isShowInspectContainer = ref(false);
const raw = ref<AccessibilityWindow | null>(null);
const pic = ref<Blob | null>(null);
const tableData = ref<FileTableData[]>([]);

const refreshTable = async () => {
  const nodeInfoList = await NodeDB.getAllNodeInfo();
  tableData.value = nodeInfoList
    .sort((a, b) => b.createTime - a.createTime)
    .map((item) => ({
      fileId: item.fileId,
      createTime: new Date(item.createTime).toLocaleString(),
      appName: item.appName,
      packageName: item.packageName,
      activityName: item.activityName,
    }));
};

const handleUploadSuccess = () => {
  refreshTable();
};

onMounted(async () => {
  const params = new URLSearchParams(window.location.href.split("?")[1]);
  const fileId = params.get("fileId");
  if (fileId == null) {
    isShowInspectContainer.value = false;
    refreshTable();
    return;
  }

  const nodeInfo = await NodeDB.getNodeInfo(fileId);
  if (nodeInfo != null) {
    raw.value = nodeInfo.raw;
    pic.value = nodeInfo.pic;
    isShowInspectContainer.value = true;
    return;
  }

  const response = await fetch(`/${fileId}.zip`);
  const arrayBuffer = await response.arrayBuffer();
  const { raw: rawJson, pic: picBlob, extractZip } = useZip(arrayBuffer);
  await extractZip();
  raw.value = rawJson.value as AccessibilityWindow;
  pic.value = picBlob.value as Blob;
  isShowInspectContainer.value = true;
});
</script>
