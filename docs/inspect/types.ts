export interface Tree {
  label: string;
  children?: Tree[];
}

export interface AccessibilityWindow {
  fileId: number;
  appName: string;
  activityName: string;
  packageName: string;
  screenHeight: number;
  screenWidth: number;
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
  fileId: number;
  createTime: string;
  appName: string;
  packageName: string;
  activityName: string;
}
