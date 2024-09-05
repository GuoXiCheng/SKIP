<template>
  <div>
    <el-upload v-model:file-list="fileList" accept=".zip" multiple :show-file-list="false" :auto-upload="false">
      <el-button v-loading.fullscreen.lock="fullscreenLoading">批量上传</el-button>
    </el-upload>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";
import type { UploadFile } from "element-plus";
import { ElNotification } from "element-plus";
import { useZip } from "./hook/useZip";

const fullscreenLoading = ref(false);
const fileList = ref<UploadFile[]>([]);

const emits = defineEmits(["uploadSuccess"]);

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
