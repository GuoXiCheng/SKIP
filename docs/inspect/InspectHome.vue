<template>
  <template v-if="isShowInspectContainer == true">
    <InspectContainer :raw="raw" :pic="pic" />
  </template>
  <template v-else-if="isShowInspectContainer == false">
    <el-container>
      <el-header class="flex items-end">
        <InspectHeader @upload-success="refreshTable()" @on-delete="handleDelete" />
      </el-header>
      <el-main>
        <InspectTable :table-data="tableData" @on-selection-change="handleSelectionChange" />
      </el-main>
    </el-container>
  </template>
</template>
<script lang="ts" setup>
import { ref, onMounted } from "vue";
import InspectContainer from "./InspectContainer.vue";
import InspectTable from "./InspectTable.vue";
import InspectHeader from "./InspectHeader.vue";
import { AccessibilityWindow, FileTableData } from "./types";
import { useZip } from "./hook/useZip";
import { FileTable } from "../my-index-db/file-table";
import { FileItem } from "../my-index-db/file-item";
import { MyIndexDB } from "../my-index-db";

const isShowInspectContainer = ref();
const raw = ref<AccessibilityWindow | null>(null);
const pic = ref<Blob | null>(null);
const tableData = ref<FileTableData[]>([]);
const selection = ref<FileTableData[]>([]);

const refreshTable = async () => {
  tableData.value = await FileTable.findAllFileTable();
};

const handleDelete = async () => {
  await Promise.all(selection.value.map((item) => MyIndexDB.deleteFileData(item.fileId)));
  refreshTable();
};

const handleSelectionChange = (selectionData: FileTableData[]) => (selection.value = selectionData);

onMounted(async () => {
  const params = new URLSearchParams(window.location.href.split("?")[1]);
  const fileId = params.get("fileId");
  if (fileId == null) {
    isShowInspectContainer.value = false;
    refreshTable();
    return;
  }

  const fileItem = await FileItem.findFileItemByFileId(fileId);
  if (fileItem != null) {
    raw.value = fileItem.raw;
    pic.value = fileItem.pic;
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
