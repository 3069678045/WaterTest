package top.lhx.myapp.utils;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import top.lhx.myapp.service.MyAccessibilityService;

import java.util.List;

/**
 * NodeUtils class
 *
 * @author auser
 * @date 11/2/2019
 */
public class NodeUtils {
    /**
     * find node by classname
     *
     * @param nodeInfo  AccessibilityNodeInfo
     * @param className classname
     * @return node
     */
    public static AccessibilityNodeInfo findNodeInfosByClassName(AccessibilityNodeInfo nodeInfo, String className) {
        if (nodeInfo == null) {
            nodeInfo = MyAccessibilityService.getInstance().getRootInActiveWindow();
            if (nodeInfo == null) {
                return null;
            }
        }
        int size = nodeInfo.getChildCount();
        for (int i = 0; i < size; i++) {
            AccessibilityNodeInfo childNode = nodeInfo.getChild(i);
            if (childNode != null) {
                String childClassName = (childNode.getClassName() != null) ? childNode.getClassName().toString() : "";
                if (childClassName.equals(className)) {
                    return childNode;
                }
                AccessibilityNodeInfo grandNode = findNodeInfosByClassName(childNode, className);
                if (grandNode != null) {
                    return grandNode;
                }
                recycel(childNode);
            }
        }
        return null;
    }

    /**
     * 查找到含应用字段的Node列表后，根据相应字段作排除，只适应节点字段相等的匹配情况。
     */
    public static AccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo root, String text) {
        if (root == null) {
            root = MyAccessibilityService.getInstance().getRootInActiveWindow();
            if (root == null) {
                return null;
            }
        }
        List<AccessibilityNodeInfo> list = root.findAccessibilityNodeInfosByText(text);
        if (list == null || list.isEmpty()) {
            return null;
        }
        AccessibilityNodeInfo resultNode = null;
        String nodeText;
        String nodeDesc;
        for (AccessibilityNodeInfo node : list) {
            Log.e("aaa","util的数据1"+node.getText());
            Log.e("aaa","util的数据2"+node.getContentDescription());
            nodeText = node.getText().toString();
            //nodeDesc = node.getContentDescription().toString();
            /*nodeText = ObjectUtils.objStr(node.getText());
            nodeDesc = ObjectUtils.objStr(node.getContentDescription());*/
            if (nodeText == null) {
                continue;
            }
            if (nodeText != null && (nodeText.equals("[" + text + "]") || nodeText.equals(text))) {
                resultNode = node;
                break;
            }
            /*if (nodeDesc != null && nodeDesc.equals(text)) {
                resultNode = node;
                break;
            }*/
        }
        for (AccessibilityNodeInfo node : list) {
            if (!node.equals(resultNode)) {
                recycel(node);
            }
        }
        return resultNode;
    }

    /**
     * find the parent node that can be clicked
     */
    public static AccessibilityNodeInfo findNodeByParentClick(AccessibilityNodeInfo root, String text) {
        AccessibilityNodeInfo node = findNodeInfosByText(root, text);
        if (node == null) {
            return null;
        }
        AccessibilityNodeInfo parent = node;
        while (parent != null && !parent.isClickable()) {
            parent = parent.getParent();
        }

        if (parent != null && parent.isClickable()) {
            return parent;
        }
        return null;
    }

    private static void recycel(AccessibilityNodeInfo node) {
        try {
            if (node != null) {
                node.recycle();
            }
        } catch (IllegalStateException e) {
            Log.e("e", e.getMessage());
        }
    }

    public static AccessibilityNodeInfo findNodeByIdClick(AccessibilityNodeInfo root, String id) {
        if (root == null) {
            root = MyAccessibilityService.getInstance().getRootInActiveWindow();
            if (root == null) {
                return null;
            }
        }
        List<AccessibilityNodeInfo> list = root.findAccessibilityNodeInfosByViewId(id);
        for (AccessibilityNodeInfo item : list) {
            item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        return null;
    }
}
