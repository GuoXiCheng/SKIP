<template>
  <div>配置版本号: {{ configVersion }}</div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import CryptoJS from "crypto-js";

const props = defineProps<{
  jsonConfig: any;
}>();

const configVersion = computed(() => md5(JSON.stringify(props.jsonConfig)));

function md5(input: string) {
  return CryptoJS.MD5(CryptoJS.enc.Utf8.parse(input)).toString(CryptoJS.enc.Hex);
}
</script>
