package com.heyi.testing.lunzi;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.heyi.testing.app.AppLication;
import com.heyi.testing.greendao.UserDao;
import com.heyi.testing.greendao.UserDaoDao;
import com.heyi.testing.service.MyAccessibilityService;
import com.heyi.testing.utils.OkhtttpUtils;
import com.heyi.testing.utils.SpUtil;
import com.heyi.testing.utils.WechatUtils;

import java.util.List;

/**
 * CheckGroupMembers class
 *
 * @author auser
 * @date 11/2/2019
 */
public class CheckGroupMembers {
    //自检群成员
    public void SelfChecking(boolean linkScroll, String keyValue) {
        try {
            MyAccessibilityService myAccessibilityService = new MyAccessibilityService();
            String[] value = keyValue.split("!==!");
            SpUtil.put("qunId", value[3]);
            SpUtil.put("bianHao", value[4]);
            //进入联系人
            List<AccessibilityNodeInfo> Enter_found = myAccessibilityService.PengYouQuan("com.tencent.mm:id/bq", "com.tencent.mm:id/dct", "com.tencent.mm:id/sg");
            WechatUtils.performClick(Enter_found.get(1));
            Thread.sleep(2000);
            while (linkScroll) {
                //com.tencent.mm:id/og
                //通讯录
                AccessibilityNodeInfo rootNodee = myAccessibilityService.getRootInActiveWindow();
                List<AccessibilityNodeInfo> linkman = rootNodee.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/dcr");
                WechatUtils.performClick(linkman.get(1));
                Thread.sleep(2000);
                //群聊
                AccessibilityNodeInfo MailListNodee = myAccessibilityService.getRootInActiveWindow();
                List<AccessibilityNodeInfo> MailList = MailListNodee.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/nw");
                WechatUtils.performClick(MailList.get(0));
                Thread.sleep(2000);
                //搜索
                AccessibilityNodeInfo qunliaoListNodee = myAccessibilityService.getRootInActiveWindow();
                List<AccessibilityNodeInfo> qunliaoList = qunliaoListNodee.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/c2");
                WechatUtils.performClick(qunliaoList.get(0));
                Thread.sleep(2000);
                //输入群名称
                WechatUtils.findViewByIdAndPasteContent(myAccessibilityService, "com.tencent.mm:id/lh", value[3]);
                Thread.sleep(2000);
                //选择搜索结果的第一个选项
                AccessibilityNodeInfo qunNodee = myAccessibilityService.getRootInActiveWindow();
                List<AccessibilityNodeInfo> qunList = qunNodee.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/o3");
                WechatUtils.performClick(qunList.get(0));
                Thread.sleep(2000);
                //···
                WechatUtils.findViewIdAndClick(myAccessibilityService, "com.tencent.mm:id/kz");
                Thread.sleep(2000);
                //
                for (int i = 0; i < 3; i++) {
                    try {
                        AccessibilityNodeInfo memberListNodee = myAccessibilityService.getRootInActiveWindow();
                        List<AccessibilityNodeInfo> memberList = memberListNodee.findAccessibilityNodeInfosByViewId("android:id/title");
                        Thread.sleep(2000);
                        if (memberList.get(0).getText().equals("查看全部群成员")) {
                            WechatUtils.performClick(memberList.get(0));
                            Thread.sleep(2000);
                            AccessibilityNodeInfo slideDownNodee = myAccessibilityService.getRootInActiveWindow();
                            List<AccessibilityNodeInfo> slideDownList = slideDownNodee.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eco");
                            slideDownList.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);//向下滑
                            Thread.sleep(2000);
                            AccessibilityNodeInfo headerNodee = myAccessibilityService.getRootInActiveWindow();
                            List<AccessibilityNodeInfo> headerList = headerNodee.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ax0");
                            Boolean yz = myAccessibilityService.xunhuanliebiao(headerList);

                        } else {
                            AccessibilityNodeInfo wipeUpNodee = myAccessibilityService.getRootInActiveWindow();
                            List<AccessibilityNodeInfo> wipeUpList = wipeUpNodee.findAccessibilityNodeInfosByViewId("android:id/list");
                            wipeUpList.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                            Thread.sleep(2000);
                            AccessibilityNodeInfo wxNameNodee = myAccessibilityService.getRootInActiveWindow();
                            List<AccessibilityNodeInfo> wxNameList = wxNameNodee.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/e9y");
                            myAccessibilityService.xunhuanpost(wxNameList);
                        }
                        //上传数据
                        OkhtttpUtils.getInstance().doGet("YM/UpTaskSch/StatusAndCompletion?TaskId=" + value[value.length - 1], new OkhtttpUtils.OkCallback() {
                            @Override
                            public void onFailure(Exception e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String json) {
                                System.out.println(json);
                            }
                        });
                    } catch (Exception e) {
                        AccessibilityNodeInfo slideDownNodee = myAccessibilityService.getRootInActiveWindow();
                        List<AccessibilityNodeInfo> slideDownList = slideDownNodee.findAccessibilityNodeInfosByViewId("android:id/list");
                        slideDownList.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                        Thread.sleep(2000);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Log.e("aaa", "自检群成员-删除数据");
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos.size() > 0) {
                Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                userDaoDao.delete(userDaos.get(0));
            }
        }
    }
}
