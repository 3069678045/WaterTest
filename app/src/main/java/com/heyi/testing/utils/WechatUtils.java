package com.heyi.testing.utils;


import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * WechatUtils class
 *
 * @author auser
 * @date 11/2/2019
 */
public class WechatUtils {

    public static String NAME = "柚屿";
    public static String CONTENT = "啦啦啦😁";


    /**
     * 在当前页面查找文字内容并点击
     *
     * @param text
     */
    public static void findTextAndClick(AccessibilityService accessibilityService, String text) {

        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }

        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null && (text.equals(nodeInfo.getText()) || text.equals(nodeInfo.getContentDescription()))) {
                    performClick(nodeInfo);
                    break;
                }
            }
        }
    }


    /**
     * 检查viewId进行点击
     *
     * @param accessibilityService
     * @param id
     */
    public static void findViewIdAndClick(AccessibilityService accessibilityService, String id) {

        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }

        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    performClick(nodeInfo);
                    break;
                }
            }
        }
    }


    /**
     * 检查viewId长按点击
     *
     * @param accessibilityService
     * @param id
     */
    public static void findViewIdAndLongClick(AccessibilityService accessibilityService, String id) {

        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }

        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    performLongClick(nodeInfo);
                    break;
                }
            }
        }
    }


    public static boolean findViewByIdAndPasteContent(AccessibilityService accessibilityService, String id, String content) {
        AccessibilityNodeInfo rootNode = accessibilityService.getRootInActiveWindow();
        if (rootNode != null) {
            List<AccessibilityNodeInfo> editInfo = rootNode.findAccessibilityNodeInfosByViewId(id);
            if (editInfo != null && !editInfo.isEmpty()) {
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, content);
                editInfo.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                return true;
            }
            return false;
        }
        return false;
    }

    public static String findTextById(AccessibilityService accessibilityService, String id) {
        AccessibilityNodeInfo rootInfo = accessibilityService.getRootInActiveWindow();
        if (rootInfo != null) {
            List<AccessibilityNodeInfo> userNames = rootInfo.findAccessibilityNodeInfosByViewId(id);
            if (userNames != null && userNames.size() > 0) {
                String name = userNames.get(0).getText().toString();
                return name;
            }
        }
        return null;
    }


    /**
     * 在当前页面查找对话框文字内容并点击
     *
     * @param text1 默认点击text1
     * @param text2
     */
    public static void findDialogAndClick(AccessibilityService accessibilityService, String text1, String text2) {

        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }

        List<AccessibilityNodeInfo> dialogWait = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text1);
        List<AccessibilityNodeInfo> dialogConfirm = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text2);
        if (!dialogWait.isEmpty() && !dialogConfirm.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : dialogWait) {
                if (nodeInfo != null && text1.equals(nodeInfo.getText())) {
                    performClick(nodeInfo);
                    break;
                }
            }
        }

    }

    //模拟点击事件
    public static void performClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.isClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            performClick(nodeInfo.getParent());

        }
        nodeInfo.recycle();
    }

    //模拟长按点击事件
    public static void performLongClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.isClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
        } else {
            performLongClick(nodeInfo.getParent());
        }
    }


    //模拟返回事件
    public static void performBack(AccessibilityService service) {
        if (service == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
    }
}