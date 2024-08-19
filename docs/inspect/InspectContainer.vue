<template>
    <el-row>
        <el-col :span="8">
            <NodePic />
        </el-col>
        <el-col :span="8">
            <NodeTree :tree-data="treeData" @handleNodeClick="handleNodeClick" />
        </el-col>
        <el-col :span="8">
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

onMounted(async () => {
    const temp = await fetch('/temp.json');
    const text = await temp.text();
    const data = JSON.parse(text) as AccessibilityWindow;
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
        }
    })
}

const handleNodeClick = (data: AccessibilityNode) => {
    nodeData.value = data;
}
</script>