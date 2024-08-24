<template>
  <el-row class="h-screen" v-if="rawData">
    <el-col :span="6">
      <NodePic
        :raw-data="rawData"
        :img-src="imgSrc"
        :current-node-key="currentNodeKey"
        @handle-img-node-click="handleImgNodeClick"
      />
    </el-col>
    <el-col :span="9" class="h-full overflow-y-auto">
      <NodeTree
        :tree-data="treeData"
        :current-node-key="currentNodeKey"
        @handle-tree-node-click="handleTreeNodeClick"
      />
    </el-col>
    <el-col :span="9">
      <NodeTable :node-data="nodeData" :raw-data="rawData" />
    </el-col>
  </el-row>
</template>

<script lang="ts" setup>
import NodeTree from "./NodeTree.vue";
import NodeTable from "./NodeTable.vue";
import NodePic from "./NodePic.vue";
import { ref, onMounted } from "vue";
import { AccessibilityNode, AccessibilityNodeTree, AccessibilityWindow } from "./types";
import JSZip from "jszip";

const treeData = ref<AccessibilityNodeTree[]>([]);
const nodeData = ref<AccessibilityNode | null>(null);
const rawData = ref<AccessibilityWindow | null>(null);
const currentNodeKey = ref<number>(-1);
const imgSrc = ref<string>("");

onMounted(async () => {
  const response = await fetch("/94e04ea4-2502-4c24-a7f1-7574270fa921.zip");
  const arrayBuffer = await response.arrayBuffer();
  const zip = await JSZip.loadAsync(arrayBuffer);

  const pngFile = zip.filter((relativePath, file) => relativePath.endsWith(".png"));
  const blob = await pngFile[0].async("blob");
  const imgUrl = URL.createObjectURL(blob);
  imgSrc.value = imgUrl;

  const jsonFile = zip.filter((relativePath, file) => relativePath.endsWith(".json"));
  const jsonText = await jsonFile[0].async("text");
  const data = JSON.parse(jsonText) as AccessibilityWindow;
  rawData.value = data;
  treeData.value = buildTree(data.nodes, -1);
});

function buildTree(data: AccessibilityNode[], parentId: number): AccessibilityNodeTree[] {
  const children = data.filter((node) => node.parentId === parentId);
  return children.map((node) => {
    const { childCount, className, nodeId, text, viewIdResourceName, left, top, right, bottom, isClickable } = node;
    return {
      label: childCount === 0 ? className : `${className} [${childCount}]`,
      children: buildTree(data, nodeId),
      text,
      className,
      childCount,
      viewIdResourceName,
      left,
      top,
      right,
      bottom,
      nodeId,
      isClickable,
    };
  });
}

const handleTreeNodeClick = (data: AccessibilityNode) => {
  nodeData.value = data;
  currentNodeKey.value = data.nodeId;
};

const handleImgNodeClick = (data: AccessibilityNode) => {
  nodeData.value = data;
  currentNodeKey.value = data.nodeId;
};
</script>
