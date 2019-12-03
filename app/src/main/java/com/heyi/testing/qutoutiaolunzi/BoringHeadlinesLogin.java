package com.heyi.testing.qutoutiaolunzi;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import com.heyi.testing.service.MyAccessibilityService;
import com.heyi.testing.utils.WechatUtils;

public class BoringHeadlinesLogin {
    public static void quTouTiao(){

        Log.e("aaa","准备进入趣头条");
        MyAccessibilityService myAccessibilityService = MyAccessibilityService.getInstance();
        try {

            Thread.sleep(1000);
            //点击我的
            AccessibilityNodeInfo rootNode = myAccessibilityService.getRootInActiveWindow();
            List<AccessibilityNodeInfo> listview = rootNode.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/am3");
            WechatUtils.performClick(listview.get(4));

            Thread.sleep(1000);
            //点击微信一键登陆
            WechatUtils.findViewIdAndClick(myAccessibilityService, "com.jifen.qukan:id/bo0");

            Thread.sleep(1000);

            try {
                Thread.sleep(1000);
                WechatUtils.findViewIdAndClick(myAccessibilityService, "com.jifen.qukan:id/ag1");
            }catch (Exception e){
                Log.e("aaa","未出现我先逛逛按钮");
            }

        }catch (Exception e){

            Log.e("aaa","已经登陆");

        }

    }
}
