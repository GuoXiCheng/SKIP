export interface Tree {
  label: string;
  children?: Tree[];
}

export interface AccessibilityWindow {
  fileId: string;
  appName: string;
  activityName: string;
  packageName: string;
  screenHeight: number;
  screenWidth: number;
  createTime: number;
  deviceName: string;
  nodes: AccessibilityNode[];
}

export interface AccessibilityNode {
  childCount: number;
  className: string;
  depth: number;
  nodeId: number;
  parentId: number;
  left: number;
  top: number;
  right: number;
  bottom: number;
  isClickable: boolean;
  text?: string;
  viewIdResourceName?: string;
  [key: string]: any;
}

export interface AccessibilityNodeTree {
  label: string;
  children: AccessibilityNodeTree[];
  text?: string;
  className?: string;
  viewIdResourceName?: string;
  childCount: number;
}

export interface FileTableData {
  fileId: string;
  createTime: number;
  appName: string;
  packageName: string;
  deviceName: string;
}

export interface FileItemData {
  fileId: string;
  pic: Blob;
  raw: AccessibilityWindow;
}

export type FileData = FileTableData & FileItemData;
