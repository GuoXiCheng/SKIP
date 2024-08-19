<template>
    <img :src="imgSrc" alt="node-pic" />
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue'

const imgSrc = ref<string>('');

onMounted(async () => {
    const img = await fetch('/temp.png');
    const blob = await img.blob();
    imgSrc.value = URL.createObjectURL(blob);
})
</script>

<style scoped>
img {
    padding: 5px;
}
</style>