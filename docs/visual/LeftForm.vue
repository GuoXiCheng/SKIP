<template>
  <div>
    <el-collapse v-model="activeNames" @change="handleChange">
      <template v-for="item in props.jsonConfig">
        <el-collapse-item :title="item.appName" :name="item.packageName">
          <!-- <div v-for="content in item.content">{{ content }}</div> -->
        </el-collapse-item>
      </template>
    </el-collapse>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

const activeNames = ref(["1"]);
const handleChange = (val: string[]) => {
  console.log(val);
};

const props = defineProps<{
  jsonConfig: any;
}>();
</script>
