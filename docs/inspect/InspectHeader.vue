<template>
  <div class="w-1/4 flex flex-row justify-between">
    <el-upload v-model:file-list="fileList" accept=".zip" multiple :show-file-list="false" :auto-upload="false">
      <el-button v-loading.fullscreen.lock="fullscreenLoading">批量上传</el-button>
    </el-upload>

    <el-button @click="emits('onDelete')" class="ml-3">批量删除</el-button>
    <el-button @click="handleGetExample">获取示例</el-button>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";
import type { UploadFile } from "element-plus";
import { ElNotification } from "element-plus";
import { useZip } from "./hook/useZip";

const fullscreenLoading = ref(false);
const fileList = ref<UploadFile[]>([]);

const emits = defineEmits(["uploadSuccess", "onDelete"]);

const handleOnChange = async () => {
  fullscreenLoading.value = false;

  const fileListPromise = fileList.value.map(async (item) => {
    const arrayBuffer = await item.raw!.arrayBuffer();
    const { raw, pic, added, extractZip } = useZip(arrayBuffer);
    await extractZip();
    return { raw, pic, added };
  });
  const result = await Promise.all(fileListPromise);
  const count = result.filter((item) => item.added.value === true).length;

  if (count > 0) emits("uploadSuccess");

  ElNotification({
    title: `上传完成 ${new Date().toLocaleString()}`,
    dangerouslyUseHTMLString: true,
    message: `
    <div>
        共需上传 ${result.length} 个文件</div>
    <div>
        检测重复 ${result.length - count} 个文件
    </div>
    <div>
        成功上传 ${count} 个文件
    </div>
    `,
    type: "success",
    duration: 0,
  });
};

const handleGetExample = async () => {
  const response = await fetch("/5f686585-c5d0-4057-93b2-c54832ffb393.zip");
  const arrayBuffer = await response.arrayBuffer();
  const { added, extractZip } = useZip(arrayBuffer);
  await extractZip();
  if (added.value === true) {
    emits("uploadSuccess");
    ElNotification({
      title: "示例文件添加成功",
      message: "示例文件添加成功",
      type: "success",
    });
  } else
    ElNotification({
      title: "示例文件已存在",
      message: "示例文件已存在，无需重复添加",
      type: "warning",
    });
};

onMounted(() => {
  const el = document.querySelector<HTMLInputElement>(".el-upload__input")!;
  el.oncancel = () => {
    fullscreenLoading.value = false;
  };
  el.onclick = () => {
    fileList.value.length = 0;
    fullscreenLoading.value = true;
  };
  el.onchange = () => handleOnChange();
});
</script>
