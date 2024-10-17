<template>
  <a v-if="latestVersion" :href="downloadUrl" target="_blank" rel="noreferrer"> {{ downloadApkName }} </a>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";

const latestVersion = ref<null | string>(null);
const downloadUrl = ref<string>("");
const downloadApkName = ref<string>("");

onMounted(async () => {
  const response = await fetch("https://skip.guoxicheng.top/latest_version.txt");
  if (response.status !== 200) return;

  latestVersion.value = await response.text();
  downloadUrl.value = `https://skip.guoxicheng.top/SKIP-v${latestVersion.value}.apk`;
  downloadApkName.value = `SKIP-v${latestVersion.value}.apk`;
});
</script>
