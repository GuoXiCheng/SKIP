<template>
    <el-tree style="max-width: 600px" :data="treeData" :props="props.treeData" @node-click="handleNodeClick" />
</template>

<script lang="ts" setup>
import { defineProps, defineEmits } from 'vue'
import { Tree, AccessibilityNodeTree } from './types'

const emit = defineEmits(['handleNodeClick']);

const props = defineProps<{
    treeData: AccessibilityNodeTree[]
}>();

const handleNodeClick = (data: Tree) => {
    emit('handleNodeClick', data)
}
</script>