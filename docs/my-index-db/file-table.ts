import { MyIndexDB } from ".";

export const FILE_TABLE_NAME = "FileTable";

export class FileTable {
  static async findFileTableByFileId(fileId: string) {
    const db = await MyIndexDB.getMyIndexDB();
    return db.get(FILE_TABLE_NAME, fileId);
  }

  static async findAllFileTable() {
    const db = await MyIndexDB.getMyIndexDB();
    return db.getAll(FILE_TABLE_NAME);
  }
}
