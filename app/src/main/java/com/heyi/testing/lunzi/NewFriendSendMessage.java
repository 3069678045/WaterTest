package com.heyi.testing.lunzi;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.heyi.testing.app.AppLication;
import com.heyi.testing.greendao.UserDao;
import com.heyi.testing.greendao.UserDaoDao;
import com.heyi.testing.service.MyAccessibilityService;
import com.heyi.testing.utils.OkhtttpUtils;
import com.heyi.testing.utils.WechatUtils;

import java.util.List;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;

/**
 * NewFriendSendMessage class
 *
 * @author auser
 * @date 11/2/2019
 */
public class NewFriendSendMessage {
    public String send(String keyValue){
        String[] value = keyValue.split("!==!");
        //点击通讯录
        String message = "0000000000000000000000000";
        try {
            MyAccessibilityService myAccessibilityService = MyAccessibilityService.getInstance();
            List<AccessibilityNodeInfo> Enter_found = myAccessibilityService.PengYouQuan("com.tencent.mm:id/bq", "com.tencent.mm:id/dct", "com.tencent.mm:id/sg");
            WechatUtils.performClick(Enter_found.get(1));

            Thread.sleep(1000);//进入新的朋友
            List<AccessibilityNodeInfo> Enter_PYQ = myAccessibilityService.PengYouQuan("com.tencent.mm:id/nm", "com.tencent.mm:id/k6", "com.tencent.mm:id/bxk");
            WechatUtils.performClick(Enter_PYQ.get(0));
            Log.e("nnnnn-size===", "" + Enter_PYQ.size());
            Thread.sleep(2000);

            //List<AccessibilityNodeInfo> Enter_new1 = PengYouQuan("com.tencent.mm:id/by5", "com.tencent.mm:id/by1", "com.tencent.mm:id/bxu");
            //WechatUtils.performClick(addFriends.get(0));
            int i = 0;
            while (i < 5) {
                List<AccessibilityNodeInfo> Enter_new = myAccessibilityService.PengYouQuan("com.tencent.mm:id/by5", "com.tencent.mm:id/bxx", "com.tencent.mm:id/bxu");
                Thread.sleep(2000);
                for (int iii = 0; iii < Enter_new.size(); iii++) {
                    //com.tencent.mm:id/by1
                    Thread.sleep(2000);
                    Log.e("aaaaaa" + iii, String.valueOf(Enter_new.get(iii).getText()));
                    WechatUtils.performClick(Enter_new.get(iii));
                    Thread.sleep(2000);
                    AccessibilityNodeInfo rootNode = myAccessibilityService.getRootInActiveWindow();
                    List<AccessibilityNodeInfo> listview = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cv");
                    if (listview.get(0).getText().equals("发消息")) {
                        myAccessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);
                        Thread.sleep(2000);
                    } else {
                        Thread.sleep(2000);
                        AccessibilityNodeInfo rootNode0 = myAccessibilityService.getRootInActiveWindow();
                        List<AccessibilityNodeInfo> listName = rootNode0.findAccessibilityNodeInfosByViewId("android:id/summary");
                        if (listName.size() > 1) {
                            Log.e("aaaa", String.valueOf(listName.get(1).getText()));
                            boolean whetherThereAre = String.valueOf(listName.get(1).getText()).contains("名片添加");
                            if (whetherThereAre) {
                                String[] brr = String.valueOf(listName.get(0).getText()).split("”");
                                String quotesbefore = brr[0];
                                String[] bre = quotesbefore.split("“");
                                String quotesafter = bre[1];
                                Log.e("aaa", "名片添加" + quotesafter);
                            } else {
                                Log.e("aaa", "不存在名片添加");
                            }
                        } else {
                            Log.e("aaa", "这是listName.size() < 1");
                            boolean whetherThereAre = String.valueOf(listName.get(0).getText()).contains("名片添加");
                            if (whetherThereAre) {
                                String[] brr = String.valueOf(listName.get(0).getText()).split("”");
                                String quotesbefore = brr[0];
                                String[] bre = quotesbefore.split("“");
                                String quotesafter = bre[1];
                                Log.e("aaa", "名片添加" + quotesafter);
                                OkhtttpUtils.getInstance().doGet("YM/Recommender/Add/Recommender?recommenderName=" + quotesafter + "&udid=" + value[0] + "&uid=" + value[3] + "&friendName=" + Enter_new.get(iii).getText(), new OkhtttpUtils.OkCallback() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onResponse(String json) {
                                        System.out.println(json);
                                    }
                                });
                            } else {
                                Log.e("aaa", "不存在名片添加");
                            }
                        }

                        Thread.sleep(2000);
                        myAccessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);
                    }
                }
                i++;
                Log.e("aaa", "翻页次数" + i);
                Thread.sleep(1000);
                AccessibilityNodeInfo rootNode = myAccessibilityService.getRootInActiveWindow();
                List<AccessibilityNodeInfo> listview = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/by5");
                listview.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                Thread.sleep(2000);
            }
            //回执修改
            OkhtttpUtils.getInstance().doGet("YM/UpTaskSch/StatusAndCompletion?TaskId="+value[value.length-1], new OkhtttpUtils.OkCallback() {
                @Override
                public void onFailure(Exception e) {
                    System.out.println(e);
                }
                @Override
                public void onResponse(String json) {
                    System.out.println(json);
                }
            });
        } catch (Exception e) {
            Log.e("aaa","新好友发消息报错了"+e);
            message = "0000000000000000000000000";
            e.printStackTrace();
        }finally {
            Log.e("aaa", "自检-删除数据");
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos.size() > 0) {
                Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                userDaoDao.delete(userDaos.get(0));
            }
        }

        message = "0000000000000000000000000";
        return message;
    }
}
