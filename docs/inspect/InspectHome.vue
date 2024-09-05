<template>
  <template v-if="isShowInspectContainer">
    <InspectContainer :raw="raw" :pic="pic" />
  </template>
  <template v-else>
    <el-container>
      <el-header class="flex items-center"><InspectHeader /></el-header>
      <el-main><InspectTable /></el-main>
    </el-container>
  </template>
</template>
<script lang="ts" setup>
import { ref, onMounted } from "vue";
import InspectContainer from "./InspectContainer.vue";
import InspectTable from "./InspectTable.vue";
import InspectHeader from "./InspectHeader.vue";
import { NodeDB } from "./MyDB";
import { AccessibilityWindow } from "./types";
import JSZip from "jszip";

const isShowInspectContainer = ref(false);
const raw = ref<AccessibilityWindow | null>(null);
const pic = ref<Blob | null>(null);

onMounted(async () => {
  const params = new URLSearchParams(window.location.href.split("?")[1]);
  const fileIdParam = params.get("fileId");
  if (fileIdParam == null) {
    isShowInspectContainer.value = false;
    return;
  }

  const fileId = parseInt(fileIdParam);
  const nodeInfo = await NodeDB.getNodeInfo(fileId);
  if (nodeInfo != null) {
    raw.value = nodeInfo.raw;
    pic.value = nodeInfo.pic;
    isShowInspectContainer.value = true;
    return;
  }

  const response = await fetch(`/${fileId}.zip`);
  const arrayBuffer = await response.arrayBuffer();
  const zip = await JSZip.loadAsync(arrayBuffer);

  const jpegFile = zip.filter((relativePath, file) => relativePath.endsWith(".jpeg"));
  const blob = await jpegFile[0].async("blob");

  const jsonFile = zip.filter((relativePath, file) => relativePath.endsWith(".json"));
  const jsonText = await jsonFile[0].async("text");
  const data = JSON.parse(jsonText) as AccessibilityWindow;

  raw.value = data;
  pic.value = blob;
  isShowInspectContainer.value = true;

  NodeDB.addNodeInfo({
    fileId: data.fileId,
    raw: data,
    pic: blob,
    appName: data.appName,
    packageName: data.packageName,
    activityName: data.activityName,
  });
});
</script>
