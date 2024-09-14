import { MyIndexDB } from ".";

export const FILE_ITEM_NAME = "FileItem";

export class FileItem {
  static async findFileItemByFileId(fileId: string) {
    const db = await MyIndexDB.getMyIndexDB();
    return db.get(FILE_ITEM_NAME, fileId);
  }
}
