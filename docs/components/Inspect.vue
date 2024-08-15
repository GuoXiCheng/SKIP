<template>
    <el-tree style="max-width: 600px" :data="treeData" :props="defaultProps" @node-click="handleNodeClick" />
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue'

interface Tree {
    label: string
    children?: Tree[]
}

interface AccessibilityNode {
    childCount: number;
    className: string;
    depth: number;
    nodeId: number;
    parentId: number;
    viewIdResourceName: string;
    text: string;
}

const handleNodeClick = (data: Tree) => {
    console.log(data)
}

const defaultProps = {
    children: 'children',
    label: 'label',
}

const treeData = ref<Tree[]>([])

onMounted(async () => {
    const temp = await fetch('/temp.json');
    const text = await temp.text();
    const data = JSON.parse(text) as AccessibilityNode[];
    treeData.value = buildTree(data, -1);
});

function buildTree(data: AccessibilityNode[], parentId: number): Tree[] {
    const children = data.filter(node => node.parentId === parentId);
    return children.map(node => {
        return {
            label: node.childCount === 0 ? node.className : `${node.className} [${node.childCount}]`,
            children: buildTree(data, node.nodeId)
        }
    })
}
</script>