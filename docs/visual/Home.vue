<template>
  <div>
    <el-container>
      <el-header><Header :json-config="jsonConfig" /></el-header>
      <el-main>
        <el-row>
          <el-col :span="12"><LeftForm :json-config="jsonConfig" /></el-col>
          <el-col :span="12"><RightCode :json-config="jsonConfig" /></el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import LeftForm from "./LeftForm.vue";
import RightCode from "./RightCode.vue";
import Header from "./Header.vue";
import jsyaml from "js-yaml";

const jsonConfig = ref<any>(null);

onMounted(async () => {
  const response = await fetch("https://skip.guoxicheng.top/skip_config_v3.yaml");
  const skipYamlConfig = await response.text();
  jsonConfig.value = jsyaml.load(skipYamlConfig);
});
</script>
