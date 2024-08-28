---
layout: false
---

<script setup>
    import InspectContainer from './InspectContainer.vue';
    import InspectTable from './InspectTable.vue';
    import {onMounted, ref} from 'vue';
    const fileId = ref(null);
    onMounted(() => {
        const params = new URLSearchParams(window.location.href.split("?")[1]);
        fileId.value = params.get('fileId');
    });
</script>

<template v-if="fileId">
    <InspectContainer />
</template>
<template v-else>
    <InspectTable />
</template>
