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
        <InspectTable
          :table-data="tableData"
          :app-filters="appFilters"
          :selection="selection"
          @on-select="handleOnSelect"
          @on-select-all="handleOnSelectAll"
          @on-sort-change="handleSortChange"
          @on-filter-change="handleFilterChange"
        />
      </el-main>
      <el-footer>
        <el-pagination
          background
          hide-on-single-page
          layout="total, prev, pager, next"
          :total="tableDataTotal"
          :current-page="currentPage"
          @current-change="handleCurrentPageChange"
        />
      </el-footer>
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
const tableDataTotal = ref(0);
const currentPage = ref(1);
const tableDataSort = ref<"ascending" | "descending" | null>(null);
const tableDataFilter = ref<string[]>([]);
const selection = ref<{ fileId: string; select: boolean }[]>([]);
const appFilters = ref<{ text: string; value: string }[]>([]);

const refreshTable = async () => {
  let totalFileTable = await FileTable.findAllFileTable();

  const map = new Map<string, string>();
  totalFileTable.forEach((item) => {
    map.set(item.packageName, item.appName);
  });
  appFilters.value = Array.from(map).map((item) => ({ text: item[1], value: item[0] }));

  if (tableDataFilter.value.length > 0) {
    totalFileTable = totalFileTable.filter((item) => tableDataFilter.value.includes(item.packageName));
  }

  if (tableDataSort.value) {
    totalFileTable.sort((a, b) => {
      if (tableDataSort.value === "ascending") {
        return a.createTime - b.createTime;
      }
      return b.createTime - a.createTime;
    });
  }

  const pageSize = 10;
  const startIndex = (currentPage.value - 1) * pageSize;
  const endIndex = startIndex + pageSize;
  tableData.value = totalFileTable.slice(startIndex, endIndex);
  tableDataTotal.value = totalFileTable.length;
};

const handleDelete = async () => {
  await Promise.all(selection.value.map((item) => MyIndexDB.deleteFileData(item.fileId)));
  await refreshTable();
  selection.value = [];
};

const handleOnSelect = (selectItem: { fileId: string; select: boolean }) => {
  const foundIndex = selection.value.findIndex((item) => item.fileId === selectItem.fileId);
  if (foundIndex === -1) {
    selection.value.push(selectItem);
  } else {
    selection.value.at(foundIndex)!.select = selectItem.select;
  }
};

const handleOnSelectAll = (selectItems: { fileId: string; select: boolean }[]) => {
  selectItems.forEach((item) => {
    const foundIndex = selection.value.findIndex((selectItem) => selectItem.fileId === item.fileId);
    if (foundIndex === -1) {
      selection.value.push(item);
    } else {
      selection.value.at(foundIndex)!.select = item.select;
    }
  });
};

const handleCurrentPageChange = (page: number) => {
  currentPage.value = page;
  refreshTable();
};

const handleSortChange = (order: "ascending" | "descending" | null) => {
  tableDataSort.value = order;
  refreshTable();
};

const handleFilterChange = (filters: string[]) => {
  tableDataFilter.value = filters;
  refreshTable();
};

onMounted(async () => {
  const params = new URLSearchParams(window.location.href.split("?")[1]);
  const fileId = params.get("fileId");
  if (fileId == null) {
    isShowInspectContainer.value = false;
    await refreshTable();
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
