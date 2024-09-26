<template>
  <el-row class="h-screen">
    <el-col :span="6">
      <NodePic
        :raw-data="rawData"
        :img-src="imgSrc"
        :current-node-key="currentNodeKey"
        @handle-img-node-click="handleImgNodeClick"
        v-if="rawData"
      />
    </el-col>
    <el-col :span="18" class="h-full flex">
      <NodeTree
        :tree-data="treeData"
        :current-node-key="currentNodeKey"
        @handle-tree-node-click="handleTreeNodeClick"
        v-if="treeData"
        class="overflow-y-auto hide-scrollbar"
        :style="{ width: `${nodeTreeWidth}px` }"
      />
      <div class="bg-gray-400 w-2 cursor-col-resize hover:bg-blue-700 transition duration-300" ref="colResize"></div>
      <div class="flex flex-col">
        <NodeTable :node-data="nodeData" :raw-data="rawData" v-if="rawData" :style="{ width: `${nodeTableWidth}px` }" />
        <NodeCode :node-data="nodeData" :raw-data="rawData" :current-node-key="currentNodeKey" />
      </div>
    </el-col>
  </el-row>
</template>

<script lang="ts" setup>
import NodeTree from "./NodeTree.vue";
import NodeTable from "./NodeTable.vue";
import NodePic from "./NodePic.vue";
import NodeCode from "./inspect-node-container/NodeCode.vue";
import { ref, onMounted, onBeforeUnmount } from "vue";
import { AccessibilityNode, AccessibilityNodeTree, AccessibilityWindow } from "./types";

const treeData = ref<AccessibilityNodeTree[]>([]);
const nodeData = ref<AccessibilityNode | null>(null);
const rawData = ref<AccessibilityWindow | null>(null);
const currentNodeKey = ref<number>(-1);
const imgSrc = ref<string>("");
const colResize = ref<HTMLDivElement>();
const nodeTreeWidth = ref<number>(0);
const nodeTableWidth = ref<number>(0);

const props = defineProps<{ raw: AccessibilityWindow | null; pic: Blob | null }>();

onMounted(async () => {
  const initColWidth = (window.innerWidth / 24) * 9;
  nodeTreeWidth.value = initColWidth;
  nodeTableWidth.value = initColWidth;

  const { raw, pic } = props;
  if (raw == null || pic == null) {
    return;
  }

  const imgUrl = URL.createObjectURL(pic);
  imgSrc.value = imgUrl;

  rawData.value = raw;
  treeData.value = buildTree(raw.nodes, -1);

  colResize.value?.addEventListener("mousedown", handleMouseDown);
});

const handleColMove = (e: MouseEvent) => {
  const value = Math.abs(e.movementX);
  if (e.movementX < 0) {
    nodeTreeWidth.value -= value;
    nodeTableWidth.value += value;
  } else {
    nodeTreeWidth.value += value;
    nodeTableWidth.value -= value;
  }
};

const handleMouseUp = () => {
  document.removeEventListener("mousemove", handleColMove);
  document.removeEventListener("mouseup", handleMouseUp);
};

const handleMouseDown = () => {
  document.addEventListener("mousemove", handleColMove);
  document.addEventListener("mouseup", handleMouseUp);
};

onBeforeUnmount(() => {
  colResize.value?.removeEventListener("mousedown", handleMouseDown);
  document.removeEventListener("mousemove", handleColMove);
  document.removeEventListener("mouseup", handleMouseUp);
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
