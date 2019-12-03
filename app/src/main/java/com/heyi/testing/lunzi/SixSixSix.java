package com.heyi.testing.lunzi;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.heyi.testing.service.MyAccessibilityService;
import com.heyi.testing.utils.OkhtttpUtils;
import com.heyi.testing.utils.WechatUtils;

import java.util.ArrayList;
import java.util.List;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;

/**
 * SixSixSix class
 *
 * @author auser
 * @date 11/2/2019
 */
public class SixSixSix {
    private String keyValue;
    private String message;
    private List<String> allNameList = new ArrayList<>();
    private boolean linkScroll = true;
    private String linkmanname;

    public void Six(String v, AccessibilityEvent event) {
        String[] value = keyValue.split("!==!");
        MyAccessibilityService myAccessibilityService = MyAccessibilityService.getInstance();
        try {
            //进入联系人
            List<AccessibilityNodeInfo> Enter_found = MyAccessibilityService.getInstance().PengYouQuan("com.tencent.mm:id/bq", "com.tencent.mm:id/dct", "com.tencent.mm:id/sg");
            WechatUtils.performClick(Enter_found.get(1));
            Thread.sleep(2000);
            while (linkScroll) {
                //com.tencent.mm:id/og
                AccessibilityNodeInfo rootNodee = myAccessibilityService.getRootInActiveWindow();
                List<AccessibilityNodeInfo> linkman = rootNodee.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ok");

                for (int i = 0; i < linkman.size(); i++) {
                    Thread.sleep(2000);
                    linkmanname = linkman.get(i).getText().toString();
                    WechatUtils.performClick(linkman.get(i));
                    Thread.sleep(2000);
                    //com.tencent.mm:id/b7l
                    AccessibilityNodeInfo wxnameInfo = myAccessibilityService.getRootInActiveWindow();
                    List<AccessibilityNodeInfo> wxnamelist = wxnameInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/b7l");
                    if (wxnamelist.size() == 0) {
                        myAccessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);
                        continue;
                    }

                    String wxname = wxnamelist.get(0).getText().toString();
                    Log.e("aaa", "微信号=" + wxname);
                    OkhtttpUtils.getInstance().doGet("YM/Self/insertFriend?wxName=" + linkmanname + "&wxHao=" + wxname + "&wxId=" + "" + "&uid=" + "" + "", new OkhtttpUtils.OkCallback() {
                        @Override
                        public void onFailure(Exception e) {
                            System.out.println(e);
                        }

                        @Override
                        public void onResponse(String json) {
                            System.out.println(json);
                        }
                    });

                    Thread.sleep(2000);
                    myAccessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);

                }
                Thread.sleep(2000);
                AccessibilityNodeInfo visiblelinkman = myAccessibilityService.getRootInActiveWindow();
                List<AccessibilityNodeInfo> linkmenlist = visiblelinkman.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/b3a");
                Log.e("aaa", "list循环退傲剑===" + linkmenlist);
                linkScroll = linkmenlist.size() == 0;

                Thread.sleep(2000);
                AccessibilityNodeInfo rootNode = myAccessibilityService.getRootInActiveWindow();
                List<AccessibilityNodeInfo> listview = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/nm");
                listview.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                Thread.sleep(2000);
            }
            //回执修改
            OkhtttpUtils.getInstance().doGet("YM/UpTaskSch/StatusAndCompletion?TaskId=" + value[value.length - 1], new OkhtttpUtils.OkCallback() {
                @Override
                public void onFailure(Exception e) {
                    System.out.println(e);
                }

                @Override
                public void onResponse(String json) {
                    System.out.println(json);
                }
            });
            message = "000000000000000000";
        } catch (Exception e) {
            Log.e("aaa","666报错"+e);
            e.printStackTrace();
        }
    }
}
