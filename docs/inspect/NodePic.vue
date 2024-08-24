<template>
  <div class="h-full relative flex justify-center">
    <div id="subWindow" class="bg-green-500 absolute z-30 hidden">
      <div>{{ position.x }},{{ position.y }}</div>
    </div>
    <canvas id="topCanvas" class="cursor-crosshair absolute z-20"></canvas>
    <canvas id="bottomCanvas" class="cursor-crosshair absolute z-10"></canvas>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, watch, ref } from "vue";
import { AccessibilityWindow } from "./types";

const emit = defineEmits(["handleImgNodeClick"]);
const renderUI = ref({ renderWidth: 0, renderHeight: 0 });
const renderRectangle = ref({ node: {}, left: 0, top: 0, right: 0, bottom: 0 });
const position = ref({ x: 0, y: 0 });

const props = defineProps<{
  rawData: AccessibilityWindow | null;
  imgSrc: string;
  currentNodeKey: number;
}>();

watch(
  () => renderRectangle.value,
  (newVal, oldVal) => {
    if (
      oldVal.left === newVal.left &&
      oldVal.top === newVal.top &&
      oldVal.bottom === newVal.bottom &&
      oldVal.right === newVal.right
    )
      return;

    const topCanvas = document.getElementById("topCanvas") as HTMLCanvasElement;
    const topCtx = topCanvas.getContext("2d") as CanvasRenderingContext2D;

    let startLeft = oldVal.left;
    let startTop = oldVal.top;
    let startWidth = oldVal.right - oldVal.left;
    let startHeight = oldVal.bottom - oldVal.top;

    let endLeft = newVal.left;
    let endTop = newVal.top;
    let endWidth = newVal.right - newVal.left;
    let endHeight = newVal.bottom - newVal.top;

    let startTime: number | null = null;
    const duration = 150; // 动画持续时间，单位为毫秒

    function animate(time: number) {
      if (!startTime) {
        startTime = time;
      }
      const elapsed = time - startTime;
      const progress = Math.min(elapsed / duration, 1);

      const currentLeft = startLeft + (endLeft - startLeft) * progress;
      const currentTop = startTop + (endTop - startTop) * progress;
      const currentWidth = startWidth + (endWidth - startWidth) * progress;
      const currentHeight = startHeight + (endHeight - startHeight) * progress;

      topCtx.clearRect(0, 0, topCanvas.width, topCanvas.height);
      topCtx.strokeStyle = "red";
      topCtx.lineWidth = 2;
      topCtx.strokeRect(currentLeft, currentTop, currentWidth, currentHeight);

      if (progress < 1) {
        requestAnimationFrame(animate);
      } else {
        // 动画结束后的操作
        oldVal = newVal;
      }
    }

    requestAnimationFrame(animate);
  }
);

watch(
  () => props.currentNodeKey,
  (newVal) => {
    if (props.rawData === null) return;

    const raw = props.rawData as AccessibilityWindow;
    const node = raw.nodes.find((node) => node.nodeId === newVal);
    if (node == null) return;

    const bottomCanvas = document.getElementById("bottomCanvas") as HTMLCanvasElement;
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
      bottom: renderBottom,
    };
  }
);

onMounted(() => {
  const raw = props.rawData as AccessibilityWindow;
  if (!raw) return;

  const bottomCanvas = document.getElementById("bottomCanvas") as HTMLCanvasElement;
  const topCanvas = document.getElementById("topCanvas") as HTMLCanvasElement;
  const bottomCtx = bottomCanvas.getContext("2d") as CanvasRenderingContext2D;

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
  };

  topCanvas.addEventListener("click", (event) => {
    const { offsetX, offsetY } = event;
    let minDistance = Infinity;

    raw.nodes.forEach((node) => {
      const rateW = bottomCanvas.width / raw.screenWidth;
      const rateH = bottomCanvas.height / raw.screenHeight;

      const renderLeft = node.left * rateW;
      const renderRight = node.right * rateW;
      const renderTop = node.top * rateH;
      const renderBottom = node.bottom * rateH;

      const centerX = (renderLeft + renderRight) / 2;
      const centerY = (renderTop + renderBottom) / 2;
      const distance = Math.sqrt(Math.pow(offsetX - centerX, 2) + Math.pow(offsetY - centerY, 2));
      if (
        distance < minDistance &&
        offsetX >= renderLeft &&
        offsetX <= renderRight &&
        offsetY >= renderTop &&
        offsetY <= renderBottom
      ) {
        minDistance = distance;
        renderRectangle.value = {
          node,
          left: renderLeft,
          right: renderRight,
          top: renderTop,
          bottom: renderBottom,
        };
        emit("handleImgNodeClick", node);
      }
    });
  });

  topCanvas.addEventListener("mouseenter", () => {
    const subWindow = document.getElementById("subWindow") as HTMLDivElement;
    subWindow.classList.remove("hidden");

    subWindow.style.left = topCanvas.parentElement?.clientWidth! + "px";
    subWindow.style.top = topCanvas.parentElement?.clientWidth! / 10 + "px";
  });

  topCanvas.addEventListener("mouseleave", () => {
    const subWindow = document.getElementById("subWindow") as HTMLDivElement;
    subWindow.classList.add("hidden");
  });

  topCanvas.addEventListener("mousemove", (event) => {
    const { offsetX, offsetY } = event;
    const rateW = topCanvas.width / raw.screenWidth;
    const rateH = topCanvas.height / raw.screenHeight;
    position.value = {
      x: Math.round(offsetX / rateW),
      y: Math.round(offsetY / rateH),
    };
  });
});
</script>
