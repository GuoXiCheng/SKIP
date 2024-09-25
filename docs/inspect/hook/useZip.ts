import JSZip from "jszip";
import { AccessibilityWindow } from "../types";
import { ref } from "vue";
import { FileTable } from "../../my-index-db/file-table";
import { MyIndexDB } from "../../my-index-db";

export function useZip(arrayBuffer: ArrayBuffer) {
  const pic = ref<Blob>();
  const raw = ref<AccessibilityWindow>();
  const added = ref(false);

  const extractZip = async () => {
    const zip = await JSZip.loadAsync(arrayBuffer);

    const jpegFile = zip.filter((relativePath, file) => relativePath.endsWith(".jpeg"));
    const blobPromise = jpegFile[0].async("blob");

    const jsonFile = zip.filter((relativePath, file) => relativePath.endsWith(".json"));
    const jsonPromise = jsonFile[0].async("text");

    const [blobFile, jsonText] = await Promise.all([blobPromise, jsonPromise]);
    const jsonObj = JSON.parse(jsonText) as AccessibilityWindow;

    raw.value = jsonObj;
    pic.value = blobFile;

    const { fileId, appName, packageName, deviceName, createTime } = jsonObj;
    const targetFileTable = await FileTable.findFileTableByFileId(fileId);
    if (targetFileTable == null) {
      MyIndexDB.addFileData({
        fileId,
        pic: blobFile,
        raw: jsonObj,
        createTime,
        appName,
        packageName,
        deviceName,
      });
      added.value = true;
    }
  };

  return { pic, raw, added, extractZip };
}
