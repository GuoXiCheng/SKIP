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
import { useZip } from "./hook/useZip";

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
  const { raw: rawJson, pic: picBlob, extractZip } = useZip(arrayBuffer);
  await extractZip();
  raw.value = rawJson.value as AccessibilityWindow;
  pic.value = picBlob.value as Blob;
  isShowInspectContainer.value = true;
});
</script>
