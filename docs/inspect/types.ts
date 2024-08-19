export interface Tree {
  label: string;
  children?: Tree[];
}

export interface AccessibilityWindow {
  className: string;
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
