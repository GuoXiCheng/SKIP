import { DBSchema, IDBPDatabase, openDB } from "idb";
import { FileData, FileItemData, FileTableData } from "../inspect/types";
import { FILE_TABLE_NAME } from "./file-table";
import { FILE_ITEM_NAME } from "./file-item";

interface MyIndexDBSchema extends DBSchema {
  FileTable: {
    key: string;
    value: FileTableData;
  };
  FileItem: {
    key: string;
    value: FileItemData;
  };
}
export class MyIndexDB {
  private static db: IDBPDatabase<MyIndexDBSchema>;

  static async getMyIndexDB(): Promise<IDBPDatabase<MyIndexDBSchema>> {
    if (MyIndexDB.db) return MyIndexDB.db;

    MyIndexDB.db = await openDB<MyIndexDBSchema>("MyIndexDB", 1, {
      upgrade(db) {
        if (!db.objectStoreNames.contains(FILE_TABLE_NAME)) {
          db.createObjectStore(FILE_TABLE_NAME, { keyPath: "fileId" });
        }
        if (!db.objectStoreNames.contains(FILE_ITEM_NAME)) {
          db.createObjectStore(FILE_ITEM_NAME, { keyPath: "fileId" });
        }
      },
    });

    return MyIndexDB.db;
  }

  static async addFileData(fileData: FileData) {
    const db = await MyIndexDB.getMyIndexDB();
    const tx = db.transaction([FILE_TABLE_NAME, FILE_ITEM_NAME], "readwrite");
    const fileTableStore = tx.objectStore(FILE_TABLE_NAME);
    const fileItemStore = tx.objectStore(FILE_ITEM_NAME);
    fileTableStore.add({
      fileId: fileData.fileId,
      createTime: fileData.createTime,
      appName: fileData.appName,
      packageName: fileData.packageName,
      deviceName: fileData.deviceName,
    });
    fileItemStore.add({ fileId: fileData.fileId, pic: fileData.pic, raw: fileData.raw });
    tx.commit();
  }

  static async deleteFileData(fileId: string) {
    const db = await MyIndexDB.getMyIndexDB();
    const tx = db.transaction([FILE_TABLE_NAME, FILE_ITEM_NAME], "readwrite");
    const fileTableStore = tx.objectStore(FILE_TABLE_NAME);
    const fileItemStore = tx.objectStore(FILE_ITEM_NAME);
    fileTableStore.delete(fileId);
    fileItemStore.delete(fileId);
    tx.commit();
  }
}
