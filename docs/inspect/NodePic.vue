<template>
    <div class="h-full relative flex justify-center">
        <canvas id="topCanvas" class="cursor-crosshair absolute z-20"></canvas>"
        <canvas id="bottomCanvas" class="cursor-crosshair absolute z-10"></canvas>
    </div>
</template>

<script lang="ts" setup>
import { onMounted, watch, ref } from 'vue'
import { AccessibilityWindow } from './types';

const emit = defineEmits(['handleImgNodeClick']);
const renderUI = ref({ renderWidth: 0, renderHeight: 0 });
const renderRectangle = ref({ node: {}, left: 0, top: 0, right: 0, bottom: 0 });

const props = defineProps<{
    rawData: AccessibilityWindow | null;
    imgSrc: string;
    currentNodeKey: number;
}>();

watch(() => renderRectangle.value, (newVal) => {
    const topCanvas = document.getElementById('topCanvas') as HTMLCanvasElement;
    const topCtx = topCanvas.getContext('2d') as CanvasRenderingContext2D;

    topCtx.clearRect(0, 0, topCanvas.width, topCanvas.height);
    topCtx.strokeStyle = 'red';
    topCtx.lineWidth = 2;
    topCtx.strokeRect(newVal.left, newVal.top, newVal.right - newVal.left, newVal.bottom - newVal.top);
});

watch(() => props.currentNodeKey, (newVal) => {
    if (props.rawData === null) return;

    const raw = props.rawData as AccessibilityWindow;
    const node = raw.nodes.find(node => node.nodeId === newVal);
    if (node == null) return;

    const bottomCanvas = document.getElementById('bottomCanvas') as HTMLCanvasElement;
    const rateW = bottomCanvas.width / raw.screenWidth;
    const rateH = bottomCanvas.height / raw.screenHeight;

    const renderLeft = node.left * rateW;
    const renderRight = node.right * rateW;
    const renderTop = node.top * rateH;
    const renderBottom = node.bottom * rateH;

    renderRectangle.value = {
        node,
        left: renderLeft,
        right: renderRight,
        top: renderTop,
        bottom: renderBottom
    };
})


onMounted(() => {
    const raw = props.rawData as AccessibilityWindow;
    if (!raw) return;

    const bottomCanvas = document.getElementById('bottomCanvas') as HTMLCanvasElement;
    const topCanvas = document.getElementById('topCanvas') as HTMLCanvasElement;
    const bottomCtx = bottomCanvas.getContext('2d') as CanvasRenderingContext2D;

    const img = new Image();
    img.src = props.imgSrc;
    img.onload = function () {
        const parentWidth = bottomCanvas.parentElement!.clientWidth;
        const parentHeight = bottomCanvas.parentElement!.clientHeight;

        const parentAspect = parentWidth / parentHeight;
        const imgAspect = img.width / img.height;

        if (imgAspect < parentAspect) {
            // 纵向填充
            renderUI.value.renderHeight = parentHeight;
            renderUI.value.renderWidth = img.width * (parentHeight / img.height);
        } else if (imgAspect > parentAspect) {
            // 横向填充
            renderUI.value.renderWidth = parentWidth;
            renderUI.value.renderHeight = img.height * (parentWidth / img.width);
        } else {
            // 完全匹配
            renderUI.value.renderWidth = parentWidth;
            renderUI.value.renderHeight = parentHeight;
        }

        bottomCanvas.width = renderUI.value.renderWidth;
        bottomCanvas.height = renderUI.value.renderHeight;
        topCanvas.width = renderUI.value.renderWidth;
        topCanvas.height = renderUI.value.renderHeight;

        bottomCtx.drawImage(img, 0, 0, renderUI.value.renderWidth, renderUI.value.renderHeight);
    }

    topCanvas.addEventListener('click', (event) => {
        const { offsetX, offsetY } = event;
        let minDistance = Infinity;

        raw.nodes.forEach(node => {
            const rateW = bottomCanvas.width / raw.screenWidth;
            const rateH = bottomCanvas.height / raw.screenHeight;

            const renderLeft = node.left * rateW;
            const renderRight = node.right * rateW;
            const renderTop = node.top * rateH;
            const renderBottom = node.bottom * rateH;

            const centerX = (renderLeft + renderRight) / 2;
            const centerY = (renderTop + renderBottom) / 2;
            const distance = Math.sqrt(Math.pow(offsetX - centerX, 2) + Math.pow(offsetY - centerY, 2));
            if (distance < minDistance && offsetX >= renderLeft && offsetX <= renderRight && offsetY >= renderTop && offsetY <= renderBottom) {
                minDistance = distance;
                renderRectangle.value = {
                    node,
                    left: renderLeft,
                    right: renderRight,
                    top: renderTop,
                    bottom: renderBottom
                };
                emit('handleImgNodeClick', node);
            }
        })
    })
});
</script>