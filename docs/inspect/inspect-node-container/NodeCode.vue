<template>
  <div class="p-2" v-if="code">
    <div class="relative">
      <el-button size="small" @click="handleClickCopy" class="absolute top-2 right-2 z-50">{{
        copyButtonText
      }}</el-button>
      <code-mirror v-model="code" :extensions="extensions" class="z-0" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { oneDark } from "@codemirror/theme-one-dark";
import { yaml } from "@codemirror/lang-yaml";
import CodeMirror from "vue-codemirror6";
import { watch, ref } from "vue";
import { AccessibilityNode, AccessibilityWindow } from "../types";
import jsyaml from "js-yaml";

const copyButtonText = ref("复制");
const code = ref<string | null>(null);

const extensions = [yaml(), oneDark];
const props = defineProps<{
  nodeData: AccessibilityNode | null;
  rawData: AccessibilityWindow | null;
  currentNodeKey: number;
}>();

watch(
  () => props.currentNodeKey,
  () => {
    if (!props.nodeData || !props.rawData) return;
    const { packageName, activityName, appName, fileId } = props.rawData;
    const { viewIdResourceName, text, left, bottom, right, top } = props.nodeData;
    const obj = {
      appName,
      packageName,
    };
    const file = `fileId=${fileId}&nodeId=${props.currentNodeKey}`;
    const desc = "请填写描述";
    if (viewIdResourceName) {
      Object.assign(obj, {
        skipIds: [
          {
            id: viewIdResourceName,
            activityName,
            file,
            desc,
          },
        ],
      });
      code.value = jsyaml.dump([obj]);
      return;
    } else if (text) {
      Object.assign(obj, {
        skipTexts: [
          {
            text,
            length: text.length,
            activityName,
            file,
            desc,
          },
        ],
      });
      code.value = jsyaml.dump([obj]);
      return;
    } else {
      Object.assign(obj, {
        skipBounds: [
          {
            bound: [left, top, right, bottom].join(","),
            activityName,
            file,
            desc,
          },
        ],
      });
      code.value = jsyaml.dump([obj]);
      return;
    }
  }
);

function handleClickCopy() {
  if (!code.value) return;
  copyButtonText.value = "已复制✔";
  navigator.clipboard.writeText(code.value);
  setTimeout(() => {
    copyButtonText.value = "复制";
  }, 1000);
}
</script>
