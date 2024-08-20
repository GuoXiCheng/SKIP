<template>
    <canvas id="myCanvas" class="cursor-crosshair"></canvas>
</template>

<script lang="ts" setup>
import { onMounted } from 'vue'
import { AccessibilityNode, AccessibilityWindow } from './types';

const props = defineProps<{
    rawData: AccessibilityWindow | null;
    imgSrc: string;
}>();

onMounted(async () => {
    const data = props.rawData as AccessibilityWindow;
    if (!data) return;

    const canvas = document.getElementById('myCanvas') as HTMLCanvasElement;
    const ctx = canvas.getContext('2d') as CanvasRenderingContext2D;

    let renderableWidth: number, renderableHeight: number;
    // 加载背景图
    const img = new Image();
    img.src = props.imgSrc; // 在此替换为实际的图片URL
    img.onload = function () {
        const parentWidth = canvas.parentElement!.clientWidth;
        const parentHeight = canvas.parentElement!.clientHeight;

        const parentAspect = parentWidth / parentHeight;
        const imgAspect = img.width / img.height;

        if (imgAspect < parentAspect) {
            // 纵向填充
            renderableHeight = parentHeight;
            renderableWidth = img.width * (renderableHeight / img.height);
        } else if (imgAspect > parentAspect) {
            // 横向填充
            renderableWidth = parentWidth;
            renderableHeight = img.height * (renderableWidth / img.width);
        } else {
            // 完全匹配
            renderableWidth = parentWidth;
            renderableHeight = parentHeight;
        }

        canvas.width = renderableWidth;
        canvas.height = renderableHeight;

        ctx.drawImage(img, 0, 0, renderableWidth, renderableHeight);
    };

    canvas.addEventListener('click', (event) => {
        const { offsetX, offsetY } = event;

        let closestNode = null;
        let minDistance = Infinity;

        data.nodes.forEach(node => {
            const rateW = canvas.width / data.screenWidth;
            const rateH = canvas.height / data.screenHeight;

            const rateLeft = node.left * rateW;
            const rateRight = node.right * rateW;
            const rateTop = node.top * rateH;
            const rateBottom = node.bottom * rateH;

            const centerX = (rateLeft + rateRight) / 2;
            const centerY = (rateTop + rateBottom) / 2;
            const distance = Math.sqrt(Math.pow(offsetX - centerX, 2) + Math.pow(offsetY - centerY, 2));

            if (distance < minDistance && offsetX >= rateLeft && offsetX <= rateRight && offsetY >= rateTop && offsetY <= rateBottom) {
                minDistance = distance;
                closestNode = {
                    ...node,
                    left: rateLeft,
                    right: rateRight,
                    top: rateTop,
                    bottom: rateBottom,
                };
            }
        });

        if (closestNode !== null) {
            closestNode = closestNode as AccessibilityNode;
            // 重新绘制背景图
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            ctx.drawImage(img, 0, 0, renderableWidth, renderableHeight);

            // 绘制标记矩形
            ctx.strokeStyle = 'red';
            ctx.lineWidth = 2;
            ctx.strokeRect(closestNode.left, closestNode.top, closestNode.right - closestNode.left, closestNode.bottom - closestNode.top);
        }

    });
})
</script>