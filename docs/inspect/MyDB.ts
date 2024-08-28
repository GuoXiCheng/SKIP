import { openDB, DBSchema, IDBPDatabase } from "idb";
import { AccessibilityWindow } from "./types";

interface NodeTableField {
  fileId: number;
  appName: string;
  packageName: string;
  activityName: string;
  raw: AccessibilityWindow;
  pic: Blob;
}

interface MyDBSchema extends DBSchema {
  NodeTableSchema: {
    key: number;
    value: NodeTableField;
  };
}

const STORE_NAME = "NodeTableSchema";

export class NodeDB {
  private static db: IDBPDatabase<MyDBSchema>;

  private static async getTable(): Promise<IDBPDatabase<MyDBSchema>> {
    if (NodeDB.db) return NodeDB.db;

    NodeDB.db = await openDB<MyDBSchema>("MyDB", 1, {
      upgrade(db) {
        if (!db.objectStoreNames.contains(STORE_NAME)) {
          db.createObjectStore(STORE_NAME, { keyPath: "fileId" });
        }
      },
    });

    return NodeDB.db;
  }

  static async addNodeInfo(data: NodeTableField) {
    const db = await NodeDB.getTable();
    return db.add(STORE_NAME, data);
  }

  static async getAllNodeInfo(): Promise<NodeTableField[]> {
    const db = await NodeDB.getTable();
    return db.getAll(STORE_NAME);
  }
}
