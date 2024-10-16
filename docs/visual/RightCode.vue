<template>
  <div>
    <code-mirror v-model="code" :extensions="extensions" class="z-0" />
  </div>
</template>

<script setup lang="ts">
import CodeMirror from "vue-codemirror6";
import { yaml } from "@codemirror/lang-yaml";
import { oneDark } from "@codemirror/theme-one-dark";
import { computed } from "vue";
import jsyaml from "js-yaml";

const extensions = [yaml(), oneDark];
const props = defineProps<{
  jsonConfig: any;
}>();

const code = computed(() => {
  return jsyaml.dump(props.jsonConfig);
});
</script>
