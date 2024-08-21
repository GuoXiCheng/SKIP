<template>
    <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="key" label="属性" width="auto" />
        <el-table-column prop="value" label="值" width="auto" />
    </el-table>
</template>

<script lang="ts" setup>
import { computed } from 'vue'
import { AccessibilityNode } from './types'

const props = defineProps<{
    nodeData: AccessibilityNode | null
}>();

const tableData = computed(() => {
    if (!props.nodeData) return [];
    const selectedKey: (keyof AccessibilityNode)[] = [
        'viewIdResourceName', 'className', 'text', 'left', 'top', 'right', 'bottom', 'childCount'
    ];

    const temp = selectedKey.map((item: keyof AccessibilityNode) => {
        return {
            key: item,
            value: props!.nodeData![item]
        }
    })

    return temp;
})
</script>