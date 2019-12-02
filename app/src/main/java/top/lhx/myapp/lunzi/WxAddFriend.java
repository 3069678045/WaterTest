package top.lhx.myapp.lunzi;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import top.lhx.myapp.app.AppLication;
import top.lhx.myapp.greendao.UserDao;
import top.lhx.myapp.greendao.UserDaoDao;
import top.lhx.myapp.service.MyAccessibilityService;
import top.lhx.myapp.utils.OkhtttpUtils;
import top.lhx.myapp.utils.WechatUtils;

import java.util.List;

/**
 * WxAddFriend class
 *
 * @author auser
 * @date 11/2/2019
 */
public class WxAddFriend {
    private static WxAddFriend instance;

    private WxAddFriend() {
    }

    public static WxAddFriend getInstance() {
        if (instance == null) {
            instance = new WxAddFriend();
        }
        return instance;
    }

    public void start(AccessibilityService context, String keyValue) {
        try {
            String[] value = keyValue.split("!==!");
            String qunNames = value[3];
            String[] name = value[4].split("!");
            Log.e("aaa", "添加好友的群名称：" + qunNames);
            Thread.sleep(1000);
            WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/c2");
            Thread.sleep(1000);
            //com.tencent.mm:id/lh
            WechatUtils.findViewByIdAndPasteContent(context, "com.tencent.mm:id/lh", qunNames);
            Thread.sleep(1000);
            //com.tencent.mm:id/r_
            AccessibilityNodeInfo itemInfo = MyAccessibilityService.getInstance().TraversalAndFindContacts("com.tencent.mm:id/c12", "com.tencent.mm:id/rc", "com.tencent.mm:id/r_", qunNames);
            WechatUtils.performClick(itemInfo);
            Thread.sleep(2500);
            //com.tencent.mm:id/kz
            WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/kz");
            Thread.sleep(1000);
            for (int i = 0; i < 5; i++) {
                AccessibilityNodeInfo rootNode = context.getRootInActiveWindow();
                List<AccessibilityNodeInfo> listview = rootNode.findAccessibilityNodeInfosByViewId("android:id/list");
                listview.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                //android:id/title
                Thread.sleep(2000);
                List<AccessibilityNodeInfo> listviewa = rootNode.findAccessibilityNodeInfosByViewId("android:id/title");
                if (listviewa.get(0).getText().equals("查看全部群成员")) {
                    WechatUtils.findViewIdAndClick(context, "android:id/title");
                    Thread.sleep(1000);
                    for (String s : name) {
                        //com.tencent.mm:id/bb4
                        Log.e("aaa", "添加的好友名称:" + s);
                        WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/bb4");
                        Thread.sleep(1000);
                        WechatUtils.findViewByIdAndPasteContent(context, "com.tencent.mm:id/bb4", s);
                        Thread.sleep(1000);
                        //com.tencent.mm:id/ax0
                        AccessibilityNodeInfo names = MyAccessibilityService.getInstance().TraversalAndFindContacts("com.tencent.mm:id/f20", "com.tencent.mm:id/ax0", "com.tencent.mm:id/awy", s);
                        WechatUtils.performClick(names);
                        Thread.sleep(1000);
                        AccessibilityNodeInfo rootNode1 = context.getRootInActiveWindow();
                        List<AccessibilityNodeInfo> listview1 = rootNode1.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cv");
                        Thread.sleep(2000);
                        Log.e("aaa", "查看此人是否存在发消息:" + listview1.get(0).getText());
                        if (listview1.get(0).getText().equals("发消息")) {
                            context.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                            Thread.sleep(2000);
                            WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/bb4");
                            Thread.sleep(1000);
                            WechatUtils.findViewByIdAndPasteContent(context, "com.tencent.mm:id/bb4", "");
                        } else {
                            //com.tencent.mm:id/cv
                            Thread.sleep(1000);
                            WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/cv");
                            //com.tencent.mm:id/e_5
                            Thread.sleep(1000);
                            WechatUtils.findViewByIdAndPasteContent(context, "com.tencent.mm:id/e_5", "");
                            Thread.sleep(1000);
                            WechatUtils.findViewByIdAndPasteContent(context, "com.tencent.mm:id/e_5", value[5]);
                            Thread.sleep(1000);
                            //com.tencent.mm:id/dcf
                            AccessibilityNodeInfo add = context.getRootInActiveWindow();
                            try {
                                List<AccessibilityNodeInfo> listviewadd = add.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/dcf");
                                Thread.sleep(1000);
                                if (listviewadd.get(0).getText().equals("由于对方的隐私设置，你无法通过群聊将其添加至通讯录。")) {
                                    Thread.sleep(1000);
                                    context.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                                    Thread.sleep(1000);
                                    context.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                                    Thread.sleep(2666);
                                    WechatUtils.findViewByIdAndPasteContent(context, "com.tencent.mm:id/bb4", "");
                                    continue;
                                }
                            } catch (Exception ignored) {

                            }
                            //com.tencent.mm:id/ky
                            WechatUtils.findViewIdAndClick(context, "com.tencent.mm:id/ky");
                            Thread.sleep(2666);
                            context.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                            Thread.sleep(2666);
                            WechatUtils.findViewByIdAndPasteContent(context, "com.tencent.mm:id/bb4", "");

                        }
                    }
                    break;
                }

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
        } catch (Exception e) {
            Log.e("aaa","微信加好友报错"+e);
        }finally {
            Log.e("aaa", "微信加好友-删除数据");
            UserDaoDao userDaoDao = AppLication.getInstance().getDaoSession().getUserDaoDao();
            List<UserDao> userDaos = userDaoDao.queryBuilder().orderAsc(UserDaoDao.Properties.Timestamp).limit(1).list();
            if (userDaos.size() > 0) {
                Log.e("aaa", "删除了" + userDaos.get(0).getMessage());
                userDaoDao.delete(userDaos.get(0));
            }
        }
    }
}
