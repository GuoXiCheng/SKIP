<template>
    <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="key" label="属性" width="auto" />
        <el-table-column prop="value" label="值" width="auto" />
    </el-table>
</template>

<script lang="ts" setup>
import { defineProps, computed } from 'vue'
import { AccessibilityNode } from './types'

const excludeKeys = ['label', 'children', 'childCount'];

const props = defineProps<{
    nodeData: AccessibilityNode | null
}>();

const tableData = computed(() => {
    if (!props.nodeData) return [];
    const temp: { key: string; value: string }[] = [];
    for (const key in props.nodeData) {
        if (excludeKeys.includes(key)) continue;
        temp.push({ key, value: props.nodeData[key] });
    }
    return temp;
})
</script>