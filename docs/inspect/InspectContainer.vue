<template>
    <el-row class="h-screen">
        <el-col :span="6">
            <NodePic v-if="rawData" :raw-data="rawData" :img-src="'/temp.png'" :current-node-key="currentNodeKey"
                @handle-img-node-click="handleImgNodeClick" />
        </el-col>
        <el-col :span="9" class="h-full overflow-y-auto">
            <NodeTree v-if="treeData" :tree-data="treeData" :current-node-key="currentNodeKey"
                @handle-tree-node-click="handleTreeNodeClick" />
        </el-col>
        <el-col :span="9">
            <NodeTable :node-data="nodeData" />
        </el-col>
    </el-row>
</template>

<script lang="ts" setup>
import NodeTree from './NodeTree.vue';
import NodeTable from './NodeTable.vue';
import NodePic from './NodePic.vue';
import { ref, onMounted } from 'vue';
import { AccessibilityNode, AccessibilityNodeTree, AccessibilityWindow } from './types';

const treeData = ref<AccessibilityNodeTree[]>([]);
const nodeData = ref<AccessibilityNode | null>(null);
const rawData = ref<AccessibilityWindow | null>(null);
const currentNodeKey = ref<number>(-1);

onMounted(async () => {
    const temp = await fetch('/temp.json');
    const text = await temp.text();
    const data = JSON.parse(text) as AccessibilityWindow;
    rawData.value = data;
    treeData.value = buildTree(data.nodes, -1);
});

function buildTree(data: AccessibilityNode[], parentId: number): AccessibilityNodeTree[] {
    const children = data.filter(node => node.parentId === parentId);
    return children.map(node => {
        const { childCount, className, nodeId, text, viewIdResourceName, left, top, right, bottom } = node;
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
            nodeId
        }
    })
}

const handleTreeNodeClick = (data: AccessibilityNode) => {
    nodeData.value = data;
    currentNodeKey.value = data.nodeId;
}

const handleImgNodeClick = (data: AccessibilityNode) => {
    nodeData.value = data;
    currentNodeKey.value = data.nodeId;
}
</script>