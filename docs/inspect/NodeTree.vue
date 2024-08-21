<template>
    <el-tree :data="treeData" :props="{ class: customNodeClass }" @node-click="handleNodeClick"
        :current-node-key="currentNodeKey" :expand-on-click-node="false" node-key="nodeId" />
</template>

<script lang="ts" setup>
import { Tree, AccessibilityNodeTree } from './types'
import type Node from 'element-plus/es/components/tree/src/model/node'

const emit = defineEmits(['handleTreeNodeClick']);

const props = defineProps<{
    treeData: AccessibilityNodeTree[],
    currentNodeKey: number
}>();

const handleNodeClick = (data: Tree) => {
    emit('handleTreeNodeClick', data)
}

const customNodeClass = (data: Tree, node: Node) => {
    return node.isCurrent ? 'text-red-500' : '';
}
</script>